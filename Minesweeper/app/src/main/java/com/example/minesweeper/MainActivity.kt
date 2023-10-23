package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.minesweeper.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.flagButton.setOnClickListener {
            binding.msView.changeMode()
        }

        binding.resetButton.setOnClickListener {
            binding.msView.resetGame()
        }
        setContentView(binding.root)
    }

    fun changeButtonText(s: String){
        binding.flagButton.text = s
    }

    fun win() {
        Snackbar.make(binding.msView, R.string.bottomTextWin, Snackbar.LENGTH_LONG)
            .show()
    }

    fun lose() {
        Snackbar.make(binding.msView, R.string.bottomTextLose, Snackbar.LENGTH_LONG)
            .show()
    }
}