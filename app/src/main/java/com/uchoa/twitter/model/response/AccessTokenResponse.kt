package com.uchoa.twitter.model.response

import java.io.Serializable

data class AccessTokenResponse(
    var token_type: String? = "",
    var access_token: String? = ""
) : Serializable