package com.mobbile.paul.mttms.ui.outlets.mapoutlet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.EntitySpiners
import com.mobbile.paul.mttms.models.Responses
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

    fun mapOutlet(repid: Int, tmid: Int, urno: Int, latitude: Double, longitude: Double, outletname: String, contactname: String,
                     outletaddress: String, contactphone: String, outletclassid: Int, outletlanguage: Int,
                     outlettypeid: Int): LiveData<Responses>{

        val mResult = MutableLiveData<Responses>()
        val rt = Responses()

        repository.mapOutlet(repid, tmid,urno,latitude,longitude,outletname,contactname,outletaddress,contactphone,
            outletclassid,outletlanguage,outlettypeid)
            .subscribe({

                rt.notis = it.body()!!.notis
                rt.status = it.body()!!.status
                mResult.postValue(rt)

            },{
                rt.notis = it.message.toString()
                rt.status = 300
                mResult.postValue(rt)

            }).isDisposed
        return mResult
    }

}