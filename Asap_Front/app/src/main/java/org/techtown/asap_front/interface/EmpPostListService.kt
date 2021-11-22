package org.techtown.asap_front.`interface`

import org.techtown.asap_front.data_object.EmpPost
import org.techtown.asap_front.data_object.JobPost
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

interface EmpPostListService {
    @FormUrlEncoded
    @GET("/get_staff/posts/")
    fun getAllPosts(): Call<EmpPost>
}