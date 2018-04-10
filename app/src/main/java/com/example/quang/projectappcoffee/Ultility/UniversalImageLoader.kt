package com.example.quang.projectappcoffee.Ultility

import android.content.Context
import android.util.Log

import com.example.quang.projectappcoffee.R
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer


class UniversalImageLoader(private val mContext: Context) {

    val config: ImageLoaderConfiguration
        get() {
            Log.d(TAG, "getConfig: Returning image loader configuration")
            val defaultOptions = DisplayImageOptions.Builder()
                    .showImageOnLoading(defaultImage)
                    .showImageForEmptyUri(defaultImage)
                    .showImageOnFail(defaultImage)
                    .cacheOnDisk(true).cacheInMemory(true)
                    .cacheOnDisk(true).resetViewBeforeLoading(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(FadeInBitmapDisplayer(300)).build()


            return ImageLoaderConfiguration.Builder(
                    mContext)
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(WeakMemoryCache())
                    .diskCacheSize(100 * 1024 * 1024)
                    .build()
        }

    init {
        Log.d(TAG, "UniversalImageLoader: started")
    }

    companion object {
        private val TAG = "UniversalImageLoader"
        private val defaultImage = R.drawable.ic_android
    }


}
