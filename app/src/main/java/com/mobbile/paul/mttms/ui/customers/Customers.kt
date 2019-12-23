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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.SalesRepAndCustomerData
import com.mobbile.paul.mttms.util.Utils.Companion.USER_INFOS
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.mobbile.paul.mttms.models.AllTheSalesRep
import com.mobbile.paul.mttms.models.EntityAllOutletsList
import com.mobbile.paul.mttms.models.repAndCustomerData
import com.mobbile.paul.mttms.util.Util.insideRadius
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
    }

    fun switchAdapters() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _r_view_pager.layoutManager = layoutManager
        _r_view_pager!!.setHasFixedSize(true)

        vmodel.fetchsAllCustomers(
            preferences!!.getInt("depot_id_user_preferences", 0),
            preferences!!.getInt("region_id_user_preferences", 0)
        ).observe(this, observers)
    }

    //pick any rep
    val observeSelectRep = Observer<repAndCustomerData> {

    }

    val observers = Observer<SalesRepAndCustomerData> {
        when (it.status) {
            200 -> {
                TmSalesReps(it.salesrepsList!!)
            }
            300 -> {
                TmSalesRepCustomer(it.salesRepCustomersList!!)
            }
            else -> {

            }
        }
    }

    fun TmSalesReps(data: List<AllTheSalesRep>) {
        showProgressBar(false)
        mAdapter = TmSalesRepListAdapter(
            data,
            this,
            vmodel,
            preferences!!.getInt("employee_id_user_preferences", 0)
        )
        mAdapter.notifyDataSetChanged()
        _r_view_pager.setItemViewCacheSize(data.size)
        _r_view_pager.adapter = mAdapter
    }

    fun TmSalesRepCustomer(data: List<EntityAllOutletsList>) {
        showProgressBar(false)
        nAdapter = TmSalesRepOutletsAdapter(data, this, this@Customers::partItemClicked)
        nAdapter.notifyDataSetChanged()
        _r_view_pager.setItemViewCacheSize(data.size)
        _r_view_pager.adapter = nAdapter
    }

    private fun partItemClicked(partItem : EntityAllOutletsList, separator: Int) {
        when(separator){
            100->{
                Toast.makeText(this, "Clicked: ${partItem.outletname} ${separator}", Toast.LENGTH_LONG).show()
            }
            200->{
                val mode = partItem.mode.single()
                val destination = "${partItem.latitude},${partItem.longitude}"
                startGoogleMapIntent(this, destination, mode, 't')
            }
            300->{
                Toast.makeText(this, "Clicked: ${partItem.outletname} ${separator}", Toast.LENGTH_LONG).show()
            }
            400->{
                dataFromAdapter = partItem
                startLocationUpdates(2)
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

    fun startLocationUpdates(swictcher: Int) {

        showProgressBar(true)

        locationRequest = LocationRequest()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(builder.build())

        when (swictcher) {
            2 -> {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    callbackClose,
                    Looper.myLooper()
                )
            }
        }
    }

    private val callbackClose = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocationChangedClose(locationResult.lastLocation)
        }
    }

    fun onLocationChangedClose(location: Location) {
        showProgressBar(false)
        if (location.latitude.isNaN() && location.longitude.isNaN()) {
            showProgressBar(false)
            stoplocationClose()
            startLocationUpdates(2)
        } else {

            showProgressBar(false)
            stoplocationClose()

            val checkCustomerOutlet: Boolean = insideRadius(
                location.latitude,
                location.longitude,
                dataFromAdapter.latitude,
                dataFromAdapter.longitude
            )

            if (!checkCustomerOutlet) {
                showSomeDialog(this,"You are not at the corresponding outlet. Thanks!","Location Error")
            } else {

            }
        }
    }

    private fun stoplocationClose() {
        fusedLocationClient.removeLocationUpdates(callbackClose)
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


    companion object {
        var TAG = "TYTYTYTYTTYYTTY"
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1235
        var ENABLE_GPS = 1000
        private const val INTERVAL: Long = 1 * 1000
        private const val FASTEST_INTERVAL: Long = 1 * 1000
        const val RC_ENABLE_LOCATION = 1
    }
}

