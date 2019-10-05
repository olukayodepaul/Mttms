package com.mobbile.paul.mttms.providers

import com.mobbile.paul.mttms.models.EntityModules
import com.mobbile.paul.mttms.models.EntitySpiners
import com.mobbile.paul.mttms.models.UserAuth
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


}


