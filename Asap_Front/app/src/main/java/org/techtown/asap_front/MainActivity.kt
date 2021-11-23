package org.techtown.asap_front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.techtown.asap_front.data_object.Comment1_Adapter


class MainActivity() : AppCompatActivity(){
    private lateinit var recyclerView1 : RecyclerView
    private lateinit var recyclerView2 : RecyclerView

    lateinit var jobPostListFragment: JobPostListFragment
    lateinit var empPostListFragment: EmpPostListFragment
    lateinit var myPageFragment: MyPageFragment

    private var user_id = ""
    private var nickname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*
        recyclerView1 = findViewById(R.id.recyclerView_job)
        recyclerView1.layoutManager = GridLayoutManager(this, 1)

        recyclerView2 = findViewById(R.id.recyclerView_emp)
        recyclerView2.layoutManager = GridLayoutManager(this, 1)


         */
        user_id = intent.getStringExtra("user_id").toString()
        var login_ID = intent.getStringExtra("login_ID")
        nickname = intent.getStringExtra("nickname").toString()

        //val intent1 = Intent(this, JobPostingActivity::class.java)
        //intent1.putExtra("userId", user_id)
        //startActivity(intent1)

        nav.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)

        //기본화면
        //jobPostListFragment = JobPostListFragment.newInstance()
        //supportFragmentManager.beginTransaction().add(R.id.fragment_frame, jobPostListFragment).commit()
        with(supportFragmentManager.beginTransaction()) {
            val fragment1 = JobPostListFragment()
            val bundle = Bundle()
            bundle.putString("userId", user_id)
            bundle.putString("nickname", nickname)
            fragment1.arguments = bundle
            replace(R.id.fragment_frame, fragment1)
            commit()
        }
    }

    //네비게이션바 이벤트 -> 프레그먼트 이동
    private val onBottomNavItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.job ->{
                with(supportFragmentManager.beginTransaction()) {
                    val fragment1 = JobPostListFragment()
                    val bundle = Bundle()
                    bundle.putString("userId", user_id)
                    bundle.putString("nickname", nickname)
                    fragment1.arguments = bundle
                    replace(R.id.fragment_frame, fragment1)
                    commit()
                }
            }
            R.id.emp ->{
                with(supportFragmentManager.beginTransaction()) {
                    val fragment2 = EmpPostListFragment()
                    val bundle = Bundle()
                    bundle.putString("userId", user_id)
                    bundle.putString("nickname", nickname)
                    fragment2.arguments = bundle
                    replace(R.id.fragment_frame, fragment2)
                    commit()
                }
            }
            R.id.my ->{
                with(supportFragmentManager.beginTransaction()) {
                    val fragment3 = MyPageFragment()
                    val bundle = Bundle()
                    bundle.putString("userId", user_id)
                    fragment3.arguments = bundle
                    replace(R.id.fragment_frame, fragment3)
                    commit()
                }
            }
        }
        true
    }

}