package com.mobbile.paul.mttms.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sqrt
import java.text.SimpleDateFormat
import java.util.*


object Util {
    fun showSomeDialog(context: Context, msg: String, title: String?) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogDanger)
        builder.setMessage(msg)
            .setTitle(title)
            .setIcon(R.drawable.icons8_google_alerts_100)
            .setCancelable(false)
            .setNegativeButton("Ok") { _, _ ->
            }
        val dialog = builder.create()
        dialog.show()
    }

    fun showMsgDialog(activity: Activity, context: Context, title: String, msg: String?) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogDanger)
        builder.setMessage(msg)
            .setTitle(title)
            .setCancelable(false)
            .setIcon(R.drawable.icons8_google_alerts_100)
            .setNegativeButton(
                "OK"
            ) { _, _ ->
                var intent = Intent(context, activity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
            }
        val dialog = builder.create()
        dialog.show()
    }

    fun AuthbiData(
        sep: Int, data: MutableLiveData<AuthBiData>, depot_id: Int,
        region_id: Int, employee_id: Int,
        status: Int, notification: String
    ) {
        val dataPasser = AuthBiData()
        dataPasser.status = status
        dataPasser.msg = notification
        dataPasser.setpref = sep
        dataPasser.employeeid = employee_id
        dataPasser.depots_id = depot_id
        dataPasser.region_id = region_id
        data.postValue(dataPasser)
    }

    fun salesRepCustdata(
        data: MutableLiveData<SalesRepAndCustomerData>,
        status: Int,
        msg:String="",
        replist: List<AllTheSalesRep>,
        repcust: List<EntityAllOutletsList>
    ) {
        val dataPasser = SalesRepAndCustomerData()
        dataPasser.status = status
        dataPasser.msg = msg
        dataPasser.salesrepsList = replist
        dataPasser.salesRepCustomersList = repcust
        data.postValue(dataPasser)
    }

    fun repCustdata(
        data: MutableLiveData<repAndCustomerData>,
        status: Int,
        notice: String

    ) {
        val dataPasser = repAndCustomerData()
        dataPasser.status = status
        dataPasser.notice = notice
        data.postValue(dataPasser)
    }

    fun repBaskets(
        data: MutableLiveData<ProductBiData>,
        status: Int,
        msg: String,
        list: List<Products>

    ) {
        val dataPasser = ProductBiData()
        dataPasser.status = status
        dataPasser.msg = msg
        dataPasser.list = list
        data.postValue(dataPasser)
    }

    fun AttendanBiData(
        data: MutableLiveData<AttendantData>,
        status: Int,
        notis: String
    ){
        val dataPasser = AttendantData()
        dataPasser.status = status
        dataPasser.notis = notis
        data.postValue(dataPasser)
    }

    fun CloseAndOpenOutletBiData(
        data: MutableLiveData<CloseAndOpenOutlet>,
        status: Int,
        lat: Double,
        lng:Double,
        nexts: Int,
        self:String,
        id:Int
    ){
        val dataPasser = CloseAndOpenOutlet()
        dataPasser.status = status
        dataPasser.lat = lat
        dataPasser.lng = lng
        dataPasser.nexts=nexts
        dataPasser.self=self
        dataPasser.id=id
        data.postValue(dataPasser)
    }

    fun insideRadius(
        currentLat: Double, currentLng: Double,
        customerLat: Double, customerLng: Double
    ): Boolean {
        val ky = 40000 / 360
        val kx = cos(PI * customerLat / 180.0) * ky
        val dx = abs(customerLng - currentLng) * kx
        val dy = abs(customerLat - currentLat) * ky
        return sqrt(dx * dx + dy * dy) <= 1
    }

    @SuppressLint("SimpleDateFormat")
    fun appTime(): String{
        return SimpleDateFormat("HH:mm:ss").format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    fun appDate(): String {
        return SimpleDateFormat("yyyy-MM-dd").format(Date())
    }

    fun ResponsesBiData(
        data: MutableLiveData<Responses>,
        status: Int,
        notis: String
    ){
        val dataPasser = Responses()
        dataPasser.status = status
        dataPasser.notis = notis
        data.postValue(dataPasser)
    }

}

