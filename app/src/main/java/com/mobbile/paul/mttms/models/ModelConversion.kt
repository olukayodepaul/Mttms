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
        longitude,outlet_pic,token,defaulttoken,sequenceno,distance,duration,mode,tm_id,dates,
        volumeclass,rep_name,sort,notice,customer_code,entry_time
    )
}

fun EntityGetSalesEntry.toAllOutletsList(): getSalesEntry {
    return getSalesEntry(
        id,product_id, product_code,product_name, soq, seperator,seperatorname, pricing, inventory,
        entry_time,controlpricing,controlinventory
    )
}

fun getSalesEntry.toEntityGetSalesEntry(): EntityGetSalesEntry {
    return EntityGetSalesEntry(
       id,product_id, product_code,product_name, soq, seperator,seperatorname, pricing, inventory,
        entry_time,controlpricing,controlinventory
    )
}


