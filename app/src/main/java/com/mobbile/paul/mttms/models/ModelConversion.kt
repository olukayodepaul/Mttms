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

fun AllOutletsList.toEntityAllOutletsList(): EntityAllOutletsList {
    return EntityAllOutletsList(
        auto, id, urno, customerno, outletclassid, outletlanguageid, outlettypeid, outletname,
        outletaddress, contactname, contactphone, latitude, longitude, outlet_pic,token, defaulttoken, sequenceno
    )
}

fun EntityAllOutletsList.toAllOutletsList(): AllOutletsList {
    return AllOutletsList(
        auto, id, urno, customerno, outletclassid, outletlanguageid, outlettypeid, outletname,
        outletaddress, contactname, contactphone, latitude, longitude, outlet_pic, token, defaulttoken, sequenceno
    )
}

fun getSalesEntry.toEntityGetSalesEntry(): EntityGetSalesEntry {
    return EntityGetSalesEntry(
        id, productid, soq, productname, qty, price, seperator, seperatorname, orders, inventory,
        pricing, entrytime, orderrice, mtcom, mtamt, contorder, contprincing, continventory
    )
}

