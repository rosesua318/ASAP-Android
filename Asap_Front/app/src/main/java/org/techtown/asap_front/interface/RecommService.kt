package org.techtown.asap_front.`interface`

import org.techtown.asap_front.data_object.PostResult
import org.techtown.asap_front.data_object.RecommBody
import retrofit2.Call
import retrofit2.http.*

interface RecommService {
    @FormUrlEncoded
    @POST("/users/recomms/")
    fun recommend(
        @Body jsonParams: RecommBody
    ) : Call<PostResult>
}