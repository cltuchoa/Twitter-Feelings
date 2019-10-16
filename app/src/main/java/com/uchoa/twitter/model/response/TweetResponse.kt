package com.uchoa.twitter.model.response

import java.io.Serializable

data class TweetResponse(
    val id: String = "",
    val created_at: String = "",
    val text: String = ""
) : Serializable
