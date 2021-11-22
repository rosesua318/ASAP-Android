package org.techtown.asap_front

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.emp_post_item.view.*
import org.techtown.asap_front.data_object.EmpPost
import java.util.*

class RecyclerEmpPostAdapter(private val items: ArrayList<EmpPost>, val context: Context): RecyclerView.Adapter<RecyclerEmpPostAdapter.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.emp_post_item, parent, false)
        return RecyclerEmpPostAdapter.ViewHolder(inflatedView)
    }
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view: View = v
        fun bind(item: EmpPost, context: Context){
            view.title.text = "글제목: "+item.title
            //데이터로 받아온 경력(jobs) ','로 연결해서 string 타입으로 변환
            view.jobs.text = "경력: " +"item.jobs"
            view.hourly_pay.text = item.hourly_pay.toString()
            view.date.text = "근무가능기간: "+item.start_date+"~"+item.end_date
            view.time.text = "근무가능시간: "+item.start_time+"~"+item.end_time
        }
    }
}