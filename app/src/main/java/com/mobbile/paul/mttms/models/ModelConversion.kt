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
        auto,rep_id,urno,customerno,outletclassid,outletlanguageid,outlettypeid,
        outletname,outletaddress,contactname,contactphone,latitude,
        longitude,outlet_pic,token,defaulttoken,sequenceno,mode,tm_id,dates,
        volumeclass,rep_name,sort,notice
    )
}

fun EntityAllOutletsList.toAllOutletsList(): AllOutletsList {
    return AllOutletsList(
        auto,rep_id,urno,customerno,outletclassid,outletlanguageid,outlettypeid,
        outletname,outletaddress,contactname,contactphone,latitude,
        longitude,outlet_pic,token,defaulttoken,sequenceno,mode,tm_id,dates,
        volumeclass,rep_name,sort, notice
    )
}

fun getSalesEntry.toEntityGetSalesEntry(): EntityGetSalesEntry {
    return EntityGetSalesEntry(
        id, productid, soq, productname, qty, price, seperator, seperatorname, orders, inventory,
        pricing, entrytime, orderrice, mtcom, mtamt, contorder, contprincing, continventory
    )
}

