package org.techtown.asap_front.`interface`

import org.techtown.asap_front.data_object.JobPost
import org.techtown.asap_front.data_object.RecommBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path

interface JobPostList {
    @FormUrlEncoded
    @GET("/search_job/posts/")
    fun getPost(
        @Body jsonParams: JobPost
    )
}