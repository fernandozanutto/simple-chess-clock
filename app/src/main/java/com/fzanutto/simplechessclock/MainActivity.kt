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
    private var activePlayer = PlayerTurn.None
    private var originalTime: Long = 180 * 1000
    private var player1TimerMilli = originalTime
    private var player2TimerMilli = originalTime
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
        if (activePlayer == PlayerTurn.PlayerOne) {
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

    private fun updatePlayPauseButton() {
        val button = binding.playButton
        if (isRunning) {
            button.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            button.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    private fun setClickListeners() {
        binding.apply {
            settingsButton.setOnClickListener {
                timePickerDialog.show(this@MainActivity.supportFragmentManager, null)
            }

            playButton.setOnClickListener {
                if (activePlayer != PlayerTurn.None) {
                    lastUpdateTick = System.currentTimeMillis()
                    isRunning = !isRunning
                    updatePlayPauseButton()
                }
            }

            resetButton.setOnClickListener {
                isRunning = false
                player1TimerMilli = originalTime
                player2TimerMilli = originalTime
                updatePlayPauseButton()
                updateUI()
            }

            player1.setOnClickListener {
                if (activePlayer != PlayerTurn.PlayerTwo){
                    lastUpdateTick = System.currentTimeMillis()
                    isRunning = true
                    updatePlayPauseButton()
                    updateTimers()
                    updateUI()
                    activePlayer = PlayerTurn.PlayerTwo
                }
            }

            player2.setOnClickListener {
                if (activePlayer != PlayerTurn.PlayerOne){
                    lastUpdateTick = System.currentTimeMillis()
                    isRunning = true
                    updatePlayPauseButton()
                    updateTimers()
                    updateUI()
                    activePlayer = PlayerTurn.PlayerOne
                }
            }
        }
    }

    override fun onPositiveClick(minutes: Int, seconds: Int) {
        val timeInMilli = (minutes * 60 + seconds) * 1000L
        originalTime = timeInMilli
        player1TimerMilli = timeInMilli
        player2TimerMilli = timeInMilli
    }

}