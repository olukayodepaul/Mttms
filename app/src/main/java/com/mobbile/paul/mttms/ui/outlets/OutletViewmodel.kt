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

    fun fetchAllOutlets(employeeid: Int) {

        repository.fetchAllOutlets(employeeid).subscribe({
            if (it.isSuccessful && it.body() != null && it.code() == 200 && it.body()!!.status == "OK") {
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
        }).isDisposed
    }

    fun fetchLocalOutlet(): LiveData<List<EntityAllOutletsList>>{
        var mResult = MutableLiveData<List<EntityAllOutletsList>>()
        repository.fetchEntityAllOutletsList()
            .subscribe({
                mResult.postValue(it)
            },{
            }).isDisposed
        return mResult
    }



    companion object{
        var TAG = "OutletViewmodel"
    }

}

