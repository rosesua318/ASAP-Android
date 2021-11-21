package org.techtown.asap_front.data_object

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Job (
        val id : Int,
        val job_name: String
        ) : Parcelable