package com.mobbile.paul.mttms.ui.outlets

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.models.EntityAllOutletsList
import com.mobbile.paul.mttms.models.toAllOutletsList
import com.mobbile.paul.mttms.ui.outlets.entries.Entries
import com.mobbile.paul.mttms.ui.outlets.updateoutlets.OutletUpdate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.outlet_adapter.view.*


class OutletLocalAdapter(
    private var mItems: List<EntityAllOutletsList>,
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

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: EntityAllOutletsList) {

            val letter: String? = item.outletname.substring(0, 1)
            val generator = ColorGenerator.MATERIAL
            val drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor())
            containerView.imageView.setImageDrawable(drawable)

            containerView.tv_name.text = item.outletname.toLowerCase().capitalize()
            containerView.tv_titles.text =
                ("${item.urno}, ${item.customerno}").toLowerCase().capitalize()

            containerView.icons_images.setOnClickListener {
                showPopup(containerView, item)
            }
        }

        private fun showPopup(view: View, item: EntityAllOutletsList) {

            val popupMenu = PopupMenu(context, view.icons_images)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.floatingcontextmenu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.entries_id -> {
                        salesEntries(item)
                    }
                    R.id.outlet_photo -> {

                    }
                    R.id.outlet_nav -> {
                        val ads = "${item.latitude},${item.longitude}"
                        startMapIntent(context, ads, 'd', 't')
                    }
                    R.id.update_outlet -> {
                        updateOutlets(item)
                    }
                }
                true
            }
            popupMenu.show()
        }

        private fun updateOutlets(item: EntityAllOutletsList) {

            val passer = EntityAllOutletsList()

            passer.auto = item.auto
            passer.id = item.id
            passer.urno = item.urno
            passer.customerno = item.customerno
            passer.outletclassid = item.outletclassid
            passer.outletlanguageid = item.outletlanguageid
            passer.outlettypeid = item.outlettypeid
            passer.outletname = item.outletname
            passer.outletaddress = item.outletaddress
            passer.contactname = item.contactname
            passer.contactphone = item.contactphone
            passer.latitude = item.latitude
            passer.longitude = item.longitude
            passer.outlet_pic = item.outlet_pic

            val intent = Intent(context, OutletUpdate::class.java)
            intent.putExtra("extra_item", passer.toAllOutletsList())
            context.startActivity(intent)

        }

        fun startMapIntent(ctx: Context, ads: String, mode: Char, avoid: Char): Any {
            val uri = Uri.parse("google.navigation:q=$ads&mode=$mode&avoid=$avoid")
            val mIntent = Intent(Intent.ACTION_VIEW, uri)
            mIntent.`package` = "com.google.android.apps.maps"
            return if (mIntent.resolveActivity(ctx.packageManager) != null) {
                ctx.startActivity(mIntent)
                true
            } else
                false
        }

        private fun salesEntries(item: EntityAllOutletsList) {
            val intent = Intent(context, Entries::class.java)
            intent.putExtra("passerUrno", item.urno)
            intent.putExtra("passerCustno", item.customerno)
            intent.putExtra("passerOutletname", item.outletname)
            intent.putExtra("passerLat", item.latitude)
            intent.putExtra("passerLng", item.longitude)
            intent.putExtra("passerToken", item.token)
            intent.putExtra("passerDtoken", item.defaulttoken)
            context.startActivity(intent)
        }

    }
}
