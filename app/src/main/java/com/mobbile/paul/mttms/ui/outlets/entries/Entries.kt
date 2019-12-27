package com.mobbile.paul.mttms.ui.outlets.entries

import android.app.AlertDialog
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
import com.mobbile.paul.mttms.models.EntryCallback
import com.mobbile.paul.mttms.models.setSalesEntry
import com.mobbile.paul.mttms.providers.Repository
import com.mobbile.paul.mttms.ui.outlets.sku.SkuActivity
import com.mobbile.paul.mttms.util.Utils
import com.mobbile.paul.mttms.util.Utils.Companion.USER_INFOS
import kotlinx.android.synthetic.main.activity_entries.*
import javax.inject.Inject

class Entries : BaseActivity() {


    var urno:Int = 0
    var outletname:String = ""
    var defaulttoken:String = ""
    var visit_sequence:Int = 0
    var currentLat:Double = 0.0
    var currentLng:Double = 0.0
    var outletLat:Double = 0.0
    var outletLng:Double = 0.0
    var distance:String = ""
    var durations:String = ""
    var arivaltime:String = ""
    var repname:String = ""


    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var repository: Repository

    lateinit var vmodel: EntriesViewModel

    private lateinit var mAdapter: EntriesAdapter

    private var preferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)

        vmodel = ViewModelProviders.of(this, modelFactory)[EntriesViewModel::class.java]
        repname = intent.getStringExtra("repname")!!
        outletname = intent.getStringExtra("outletname")!!
        tv_outlet_name.text = repname
        tv_modules.text = "Customer: $outletname"

        back_btn.setOnClickListener {
            onBackPressed()
        }

        /*

        vmodel.fetchSales(preferencesByInfo!!.getInt("specific_rep_id",0),
            preferencesByInfo!!.getString("specific_customer_id","")!!,
            custUrno)

        vmodel.comData().observe(this, observeComData)
        initViews()
         */
    }
    /*

    override fun onRestart() {
        super.onRestart()
        showProgressBar(false)
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
        Log.d(TAG, "1-$custToken 2-$defaultCustToken 3-$custids 4-$custUrno")
        if(it==0) {
            val intent = Intent(this, SkuActivity::class.java)
            intent.putExtra("token",custToken)
            intent.putExtra("dtoken",defaultCustToken)
            intent.putExtra("outletids",custids)
            intent.putExtra("urno",custUrno)
            intent.putExtra("rep_employee_id",preferencesByInfo!!.getInt("specific_rep_id",0))
            intent.putExtra("tm_employee_id",preferences!!.getInt("employee_id_user_preferences",0))
            intent.putExtra("passerLat",0.0)
            intent.putExtra("passerLng",0.0)
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
    */

}

