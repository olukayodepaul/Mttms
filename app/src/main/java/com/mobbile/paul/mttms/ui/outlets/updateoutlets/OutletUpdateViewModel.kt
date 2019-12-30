package com.mobbile.paul.mttms.ui.outlets.updateoutlets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.EntitySpiners
import com.mobbile.paul.mttms.models.getCards
import com.mobbile.paul.mttms.providers.Repository
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class OutletUpdateViewModel @Inject constructor(private var repository: Repository) : ViewModel() {

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

    fun updateOutlet(repid:Int, tmid: Int, lat: Double, lng: Double, customername: String, contactname: String,
                  address: String, phonenumber: String, outletclass: Int, outletlanguage: Int,outlettype: Int) {
        repository.updateOutlet(repid, tmid,lat,lng,customername,contactname,address,phonenumber,outletclass,outletlanguage,outlettype)
            .subscribe({

            },{

            }).isDisposed

    }



}