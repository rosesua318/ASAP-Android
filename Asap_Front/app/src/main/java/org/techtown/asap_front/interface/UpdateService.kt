package org.techtown.asap_front.`interface`

import org.techtown.asap_front.data_object.PostResult
import org.techtown.asap_front.data_object.UpdateJobBody
import org.techtown.asap_front.data_object.UpdateNickIntroBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UpdateService {
    @PUT("/users/profiles/{id}")
    fun updateNickIntro(
            @Path("id") id: Int,
            @Body jsonParams: UpdateNickIntroBody
    ) : Call<PostResult>

    @PUT("/users/job/")
    fun updateJob(
            @Body jsonParams : UpdateJobBody
    ) : Call<PostResult>
}