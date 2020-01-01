package com.mobbile.paul.mttms.ui.outlets.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.DetailsList
import com.mobbile.paul.mttms.providers.Repository
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val repository: Repository):ViewModel() {

    fun getEntryDetails(urno: Int): LiveData<List<DetailsList>> {

        var mResult = MutableLiveData<List<DetailsList>>()
        repository.getEntryDetails(urno)
            .subscribe({
                //Log.d(TAG, it.body()!!.data.toString())
                mResult.postValue(it.body()!!.data)
            },{
                mResult.postValue(emptyList())
            }).isDisposed

        return mResult
    }

    companion object{
        val TAG = "CJSCHSDCDSJCODSNIDNI"
    }
}