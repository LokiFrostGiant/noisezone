package com.example.soundboardapp.data
import com.example.soundboardapp.R
import com.example.soundboardapp.model.Sounds

class Datasource() {
    fun loadSounds(): List<Sounds>{
        return listOf<Sounds>(
            Sounds(R.string.android,R.drawable.image1, R.raw.android)
        )
    }
}