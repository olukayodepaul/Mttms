package com.mobbile.paul.mttms.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class UserAuth(

    @SerializedName("status")
    @Expose
    var status: Int,
    @SerializedName("massage")
    @Expose
    var massage: String = "",
    @SerializedName("notification")
    @Expose
    var notification: String = "",
    @SerializedName("employee_id")
    @Expose
    var employee_id: Int,
    @SerializedName("depots_id")
    @Expose
    var depots_id: Int,
    @SerializedName("dates")
    @Expose
    var dates: String,
    @SerializedName("region_id")
    @Expose
    var region_id: Int,
    @SerializedName("modules")
    @Expose
    var modules: List<UserModules>? = null,
    @SerializedName("reps")
    @Expose
    var reps: List<RepList>? = null,
    @SerializedName("spinners")
    @Expose
    var spinners: List<UserSpinners>? = null
)

data class UserModules(
    @SerializedName("auto")
    @Expose
    var auto: Int = 0,
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("nav")
    @Expose
    var nav: Int = 0,
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("imageurl")
    @Expose
    var imageurl: String = ""
)

data class RepList(
    @SerializedName("auto")
    @Expose
    var auto: Int = 0,
    @SerializedName("employeeid")
    @Expose
    var employeeid: Int = 0,
    @SerializedName("edcode")
    @Expose
    var edcode: String = "",
    @SerializedName("custcode")
    @Expose
    var custcode: String = "",
    @SerializedName("fullname")
    @Expose
    var fullname: String = "",
    @SerializedName("mode")
    @Expose
    var mode: String = ""
)

data class UserSpinners(
    @SerializedName("auto")
    @Expose
    var auto: Int = 0,
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("sep")
    @Expose
    var sep: Int = 0
)

data class SalesReps (
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("allreps")
    @Expose
    var allreps: List<AllTheSalesRep>? = null
)

data class AllTheSalesRep (
    @SerializedName("auto")
    @Expose
    var auto: Int = 0,
    @SerializedName("employeeid")
    @Expose
    var employeeid: Int = 0,
    @SerializedName("ecode")
    @Expose
    var ecode: String = "",
    @SerializedName("custcode")
    @Expose
    var custcode: String = "",
    @SerializedName("fullname")
    @Expose
    var fullname: String = "",
    @SerializedName("mode")
    @Expose
    var mode: String = ""
)

data class InitAllOutlets (
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = "",
    @SerializedName("alloutlets")
    @Expose
    var alloutlets: List<AllOutletsList>?=null
)

@Parcelize
data class AllOutletsList (
    @SerializedName("auto")
    @Expose
    var auto: Int = 0,
    @SerializedName("rep_id")
    @Expose
    var rep_id: Int = 0,
    @SerializedName("urno")
    @Expose
    var urno: Int = 0,
    @SerializedName("customerno")
    @Expose
    var customerno: String = "",
    @SerializedName("outletclassid")
    @Expose
    var outletclassid: Int = 0,
    @SerializedName("outletlanguageid")
    @Expose
    var outletlanguageid: Int = 0,
    @SerializedName("outlettypeid")
    @Expose
    var outlettypeid: Int = 0,
    @SerializedName("outletname")
    @Expose
    var outletname: String = "",
    @SerializedName("outletaddress")
    @Expose
    var outletaddress: String = "",
    @SerializedName("contactname")
    @Expose
    var contactname: String = "",
    @SerializedName("contactphone")
    @Expose
    var contactphone: String = "",
    @SerializedName("latitude")
    @Expose
    var latitude: Double = 0.0,
    @SerializedName("longitude")
    @Expose
    var longitude: Double = 0.0,
    @SerializedName("outlet_pic")
    @Expose
    var outlet_pic: String = "",
    @SerializedName("token")
    @Expose
    var token: String = "",
    @SerializedName("defaulttoken")
    @Expose
    var defaulttoken: String = "",
    @SerializedName("sequenceno")
    @Expose
    var sequenceno: Int = 0,
    @SerializedName("distance")
    @Expose
    var distance: String = "",
    @SerializedName("duration")
    @Expose
    var duration: String = "",
    @SerializedName("mode")
    @Expose
    var mode:String="",
    @SerializedName("tm_id")
    @Expose
    var tm_id: Int = 0,
    @SerializedName("dates")
    @Expose
    var dates:String="",
    @SerializedName("volumeclass")
    @Expose
    var volumeclass:String="",
    @SerializedName("rep_name")
    @Expose
    var rep_name:String="",
    @SerializedName("sort")
    @Expose
    var sort: Int= 0,
    @SerializedName("notice")
    @Expose
    var notice: String= "",
    @SerializedName("customer_code")
    @Expose
    var customer_code: String= "",
    @SerializedName("entry_time")
    @Expose
    var entry_time: String= ""
): Parcelable

data class InitBbasket (
    @SerializedName("status")
    @Expose
    var status: String = "",
    @SerializedName("sales")
    @Expose
    var sales: List<setSalesEntry>?=null,
    @SerializedName("entry")
    @Expose
    var entry: List<getSalesEntry>?=null
)

data class setSalesEntry (
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("product_id")
    @Expose
    var product_id: String = "",
    @SerializedName("product_code")
    @Expose
    var product_code: String = "",
    @SerializedName("product_name")
    @Expose
    var product_name: String = "",
    @SerializedName("soq")
    @Expose
    var soq: String = "",
    @SerializedName("seperator")
    @Expose
    var seperator: String = ""
)

data class getSalesEntry (
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("product_id")
    @Expose
    var product_id: String = "",
    @SerializedName("product_code")
    @Expose
    var product_code: String = "",
    @SerializedName("product_name")
    @Expose
    var product_name: String = "",
    @SerializedName("soq")
    @Expose
    var soq: String = "",
    @SerializedName("seperator")
    @Expose
    var seperator: String = "",
    @SerializedName("seperatorname")
    @Expose
    var seperatorname: String = "",
    @SerializedName("pricing")
    @Expose
    var pricing: String = "",
    @SerializedName("inventory")
    @Expose
    var inventory: String = "",
    @SerializedName("entry_time")
    @Expose
    var entry_time: String = "",
    @SerializedName("controlpricing")
    @Expose
    var controlpricing: String = "",
    @SerializedName("controlinventory")
    @Expose
    var controlinventory: String = ""
)

data class SumSales(
    var spricing: Int = 0,
    var sinventory: Double = 0.0
)

data class getCards (
    @SerializedName("status")
    @Expose
    val status: String
)

//products as basket
data class Basket (
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = "",
    @SerializedName("allrepsproducts")
    @Expose
    var allrepsproducts: List<Products>? = null
)

data class Products(
    @SerializedName("productname")
    @Expose
    var productname: String = "",
    @SerializedName("qty")
    @Expose
    var qty: String = ""
)

data class Attendant(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = ""
)

data class postToServer(
    @SerializedName("repid")
    @Expose
    var repid: Int = 0,
    @SerializedName("tmid")
    @Expose
    var tmid: Int = 0,
    @SerializedName("currentlat")
    @Expose
    var currentlat: String = "",
    @SerializedName("currentlng")
    @Expose
    var currentlng: String = "",
    @SerializedName("outletlat")
    @Expose
    var outletlat: String = "",
    @SerializedName("outletlng")
    @Expose
    var outletlng: String = "",
    @SerializedName("arrivaltime")
    @Expose
    var arrivaltime: String = "",
    @SerializedName("visitsequence")
    @Expose
    var visitsequence: String = "",
    @SerializedName("distance")
    @Expose
    var distance: String = "",
    @SerializedName("duration")
    @Expose
    var duration: String = "",
    @SerializedName("urno")
    @Expose
    var urno: Int = 0,
    @SerializedName("token")
    @Expose
    var token: String = "",
    @SerializedName("saleslist")
    @Expose
    var entry: List<getSalesEntry>? = null
)

data class Exchange(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = ""
)

data class OutletAsyn(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("outletclassid")
    @Expose
    var outletclassid: Int = 0,
    @SerializedName("outletlanguageid")
    @Expose
    var outletlanguageid: Int = 0,
    @SerializedName("outlettypeid")
    @Expose
    var outlettypeid: Int = 0,
    @SerializedName("outletname")
    @Expose
    var outletname: String = "",
    @SerializedName("outletaddress")
    @Expose
    var outletaddress: String = "",
    @SerializedName("contactname")
    @Expose
    var contactname: String = "",
    @SerializedName("contactphone")
    @Expose
    var contactphone: String = "",
    @SerializedName("longitude")
    @Expose
    var longitude: String = "",
    @SerializedName("latitude")
    @Expose
    var latitude: String = ""
)

data class Details(
    @SerializedName("data")
    @Expose
    var data: List<DetailsList>? = null
)

data class DetailsList(
    @SerializedName("sku")
    @Expose
    var sku: String = "",
    @SerializedName("princing")
    @Expose
    var princing: String = "",
    @SerializedName("inventory")
    @Expose
    var inventory: String = "",
    @SerializedName("category")
    @Expose
    var category: String = ""
)








