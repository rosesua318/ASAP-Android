package org.techtown.asap_front.data_object


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comment_item.view.*
import org.techtown.asap_front.Comment_1
import org.techtown.asap_front.ProfileActivity
import org.techtown.asap_front.R

// 어댑터는 보여지는 View와 그 View에 올릴 Data를 연결하는 일종의 Bridge
// 뷰홀더 데이터 효율적으로 관리
class Comment1_Adapter(var postId:Int) : RecyclerView.Adapter<Comment1_Adapter.ViewHolder>() {
    var items = ArrayList<Comment_1>()

    //뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // 뷰홀더와 아이템 연결
        val item = items[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        if (items != null) {
            return items.size
        } else {
            return 0
        }
    }

    // 뷰홀더 클래스 생성, 보여지는 아이템들 관리
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init{
            itemView.setOnClickListener{
                // 프로필로 넘어가기
            }
        }
        fun setItem(item: Comment_1){
            if(item.is_anon){
                if(postId != item.profile){
                    itemView.commentNick.text = "비밀 댓글"
                    itemView.commentContent.text = "게시글 작성자만 읽을 수 있습니다."
                }
            }
            else{
                itemView.commentNick.text = item.created_at
                itemView.commentContent.text = item.content
            }
        }
    }
}