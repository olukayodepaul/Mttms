package com.mobbile.paul.mttms.ui.outlets.sku


import android.app.AlertDialog
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
import com.mobbile.paul.mttms.models.SumSales
import kotlinx.android.synthetic.main.activity_sku.*
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class SkuActivity : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: SkuViewmodel

    private lateinit var mAdapter: SkuAdapter

    var token: String? = null

    var urno: String? = null

    var defaulttoken: String? = null

    var outletid: Int? = null

    var repid: Int? = null

    var tmid: Int? = null

    var lat: Double? = null

    var lng: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sku)
        vmodel = ViewModelProviders.of(this, modelFactory)[SkuViewmodel::class.java]
        vmodel.fetch().observe(this, observerOfSalesEntry)
        IntAdapter()
        urno = intent.getStringExtra("urno")
        token = intent.getStringExtra("token")
        defaulttoken = intent.getStringExtra("dtoken")
        outletid = intent.getIntExtra("outletids",0)
        repid = intent.getIntExtra("rep_employee_id",0)
        tmid = intent.getIntExtra("tm_employee_id",0)
        lat = intent.getDoubleExtra("passerLat",0.0)
        lng = intent.getDoubleExtra("passerLng",0.0)

        back_btn.setOnClickListener {
            onBackPressed()
        }

        Log.d(TAG, "1$token 2$defaulttoken 3$repid 4$tmid 5$outletid 6$urno $lat $lng")
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

    fun IntAdapter() {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler_view_complete.layoutManager = layoutManager
        recycler_view_complete!!.setHasFixedSize(true)

        btn_complete.setOnClickListener {
            when {
                token.equals(token_form.text.toString().trim()) -> {
                    showProgressBar(true)
                    btn_complete.visibility = View.INVISIBLE
                    //vmodel.pullAllSalesEntry().observe(this, obervePullinSalesData)
                }
                defaulttoken.equals(token_form.text.toString().trim()) -> {
                    showProgressBar(true)
                    btn_complete.visibility = View.INVISIBLE
                    //vmodel.pullAllSalesEntry().observe(this, obervePullinSalesData)
                }
                else -> tokenVerify(2, "Error",  "Invalid Customer Verification code")
            }
        }
    }

    private fun loadTotalSales() {
        vmodel.sumAllSalesEntry().observe(this, obserTotal)
    }

    private val obserTotal = Observer<SumSales> {
        if (it != null) {
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.FLOOR
            s_s_amount.text = String.format("%,.1f",(df.format(it.stotalsum).toDouble()))
            s_s_order.text = String.format("%,.1f",(df.format(it.sorder).toDouble()))
            s_s_pricing.text = String.format("%,.1f",(df.format(it.spricing).toDouble()))
            s_s_invetory.text = String.format("%,.1f",(df.format(it.sinventory).toDouble()))
        }
    }

    private fun tokenVerify(status: Int, title: String, msg: String) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogDanger)
        builder.setMessage(msg)
            .setTitle(title)
            .setIcon(R.drawable.icons8_google_alerts_100)
            .setCancelable(false)
            .setPositiveButton("Ok") { _, _ ->
                if(status==1) {

                }
            }
        val dialog = builder.create()
        dialog.show()
    }

    companion object{
        var TAG = "SkuActivity"
    }

}
