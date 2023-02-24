package com.example.diceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val aboutBtn = findViewById<Button>(R.id.aboutBtn)
        aboutBtn.setOnClickListener {
            val intent = Intent(this,AboutUsActivity::class.java)
            startActivity(intent)
        }
    }


}