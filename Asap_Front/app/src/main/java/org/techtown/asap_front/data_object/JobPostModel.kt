package org.techtown.asap_front.data_object

import java.util.ArrayList

data class JobPostModel(
    val profile: Int,
    val title: String,
    val jobs: ArrayList<Int>,
    val start_date: String,
    val end_date: String,
    val start_time: String,
    val end_time: String,
    val content: String
)
