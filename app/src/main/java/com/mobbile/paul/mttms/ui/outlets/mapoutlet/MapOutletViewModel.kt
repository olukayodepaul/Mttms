package com.mobbile.paul.mttms.ui.outlets.mapoutlet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.EntitySpiners
import com.mobbile.paul.mttms.providers.Repository
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class MapOutletViewModel @Inject constructor(private var repository: Repository): ViewModel() {

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

    fun mapOutlet(tmid: Int, lat: Double, lng: Double, customername: String, contactname: String,
                       address: String, phonenumber: String, outletclass: Int, outletlanguage: Int,outlettype: Int) {
        repository.mapOutlet(tmid,lat,lng,customername,contactname,address,phonenumber,outletclass,outletlanguage,outlettype)
            .subscribe({

            },{

            }).isDisposed

    }


}