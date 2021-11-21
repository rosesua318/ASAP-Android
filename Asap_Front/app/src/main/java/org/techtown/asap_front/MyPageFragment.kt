package org.techtown.asap_front

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.mypage_fragment.*
import kotlinx.android.synthetic.main.mypage_fragment.view.*
import org.techtown.asap_front.`interface`.JobService
import org.techtown.asap_front.`interface`.ProfileService
import org.techtown.asap_front.`interface`.UpdateService
import org.techtown.asap_front.data_object.*
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

    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userId = it.getString("userId") as String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.mypage_fragment, container, false)

        val spinner = view.mypageSpinner

        var jobList = ArrayList<Int>()
        var allJob = HashMap<String, Int>()

        // 디비에 있는 닉네임, 경력, 자기소개 내용 가져와서 editNick.text 등에 할당

        var retrofit = Retrofit.Builder()
                .baseUrl("https://asap-ds.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var profileService = retrofit.create(ProfileService::class.java)
        var updateService = retrofit.create(UpdateService::class.java)
        var jobService = retrofit.create(JobService::class.java)

        jobService.getJobs().enqueue(object: Callback<List<Job>> {
            override fun onResponse(call: Call<List<Job>>, response: Response<List<Job>>) {
                var jobs = response.body()

                if(jobs != null) {
                    for(i in jobs.indices) {
                        allJob.put(jobs.get(i).job_name.toString(), jobs.get(i).id)
                    }
                }
            }

            override fun onFailure(call: Call<List<Job>>, t: Throwable) {
                Log.d("log",t.message.toString())
                Log.d("log","fail")
            }

        })

        profileService.getProfile(userId.toInt()).enqueue(object: Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                profile = response.body()

                editNick.setText(profile?.nickname.toString())
                Log.d("profile", profile?.nickname.toString())
                recommText.text = profile?.recomms_cnt.toString()
                if(profile?.related_user?.gender == 0) {
                    sText.text = "여성"
                } else {
                    sText.text = "남성"
                }
                ageText.text = profile?.related_user?.age.toString()

                var jobT = ""
                val jobs = profile?.jobs
                if(jobs != null) {
                    for(i in jobs.indices) {
                        jobT += jobs.get(i).job_name
                        jobList.add(jobs.get(i).id)
                        if(i <= (jobs.size-2)) {
                            jobT += ", "
                        }
                    }
                }
                jobText.text = jobT
                editIntroduce.setText(profile?.introduction.toString())
            }

            override fun onFailure(call: Call<Profile>, t: Throwable) {
                Log.d("log", t.printStackTrace().toString())
            }

        })



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val data = resources.getStringArray(R.array.job)
                val jText = jobText.text.toString()
                val token = jText.split(", ")

                var b = true
                for(t in token.indices) {
                    if(token[t].equals(data[p2]))
                        b = false
                }
                if(b) {
                    if(jobText.text.equals("")) {
                        jobText.text = data[p2].toString()
                    } else {
                        jobText.text = jText + ", " + data[p2].toString()
                    }

                    if(allJob.containsKey(data[p2].toString())) {
                        jobList.add(allJob.get(data[p2].toString())!!)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        view.updateBtn.setOnClickListener {
            // val edtNick = editNick.text 이렇게 변수들 할당해서
            // 디비에 갱신하는 함수로직에 매개변수들로 넣어서 갱신시켜줌

            val edtNick = editNick.text.toString()
            val edtIntro = editIntroduce.text.toString()
            Log.d("edtNick", edtNick)
            Log.d("edtIntro", edtIntro)


            jobList.sort()
            Log.d("jobList", jobList.toString())

            var updateJob = UpdateJobBody(jobList, userId.toInt())
            updateService.updateJob(updateJob).enqueue(object: Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    Log.d("log",response.toString())
                    Log.d("log", response.body().toString())

                    Toast.makeText(activity, "수정되었습니다", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("log",t.message.toString())
                    Log.d("log","fail2")
                }
            })

            var updateNickIntro = UpdateNickIntroBody(edtNick, edtIntro)
            updateService.updateNickIntro(userId.toInt(), updateNickIntro).enqueue(object: Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    if(response.code() == 200) {
                        Log.d("log",response.toString())
                        Log.d("log", response.body().toString())

                        Toast.makeText(activity, "수정되었습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("log", "실패")
                    }

                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("log",t.message.toString())
                    Log.d("log","fail1")
                }

            })
        }

        view.deleteJobBtn.setOnClickListener {
            jobText.text = ""
            jobList.clear()
        }

        view.logoutBtn.setOnClickListener {
            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                val f = requireActivity().supportFragmentManager
                f.beginTransaction().remove(this).commit()
                f.popBackStack()
                startActivity(intent)

            }

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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