package com.mobbile.paul.mttms.ui.outlets.entries

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.EntryCallback
import com.mobbile.paul.mttms.models.InitBbasket
import com.mobbile.paul.mttms.models.toEntityGetSalesEntry
import com.mobbile.paul.mttms.providers.Repository
import javax.inject.Inject

class EntriesViewModel @Inject constructor(private var repository: Repository) : ViewModel() {

    var mData = MutableLiveData<EntryCallback>()

    fun basketData(): MutableLiveData<EntryCallback> {
        return mData
    }

    var obj = EntryCallback()

    var dataValues: InitBbasket? = null

    fun fetchSales(customerno: String, customer_code: String, repid: Int) {
        repository.getbasket(customerno, customer_code, repid)
            .subscribe(
                {
                    dataValues = it.body()!!
                    deleteEntityGetSalesEntry()
                }
            ) {
            }.isDisposed
    }

    fun deleteEntityGetSalesEntry() {
        repository.deleteEntityGetSalesEntry().subscribe({
            createDailySales()
        }, {
        }).isDisposed
    }

    fun createDailySales() {
        repository.createDailySales(dataValues!!.entry!!.map { it.toEntityGetSalesEntry() })
            .subscribe({
                obj.status = dataValues!!.status
                obj.msg = "CHECK HERE"
                obj.data = dataValues!!.sales
                mData.postValue(obj)
            }, {

            }).isDisposed
    }

    fun updateDailySales(inventory: Double, pricing: Int, entry_time: String, controlpricing:String, controlinventory:String, product_code:String){
        repository.updateDailySales(inventory,pricing,entry_time,controlpricing,controlinventory,product_code)
            .subscribe({
            },{

            }).isDisposed
    }

    fun validateEntryStatus(): MutableLiveData<Int> {
        var mResult = MutableLiveData<Int>()
        repository.validateSalesEntry()
            .subscribe({
                mResult.postValue(it)
                Log.d(TAG, it.toString())
            }, {
                Log.d(TAG, it.message.toString())
                mResult.postValue(null)
            }).isDisposed
        return mResult
    }

    companion object {
        var TAG = "EntriesViewModel"
    }

}