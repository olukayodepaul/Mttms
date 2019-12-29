package com.mobbile.paul.mttms.ui.outlets.entries

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.EntryCallback
import com.mobbile.paul.mttms.models.setSalesEntry
import com.mobbile.paul.mttms.providers.Repository
import com.mobbile.paul.mttms.ui.outlets.sku.SkuActivity
import com.mobbile.paul.mttms.util.Util.appTime
import com.mobbile.paul.mttms.util.Util.showSomeDialog
import kotlinx.android.synthetic.main.activity_entries.*
import kotlinx.android.synthetic.main.sales_entry_adapter.view.*
import javax.inject.Inject

class Entries : BaseActivity() {


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

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var repository: Repository

    lateinit var vmodel: EntriesViewModel

    private lateinit var mAdapter: EntriesAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)

        vmodel = ViewModelProviders.of(this, modelFactory)[EntriesViewModel::class.java]

        repid = intent.getIntExtra("repid", 0)
        tmid = intent.getIntExtra("tmid", 0)
        currentlat = intent.getStringExtra("currentlat")!!
        currentlng = intent.getStringExtra("currentlng")!!
        outletlat = intent.getStringExtra("outletlat")!!
        outletlng = intent.getStringExtra("outletlng")!!
        distance = intent.getStringExtra("distance")!!
        durations = intent.getStringExtra("durations")!!
        urno = intent.getIntExtra("urno", 0)
        sequenceno = intent.getIntExtra("sequenceno", 0)
        token = intent.getStringExtra("token")!!
        outletname = intent.getStringExtra("outletname")!!
        defaulttoken = intent.getStringExtra("defaulttoken")!!
        repname = intent.getStringExtra("repname")!!
        arivaltime = intent.getStringExtra("arivaltime")!!
        auto = intent.getIntExtra("auto", 0)
        customerno = intent.getStringExtra("customerno")!!
        customer_code = intent.getStringExtra("customer_code")!!

        tv_outlet_name.text = repname
        tv_modules.text = "Customer ($outletname)"

        vmodel.fetchSales(customerno, customer_code, repid)

        back_btn.setOnClickListener {
            onBackPressed()
        }

        initViews()

        vmodel.basketData().observe(this, observeComData)
    }

    private fun initViews() {

        save_sales_entry.setOnClickListener {
            showProgressBar(true)
            vmodel.validateEntryStatus().observe(this, checkUnEntrySales)
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _sales_entry_recycler.layoutManager = layoutManager
        _sales_entry_recycler.setHasFixedSize(true)
    }

    private val checkUnEntrySales = Observer<Int> {
        if(it==0) {
            showProgressBar(false)
            val intent = Intent(this, SkuActivity::class.java)
            intent.putExtra("repid", repid)
            intent.putExtra("tmid", tmid)
            intent.putExtra("currentlat",currentlat)
            intent.putExtra("currentlng", currentlng)
            intent.putExtra("outletlat", outletlat)
            intent.putExtra("outletlng", outletlng)
            intent.putExtra("distance",distance)
            intent.putExtra("durations",durations)
            intent.putExtra("urno", urno)
            intent.putExtra("visit_sequence", sequenceno)
            intent.putExtra("token", token)
            intent.putExtra("outletname",outletname)
            intent.putExtra("defaulttoken", defaulttoken)
            intent.putExtra("arivaltime", arivaltime)
            intent.putExtra("repname", repname)
            intent.putExtra("auto", auto)
            intent.putExtra("customerno", customerno)
            intent.putExtra("customer_code", customer_code)
            startActivity(intent)
        }else {
            showProgressBar(false)
            showSomeDialog(this, "Please enter all the fields and save. Thanks!", "Error entries")
        }
    }

    private val observeComData = Observer<EntryCallback> {
        showProgressBar(false)
        var list: List<setSalesEntry> = it.data!!
        mAdapter = EntriesAdapter(list, ::partItemClicked)
        mAdapter.notifyDataSetChanged()
        _sales_entry_recycler.setItemViewCacheSize(list.size)
        _sales_entry_recycler.adapter = mAdapter
    }

    private fun partItemClicked(partItem: setSalesEntry, containerView: View) {

        var trasformPricing = 0
        var trasformInventory = 0.0
        var controltrasformPricing = ""
        var controltrasformInventory = ""

        if (containerView.mt_pricing.text.toString().isNotEmpty()) {
            trasformPricing = containerView.mt_pricing.text.toString().toInt()
            controltrasformPricing = "0"
        }

        if (containerView.mt_inventory.text.toString().isNotEmpty()) {
            trasformInventory = containerView.mt_inventory.text.toString().toDouble()
            controltrasformInventory = "0"
        }

        vmodel.updateDailySales(trasformInventory, trasformPricing, appTime(), controltrasformInventory, controltrasformPricing, partItem.product_code)
    }



    companion object {
        var TAG = "Entries"
    }
}

