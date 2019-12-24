package com.mobbile.paul.mttms.ui.attendant_basket


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.Basket
import com.mobbile.paul.mttms.models.ProductBiData
import com.mobbile.paul.mttms.models.Products
import com.mobbile.paul.mttms.providers.Repository
import com.mobbile.paul.mttms.util.Util.repBaskets
import javax.inject.Inject

class AttendantViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val emptyList: List<Products> = emptyList()

    fun getRepBasket(customerno:String): LiveData<ProductBiData> {

        val mResult = MutableLiveData<ProductBiData>()

        repository.getCustomerNo(customerno)
            .subscribe({
                val data: Basket = it.body()!!
                if(data.status==200) {
                    repBaskets(mResult,data.status, data.notis, data.allrepsproducts!!)
                }else{
                    repBaskets(mResult,data.status, data.notis, emptyList)
                }
            },{
                repBaskets(mResult,400,"No Basket available for this Rep", emptyList)
            }).isDisposed

        return mResult
    }

    companion object{
        val TAG = "AttendantViewModel"
    }
}
