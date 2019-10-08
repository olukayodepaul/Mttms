package com.mobbile.paul.mttms.ui.outlets.entries

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.EntryCallback
import com.mobbile.paul.mttms.models.InitBbasket
import com.mobbile.paul.mttms.models.toEntityGetSalesEntry
import com.mobbile.paul.mttms.providers.Repository
import javax.inject.Inject

class EntriesViewModel @Inject constructor(private var repository: Repository): ViewModel() {

    var mData = MutableLiveData<EntryCallback>()

    fun comData(): MutableLiveData<EntryCallback> {
        return mData
    }

    var obj = EntryCallback()

    var dataValues: InitBbasket? = null

    fun fetchSales(employee_id: Int,  customer: String, urno: String) {

        repository.getbasket(employee_id, customer, urno)
            .subscribe(
                {
                    dataValues = it.body()
                    deleteEntityGetSalesEntry()
                }
            ) {
                obj.status = "FAIL"
                obj.msg = it.message!!
                obj.data = null
                mData.postValue(obj)
            }.isDisposed

    }

    fun deleteEntityGetSalesEntry() {

        repository.deleteEntityGetSalesEntry().subscribe({
            createDailySales()
        },{
            obj.status = "FAIL"
            obj.msg = it.message!!
            obj.data = null
            mData.postValue(obj)
        }).isDisposed
    }

    fun createDailySales() {

        repository.createDailySales(dataValues!!.entry!!.map {it.toEntityGetSalesEntry()})
            .subscribe({
                obj.status = dataValues!!.status
                obj.msg = "CHECK HERE"
                obj.data = dataValues!!.sales
                mData.postValue(obj)
            },{
                obj.status = "FAIL"
                obj.msg = it.message!!
                obj.data = null
                mData.postValue(obj)
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

    companion object{
        var TAG = "EntriesViewModel"
    }

}