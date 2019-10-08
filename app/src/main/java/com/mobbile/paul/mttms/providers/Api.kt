package com.mobbile.paul.mttms.providers

import com.mobbile.paul.mttms.models.InitAllCustomers
import com.mobbile.paul.mttms.models.InitAllOutlets
import com.mobbile.paul.mttms.models.InitBbasket
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

    @Headers("Connection:close")
    @POST("/mobiletrader/tm_reps")
    fun fetchAllCustomers(
        @Query("depotid") depotid: Int,
        @Query("regionid") regionid: Int
    ): Single<Response<InitAllCustomers>>

    @Headers("Connection:close")
    @POST("/mobiletrader/tm_outlets")
    fun fetchAllOutlets(
        @Query("employeeid") employeeid: Int,
        @Query("today") today: String
    ): Single<Response<InitAllOutlets>>

    @Headers("Connection:close")
    @POST("/mobiletrader/tm_basket")
    fun getbasket(
        @Query("employeeid") employeeid: Int,
        @Query("custno") custno: String,
        @Query("urno") urno: String
    ): Single<Response<InitBbasket>>

}

