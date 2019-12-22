package com.mobbile.paul.mttms.ui.outlets

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.AllOutletsList
import com.mobbile.paul.mttms.models.EntityAllOutletsList
import com.mobbile.paul.mttms.models.toEntityAllOutletsList
import com.mobbile.paul.mttms.providers.Repository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class OutletViewmodel @Inject constructor(private val repository: Repository): ViewModel() {

    var nResult = MutableLiveData<List<AllOutletsList>>()

    fun MutableAllOuletList(): LiveData<List<AllOutletsList>>{
        return nResult
    }

    var callback: List<AllOutletsList>? = null

    fun fetchAllOutlets(employeeid: Int, today: String) {

        repository.fetchAllOutlets(employeeid, today).subscribe({
            if (it.isSuccessful && it.body() != null && it.code() == 200 ) {
                callback = it.body()!!.alloutlets
                saveEntityAllOutletsList(it.body()!!.alloutlets)
            }
        },{
            Log.d(TAG, it.message.toString())
        }).isDisposed
    }

    fun saveEntityAllOutletsList(outlets: List<AllOutletsList>?){
        repository.saveEntityAllOutletsList(
            outlets!!.map { it.toEntityAllOutletsList()}
        ).subscribe({
            nResult.postValue(callback)
        },{
            Log.d(TAG, it.message.toString())
        }).isDisposed
    }

    fun fetchLocalOutlet(): LiveData<List<EntityAllOutletsList>>{
        var mResult = MutableLiveData<List<EntityAllOutletsList>>()
        repository.fetchEntityAllOutletsList()
            .subscribe({
                mResult.postValue(it)
            },{
                Log.d(TAG, it.message.toString())
            }).isDisposed
        return mResult
    }

    fun takeTmVit(rep_id: Int, tm_id: Int, entry_date: String, entry_date_time: String): LiveData<String> {
        var mResult = MutableLiveData<String>()
        repository.tmVisit(rep_id, tm_id, entry_date, entry_date_time)
            .subscribe({
                if(it.body()!!.status=="OK") {
                    mResult.postValue(it.body()!!.status)
                }else{
                    mResult.postValue(it.body()!!.status)
                }
            },{
                mResult.postValue(it.message)
            }).isDisposed
        return mResult
    }

    companion object{
        var TAG = "OutletViewmodel"
    }

}

