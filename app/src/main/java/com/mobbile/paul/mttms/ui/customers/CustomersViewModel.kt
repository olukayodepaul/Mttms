package com.mobbile.paul.mttms.ui.customers


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.*
import com.mobbile.paul.mttms.providers.Repository
import com.mobbile.paul.mttms.util.Util.AttendanBiData
import com.mobbile.paul.mttms.util.Util.CloseAndOpenOutletBiData
import com.mobbile.paul.mttms.util.Util.appTime
import com.mobbile.paul.mttms.util.Util.repCustdata
import com.mobbile.paul.mttms.util.Util.salesRepCustdata
import javax.inject.Inject

@Suppress("CAST_NEVER_SUCCEEDS")
class CustomersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val emptyoutlet: List<EntityAllOutletsList> = emptyList()

    private val emptyrep: List<AllTheSalesRep> = emptyList()

    var repSelection = MutableLiveData<repAndCustomerData>()

    fun selectAnyReps(): LiveData<repAndCustomerData> {
        return repSelection
    }

    private val attendantData = MutableLiveData<AttendantData>()

    fun closeOutletMutable(): LiveData<AttendantData> {
        return attendantData
    }

    lateinit var outletClose: Attendant

    lateinit var initData: InitAllOutlets

    fun fetchsAllCustomers(depotid: Int, regionid: Int): LiveData<SalesRepAndCustomerData> {
        val mResult = MutableLiveData<SalesRepAndCustomerData>()
        repository.naviBtwcustAndRe()
            .subscribe({ localData ->
                if (localData == 0) {
                    repository.tmreplist(depotid, regionid)
                        .subscribe({ apiData ->
                            val data: SalesReps = apiData.body()!!
                            if (data.allreps!!.isEmpty()) {
                                salesRepCustdata(
                                    mResult,
                                    400,
                                    "Please TM, no rep is assign to you",
                                    emptyrep,
                                    emptyoutlet
                                )
                            } else {
                                salesRepCustdata(mResult, 200, "", data.allreps!!, emptyoutlet)
                            }
                        }, {
                            salesRepCustdata(mResult, 400, "${it.message}", emptyrep, emptyoutlet)
                        }).isDisposed
                } else {
                    repository.fetchAllCustomers()
                        .subscribe({
                            salesRepCustdata(mResult, 300, "", emptyrep, it)
                        }, {
                            salesRepCustdata(mResult, 400, "${it.message}", emptyrep, emptyoutlet)
                        }).isDisposed
                }
            }, {
                salesRepCustdata(mResult, 400, "${it.message}", emptyrep, emptyoutlet)
            }).isDisposed
        return mResult
    }

    fun getAllCustomerReps(repid: Int, tmid: Int) {
        repository.tmrepcustomer(repid, tmid)
            .subscribe(
                {
                    initData = it.body()!!
                    if (initData.status == 200) {
                        inserIntoTable()
                    } else {
                        repCustdata(repSelection, initData.status, initData.notis)
                    }
                },
                {
                    repCustdata(repSelection, 401, "${it.message}")
                }
            ).isDisposed
    }

    private fun inserIntoTable() {
        repository.saveEntityAllOutletsList(
            initData.alloutlets!!.map { it.toEntityAllOutletsList() }
        ).subscribe({
            repCustdata(repSelection, initData.status, initData.notis)
        }, {
            repCustdata(repSelection, 401, "${it.message}")
        }).isDisposed
    }

    fun ValidateSeque(id: Int, nexts: Int, lat: Double, lng: Double): LiveData<CloseAndOpenOutlet> {
        val nInt = MutableLiveData<CloseAndOpenOutlet>()
        repository.ValidateSeque(id)
            .subscribe(
                {
                    val intArray = nexts in it.self.split(",").map { it.toInt() }
                    when {
                        nexts == it.nexts -> {
                            CloseAndOpenOutletBiData(nInt, 200, lat, lng, it.nexts, it.self, it.id)
                        }
                        intArray -> {
                            CloseAndOpenOutletBiData(nInt, 300, lat, lng, it.nexts, it.self, it.id)
                        }
                        else -> {
                            CloseAndOpenOutletBiData(nInt, 400, lat, lng, it.nexts, it.self, it.id)
                        }
                    }
                }, {
                    CloseAndOpenOutletBiData(nInt, 500, 0.0, 0.0, 0, "0", 0)
                }).isDisposed

        return nInt
    }

    fun CloseOutlets(
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
        id: Int,
        nexts: Int,
        self: String,
        sep: Int,
        auto: Int
    ) {
        repository.CloseOutlets(
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
            urno
        )
            .subscribe({
                outletClose = it.body()!!
                when (sep) {
                    1 -> {
                        UpdateSeque(id, nexts, self, auto)
                    }
                    2 -> {
                        AttendanBiData(attendantData, outletClose.status, outletClose.notis)
                    }
                }
            }, {
                AttendanBiData(attendantData, 300, it.message.toString())
            }).isDisposed
    }

    fun UpdateSeque(id: Int, nexts: Int, self: String, auto: Int) {
        repository.UpdateSeque(id, nexts, self).subscribe({
            setEntryTime(auto)
        }, {
            AttendanBiData(attendantData, 300, it.message.toString())
        }).isDisposed
    }

    fun setEntryTime(auto: Int) {
        repository.setEntryTime(appTime(), auto).subscribe({
            AttendanBiData(attendantData, outletClose.status, outletClose.notis)
        }, {
            AttendanBiData(attendantData, 300, it.message.toString())
        }).isDisposed
    }


    fun AsynData(repid:Int, tmid:Int, auto:Int) {
        repository.AsynData(repid,tmid).subscribe(
            {

            },{

            }
        ).isDisposed
    }

    companion object {
        private val TAG = "ALLTo"
    }
}