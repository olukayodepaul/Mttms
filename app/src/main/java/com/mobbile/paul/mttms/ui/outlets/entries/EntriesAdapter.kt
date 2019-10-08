package com.mobbile.paul.mttms.ui.outlets.entries

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.setSalesEntry
import com.mobbile.paul.mttms.providers.Repository
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.sales_entry_adapter.view.*
import java.text.SimpleDateFormat
import java.util.*


class EntriesAdapter(
    private var mItems: List<setSalesEntry>,
    private var repository: Repository
) :
    RecyclerView.Adapter<EntriesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.sales_entry_adapter, p0, false)
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
        private val TAG = "SalesEntriesAdapter"
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: setSalesEntry) {

            containerView.tv_skus.text = item.product_name.toLowerCase().capitalize()
            containerView.tv_soq.text = item.soq

            if (item.seperator.equals("3")) {
                containerView.tv_skus.setTextColor(Color.parseColor("#01579B"))
            }

            if (item.soq.isEmpty()) {
                containerView.tv_soq.text = "0"
            }

            containerView.mt_pricing.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                        var trasformOrder = 0.0
                        var trasformPricing = 0
                        var trasformInventory = 0.0
                        var controltrasformOrder = ""
                        var controltrasformPricing = ""
                        var controltrasformInventory = ""

                        if (containerView.mt_order.text.toString().isNotEmpty()) {
                            trasformOrder = containerView.mt_order.text.toString().toDouble()
                            controltrasformOrder = "0"
                        }

                        if (containerView.mt_pricing.text.toString().isNotEmpty()) {
                            trasformPricing = containerView.mt_pricing.text.toString().toInt()
                            controltrasformPricing = "0"
                        }

                        if (containerView.mt_inventory.text.toString().isNotEmpty()) {
                            trasformInventory = containerView.mt_inventory.text.toString().toDouble()
                            controltrasformInventory = "0"
                        }

                        enterDailySales(
                            trasformOrder,
                            trasformInventory,
                            trasformPricing,
                            SimpleDateFormat("HH:mm:ss").format(Date()),
                            item.product_id,
                            trasformOrder * item.price.toDouble(),
                            controltrasformOrder,
                            controltrasformPricing,
                            controltrasformInventory
                        )
                    }
                })

            containerView.mt_inventory.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (containerView.mt_inventory.text.toString() == ".") {

                            containerView.mt_inventory.setText("")

                        } else {

                            var trasformOrder = 0.0
                            var trasformPricing = 0
                            var trasformInventory = 0.0
                            var controltrasformOrder = ""
                            var controltrasformPricing = ""
                            var controltrasformInventory = ""

                            if (containerView.mt_order.text.toString().isNotEmpty()) {
                                trasformOrder = containerView.mt_order.text.toString().toDouble()
                                controltrasformOrder = "0"
                            }

                            if (containerView.mt_pricing.text.toString().isNotEmpty()) {
                                trasformPricing = containerView.mt_pricing.text.toString().toInt()
                                controltrasformPricing = "0"
                            }

                            if (containerView.mt_inventory.text.toString().isNotEmpty()) {
                                trasformInventory =
                                    containerView.mt_inventory.text.toString().toDouble()
                                controltrasformInventory = "0"

                            }
                            enterDailySales(
                                trasformOrder,
                                trasformInventory,
                                trasformPricing,
                                SimpleDateFormat("HH:mm:ss").format(Date()),
                                item.product_id,
                                trasformOrder * item.price.toDouble(),
                                controltrasformOrder,
                                controltrasformPricing,
                                controltrasformInventory
                            )
                        }
                    }
                })

            containerView.mt_order.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                        if (containerView.mt_order.text.toString() == ".") {

                            containerView.mt_order.setText("")

                        } else {

                            var trasformOrder = 0.0
                            var trasformPricing = 0
                            var trasformInventory = 0.0
                            var controltrasformOrder = ""
                            var controltrasformPricing = ""
                            var controltrasformInventory = ""

                            if (containerView.mt_order.text.toString().isNotEmpty()) {
                                trasformOrder = containerView.mt_order.text.toString().toDouble()
                                controltrasformOrder = "0"
                            }

                            if (containerView.mt_pricing.text.toString().isNotEmpty()) {
                                trasformPricing = containerView.mt_pricing.text.toString().toInt()
                                controltrasformPricing = "0"
                            }

                            if (containerView.mt_inventory.text.toString().isNotEmpty()) {
                                trasformInventory =
                                    containerView.mt_inventory.text.toString().toDouble()
                                controltrasformInventory = "0"
                            }

                            if ((item.qty) < trasformOrder) {
                                containerView.mt_order.setText("")
                            } else {
                                enterDailySales(
                                    trasformOrder,
                                    trasformInventory,
                                    trasformPricing,
                                    SimpleDateFormat("HH:mm:ss").format(Date()),
                                    item.product_id,
                                    trasformOrder * item.price.toDouble(),
                                    controltrasformOrder,
                                    controltrasformPricing,
                                    controltrasformInventory
                                )
                            }
                        }
                    }
                })
        }

        fun enterDailySales(
            orders: Double, inventory: Double, pricing: Int,
            entry_time: String,  product_id: String,
            salesprice: Double, contOrder: String, contPrincing: String, contInventory: String
        ) {
            repository.updateDailySales (
                orders, inventory,
                pricing, entry_time,  product_id, salesprice, contOrder, contPrincing, contInventory
            ).subscribe()
        }
    }
}