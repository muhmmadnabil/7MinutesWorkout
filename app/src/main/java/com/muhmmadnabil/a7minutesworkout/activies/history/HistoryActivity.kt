package com.muhmmadnabil.a7minutesworkout.activies.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhmmadnabil.a7minutesworkout.HistoryAdapter
import com.muhmmadnabil.a7minutesworkout.HistoryDao
import com.muhmmadnabil.a7minutesworkout.R
import com.muhmmadnabil.a7minutesworkout.WorkOutApp
import com.muhmmadnabil.a7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistoryActivity)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        val dao = (application as WorkOutApp).db.historyDao()
        getAllDates(dao)

    }

    private fun getAllDates(historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.allDates().collect { dates ->
                if (dates.isNotEmpty()) {
                    binding?.tvHistory?.visibility = View.VISIBLE
                    binding?.rvHistory?.visibility = View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.GONE

                    binding?.rvHistory?.layoutManager=LinearLayoutManager(this@HistoryActivity)

                    val date=ArrayList<String>()
                    for(i in dates){
                        date.add(i.date)
                    }

                    val historyAdapter=HistoryAdapter(date)
                    binding?.rvHistory?.adapter=historyAdapter


                } else {
                    binding?.tvHistory?.visibility = View.GONE
                    binding?.rvHistory?.visibility = View.GONE
                    binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null

    }

}