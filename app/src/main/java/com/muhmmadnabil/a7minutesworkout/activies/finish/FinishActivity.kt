package com.muhmmadnabil.a7minutesworkout.activies.finish

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.muhmmadnabil.a7minutesworkout.HistoryDao
import com.muhmmadnabil.a7minutesworkout.HistoryEntity
import com.muhmmadnabil.a7minutesworkout.WorkOutApp
import com.muhmmadnabil.a7minutesworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {

    private var binding:ActivityFinishBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)

        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener {
            finish()
        }

        val dao=(application as WorkOutApp).db.historyDao()
        addDateToDatabase(dao)
    }

    private fun addDateToDatabase(historyDao: HistoryDao){

        val dateTime= Calendar.getInstance().time
        val sdf=SimpleDateFormat("dd MMM yyyy HH:ss",Locale.getDefault())
        val date=sdf.format(dateTime)

        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
        }

    }
}