package com.mobbile.paul.mttms.ui.customers


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
import com.mobbile.paul.mttms.models.AllCustomersList
import com.mobbile.paul.mttms.models.EntityAllCustomersList
import com.mobbile.paul.mttms.models.InitAllCustomers
import com.mobbile.paul.mttms.util.Utils.Companion.CUSTOMERS_INFORMATION
import com.mobbile.paul.mttms.util.Utils.Companion.CUSTOMERS_VISIT
import com.mobbile.paul.mttms.util.Utils.Companion.USER_INFOS
import kotlinx.android.synthetic.main.activity_customer_list_viwe_pager.backbtn
import kotlinx.android.synthetic.main.activity_customers.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Customers : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: CustomersViewModel

    private lateinit var mAdapter: CustomersAdapter

    private lateinit var nAdapter: PcustomersAdapter

    private var preferences: SharedPreferences? = null

    private var preferencesByVisit: SharedPreferences? = null

    private var preferencesByInfo: SharedPreferences? = null

    var todayDates: String? = null

    var pDate: String? = null

    var pStatus: Int? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customers)
        vmodel = ViewModelProviders.of(this, modelFactory)[CustomersViewModel::class.java]

        preferences = getSharedPreferences(USER_INFOS, Context.MODE_PRIVATE)
        preferencesByVisit = getSharedPreferences(CUSTOMERS_VISIT, Context.MODE_PRIVATE)
        preferencesByInfo = getSharedPreferences(CUSTOMERS_INFORMATION, Context.MODE_PRIVATE)

        todayDates = SimpleDateFormat("yyyy-MM-dd").format(Date())

        pDate = preferencesByVisit!!.getString("specific_rep_date", "")
        pStatus = preferencesByVisit!!.getInt("specific_rep_changevalues", 0)

        switchAdapters()

        initWidget()

        backbtn.setOnClickListener {
            onBackPressed()
        }
    }

    fun switchAdapters() {

        Log.d(TAG, pStatus .toString())
        when {
            pDate == todayDates && pStatus == 200 -> {
               vmodel.persistAndFetchCustomers(
                   1,
                   preferencesByInfo!!.getInt("specific_rep_id",0),
                   preferencesByInfo!!.getString("specific_edcode_id", ""),
                   preferencesByInfo!!.getString("specific_customer_id", ""),
                   preferencesByInfo!!.getString("specific_fname_id", "")
                ).observe(this, PersistObserver)
            }
            pDate == todayDates && pStatus == 300 -> {
                vmodel.fetchCustOnly(
                ).observe(this, PersistObservers)
            }
            else -> {
                vmodel.fetchAllCustomers(
                    preferences!!.getInt("depot_id_user_preferences", 0),
                    preferences!!.getInt("region_id_user_preferences", 0)
                ).observe(this, observers)
            }
        }
    }

    val observers = Observer<InitAllCustomers> {
        if (it != null) {
            showProgressBar(false)
            setPreference()
            counts.text = it.counts.toString()
            var list: List<AllCustomersList>? = it.allreps
            mAdapter = CustomersAdapter(list!!, this, preferencesByVisit, preferencesByInfo)
            mAdapter.notifyDataSetChanged()
            _r_view_pager.adapter = mAdapter
        }
    }

    val PersistObserver = Observer<List<EntityAllCustomersList>> {
        if (it != null) {
            showProgressBar(false)
            setPreferences()
            counts.text = "1"
            var list: List<EntityAllCustomersList>? = it
            nAdapter = PcustomersAdapter(list!!, this)
            nAdapter.notifyDataSetChanged()
            _r_view_pager.adapter = nAdapter
        }
    }

    val PersistObservers = Observer<List<EntityAllCustomersList>> {
        if (it != null) {
            showProgressBar(false)
            counts.text = "1"
            var list: List<EntityAllCustomersList>? = it
            nAdapter = PcustomersAdapter(list!!, this)
            nAdapter.notifyDataSetChanged()
            _r_view_pager.adapter = nAdapter
        }
    }

    fun initWidget() {
        _r_view_pager.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _r_view_pager.layoutManager = layoutManager
    }

    fun setPreferences() {
        preferencesByVisit!!.edit().clear().apply()
        val editor = preferencesByVisit!!.edit()
        editor.clear()
        editor.putString("specific_rep_date", SimpleDateFormat("yyyy-MM-dd").format(Date()))
        editor.putInt("specific_rep_changevalues", 300)
        editor.apply()
    }

    fun setPreference() {
        preferencesByVisit!!.edit().clear().apply()
        val editor = preferencesByVisit!!.edit()
        editor.clear()
        editor.putString("specific_rep_date", SimpleDateFormat("yyyy-MM-dd").format(Date()))
        editor.putInt("specific_rep_changevalues", 200)
        editor.apply()
    }

    companion object{
        var TAG = "Outlets"
    }
}

