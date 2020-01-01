package com.mobbile.paul.mttms.ui.auth


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.*
import com.mobbile.paul.mttms.providers.Repository
import com.mobbile.paul.mttms.util.Util.AuthbiData
import com.mobbile.paul.mttms.util.Util.appDate
import com.mobbile.paul.mttms.util.Util.appTime
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private lateinit var data: UserAuth

    private var responseData = MutableLiveData<AuthBiData>()

    fun authObservable(): MutableLiveData<AuthBiData> {
        return responseData
    }

    fun userAuth(username: String, password: String, imei: String, appdate: String) {
        repository.userAuth(username, password, imei)
            .subscribe({

                data = it.body()!!

                if(it.isSuccessful && it.body() != null && it.code() == 200 && it.body()!!.status==200) {
                    if(data.modules!!.isEmpty() && data.spinners!!.isEmpty()) {
                        AuthbiData(0,responseData,0,0,0,500,"Error, App Module and TM Rep is missing from the endpoint. Please contact developer")
                    }else{
                        if(appdate != appDate()) {
                            Log.d(TAG, "${appdate} ${appTime()} 2")
                            deleteModules()
                        }else{
                            Log.d(TAG, "${appdate} ${appTime()} 4")
                            AuthbiData(2,responseData,data.depots_id,data.region_id,data.employee_id,data.status,data.notification)
                        }
                    }
                }else{
                    AuthbiData(0,responseData,data.depots_id,data.region_id,data.employee_id,data.status,data.notification)
                }
            }, {
                AuthbiData(0,responseData,0,0,0,500,it.message.toString())
            }).isDisposed
    }

    private fun deleteModules() {
        repository.deleteModules()
            .subscribe(
                {
                    deleteSpiners()
                }, {
                    AuthbiData(0,responseData,0,0,0,500,it.message.toString())
                }
            ).isDisposed
    }

    private fun deleteSpiners() {
        repository.deleteSpiners()
            .subscribe(
                {
                    custometvisitsequence()
                }, {
                    AuthbiData(0,responseData,0,0,0,500,it.message.toString())
                }
            ).isDisposed
    }

    private fun custometvisitsequence() {
        repository.custometvisitsequence().subscribe(
            {
                deleteAlloutlets()
            },{
                AuthbiData(0,responseData,0,0,0,500,it.message.toString())
            }
        ).isDisposed
    }

    private fun deleteAlloutlets(){
        repository.deleteAlloutlets()
            .subscribe(
                {
                    inserIntoTable()
                }, {
                    AuthbiData(0,responseData,0,0,0,500,it.message.toString())
                }
            ).isDisposed
    }

    private fun inserIntoTable() {
        repository.saveModulesANDspiners(
            data.modules!!.map{it.toEntityModules()},
            data.spinners!!.map{it.toEntitySpiners()}
        ).subscribe({
            AuthbiData(1,responseData,data.depots_id,data.region_id,data.employee_id,data.status,data.notification)
        },{
            AuthbiData(0,responseData,0,0,0,500,it.message.toString())
        }).isDisposed
    }

    companion object {
        val TAG = "AuthViewModel"
    }
}