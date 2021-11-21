package org.techtown.asap_front

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    var api: RetrofitInteface

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://asap-ds.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        api = retrofit.create(RetrofitInteface::class.java)
    }
}