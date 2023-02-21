package com.example.diceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val button  = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            changeText(textView)
        }


    }

    private fun changeText(textView: TextView?) {
        if (textView != null) {
            textView.text = "Hi shabeer"
        }
    }
}