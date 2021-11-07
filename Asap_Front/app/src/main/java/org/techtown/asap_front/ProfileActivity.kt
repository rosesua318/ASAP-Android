package org.techtown.asap_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.profile_activity.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        // 디비에 있는 추천수, 경력, 자기소개 내용 가져오기
        // 가져온 추천수 개수를 recommNum.text에 할당

        recommBtn.setOnClickListener {
            val numText = recommNum.text.toString()
            var num = numText.toInt()
            num += 1

            // 디비에 갱신하는 함수로직(num을 매개변수로)
        }
    }
}