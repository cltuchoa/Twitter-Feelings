package com.uchoa.twitter.api

import com.uchoa.twitter.model.response.AccessTokenResponse
import com.uchoa.twitter.model.response.TweetResponse
import com.uchoa.twitter.model.response.UserResponse
import com.uchoa.twitter.utils.Constant
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TwitterService {

    object Model {
        const val countParam = "count"
        const val userNameParam = "screen_name"
        const val contentType = "ContentType"
        const val grantType = "grant_type"
        const val authorization = "Authorization"
    }

    @POST(Constant.TWITTER_TOKEN_URL)
    fun getAccessToken(
        @Header(Model.authorization) authorization: String,
        @Header(Model.contentType) contentType: String = "application/x-www-form-urlencoded;charset=UTF-8",
        @Query(Model.grantType) grant_type: String = "client_credentials"
    ): Observable<AccessTokenResponse>

    @GET(Constant.TWITTER_USER_URL)
    fun getUser(
        @Query(Model.userNameParam) screenName: String,
        @Header(Model.authorization) authorization: String,
        @Header(Model.contentType) contentType: String = "application/json"
    ): Observable<UserResponse>

    @GET(Constant.TWITTER_TWEETS_URL)
    fun getTweets(
        @Query(Model.userNameParam) screenName: String,
        @Header(Model.authorization) authorization: String,
        @Query(Model.countParam) count: Int = 40,
        @Header(Model.contentType) contentType: String = "application/json"
    ): Observable<MutableList<TweetResponse>>
}