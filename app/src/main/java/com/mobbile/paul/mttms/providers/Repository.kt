package com.mobbile.paul.mttms.providers

import com.mobbile.paul.mttms.models.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject
constructor(private val appDao: AppDao, private val api: Api, private val nodejsApi: NodejsApi) {

    fun userAuth(username: String, password: String, imei: String): Single<Response<UserAuth>> =
        nodejsApi.getUser(username, password, imei)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it
            }

    fun deleteModules() = Observable.fromCallable {
        appDao.deleteModules()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    /*fun deleteRepList() = Observable.fromCallable {
        appDao.deleteRepList()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

     */

    fun deleteSpiners() = Observable.fromCallable {
        appDao.deleteSpiners()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun custometvisitsequence() = Observable.fromCallable {
        appDao.custometvisitsequence()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun deleteAlloutlets() = Observable.fromCallable {
        appDao.deleteAlloutlets()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())


    /*fun deleteAllcustomers() = Observable.fromCallable {
        appDao.deleteAllcustomers()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun deleteAlloutlets() = Observable.fromCallable {
        appDao.deleteAlloutlets()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())*/

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

    /*fun fetchAllCustomers(depotid: Int, regionid: Int): Single<Response<InitAllCustomers>> =
        nodejsApi.fetchAllCustomers(depotid, regionid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it
            }

     */

    /*fun insertIntoAllcustomers(auto:Int, employeeid:Int, ecode:String, custcode:String, fullname:String, mode:String) = Observable.fromCallable {
        appDao.insertIntoAllcustomers(auto, employeeid, ecode, custcode, fullname, mode)
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

     */


    /*fun fetchEntityAllCustomersList(): Single<List<EntityAllCustomersList>> =
        Single.fromCallable {
            appDao.fetchEntityAllCustomersList()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

     */

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

    /*fun updateDailySales(orders: Double, inventory: Double, pricing: Int, entry_time: String,  product_id: String,
                         salesprice: Double, contOrder: String, contPrincing: String, contInventory: String)  =
        Observable.fromCallable {
            appDao.updateDailySales(orders, inventory, pricing, entry_time,  product_id,
                salesprice, contOrder, contPrincing, contInventory )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    */



    fun fetchAllEntryPerDaily(): Observable<List<EntityGetSalesEntry>> =
        Observable.fromCallable {
            appDao.fetchAllEntryPerDay()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    fun createNewCards(employee_id: Int, outletclassid:Int, outletlanguageid: Int, outlettypeid:Int,
                 outletname:String, outletaddress:String, contactname: String, contactphone:String, latitude:String,
                 longitude:String,  entry_date_time:String, entry_date:String, outlet_pic: Map<String, RequestBody>): Single<Response<getCards>> =
        api.createNewCards(employee_id,  outletclassid, outletlanguageid, outlettypeid,
            outletname, outletaddress, contactname, contactphone, latitude,
            longitude, outlet_pic, entry_date_time, entry_date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

    fun tmVisit( rep_id: Int, tm_id: Int, entry_date: String, entry_date_time: String): Single<Response<getCards>> =
        api.tmVisit( rep_id, tm_id, entry_date, entry_date_time)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

    //new adjustment
    fun naviBtwcustAndRe(): Single<Int> =
        Single.fromCallable{
            appDao.naviBtwcustAndRep()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun tmreplist(depotid: Int, regionid: Int): Single<Response<SalesReps>> =
        nodejsApi.fetchAllReps(depotid,regionid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map{it}

    fun tmrepcustomer(repid: Int, tmid: Int): Single<Response<InitAllOutlets>> =
        nodejsApi.fetchAllCustomers(repid,tmid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map{it}

    fun fetchAllCustomers(): Single<List<EntityAllOutletsList>> =
        Single.fromCallable {
            appDao.fetchEntityAllOutletsList()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getCustomerNo(customerno: String): Single<Response<Basket>> =
        nodejsApi.getCustomerNo(customerno)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map{it}

    fun takeAttendant(tmid: Int, taskid: Int, repid: Int, outletlat:Double, outletlng:Double, currentLat:Double,
                      currentLng:Double, distance:String, duration:String, sequenceno:String, arrivaltime:String): Single<Response<Attendant>> =
        nodejsApi.takeAttendant(tmid, taskid, repid, outletlat, outletlng, currentLat, currentLng, distance, duration, sequenceno, arrivaltime)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map{it}

    fun setAttendantTime(time: String) =
        Single.fromCallable{
            appDao.setAttendantTime(time)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun SequencetManager(id: Int, nexts:Int, self:String) =
        Single.fromCallable{
            appDao.SequencetManager(id, nexts, self)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun ValidateSeque(id: Int): Single<EntityCustomerVisitSequence> =
        Single.fromCallable{
            appDao.ValidateSeque(id)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun UpdateSeque(id: Int,nexts:Int,self:String) =
        Single.fromCallable{
            appDao.UpdateSeque(id,nexts,self)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun CloseOutlets(repid: Int, tmid: Int, currentlat: String, currentlng: String,
                     outletlat: String, outletlng: String, arrivaltime: String,
                     visitsequence: String, distance: String, duration: String, urno: Int): Single<Response<Attendant>> =
        nodejsApi.CloseOutlets(repid,tmid,currentlat,currentlng,outletlat, outletlng,arrivaltime, visitsequence,distance,duration,urno)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map{it}

    fun setEntryTime(time: String, auto:Int) =
        Single.fromCallable{
            appDao.setEntryTime(time, auto)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getbasket(customerno: String, customer_code: String, repid: Int): Single<Response<InitBbasket>> =
        nodejsApi.getbasket(customerno, customer_code, repid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

    fun updateDailySales(inventory: Double, pricing: Int, entry_time: String, controlpricing:String, controlinventory:String, product_code:String) =
        Single.fromCallable{
            appDao.updateDailySales(inventory,pricing,entry_time,controlpricing,controlinventory,product_code)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun validateSalesEntry(): Observable<Int> =
        Observable.fromCallable {
            appDao.validateSalesEntry()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun sumAllSalesEntry(): Observable<SumSales> =
        Observable.fromCallable {
            appDao.sumAllSalesEntry()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    fun pullAllSalesEntry(): Single<List<EntityGetSalesEntry>> =
        Single.fromCallable {
            appDao.pullAllSalesEntry()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun fetchPostSales(data: postToServer): Single<Response<Exchange>> =
        nodejsApi.postSales(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

    fun CustometInfoAsync(urno:Int): Single<Response<OutletAsyn>> =
        nodejsApi.CustometInfoAsync(urno)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

    fun updateOutlet(tmid: Int, urno: Int, latitude: Double, longitude: Double, outletname: String, contactname: String,
                     outletaddress: String, contactphone: String, outletclassid: Int, outletlanguage: Int,
                     outlettypeid: Int): Single<Response<Exchange>> =
        nodejsApi.updateOutlet(tmid,urno,latitude,longitude,outletname,contactname,outletaddress,contactphone,outletclassid,outletlanguage,outlettypeid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

    fun mapOutlet(repid: Int, tmid: Int, urno: Int, latitude: Double, longitude: Double, outletname: String, contactname: String,
                  outletaddress: String, contactphone: String, outletclassid: Int, outletlanguage: Int,
                  outlettypeid: Int): Single<Response<Exchange>> =
        nodejsApi.mapOutlet(repid,tmid,urno,latitude,longitude,outletname,contactname,outletaddress,contactphone,outletclassid,outletlanguage,outlettypeid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

    fun updateIndividualCustomer(outletclassid:Int, outletlanguageid:Int, outlettypeid:Int, outletname:String, outletaddress:String, contactname:String, contactphone:String, latitude:Double, longitude:Double,auto:Int) =
        Single.fromCallable{
            appDao.updateIndividualCustomer(outletclassid, outletlanguageid, outlettypeid, outletname, outletaddress, contactname, contactphone, latitude, longitude,auto)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    fun getEntryDetails(urno: Int): Single<Response<Details>> =
        nodejsApi.getEntryDetails(urno)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

}

