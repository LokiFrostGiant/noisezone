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
import androidx.compose.material.TopAppBar
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.net.URL

private lateinit var database: DatabaseReference

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
        Column {
            TopBarWithAdd()
            SoundsList(soundList = Datasource().loadSounds(), modifier = Modifier,context)
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopBarWithAdd(){
    TopAppBar() {
        Text(
            text = "noisezone",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
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

    database = Firebase.database.reference
    //val storageReference = Firebase.storage.reference
    val db = Firebase.firestore
    val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()
    val s =  URL("https://firebasestorage.googleapis.com/v0/b/noisezone-e2294.appspot.com/o/sounds%2Fahhh.mp3?alt=media&token=f043f8b6-d133-4d43-9694-0712f25e5854")
    val i = URL("https://firebasestorage.googleapis.com/v0/b/noisezone-e2294.appspot.com/o/images%2Fimage1.jpg?alt=media&token=0840fc5b-b32a-4614-a8c7-47ce3bba13dd")

    //painter = rememberAsyncImagePainter(sounds.imageResourceId)

    Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(i),
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