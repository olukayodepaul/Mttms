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

    fun updateCards(employee_id: Int, urno: String, outletclassid: Int, outletlanguageid: Int, outlettypeid: Int,
        outletname: String, outletaddress: String, contactname: String, contactphone: String, latitude: String,
        longitude: String, outlet_pic: String, entry_date_time: String, entry_date: String
    ) : LiveData<String> {

        var mResult = MutableLiveData<String>()

        val map = HashMap<String, RequestBody>()
        val file = File(outlet_pic)
        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
        map["map\"; filename=\"" + file.name + "\""] = requestBody

        repository.updateCards(employee_id, urno, outletclassid, outletlanguageid, outlettypeid, outletname, outletaddress, contactname,
            contactphone, latitude, longitude, entry_date_time, entry_date, map)
            .subscribe({
                mResult.postValue(it.body()!!.status)
            },{
                mResult.postValue(it.message)
            }).isDisposed

        return mResult
    }

}