package com.example.tripapp

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyJobService : JobService() {

    /* return
        false : 작업 정상 종료
        true :  작업 진행 중
     */
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("jiy00nlee", "MyJobService")
        return false
    }

    /* return
        false : 조건에 만족시 재등록
        true : 조건 만족시 등록 취소
     */
    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

}