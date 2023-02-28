package com.example.diceapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    lateinit var playerImgView: List<ImageView>
    lateinit var computerImgView: List<ImageView>
    private var playerDiceArray = mutableListOf<Int>()
    private var computerDiceArray = mutableListOf<Int>()
    private var playerScore = 0
    private var computerScore = 0
    private var playerRollCount = 0
    private var optionalRollCount = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val throwButton = findViewById<Button>(R.id.throwbtn)
        val scoreBtn = findViewById<Button>(R.id.scoreBtn)

        playerImgView = listOf(
            findViewById(R.id.hRoll1),
            findViewById(R.id.hRoll2),
            findViewById(R.id.hRoll3),
            findViewById(R.id.hRoll4),
            findViewById(R.id.hRoll5)
        )

        computerImgView = listOf(
            findViewById(R.id.cRoll1),
            findViewById(R.id.cRoll2),
            findViewById(R.id.cRoll3),
            findViewById(R.id.cRoll4),
            findViewById(R.id.cRoll5)
        )

        if (playerDiceArray != null){

        }

        throwButton.setOnClickListener {
            // Generate random values for the dice
            if (playerRollCount<3){
                rollDice()
                // Display the dice images
                showDiceImages(playerDiceArray, playerImgView)
                showDiceImages(computerDiceArray, computerImgView)
                playerRollCount++

                val reRollbtn = findViewById<Button>(R.id.reRollBtn)
                reRollbtn.visibility = View.VISIBLE
                reRollbtn.setOnClickListener {
                    if (optionalRollCount!=0){

                    }
                }
                totalScore()
                if (playerRollCount == 3){
                    scoreBtn.performClick()
                }
            }else{
                Toast.makeText(this,"Maximum rolls reached!",Toast.LENGTH_SHORT).show()
            }
        }
        scoreBtn.setOnClickListener {
//            totalScore()
            playerRollCount = 0
            showScore()
        }
    }

    private fun rollDice() {
        for (i in 0..4) {
            playerDiceArray.add(Random.nextInt(1, 7))
            computerDiceArray.add(Random.nextInt(1, 7))
        }
    }

    private fun totalScore() {
        playerScore += addScore(playerDiceArray)
        computerScore += addScore(computerDiceArray)
        playerDiceArray.clear()
        computerDiceArray.clear()
    }

    private fun showScore() {
        val viewScore = "SCORE:\nCOM:$computerScore\nME:$playerScore"
        findViewById<TextView>(R.id.scoreTextView).text = viewScore
    }

    private fun addScore(DiceArray: MutableList<Int>): Int {
        var tot = 0
        for (i in DiceArray){
            tot+= i
        }
        return tot
    }

    private fun showDiceImages(values: MutableList<Int>, humanImgView: List<ImageView>) {

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
        }
        println(values)

    }

}