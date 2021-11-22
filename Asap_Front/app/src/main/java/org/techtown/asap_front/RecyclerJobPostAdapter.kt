package org.techtown.asap_front

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.job_post_item.view.*
import org.techtown.asap_front.`interface`.JobService
import org.techtown.asap_front.data_object.Job
import org.techtown.asap_front.data_object.JobPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.HashMap

class RecyclerJobPostAdapter(private var items: ArrayList<JobPost>, val context: Context, val allJob: HashMap<Int, String>, val userId: String): RecyclerView.Adapter<RecyclerJobPostAdapter.ViewHolder>(){
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: RecyclerJobPostAdapter.ViewHolder, position: Int){
        val item = items[position]
        holder.bind(item, context, allJob, userId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerJobPostAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.job_post_item, parent, false)

        return RecyclerJobPostAdapter.ViewHolder(inflatedView)
    }

    fun sortItem(it: ArrayList<JobPost>) {
        items.clear()
        for(i in it) {
            items.add(i)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view: View = v

        fun bind(item: JobPost, context: Context, allJob: HashMap<Int, String>, userId: String){
            view.title.text = "글제목: "+item.title
            //데이터로 받아온 경력(jobs) ','로 연결해서 string 타입으로 변환
            var t = ""
            for(i in item.jobs.indices) {
                if(allJob.containsKey(item.jobs[i])) {
                    if ( i != item.jobs.size - 1) {
                        t += allJob.get(item.jobs[i]) + ","
                        Log.d("t text", t)
                    } else {
                        t += allJob.get(item.jobs[i])
                    }
                }
            }

            view.jobs.text = "경력: " + t
            view.date.text = "근무가능기간: "+item.start_date+"~"+item.end_date
            view.time.text = "근무가능시간: "+item.start_time+"~"+item.end_time

            Log.d("userId", userId)
            view.setOnClickListener {
                Intent(context, JobPostActivity::class.java).apply {
                    putExtra("postId", item.id)
                    putExtra("profId", item.profile.related_user_id)
                    putExtra("userId", userId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                }.run { context.startActivity(this)}

            }
        }
    }


}