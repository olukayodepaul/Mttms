package com.mobbile.paul.mttms.ui.outlets.details


import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.util.Util.showSomeDialog
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

class Details : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: DetailsViewModel

    private lateinit var mAdapter: DetailsAdapter

    var urno: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        urno = intent.getIntExtra("urno", 0)
        vmodel = ViewModelProviders.of(this, modelFactory)[DetailsViewModel::class.java]
        Init()
    }

    fun Init() {

        back_btn.setOnClickListener {
            onBackPressed()
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _recyclers.layoutManager = layoutManager
        _recyclers!!.setHasFixedSize(true)

        vmodel.getEntryDetails(urno).observe(this, Observer {
            if(it.isEmpty()){
                showProgressBar(false)
                showSomeDialog(this, "No Entry for this outlet","Notification")
            }else{
                showProgressBar(false)
                mAdapter = DetailsAdapter(it)
                mAdapter.notifyDataSetChanged()
                _recyclers.setItemViewCacheSize(it.size)
                _recyclers.adapter = mAdapter
            }
        })
    }
}
