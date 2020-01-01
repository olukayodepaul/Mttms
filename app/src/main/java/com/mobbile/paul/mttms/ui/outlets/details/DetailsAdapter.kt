package com.mobbile.paul.mttms.ui.outlets.details

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.DetailsList
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.order_adapter_layout.view.*

class DetailsAdapter(private var mItems: List<DetailsList>):
    RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.order_adapter_layout, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: DetailsList) {

            containerView.mt_sku_id_tv.text = item.sku.toLowerCase().capitalize()
            containerView.mt_inventory_id_tv.text = item.inventory
            containerView.mt_pricing_id_tv.text = item.princing
            if(item.category.equals("competition")){
                containerView.mt_sku_id_tv.setTextColor(Color.parseColor("#01579B"))
            }
        }
    }
}

