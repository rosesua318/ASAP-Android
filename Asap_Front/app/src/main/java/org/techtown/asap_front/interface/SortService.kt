package org.techtown.asap_front.`interface`

import org.techtown.asap_front.data_object.EmpPost
import org.techtown.asap_front.data_object.JobPost
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SortService {
    @GET("/get_staff/sort_high_pay/")
    fun getHighPayEmp() : Call<ArrayList<EmpPost>>

    @GET("/search_job/sort_early_start/")
    fun getEarlyStartJob() : Call<ArrayList<JobPost>>
}