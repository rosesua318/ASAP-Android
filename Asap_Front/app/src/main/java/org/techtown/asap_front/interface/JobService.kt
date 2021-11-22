package org.techtown.asap_front.`interface`

import org.techtown.asap_front.data_object.Job
import retrofit2.Call
import retrofit2.http.GET

interface JobService {
    @GET("/users/alljobs/")
    fun getJobs() : Call<List<Job>>
}