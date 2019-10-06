package com.mobbile.paul.mttms.ui.customers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.AllCustomersList
import com.mobbile.paul.mttms.ui.outlets.Outlets
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.customers_adapters.view.*
import kotlinx.android.synthetic.main.module_adapter.view.imageView
import kotlinx.android.synthetic.main.module_adapter.view.tv_name
import java.text.SimpleDateFormat
import java.util.*


class CustomersAdapter(private var mItems: List<AllCustomersList>,
                       private var context: Context,
                       private var preferencesByAdapter: SharedPreferences?) : RecyclerView.Adapter<CustomersAdapter.ViewHolder>() {


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
        fun bind(item: AllCustomersList) {

            var letter: String? = item.fullname.substring(0, 1)
            var generator = ColorGenerator.MATERIAL
            var drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor())
            containerView.imageView.setImageDrawable(drawable)

            containerView.tv_name.text = item.fullname.toLowerCase().capitalize()
            containerView.tv_titles.text = ("${item.ecode}, ${item.custcode}").toLowerCase().capitalize()

            containerView.setOnClickListener {
                message(item.fullname.toLowerCase().capitalize(), item.employeeid, item.custcode, item.ecode, item.fullname)
            }
        }

        fun visitRepWithSharePreference() {
            var intent = Intent(context, Outlets::class.java)
            context.startActivity(intent)
        }


        @SuppressLint("SimpleDateFormat")
        fun setPreferences(employeeid: Int, custcode: String, ecode: String, fname: String) {
            preferencesByAdapter!!.edit().clear().apply()
            val editor = preferencesByAdapter!!.edit()
            editor.clear()
            editor.putString("specific_rep_date", SimpleDateFormat("yyyy-MM-dd").format(Date()))
            editor.putInt("specific_rep_changevalues", 200)
            editor.putInt("specific_rep_id",employeeid)
            editor.putString("specific_customer_id",custcode)
            editor.putString("specific_edcode_id",ecode)
            editor.putString("specific_fname_id",fname)
            editor.apply()
            visitRepWithSharePreference()
        }

        private fun message(name:String, employeeid: Int, custcode: String, ecode: String, fname: String) {
            val builder = AlertDialog.Builder(context, R.style.AlertDialogDanger)
            builder.setMessage("Are you sure you want to visit $name")
                .setTitle("Customer Visit")
                .setIcon(R.drawable.icons8_google_alerts_100)
                .setCancelable(false)
                .setNegativeButton("YES") { _, _ ->
                    setPreferences(employeeid, custcode, ecode, fname)
                }
                .setPositiveButton("NO") { _, _ ->
                }
            val dialog = builder.create()
            dialog.show()
        }
    }
}

