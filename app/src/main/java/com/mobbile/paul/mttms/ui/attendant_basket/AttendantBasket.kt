package com.mobbile.paul.mttms.ui.attendant_basket


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.AttendantData
import com.mobbile.paul.mttms.models.ProductBiData
import com.mobbile.paul.mttms.ui.customers.Customers
import com.mobbile.paul.mttms.util.Util.showMsgDialog
import com.mobbile.paul.mttms.util.Util.showSomeDialog
import kotlinx.android.synthetic.main.activity_attendant_basket.*
import javax.inject.Inject

class AttendantBasket : BaseActivity()  {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: AttendantViewModel

    private lateinit var nAdapter: AttandantAdapter

    var customer_code: String = ""
    var tmid: Int = 0
    var repid: Int = 0
    var mode:Int = 0
    var sortid:Int = 0
    var sequenceno:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendant_basket)
        vmodel = ViewModelProviders.of(this, modelFactory)[AttendantViewModel::class.java]
        customer_code = intent.getStringExtra("customer_code")!!
        tmid = intent.getIntExtra("tmid",0)
        repid = intent.getIntExtra("repid",0)
        sortid = intent.getIntExtra("sortid",0)
        sequenceno = intent.getIntExtra("sequenceno",0)
        vmodel.getRepBasket(customer_code).observe(this, observebasket)
        vmodel.attendantMutable().observe(this, observeAttendant)
        init()
    }

    fun init() {

        refresh_image.setOnClickListener {
            showProgressBar(true)
            vmodel.getRepBasket(customer_code).observe(this, observebasket)
        }

        backbtn.setOnClickListener {
            onBackPressed()
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        view_pager.layoutManager = layoutManager
        view_pager!!.setHasFixedSize(true)

        resumebtn.setOnClickListener {
            mode = 1
            showProgressBar(true)
            vmodel.takeAttendant(tmid,1,repid,sortid,"${sequenceno}")
        }

        clockoutbtn.setOnClickListener {
            mode = 2
            showProgressBar(true)
            vmodel.takeAttendant(tmid,2,repid, sortid,"${sequenceno}")
        }
    }

    private val observeAttendant = Observer<AttendantData> {
        when(it.status) {
            200->{
                showProgressBar(false)
                if(mode==1) {
                    showSomeDialog(this, it.notis, "Successful")
                }else{
                    showMsgDialog(Customers(),this,"Successful", it.notis)
                }
            }else->{
                showProgressBar(false)
                showSomeDialog(this, it.notis, "Error")
            }
        }
    }

    private val observebasket = Observer<ProductBiData> {
        val data: ProductBiData  = it
        when(it.status) {
            200->{
                showProgressBar(false)
                nAdapter = AttandantAdapter(data.list!!)
                nAdapter.notifyDataSetChanged()
                view_pager.setItemViewCacheSize(data.list!!.size)
                view_pager.adapter = nAdapter
                refresh_image.visibility = View.INVISIBLE
            }else->{
                showProgressBar(false)
                showSomeDialog(this, "${it.msg} ${customer_code}", "Basket Error")
                refresh_image.visibility = View.VISIBLE
            }
        }
    }


    companion object{
        var TAG = "AttendantBasket"
    }

}
