package com.uchoa.twitter.api

import com.uchoa.twitter.utils.Constant
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceFactory {

    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constant.TWITTER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}