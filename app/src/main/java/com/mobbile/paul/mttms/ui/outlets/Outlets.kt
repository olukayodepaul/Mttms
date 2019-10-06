package com.mobbile.paul.mttms.ui.outlets

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.AllOutletsList
import com.mobbile.paul.mttms.models.EntityAllOutletsList
import com.mobbile.paul.mttms.util.Utils.Companion.CUSTOMERS_INFORMATION
import com.mobbile.paul.mttms.util.Utils.Companion.CUSTOMERS_VISIT
import kotlinx.android.synthetic.main.activity_customer_list_viwe_pager.*
import kotlinx.android.synthetic.main.activity_outlets.*
import kotlinx.android.synthetic.main.activity_outlets.backbtn
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Outlets : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: OutletViewmodel

    private lateinit var mAdapter: OutletRemoteAdapter

    private lateinit var nAdapter: OutletLocalAdapter

    private var preferencesByInfo: SharedPreferences? = null

    private var preferencesByVisit: SharedPreferences? = null

    var todayDates: String? = null

    var pDate: String? = null

    var pStatus: Int? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outlets)
        vmodel = ViewModelProviders.of(this, modelFactory)[OutletViewmodel::class.java]
        preferencesByInfo = getSharedPreferences(CUSTOMERS_INFORMATION, Context.MODE_PRIVATE)
        preferencesByVisit = getSharedPreferences(CUSTOMERS_VISIT, Context.MODE_PRIVATE)

        todayDates = SimpleDateFormat("yyyy-MM-dd").format(Date())

        pDate = preferencesByVisit!!.getString("specific_rep_date", "")

        pStatus = preferencesByVisit!!.getInt("specific_rep_changevalues", 0)

        switchAdapters()

        initViews()

        showProgressBar(true)

        vmodel.MutableAllOuletList().observe(this, remoteObservers)

        backbtn.setOnClickListener {
            onBackPressed()
        }
    }

    fun switchAdapters() {
        Log.d(TAG, pStatus.toString())
        when {
            pDate == todayDates && pStatus == 200 -> {
                vmodel.fetchAllOutlets(preferencesByInfo!!.getInt("specific_rep_id",0))
            }
            pDate == todayDates && pStatus == 300 -> {
                vmodel.fetchLocalOutlet().observe(this, localObservers)
            }
        }
    }

    private fun initViews() {
        _r_view_pager.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _r_view_pager.layoutManager = layoutManager
    }

    private val localObservers = Observer<List<EntityAllOutletsList>> {
        if (it != null) {
            showProgressBar(false)
            val list: List<EntityAllOutletsList> = it
            nAdapter = OutletLocalAdapter(list, this)
            nAdapter.notifyDataSetChanged()
            _r_view_pager.adapter = nAdapter
        }
    }

    private val remoteObservers = Observer<List<AllOutletsList>> {
        if (it != null) {
            //setVisitSharePref()
            showProgressBar(false)
            val list: List<AllOutletsList> = it
            mAdapter = OutletRemoteAdapter(list, this)
            mAdapter.notifyDataSetChanged()
            _r_view_pager.adapter = mAdapter
        }
    }

    companion object{
        var TAG = "Outlets"
    }
}
