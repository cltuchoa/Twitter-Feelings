package com.uchoa.twitter.ui.fragments.list

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.uchoa.twitter.R
import com.uchoa.twitter.downloaders.DownloaderFactory
import com.uchoa.twitter.downloaders.Downloaders
import com.uchoa.twitter.model.Tweet
import com.uchoa.twitter.ui.fragments.list.listeners.TweetClickListener
import com.uchoa.twitter.utils.Constant
import com.uchoa.twitter.utils.TwitterAppHelper


class TwitterListAdapter(
    var tweetList: MutableList<Tweet>,
    var profileImageUrl: String,
    private val tweetClickListener: TweetClickListener
) : RecyclerView.Adapter<TwitterListAdapter.MovieViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tweet_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(tweetList[position])
    }

    override fun getItemCount(): Int {
        return tweetList.size
    }

    inner class MovieViewHolder(private val layout: View) : RecyclerView.ViewHolder(layout) {

        private var name: TextView = layout.findViewById(R.id.item_name)
        private var date: TextView = layout.findViewById(R.id.item_date)
        private var image: ImageView = layout.findViewById(R.id.item_image)

        fun bind(tweet: Tweet) {
            name.text = tweet.text
            date.text = tweet.date

            applySpanToText(name)

            DownloaderFactory.getDownloader(Downloaders.GLIDE).loadImage(image, profileImageUrl)
            val view: ConstraintLayout = itemView.findViewById(R.id.item_content)
            val anim = AnimationUtils.loadAnimation(itemView.context, R.anim.fade_in)
            anim.interpolator = DecelerateInterpolator()
            view.startAnimation(anim)

            layout.setOnClickListener {
                tweetClickListener.onClick(tweet)
            }
        }

        private fun applySpanToText(textView: TextView) {
            val text = textView.text.toString()
            textView.text = getSpannableString(text)
            textView.movementMethod = LinkMovementMethod.getInstance()
        }

        private fun getSpannableString(text: String): SpannableString {
            val spannableString = SpannableString(text)
            val links = TwitterAppHelper.getLinksFromTweet(text)

            links.forEach {
                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(textView: View) {
                        val uri = Uri.parse(it.url)
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = uri
                        try {
                            startActivity(context!!, intent, null)
                        } catch (e: ActivityNotFoundException) {
                            Log.e(Constant.APP_TAG, e.message)
                        }
                    }
                }
                spannableString.setSpan(
                    clickableSpan,
                    it.startIndex,
                    it.endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            return spannableString
        }
    }
}