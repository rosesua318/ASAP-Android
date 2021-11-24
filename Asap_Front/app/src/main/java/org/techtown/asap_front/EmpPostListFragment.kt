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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.emp_post_list_fragment.*
import kotlinx.android.synthetic.main.emp_post_list_fragment.view.*
import org.techtown.asap_front.`interface`.EmpPostListService
import org.techtown.asap_front.`interface`.JobService
import org.techtown.asap_front.data_object.EmpPost
import org.techtown.asap_front.data_object.Job
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
 * Use the [EmpPostListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmpPostListFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var mContext: Context

    private lateinit var userId: String
    private var nickname: String = ""

    private var adapter : RecyclerEmpPostAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userId = it.getString("userId") as String
            nickname = it.getString("nickname") as String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.emp_post_list_fragment, container, false)
        val sortingSpinner=view.eSortingSpinner

        loadData()
        
        view.eWriteBtn.setOnClickListener { //구인작성 액티비티로 이동
            //?
            activity?.let{
                val intent = Intent(it, EmpPostingActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }
        }
        
        sortingSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val data = resources.getStringArray(R.array.emppost_sort)

                //선택된 정렬기준 전송
                //재정렬된 포스트 데이터 가져와
                //리사이클러뷰 재출력
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        return view
    }

    private fun setAdapter(postList: ArrayList<EmpPost>, allJob: HashMap<Int, String>, userId: String, nickname: String){
        adapter = RecyclerEmpPostAdapter(postList, requireActivity(), allJob, userId, nickname)
        emp_recyclerview.adapter = adapter
        emp_recyclerview.layoutManager = LinearLayoutManager(requireActivity())
    }
    private fun loadData(){
        var retrofit = Retrofit.Builder()
                .baseUrl("https://asap-ds.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var EmpPostListService = retrofit.create(EmpPostListService::class.java)
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


        EmpPostListService.getAllPosts().enqueue(object : Callback<ArrayList<EmpPost>> {
            override fun onResponse(call: Call<ArrayList<EmpPost>>, response: Response<ArrayList<EmpPost>>) {
                if (response.isSuccessful) {
                    val body = response.body()

                    body?.let {
                        setAdapter(body, allJob, userId, nickname)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<EmpPost>>, t: Throwable) {
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
        Log.d("프래그먼트", "onAttached")
        if(context is EmpPostingActivity)
            mContext=context as EmpPostingActivity
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
         * @return A new instance of fragment EmpPostListFragment.
         */
        // TODO: Rename and change types and number of parameters
        //@JvmStatic
        fun newInstance(): EmpPostListFragment{
            return EmpPostListFragment()
        }
    }
}