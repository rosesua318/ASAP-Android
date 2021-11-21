package org.techtown.asap_front

import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface RetrofitInteface {
    @POST("/users/login/")
    fun executeLogin(@Body map: HashMap<String, String>): Call<LoginResult?>?

    @POST("/users/profiles/")
    fun executeSignup(@Body map: HashMap<String, String>): Call<Void?>?
}