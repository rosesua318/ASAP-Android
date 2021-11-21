package org.techtown.asap_front

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.emp_post_list_fragment.*
import kotlinx.android.synthetic.main.job_post_list_fragment.*

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
        return inflater.inflate(R.layout.job_post_list_fragment, container, false)

        val sortingSpinner=jSortingSpinner

        //post 데이터 받아오기
        //기본정보 리사이클러뷰에 담아 보이기

        jWriteBtn.setOnClickListener {//구직작성 액티비티로 이동
            //?
            activity?.let{
                val intent = Intent(it, EmpPostingActivity::class.java)
                startActivity(intent)
            }
        }

        sortingSpinner.setOnItemClickListener{ adapterView, view, i, l ->//스피너에서 정렬기준 선택되면
            val data = resources.getStringArray(R.array.sort)
            //선택된 정렬기준 전송
            //재정렬된 포스트 데이터 가져와
            //리사이클러뷰 출력
        }
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

