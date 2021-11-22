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
import org.techtown.asap_front.`interface`.ProfileService
import org.techtown.asap_front.data_object.Comment1_Adapter
import org.techtown.asap_front.data_object.CommentBody
import org.techtown.asap_front.data_object.PostResult
import org.techtown.asap_front.data_object.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        // 아래는 테스트 코드
        adapter1 = Comment1_Adapter()

        val call = retrofitInterface.executeComment1(id)

        call!!.enqueue(object : Callback<ArrayList<Comment_1>> {
            override fun onResponse(call: Call<ArrayList<Comment_1>>, response: Response<ArrayList<Comment_1>>) {
                comment1 = response.body()

                for(i in comment1!!.indices){
                    indexNum += 1
                    commentArray.add(Comment_1(comment1!![i].id,comment1!![i].content,"",comment1!![i].is_anon,comment1!![i].post,comment1!![i].profile))
                }
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

        val postId = 1
        val profId = 10 // postId를 넣으면 profId db에서 가져오는 로직으로 구함

        // 만약 글작성자라면 비밀 댓글 체크박스 비활성화
        val userId = 2 // 로그인에서부터 가져옴(나중에 로그인 기능 완성되면 구현)
        if(profId == userId) {
            empSecret.setEnabled(false)
        }

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