package com.e.workmanagerkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    internal lateinit var oneTimeWorkRequest: OneTimeWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val data = Data.Builder().putString(Constants.KEY, "Do this work").build()
        //For setting the constraints
        //Constraints constraints = new Constraints.Builder().setRequiresCharging(false).build();
        oneTimeWorkRequest = OneTimeWorkRequest.Builder(BgWorker::class.java!!).setInputData(data).build()
    }

    fun startWork(view: View) {
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this,
            Observer { workInfo ->
                if (null != workInfo) {
                    if (workInfo.state.isFinished) {
                        val data = workInfo.outputData
                        val status = data.getString(Constants.RESULT)
                        mTvResult!!.append(status!! + " \n")
                    }
                }
            })
    }
}
