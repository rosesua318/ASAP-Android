package org.techtown.asap_front.data_object

data class CommentBody (
        val post_id: Int,
        val profile_id: Int,
        var content: String,
        var is_anon: Boolean
        )