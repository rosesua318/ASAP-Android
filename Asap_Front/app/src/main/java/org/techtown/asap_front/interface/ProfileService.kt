package org.techtown.asap_front.`interface`

import org.techtown.asap_front.data_object.Profile
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {
    @GET("/users/profiles/{id}")
    fun getProfile(
        @Path("id") id: Int
    ) : Call<Profile>
}