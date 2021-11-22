package org.techtown.asap_front.`interface`

import org.techtown.asap_front.data_object.EmpPost
import org.techtown.asap_front.data_object.JobPost
import org.techtown.asap_front.data_object.PostResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface EmpPostListService {
    @GET("/get_staff/posts/")
    fun getAllPosts(): Call<ArrayList<EmpPost>>

    @POST("/get_staff/posts/")
    fun post(
        @Body jsonparams: EmpPost
    ) : Call<PostResult>
}