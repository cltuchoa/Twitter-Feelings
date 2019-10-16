package com.uchoa.twitter.downloaders


class DownloaderFactory {

    companion object {
        fun getDownloader(type: Downloaders): ImageDownloader {
            return when (type) {
                Downloaders.GLIDE -> GlideDownloader.instance
            }
        }
    }
}