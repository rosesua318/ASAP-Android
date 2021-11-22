package org.techtown.asap_front.`interface`

import org.techtown.asap_front.data_object.CommentBody
import org.techtown.asap_front.data_object.PostResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CommentService {
    @POST("/search_job/add_comment/")
    fun addCommentJob(
            @Body jsonParams: CommentBody
    ) : Call<PostResult>

    @POST("/get_staff/add_comment/")
    fun addCommentEmp(
            @Body jsonParams: CommentBody
    ) : Call<PostResult>
}