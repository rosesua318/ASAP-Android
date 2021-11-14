package org.techtown.asap_front

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.mypage_fragment.*
import org.techtown.asap_front.`interface`.ProfileService
import org.techtown.asap_front.data_object.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.mypage_fragment, container, false)

        val spinner = mypageSpinner

        // 디비에 있는 닉네임, 경력, 자기소개 내용 가져와서 editNick.text 등에 할당
        val userId = 1 // 로그인 성공에서부터 가져옴(나중에 구현)
        var retrofit = Retrofit.Builder()
            .baseUrl("http://asap-ds.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var profileService = retrofit.create(ProfileService::class.java)

        profileService.getProfile(userId).enqueue(object: Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                profile = response.body()

                editNick.setText(profile?.nickname)
                recommText.text = profile?.recomms_cnt.toString()
                sText.text = profile?.related_user?.gender.toString()
                ageText.text = profile?.related_user?.age.toString()

                var jobT = ""
                val jobs = profile?.jobs
                if(jobs != null) {
                    for(i in jobs.indices) {
                        jobT += jobs.get(i).job_name
                        if(i <= (jobs.size-2)) {
                            jobT += ", "
                        }
                    }
                }
                jobText.text = jobT
                editIntroduce.setText(profile?.introduction)
            }

            override fun onFailure(call: Call<Profile>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        spinner.setOnItemClickListener { adapterView, view, i, l ->
            val data = resources.getStringArray(R.array.job)
            val jText = jobText.text.toString()
            val token = jText.split(", ")

            var b = true
            for(t in token.indices) {
                if(token[t].equals(data[i]))
                    b = false
            }
            if(b) {
                jobText.text = jText + ", " + data[i].toString()
            }
        }

        updateBtn.setOnClickListener {
            // val edtNick = editNick.text 이렇게 변수들 할당해서
            // 디비에 갱신하는 함수로직에 매개변수들로 넣어서 갱신시켜줌

            val edtNick = editNick.text
            val jobText = jobText.text
            val edtIntro = editIntroduce.text
        }

        logoutBtn.setOnClickListener {
            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                val f = requireActivity().supportFragmentManager
                f.beginTransaction().remove(this).commit()
                f.popBackStack()
            }

        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        //@JvmStatic
        fun newInstance(): MyPageFragment{
            return MyPageFragment()
        }
    }
}