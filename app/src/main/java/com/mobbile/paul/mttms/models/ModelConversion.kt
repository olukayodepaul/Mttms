package com.mobbile.paul.mttms.models

fun UserModules.toEntityModules(): EntityModules {
    return EntityModules(
        auto,id,nav,name,imageurl
    )
}

fun UserSpinners.toEntitySpiners(): EntitySpiners {
    return EntitySpiners(
        auto,id, name, sep
    )
}