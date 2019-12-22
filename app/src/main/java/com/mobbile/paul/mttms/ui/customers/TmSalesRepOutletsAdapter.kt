package com.mobbile.paul.mttms.ui.customers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.EntityAllOutletsList
import com.mobbile.paul.mttms.ui.StockReturn.StockReturn
import com.mobbile.paul.mttms.ui.attendant_basket.AttendantBasket
import com.mobbile.paul.mttms.ui.banks.Banks
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.outlet_adapter.view.*
import kotlin.reflect.KFunction2


class TmSalesRepOutletsAdapter(

    private var mItems: List<EntityAllOutletsList>,
    private var context: Context,
    private var clickListener: KFunction2<@ParameterName(name = "partItem") EntityAllOutletsList, @ParameterName(name = "separator") Int, Unit>

) : RecyclerView.Adapter<TmSalesRepOutletsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.outlet_adapter, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item,clickListener)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        @SuppressLint("DefaultLocale")
        fun bind(
            item: EntityAllOutletsList,
            itemClickListener: KFunction2<@ParameterName(name = "partItem") EntityAllOutletsList, @ParameterName(name = "separator") Int, Unit>
        ) {
            val letter: String? = item.outletname.substring(0, 1).toUpperCase()
            val generator = ColorGenerator.MATERIAL
            val drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor())
            containerView.imageView.setImageDrawable(drawable)

            containerView.tv_name.text = item.outletname.toLowerCase().capitalize()
            containerView.tv_titles.text = ("${item.urno}, ${item.customerno}").toLowerCase().capitalize()
            containerView.tv_sequence.text = "${item.sequenceno}"

            containerView.icons_images.setOnClickListener {
                showPopup(containerView, item, itemClickListener)
            }

            when(item.sort){
                1->{
                    containerView.icons_images.visibility = View.GONE
                    containerView.tv_sequence.visibility = View.GONE
                    containerView.tv_titles.text = item.notice.toLowerCase().capitalize()
                }
                3->{
                    containerView.icons_images.visibility = View.GONE
                    containerView.tv_sequence.visibility = View.GONE
                    containerView.tv_titles.text = item.notice.toLowerCase().capitalize()
                }
                4->{
                    containerView.icons_images.visibility = View.GONE
                    containerView.tv_sequence.visibility = View.GONE
                    containerView.tv_titles.text = item.notice.toLowerCase().capitalize()
                }
            }

            containerView.setOnClickListener {
                var intent: Intent? = null
                when(item.sort) {
                    1->{
                        intent = Intent(context, AttendantBasket::class.java)
                    }
                    3->{
                        intent = Intent(context, Banks::class.java)
                    }
                    4->{
                        intent = Intent(context, StockReturn::class.java)
                    }
                }
                context.startActivity(intent)
            }
        }


        private fun showPopup(
            view: View, item: EntityAllOutletsList,
            itemClickListener: KFunction2<@ParameterName(name = "partItem") EntityAllOutletsList, @ParameterName(name = "separator") Int, Unit>
        ) {

            val popupMenu = PopupMenu(context, view.icons_images)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.floatingcontextmenu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.entries_id -> {
                        itemClickListener(item,100)
                    }
                    R.id.outlet_nav -> {
                        itemClickListener(item,200)
                    }
                    R.id.update_outlet -> {
                        itemClickListener(item,300)
                    }
                    R.id.close_outlet -> {
                        itemClickListener(item,400)
                    }
                }
                true
            }
            popupMenu.show()
        }
    }

    companion object {
        var TAG = "TYTYTYTYTTYYTTY"
    }
}



