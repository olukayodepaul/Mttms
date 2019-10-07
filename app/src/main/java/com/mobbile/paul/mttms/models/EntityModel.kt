package com.mobbile.paul.mttms.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "modules")
data class EntityModules(
    @PrimaryKey(autoGenerate = false)
    var auto: Int = 0,
    var id: Int = 0,
    var nav: Int = 0,
    var name: String = "",
    var imageurl: String = ""
)

@Entity(tableName = "replist")
data class EntityRepList(
    @PrimaryKey(autoGenerate = false)
    var auto: Int = 0,
    var employeeid: Int = 0,
    var edcode: String = "",
    var custcode: String = "",
    var fullname: String = ""
)

@Entity(tableName = "spiners")
data class EntitySpiners(
    @PrimaryKey(autoGenerate = false)
    var auto: Int = 0,
    var id: Int = 0,
    var name: String = "",
    var sep: Int = 0
)

@Entity(tableName = "allcustomers")
data class EntityAllCustomersList (
    @PrimaryKey(autoGenerate = false)
    var auto: Int = 0,
    var employeeid: Int = 0,
    var ecode: String = "",
    var custcode: String = "",
    var fullname: String = ""
)

@Entity(tableName = "alloutlets")
data class EntityAllOutletsList (
    @PrimaryKey(autoGenerate = false)
    var auto: Int = 0,
    var id: Int = 0,
    var urno: Int = 0 ,
    var customerno: String = "",
    var outletclassid: Int = 0,
    var outletlanguageid: Int = 0,
    var outlettypeid: Int = 0,
    var outletname: String = "",
    var outletaddress: String = "",
    var contactname: String = "",
    var contactphone: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var outlet_pic: String = ""
)