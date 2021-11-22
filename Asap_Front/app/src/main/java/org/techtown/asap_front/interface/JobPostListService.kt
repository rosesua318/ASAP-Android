package org.techtown.asap_front.`interface`

import org.techtown.asap_front.data_object.JobPost
import org.techtown.asap_front.data_object.RecommBody
import retrofit2.Call
import retrofit2.http.*

interface JobPostListService {
    @FormUrlEncoded
    @GET("/search_job/posts/")
    fun getAllPosts(): Call<JobPost>

    @POST("/search_job/posts/")
    fun post(
            @Body jsonparams: JobPost
    )
}