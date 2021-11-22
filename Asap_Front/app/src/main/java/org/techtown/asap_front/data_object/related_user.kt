package org.techtown.asap_front.data_object

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class related_user (
    var phone_nm: String,
    var login_ID: String,
    var login_PW: String,
    var age: Int,
    var gender: Int,
    var password : String
) : Parcelable