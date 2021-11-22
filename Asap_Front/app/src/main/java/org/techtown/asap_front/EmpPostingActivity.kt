package org.techtown.asap_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.emp_posting_activity.*
import kotlinx.android.synthetic.main.job_posting_activity.*
import kotlinx.android.synthetic.main.job_posting_activity.jbtn

class EmpPostingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emp_posting_activity)

        ebtn.setOnClickListener{//완료버튼 클릭 시 입력한 정보 변수에 저장, 데이터 전송
            val title=eTitle.text.toString()
            val content=eContent.text.toString()
            val pay=pay.text //integer로 전환?
            //직종 정보
            //날짜(start, end)
            //시간(start, end)

            //정보 전송
        }
    }
}