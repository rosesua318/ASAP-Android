package org.techtown.asap_front

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.emp_post_activity.*
import kotlinx.android.synthetic.main.job_post_activity.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    var comment1: ArrayList<Comment_1>? = null
    private lateinit var recyclerView1 : RecyclerView
    private lateinit var adapter1 : Comment1_Adapter
    var commentArray = ArrayList<Comment_1>()
    var indexNum : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_post_activity)
        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api
        val userId = intent.getStringExtra("userId")

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

        // 아래는 테스트 코드
        val profId = intent.getIntExtra("profId", 0)
        val adapter1 = Comment1_Adapter(userId!!.toInt(), profId)
        val postId = intent.getIntExtra("postId", 0)
        val call = retrofitInterface.executeComment2(postId)

        call!!.enqueue(object : Callback<ArrayList<Comment_1>> {
            override fun onResponse(call: Call<ArrayList<Comment_1>>, response: Response<ArrayList<Comment_1>>) {
                comment1 = response.body()

                for(i in comment1!!.indices){
                    indexNum += 1
                    commentArray.add(Comment_1(comment1!![i].id,comment1!![i].content,"",comment1!![i].is_anon,comment1!![i].post,comment1!![i].profile))
                }
                // 확인 차 출력
                println("indexNum 값 : "+indexNum)
                println("응답코드 : " + response.code().toString()+response.message())
            }

            override fun onFailure(call: Call<ArrayList<Comment_1>>, t: Throwable) {
                Toast.makeText(this@JobPostActivity, t.message,
                        Toast.LENGTH_LONG).show()
            }
        })

        GlobalScope.launch {    // 2
            delay(18000)
            Log.d(ContentValues.TAG, "done something in Coroutine")   // 3

            for(i in 0 .. (indexNum-1)){
                profileService.getProfile(comment1!![i].profile).enqueue(object: Callback<Profile> {
                    override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                        profile = response.body()
                        println(response.body())
                        commentArray!![i].created_at = profile!!.nickname
                        print(profile!!.nickname)
                        adapter1.items.add(commentArray!![i])
                        recyclerView1.adapter = adapter1
                    }

                    override fun onFailure(call: Call<Profile>, t: Throwable) { }
                })
            }
        }




        val nickname = intent.getStringExtra("nickname")
        var allJob = HashMap<Int, String>()

        // 만약 글작성자라면 비밀 댓글 체크박스 비활성화

        Log.d("JobPostUserId", userId!!)
        if(profId == userId?.toInt()) {
            jobSecret.setEnabled(false)
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
            val comment = CommentBody(postId, userId!!.toInt(), jobEditComment.text.toString(), b)
            commentService.addCommentJob(comment).enqueue(object: Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    Log.d("log",response.toString())
                    Log.d("log", response.body().toString())
                    try {
                        val Intent = getIntent()
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    } catch(e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("log",t.message.toString())
                }

            })
            jobEditComment.setText("")
        }
    }
}