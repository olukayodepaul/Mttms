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
    var sep: String = ""
)