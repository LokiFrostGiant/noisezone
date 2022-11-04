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
import com.example.noisezone.model.SoundsURL
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
private var dispNum : Long = 9001
private var image : String = "";

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SoundBoard(this)
        }
    }

    override fun onResume() {
        super.onResume()
        database = Firebase.database.reference
        val db = Firebase.firestore
        dbSetup(db)
    }

    fun dbSetup(db : FirebaseFirestore){
        db.collection("sounds").document("tests")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    println("Document grabbed!")
                    var soundsOut = document.toObject<SoundsURL>()
                    if (soundsOut != null) {
                        image = soundsOut.image
                        setContent {
                            SoundBoard(this)
                        }
                    }
                    println(soundsOut.toString())
                } else {
                    println("It succeeded but also didn't")
                }
            }
            .addOnFailureListener { exception ->
                println("Well that failed.")
            }
    }

    fun dbSetupTest(db : FirebaseFirestore){

        var testMap = hashMapOf(
            "num" to 1111
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
        Column {
            Text(
                text = "noisezone $dispNum",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
        }
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
    val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()

    //painter = rememberAsyncImagePainter()
    //painter = painterResource(sounds.imageResourceId)

    Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(image),
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