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
import java.io.Serializable
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
    private var playerSelectedRoll = mutableListOf<Int>()
    private var computerSelectedRoll = mutableListOf<Int>()
    private var targetValue = 0
    private var tempPlayerScore = 0
    private var tempComputerScore = 0
    private lateinit var computerMessage:TextView
    private var tieScore = false
    private var playerAttemptsMade = 0
    private var computerAttemptsMade = 0
    companion object {
        var totalHumanWins = 0
        var totalComputerWins = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        if (savedInstanceState != null) {
            val savedState = savedInstanceState.getSerializable("savedState") as? Map<String, Any>
            if (savedState != null) {
                playerScore = savedState["playerScore"] as? Int ?: playerScore
                computerScore = savedState["computerScore"] as? Int ?: computerScore
                playerRollCount = savedState["playerRollCount"] as? Int ?: playerRollCount
                optionalRollCount = savedState["optionalRollCount"] as? Int ?: optionalRollCount
                playerSelectedRoll = savedState["playerSelectedRoll"] as? MutableList<Int> ?: playerSelectedRoll
                computerSelectedRoll = savedState["computerSelectedRoll"] as? MutableList<Int> ?: computerSelectedRoll
                targetValue = savedState["targetValue"] as? Int ?: targetValue
                tempPlayerScore = savedState["tempPlayerScore"] as? Int ?: tempPlayerScore
                tempComputerScore = savedState["tempComputerScore"] as? Int ?: tempComputerScore
                tieScore = savedState["tieScore"] as? Boolean ?: tieScore
                playerAttemptsMade = savedState["playerAttemptsMade"] as? Int ?: playerAttemptsMade
                computerAttemptsMade = savedState["computerAttemptsMade"] as? Int ?: computerAttemptsMade
                playerDiceArray = savedState["playerDiceArray"] as? MutableList<Int> ?: playerDiceArray
                computerDiceArray = savedState["computerDiceArray"] as? MutableList<Int> ?: computerDiceArray

            }
        }

        val throwButton = findViewById<Button>(R.id.throwbtn)
        val scoreBtn = findViewById<Button>(R.id.scoreBtn)
        val reRollbtn = findViewById<Button>(R.id.reRollBtn)
        val winScoreText = findViewById<TextView>(R.id.WinScoreText)
        computerMessage = findViewById(R.id.computerMessage)
        targetValue = intent.getIntExtra("target",101)

        if (tieScore){
            reRollbtn.isEnabled = false
        }

        winScoreText.text ="H: $totalHumanWins/C: $totalComputerWins"

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
        //if the playerDiceArray is not empty it was destroyed in a state where the dice was rolled
        if (playerDiceArray.isNotEmpty()){
            showDiceImages(playerDiceArray, playerImgView)
            showDiceImages(computerDiceArray, computerImgView)
            throwButton.isEnabled = false

            if (optionalRollCount!=0){
                //if the optional count is not 2 then the user has re rolled before destroying
                reRollbtn.visibility = View.VISIBLE
            }
        }
        if (playerScore>0){
            showScore()
        }

        throwButton.setOnClickListener {
            optionalRollCount = 2
            // Generate random values for the dice
            if (playerRollCount<3){
                rollDice()
                // Display the dice images
                showDiceImages(playerDiceArray, playerImgView)
                showDiceImages(computerDiceArray, computerImgView)
                playerRollCount++


                tempPlayerScore = playerDiceArray.sum()
                tempComputerScore = computerDiceArray.sum()

                playerImgView[0].setOnClickListener{
                    playerSelectedRoll.add(0)
                    Toast.makeText(this,"Roll 1 selected",Toast.LENGTH_SHORT).show()
                }
                playerImgView[1].setOnClickListener{
                    playerSelectedRoll.add(1)
                    Toast.makeText(this,"Roll 2 selected",Toast.LENGTH_SHORT).show()
                }
                playerImgView[2].setOnClickListener{
                    playerSelectedRoll.add(2)
                    Toast.makeText(this,"Roll 3 selected",Toast.LENGTH_SHORT).show()
                }
                playerImgView[3].setOnClickListener{
                    playerSelectedRoll.add(3)
                    Toast.makeText(this,"Roll 4 selected",Toast.LENGTH_SHORT).show()
                }
                playerImgView[4].setOnClickListener{
                    playerSelectedRoll.add(4)
                    Toast.makeText(this,"Roll 5 selected",Toast.LENGTH_SHORT).show()
                }

                if (!tieScore){
                    reRollbtn.visibility = View.VISIBLE
                }

                computerStrategy()
                optionalRollCount = 2
                // to disable throw button once is it hit unless the score button is clicked the throw button will stay disabled
                throwButton.isEnabled = false
                scoreBtn.visibility = View.VISIBLE


            }else{
                Toast.makeText(this,"Maximum rolls reached!",Toast.LENGTH_SHORT).show()
            }
        }

        reRollbtn.setOnClickListener {
            //once reroll is called it subtracts the throw dice total because re-roll will roll that same turn again
            tempPlayerScore -= playerDiceArray.sum()
            reRolldices(playerDiceArray,playerSelectedRoll,playerImgView,"Player")
            tempPlayerScore = playerDiceArray.sum()
        }


        scoreBtn.setOnClickListener {

            playerScore += tempPlayerScore
            computerScore += tempComputerScore
            playerAttemptsMade++
            computerAttemptsMade++
            showScore()

            if (!tieScore) {
                if (playerScore >= targetValue || computerScore >= targetValue) {
                    // Determine winner based on score and attempts
                    if (playerScore > computerScore || (playerScore == computerScore && playerAttemptsMade < computerAttemptsMade)) {
                        displayPopup("You win!", Color.GREEN)
                        totalHumanWins++
                    } else if (playerScore == computerScore) {
                        tieScore = true
                        displayPopup("tie", Color.CYAN)
                    } else {
                        displayPopup("You lose", Color.RED)
                        totalComputerWins++
                    }
                }
            }else{
                if (playerScore> computerScore){
                    tieScore = false
                    displayPopup("You win!", Color.GREEN)
                    totalHumanWins++
                }else if (computerScore>playerScore){
                    tieScore = false
                    displayPopup("You lose", Color.RED)
                    totalComputerWins++
                }
            }
            tempPlayerScore = 0
            tempComputerScore = 0
            playerRollCount = 0
            playerDiceArray.clear()
            computerDiceArray.clear()
            playerSelectedRoll.clear()
            computerSelectedRoll.clear()
            throwButton.isEnabled = true
            computerMessage.text = ""
            reRollbtn.visibility = View.INVISIBLE
            scoreBtn.visibility = View.INVISIBLE
            setDefaultImage(playerImgView)
            setDefaultImage(computerImgView)
        }
    }


    //below functions saves the data incase the application destroys or rotates
    override fun onDestroy() {
        super.onDestroy()
        totalHumanWins = totalHumanWins
        totalComputerWins  = totalComputerWins
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the variables in a map
        val savedState = mapOf(
            "playerScore" to playerScore,
            "computerScore" to computerScore,
            "playerRollCount" to playerRollCount,
            "optionalRollCount" to optionalRollCount,
            "playerSelectedRoll" to playerSelectedRoll,
            "computerSelectedRoll" to computerSelectedRoll,
            "targetValue" to targetValue,
            "tempPlayerScore" to tempPlayerScore,
            "tempComputerScore" to tempComputerScore,
            "tieScore" to tieScore,
            "playerAttemptsMade" to playerAttemptsMade,
            "computerAttemptsMade" to computerAttemptsMade,
            "playerDiceArray" to playerDiceArray,
            "computerDiceArray" to computerDiceArray
        )

        // Store the map in the bundle
        outState.putSerializable("savedState", savedState as Serializable)
    }



    //below function handles the number of re rolls
    private fun reRolldices(
        DiceArray: MutableList<Int>,
        SelectedRoll: MutableList<Int>,
        ImgView: List<ImageView>,
        s: String
    ) {

        if (optionalRollCount!=0){
            optionalRollCount--
            rollSelectedDice(SelectedRoll,DiceArray,s)
            showDiceImages(DiceArray,ImgView)


        }else{
            Toast.makeText(this,"Maximum re-rolls reached!",Toast.LENGTH_SHORT).show()

        }
    }

    //below handles the decision making for the computer
    private fun computerStrategy() {
        //algorithm to check if computer score is less than 20 points of the player score if so will re roll based on the highest numbers in rolled

        //If the computer's score is less than 20 and the player's score is less than 30, keep all five dice and roll again.
        if (!tieScore){
            if (computerScore<(targetValue*0.2) && playerScore<(targetValue*0.3)){
                println("Rule 1")
                computerMessage.text = "Im not going for a re-roll"
            }else if ((computerScore>=targetValue*0.2 && computerScore<targetValue*0.4) && playerScore<targetValue*0.4){
                println("Rule 2")
                //If the computer's score is between 20 and 39 and the player's score is less than 40, keep any dice that show 4 or higher and roll the remaining dice again.
                for (i in 0 until computerDiceArray.size){
                    if (computerDiceArray[i] >= 4){
                        computerSelectedRoll.add(i)
                    }
                }
                computerReRoll()
                computerSelectedRoll.clear()
                for (i in 0 until computerDiceArray.size){
                    if (computerDiceArray[i] >= 4){
                        computerSelectedRoll.add(i)
                    }
                }
                computerReRoll()
            }else if ((computerScore>=targetValue*0.4 && computerScore<targetValue*0.6) && playerScore<targetValue*0.6){
                println("Rule 3")
                //If the computer's score is between 40 and 59 and the player's score is less than 60, keep any dice that show 5 or higher and roll the remaining dice again.
                for (i in 0 until computerDiceArray.size){
                    if (computerDiceArray[i] >= 5){
                        computerSelectedRoll.add(i)
                    }
                }
                computerReRoll()
                computerSelectedRoll.clear()
                for (i in 0 until computerDiceArray.size){
                    if (computerDiceArray[i] >= 5){
                        computerSelectedRoll.add(i)
                    }
                }
                computerReRoll()
            }else if ((computerScore>=targetValue*0.6 && computerScore<targetValue*0.8) && playerScore<targetValue*0.8){
                println("Rule 4")
                //If the computer's score is between 60 and 79 and the player's score is less than 80, keep any dice that show 5 or 6 and roll the remaining dice again.
                for (i in 0 until computerDiceArray.size){
                    if (computerDiceArray[i] >= 5){
                        computerSelectedRoll.add(i)
                    }
                }
                computerReRoll()
                computerSelectedRoll.clear()
                for (i in 0 until computerDiceArray.size){
                    if (computerDiceArray[i] >= 5){
                        computerSelectedRoll.add(i)
                    }
                }
                computerReRoll()
            }else if (computerScore>=targetValue*0.8 && playerScore<targetValue*0.9){
                println("Rule 5")
                //If the computer's score is 80 or higher and the player's score is less than 90, keep any dice that show 4 or higher and roll the remaining dice again.
                for (i in 0 until computerDiceArray.size){
                    if (computerDiceArray[i] >= 4){
                        computerSelectedRoll.add(i)
                    }
                }
                computerReRoll()
                computerSelectedRoll.clear()
                for (i in 0 until computerDiceArray.size){
                    if (computerDiceArray[i] >= 4){
                        computerSelectedRoll.add(i)
                    }
                }
                computerReRoll()
            }else if (playerScore>= targetValue*0.9){
                println("Rule 6")
                //If the player's score is 90 or higher, keep any dice that show 3 or higher and roll the remaining dice again, regardless of the computer's score.
                for (i in 0 until computerDiceArray.size){
                    if (computerDiceArray[i] >= 3){
                        computerSelectedRoll.add(i)
                    }
                }
                computerReRoll()
                computerSelectedRoll.clear()
                for (i in 0 until computerDiceArray.size){
                    if (computerDiceArray[i] >= 3){
                        computerSelectedRoll.add(i)
                    }
                }
                computerReRoll()
            }else{
                computerMessage.text = "Im not going for a re-roll"
            }
            //if none of the conditions are met computer will not go for any rerolls
        }
    }

    //below function handles the computer re rolls
    private fun computerReRoll() {
        computerMessage.text = "I re-rolled selecting dice: $computerSelectedRoll"
        tempComputerScore -= computerDiceArray.sum()
        reRolldices(computerDiceArray, computerSelectedRoll, computerImgView, "Computer")
        tempComputerScore = computerDiceArray.sum()
    }

    //below function sets the default once a round is over
    private fun setDefaultImage(diceImageViews: List<ImageView>) {
        for (i in diceImageViews){
            i.setImageResource(R.drawable.rolling_cup)
        }
    }

    //below function displasy the result if a win or loss and tie occurs
    private fun displayPopup(result: String, color: Int) {
        val builder = AlertDialog.Builder(this)
        if(!tieScore) {
            builder.setMessage(result)
                .setPositiveButton("OK", null)
            // Disable all clicking features for the activity
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }else{
            builder.setMessage(result)
                .setPositiveButton("OK", null)
        }
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        dialog.findViewById<TextView>(android.R.id.message)?.setTextColor(color)
    }

    //below function rolls the non selected dice in a re roll
    private fun rollSelectedDice(
        selectedRoll: MutableList<Int>,
        DiceArray: MutableList<Int>,
        s: String
    ) {
        println("$s selected dice:$selectedRoll")
        println("$s:$DiceArray")
        for (i in 0 until DiceArray.size){
            if (!selectedRoll.contains(i)){
                DiceArray[i] = Random.nextInt(1, 7)
            }
        }
        println("$s:$DiceArray")
    }

    //below function rolls the dice for both player and computer
    private fun rollDice() {
        for (i in 0..4) {
            playerDiceArray.add(Random.nextInt(1, 7))
            computerDiceArray.add(Random.nextInt(1, 7))
        }
    }

    //below function displays the score
    private fun showScore() {
        val viewScore = "SCORE:\nCOM:$computerScore\nME:$playerScore"
        findViewById<TextView>(R.id.scoreTextView).text = viewScore
    }

    //the below function display the dice numbers in images
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

    }

}


/*
The computer strategy above is a decision making algorithm that is implemented to be used by the computer. the objective of the strategy is to reach the target value or else 101 points(if the target is not set). The strategy is designed in a way that the computer decides whether to re roll or not if it decides to re roll which dices are selected andf re rolled.
The algorithm is based on 6 rules and decision is made looking at the current score of the computer as well as the player score

Justification:  It takes into account the current score of both players and balances the risk of going for a re-roll with the potential reward of getting a higher score. By keeping a certain number of dice and re-rolling others, the computer can increase its chances of getting a better combination that could lead to a higher score.

Advantages: The accumulates the scores of both computer and player and makes its decision on what to do rather than a random selection.
the algorithm decides on the best possible dices to keep and re roll which in turn give higher chances of getting a higher score
brings a slight challenge for the player which makes the game interesting

Disadvantages: The algorithm is relatively simple there the moves can be predicted by the player after few rounds
the algorithm takes both re rolls if it decides to re roll. it doesn't have the capability of choosing number of times to reroll
the algorithm does not take into account any potential psychological factors or bluffing strategies that the opponent may be using, which could influence the outcome of the game.

 */