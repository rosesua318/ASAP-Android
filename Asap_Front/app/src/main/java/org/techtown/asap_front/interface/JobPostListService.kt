package org.techtown.asap_front.`interface`

import org.techtown.asap_front.data_object.JobPost
import org.techtown.asap_front.data_object.JobPostModel
import org.techtown.asap_front.data_object.PostResult
import org.techtown.asap_front.data_object.RecommBody
import retrofit2.Call
import retrofit2.http.*

interface JobPostListService {
    @GET("/search_job/posts/")
    fun getAllPosts(): Call<ArrayList<JobPost>>

    @POST("/search_job/posts/")
    fun post(
            @Body jsonparams: JobPostModel
    ) : Call<PostResult>

    @GET("/search_job/posts/{id}")
    fun getPost(
            @Path("id") id: Int
    ) : Call<JobPost>
}