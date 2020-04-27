package com.example.diagnal.feature.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.diagnal.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
    }

    private fun setupView() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.containerFrameLayout, MainFragment())
            commit()
        }
    }
}
