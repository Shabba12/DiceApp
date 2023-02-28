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
    private var selectedRoll = mutableListOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val throwButton = findViewById<Button>(R.id.throwbtn)
        val scoreBtn = findViewById<Button>(R.id.scoreBtn)
        val reRollbtn = findViewById<Button>(R.id.reRollBtn)

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



        throwButton.setOnClickListener {
            playerDiceArray.clear()
            computerDiceArray.clear()
            selectedRoll.clear()
            optionalRollCount = 2
            // Generate random values for the dice
            if (playerRollCount<3){
                rollDice()
                // Display the dice images
                showDiceImages(playerDiceArray, playerImgView)
                showDiceImages(computerDiceArray, computerImgView)
                playerRollCount++

                totalScore()
                scoreBtn.performClick()
                playerImgView[0].setOnClickListener{
                    selectedRoll.add(0)
                    Toast.makeText(this,"Roll 1 selected",Toast.LENGTH_SHORT).show()
                }
                playerImgView[1].setOnClickListener{
                    selectedRoll.add(1)
                    Toast.makeText(this,"Roll 2 selected",Toast.LENGTH_SHORT).show()
                }
                playerImgView[2].setOnClickListener{
                    selectedRoll.add(2)
                    Toast.makeText(this,"Roll 3 selected",Toast.LENGTH_SHORT).show()
                }
                playerImgView[3].setOnClickListener{
                    selectedRoll.add(3)
                    Toast.makeText(this,"Roll 4 selected",Toast.LENGTH_SHORT).show()
                }
                playerImgView[4].setOnClickListener{
                    selectedRoll.add(4)
                    Toast.makeText(this,"Roll 5 selected",Toast.LENGTH_SHORT).show()
                }

                reRollbtn.visibility = View.VISIBLE



            }else{
                Toast.makeText(this,"Maximum rolls reached!",Toast.LENGTH_SHORT).show()
            }
        }
        reRollbtn.setOnClickListener {
            //once reroll is called it substarts the throw dice total because reroll will roll that saem turn again
            playerScore -= playerDiceArray.sum()
            if (optionalRollCount!=0){
                optionalRollCount--
                rollSelectedDice(selectedRoll,playerDiceArray)
                showDiceImages(playerDiceArray,playerImgView)
                //adds the reroll value to the score board
                playerScore += playerDiceArray.sum()
                scoreBtn.performClick()
            }else{
                Toast.makeText(this,"Maximum re-rolls reached!",Toast.LENGTH_SHORT).show()
            }
        }
        scoreBtn.setOnClickListener {
//            totalScore()
            playerRollCount = 0
            showScore()
        }
    }

    private fun rollSelectedDice(selectedRoll: MutableList<Int>, playerDiceArray: MutableList<Int>) {
        println(selectedRoll)
        println(playerDiceArray)
        for (i in 0 until playerDiceArray.size){
            if (!selectedRoll.contains(i)){
                playerDiceArray[i] = Random.nextInt(1, 7)
            }
        }
        println(playerDiceArray)
    }

    private fun rollDice() {
        for (i in 0..4) {
            playerDiceArray.add(Random.nextInt(1, 7))
            computerDiceArray.add(Random.nextInt(1, 7))
        }
    }

    private fun totalScore() {
        playerScore += playerDiceArray.sum()
        computerScore += computerDiceArray.sum()
//        playerDiceArray.clear()
//        computerDiceArray.clear()
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