package com.mobbile.paul.mttms.providers


import com.mobbile.paul.mttms.models.*
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
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

    @Headers("Connection:close")
    @POST("/api/attendant")
    fun takeAttendant(
        @Query("tmid") tmid: Int,
        @Query("taskid") taskid: Int,
        @Query("repid") repid: Int
    ): Single<Response<Attendant>>

    @Headers("Connection:close")
    @POST("/api/closetmoutlet")
    fun CloseOutlets(
        @Query("repid") repid: Int,
        @Query("tmid") tmid: Int,
        @Query("currentlat") currentlat: String,
        @Query("currentlng") currentlng: String,
        @Query("outletlat") outletlat: String,
        @Query("outletlng") outletlng: String,
        @Query("arrivaltime") arrivaltime: String,
        @Query("visitsequence") visitsequence: String,
        @Query("distance") distance: String,
        @Query("duration") duration: String,
        @Query("urno") urno: Int
    ): Single<Response<Attendant>>

    @Headers("Connection:close")
    @POST("api/dailysalesentry")
    fun getbasket(
        @Query("customerno") customerno: String,
        @Query("customer_code") customer_code: String,
        @Query("repid") repid: Int
    ): Single<Response<InitBbasket>>

    //working on this four
    @Headers("Connection:close")
    @POST("/api/tm_sales_visit")
    fun postSales(
        @Body datas: postToServer
    ): Single<Response<Exchange>>

    @Headers("Connection:close")
    @POST("/api/tm_outlet_async")
    fun AsynData(
        @Query("repid") repid: Int,
        @Query("tmid") tmid: Int
    ): Single<Response<Exchange>>

    @Headers("Connection:close")
    @POST("/api/tm_update_outlet")
    fun updateOutlet(
        @Query("repid") repid: Int,
        @Query("tmid") tmid: Int,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("customername") customername: String,
        @Query("contactname") contactname: String,
        @Query("address") address: String,
        @Query("phonenumber") phonenumber: String,
        @Query("outletclass") outletclass: Int,
        @Query("outletlanguage") outletlanguage: Int,
        @Query("outlettype") outlettype: Int
    ): Single<Response<Exchange>>

    @Headers("Connection:close")
    @POST("/api/tm_map_outlet")
    fun mapOutlet(
        @Query("tmid") tmid: Int,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("customername") customername: String,
        @Query("contactname") contactname: String,
        @Query("address") address: String,
        @Query("phonenumber") phonenumber: String,
        @Query("outletclass") outletclass: Int,
        @Query("outletlanguage") outletlanguage: Int,
        @Query("outlettype") outlettype: Int
    ): Single<Response<Exchange>>
}
