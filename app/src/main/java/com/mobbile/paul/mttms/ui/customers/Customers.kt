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
import com.mobbile.paul.mttms.util.Utils.Companion.CUSTOMERS_VISITATION
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

    private var preferencesByAdapter: SharedPreferences? = null

    var todayDates: String? = null

    var pDate: String? = null

    var pStatus: Int? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customers)
        vmodel = ViewModelProviders.of(this, modelFactory)[CustomersViewModel::class.java]
        preferences = getSharedPreferences(USER_INFOS, Context.MODE_PRIVATE)
        preferencesByAdapter = getSharedPreferences(CUSTOMERS_VISITATION, Context.MODE_PRIVATE)
        todayDates = SimpleDateFormat("yyyy-MM-dd").format(Date())

        pDate = preferencesByAdapter!!.getString("specific_rep_date", "")
        pStatus = preferencesByAdapter!!.getInt("specific_rep_changevalues", 0)

        switchAdapters()

        initWidget()

        backbtn.setOnClickListener {
            onBackPressed()
        }
    }

    fun switchAdapters(){


        when {
            pDate == todayDates && pStatus == 200 -> {
                Log.d(TAG, "$pDate $pStatus HERE 1")
               vmodel.persistAndFetchCustomers(
                    1,
                    preferencesByAdapter!!.getInt("specific_rep_id",0),
                    preferencesByAdapter!!.getString("specific_edcode_id", ""),
                    preferencesByAdapter!!.getString("specific_customer_id", ""),
                    preferencesByAdapter!!.getString("specific_fname_id", "")
                ).observe(this, PersistObserver)
            }
            pDate == todayDates && pStatus == 300 -> {
                vmodel.fetchCustOnly(
                ).observe(this, PersistObservers)
            }
            pDate == todayDates && pStatus == 400 -> {
                vmodel.fetchCustOnly(
                ).observe(this, PersistObservers)
            }
            else -> {
                Log.d(TAG, "$pDate $pStatus HERE 2")
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
            counts.text = it.counts.toString()
            var list: List<AllCustomersList>? = it.allreps
            mAdapter = CustomersAdapter(list!!, this, preferencesByAdapter)
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
        preferencesByAdapter!!.edit().clear().apply()
        val editor = preferencesByAdapter!!.edit()
        editor.clear()
        editor.putString("specific_rep_date", SimpleDateFormat("yyyy-MM-dd").format(Date()))
        editor.putInt("specific_rep_changevalues", 300)
        editor.putInt("specific_rep_id",preferencesByAdapter!!.getInt("specific_rep_id",0))
        editor.putString("specific_customer_id",preferencesByAdapter!!.getString("specific_customer_id", ""))
        editor.putString("specific_edcode_id",preferencesByAdapter!!.getString("specific_edcode_id", ""))
        editor.putString("specific_fname_id",preferencesByAdapter!!.getString("specific_fname_id", ""))
        editor.apply()
    }

    companion object{
        var TAG = "Customers"
    }
}

