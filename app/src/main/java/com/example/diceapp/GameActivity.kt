package com.example.diceapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private var humanDiceArray = mutableListOf<Int>()
    private var computerDiceArray = mutableListOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val throwButton = findViewById<Button>(R.id.throwbtn)
        val scoreBtn = findViewById<Button>(R.id.scoreBtn)

        val humanImgView = arrayOf(
            findViewById<ImageView>(R.id.hRoll1),
            findViewById<ImageView>(R.id.hRoll2),
            findViewById<ImageView>(R.id.hRoll3),
            findViewById<ImageView>(R.id.hRoll4),
            findViewById<ImageView>(R.id.hRoll5)

        )
        val computerImgView = arrayOf(
            findViewById(R.id.cRoll1),
            findViewById(R.id.cRoll2),
            findViewById(R.id.cRoll3),
            findViewById(R.id.cRoll4),
            findViewById(R.id.cRoll5),
        )


        throwButton.setOnClickListener {
            // Generate random values for the dice
            for (i in 0..4) {
                humanDiceArray.add(Random.nextInt(1, 7))
                computerDiceArray.add(Random.nextInt(1, 7))
            }

            // Display the dice images
            showDiceImages(humanDiceArray, humanImgView)
//            showDiceImages(computerDiceArray, "computer")
        }

    }

    private fun showDiceImages(values: MutableList<Int>, humanImgView: Array<ImageView>) {

        val imageId = arrayOf(
            R.drawable.dice_1,
            R.drawable.dice_2,
            R.drawable.dice_3,
            R.drawable.dice_4,
            R.drawable.dice_5,
            R.drawable.dice_6
        )
        for (i in humanImgView.indices){
            humanImgView[i].setImageResource(imageId[values[i]-1])
            println(values[i])
        }

        println(values)

    }

}