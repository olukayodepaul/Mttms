package com.mobbile.paul.mttms.ui.outlets.mapoutlet


import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.EntitySpiners
import kotlinx.android.synthetic.main.activity_map_outlet.*
import javax.inject.Inject

class MapOutlet : BaseActivity(){

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: MapOutletViewModel

    lateinit var customerClassAdapter: CustomerClassSpinnerAdapter

    lateinit var preferedLangAdapter: PreferedLanguageSpinnerAdapter

    lateinit var outletTypeAdapter: OutletTypeSpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_outlet)
        vmodel = ViewModelProviders.of(this, modelFactory)[MapOutletViewModel::class.java]

        backbtn.setOnClickListener {
            onBackPressed()
        }

        customerClassAdapter = CustomerClassSpinnerAdapter()
        preferedLangAdapter = PreferedLanguageSpinnerAdapter()
        outletTypeAdapter = OutletTypeSpinnerAdapter()

        vmodel.fetchSpinners().observe(this, languageObserver)
    }

    val languageObserver = Observer<List<EntitySpiners>> {
        if (it != null) {
            val outletClassList = ArrayList<String>()
            val preLangsList = ArrayList<String>()
            val outletTypeList = ArrayList<String>()
            if (customerClassAdapter.size() > 0) {
                customerClassAdapter.clear()
            }
            if (preferedLangAdapter.size() > 0) {
                preferedLangAdapter.clear()
            }
            if (outletTypeAdapter.size() > 0) {
                outletTypeAdapter.clear()
            }
            for (i in it.indices) {
                when (it[i].sep) {
                    1 -> {
                        outletClassList.add(it[i].name)
                        customerClassAdapter.add(it[i].id, it[i].name)
                    }
                    2 -> {
                        preLangsList.add(it[i].name)
                        preferedLangAdapter.add(it[i].id, it[i].name)
                    }
                    3 -> {
                        outletTypeList.add(it[i].name)
                        outletTypeAdapter.add(it[i].id, it[i].name)
                    }
                }
            }
            val mOutletClass =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, outletClassList)
            mOutletClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            custClass!!.adapter = mOutletClass

            val mPreferedLang =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, preLangsList)
            mPreferedLang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            preflang!!.adapter = mPreferedLang

            val mOutletType =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, outletTypeList)
            mOutletType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            outlettypeedit!!.adapter = mOutletType
            showProgressBar(false)
        }
    }
}
