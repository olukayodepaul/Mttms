package com.mobbile.paul.mttms.ui.customers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
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
                            private var context: Context,
                            private var vmodel: CustomersViewModel,
                            private var tm_id: Int
                        ) : RecyclerView.Adapter<TmSalesRepListAdapter.ViewHolder>() {


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
        private val TAG = "PcustomersAdapter"
    }



    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        @SuppressLint("DefaultLocale", "SetTextI18n")
        fun bind(item: AllTheSalesRep) {

            val letter: String? = item.fullname.substring(0, 1).toUpperCase()
            val generator = ColorGenerator.MATERIAL
            val drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor())
            containerView.imageView.setImageDrawable(drawable)

            containerView.tv_name.text = item.fullname.toLowerCase().capitalize()
            containerView.tv_titles.text = "${item.ecode.toUpperCase()}, ${item.custcode.toUpperCase()}"

            containerView.setOnClickListener {
                message(item.employeeid, tm_id, item.fullname)
            }
        }

        fun getAllRepCustomersForToday(repid: Int, tmid:Int) {
            vmodel.getAllCustomerReps(repid, tmid)
        }

        private fun message(repid: Int, tmid:Int, fullname:String) {
            val builder = AlertDialog.Builder(context, R.style.AlertDialogDanger)
            builder.setMessage("Are you sure you want to visit $fullname")
                .setTitle("Customer Visit")
                .setIcon(R.drawable.icons8_google_alerts_100)
                .setCancelable(false)
                .setNegativeButton("YES") { _, _ ->
                    getAllRepCustomersForToday(repid, tmid)
                }
                .setPositiveButton("NO") { _, _ ->
                }
            val dialog = builder.create()
            dialog.show()
        }
    }
}

