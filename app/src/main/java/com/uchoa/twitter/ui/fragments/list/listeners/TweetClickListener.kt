package com.uchoa.twitter.ui.fragments.list.listeners

import com.uchoa.twitter.model.Tweet

interface TweetClickListener {
    fun onClick(tweet: Tweet)
}