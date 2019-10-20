package com.mobbile.paul.mttms.ui.outlets.mapoutlet


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.EntitySpiners
import com.mobbile.paul.mttms.util.Util.showSomeDialog
import com.mobbile.paul.mttms.util.Utils.Companion.CUSTOMERS_INFORMATION
import kotlinx.android.synthetic.main.activity_map_outlet.*
import javax.inject.Inject

class MapOutlet : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: MapOutletViewModel

    lateinit var customerClassAdapter: CustomerClassSpinnerAdapter

    lateinit var preferedLangAdapter: PreferedLanguageSpinnerAdapter

    lateinit var outletTypeAdapter: OutletTypeSpinnerAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var hasGps = false

    lateinit var mLocationManager: LocationManager

    lateinit var locationRequest: LocationRequest

    private var preferencesByInfo: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_outlet)
        vmodel = ViewModelProviders.of(this, modelFactory)[MapOutletViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        preferencesByInfo = getSharedPreferences(CUSTOMERS_INFORMATION, Context.MODE_PRIVATE)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        registerBtn.setOnClickListener {
            getGps()
        }

        customerClassAdapter = CustomerClassSpinnerAdapter()
        preferedLangAdapter = PreferedLanguageSpinnerAdapter()
        outletTypeAdapter = OutletTypeSpinnerAdapter()

        vmodel.fetchSpinners().observe(this, languageObserver)
    }

    val languageObserver = Observer<List<EntitySpiners>> {
        if (it != null) {

            val outletClassList = ArrayList<String>()
            val preLangsList = ArrayList<String>()
            val outletTypeList = ArrayList<String>()

            if (customerClassAdapter.size() > 0) {
                customerClassAdapter.clear()
            }
            if (preferedLangAdapter.size() > 0) {
                preferedLangAdapter.clear()
            }
            if (outletTypeAdapter.size() > 0) {
                outletTypeAdapter.clear()
            }
            for (i in it.indices) {
                when (it[i].sep) {
                    1 -> {
                        outletClassList.add(it[i].name)
                        customerClassAdapter.add(it[i].id, it[i].name)
                    }
                    2 -> {
                        preLangsList.add(it[i].name)
                        preferedLangAdapter.add(it[i].id, it[i].name)
                    }
                    3 -> {
                        outletTypeList.add(it[i].name)
                        outletTypeAdapter.add(it[i].id, it[i].name)
                    }
                }
            }
            val mOutletClass = ArrayAdapter(this, android.R.layout.simple_spinner_item, outletClassList)
            mOutletClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            custClass!!.adapter = mOutletClass

            val mPreferedLang = ArrayAdapter(this, android.R.layout.simple_spinner_item, preLangsList)
            mPreferedLang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            preflang!!.adapter = mPreferedLang

            val mOutletType =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, outletTypeList)
            mOutletType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            outlettypeedit!!.adapter = mOutletType
            showProgressBar(false)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getGps() {

        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

        val accessPermissionStatus =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarsePermissionStatus =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (accessPermissionStatus == PackageManager.PERMISSION_DENIED
            && coarsePermissionStatus == PackageManager.PERMISSION_DENIED
        ) {
            showProgressBar(false)
            requestLocationPermission()

        } else if (!hasGps) {
            showProgressBar(false)
            callGpsIntent()
        } else if (available == ConnectionResult.API_UNAVAILABLE) {
            showSomeDialog(this, "Please Install and setup Google Play service", "Google Play")
        } else {
            startLocationUpdates()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this@MapOutlet,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestLocationPermission()
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
                mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (!hasGps) {
                    callGpsIntent()
                }
            }
        }
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
            LocationFinder,
            Looper.myLooper()
        )
    }

    private val LocationFinder = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocationChangedForClose(locationResult.lastLocation)
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun onLocationChangedForClose(location: Location) {

        Log.d(TAG,"2-2")
        if(location.latitude.isNaN() && location.longitude.isNaN()) {
            Log.d(TAG,"2-2")
            stoplocationUpdates()
            startLocationUpdates()

        }else{
            Log.d(TAG,"2-2")
            val outletName = customer_name_edit.text.toString()
            val contactName = contact_name_edit.text.toString()
            val address = address_edit.text.toString()
            val phones = phone_number_edit.text.toString()
            val outletClass = customerClassAdapter.getValueId(custClass.selectedItem.toString())
            val prefLang = preferedLangAdapter.getValueId(preflang.selectedItem.toString())
            val outletTypeId = outletTypeAdapter.getValueId(outlettypeedit.selectedItem.toString())
            val repid = preferencesByInfo!!.getInt("specific_rep_id", 0)

            stoplocationUpdates()

            val intent = Intent(this, AttachPhotos::class.java)
            intent.putExtra("outletName", outletName)
            intent.putExtra("contactName", contactName)
            intent.putExtra("address", address)
            intent.putExtra("phones", phones)
            intent.putExtra("outletClass", outletClass)
            intent.putExtra("prefLang", prefLang)
            intent.putExtra("outletTypeId", outletTypeId)
            intent.putExtra("repid", repid)
            intent.putExtra("lat", location.latitude.toString())
            intent.putExtra("lng", location.longitude.toString())
            startActivity(intent)

        }
    }

    private fun stoplocationUpdates() {
        fusedLocationClient.removeLocationUpdates(LocationFinder)
    }

    companion object {
        var REQUEST_PERMISSIONS_REQUEST_CODE = 1000
        var TAG = "MapOutlet"
        var RC_ENABLE_LOCATION = 1000
        private const val INTERVAL: Long = 1 * 1000
        private const val FASTEST_INTERVAL: Long = 1 * 1000
    }
}
