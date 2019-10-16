package com.uchoa.twitter.ui.fragments.home

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.uchoa.twitter.R
import com.uchoa.twitter.enums.RequestStatus
import com.uchoa.twitter.extension.gone
import com.uchoa.twitter.extension.hide
import com.uchoa.twitter.extension.show
import com.uchoa.twitter.ui.fragments.TwitterBaseFragment
import com.uchoa.twitter.utils.TwitterAppHelper
import kotlinx.android.synthetic.main.home_fragment.*


class TwitterHomeFragment : TwitterBaseFragment(), View.OnClickListener {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = TwitterAppHelper.getViewModel(activity!!, service)
        setupListeners()
    }

    private fun setupListeners() {
        home_btn_show.setOnClickListener(this)
        home_btn_clear.setOnClickListener(this)
        home_user_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                home_message_error.hide()
                if (s.isNotEmpty()) {
                    home_btn_clear.show()
                } else {
                    home_btn_clear.gone()
                }
            }
        })
        home_user_name.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    getTweets()
                    return true
                }
                return false
            }
        })
        home_user_name.requestFocus()
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.home_btn_show) {
            getTweets()
        } else if (view?.id == R.id.home_btn_clear) {
            home_user_name.setText("")
            home_user_name.requestFocus()

            val im = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.showSoftInput(home_user_name, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun getTweets() {
        userName = home_user_name.text.toString()
        if (userName.isNotEmpty()) {
            getDataFromUser()
        } else {
            showMessageError(R.string.home_empty_user_error)
        }
    }

    private fun showMessageError(@StringRes resId: Int) {
        home_message_error.setText(resId)
        home_message_error.show()
    }

    override fun onRequestStatusChange(status: RequestStatus?) {
        when (status) {
            RequestStatus.LOADING -> {
                home_btn_show.isEnabled = false
                showProgressingBar(true)
            }
            RequestStatus.DONE -> {
                showProgressingBar(false)
                val bundle = bundleOf(Pair(USER_NAME_BUNDLE, userName))
                val extras = FragmentNavigatorExtras(
                    home_icon to getString(R.string.nav_twitter_icon),
                    home_user_name to getString(R.string.nav_user_name)
                )
                navController.navigate(
                    R.id.action_twitterHomeFragment_to_twitterListFragment,
                    bundle,
                    null,
                    extras
                )
            }
            RequestStatus.ERROR -> {
                home_btn_show.isEnabled = true
                showMessageError(R.string.home_user_not_found)
            }
            RequestStatus.IDLE -> {
                showProgressingBar(false)
            }
        }
    }
}