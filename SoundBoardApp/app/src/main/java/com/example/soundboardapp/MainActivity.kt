package com.example.soundboardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundboardapp.data.Datasource
import com.example.soundboardapp.model.Sounds
import com.example.soundboardapp.ui.theme.SoundBoardAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoundBoard()
        }
    }
}

@Composable
fun SoundBoard() {
    SoundBoardAppTheme {
        SoundsList(soundList = Datasource().loadSounds())
    }
}

@Composable
fun SoundsList(soundList: List<Sounds>, modifier: Modifier = Modifier){
    LazyColumn {
        items(soundList) { sounds ->
            SoundsCard(sounds)
        }
    }
}

@Composable
fun SoundsCard(sounds: Sounds, modifier: Modifier = Modifier){
    Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
        Column {
            Image(
                painter = painterResource(sounds.imageResourceId),
                contentDescription = stringResource(sounds.stringResourceId),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = LocalContext.current.getString(sounds.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SoundBoardAppTheme {
        SoundsCard (Sounds(R.string.android, R.drawable.image1,R.raw.android))
    }
}