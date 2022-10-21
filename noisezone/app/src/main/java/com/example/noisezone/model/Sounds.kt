package com.example.noisezone.model
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes

data class Sounds (
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int,
    @RawRes val soundResourceId: Int
    )
