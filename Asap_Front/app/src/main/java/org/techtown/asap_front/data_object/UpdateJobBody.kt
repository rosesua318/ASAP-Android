package org.techtown.asap_front.data_object

data class UpdateJobBody (
        val jobs_list : ArrayList<Int>,
        val related_user_id : Int
        )