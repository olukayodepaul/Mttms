package com.mobbile.paul.mttms.ui.customers


import android.util.Log
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

    private val attendantData = MutableLiveData<AttendantData>()

    private val res = MutableLiveData<Responses>()

    fun selectAnyReps(): LiveData<repAndCustomerData> {
        return repSelection
    }

    fun closeOutletMutable(): LiveData<AttendantData> {
        return attendantData
    }

    fun responds(): LiveData<Responses> {
        return res
    }

    lateinit var outletClose: Attendant

    lateinit var initData: InitAllOutlets

    lateinit var asy_data: OutletAsyn

    val rst = Responses()

    fun fetchsAllCustomers(depotid: Int, regionid: Int): LiveData<SalesRepAndCustomerData> {
        val mResult = MutableLiveData<SalesRepAndCustomerData>()
        repository.naviBtwcustAndRe()
            .subscribe({ localData ->
                if (localData == 0) {//switch ou
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
                            CloseAndOpenOutletBiData(nInt, 200, lat, lng, it.nexts, it.self, 1)
                        }
                        intArray -> {
                            CloseAndOpenOutletBiData(nInt, 300, lat, lng, it.nexts, it.self, 1)
                        }
                        else -> {
                            CloseAndOpenOutletBiData(nInt, 400, lat, lng, it.nexts, it.self, 1)
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
                        UpdateSeque(1, visitsequence.toInt(), self, auto)
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
        repository.UpdateSeque(id,nexts+1, ",$nexts").subscribe({
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


    fun CustometInfoAsync(urno:Int,auto:Int) {

        repository.CustometInfoAsync(urno)
            .subscribe(
                {
                    asy_data = it.body()!!
                    UpdateCustomerInformation(asy_data,auto)
                },{
                    rst.status = 400
                    rst.notis = it.message.toString()
                    res.postValue(rst)
                }
            ).isDisposed
    }

    fun UpdateCustomerInformation(allCustInfo:OutletAsyn, auto:Int){
        repository.updateIndividualCustomer(allCustInfo.outletclassid,allCustInfo.outletlanguageid,allCustInfo.outlettypeid,
            allCustInfo.outletname,allCustInfo.outletaddress,allCustInfo.contactname,allCustInfo.contactphone,allCustInfo.latitude.toDouble(),
            allCustInfo.longitude.toDouble(), auto)
            .subscribe({
                rst.status = 200
                rst.notis = ""
                res.postValue(rst)
            },{
                rst.status = 400
                rst.notis = it.message.toString()
                res.postValue(rst)
            }).isDisposed
    }

    companion object {
        private val TAG = "HDBCJBDHCBDHJBCDJS"
    }
}