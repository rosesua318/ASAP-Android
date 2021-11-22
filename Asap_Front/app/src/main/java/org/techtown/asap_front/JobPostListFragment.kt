package org.techtown.asap_front

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.job_post_list_fragment.*
import kotlinx.android.synthetic.main.job_post_list_fragment.view.*
import org.techtown.asap_front.`interface`.JobPostListService
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.job_post_list_fragment, container, false)

        val sortingSpinner=view.jSortingSpinner

        loadData()

        view.jWriteBtn.setOnClickListener {//구직작성 액티비티로 이동
            //?
            activity?.let{
                val intent = Intent(it, JobPostingActivity::class.java)
                startActivity(intent)
            }
        }

        sortingSpinner.setSelection(1)
        sortingSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val data = resources.getStringArray(R.array.jobpost_sort)

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

    private fun setAdapter(postList: ArrayList<JobPost>){
        val adapter = RecyclerJobPostAdapter(postList, requireActivity())
        job_recyclerview.adapter = adapter
        job_recyclerview.layoutManager = LinearLayoutManager(requireActivity())
    }
    private fun loadData(){
        var retrofit = Retrofit.Builder()
                .baseUrl("https://asap-ds.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var JobPostListService = retrofit.create(JobPostListService::class.java)

        JobPostListService.getAllPosts().enqueue(object : Callback<JobPost>{
            override fun onResponse(call: Call<JobPost>, response: Response<JobPost>) {
                if(response.isSuccessful){
                    val body = response.body()
                    body?.let{
                        setAdapter(it.)
                    }
                }
            }

            override fun onFailure(call: Call<JobPost>, t: Throwable) {
                Log.d("log",t.message.toString())
                Log.d("log","fail")
            }
        })

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

