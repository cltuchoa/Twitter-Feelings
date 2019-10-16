package com.uchoa.twitter.model.response

import java.io.Serializable

data class UserResponse(
    val id: String = "",
    val name: String = "",
    val screen_name: String = "",
    val url: String = "",
    val description: String = "",
    val friends_count: Int = 0,
    val statuses_count: Int = 0,
    val created_at: String = "",
    val profile_banner_url: String = "",
    val profile_image_url_https: String = ""
) : Serializable