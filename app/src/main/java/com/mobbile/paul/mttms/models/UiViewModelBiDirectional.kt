package com.mobbile.paul.mttms.models

data class AuthObjectData (
    var status: Int = 0,
    var msg: String? = null,
    var setpref:Int?=null,
    var employeeid: Int = 0,
    var depots_id: Int = 0 ,
    var region_id: Int  = 0
)

data class EntryCallback (
    var status: String? = null,
    var msg: String? = null,
    var data: List<setSalesEntry>? = null
)
