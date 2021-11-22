package org.techtown.asap_front

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import kotlinx.android.synthetic.main.emp_post_list_fragment.*
import kotlinx.android.synthetic.main.emp_post_list_fragment.view.*
import org.techtown.asap_front.`interface`.EmpPostListService
import org.techtown.asap_front.data_object.EmpPost
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
        val view = inflater.inflate(R.layout.emp_post_list_fragment, container, false)

        val sortingSpinner=view.eSortingSpinner

        var retrofit = Retrofit.Builder()
                .baseUrl("https://asap-ds.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var EmpPostListService = retrofit.create(EmpPostListService::class.java)

        EmpPostListService.getAllPosts().enqueue(object : Callback<EmpPost> {
            override fun onResponse(call: Call<EmpPost>, response: Response<EmpPost>) {
                if(response.isSuccessful){
                    val body = response.body()

                    body?.let{
                        //setAdapter(it.)
                    }
                }
            }

            override fun onFailure(call: Call<EmpPost>, t: Throwable) {
                Log.d("log",t.message.toString())
                Log.d("log","fail")
            }
        })

        //post 데이터 받아오기
        var list = ArrayList<EmpPost>()
        //요약정보 리사이클러뷰에 담기
        //리사이클러뷰 어댑터 연결
        val adapter = RecyclerEmpPostAdapter(list)
        view.emp_recyclerview.adapter = adapter
        
        view.eWriteBtn.setOnClickListener { //구인작성 액티비티로 이동
            //?
            activity?.let{
                val intent = Intent(it, EmpPostingActivity::class.java)
                startActivity(intent)
            }
        }

        sortingSpinner.setSelection(1)
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