package org.techtown.asap_front

import org.techtown.asap_front.data_object.related_user

data class LoginResult(
        var user_id : Int,
        var login_ID: String,
        var nickname: String)

data class JoinSend(
        var nickname: String,
        var introduction: String,
        var jobs: String,
        var related_user: related_user
)

data class Comment_1 (
        val id:Int, // 댓글 아이디
        val content:String, // 댓글 내용
        val created_at:String, // 댓글 작성 시간
        val is_anon:Boolean, // 익명 여부
        val post:Int, // 게시글 아이디
        val profile: Int // 작성자 아이디
)