package org.techtown.asap_front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity() : AppCompatActivity(){

    lateinit var jobPostListFragment: JobPostListFragment
    lateinit var empPostListFragment: EmpPostListFragment
    lateinit var myPageFragment: MyPageFragment

    private var user_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        user_id = intent.getStringExtra("user_id").toString()
        var login_ID = intent.getStringExtra("login_ID")
        var nickname = intent.getStringExtra("nickname")

        nav.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)

        //기본화면
        jobPostListFragment = JobPostListFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragment_frame, jobPostListFragment).commit()
    }

    //네비게이션바 이벤트 -> 프레그먼트 이동
    private val onBottomNavItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.job ->{
                jobPostListFragment = JobPostListFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, jobPostListFragment).commit()
            }
            R.id.emp ->{
                empPostListFragment = EmpPostListFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, empPostListFragment).commit()
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