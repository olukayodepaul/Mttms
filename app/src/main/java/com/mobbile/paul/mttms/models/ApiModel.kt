package com.mobbile.paul.mttms.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserAuth(
    @SerializedName("status")
    @Expose
    var status: String = "",
    @SerializedName("msg")
    @Expose
    var msg: String = "",
    @SerializedName("employee_id")
    @Expose
    var employee_id: Int,
    @SerializedName("depots_id")
    @Expose
    var depots_id: Int,
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
    var fullname: String = ""
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
    var sep: String = ""
)

data class InitAllCustomers (
    @SerializedName("status")
    @Expose
    var status: String? = null,
    @SerializedName("counts")
    @Expose
    var counts: Int? = null,
    @SerializedName("allreps")
    @Expose
    var allreps: List<AllCustomersList>? = null
)

data class AllCustomersList (
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
    var fullname: String = ""
)

data class InitAllOutlets (
    @SerializedName("status")
    @Expose
    var status: String? = null,
    @SerializedName("counts")
    @Expose
    var counts: Int? = null,
    @SerializedName("allreps")
    @Expose
    var alloutlets: List<AllOutletsList>? = null
)

data class AllOutletsList (
    @SerializedName("auto")
    @Expose
    var auto: Int = 0,
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("urno")
    @Expose
    var urno: Int = 0 ,
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
    var outlet_pic: String = ""
)



