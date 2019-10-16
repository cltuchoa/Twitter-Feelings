package com.uchoa.twitter.model

class User(
    val id: String,
    val name: String,
    val imageProfileUrl: String,
    val imageBannerUrl: String,
    var tweetList: MutableList<Tweet>
)
