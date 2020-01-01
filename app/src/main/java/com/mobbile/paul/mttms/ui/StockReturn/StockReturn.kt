package com.mobbile.paul.mttms.ui.StockReturn


import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.AttendantData
import com.mobbile.paul.mttms.models.ProductBiData
import com.mobbile.paul.mttms.ui.attendant_basket.AttendantViewModel
import com.mobbile.paul.mttms.ui.customers.Customers
import com.mobbile.paul.mttms.util.Util
import com.mobbile.paul.mttms.util.Util.appDate
import com.mobbile.paul.mttms.util.Util.showMsgDialog
import com.mobbile.paul.mttms.util.Util.showSomeDialog
import kotlinx.android.synthetic.main.activity_stock_return.*
import javax.inject.Inject

class StockReturn : BaseActivity() {


    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: AttendantViewModel

    private lateinit var nAdapter: StockReturnAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var locationRequest: LocationRequest

    var mode:Int = 0
    var tmid: Int = 0
    var repid: Int = 0
    var outletlat: Double = 0.0
    var outletlng: Double = 0.0
    var sequenceno:Int = 0
    var distance:String = ""
    var duration:String = ""
    var customer_code: String = ""
    var sortid: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_return)
        vmodel = ViewModelProviders.of(this, modelFactory)[AttendantViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        tmid = intent.getIntExtra("tmid",0)
        repid = intent.getIntExtra("repid",0)
        outletlat = intent.getDoubleExtra("outletlat",0.0)
        outletlng = intent.getDoubleExtra("outletlng",0.0)
        sequenceno = intent.getIntExtra("sequenceno",0)
        distance = intent.getStringExtra("distance")!!
        duration = intent.getStringExtra("duration")!!
        customer_code = intent.getStringExtra("customer_code")!!
        sortid = intent.getIntExtra("sort",0)
        vmodel.getRepBasket(customer_code).observe(this, observebasket)
        vmodel.attendantMutable().observe(this, observeAttendant)

        Log.d(TAG, customer_code)

        init()
    }

    fun init() {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        tv_view_pagers.layoutManager = layoutManager
        tv_view_pagers!!.setHasFixedSize(true)

        refresh_image.setOnClickListener {
            showProgressBar(true)
            vmodel.getRepBasket(customer_code).observe(this, observebasket)
        }

        backbtn.setOnClickListener {
            onBackPressed()
        }

        resumebtn.setOnClickListener {
            mode = 1
            showProgressBar(true)
            startLocationUpdates()
        }

        clockoutbtn.setOnClickListener {
            mode = 2
            showProgressBar(true)
            startLocationUpdates()
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

            val checkCustomerOutlet: Boolean =
                Util.insideRadius(location.latitude, location.longitude, outletlat, outletlng)

            if (!checkCustomerOutlet) {
                showProgressBar(false)
                showSomeDialog(this,"You are not at the DEPOT. Thanks!","Location Error")
            } else {
                when(mode) {
                    1->{
                        vmodel.takeAttendant(tmid,3,repid,sortid, outletlat, outletlng, location.latitude, location.longitude,
                            distance,duration,"${sequenceno}",
                            appDate()
                        )
                    }
                    2->{
                        vmodel.takeAttendant(tmid,4,repid,sortid, outletlat, outletlng, location.latitude, location.longitude,
                            distance,duration,"${sequenceno}",
                            appDate()
                        )
                    }
                }
            }
        }
    }

    private fun stoplocation() {
        fusedLocationClient.removeLocationUpdates(callback)
    }

    private val observebasket = Observer<ProductBiData> {
        val data: ProductBiData = it
        when (it.status) {
            200 -> {
                showProgressBar(false)
                nAdapter = StockReturnAdapter(data.list!!)
                nAdapter.notifyDataSetChanged()
                tv_view_pagers.setItemViewCacheSize(data.list!!.size)
                tv_view_pagers.adapter = nAdapter
                refresh_image.visibility = View.INVISIBLE
            }
            else -> {
                showProgressBar(false)
                showSomeDialog(this, "${it.msg} ${customer_code}", "Basket Error")
                refresh_image.visibility = View.VISIBLE
            }
        }
    }

    private val observeAttendant = Observer<AttendantData> {
        when (it.status) {
            200 -> {
                showProgressBar(false)
                if (mode == 1) {
                    showSomeDialog(this, it.notis, "Successful")
                } else {
                    showMsgDialog(Customers(), this, "Successful", it.notis)
                }
            }
            else -> {
                showProgressBar(false)
                showSomeDialog(this, it.notis, "Error")
            }
        }
    }

    companion object{
        val TAG = "rtyuiojhghjkl"
        private const val INTERVAL: Long = 1 * 1000
        private const val FASTEST_INTERVAL: Long = 1 * 1000
    }


}
