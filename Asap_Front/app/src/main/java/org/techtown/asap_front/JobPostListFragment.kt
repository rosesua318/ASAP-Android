package org.techtown.asap_front


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.job_post_list_fragment.*
import kotlinx.android.synthetic.main.job_post_list_fragment.view.*
import org.techtown.asap_front.`interface`.JobPostListService
import org.techtown.asap_front.`interface`.JobService
import org.techtown.asap_front.`interface`.SortService
import org.techtown.asap_front.data_object.Job
import org.techtown.asap_front.data_object.JobPost
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
 * Use the [JobPostListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JobPostListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var mContext: Context

    private lateinit var userId: String
    private var nickname: String = ""

    private var adapter : RecyclerJobPostAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userId = it.getString("userId") as String
            nickname = it.getString("nickname") as String
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.job_post_list_fragment, container, false)
        Log.d("JobUserId", userId)
        val sortingSpinner = view.jSortingSpinner

        loadData()

        view.jWriteBtn.setOnClickListener {
            activity?.let {
                val intent = Intent(it, JobPostingActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }
        }

        var retrofit = Retrofit.Builder()
            .baseUrl("https://asap-ds.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var sortService = retrofit.create(SortService::class.java)
        var allJob = HashMap<Int, String>()
        var jobService = retrofit.create(JobService::class.java)

        jobService.getJobs().enqueue(object : Callback<List<Job>> {
            override fun onResponse(call: Call<List<Job>>, response: Response<List<Job>>) {
                var jobs = response.body()

                if (jobs != null) {
                    for (i in jobs.indices) {
                        allJob.put(jobs.get(i).id, jobs.get(i).job_name)
                    }
                }
            }

            override fun onFailure(call: Call<List<Job>>, t: Throwable) {
                Log.d("log", t.message.toString())
                Log.d("log", "fail")
            }

        })


        sortingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val data = resources.getStringArray(R.array.jobpost_sort)

                //선택된 정렬기준 전송
                Log.d("sortingspinner", data[position].toString())
                if (data[position].toString().equals("근무시작시간순")) {
                    sortService.getEarlyStartJob().enqueue(object : Callback<ArrayList<JobPost>> {
                        override fun onResponse(
                            call: Call<ArrayList<JobPost>>,
                            response: Response<ArrayList<JobPost>>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()
                                body?.let {
                                    //setAdapter(body, allJob, userId)
                                    Log.d("sort body", body.toString())
                                    adapter?.sortItem(body)
                                }
                            }
                        }

                        override fun onFailure(call: Call<ArrayList<JobPost>>, t: Throwable) {
                            Log.d("log", t.message.toString())
                        }

                    })
                }
                //재정렬된 포스트 데이터 가져와
                //리사이클러뷰 재출력
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        return view
    }


    private fun setAdapter(postList: ArrayList<JobPost>, allJob: HashMap<Int, String>, userId: String, nickname: String){
        adapter = RecyclerJobPostAdapter(postList, requireActivity(), allJob, userId, nickname)
        job_recyclerview.adapter = adapter
        job_recyclerview.layoutManager = LinearLayoutManager(requireActivity())
    }
    private fun loadData(){

        var retrofit = Retrofit.Builder()
            .baseUrl("https://asap-ds.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var JobPostListService = retrofit.create(JobPostListService::class.java)

        var allJob = HashMap<Int, String>()
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

        JobPostListService.getAllPosts().enqueue(object : Callback<ArrayList<JobPost>>{
            override fun onResponse(call: Call<ArrayList<JobPost>>, response: Response<ArrayList<JobPost>>) {
                if(response.isSuccessful){
                    val body = response.body()
                    body?.let{

                        setAdapter(body, allJob, userId, nickname)

                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<JobPost>>, t: Throwable) {
                Log.d("log",t.message.toString())
                Log.d("log","fail")
            }
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("프래그먼트","onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("프래그먼트","onActivityCreated")
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        Log.d("프래그먼트","onAttached")
        if(context is JobPostingActivity)
            mContext=context as JobPostingActivity
        else if(context is MainActivity)
            mContext=context as MainActivity
    }
    override fun onPause() {
        super.onPause()
        Log.d("프래그먼트","onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("프래그먼트","onResume")
        //loadData()
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("프래그먼트","onViewStateRestored")
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment JobPostListFragment.
         */
        // TODO: Rename and change types and number of parameters
        //@JvmStatic
        fun newInstance(): JobPostListFragment {
            return JobPostListFragment()
        }
    }
}

