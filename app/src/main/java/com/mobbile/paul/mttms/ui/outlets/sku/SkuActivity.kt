package com.mobbile.paul.mttms.ui.outlets.sku


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.EntityGetSalesEntry
import com.mobbile.paul.mttms.models.Responses
import com.mobbile.paul.mttms.models.SumSales
import com.mobbile.paul.mttms.ui.customers.Customers
import com.mobbile.paul.mttms.util.Util.showSomeDialog
import kotlinx.android.synthetic.main.activity_sku.*
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class SkuActivity : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: SkuViewmodel

    private lateinit var mAdapter: SkuAdapter

    var repid: Int = 0
    var tmid: Int = 0
    var currentlat: String = "0.0"
    var currentlng: String = "0.0"
    var outletlat: String = "0.0"
    var outletlng: String = "0.0"
    var distance: String = "0 km"
    var durations: String = "0 MS"
    var urno: Int = 0
    var sequenceno: Int = 0
    var token: String = ""
    var outletname: String = ""
    var defaulttoken: String = ""
    var repname: String = ""
    var arivaltime: String = ""
    var auto: Int = 0
    var customerno: String = ""
    var customer_code: String = ""
    var id: Int = 0
    var self: String = ""
    var nexts: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sku)
        vmodel = ViewModelProviders.of(this, modelFactory)[SkuViewmodel::class.java]

        repid = intent.getIntExtra("repid", 0)
        tmid = intent.getIntExtra("tmid", 0)
        currentlat = intent.getStringExtra("currentlat")!!
        currentlng = intent.getStringExtra("currentlng")!!
        outletlat = intent.getStringExtra("outletlat")!!
        outletlng = intent.getStringExtra("outletlng")!!
        distance = intent.getStringExtra("distance")!!
        durations = intent.getStringExtra("durations")!!
        urno = intent.getIntExtra("urno", 0)
        sequenceno = intent.getIntExtra("visit_sequence", 0)
        token = intent.getStringExtra("token")!!
        outletname = intent.getStringExtra("outletname")!!
        defaulttoken = intent.getStringExtra("defaulttoken")!!
        repname = intent.getStringExtra("repname")!!
        arivaltime = intent.getStringExtra("arivaltime")!!
        auto = intent.getIntExtra("auto", 0)
        customerno = intent.getStringExtra("customerno")!!
        customer_code = intent.getStringExtra("customer_code")!!
        id = intent.getIntExtra("id", 0)
        self = intent.getStringExtra("self")!!
        nexts = intent.getIntExtra("nexts", 0)

        IntAdapter()

        back_btn.setOnClickListener {
            onBackPressed()
        }
        vmodel.fetch().observe(this, observerOfSalesEntry)
        vmodel.DataResponseMethod().observe(this, _OfSalesEntry)
    }

    fun IntAdapter() {

        tv_outlet_name.text= repname
        tv_modules.text = "Entry History (${outletname})"

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler_view_complete!!.setHasFixedSize(true)
        recycler_view_complete.layoutManager = layoutManager


        btn_complete.setOnClickListener {
            when {
                token.equals(token_form.text.toString().trim()) -> {
                    showProgressBar(true)
                    btn_complete.visibility = View.INVISIBLE
                    vmodel.pullAllSalesEntry(repid, tmid, currentlat, currentlng, outletlat, outletlng,
                        arivaltime, sequenceno.toString(), distance,  durations, urno, id, nexts, self, auto)

                }
                defaulttoken.equals(token_form.text.toString().trim()) -> {
                    showProgressBar(true)
                    btn_complete.visibility = View.INVISIBLE
                    vmodel.pullAllSalesEntry(repid, tmid, currentlat, currentlng, outletlat, outletlng,
                        arivaltime, sequenceno.toString(), distance,  durations, urno, id, nexts, self, auto)
                }
                else -> showSomeDialog(this,  "Invalid Customer Verification code", "Error")
            }
        }
    }

    private val _OfSalesEntry = Observer<Responses> {
        if(it.status==200){
            val intent = Intent(this, Customers::class.java)
            startActivity(intent)
        }else{
            showSomeDialog(this, it.notis,"Server Error")
        }
    }

    private val observerOfSalesEntry = Observer<List<EntityGetSalesEntry>> {
        if (it != null) {
            var list: List<EntityGetSalesEntry> = it
            mAdapter = SkuAdapter(list)
            mAdapter.notifyDataSetChanged()
            recycler_view_complete.adapter = mAdapter
            loadTotalSales()
        }
        showProgressBar(false)
    }

    private fun loadTotalSales() {
        vmodel.sumAllSalesEntry().observe(this, obserTotal)
    }

    private val obserTotal = Observer<SumSales> {
        if (it != null) {
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.FLOOR
            s_s_pricing.text = String.format("%,.1f",(df.format(it.spricing).toDouble()))
            s_s_invetory.text = String.format("%,.1f",(df.format(it.sinventory).toDouble()))
        }
    }


    companion object{
        var TAG = "SkuActivity"
    }

}
