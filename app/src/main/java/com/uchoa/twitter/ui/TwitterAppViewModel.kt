package com.uchoa.twitter.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uchoa.twitter.api.TwitterMapper
import com.uchoa.twitter.api.TwitterService
import com.uchoa.twitter.enums.RequestStatus
import com.uchoa.twitter.model.User
import com.uchoa.twitter.utils.TwitterAppHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers


class TwitterAppViewModel(
    private val service: TwitterService,
    private val authorization: String = TwitterAppHelper.getBasicTwitterAuthorization()
) : ViewModel() {

    private var userName = ""
    private var disposable: Disposable? = null
    private val mapper = TwitterMapper()

    var statusData = MutableLiveData<RequestStatus>()
    var userData = MutableLiveData<User>()

    fun getDataFromUser(userName: String = this.userName) {
        this.userName = userName
        disposable = service.getAccessToken(authorization)
            .map { accessTokenResponse ->
                accessTokenResponse.access_token
            }
            .flatMap { accessToken ->
                val token = "Bearer $accessToken"
                Observables.zip(
                    service.getUser(userName, token),
                    service.getTweets(userName, token)
                ) { userResponse, tweetResponse ->
                    mapper.convertResponseToUser(userResponse, tweetResponse)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { statusData.value = RequestStatus.LOADING }
            .doAfterTerminate { statusData.value = RequestStatus.IDLE }
            .subscribe(
                { user -> handleServiceSuccess(user) },
                { handleServiceError() },
                {
                    disposable?.dispose()
                })
    }

    private fun handleServiceError() {
        statusData.value = RequestStatus.ERROR
    }

    private fun handleServiceSuccess(user: User) {
        userData.value = user
        statusData.value = RequestStatus.DONE
    }
}