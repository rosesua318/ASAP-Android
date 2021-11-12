package org.techtown.asap_front

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.mypage_fragment.*
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