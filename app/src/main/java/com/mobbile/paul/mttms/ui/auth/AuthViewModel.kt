package com.mobbile.paul.mttms.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.mttms.models.*
import com.mobbile.paul.mttms.providers.Repository
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private lateinit var data: UserAuth

    private var dataPasser = AuthObjectData()

    private var ObResult = MutableLiveData<AuthObjectData>()

    fun authObservable(): MutableLiveData<AuthObjectData> {
        return ObResult
    }

    fun userAuth(username: String, password: String, imei: String, dateCheck: Boolean) {

        repository.userAuth(username, password, imei)
            .subscribe({

                if (it.isSuccessful && it.body() != null && it.code() == 200 && it.body()!!.status == "OK") {

                    data = it.body()!!

                    if (!dateCheck) {
                        deleteModules()
                    } else {
                        mSG(it.body()!!.msg, 200, it.body()!!.employee_id)
                    }

                } else {
                    mSG(it.body()!!.msg, 400, 0)
                }

            }, {
                mSG(it.message.toString(), 400, 0)
            }).isDisposed
    }

    private fun deleteModules() {
        repository.deleteModules()
            .subscribe(
                {
                    deleteRepList()
                }, {
                    mSG(it.message.toString(), 400, 0)
                }
            ).isDisposed
    }

    private fun deleteRepList() {
        repository.deleteRepList()
            .subscribe(
                {
                    deleteSpiners()
                }, {
                    mSG(it.message.toString(), 400, 0)
                }
            ).isDisposed
    }

    private fun deleteSpiners() {
        repository.deleteSpiners()
            .subscribe(
                {
                    inserIntoTable()
                }, {
                    mSG(it.message.toString(), 400, 0)
                }
            ).isDisposed
    }

    private fun inserIntoTable() {
        repository.saveModulesANDspiners(
            data.modules!!.map { it.toEntityModules() },
            data.spinners!!.map { it.toEntitySpiners() }
        ).subscribe({
            mSG(data.msg, 200, data.employee_id)
        }, {
            mSG(it.message.toString(), 200, data.employee_id)
        }).isDisposed
    }

    private fun mSG(msg: String, status: Int, employeeid: Int) {
        dataPasser.status = status
        dataPasser.msg = msg
        dataPasser.employeeid = employeeid
        ObResult.postValue(dataPasser)
    }

    companion object {
        val TAG = "AuthViewModel"
    }
}