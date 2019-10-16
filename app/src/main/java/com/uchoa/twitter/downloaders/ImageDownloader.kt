package com.uchoa.twitter.downloaders

import android.widget.ImageView

interface ImageDownloader {

    fun loadImage(imageView: ImageView, imagePath: String)

}