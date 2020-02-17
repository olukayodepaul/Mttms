package com.mobbile.paul.mttms.ui.customers


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.util.Utils.Companion.USER_INFOS
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.mobbile.paul.mttms.models.*
import com.mobbile.paul.mttms.ui.outlets.details.Details
import com.mobbile.paul.mttms.ui.outlets.entries.Entries
import com.mobbile.paul.mttms.ui.outlets.mapoutlet.MapOutlet
import com.mobbile.paul.mttms.ui.outlets.updateoutlets.OutletUpdate
import com.mobbile.paul.mttms.util.Util.appTime
import com.mobbile.paul.mttms.util.Util.insideRadius
import com.mobbile.paul.mttms.util.Util.showMsgDialog
import com.mobbile.paul.mttms.util.Util.showSomeDialog
import kotlinx.android.synthetic.main.activity_customers.*
import javax.inject.Inject

class Customers : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: CustomersViewModel

    private lateinit var mAdapter: TmSalesRepListAdapter

    private lateinit var nAdapter: TmSalesRepOutletsAdapter

    private var preferences: SharedPreferences? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var locationRequest: LocationRequest

    lateinit var mLocationManager: LocationManager

    private var hasGps = false

    private var mode = 0

    private lateinit var dataFromAdapter:  EntityAllOutletsList

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customers)
        vmodel = ViewModelProviders.of(this, modelFactory)[CustomersViewModel::class.java]
        preferences = getSharedPreferences(USER_INFOS, Context.MODE_PRIVATE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

        vmodel.selectAnyReps().observe(this, observeSelectRep)
        vmodel.closeOutletMutable().observe(this, observeCloseOutlets)
        vmodel.responds().observe(this, observeDetailsChange)


    }

    fun switchAdapters() {

        backbtn.setOnClickListener {
            onBackPressed()
        }

        fab.setOnClickListener {
            val intent = Intent(this, MapOutlet::class.java)
            startActivity(intent)
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _r_view_pager.layoutManager = layoutManager
        _r_view_pager!!.setHasFixedSize(true)

        vmodel.fetchsAllCustomers(
            preferences!!.getInt("depot_id_user_preferences", 0),
            preferences!!.getInt("region_id_user_preferences", 0)
        ).observe(this, observers)
    }

    private val observeSelectRep = Observer<repAndCustomerData> {
        if(it.status==200){
            val intent = Intent(this, Customers::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }else{
            showProgressBar(false)
            showSomeDialog( this,"${it.notice}","Error")
        }
    }

    private val observeDetailsChange = Observer<Responses> {
        if(it.status==200){
            showProgressBar(false)
            showMsgDialog(Customers(), this, "Successful","Customer data successfully synchronise")
        }else{
            showProgressBar(false)
            showSomeDialog(this,"Customer data fail to synchronise","Outlet Close Error")
        }
    }

    private val observeCloseOutlets = Observer<AttendantData> {
       when(it.status){
        200->{
            showProgressBar(false)
            showMsgDialog(Customers(), this, "Successful",it.notis)
        }else->{
           showProgressBar(false)
           showSomeDialog(this,it.notis,"Outlet Close Error")
        }
       }
    }

    @SuppressLint("SetTextI18n")
    private val observers = Observer<SalesRepAndCustomerData> {
        when (it.status) {
            200 -> {
                titles.text = "SALES REP LIST"
                TmSalesReps(it.salesrepsList!!)
            }
            300 -> {
                titles.text = "CUSTOMERS"
                TmSalesRepCustomer(it.salesRepCustomersList!!)
            }
            else -> {
                titles.text = "SALES REP LIST"
                showSomeDialog(this, it.msg, "Error")
            }
        }
    }

    fun TmSalesReps(data: List<AllTheSalesRep>) {
        showProgressBar(false)
        mAdapter = TmSalesRepListAdapter(data,::adapterItemClicked)
        mAdapter.notifyDataSetChanged()
        _r_view_pager.setItemViewCacheSize(data.size)
        _r_view_pager.adapter = mAdapter
    }

    private fun adapterItemClicked(items : AllTheSalesRep) {
        message(items.employeeid,  items.fullname)
    }

    fun TmSalesRepCustomer(data: List<EntityAllOutletsList>) {
            showProgressBar(false)
            title_name.text = data[2].rep_name
            nAdapter = TmSalesRepOutletsAdapter(data, this,::partItemClicked)
            nAdapter.notifyDataSetChanged()
            _r_view_pager.setItemViewCacheSize(data.size)
            _r_view_pager.adapter = nAdapter
    }

    private fun partItemClicked(partItem : EntityAllOutletsList, separator: Int) {
        when(separator){
            100->{
                showProgressBar(true)
                mode = 1
                dataFromAdapter = partItem
                startLocationUpdates()
            }
            200->{
                val dmode = partItem.mode.single()
                val destination = "${partItem.latitude},${partItem.longitude}"
                startGoogleMapIntent(this, destination, dmode, 't')
            }
            300->{
                //update outlets
                dataFromAdapter = partItem
                val intent = Intent(this, OutletUpdate::class.java)
                intent.putExtra("tmid", dataFromAdapter.tm_id)
                intent.putExtra("outletname", dataFromAdapter.outletname)
                intent.putExtra("contactname", dataFromAdapter.contactname)
                intent.putExtra("contactphone", dataFromAdapter.contactphone)
                intent.putExtra("outletaddress", dataFromAdapter.outletaddress)
                intent.putExtra("outletclassid", dataFromAdapter.outletclassid)
                intent.putExtra("outletlanguageid", dataFromAdapter.outletlanguageid)
                intent.putExtra("outlettypeid", dataFromAdapter.outlettypeid)
                intent.putExtra("urno", dataFromAdapter.urno)
                startActivity(intent)
            }
            400->{
                showProgressBar(true)
                mode = 2
                dataFromAdapter = partItem
                startLocationUpdates()
            }
            500-> {
                showProgressBar(true)
                dataFromAdapter = partItem
                asynchroniseDialogWithoutIntent()
            }
            600->{
                dataFromAdapter = partItem
                val intent = Intent(this, Details::class.java)
                intent.putExtra("urno", dataFromAdapter.urno)
                startActivity(intent)
            }
        }
    }

    fun startGoogleMapIntent(ctx: Context, ads: String, mode: Char, avoid: Char): Any {
        val uri = Uri.parse("google.navigation:q=$ads&mode=$mode&avoid=$avoid")
        val mIntent = Intent(Intent.ACTION_VIEW, uri)
        mIntent.`package` = "com.google.android.apps.maps"
        return if (mIntent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(mIntent)
            true
        } else
            false
    }

    fun startLocationUpdates() {

        locationRequest = LocationRequest()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(builder.build())

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.myLooper()
        )
    }

    private val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {

        if (location.latitude.isNaN() && location.longitude.isNaN()) {

            showProgressBar(false)
            stoplocation()
            startLocationUpdates()
        } else {

            stoplocation()

            val checkCustomerOutlet: Boolean = insideRadius(
                location.latitude,
                location.longitude,
                dataFromAdapter.latitude,
                dataFromAdapter.longitude
            )

            if (!checkCustomerOutlet) {
                showProgressBar(false)
                showSomeDialog(this,"You are not at the corresponding outlet. Thanks!","Location Error")
            } else {
                vmodel.ValidateSeque(1, dataFromAdapter.sequenceno, location.latitude, location.longitude).observe(this,observeVisitSequence)
            }
        }
    }

    private val observeVisitSequence = Observer<CloseAndOpenOutlet> {
        when(it.status) {
            200->{

                when(mode) {
                    1->{ //refactor here to paecelable
                        showProgressBar(false)
                        val intent = Intent(this, Entries::class.java)
                        intent.putExtra("repid", dataFromAdapter.rep_id)
                        intent.putExtra("tmid", dataFromAdapter.tm_id)
                        intent.putExtra("currentlat", it.lat.toString())
                        intent.putExtra("currentlng", it.lng.toString())
                        intent.putExtra("outletlat", dataFromAdapter.latitude.toString())
                        intent.putExtra("outletlng", dataFromAdapter.longitude.toString())
                        intent.putExtra("distance",dataFromAdapter.distance)
                        intent.putExtra("durations", dataFromAdapter.duration)
                        intent.putExtra("urno", dataFromAdapter.urno)
                        intent.putExtra("visit_sequence", dataFromAdapter.sequenceno)
                        intent.putExtra("token", dataFromAdapter.token)
                        intent.putExtra("outletname", dataFromAdapter.outletname)
                        intent.putExtra("defaulttoken", dataFromAdapter.defaulttoken)
                        intent.putExtra("arivaltime", appTime())
                        intent.putExtra("repname", dataFromAdapter.rep_name)
                        intent.putExtra("customerno", dataFromAdapter.customerno)
                        intent.putExtra("customer_code", dataFromAdapter.customer_code)
                        intent.putExtra("auto", dataFromAdapter.auto)
                        intent.putExtra("id", it.id)
                        intent.putExtra("self", it.self)
                        intent.putExtra("nexts", it.nexts)
                        intent.putExtra("specifier", 1)
                        startActivity(intent)
                    }
                    2->{
                        vmodel.CloseOutlets(dataFromAdapter.rep_id, dataFromAdapter.tm_id, it.lat.toString(), it.lng.toString(),
                            dataFromAdapter.latitude.toString(), dataFromAdapter.longitude.toString(), appTime(),
                            dataFromAdapter.sequenceno.toString(),dataFromAdapter.distance, dataFromAdapter.duration, dataFromAdapter.urno,
                            it.id, it.nexts+1, "${it.self},${it.nexts}",1, dataFromAdapter.auto)
                    }
                }
            }
            300->{
                //push to server without update local db
                when(mode) {
                    1->{
                        showProgressBar(false)
                        val intent = Intent(this, Entries::class.java)
                        intent.putExtra("repid", dataFromAdapter.rep_id)
                        intent.putExtra("tmid", dataFromAdapter.tm_id)
                        intent.putExtra("currentlat", it.lat.toString())
                        intent.putExtra("currentlng", it.lng.toString())
                        intent.putExtra("outletlat", dataFromAdapter.latitude.toString())
                        intent.putExtra("outletlng", dataFromAdapter.longitude.toString())
                        intent.putExtra("distance",dataFromAdapter.distance)
                        intent.putExtra("durations", dataFromAdapter.duration)
                        intent.putExtra("urno", dataFromAdapter.urno)
                        intent.putExtra("visit_sequence", dataFromAdapter.sequenceno)
                        intent.putExtra("token", dataFromAdapter.token)
                        intent.putExtra("outletname", dataFromAdapter.outletname)
                        intent.putExtra("defaulttoken", dataFromAdapter.defaulttoken)
                        intent.putExtra("arivaltime", appTime())
                        intent.putExtra("repname", dataFromAdapter.rep_name)
                        intent.putExtra("customerno", dataFromAdapter.customerno)
                        intent.putExtra("customer_code", dataFromAdapter.customer_code)
                        intent.putExtra("auto", dataFromAdapter.auto)
                        intent.putExtra("id", it.id)
                        intent.putExtra("self", it.self)
                        intent.putExtra("nexts", it.nexts)
                        startActivity(intent)
                    }
                    2->{
                        vmodel.CloseOutlets(dataFromAdapter.rep_id, dataFromAdapter.tm_id, it.lat.toString(), it.lng.toString(),
                            dataFromAdapter.latitude.toString(), dataFromAdapter.longitude.toString(), appTime(),
                            dataFromAdapter.sequenceno.toString(),dataFromAdapter.distance, dataFromAdapter.duration, dataFromAdapter.urno,
                            it.id, it.nexts+1, "${it.self},${it.nexts}",2, dataFromAdapter.auto)
                    }
                }
            }
            400->{
                showProgressBar(false)
                showSomeDialog(this,"Please follow the outlet visit sequence. Thanks!","Visit Error")
            }else->{
                showProgressBar(false)
                showSomeDialog(this,"Please resume and clock out before you proceed. Thanks!","Attendant Error")
            }
        }
    }

    private fun stoplocation() {
        fusedLocationClient.removeLocationUpdates(callback)
    }

    fun checkLocationPermission() {
        val accessPermissionStatus = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarsePermissionStatus = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (accessPermissionStatus == PackageManager.PERMISSION_GRANTED
            && coarsePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            gPSRationaleEnable()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    gPSPermissionRationaleAlert()
                }else{
                    mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    if(hasGps){
                        switchAdapters()
                    }else{
                        callGpsIntent()
                    }
                }
            }
        }
    }

    private fun callGpsIntent() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, RC_ENABLE_LOCATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_ENABLE_LOCATION -> {
                checkGpsEnabledAndPrompt()
            }
        }
    }

    private fun checkGpsEnabledAndPrompt() {
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        when {
            hasGps -> {
                switchAdapters()
            }
            available == ConnectionResult.API_UNAVAILABLE -> {
                showSomeDialog(this, "Google Play Services is not Available on this device, please install", "Google Play Services")
            }
            else -> {
                gPSRationaleEnable()
            }
        }
    }

    private fun gPSRationaleEnable() {
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(!hasGps) {
            val builder = AlertDialog.Builder(this, R.style.AlertDialogDanger)
            builder.setMessage("Your location need to be put On")
                .setTitle("GPS Enable")
                .setIcon(R.drawable.icons8_google_alerts_100)
                .setCancelable(false)
                .setNegativeButton("OK") { _, _ ->
                    callGpsIntent()
                }
            val dialog  = builder.create()
            dialog.show()
        }else {
            switchAdapters()
            Log.d(TAG,"")
        }
    }

    private fun gPSPermissionRationaleAlert() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogDanger)
        builder.setMessage("Without allowing GPS permission, this application will not work for you")
            .setTitle("GPS Permission")
            .setIcon(R.drawable.icons8_google_alerts_100)
            .setCancelable(false)
            .setNegativeButton("OK") { _, _ ->
                requestLocationPermission()
            }
        val dialog  = builder.create()
        dialog.show()
    }

    private fun message(repid: Int,  fullname:String) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogDanger)
        builder.setMessage("Are you sure you want to visit $fullname")
            .setTitle("Customer Visit")
            .setIcon(R.drawable.icons8_google_alerts_100)
            .setCancelable(false)
            .setNegativeButton("No") { _, _ ->
            }
            .setPositiveButton("Yes") { _, _ ->
                showProgressBar(true)
                vmodel.getAllCustomerReps(repid,preferences!!.getInt("employee_id_user_preferences", 0))
            }
        val dialog = builder.create()
        dialog.show()
    }

    fun asynchroniseDialogWithoutIntent() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogDanger)
        builder.setMessage("Are you sure you want to synchronise ${dataFromAdapter.outletname} outlet data")
            .setTitle("Data Async")
            .setIcon(R.drawable.icons8_google_alerts_100)
            .setCancelable(false)
            .setNegativeButton("Ok") { _, _ ->
                vmodel.CustometInfoAsync(dataFromAdapter.urno,dataFromAdapter.auto)
            }.setPositiveButton("NO"){ _, _ ->
                showProgressBar(false)
            }
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        var TAG = "TYTYTYTYTTYYTTY"
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1235
        private const val INTERVAL: Long = 1 * 1000
        private const val FASTEST_INTERVAL: Long = 1 * 1000
        const val RC_ENABLE_LOCATION = 1
    }
}

