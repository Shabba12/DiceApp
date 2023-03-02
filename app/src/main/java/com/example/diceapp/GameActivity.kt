package com.example.diceapp


import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var playerImgView: List<ImageView>
    private lateinit var computerImgView: List<ImageView>
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
            //once reroll is called it subtracts the throw dice total because re-roll will roll that same turn again
            playerScore -= playerDiceArray.sum()
            if (optionalRollCount!=0){
                optionalRollCount--
                rollSelectedDice(selectedRoll,playerDiceArray)
                showDiceImages(playerDiceArray,playerImgView)
                //adds the reroll value to the score board
                playerScore += playerDiceArray.sum()

            }else{
                Toast.makeText(this,"Maximum re-rolls reached!",Toast.LENGTH_SHORT).show()
                playerScore += playerDiceArray.sum()
            }
        }
        scoreBtn.setOnClickListener {
//            totalScore()
            playerRollCount = 0
            showScore()
            playerDiceArray.clear()
            computerDiceArray.clear()
            selectedRoll.clear()
            checkScore()
            setDefaultImage(playerImgView)
            setDefaultImage(computerImgView)
        }
    }

    private fun setDefaultImage(diceImageViews: List<ImageView>) {
        for (i in diceImageViews){
            i.setImageResource(R.drawable.rolling_cup)
        }
    }

    private fun checkScore() {
        if (playerScore >= 101) {
            displayPopup("You win!", Color.GREEN)
        } else if (computerScore >= 101) {
            displayPopup("You lose", Color.RED)
        }
    }

    private fun displayPopup(result: String, color: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(result)
            .setPositiveButton("OK", null)
        // Disable all clicking features for the activity
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        dialog.findViewById<TextView>(android.R.id.message)?.setTextColor(color)
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