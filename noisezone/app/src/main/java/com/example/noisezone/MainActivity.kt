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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.net.URL

private lateinit var database: DatabaseReference
lateinit var currCard : Sounds
var dispNum : Long = 9001

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database.reference
        val db = Firebase.firestore
        dbSetupTest(db)

        setContent {
            SoundBoard(this)
        }
    }

    fun dbSetup(db : FirebaseFirestore){
        var currSound = Sounds(R.string.android, R.drawable.image1,R.raw.android)
        var currMap = hashMapOf(
            "soundObj" to currSound
        )
        db.collection("sounds").document("test")
            .set(currMap)

        val soundsOut = db.collection("sounds").document("713OWWqHmv8OhV8YJpVh")
            .get()
            .result
            .toObject<Sounds>()

    }

    fun dbSetupTest(db : FirebaseFirestore){

        var testMap = hashMapOf(
            "num" to 42424242
        )

        db.collection("testInt").document("test")
            .set(testMap)

        var docOut = db.collection("testInt").document("test")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    println("Document grabbed!")
                    dispNum = document.data?.get("num") as Long? ?: 0
                    setContent {
                        SoundBoard(this)
                    }
                } else {
                    println("It succeeded but also didn't")
                }
            }
            .addOnFailureListener { exception ->
                println("Well that failed.")
            }


        println("Test function end. dispNum set to: $dispNum")
    }
}

@Composable
fun SoundBoard(context: Context) {
    SoundBoardAppTheme {
        Column {
            TopBarWithAdd()
            SoundsList(soundList = Datasource().loadSounds(), modifier = Modifier,context)
            //SoundsList(soundList = listOf(currCard), modifier = Modifier,context)
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopBarWithAdd(){
    TopAppBar() {
        Text(
            text = "noisezone $dispNum",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
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


    //val storageReference = Firebase.storage.reference

    val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()
    val s =  URL("https://firebasestorage.googleapis.com/v0/b/noisezone-e2294.appspot.com/o/sounds%2Fahhh.mp3?alt=media&token=f043f8b6-d133-4d43-9694-0712f25e5854")
    val i = URL("https://firebasestorage.googleapis.com/v0/b/noisezone-e2294.appspot.com/o/images%2Fimage1.jpg?alt=media&token=0840fc5b-b32a-4614-a8c7-47ce3bba13dd")

    //painter = rememberAsyncImagePainter(sounds.imageResourceId)

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