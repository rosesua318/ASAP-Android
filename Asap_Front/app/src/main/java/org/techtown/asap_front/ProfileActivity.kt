package org.techtown.asap_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.profile_activity.*
import org.techtown.asap_front.`interface`.RecommService
import org.techtown.asap_front.data_object.Job
import org.techtown.asap_front.data_object.PostResult
import org.techtown.asap_front.data_object.RecommBody
import org.techtown.asap_front.data_object.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        var userId = intent.getIntExtra("userId", 0)

        // 디비에 있는 추천수, 경력, 자기소개 내용 가져오기
        var related_userId = intent.getIntExtra("related_user_id", 0)
        Log.d("profileUserId", userId.toString())
        Log.d("profileRelated", related_userId.toString())
        var nick = intent.getStringExtra("nick")
        Log.d("profileNick", nick!!)
        var intro = intent.getStringExtra("introduction")
        var jobs = intent.getParcelableArrayListExtra<Job>("jobs")
        var recomms_cnt = intent.getIntExtra("recomms_cnt", 0)
        var related_user = intent.getParcelableExtra<User>("related_user")

        nickProfile.text = nick
        recommNum.text = recomms_cnt.toString()
        if(related_user?.gender == 0) {
            sProfile.text = "여성"
        } else {
            sProfile.text = "남성"
        }
        ageProfile.text = related_user?.age.toString()

        var jobText = ""
        if(jobs != null) {
            for(i in jobs.indices) {
                jobText += jobs.get(i).job_name
                if(i <= (jobs.size-2)) {
                    jobText += ", "
                }
            }
        }

        jobProfile.text = jobText

        introduceProfile.text = intro


        // 가져온 추천수 개수를 recommNum.text에 할당
        var retrofit = Retrofit.Builder()
            .baseUrl("https://asap-ds.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var recommService = retrofit.create(RecommService::class.java)

        if (userId == related_userId?.toInt()) {
            recommBtn.setEnabled(false)
        }

        recommBtn.setOnClickListener {
            val data = RecommBody(userId, related_userId)
            recommService.recommend(data).enqueue(object : Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    Log.d("log",response.toString())
                    Log.d("log", response.body().toString())
                    val numText = recommNum.text.toString()
                    var num = numText.toInt()
                    num += 1
                    recommNum.text = num.toString()
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("log",t.message.toString())
                    Log.d("log","fail")
                }

            })

        }
    }
}