package com.uchoa.twitter.ui.fragments

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.uchoa.twitter.api.ServiceFactory
import com.uchoa.twitter.api.TwitterService
import com.uchoa.twitter.enums.RequestStatus
import com.uchoa.twitter.extension.gone
import com.uchoa.twitter.extension.show
import com.uchoa.twitter.ui.TwitterAppViewModel
import kotlinx.android.synthetic.main.twitter_progress_layout.*

abstract class TwitterBaseFragment : Fragment() {

    protected val service = ServiceFactory.getRetrofit().create(TwitterService::class.java)!!
    protected lateinit var viewModel: TwitterAppViewModel
    protected var userName = ""

    protected fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showProgressingBar(show: Boolean) {
        if (show) app_progress.show() else app_progress.gone()
    }

    protected fun getDataFromUser() {
        viewModel.statusData.removeObservers(this)
        viewModel.statusData.observe(this, Observer<RequestStatus> {
            onRequestStatusChange(it)
        })
        viewModel.getDataFromUser(userName)
    }

    abstract fun onRequestStatusChange(status: RequestStatus?)

    companion object {
        const val USER_NAME_BUNDLE = "user_name"
    }
}