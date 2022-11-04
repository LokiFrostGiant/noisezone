package com.example.noisezone.model

data class SoundsURL (
    val image: String = "",
    val name: String = "",
    val soundID: Int = 0
) {
    override fun toString(): String {
        return "image: $image name: $name soundID: $soundID"
    }

}