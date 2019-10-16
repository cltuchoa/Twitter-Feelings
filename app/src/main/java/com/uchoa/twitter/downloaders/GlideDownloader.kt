package com.uchoa.twitter.downloaders

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.uchoa.twitter.R

class GlideDownloader private constructor() : ImageDownloader {

    override fun loadImage(imageView: ImageView, imagePath: String) {
        Log.d("ImageDownloader", "download image using Glide")
        Glide.with(imageView.context)
            .load(imagePath)
            .apply(RequestOptions.placeholderOf(R.color.black_transparent_20))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

    companion object {

        private var downloader: GlideDownloader? = null

        val instance: GlideDownloader
            get() {
                if (downloader == null) {
                    downloader = GlideDownloader()
                }
                return downloader as GlideDownloader
            }
    }
}