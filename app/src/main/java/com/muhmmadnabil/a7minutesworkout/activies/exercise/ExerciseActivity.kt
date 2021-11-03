package com.muhmmadnabil.a7minutesworkout.activies.exercise

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhmmadnabil.a7minutesworkout.*
import com.muhmmadnabil.a7minutesworkout.activies.finish.FinishActivity
import com.muhmmadnabil.a7minutesworkout.data.Constants
import com.muhmmadnabil.a7minutesworkout.data.model.ExerciseModel
import com.muhmmadnabil.a7minutesworkout.databinding.ActivityExerciseBinding
import com.muhmmadnabil.a7minutesworkout.databinding.DialogBackBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 10

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 30

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExercise)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)

        binding?.toolBarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setUpRestView()
        setUpExerciseRecyclerView()
    }

    override fun onBackPressed() {
        customDialogBack()
    }

    private fun customDialogBack() {
        val customDialog= Dialog(this)
        val dialogBinding=DialogBackBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)

        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }

        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setUpExerciseRecyclerView() {
        binding?.recyclerViewExercise?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.recyclerViewExercise?.adapter = exerciseAdapter
    }

    private fun setUpRestView() {

        try {
            val soundUri =
                Uri.parse("android.resource://com.muhmmadnabil.a7minutesworkout/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundUri)
            player?.isLooping = false
            player?.start()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        binding?.llRest?.visibility = View.VISIBLE
        binding?.llExercise?.visibility = View.GONE

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 10
        }

        binding?.tvUpcomingExercise?.text = exerciseList!![currentExercisePosition + 1].getName()

        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(p0: Long) {
                restProgress--
                binding?.progressBar?.progress = restProgress
                binding?.tvTimer?.text = restProgress.toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setUpExerciseView()
            }

        }.start()
    }

    private fun setUpExerciseView() {

        binding?.llExercise?.visibility = View.VISIBLE
        binding?.llRest?.visibility = View.GONE

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 30
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivExerciseImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseTitle?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar() {
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress--
                binding?.progressBarExercise?.progress = exerciseProgress
                binding?.tvTimerExercise?.text = exerciseProgress.toString()
            }

            override fun onFinish() {

                if (currentExercisePosition < exerciseList?.size!! - 1) {

                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()

                    setUpRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 10
        }

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 30
        }

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }
    }

    override fun onInit(p0: Int) {
        val result = tts?.setLanguage(Locale.ENGLISH)
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}