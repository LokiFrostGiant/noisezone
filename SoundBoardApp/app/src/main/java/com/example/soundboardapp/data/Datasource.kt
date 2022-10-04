package com.example.soundboardapp.data
import com.example.soundboardapp.R
import com.example.soundboardapp.model.Sounds

class Datasource() {
    fun loadSounds(): List<Sounds>{
        return listOf<Sounds>(
            Sounds(R.string.android,R.drawable.image1, R.raw.android),
            Sounds(R.string.vine_boom,R.drawable.image2, R.raw.vine_boom),
            Sounds(R.string.bruh,R.drawable.image3, R.raw.bruh),
            Sounds(R.string.ahhh,R.drawable.image4, R.raw.ahhh),
            Sounds(R.string.oof,R.drawable.image5, R.raw.oof)
        )
    }
}