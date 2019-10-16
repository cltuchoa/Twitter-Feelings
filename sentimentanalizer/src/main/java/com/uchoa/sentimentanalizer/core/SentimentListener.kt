package com.uchoa.sentimentanalizer.core

import com.uchoa.sentimentanalizer.model.UserSentiment

interface SentimentListener {

    fun onAnalizerSuccess(userSentiment: UserSentiment)

    fun onAnalizerFailed()

}