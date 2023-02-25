package com.example.diceapp

import android.content.ContentValues.TAG
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.tabs.TabLayout.TabGravity
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private var humanDiceArray = Array(5){0
    }
    private var computerDiceArray = Array(5){0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val throwButton = findViewById<Button>(R.id.throwbtn)
        val scoreBtn = findViewById<Button>(R.id.scoreBtn)

        throwButton.setOnClickListener {
            // Generate random values for the dice
            for (i in 0..4) {
                humanDiceArray[i] = Random.nextInt(1, 7)
                computerDiceArray[i] = Random.nextInt(1, 7)
            }

            // Display the dice images
            showDiceImages(humanDiceArray, "human")
//            showDiceImages(computerDiceArray, "computer")
        }

    }

    private fun showDiceImages(values: Array<Int>, s: String) {
        val imageId = arrayOf(
            R.drawable.dice_1,
            R.drawable.dice_2,
            R.drawable.dice_3,
            R.drawable.dice_4,
            R.drawable.dice_5,
            R.drawable.dice_6
        )
        val roll_1 = findViewById<ImageView>(R.id.roll1)
        val roll_2 = findViewById<ImageView>(R.id.roll2)
        val roll_3 = findViewById<ImageView>(R.id.roll3)
        val roll_4 = findViewById<ImageView>(R.id.roll4)
        val roll_5 = findViewById<ImageView>(R.id.roll5)

        roll_1.setImageResource(imageId[values[0]])
        roll_2.setImageResource(imageId[values[1]])
        roll_3.setImageResource(imageId[values[2]])
        roll_4.setImageResource(imageId[values[3]])
        roll_5.setImageResource(imageId[values[4]])
        Log.d("TAG", values.toString())
        println(s)

    }

}