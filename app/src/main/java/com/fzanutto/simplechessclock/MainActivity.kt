package com.fzanutto.simplechessclock

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fzanutto.simplechessclock.databinding.ActivityMainBinding
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var activePlayer = 1
    private var player1TimerMili: Long = 120 * 1000
    private var player2TimerMili: Long = 160 * 1000
    private var lastUpdateTick = System.currentTimeMillis()
    private var isRunning = false
    private var timer = Timer()
    private var timerTask = object : TimerTask() {
        override fun run() {
            runOnUiThread {
                updateTimers()
                updateUI()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListeners()

        setTimerTask()
    }

    private fun convertMiliSecondsToMinutesString(miliseconds: Long): String {
        val seconds = miliseconds / 1000
        val minutesString = (seconds / 60).toString().padStart(2, '0')
        val secondsString = (seconds % 60).toString().padStart(2, '0')

        return "$minutesString:$secondsString"
    }

    private fun updateUI() {
        binding.apply {
            player1Clock.text = convertMiliSecondsToMinutesString(player1TimerMili)
            player2Clock.text = convertMiliSecondsToMinutesString(player2TimerMili)
        }
    }

    private fun updateTimers() {
        if (!isRunning) return

        val currentTime = System.currentTimeMillis()
        if (activePlayer == 1) {
            player1TimerMili -= currentTime - lastUpdateTick
            Log.d("CURRENTTIME", "PLAYER 1 time: $player1TimerMili")
        } else {
            player2TimerMili -= currentTime - lastUpdateTick
            Log.d("CURRENTTIME", "PLAYER 2 time: $player2TimerMili")
        }
        lastUpdateTick = currentTime
    }


    private fun setTimerTask() {
        timer.schedule(timerTask, 0, 100)
    }


    private fun setClickListeners() {
        binding.apply {
            settingsButton.setOnClickListener {
            }

            playButton.setOnClickListener {
                lastUpdateTick = System.currentTimeMillis()
                isRunning = true
            }

            pauseButton.setOnClickListener {
                isRunning = false
            }

            player1.setOnClickListener {
                updateTimers()
                updateUI()
                activePlayer = 2
            }

            player2.setOnClickListener {
                updateTimers()
                updateUI()
                activePlayer = 1
            }
        }
    }
}