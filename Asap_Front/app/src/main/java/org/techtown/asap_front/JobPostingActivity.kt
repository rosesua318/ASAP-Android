package org.techtown.asap_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.job_posting_activity.*

class JobPostingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_posting_activity)

        jbtn.setOnClickListener{//완료버튼 클릭 시 입력한 정보 변수에 저장, 데이터 전송
            val title=jTitle.text.toString() 
            val content=jContent.text.toString()
            //경력 정보
            //날짜(start, end)
            //시간(start, end)
            
            //정보 전송
        }
    }
}