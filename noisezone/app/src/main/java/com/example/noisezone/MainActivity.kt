package com.example.noisezone

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noisezone.data.Datasource
import com.example.noisezone.model.Sounds
import com.example.noisezone.ui.theme.SoundBoardAppTheme
import android.content.Context
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoundBoard(this)
        }
    }
}

@Composable
fun SoundBoard(context: Context) {
    SoundBoardAppTheme {
        SoundsList(soundList = Datasource().loadSounds(), modifier = Modifier,context)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SoundsList(soundList: List<Sounds>, modifier: Modifier = Modifier, context: Context){
    LazyVerticalGrid (cells = GridCells.Adaptive(minSize = 128.dp)) {

        items(soundList) { sounds ->
            SoundsCard(sounds, modifier = Modifier, context)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SoundsCard(sounds: Sounds, modifier: Modifier = Modifier, context: Context){
    Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
        Column {
            Image(
                painter = painterResource(sounds.imageResourceId),
                contentDescription = stringResource(sounds.stringResourceId),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp)
                    .combinedClickable {
                        val mp = MediaPlayer.create(context, sounds.soundResourceId)
                        mp.start()
                    },
                contentScale = ContentScale.Crop
            )
            Text(
                text = LocalContext.current.getString(sounds.stringResourceId),
                modifier = Modifier
                    .padding(16.dp)
                    .combinedClickable {
                        val mp = MediaPlayer.create(context, sounds.soundResourceId)
                        mp.start()
                    },
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SoundBoardAppTheme {

        SoundsCard (Sounds(R.string.android, R.drawable.image1,R.raw.android),modifier = Modifier, LocalContext.current)
    }
}