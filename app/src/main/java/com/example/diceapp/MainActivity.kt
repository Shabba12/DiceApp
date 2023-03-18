package com.example.diceapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val aboutBtn = findViewById<Button>(R.id.aboutBtn)
        val newGameBtn = findViewById<Button>(R.id.newGameBtn)
        val setTarget = findViewById<EditText>(R.id.editTextNumber)
        val howToPlayImg = findViewById<ImageView>(R.id.howToPlayImg)


        howToPlayImg.setOnClickListener{
            displayGameRules()
        }

        aboutBtn.setOnClickListener {
            val intent = Intent(this,AboutUsActivity::class.java)
            startActivity(intent)
        }
        newGameBtn.setOnClickListener {
            var targetValue = setTarget.text.toString().toIntOrNull()
            if (targetValue == 0){
                Toast.makeText(this,"0 can not be set as a target default value 101 have been set!", Toast.LENGTH_SHORT).show()
                targetValue = null
            }
            val intent = Intent(this,GameActivity::class.java)
            intent.putExtra("target",targetValue)
            startActivity(intent)
            setTarget.setText("")


        }
    }

    private fun displayGameRules() {
        val gameRules = "Rules of the game:\n\nThrow Button: You will throw 5 dices at the same time.\n\nScore Button: You will score the sum of all 5 dices if you are happy with the dices rolled\n\nReRoll Button: You get upto 2 rerolls per throw you can select which dices you want to keep and re-roll the rest.\n\nGoal of the game: Reach the target before the computer does to win"
        val builder = AlertDialog.Builder(this)
        builder.setMessage(gameRules)
            .setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        dialog.findViewById<TextView>(android.R.id.message)
    }
}