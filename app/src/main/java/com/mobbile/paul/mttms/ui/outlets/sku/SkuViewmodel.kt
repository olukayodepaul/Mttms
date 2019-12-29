package com.mobbile.paul.mttms.ui.outlets.sku

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.*
import com.mobbile.paul.mttms.providers.Repository
import javax.inject.Inject

class SkuViewmodel @Inject constructor(private var repository: Repository) : ViewModel() {

    fun fetch(): LiveData<List<EntityGetSalesEntry>> {

        var mResult = MutableLiveData<List<EntityGetSalesEntry>>()

        repository.fetchAllEntryPerDaily()
            .subscribe({
                mResult.postValue(it)
            }, {
                mResult.postValue(null)
            }).isDisposed

        return mResult
    }

    fun sumAllSalesEntry(): LiveData<SumSales> {
        val mResult = MutableLiveData<SumSales>()
        repository.sumAllSalesEntry()
            .subscribe({
                mResult.postValue(it)
            }, {
                mResult.postValue(null)
            }).isDisposed
        return mResult
    }

    fun postSalesToServer (
        repid: Int,
        tmid: Int,
        currentlat: String,
        currentlng: String,
        outletlat: String,
        outletlng: String,
        arrivaltime: String,
        visitsequence: String,
        distance: String,
        duration: String,
        urno: Int,
        entry: List<getSalesEntry>,
        id: Int,
        nexts: Int,
        self: String
    ) {

        val list = postToServer()
        list.repid = repid
        list.tmid = tmid
        list.currentlat = currentlat
        list.currentlng = currentlng
        list.outletlat = outletlat
        list.outletlng = outletlng
        list.arrivaltime = arrivaltime
        list.visitsequence = visitsequence
        list.distance = distance
        list.duration = duration
        list.urno = urno
        list.entry = entry
        repository.fetchPostSales(list)
            .subscribe({
                if (it != null) {
                    if (it.body()!!.status == 200) {
                        UpdateSeque(id,nexts,self)
                    } else {

                    }
                }
            }, {

            }).isDisposed
    }

    fun pullAllSalesEntry(
        repid: Int, tmid: Int, currentlat: String, currentlng: String,
        outletlat: String, outletlng: String, arrivaltime: String,
        visitsequence: String, distance: String, duration: String, urno: Int,
        id: Int, nexts: Int, self: String
    ): LiveData<String> {

        var mResult = MutableLiveData<String>()
        repository.pullAllSalesEntry()
            .subscribe({ data ->
                postSalesToServer(
                    repid,
                    tmid,
                    currentlat,
                    currentlng,
                    outletlat,
                    outletlng,
                    arrivaltime,
                    visitsequence,
                    distance,
                    duration,
                    urno,
                    data.map{it.toAllOutletsList()},
                    id,
                    nexts,
                    self
                )
            }, {

            }).isDisposed

        return mResult
    }

    fun UpdateSeque(id: Int,nexts:Int,self:String) {
        repository.UpdateSeque(id,nexts,self).subscribe({

        },{

        }).isDisposed
    }

}