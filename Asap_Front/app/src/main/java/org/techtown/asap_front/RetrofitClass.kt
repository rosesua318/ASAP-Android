package org.techtown.asap_front

import org.techtown.asap_front.data_object.related_user

data class LoginResult(
        var user_id : Int,
        var login_ID: String,
        var nickname: String)

data class JoinSend(
        var nickname: String,
        var introduction: String,
        var jobs: Int,
        var related_user: related_user
)