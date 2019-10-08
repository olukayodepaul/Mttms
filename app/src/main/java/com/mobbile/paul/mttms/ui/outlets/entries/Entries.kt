package com.mobbile.paul.mttms.ui.outlets.entries

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_entries.*
import javax.inject.Inject

class Entries : BaseActivity() {

    var custName: String = ""
    var custUrno: String = ""
    var custLatitude: Double = 0.0
    var custLongitude: Double = 0.0
    var custToken: String = ""
    var defaultCustToken: String = ""

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var repository: Repository

    lateinit var vmodel: EntriesViewModel

    private lateinit var mAdapter: EntriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)
        vmodel = ViewModelProviders.of(this, modelFactory)[EntriesViewModel::class.java]

        custName = intent.getStringExtra("passerOutletname")!!
        custUrno = intent.getStringExtra("passerCustno")!!
        custLatitude = intent.getDoubleExtra("passerLat",0.0)
        custLongitude = intent.getDoubleExtra("passerLng",0.0)
        custToken = intent.getStringExtra("passerToken")!!
        defaultCustToken = intent.getStringExtra("passerDtoken")!!

        tv_outlet_name.text = custName

        back_btn.setOnClickListener {
            onBackPressed()
        }

        vmodel.fetchSales(3262,"SWC2931","020565")
        vmodel.comData().observe(this, observeComData)
        initViews()
    }

    private fun initViews() {
        _sales_entry_recycler.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _sales_entry_recycler.layoutManager = layoutManager

        save_sales_entry.setOnClickListener {
            showProgressBar(true)
            vmodel.validateEntryStatus().observe(this, countOserver)
        }
    }

    val countOserver = Observer<Int> {
        showProgressBar(true)
        if(it==0) {
            val intent = Intent(this, SkuActivity::class.java)
            startActivity(intent)
        }else {
            showProgressBar(false)
            notifyValidation("Please enter all the fields and save. Thanks!", "Error entries")
        }
    }

    private fun notifyValidation(msg: String, title: String) {

        val builder = AlertDialog.Builder(this, R.style.AlertDialogDanger)
        builder.setMessage(msg)
            .setTitle(title)
            .setIcon(R.drawable.icons8_error_90)
            .setCancelable(false)
            .setNegativeButton("Ok") { _, _ ->
            }
        val dialog  = builder.create()
        dialog.show()
    }

    private val observeComData = Observer<EntryCallback> {

        if (it != null) {
            if(it.status.equals("FAIL")) {
                notifyValidation(it.msg!!, "Error")
            }else {
                var list: List<setSalesEntry> = it.data!!
                mAdapter = EntriesAdapter(list,  repository)
                mAdapter.notifyDataSetChanged()
                _sales_entry_recycler.setItemViewCacheSize(list.size)
                _sales_entry_recycler.adapter = mAdapter
            }
        }
        showProgressBar(false)
    }

    companion object{
        var TAG = "Entries"
    }
}

