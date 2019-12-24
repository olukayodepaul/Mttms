package com.mobbile.paul.mttms.ui.customers


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.AllTheSalesRep
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.customers_adapters.view.*
import kotlinx.android.synthetic.main.module_adapter.view.imageView
import kotlinx.android.synthetic.main.module_adapter.view.tv_name




class TmSalesRepListAdapter(private var mItems: List<AllTheSalesRep>,
                            private val clickListener: (AllTheSalesRep) -> Unit
                        ) : RecyclerView.Adapter<TmSalesRepListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.customers_adapters, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item,clickListener)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        @SuppressLint("SetTextI18n")
        fun bind(item: AllTheSalesRep, itemclickListener: (AllTheSalesRep) -> Unit) {

            val letter: String? = item.fullname.substring(0, 1)
            val generator = ColorGenerator.MATERIAL
            val drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor())
            containerView.imageView.setImageDrawable(drawable)

            containerView.tv_name.text = item.fullname
            containerView.tv_titles.text = "${item.ecode}, ${item.custcode}"
            containerView.setOnClickListener { itemclickListener(item)}
        }
    }
}

