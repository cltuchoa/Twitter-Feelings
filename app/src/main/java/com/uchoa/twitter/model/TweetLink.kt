package com.uchoa.twitter.model

data class TweetLink(
    val text: String,
    val url: String,
    val startIndex: Int,
    val endIndex: Int
)