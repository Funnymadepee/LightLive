package com.lzm.lightLive.model

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.lzm.lightLive.R

object ImageViewAdapter {
    @JvmStatic
    @BindingAdapter("android:src")
    fun setSrc(imageView: ImageView, bitmap: Bitmap?) {
        imageView.setImageBitmap(bitmap)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setSrc(imageView: ImageView, url: String?) {
        Glide.with(imageView.context).load(url)
            .placeholder(R.mipmap.ic_launcher)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }
}