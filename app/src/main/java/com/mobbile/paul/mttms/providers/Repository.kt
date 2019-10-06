package com.mobbile.paul.mttms.providers

import com.mobbile.paul.mttms.models.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject
constructor(private val appDao: AppDao, private val api: Api) {

    fun userAuth(username: String, password: String, imei: String): Single<Response<UserAuth>> =
        api.getUser(username, password, imei)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it
            }

    fun deleteModules() = Observable.fromCallable {
        appDao.deleteModules()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun deleteRepList() = Observable.fromCallable {
        appDao.deleteRepList()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun deleteSpiners() = Observable.fromCallable {
        appDao.deleteSpiners()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun saveModulesANDspiners(
        modules: List<EntityModules>,
        spinners: List<EntitySpiners>
    ) = Observable.fromCallable {
        appDao.saveModulesANDspiners(
            modules,spinners
        )
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun fetchModules(): Single<List<EntityModules>> =
        Single.fromCallable {
            appDao.fetchModules()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun fetchAllCustomers(depotid: Int, regionid: Int): Single<Response<InitAllCustomers>> =
        api.fetchAllCustomers(depotid, regionid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it
            }

    fun insertIntoAllcustomers(auto:Int, employeeid:Int, ecode:String, custcode:String, fullname:String) = Observable.fromCallable {
        appDao.insertIntoAllcustomers(auto, employeeid, ecode, custcode, fullname)
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())


    fun fetchEntityAllCustomersList(): Single<List<EntityAllCustomersList>> =
        Single.fromCallable {
            appDao.fetchEntityAllCustomersList()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun fetchAllOutlets(employeeid: Int): Single<Response<InitAllOutlets>> =
        api.fetchAllOutlets(employeeid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it
            }

    fun saveEntityAllOutletsList(
        alloutlets: List<EntityAllOutletsList>
    ) = Observable.fromCallable {
        appDao.saveEntityAllOutletsList(
            alloutlets
        )
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun fetchEntityAllOutletsList(): Single<List<EntityAllOutletsList>> =
        Single.fromCallable {
            appDao.fetchEntityAllOutletsList()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())



}


