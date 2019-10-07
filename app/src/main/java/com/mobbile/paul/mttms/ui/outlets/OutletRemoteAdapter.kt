package com.mobbile.paul.mttms.ui.outlets

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.AllOutletsList
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.outlet_adapter.view.*
import android.widget.PopupMenu
import android.widget.Toast
import com.mobbile.paul.mttms.ui.outlets.updateoutlets.OutletUpdate


class OutletRemoteAdapter(private var mItems: List<AllOutletsList>,
                          private var context: Context
) : RecyclerView.Adapter<OutletRemoteAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.outlet_adapter, p0, false)
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
        fun bind(item: AllOutletsList) {

            val letter: String? = item.outletname.substring(0, 1)
            val generator = ColorGenerator.MATERIAL
            val drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor())
            containerView.imageView.setImageDrawable(drawable)

            containerView.tv_name.text = item.outletname.toLowerCase().capitalize()
            containerView.tv_titles.text = ("${item.urno}, ${item.customerno}").toLowerCase().capitalize()

            containerView.icons_images.setOnClickListener {
                showPopup(containerView, item)
            }
        }

        private fun showPopup(view: View, item: AllOutletsList) {
            val popupMenu = PopupMenu(context, view.icons_images)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.floatingcontextmenu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.entries_id -> {
                        Toast.makeText(context, item.id.toString(), Toast.LENGTH_SHORT).show()
                    }
                    R.id.outlet_photo -> {
                        Toast.makeText(context, item.id.toString(), Toast.LENGTH_SHORT).show()
                    }
                    R.id.map_outlet -> {
                        Toast.makeText(context, item.id.toString(), Toast.LENGTH_SHORT).show()
                    }
                    R.id.outlet_nav -> {
                        Toast.makeText(context, item.id.toString(), Toast.LENGTH_SHORT).show()
                    }
                    R.id.update_outlet -> {
                        updateOutlets(item)
                    }
                    R.id.v_details -> {
                        Toast.makeText(context, item.id.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            popupMenu.show()
        }

        private fun updateOutlets(item: AllOutletsList) {

            val passer = AllOutletsList(
                item.auto, item.id,item.urno, item.customerno,
                item.outletclassid, item.outletlanguageid,
                item.outlettypeid, item.outletname, item.outletaddress,
                item.contactname, item.contactphone, item.latitude,
                item.longitude, item.outlet_pic
            )

            var intent = Intent(context, OutletUpdate::class.java)
            intent.putExtra("extra_item", passer)
            context.startActivity(intent)
        }

    }
}

