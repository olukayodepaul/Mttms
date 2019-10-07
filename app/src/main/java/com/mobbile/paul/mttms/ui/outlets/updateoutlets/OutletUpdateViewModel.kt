package com.mobbile.paul.mttms.ui.outlets.updateoutlets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.EntitySpiners
import com.mobbile.paul.mttms.providers.Repository
import javax.inject.Inject

class OutletUpdateViewModel @Inject constructor(private var repository: Repository): ViewModel() {

    fun fetchSpinners(): LiveData<List<EntitySpiners>> {

        var mResult = MutableLiveData<List<EntitySpiners>>()

        repository.fetchSpinners()
            .subscribe({
                mResult.postValue(it)
            }, {
                mResult.postValue(null)
            }).isDisposed

        return mResult
    }

}