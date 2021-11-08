package org.techtown.asap_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.job_post_activity.*

class JobPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_post_activity)

        // 디비에서 가져와서 해당 내용 텍스트뷰들에 할당
        // 게시물번호를 이용하여 해당하는 댓글만 디비에서 가져와서 리스트에 할당 -> 어댑터로 구성

        // 만약 글작성자라면 비밀 댓글 체크박스 비활성화

        var b = false
        jobSecret.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) { b = true }
            else {
                b = false
            }
        }

        jobCommentBtn.setOnClickListener {
            if(b) {
                // 비밀 댓글로 구분하여 디비에 저장
            } else {
                // 공개 댓글로 구분하여 디비에 저장
            }

        }
    }
}