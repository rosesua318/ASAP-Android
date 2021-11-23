package org.techtown.asap_front

import retrofit2.Call
import retrofit2.http.*
import kotlin.collections.HashMap

interface RetrofitInteface {
    @POST("/users/login/")
    fun executeLogin(@Body map: HashMap<String, String>): Call<String?>?

    @POST("/users/profiles/")
    fun executeSignup(@Body map: JoinSend): Call<Void?>?

    @GET("/get_staff/{pk}/comments/")
    fun executeComment1(
            @Path("pk") pk: Int
    ) : Call<ArrayList<Comment_1>>

    @GET("/search_job/{pk}/comments/")
    fun executeComment2(
            @Path("pk") pk: Int
    ) : Call<ArrayList<Comment_1>>

}