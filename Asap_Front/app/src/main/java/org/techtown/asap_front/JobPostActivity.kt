package org.techtown.asap_front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.emp_post_activity.*
import kotlinx.android.synthetic.main.job_post_activity.*
import org.techtown.asap_front.`interface`.CommentService
import org.techtown.asap_front.`interface`.JobPostListService
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

class JobPostActivity : AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInteface
    var profile: Profile? = null
    var comment1: Comment_1? = null
    private lateinit var recyclerView1 : RecyclerView
    private lateinit var adapter1 : Comment1_Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_post_activity)
        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api
        val id = 1 // 임시 id, 연결되면 로그인에서 아이디 가져오기

        var retrofit = Retrofit.Builder()
            .baseUrl("https://asap-ds.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var profileService = retrofit.create(ProfileService::class.java)
        var commentService = retrofit.create(CommentService::class.java)
        var act = this

        recyclerView1 = findViewById(R.id.recyclerView_job)
        recyclerView1.layoutManager = GridLayoutManager(this,1)
        // 디비에서 가져와서 해당 내용 텍스트뷰들에 할당
        // 게시물번호를 이용하여 해당하는 댓글만 디비에서 가져와서 리스트에 할당 -> 어댑터로 구성
        /*
        // 아래는 테스트 코드
        adapter1 = Comment1_Adapter()

        adapter1.items.add(Comment_1("댓글 내용1",1))
        adapter1.items.add(Comment_1("댓글 내용2",2))
        adapter1.items.add(Comment_1("댓글 내용3",3))
        recyclerView1.adapter = adapter1
        println("어댑터 아이템 리스트 : "+adapter1.items[0]+", "+adapter1.items[1])
        */
        val call = retrofitInterface.executeComment1(id)
        call!!.enqueue(object : Callback<Comment_1?> {
            override fun onResponse(call: Call<Comment_1?>, response: Response<Comment_1?>) {
                comment1 = response.body()
                println("응답 출력 : "+response.code()+" , "+ response.body())
                //adapter.items.add(Comment_1(comment1!!.id,comment1!!.content,comment1!!.created_at,comment1!!.is_anon,comment1!!.post,comment1!!.profile))
                adapter1.items.add(Comment_1(id,comment1!!.content,comment1!!.created_at,true,1,1))
                recyclerView1.adapter = adapter1
                // 확인 차 출력
                println("응답코드 : " + response.code().toString()+response.message())
            }

            override fun onFailure(call: Call<Comment_1?>, t: Throwable) {
                Toast.makeText(this@JobPostActivity, t.message,
                        Toast.LENGTH_LONG).show()
            }
        })

        val postId = intent.getIntExtra("postId", 0)
        val profId = intent.getIntExtra("profId", 0)
        var allJob = HashMap<Int, String>()

        // 만약 글작성자라면 비밀 댓글 체크박스 비활성화
        val userId = intent.getStringExtra("userId")
        Log.d("JobPostUserId", userId!!)
        if(profId == userId?.toInt()) {
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

        var jobPostListService = retrofit.create(JobPostListService::class.java)
        jobPostListService.getPost(postId).enqueue(object : Callback<JobPost> {
            override fun onResponse(call: Call<JobPost>, response: Response<JobPost>) {
                val body = response.body()
                postJobNick.text = body?.profile?.nickname
                postJobTime.text = body?.created_at!!.substring(0, body?.created_at!!.indexOf("."))
                postJobTitle.text = "글제목 : " + body?.title
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
                postJob.text = "경력 : " + t
                postJobDate.text = "근무 가능 기간 : " + body?.start_date.toString() + "~" + body?.end_date.toString()
                postJobDuringTime.text = "근무 가능 시간 : " + body?.start_time.toString() + "~" + body?.end_time.toString()
                postJobDetail.text = "[상세 내용]\n" + body?.content
            }

            override fun onFailure(call: Call<JobPost>, t: Throwable) {
                Log.d("log",t.message.toString())
            }

        })

        postJobNick.setOnClickListener {

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
                    Log.d("log", t.printStackTrace().toString())
                }

            })

        }

        var b = false
        jobSecret.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) { b = true }
            else {
                b = false
            }
        }

        jobCommentBtn.setOnClickListener {
            if(b) {

            } else {
                // 공개 댓글로 구분하여 디비에 저장
            }
            // 비밀 댓글로 구분하여 디비에 저장
            val comment = CommentBody(postId, profId, jobEditComment.text.toString(), b)
            commentService.addCommentJob(comment).enqueue(object: Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    Log.d("log",response.toString())
                    Log.d("log", response.body().toString())
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("log",t.message.toString())
                }

            })
            jobEditComment.setText("")
        }
    }
}