package com.mobbile.paul.mttms.ui.modules

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.EntityModules
import com.mobbile.paul.mttms.ui.customers.Customers
import com.mobbile.paul.mttms.ui.messages.Messages
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.module_adapter.view.*

class ModulesAdapter(private var mItems: List<EntityModules>, private var contexts: Context):
    RecyclerView.Adapter<ModulesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.module_adapter, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    companion object {
        private val TAG = "ModulesActivity"
    }

    inner class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: EntityModules) {
            containerView.tv_name.text = item.name

            Glide.with(contexts)
                .load(item.imageurl)
                .override(50,50)
                .into(containerView.imageView)

            containerView.setOnClickListener {

                var switcher:Int = item.nav
                var intent: Intent? = null

                when(switcher) {
                    4->{
                        intent = Intent(contexts, Customers::class.java)
                    }
                    3->{
                        intent = Intent(contexts, Messages::class.java)
                    }
                }
                contexts.startActivity(intent)
            }
        }
    }
}