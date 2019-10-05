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

