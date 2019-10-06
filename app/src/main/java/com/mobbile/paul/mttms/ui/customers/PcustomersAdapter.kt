package com.mobbile.paul.mttms.ui.customers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.EntityAllCustomersList
import com.mobbile.paul.mttms.ui.outlets.Outlets
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.customers_adapters.view.*
import kotlinx.android.synthetic.main.module_adapter.view.imageView
import kotlinx.android.synthetic.main.module_adapter.view.tv_name



class PcustomersAdapter(private var mItems: List<EntityAllCustomersList>,
                        private var context: Context
                        ) : RecyclerView.Adapter<PcustomersAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.customers_adapters, p0, false)
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
        private val TAG = "CustomersAdapter"
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        @SuppressLint("DefaultLocale")
        fun bind(item: EntityAllCustomersList) {

            var letter: String? = item.fullname.substring(0, 1)
            var generator = ColorGenerator.MATERIAL
            var drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor())
            containerView.imageView.setImageDrawable(drawable)

            containerView.tv_name.text = item.fullname.toLowerCase().capitalize()
            containerView.tv_titles.text = ("${item.ecode}, ${item.custcode}").toLowerCase().capitalize()

            containerView.setOnClickListener {
                visitRepWithSharePreference()
            }
        }

        fun visitRepWithSharePreference() {
            var intent = Intent(context, Outlets::class.java)
            context.startActivity(intent)
        }
    }
}

