package org.techtown.asap_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.job_posting_activity.*
import java.util.*

class JobPostingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_posting_activity)

        //데이트피커
        jStartDate.minDate = System.currentTimeMillis()
        jEndDate.minDate = System.currentTimeMillis()
        //타임피커
        jStartTime.setIs24HourView(true)
        jEndTime.setIs24HourView(true)

        jSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val data = resources.getStringArray(R.array.job)
                val jText = jJoblist.text.toString()
                val token = jText.split(", ")

                var b = true
                for (t in token.indices) {
                    if (token[t].equals(data[p2]))
                        b = false
                }
                if (b) {
                    if (jJoblist.text.equals("")) {
                        jJoblist.text = data[p2].toString()
                    } else {
                        jJoblist.text = jText + ", " + data[p2].toString()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        jbtn.setOnClickListener{//완료버튼 클릭 시 입력한 정보 변수에 저장, 데이터 전송
            val title=jTitle.text.toString() 
            val content=jContent.text.toString()
            //경력 정보, 텍스트필드 내용->,로 split해서 리스트 타입 token으로 변환
            val joblist = jJoblist.text.toString()
            val token = joblist.split(", ")

            //날짜(start, end)
            val startDate = jStartDate.year.toString()+"-"+(jStartDate.month+1).toString()+"-"+jStartDate.dayOfMonth.toString()
            val endDate = jEndDate.year.toString()+"-"+(jEndDate.month+1).toString()+"-"+jEndDate.dayOfMonth.toString()

            //시간(start, end)
            val startTime = jStartTime.hour.toString()+":"+jStartTime.minute.toString()+":00"
            val endTime = jEndTime.hour.toString()+":"+jEndTime.minute.toString()+":00"
            
            //정보 전송
        }
    }
}