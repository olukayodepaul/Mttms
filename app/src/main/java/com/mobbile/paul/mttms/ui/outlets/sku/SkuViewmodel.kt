package com.mobbile.paul.mttms.ui.outlets.sku

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.EntityGetSalesEntry
import com.mobbile.paul.mttms.models.SumSales
import com.mobbile.paul.mttms.providers.Repository
import javax.inject.Inject

class SkuViewmodel @Inject constructor(private var repository: Repository): ViewModel() {

    fun fetch() : LiveData<List<EntityGetSalesEntry>> {

        var mResult = MutableLiveData<List<EntityGetSalesEntry>>()

        repository.fetchAllEntryPerDaily()
            .subscribe({
                mResult.postValue(it)
            },{
                mResult.postValue(null)
            }).isDisposed

        return mResult
    }

    fun sumAllSalesEntry() : LiveData<SumSales> {

        var mResult = MutableLiveData<SumSales>()

       /* repository.sumAllSalesEntry()
            .subscribe({
                mResult.postValue(it)
            },{
                mResult.postValue(null)
            }).isDisposed
     */
        return mResult
    }
}