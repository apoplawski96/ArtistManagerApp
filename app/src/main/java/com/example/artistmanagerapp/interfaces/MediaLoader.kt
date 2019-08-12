package com.example.artistmanagerapp.interfaces

import android.graphics.Bitmap

interface MediaLoader {

    enum class MediaLoaderOptions{

    }

    fun loadImage(bitmap: Bitmap?, option : MediaLoaderOptions?)
}