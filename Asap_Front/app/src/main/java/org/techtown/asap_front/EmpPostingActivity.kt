package org.techtown.asap_front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.emp_posting_activity.*
import org.techtown.asap_front.`interface`.EmpPostListService
import org.techtown.asap_front.data_object.EmpPostModel
import org.techtown.asap_front.data_object.PostResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class EmpPostingActivity : AppCompatActivity() {
    private var userId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emp_posting_activity)

        userId = intent.getStringExtra("userId")
        Log.d("JobUserId", userId!!)

        //데이트피커
        eStartDate.minDate = System.currentTimeMillis()
        eEndDate.minDate = System.currentTimeMillis()

        //타임피커
        eStartTime.setIs24HourView(true)
        eEndTime.setIs24HourView(true)

        var jobList = ArrayList<Int>()

        var retrofit = Retrofit.Builder()
            .baseUrl("https://asap-ds.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var empPostListService = retrofit.create(EmpPostListService::class.java)
        eSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val data = resources.getStringArray(R.array.job)
                val eText = eJoblist.text.toString()
                val token = eText.split(", ")

                var b = true
                for (t in token.indices) {
                    if (token[t].equals(data[p2]))
                        b = false
                }
                if (b) {
                    if (eJoblist.text.equals("")) {
                        eJoblist.text = data[p2].toString()
                    } else {
                        eJoblist.text = eText + ", " + data[p2].toString()
                    }

                    jobList.add(p2+1) //(선택된 포지션+1) 배열에 추가
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        ebtn.setOnClickListener{//완료버튼 클릭 시 입력한 정보 변수에 저장, 데이터 전송
            val title=eTitle.text.toString()
            val content=eContent.text.toString()
            val hourly_pay=pay.text.toString()

            //날짜(start, end)
            val startDate = eStartDate.year.toString()+"-"+(eStartDate.month.toString()+1)+"-"+eStartDate.dayOfMonth.toString()
            val endDate = eEndDate.year.toString()+"-"+(eEndDate.month.toString()+1)+"-"+eEndDate.dayOfMonth.toString()

            //시간(start, end)
            val startTime = eStartTime.hour.toString()+":"+eStartTime.minute.toString()+":00"
            val endTime = eEndTime.hour.toString()+":"+eEndTime.minute.toString()+":00"

            //정보 전송
            val empPost = EmpPostModel(userId!!.toInt(), title, hourly_pay.toInt(), jobList, startDate, endDate, startTime, endTime, content)
            empPostListService.post(empPost).enqueue(object: Callback<PostResult>{
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    Log.d("log",response.toString())
                    Log.d("log", response.body().toString())
                    Log.d("log", "emppost: "+empPost)
                    Toast.makeText(this@EmpPostingActivity, "작성되었습니다", Toast.LENGTH_SHORT).show()

                    val intent1 = Intent(this@EmpPostingActivity, MainActivity::class.java)
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