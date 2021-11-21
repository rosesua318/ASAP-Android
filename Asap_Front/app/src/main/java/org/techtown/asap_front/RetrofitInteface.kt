package org.techtown.asap_front

import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.HashMap

interface RetrofitInteface {
    @POST("/users/login/")
    fun executeLogin(@Body map: HashMap<String, String>): Call<String?>?

    @POST("/users/profiles")
    fun executeSignup(@Body map: JoinSend): Call<Void?>?
}