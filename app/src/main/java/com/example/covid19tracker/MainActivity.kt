package com.example.covid19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Adapter
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    //private val list:ListView=findViewById(R.id.lv_cases)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list:ListView=findViewById(R.id.lv_cases)
        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_header,list,false))
        run()
    }

    private fun run() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) {
                Client.api.execute()
            }
            if (response.isSuccessful) {
                //println(response.body!!.string())
                val data = Gson().fromJson(response.body!!.string(), Resp::class.java)
                launch(Dispatchers.Main) {
                    bindCombinedData(data.statewise[0])
                    bindStateWiseData(data.statewise.subList(1,data.statewise.size))
                }
            } else {
                throw IOException("Unexpected code $response")
            }
        }
    }

    private fun bindStateWiseData(subList: List<statewise>) {
        val Listadapter = ListAdapter(subList)
        val list:ListView=findViewById(R.id.lv_cases)
        list.adapter=Listadapter
    }

    fun bindCombinedData(data: statewise) {
        val lastUpdatedTime = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val tv_lastUpdatedText: TextView = findViewById(R.id.tv_lastUpdated)
        tv_lastUpdatedText.text = "LAST UPDATED\n ${getTimeAgo(simpleDateFormat.parse(lastUpdatedTime))}"

        val confirmed:TextView=findViewById(R.id.tv_confirmed)
        val active:TextView=findViewById(R.id.tv_active)
        val recovered:TextView=findViewById(R.id.tv_recovered)
        val dead:TextView=findViewById(R.id.tv_dead)

        confirmed.text=data.confirmed
        active.text=data.active
        recovered.text=data.recovered
        dead.text=data.deaths
    }


    fun getTimeAgo(past: Date): String {
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

            return when {
                seconds < 60 -> {
                    "FEW SECONDS AGO"
                }
                minutes < 60 -> {
                    "$minutes MINUTES AGO"
                }
                hours < 24 -> {
                    "$hours HOUR ${minutes % 60} MINS AGO"
                }
                else -> {
                    SimpleDateFormat("dd/MM/yy, hh:mm a").format(past).toString()
                }
            }
    }
}
