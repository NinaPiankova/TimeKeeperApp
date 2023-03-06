package com.example.timekeeperapp

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.view.Menu
import android.view.View
import java.util.*
import android.view.MenuItem
import android.widget.*

class MainActivity : AppCompatActivity() {
    private val _StartTimeInMillis:Long = 60000

    private var textViewCountDown:TextView? = null
    private var btnStartPause: ImageButton? = null
    private var btnReset: ImageButton? = null

    private var mCountDownTimer:CountDownTimer? = null

    private var timerRunning:Boolean = false
    private var timeLeftInMillies: Long = _StartTimeInMillis
    private var endTime:Long = 0;

    private var progressBar: ProgressBar? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_copy)


        textViewCountDown = findViewById(R.id.text_view_downcount)
        btnStartPause = findViewById(R.id.btn_start_pause1)
        btnReset = findViewById(R.id.btn_reset1)

        progressBar = findViewById(R.id.progress_bar)
        //progressBar!!.progress = (_StartTimeInMillis/10000).toInt()

        progressBar!!.max = (_StartTimeInMillis/1000).toInt()
        progressBar!!.progress = (_StartTimeInMillis/1000).toInt()

        btnStartPause!!.setOnClickListener{
            if(timerRunning){
                pauseTimer()
            }
            else{
                startTimer()
            }
        }

        btnReset!!.setOnClickListener {
            resetTimer()
        }
        updateCountDownText()
        //updateProgressBar()
    }

    private fun resetTimer() {
        timeLeftInMillies = _StartTimeInMillis
        updateCountDownText()
        //btnReset!!.visibility = View.INVISIBLE
        //btnStartPause!!.visibility = View.VISIBLE
        updateButtons()
    }

    private fun pauseTimer() {
        mCountDownTimer!!.cancel()
        timerRunning = false
        //btnStartPause!!.text = "start"
        //btnReset!!.visibility = View.VISIBLE
        updateButtons()
    }

    private fun startTimer() {
        endTime = System.currentTimeMillis()+timeLeftInMillies
        mCountDownTimer = object: CountDownTimer(timeLeftInMillies, 1000) {

            override fun onTick(millisUntilfinished: Long) {
                timeLeftInMillies = millisUntilfinished
                updateCountDownText()
                progressBar!!.progress --
                //updateProgressBar()
            }

            override fun onFinish() {


                timerRunning = false
                //btnStartPause!!.text = "start"
                btnStartPause!!.setImageResource(R.drawable.start_icons)


                btnStartPause!!.visibility = View.INVISIBLE
                btnReset!!.visibility = View.VISIBLE
            }
        }
        mCountDownTimer!!.start()
        timerRunning = true
        //btnStartPause!!.text = "pause"
        //btnReset!!.visibility = View.INVISIBLE
        updateButtons();
    }

   /* private fun updateProgressBar() {
        progressBar!!.progress = (timeLeftInMillies/10000).toInt()
    }*/

    private fun updateCountDownText() {
        val minutes:Int = (timeLeftInMillies/1000/60).toInt()
        val seconds:Int = (timeLeftInMillies/1000%60).toInt()
        val timeLeftFormatted: String = String.format(Locale.getDefault(),
            "%02d:%02d", minutes, seconds)
        textViewCountDown!!.text = timeLeftFormatted
        progressBar!!.progress = (timeLeftInMillies/1000).toInt()
    }
    private fun updateButtons(){
        if(timerRunning){
            btnReset!!.visibility = View.INVISIBLE
            btnStartPause!!.setImageResource(R.drawable.pause_icons)
            //btnStartPause!!.text = "pause"
        }
        else{
            btnStartPause!!.setImageResource(R.drawable.start_icons)
            //btnStartPause!!.text = "start"
        }
        if(timeLeftInMillies<1000){
            btnStartPause!!.visibility = View.INVISIBLE
        }
        else{
            btnStartPause!!.visibility = View.VISIBLE
        }
        if (timeLeftInMillies<_StartTimeInMillis){
            btnReset!!.visibility = View.VISIBLE
        }
        else{
            btnReset!!.visibility = View.INVISIBLE
        }
    }

    //Поворот экрана
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putLong("millisLeft", timeLeftInMillies)
        outState.putBoolean("timerRunner", timerRunning)
        outState.putLong("endTime",endTime)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        timeLeftInMillies = savedInstanceState.getLong("millisLeft")
        timerRunning = savedInstanceState.getBoolean("timerRunner")
        updateCountDownText()
        updateButtons()
        if(timerRunning){
            endTime = savedInstanceState.getLong("endTime")
            timeLeftInMillies = endTime - System.currentTimeMillis()
            startTimer()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(applicationContext, "click on setting", Toast.LENGTH_LONG).show()
                true
            }

            R.id.goal_settings ->{
                Toast.makeText(applicationContext, "click on exit", Toast.LENGTH_LONG).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


/* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }*/

    /*override fun onStop() {
        super.onStop()
        var sharePreferences: SharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sharePreferences.edit()
        editor.putLong("millisLeft",timeLeftInMillies)
        editor.putBoolean("timerRunner", timerRunning)
        editor.putLong("endTime",endTime)
        editor.apply()

    }

    override fun onStart() {
        super.onStart()

        var sharePreferences: SharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        timeLeftInMillies = sharePreferences.getLong("millisLeft", _StartTimeInMillis)
        timerRunning = sharePreferences.getBoolean("timerRunner",false)

        updateCountDownText()
        updateButtons()

        if(timerRunning){
            endTime = sharePreferences.getLong("endTime",0)
            timeLeftInMillies = endTime - System.currentTimeMillis()

            if(timeLeftInMillies<0){
                timeLeftInMillies=0
                timerRunning=false
                updateCountDownText()
                updateButtons()
            }
            else{
                startTimer()
            }
        }
    }*/
}