package com.mobbile.paul.mttms.ui.modules

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.EntityModules
import kotlinx.android.synthetic.main.activity_modules.*
import javax.inject.Inject

class Modules : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: ModulesViewModel

    private lateinit var mAdapter: ModulesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modules)
        vmodel = ViewModelProviders.of(this, modelFactory)[ModulesViewModel::class.java]
        vmodel.getAllUsersModules().observe(this, modulesObservers)
        initViews()
        showProgressBar(true)
    }

    private fun initViews() {
        module_recycler.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        module_recycler.layoutManager = layoutManager
    }

    private val modulesObservers = Observer<List<EntityModules>> {
        if (it != null) {
            showProgressBar(false)
            var list: List<EntityModules> = it
            mAdapter = ModulesAdapter(list, this)
            mAdapter.notifyDataSetChanged()
            module_recycler.adapter = mAdapter
        }
    }

    companion object{
        var TAG = "Modules"
    }
}
