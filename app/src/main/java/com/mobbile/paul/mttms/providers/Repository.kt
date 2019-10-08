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

    fun fetchAllOutlets(employeeid: Int, today: String): Single<Response<InitAllOutlets>> =
        api.fetchAllOutlets(employeeid,today)
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

    fun fetchSpinners(): Single<List<EntitySpiners>> =
        Single.fromCallable {
            appDao.fetchSpinners()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getbasket(employee_id: Int, customer: String, urno: String): Single<Response<InitBbasket>> =
        api.getbasket(employee_id, customer, urno )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

    fun deleteEntityGetSalesEntry() = Observable.fromCallable {
        appDao.deleteEntityGetSalesEntry()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun createDailySales(
        salesen: List<EntityGetSalesEntry>
    ) =
        Observable.fromCallable { appDao.saveSalesEntry(salesen) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateDailySales(orders: Double, inventory: Double, pricing: Int, entry_time: String,  product_id: String,
                         salesprice: Double, contOrder: String, contPrincing: String, contInventory: String)  =
        Observable.fromCallable {
            appDao.updateDailySales(orders, inventory, pricing, entry_time,  product_id,
                salesprice, contOrder, contPrincing, contInventory )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun validateSalesEntry(): Observable<Int> =
        Observable.fromCallable {
            appDao.validateSalesEntry()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())



}


