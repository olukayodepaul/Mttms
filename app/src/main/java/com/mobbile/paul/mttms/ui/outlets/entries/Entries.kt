package com.mobbile.paul.mttms.ui.outlets.entries

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.activity_entries.*
import kotlinx.android.synthetic.main.sales_entry_adapter.view.*
import javax.inject.Inject

class Entries : BaseActivity() {


    var urno: Int = 0
    var outletname: String = ""
    var defaulttoken: String = ""
    var visit_sequence: Int = 0
    var currentLat: Double = 0.0
    var currentLng: Double = 0.0
    var outletLat: Double = 0.0
    var outletLng: Double = 0.0
    var distance: String = ""
    var durations: String = ""
    var arivaltime: String = ""
    var repname: String = ""
    var auto: Int = 0
    var repid: Int = 0
    var customerno: String = ""
    var customer_code: String = ""

    var trasformPricing = 0
    var trasformInventory = 0.0
    var controltrasformPricing = ""
    var controltrasformInventory = ""


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
        repname = intent.getStringExtra("repname")!!
        outletname = intent.getStringExtra("outletname")!!
        repid = intent.getIntExtra("repid", 0)
        customerno = intent.getStringExtra("customerno")!!
        customer_code = intent.getStringExtra("customer_code")!!
        urno = intent.getIntExtra("urno", 0)
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
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _sales_entry_recycler.layoutManager = layoutManager
        _sales_entry_recycler.setHasFixedSize(true)
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

        if (containerView.mt_pricing.text.toString().isNotEmpty()) {
            trasformPricing = containerView.mt_pricing.text.toString().toInt()
            controltrasformPricing = "0"
        }

        if (containerView.mt_inventory.text.toString().isNotEmpty()) {
            trasformInventory = containerView.mt_inventory.text.toString().toDouble()
            controltrasformInventory = "0"
        }

    }



    companion object {
        var TAG = "Entries"
    }

}

