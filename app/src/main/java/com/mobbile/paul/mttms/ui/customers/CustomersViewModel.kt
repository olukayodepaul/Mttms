package com.mobbile.paul.mttms.ui.customers


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.*
import com.mobbile.paul.mttms.providers.Repository
import com.mobbile.paul.mttms.util.Util.repCustdata
import com.mobbile.paul.mttms.util.Util.salesRepCustdata
import javax.inject.Inject

class CustomersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val emptyoutlet: List<EntityAllOutletsList> = emptyList()

    private val emptyrep: List<AllTheSalesRep> = emptyList()

    var repSelection = MutableLiveData<repAndCustomerData>()

    fun selectAnyReps(): LiveData<repAndCustomerData> {
        return repSelection
    }

    lateinit var  initData: InitAllOutlets

    fun fetchsAllCustomers(depotid: Int, regionid: Int): LiveData<SalesRepAndCustomerData> {
        val mResult = MutableLiveData<SalesRepAndCustomerData>()
        repository.naviBtwcustAndRe()
            .subscribe({ localData ->
                if(localData==0){
                    repository.tmreplist(depotid, regionid)
                        .subscribe({apiData ->
                            val  data: SalesReps = apiData.body()!!
                            if (data.allreps!!.isEmpty()) {
                                salesRepCustdata(mResult, 400, emptyrep, emptyoutlet)
                            } else {
                                salesRepCustdata(mResult, 200, data.allreps!!, emptyoutlet)
                            }
                        },{
                            salesRepCustdata(mResult, 400, emptyrep, emptyoutlet)
                        }).isDisposed
                }else{
                    repository.fetchAllCustomers()
                        .subscribe({
                            salesRepCustdata(mResult, 300, emptyrep, it)
                        },{
                            salesRepCustdata(mResult, 400, emptyrep, emptyoutlet)
                        }).isDisposed
                }
            }, {
                salesRepCustdata(mResult, 400, emptyrep, emptyoutlet)
            }).isDisposed
        return mResult
    }

    fun getAllCustomerReps(repid: Int, tmid:Int) {
        repository.tmrepcustomer(repid,tmid)
            .subscribe(
                {
                    initData  = it.body()!!
                    if(initData.status==200) {
                        inserIntoTable()
                    }else {
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
            initData.alloutlets!!.map{ it.toEntityAllOutletsList()}
        ).subscribe({
            repCustdata(repSelection, initData.status, initData.notis)
        },{
            repCustdata(repSelection, 401, "${it.message}")
        }).isDisposed
    }

    companion object {
        private val TAG = "ALLTo"
    }
}