package com.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.adapters.HistoryAdapter
import com.config.ConsumerData
import com.db.DatabaseHandler
import com.example.veriparkassignment.R
import com.helper.HelperMethods

class HistoryActivity : Activity() {

    val db = DatabaseHandler(this@HistoryActivity)
    val consumerDataList = ArrayList<ConsumerData>()
    var helperMethods = HelperMethods.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_layout)

        val service_number = intent.getStringExtra(INTENT_SERVICE_NUMBER)

        val selectedConsumerData = db.getSelectedConsumerData(service_number)

        for (cd in selectedConsumerData) {
            consumerDataList.add(cd)
        }

        val rv = findViewById<RecyclerView>(R.id.recyclerView1)

        if(consumerDataList.size >0 && consumerDataList != null){
            var adapter = HistoryAdapter(consumerDataList)
            rv.adapter = adapter
        }else{
            helperMethods.messagePopUp(getString(R.string.no_data_found),"error",this@HistoryActivity)
        }
    }

    companion object {

        private val INTENT_SERVICE_NUMBER = "service_number"

        fun newIntent(context: Context, serviceNumber: String): Intent {
            val intent = Intent(context, HistoryActivity::class.java)
            intent.putExtra(INTENT_SERVICE_NUMBER, serviceNumber)
            return intent
        }
    }
}
