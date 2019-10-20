package com.mobbile.paul.mttms.ui.outlets

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.mobbile.paul.mttms.ui.outlets.mapoutlet.MapOutlet
import com.mobbile.paul.mttms.util.Util.showSomeDialog
import com.mobbile.paul.mttms.util.Utils.Companion.CUSTOMERS_INFORMATION
import com.mobbile.paul.mttms.util.Utils.Companion.CUSTOMERS_VISIT
import com.mobbile.paul.mttms.util.Utils.Companion.LOCAL_AND_REMOTE_CUSTOMERS
import com.mobbile.paul.mttms.util.Utils.Companion.USER_INFOS
import kotlinx.android.synthetic.main.activity_outlets.*
import kotlinx.android.synthetic.main.activity_outlets.backbtn
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@Suppress("CAST_NEVER_SUCCEEDS")
class Outlets : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: OutletViewmodel

    private lateinit var mAdapter: OutletRemoteAdapter

    private lateinit var nAdapter: OutletLocalAdapter

    private var preferencesByInfo: SharedPreferences? = null

    private var preferencesByVisit: SharedPreferences? = null

    private var preferencesSwitcher: SharedPreferences? = null

    private var preferences: SharedPreferences? = null

    var todayDates: String? = null

    var pDate: String? = null

    var pStatus: Int? = null

    var prefSwitch: Int? = null

    lateinit var dataLocalList : List<AllOutletsList>

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outlets)
        vmodel = ViewModelProviders.of(this, modelFactory)[OutletViewmodel::class.java]
        preferencesByInfo = getSharedPreferences(CUSTOMERS_INFORMATION, Context.MODE_PRIVATE)
        preferencesByVisit = getSharedPreferences(CUSTOMERS_VISIT, Context.MODE_PRIVATE)
        preferencesSwitcher = getSharedPreferences(LOCAL_AND_REMOTE_CUSTOMERS, Context.MODE_PRIVATE)
        preferences = getSharedPreferences(USER_INFOS, Context.MODE_PRIVATE)

        todayDates = SimpleDateFormat("yyyy-MM-dd").format(Date())

        pDate = preferencesByVisit!!.getString("specific_rep_date", "")

        pStatus = preferencesByVisit!!.getInt("specific_rep_changevalues", 0)

        prefSwitch = preferencesSwitcher!!.getInt("switch_locat_remote", 0)

        switchAdapters()

        initViews()

        showProgressBar(true)

        vmodel.MutableAllOuletList().observe(this, remoteObservers)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        floatingActionButton.setOnClickListener {
            mapOutlets()
        }
        counts.text = SimpleDateFormat("EEE, MMM dd, ''yy").format(Date())
    }

    private fun mapOutlets(){
        val intent = Intent(this, MapOutlet::class.java)
        startActivity(intent)
    }

    fun switchAdapters() {
        when (prefSwitch) {
            200 -> {
                Log.d(TAG, "1-1")
                vmodel.fetchLocalOutlet().observe(this, localObservers)
            }
            else -> {
                Log.d(TAG, "1-2 "+preferencesByInfo!!.getString("specific_mode_id","")+" "+todayDates)
                vmodel.fetchAllOutlets(preferencesByInfo!!.getInt("specific_rep_id",0), todayDates!!)
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
            nAdapter = OutletLocalAdapter(list, this, preferencesByInfo!!.getString("specific_mode_id","")!!)
            nAdapter.notifyDataSetChanged()
            _r_view_pager.adapter = nAdapter
        }
    }

    @SuppressLint("SimpleDateFormat")
    private val remoteObservers = Observer<List<AllOutletsList>> {
        if (it != null && it.isNotEmpty()) {
            dataLocalList = it
            vmodel.takeTmVit(
                preferencesByInfo!!.getInt("specific_rep_id",0),
                preferences!!.getInt("employee_id_user_preferences",0),
                todayDates!!,
                todayDates!!+' '+SimpleDateFormat("HH:mm:ss").format(Date())
                ).observe(this,observeTmVisit)
        }else{
            showProgressBar(false)
            showSomeDialog(this, "No Outlet populated for this rep","Outlet Error")
        }
    }

    private val observeTmVisit = Observer<String> {
        if (it =="OK") {
            showProgressBar(false)

            preferencesSwitcher!!.edit().clear().apply()
            val editor = preferencesSwitcher!!.edit()
            editor.clear()
            editor.putInt("switch_locat_remote", 200)
            editor.apply()

            val list: List<AllOutletsList> = dataLocalList
            mAdapter = OutletRemoteAdapter(list, this, preferencesByInfo!!.getString("specific_mode_id","")!!)
            mAdapter.notifyDataSetChanged()
            _r_view_pager.adapter = mAdapter
        }
    }

    companion object{
        var TAG = "DODOS"
    }
}
