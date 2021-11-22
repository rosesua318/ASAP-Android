package org.techtown.asap_front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.emp_post_activity.*
import org.techtown.asap_front.`interface`.CommentService
import org.techtown.asap_front.`interface`.EmpPostListService
import org.techtown.asap_front.`interface`.JobService
import org.techtown.asap_front.`interface`.ProfileService
import org.techtown.asap_front.data_object.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import java.util.HashMap

class EmpPostActivity : AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInteface
    var profile: Profile? = null
    var comment1: Comment_1? = null
    private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emp_post_activity)
        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        var retrofit = Retrofit.Builder()
            .baseUrl("https://asap-ds.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var profileService = retrofit.create(ProfileService::class.java)
        var commentService = retrofit.create(CommentService::class.java)
        var act = this

        // 디비에서 가져와서 해당 내용 텍스트뷰들에 할당
        // 게시물번호를 이용하여 해당하는 댓글만 디비에서 가져와서 리스트에 할당 -> 어댑터로 구성

        recyclerView = findViewById(R.id.recyclerView_emp)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        val adapter = Comment1_Adapter()
        val id = 1 // 임시 id, 연결되면 로그인에서 아이디 가져오기
/*
        val call = retrofitInterface.executeComment2(id)
        call!!.enqueue(object : Callback<Comment_1?> {
            override fun onResponse(call: Call<Comment_1?>, response: Response<Comment_1?>) {
                comment1 = response.body()
                println(response.body())
                adapter.items.add(Comment_1(comment1!!.id,comment1!!.content,comment1!!.created_at,comment1!!.is_anon,comment1!!.post,comment1!!.profile))
                recyclerView.adapter = adapter
                // 확인 차 출력
                println("응답코드 : " + response.code().toString()+response.message())
            }

            override fun onFailure(call: Call<Comment_1?>, t: Throwable) {
                Toast.makeText(this@EmpPostActivity, t.message,
                        Toast.LENGTH_LONG).show()
            }
        })
*/



        val postId = intent.getIntExtra("postId", 0)
        val profId = intent.getIntExtra("profId", 0)
        var allJob = HashMap<Int, String>()

        // 만약 글작성자라면 비밀 댓글 체크박스 비활성화
        val userId = intent.getStringExtra("userId")?.toInt()
        if(profId == userId) {
            empSecret.setEnabled(false)
        }

        var jobService = retrofit.create(JobService::class.java)

        jobService.getJobs().enqueue(object: Callback<List<Job>> {
            override fun onResponse(call: Call<List<Job>>, response: Response<List<Job>>) {
                var jobs = response.body()

                if(jobs != null) {
                    for(i in jobs.indices) {
                        allJob.put(jobs.get(i).id, jobs.get(i).job_name)
                    }
                }
            }

            override fun onFailure(call: Call<List<Job>>, t: Throwable) {
                Log.d("log",t.message.toString())
                Log.d("log","fail")
            }

        })

        var empPostListService = retrofit.create(EmpPostListService::class.java)
        empPostListService.getPost(postId).enqueue(object : Callback<EmpPost> {
            override fun onResponse(call: Call<EmpPost>, response: Response<EmpPost>) {
                val body = response.body()
                postEmpNick.text = body?.profile?.nickname
                postEmpTime.text = body?.created_at!!.substring(0, body?.created_at!!.indexOf("."))
                postEmpTitle.text = "글제목 : " + body?.title
                var t = ""
                for(i in body?.jobs!!.indices) {
                    if(allJob.containsKey(body?.jobs[i])) {
                        if ( i != body?.jobs.size - 1) {
                            t += allJob.get(body.jobs[i]) + ","
                            Log.d("t text", t)
                        } else {
                            t += allJob.get(body.jobs[i])
                        }
                    }
                }
                postEmpJob.text = "직종 : " + t
                postEmpMoney.text = "급여 : " + body?.hourly_pay.toString()
                postEmpDate.text = "근무 일정 : " + body?.start_date.toString() + "~" + body?.end_date.toString()
                postEmpDuringTime.text = "근무 시간 : " + body?.start_time.toString() + "~" + body?.end_time.toString()
                postEmpDetail.text = "[상세 내용]\n" + body?.content
            }

            override fun onFailure(call: Call<EmpPost>, t: Throwable) {
                Log.d("log",t.message.toString())
            }

        })

        postEmpNick.setOnClickListener {
            profileService.getProfile(profId).enqueue(object: Callback<Profile> {
                override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                    profile = response.body()

                    val intent = Intent(act, ProfileActivity::class.java)
                    intent.putExtra("realted_user_id", profile?.related_user_id)
                    intent.putExtra("nick", profile?.nickname)
                    intent.putExtra("introduction", profile?.introduction)
                    intent.putParcelableArrayListExtra("jobs", profile?.jobs)
                    intent.putExtra("recomms_cnt", profile?.recomms_cnt)
                    intent.putExtra("related_user", profile?.related_user)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<Profile>, t: Throwable) {
                    Log.d("log",t.message.toString())
                }

            })

        }



        var b = false
        empSecret.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) { b = true }
            else {
                b = false
            }
        }

        empCommentBtn.setOnClickListener {
            if(b) {
                // 비밀 댓글로 구분하여 디비에 저장
            } else {
                // 공개 댓글로 구분하여 디비에 저장
            }
            val comment = CommentBody(postId, profId, empEditComment.text.toString(), b)
            commentService.addCommentEmp(comment).enqueue(object: Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    Log.d("log",response.toString())
                    Log.d("log", response.body().toString())
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("log",t.message.toString())
                }

            })
            empEditComment.setText("")
        }
    }
}