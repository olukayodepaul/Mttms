package com.mobbile.paul.mttms.ui.customers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.AllCustomersList
import com.mobbile.paul.mttms.ui.modules.Modules
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.customers_adapters.view.*
import kotlinx.android.synthetic.main.module_adapter.view.imageView
import kotlinx.android.synthetic.main.module_adapter.view.tv_name
import java.text.SimpleDateFormat
import java.util.*


class CustomersAdapter(private var mItems: List<AllCustomersList>,
                       private var context: Context,
                       private var preferencesByVisit: SharedPreferences?,
                       private var preferencesByInfo: SharedPreferences?
                       ) : RecyclerView.Adapter<CustomersAdapter.ViewHolder>() {


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
        @SuppressLint("DefaultLocale", "SetTextI18n")
        fun bind(item: AllCustomersList) {

            var letter: String? = item.fullname.substring(0, 1).toUpperCase()
            var generator = ColorGenerator.MATERIAL
            var drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor())
            containerView.imageView.setImageDrawable(drawable)

            containerView.tv_name.text = item.fullname.toLowerCase().capitalize()
            containerView.tv_titles.text = "${item.ecode.toLowerCase().toUpperCase()}, ${item.custcode.toUpperCase()}"

            containerView.setOnClickListener {
                message(item.fullname.toLowerCase().capitalize(), item.employeeid, item.custcode, item.ecode, item.fullname, item.mode)
            }
        }

        fun setInfoSharePref(employeeid: Int, custcode: String, ecode: String, fname: String, mode:String) {
            preferencesByInfo!!.edit().clear().apply()
            val editor = preferencesByInfo!!.edit()
            editor.clear()
            editor.putInt("specific_rep_id",employeeid)
            editor.putString("specific_customer_id",custcode)
            editor.putString("specific_edcode_id",ecode)
            editor.putString("specific_fname_id",fname)
            editor.putString("specific_mode_id",mode)
            editor.apply()
            setVisitSharePref()
            Log.d(TAG, preferencesByInfo!!.getString("specific_edcode_id","").toString() + "~"+ ecode)
        }

        @SuppressLint("SimpleDateFormat")
        fun setVisitSharePref() {
            preferencesByVisit!!.edit().clear().apply()
            val editor = preferencesByVisit!!.edit()
            editor.clear()
            editor.putString("specific_rep_date", SimpleDateFormat("yyyy-MM-dd").format(Date()))
            editor.putInt("specific_rep_changevalues", 200)
            editor.apply()
            setIntent()
        }

        fun setIntent() {
            var intent = Intent(context, Modules::class.java)
            context.startActivity(intent)
        }

        private fun message(name:String, employeeid: Int, custcode: String, ecode: String, fname: String, mode:String) {
            val builder = AlertDialog.Builder(context, R.style.AlertDialogDanger)
            builder.setMessage("Are you sure you want to visit $name")
                .setTitle("Customer Visit")
                .setIcon(R.drawable.icons8_google_alerts_100)
                .setCancelable(false)
                .setNegativeButton("YES") { _, _ ->
                    setInfoSharePref(employeeid, custcode, ecode, fname, mode)
                }
                .setPositiveButton("NO") { _, _ ->
                }
            val dialog = builder.create()
            dialog.show()
        }
    }
}

