package com.mobbile.paul.mttms.providers


import com.mobbile.paul.mttms.models.Basket
import com.mobbile.paul.mttms.models.InitAllOutlets
import com.mobbile.paul.mttms.models.SalesReps
import com.mobbile.paul.mttms.models.UserAuth
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface NodejsApi {

    @Headers("Connection:close")
    @POST("/api/tmlogin")
    fun getUser(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("imei") imei: String
    ): Single<Response<UserAuth>>

    @Headers("Connection:close")
    @POST("/api/tmreps")
    fun fetchAllReps(
        @Query("depotid") depotid: Int,
        @Query("regionid") regionid: Int
    ): Single<Response<SalesReps>>

    @Headers("Connection:close")
    @POST("/api/allrepcust")
    fun fetchAllCustomers(
        @Query("repid") repid: Int,
        @Query("tmid") tmid: Int
    ): Single<Response<InitAllOutlets>>

    @Headers("Connection:close")
    @POST("/api/tmrepbasket")
    fun getCustomerNo(
        @Query("customerno") customerno: String
    ): Single<Response<Basket>>
}