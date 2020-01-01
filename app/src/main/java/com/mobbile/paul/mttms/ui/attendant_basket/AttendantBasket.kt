package com.mobbile.paul.mttms.ui.attendant_basket


import android.location.Location
import android.os.Bundle
import android.os.Looper
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
import com.mobbile.paul.mttms.ui.customers.Customers
import com.mobbile.paul.mttms.util.Util.appDate
import com.mobbile.paul.mttms.util.Util.insideRadius
import com.mobbile.paul.mttms.util.Util.showMsgDialog
import com.mobbile.paul.mttms.util.Util.showSomeDialog
import kotlinx.android.synthetic.main.activity_attendant_basket.*
import javax.inject.Inject

class AttendantBasket : BaseActivity()  {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: AttendantViewModel

    private lateinit var nAdapter: AttandantAdapter

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
        setContentView(R.layout.activity_attendant_basket)
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
        init()
    }

    fun init() {

        refresh_image.setOnClickListener {
            showProgressBar(true)
            vmodel.getRepBasket(customer_code).observe(this, observebasket)
        }

        backbtn.setOnClickListener {
            onBackPressed()
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        view_pager.layoutManager = layoutManager
        view_pager!!.setHasFixedSize(true)

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

    private val observeAttendant = Observer<AttendantData> {
        when(it.status) {
            200->{
                showProgressBar(false)
                if(mode==1) {
                    showSomeDialog(this, it.notis, "Successful")
                }else{
                    showMsgDialog(Customers(),this,"Successful", it.notis)
                }
            }else->{
                showProgressBar(false)
                showSomeDialog(this, it.notis, "Error")
            }
        }
    }

    private val observebasket = Observer<ProductBiData> {
        val data: ProductBiData  = it
        when(it.status) {
            200->{
                showProgressBar(false)
                nAdapter = AttandantAdapter(data.list!!)
                nAdapter.notifyDataSetChanged()
                view_pager.setItemViewCacheSize(data.list!!.size)
                view_pager.adapter = nAdapter
                refresh_image.visibility = View.INVISIBLE
            }else->{
                showProgressBar(false)
                showSomeDialog(this, "${it.msg} ${customer_code}", "Basket Error")
                refresh_image.visibility = View.VISIBLE
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

            val checkCustomerOutlet: Boolean = insideRadius(location.latitude, location.longitude, outletlat, outletlng)

            if (!checkCustomerOutlet) {
                showProgressBar(false)
                showSomeDialog(this,"You are not at the corresponding DEPOT. Thanks!","Location Error")
            } else {
                when(mode){
                    1->{
                        vmodel.takeAttendant(tmid,1,repid,sortid, outletlat, outletlng, location.latitude, location.longitude,
                            distance,duration,"${sequenceno}", appDate())
                    }
                    2->{
                        vmodel.takeAttendant(tmid,2,repid,sortid, outletlat, outletlng, location.latitude, location.longitude,
                            distance,duration,"${sequenceno}", appDate())
                    }
                }
            }
        }
    }

    private fun stoplocation() {
        fusedLocationClient.removeLocationUpdates(callback)
    }

    companion object{
        var TAG = "AttendantBasket"
        private const val INTERVAL: Long = 1 * 1000
        private const val FASTEST_INTERVAL: Long = 1 * 1000
    }

}
