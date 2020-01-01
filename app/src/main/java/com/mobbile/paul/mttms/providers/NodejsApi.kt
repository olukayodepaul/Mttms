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
        @Query("repid") repid: Int,
        @Query("outletlat") outletlat: Double,
        @Query("outletlng") outletlng: Double,
        @Query("currentLat") currentLat: Double,
        @Query("currentLng") currentLng: Double,
        @Query("distance") distance: String,
        @Query("duration") duration: String,
        @Query("sequenceno") sequenceno: String,
        @Query("arrivaltime") arrivaltime: String

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
    @POST("/api/tm_outlet_info_async")
    fun CustometInfoAsync(
        @Query("urno") urno: Int
    ): Single<Response<OutletAsyn>>

    @Headers("Connection:close")
    @POST("/api/tm_update_outlet")
    fun updateOutlet(
        @Query("tmid") tmid: Int,
        @Query("urno") urno: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("outletname") outletname: String,
        @Query("contactname") contactname: String,
        @Query("outletaddress") outletaddress: String,
        @Query("contactphone") contactphone: String,
        @Query("outletclassid") outletclassid: Int,
        @Query("outletlanguageid") outletlanguageid: Int,
        @Query("outlettypeid") outlettypeid: Int
    ): Single<Response<Exchange>>

    @Headers("Connection:close")
    @POST("/api/tm_map_outlet")
    fun mapOutlet(
        @Query("repid") repid: Int,
        @Query("tmid") tmid: Int,
        @Query("urno") urno: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("outletname") outletname: String,
        @Query("contactname") contactname: String,
        @Query("outletaddress") outletaddress: String,
        @Query("contactphone") contactphone: String,
        @Query("outletclassid") outletclassid: Int,
        @Query("outletlanguageid") outletlanguageid: Int,
        @Query("outlettypeid") outlettypeid: Int
    ): Single<Response<Exchange>>

    @Headers("Connection:close")
    @POST("/api/entry_details")
    fun getEntryDetails(
        @Query("urno") urno: Int
    ): Single<Response<Details>>

}
