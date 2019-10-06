package com.mobbile.paul.mttms.ui.customers


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.*
import com.mobbile.paul.mttms.providers.Repository
import javax.inject.Inject

class CustomersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {


    fun persistAndFetchCustomers(
        auto: Int,
        employeeid: Int,
        ecode: String?,
        custcode: String?,
        fullname: String?
    ): LiveData<List<EntityAllCustomersList>> {
        var mResult = MutableLiveData<List<EntityAllCustomersList>>()
        repository.insertIntoAllcustomers(auto, employeeid, ecode!!, custcode!!, fullname!!)
            .subscribe({
                repository.fetchEntityAllCustomersList()
                    .subscribe({
                        mResult.postValue(it)
                    }, {

                    }).isDisposed
            }, {
            }).isDisposed
        return mResult
    }

    fun fetchAllCustomers(depotid: Int, regionid: Int): LiveData<InitAllCustomers> {
        var mResult = MutableLiveData<InitAllCustomers>()
        repository.fetchAllCustomers(depotid, regionid)
            .subscribe({
                if (it.isSuccessful && it.body() != null && it.code() == 200 && it.body()!!.status == "OK") {
                    Log.d(TAG, it.body().toString())
                    mResult.postValue(it.body())
                }
            }, {
                Log.d(TAG, it.message.toString())
            }).isDisposed
        return mResult
    }

    fun fetchCustOnly(): LiveData<List<EntityAllCustomersList>> {
        var mResult = MutableLiveData<List<EntityAllCustomersList>>()
        repository.fetchEntityAllCustomersList()
            .subscribe({
                mResult.postValue(it)
            }, {
            }).isDisposed
        return mResult
    }


    companion object {
        private val TAG = "CustomersViewModel"
    }
}