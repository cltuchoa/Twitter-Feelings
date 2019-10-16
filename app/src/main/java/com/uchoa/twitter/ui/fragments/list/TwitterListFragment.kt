package com.uchoa.twitter.ui.fragments.list

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.view.Gravity.NO_GRAVITY
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.TransitionInflater
import com.uchoa.sentimentanalizer.core.SentimentAnalyzer
import com.uchoa.sentimentanalizer.core.SentimentListener
import com.uchoa.sentimentanalizer.enums.Mood
import com.uchoa.sentimentanalizer.model.UserSentiment
import com.uchoa.twitter.R
import com.uchoa.twitter.downloaders.DownloaderFactory
import com.uchoa.twitter.downloaders.Downloaders
import com.uchoa.twitter.enums.RequestStatus
import com.uchoa.twitter.extension.gone
import com.uchoa.twitter.extension.show
import com.uchoa.twitter.model.Tweet
import com.uchoa.twitter.model.User
import com.uchoa.twitter.ui.fragments.TwitterBaseFragment
import com.uchoa.twitter.ui.fragments.list.listeners.TweetClickListener
import com.uchoa.twitter.utils.Constant
import com.uchoa.twitter.utils.TwitterAppHelper
import kotlinx.android.synthetic.main.list_tweet_fragment.*


class TwitterListFragment : TwitterBaseFragment(),
    TweetClickListener,
    SwipeRefreshLayout.OnRefreshListener, SentimentListener {

    private lateinit var manager: LinearLayoutManager
    private val sentimentAnalyzer = SentimentAnalyzer(Constant.GOOGLE_NATURAL_LANGUAGE_API_KEY)
    private var adapter: TwitterListAdapter? = null
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_tweet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupData()
        setupRecycleView()
        setupPullToRefresh()
        setupActionBar()
        setupBanner()
        showTweets()
    }

    private fun setupData() {
        userName = arguments?.getString(USER_NAME_BUNDLE) ?: ""
        viewModel = TwitterAppHelper.getViewModel(activity!!, service)
        user = viewModel.userData.value
    }

    private fun showTweets() {
        val tweetList = user?.tweetList ?: mutableListOf()
        val profileImageUrl = user?.imageProfileUrl ?: ""

        if (adapter == null) {
            adapter = TwitterListAdapter(
                tweetList,
                profileImageUrl,
                this
            )
            tweets_recycler_view.adapter = adapter
        } else {
            adapter?.tweetList = tweetList
            adapter?.profileImageUrl = profileImageUrl
            adapter?.notifyDataSetChanged()
        }
    }

    private fun setupRecycleView() {
        manager = LinearLayoutManager(context)
        tweets_recycler_view.layoutManager = manager
        tweets_recycler_view.addItemDecoration(
            DividerItemDecoration(tweets_recycler_view.context, DividerItemDecoration.HORIZONTAL)
        )
    }

    private fun setupPullToRefresh() {
        tweet_list_swipe_refresh.setOnRefreshListener(this)
        tweet_list_swipe_refresh.setColorSchemeResources(R.color.colorAccent)
    }

    private fun setupBanner() {
        val bannerUrl = viewModel.userData.value?.imageBannerUrl ?: ""
        if (bannerUrl.isNotEmpty()) {
            tweet_list_banner_image.show()
            DownloaderFactory.getDownloader(Downloaders.GLIDE)
                .loadImage(tweet_list_banner_image, bannerUrl)
        } else {
            tweet_list_banner_image.gone()
        }
    }

    private fun setupActionBar() {
        tweet_list_user_name.text = userName

        val appCompactActivity = (activity as AppCompatActivity)
        appCompactActivity.setSupportActionBar(tweet_list_toolbar)
        tweet_list_toolbar.setNavigationOnClickListener {
            appCompactActivity.onBackPressed()
        }

        if (appCompactActivity.supportActionBar != null) {
            appCompactActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onRequestStatusChange(status: RequestStatus?) {
        when (status) {
            RequestStatus.LOADING -> {
                tweet_list_swipe_refresh.isRefreshing = true
            }
            RequestStatus.ERROR -> {
                tweet_list_swipe_refresh.isRefreshing = false
                showMessage(getString(R.string.home_user_not_found))
            }
            RequestStatus.IDLE -> {
                tweet_list_swipe_refresh.isRefreshing = false
            }
            RequestStatus.DONE -> {
                tweet_list_swipe_refresh.isRefreshing = false
                showTweets()
            }
        }
    }

    private fun showPopupWindows(userSentiment: UserSentiment) {
        val layoutInflater = context?.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = layoutInflater.inflate(R.layout.mood_popup, null)

            val popupWindow = PopupWindow(
            popupView,
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.MATCH_PARENT
        )

        val imageMood = popupView.findViewById(R.id.mood_popup_image) as ImageView
        imageMood.setImageResource(
            when {
                userSentiment.mood == Mood.NEGATIVE -> R.drawable.pensive
                userSentiment.mood == Mood.NEUTRAL -> R.drawable.neutral_face
                else -> R.drawable.smile
            }
        )

        val btnClose = popupView.findViewById(R.id.mood_popup_close) as Button
        btnClose.setOnClickListener { popupWindow.dismiss() }

        val title = popupView.findViewById(R.id.mood_popup_name) as TextView
        title.text = getString(R.string.popup_mood_title).format(user?.name)


        val mCurrentX = 0
        val mCurrentY = 0

        popupWindow.showAtLocation(tweet_list_main, NO_GRAVITY, mCurrentX, mCurrentY)
    }

    override fun onClick(tweet: Tweet) {
        showProgressingBar(true)
        sentimentAnalyzer.analyze(tweet.text, this)
    }

    override fun onRefresh() {
        getDataFromUser()
    }

    override fun onAnalizerSuccess(userSentiment: UserSentiment) {
        showPopupWindows(userSentiment)
        showProgressingBar(false)
    }

    override fun onAnalizerFailed() {
        showProgressingBar(false)
    }
}