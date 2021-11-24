package org.techtown.asap_front

import android.app.Fragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.job_posting_activity.*
import org.techtown.asap_front.`interface`.JobPostListService
import org.techtown.asap_front.data_object.JobPost
import org.techtown.asap_front.data_object.JobPostModel
import org.techtown.asap_front.data_object.PostResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class JobPostingActivity : AppCompatActivity() {
    private var userId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_posting_activity)

        userId = intent.getStringExtra("userId")
        Log.d("JobUserId", userId!!)

        //데이트피커
        jStartDate.minDate = System.currentTimeMillis()
        jEndDate.minDate = System.currentTimeMillis()
        //타임피커
        jStartTime.setIs24HourView(true)
        jEndTime.setIs24HourView(true)

        var jobList = ArrayList<Int>()

        var retrofit = Retrofit.Builder()
            .baseUrl("https://asap-ds.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var jobPostListService = retrofit.create(JobPostListService::class.java)
        jSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val data = resources.getStringArray(R.array.select_job)
                val jText = jJoblist.text.toString()
                val token = jText.split(", ")

                var b = true
                for (t in token.indices) {
                    if (token[t].equals(data[p2]))
                        b = false
                }
                if(p2 == 0){
                    b = false
                }
                if (b) {
                    if (jJoblist.text.equals("")) {
                        jJoblist.text = data[p2].toString()
                    } else {
                        jJoblist.text = jText + ", " + data[p2].toString()
                    }

                    jobList.add(p2+1) //(선택된 포지션+1) 배열에 추가
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        jbtn.setOnClickListener{//완료버튼 클릭 시 입력한 정보 변수에 저장, 데이터 전송
            val title=jTitle.text.toString()
            val content=jContent.text.toString()

            //날짜(start, end)
            val startDate = jStartDate.year.toString()+"-"+(jStartDate.month+1).toString()+"-"+jStartDate.dayOfMonth.toString()
            val endDate = jEndDate.year.toString()+"-"+(jEndDate.month+1).toString()+"-"+jEndDate.dayOfMonth.toString()

            //시간(start, end)
            val startTime = jStartTime.hour.toString()+":"+jStartTime.minute.toString()+":00"
            val endTime = jEndTime.hour.toString()+":"+jEndTime.minute.toString()+":00"
            
            //정보 전송
            if(title==null || content==null || jobList.isEmpty()){
                Toast.makeText(this@JobPostingActivity, "입력하지 않은 항목이 있습니다", Toast.LENGTH_SHORT).show()
            }
            else{
                val jobPost = JobPostModel(userId!!.toInt(), title, jobList, startDate, endDate, startTime, endTime, content)
                jobPostListService.post(jobPost).enqueue(object: Callback<PostResult>{
                    override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                        Log.d("log",response.toString())
                        Log.d("log", response.body().toString())
                        Log.d("log", "jobpost: "+jobPost)
                        Toast.makeText(this@JobPostingActivity, "작성되었습니다", Toast.LENGTH_SHORT).show()

                        val intent1 = Intent(this@JobPostingActivity, MainActivity::class.java)
                        startActivity(intent1)
                    }

                    override fun onFailure(call: Call<PostResult>, t: Throwable) {
                        Log.d("log",t.message.toString())
                        Log.d("log","post fail")
                    }
                })
            }

        }
    }
}