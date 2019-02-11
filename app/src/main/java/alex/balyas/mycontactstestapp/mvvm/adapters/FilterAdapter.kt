package alex.balyas.mycontactstestapp.mvvm.adapters

import alex.balyas.mycontactstestapp.R
import alex.balyas.mycontactstestapp.mvvm.filter.FilterActivity
import alex.balyas.mycontactstestapp.mvvm.filter.FilterSingleton
import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_filter.view.*

/**
 * Created by Alex Balyas on 11.02.2019.
 */
class FilterAdapter(val context: Context) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    var currentCheckedPosition = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var items: List<String>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): FilterViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.card_filter, parent, false)
        return FilterViewHolder(itemView)
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(items?.get(position) ?: "", currentCheckedPosition)
    }


    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String, currentCheckedPosition: Int) {
            itemView.tv_filter_item.text = item
            if (currentCheckedPosition == layoutPosition)
                itemView.iv_filter_item.visibility = View.VISIBLE
            else itemView.iv_filter_item.visibility = View.GONE

            itemView.layout.setOnClickListener {
                FilterSingleton.filterPosition = layoutPosition
                (context as FilterActivity).setResult(Activity.RESULT_OK)
                context.finish()
            }
        }
    }
}