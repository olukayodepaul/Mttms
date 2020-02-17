package com.mobbile.paul.mttms.ui.modules

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.EntityModules
import com.mobbile.paul.mttms.providers.Repository
import javax.inject.Inject

class ModulesViewModel @Inject constructor(private val repository: Repository): ViewModel(){

    fun getAllUsersModules(): LiveData<List<EntityModules>> {
        val mResult = MutableLiveData<List<EntityModules>>()
        repository.fetchModules()
            .subscribe({
                mResult.postValue(it)
                Log.d(TAG,"onObserve "+ it as ArrayList<EntityModules>)
            },{
                mResult.postValue(emptyList())
            }).isDisposed
        return mResult
    }

    companion object {
        private val TAG = "ModulesViewMode"
    }
}