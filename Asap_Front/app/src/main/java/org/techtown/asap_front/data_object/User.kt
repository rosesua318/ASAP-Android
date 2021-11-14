package org.techtown.asap_front.data_object

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int,
    val phone_nm: String,
    val login_ID: String,
    val login_PW: String,
    val age: Int,
    val gender: Int,
    val password: String
        ) : Parcelable