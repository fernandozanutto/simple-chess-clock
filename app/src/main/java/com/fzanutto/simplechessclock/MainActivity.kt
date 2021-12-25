package com.fzanutto.simplechessclock

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fzanutto.simplechessclock.databinding.ActivityMainBinding
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity(), TimePickerDialog.TimePickerDialogListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var timePickerDialog: TimePickerDialog
    private var activePlayer = 1
    private var player1TimerMilli: Long = 180 * 1000
    private var player2TimerMilli: Long = 180 * 1000
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

        timePickerDialog = TimePickerDialog()

        setClickListeners()

        setTimerTask()
    }

    private fun convertMilliSecondsToMinutesString(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutesString = (seconds / 60).toString().padStart(2, '0')
        val secondsString = (seconds % 60).toString().padStart(2, '0')

        return "$minutesString:$secondsString"
    }

    private fun updateUI() {
        binding.apply {
            player1Clock.text = convertMilliSecondsToMinutesString(player1TimerMilli)
            player2Clock.text = convertMilliSecondsToMinutesString(player2TimerMilli)
        }
    }

    private fun updateTimers() {
        if (!isRunning) return

        val currentTime = System.currentTimeMillis()
        if (activePlayer == 1) {
            player1TimerMilli -= currentTime - lastUpdateTick
            Log.d("CURRENTTIME", "PLAYER 1 time: $player1TimerMilli")
        } else {
            player2TimerMilli -= currentTime - lastUpdateTick
            Log.d("CURRENTTIME", "PLAYER 2 time: $player2TimerMilli")
        }
        lastUpdateTick = currentTime
    }


    private fun setTimerTask() {
        timer.schedule(timerTask, 0, 100)
    }


    private fun setClickListeners() {
        binding.apply {
            settingsButton.setOnClickListener {
                timePickerDialog.show(this@MainActivity.supportFragmentManager, null)
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

    override fun onPositiveClick(minutes: Int, seconds: Int) {
        val timeInMilli = (minutes * 60 + seconds) * 1000L

        player1TimerMilli = timeInMilli
        player2TimerMilli = timeInMilli
    }

}