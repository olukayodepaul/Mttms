package com.mobbile.paul.mttms.ui.attendant_basket



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.*
import com.mobbile.paul.mttms.providers.Repository
import com.mobbile.paul.mttms.util.Util.AttendanBiData
import com.mobbile.paul.mttms.util.Util.appTime
import com.mobbile.paul.mttms.util.Util.repBaskets
import javax.inject.Inject

class AttendantViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    lateinit var  at_Data: Attendant

    private val emptyList: List<Products> = emptyList()

    private val attendantData = MutableLiveData<AttendantData>()

    fun attendantMutable(): LiveData<AttendantData> {
        return attendantData
    }

    fun getRepBasket(customerno: String): LiveData<ProductBiData> {
        val mResult = MutableLiveData<ProductBiData>()
        repository.getCustomerNo(customerno)
            .subscribe({
                val data: Basket = it.body()!!
                if (data.status == 200) {
                    repBaskets(mResult, data.status, data.notis, data.allrepsproducts!!)
                } else {
                    repBaskets(mResult, data.status, data.notis, emptyList)
                }
            }, {
                repBaskets(mResult, 400, "No Basket available for this Rep", emptyList)
            }).isDisposed
        return mResult
    }

    fun takeAttendant(tmid: Int, taskid: Int, repid: Int,sortid:Int,sequenceno:String) {
        repository.takeAttendant(tmid, taskid, repid)
            .subscribe({
                 at_Data = it.body()!!
                if (at_Data.status == 200 && taskid == 2) {
                    //generate time here from the application.
                    setAttendantTime(appTime(), sortid, sequenceno)
                } else {
                    AttendanBiData(attendantData, at_Data.status, at_Data.notis)
                }
            }, {
                AttendanBiData(attendantData, 400, "${it.message}")
            }).isDisposed
    }

    fun setAttendantTime(time:String,sortid:Int,sequenceno:String) {
        repository.setAttendantTime(time)
            .subscribe({
                SequencetManager(sortid,sequenceno)
            },{
                AttendanBiData(attendantData, 400, "${it.message}")
            }).isDisposed
    }

    fun SequencetManager(sortid:Int,sequenceno:String) {
        repository.SequencetManager(sortid, sequenceno.toInt()+1, "0,${sequenceno}")
            .subscribe({
                AttendanBiData(attendantData, at_Data.status, at_Data.notis)
            },{
                AttendanBiData(attendantData, 400, "${it.message}")
            }).isDisposed
    }

    companion object {
        val TAG = "AttendantViewModel"
    }
}
