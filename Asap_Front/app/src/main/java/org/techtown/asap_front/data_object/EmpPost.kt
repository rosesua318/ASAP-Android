package org.techtown.asap_front.data_object

import java.time.ZonedDateTime
import java.util.*

data class EmpPost(
        val id: Int,
        val profile: Profile,
        val title: String,
        val jobs: ArrayList<Job>,
        val hourly_pay: Int,
        val start_date: String, //Date? String?
        val end_date: String, //Date? String?
        val start_time: String, //Time? String?
        val end_time: String, //Time? String?
        val created_at: ZonedDateTime,
        val content: String
)
