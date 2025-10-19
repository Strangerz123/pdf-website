package com.example.phoneinfo.ui.tests

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.phoneinfo.databinding.ActivityDisplayTestBinding

class DisplayTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisplayTestBinding
    private val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, Color.WHITE)
    private var colorIndex = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.displayTestLayout.setOnClickListener {
            finish()
        }

        changeColor()
    }

    private fun changeColor() {
        if (colorIndex < colors.size) {
            binding.displayTestLayout.setBackgroundColor(colors[colorIndex])
            colorIndex++
            handler.postDelayed({ changeColor() }, 1000)
        } else {
            finish()
        }
    }
}