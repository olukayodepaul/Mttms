package com.mobbile.paul.mttms.models

data class AuthBiData(
    var status: Int = 0,
    var msg: String? = null,
    var setpref:Int?=null,
    var employeeid: Int = 0,
    var depots_id: Int = 0 ,
    var region_id: Int  = 0,
    var notification: String? = null
)

data class SalesRepAndCustomerData (
    var status: Int = 0,
    var msg:String="",
    var salesrepsList: List<AllTheSalesRep>? = null,
    var salesRepCustomersList: List<EntityAllOutletsList>? = null
)

data class repAndCustomerData (
    var status: Int = 0,
    var notice: String = ""
)

data class EntryCallback (
    var status: String? = null,
    var msg: String? = null,
    var data: List<setSalesEntry>? = null
)

data class ProductBiData (
    var status: Int = 0,
    var msg: String = "",
    var list: List<Products>? = null
)
