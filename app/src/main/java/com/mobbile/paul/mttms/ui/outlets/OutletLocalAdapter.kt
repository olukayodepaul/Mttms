package com.mobbile.paul.mttms.ui.outlets

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.EntityAllOutletsList
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.outlet_adapter.view.*


class OutletLocalAdapter(private var mItems: List<EntityAllOutletsList>,
                         private var context: Context
) : RecyclerView.Adapter<OutletLocalAdapter.ViewHolder>() {


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

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: EntityAllOutletsList) {


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

        fun setIntents() {
            var intent = Intent(context, Outlets::class.java)
            context.startActivity(intent)
        }

        private fun showPopup(view: View, item: EntityAllOutletsList) {
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
                        Toast.makeText(context, item.id.toString(), Toast.LENGTH_SHORT).show()
                    }
                    R.id.v_details -> {
                        Toast.makeText(context, item.id.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            popupMenu.show()
        }

    }
}

