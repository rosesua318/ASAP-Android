package org.techtown.asap_front.data_object

data class Profile (
    val related_user_id: Int,
    val nickname: String,
    val introduction: String,
    val jobs: ArrayList<Job>,
    val recomms_cnt: Int,
    val related_user: User
        )