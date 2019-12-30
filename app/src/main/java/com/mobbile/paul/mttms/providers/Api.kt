package com.mobbile.paul.mttms.providers

import com.mobbile.paul.mttms.models.*
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @Headers("Connection:close")
    @POST("/tm_outlets")
    fun fetchAllOutlets(
        @Query("employeeid") employeeid: Int,
        @Query("today") today: String
    ): Single<Response<InitAllOutlets>>

    @Headers("Connection:close")
    @Multipart
    @POST("/mapoutlets")
    fun createNewCards(
        @Query("employee_id") employee_id: Int,
        @Query("outletclassid") outletclassid: Int,
        @Query("outletlanguageid") outletlanguageid: Int,
        @Query("outlettypeid") outlettypeid: Int,
        @Query("outletname") outletname: String,
        @Query("outletaddress") outletaddress: String,
        @Query("contactname") contactname: String,
        @Query("contactphone") contactphone: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Query("entry_date_time") entry_date_time: String,
        @Query("entry_date") entry_date: String
    ): Single<Response<getCards>>

    @Headers("Connection:close")
    @POST("/tm_visit")
    fun tmVisit(
        @Query("rep_id") rep_id: Int,
        @Query("tm_id") tm_id: Int,
        @Query("entry_date") entry_date: String,
        @Query("entry_date_time") entry_date_time: String
    ): Single<Response<getCards>>

}



