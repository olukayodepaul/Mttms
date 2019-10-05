package com.mobbile.paul.mttms.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Modules(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var nav: String = "",
    var name: String = "",
    var imageurl: String = ""
)
