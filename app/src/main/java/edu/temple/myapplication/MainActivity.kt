package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    var timerBinder: TimerService.TimerBinder? = null

    private lateinit var editText: EditText

    val handler: Handler = Handler(Looper.getMainLooper()){
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serviceConnection = object: ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                timerBinder = service as TimerService.TimerBinder
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                timerBinder = null
            }
        }

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        editText = findViewById(R.id.timerEditText)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            val editTextNum = editText.text.toString()
            try {
                editTextNum.toInt()
                timerBinder?.start(editTextNum.toInt(), handler)
            } catch (e: Exception){
                Log.d("Conversion Error", e.toString())
            }

        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            timerBinder?.pause()
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            timerBinder?.stop()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.timermenu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.action_start_timer -> {
                val editTextNum = editText.text.toString()
                try {
                    editTextNum.toInt()
                    timerBinder?.start(editTextNum.toInt(), handler)
                } catch (e: Exception){
                    Log.d("Conversion Error", e.toString())
                }
            }

            R.id.action_pause_timer -> timerBinder?.pause()

            R.id.action_stop_timer -> timerBinder?.stop()
        }

        return super.onOptionsItemSelected(item)
    }
}