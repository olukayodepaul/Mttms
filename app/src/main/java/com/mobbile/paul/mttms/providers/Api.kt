package com.mobbile.paul.mttms.providers

import com.mobbile.paul.mttms.models.UserAuth
import io.reactivex.Single
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.Response

interface Api {

    @Headers("Connection:close")
    @POST("/mobiletrader/tm_login")
    fun getUser(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("imei") imei: String
    ): Single<Response<UserAuth>>
}

