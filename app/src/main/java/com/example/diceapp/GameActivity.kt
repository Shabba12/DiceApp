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
import kotlin.random.nextInt

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

        throwButton.setOnClickListener {
            optionalRollCount = 2
            // Generate random values for the dice
            if (playerRollCount<3){
                rollDice()
                // Display the dice images
                showDiceImages(playerDiceArray, playerImgView)
                showDiceImages(computerDiceArray, computerImgView)
                playerRollCount++

//                totalScore()
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

                reRollbtn.visibility = View.VISIBLE

                computerStrategy()
                optionalRollCount = 2


            }else{
                Toast.makeText(this,"Maximum rolls reached!",Toast.LENGTH_SHORT).show()
            }
        }

        reRollbtn.setOnClickListener {
            //once reroll is called it subtracts the throw dice total because re-roll will roll that same turn again
            tempPlayerScore -= playerDiceArray.sum()
            reRolldices(playerDiceArray,playerSelectedRoll,playerImgView)
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
                    displayPopup("You win!", Color.GREEN)
                }else if (computerScore>playerScore){
                    displayPopup("You lose", Color.RED)
                }
            }
            tempPlayerScore = 0
            tempComputerScore = 0
            playerRollCount = 0
            playerDiceArray.clear()
            computerDiceArray.clear()
            playerSelectedRoll.clear()
            computerSelectedRoll.clear()
            computerMessage.text = ""
            reRollbtn.visibility = View.INVISIBLE
            setDefaultImage(playerImgView)
            setDefaultImage(computerImgView)
        }
    }


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
            "computerAttemptsMade" to computerAttemptsMade
        )

        // Store the map in the bundle
        outState.putSerializable("savedState", savedState as Serializable)
    }




    private fun reRolldices(
        DiceArray: MutableList<Int>,
        SelectedRoll: MutableList<Int>,
        ImgView: List<ImageView>
    ) {
//        tempScore -= DiceArray.sum()
        if (optionalRollCount!=0){
            optionalRollCount--
            rollSelectedDice(SelectedRoll,DiceArray)
            showDiceImages(DiceArray,ImgView)
            //adds the reroll value to the score board
//            tempScore = DiceArray.sum()

        }else{
            Toast.makeText(this,"Maximum re-rolls reached!",Toast.LENGTH_SHORT).show()
//            tempScore = DiceArray.sum()
        }
    }

    private fun computerStrategy() {

        val toReRoll = Random.nextInt(1,10)
        if (toReRoll % 2 == 0 ){
            optionalRollCount = 2
            for (i in 0 until Random.nextInt(0,4)){
                computerSelectedRoll.add(Random.nextInt(0,4))
            }
//            computerSelectedRoll.add(Random.nextInt(0,4))
            computerMessage.text = "I re-rolled selecting dice: $computerSelectedRoll"
            tempComputerScore -= computerDiceArray.sum()
            reRolldices(computerDiceArray,computerSelectedRoll,computerImgView)
            tempComputerScore = computerDiceArray.sum()
        }else{
            computerMessage.text = "Im happy with the result"
        }

        //algorithm
        if (computerScore<playerScore-20){
            for (i in 0 until  computerDiceArray.size){
                if (computerDiceArray[i]>4){
                    computerSelectedRoll.add(i)
                }
            }
        }
    }

    private fun setDefaultImage(diceImageViews: List<ImageView>) {
        for (i in diceImageViews){
            i.setImageResource(R.drawable.rolling_cup)
        }
    }

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

    private fun rollSelectedDice(selectedRoll: MutableList<Int>, DiceArray: MutableList<Int>) {
        println(selectedRoll)
        println(DiceArray)
        for (i in 0 until DiceArray.size){
            if (!selectedRoll.contains(i)){
                DiceArray[i] = Random.nextInt(1, 7)
            }
        }
        println(DiceArray)
    }

    private fun rollDice() {
        for (i in 0..4) {
            playerDiceArray.add(Random.nextInt(1, 7))
            computerDiceArray.add(Random.nextInt(1, 7))
        }
    }

    private fun totalScore() {
        playerScore += tempPlayerScore
        computerScore += tempComputerScore
        tempPlayerScore = 0
        tempComputerScore = 0
        playerRollCount = 0
        playerDiceArray.clear()
        computerDiceArray.clear()
        playerSelectedRoll.clear()

    }

    private fun showScore() {
        val viewScore = "SCORE:\nCOM:$computerScore\nME:$playerScore"
        findViewById<TextView>(R.id.scoreTextView).text = viewScore
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

    }

}