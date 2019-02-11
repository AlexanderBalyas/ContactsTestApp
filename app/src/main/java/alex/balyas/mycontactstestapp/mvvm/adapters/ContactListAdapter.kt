package alex.balyas.mycontactstestapp.mvvm.adapters

import alex.balyas.mycontactstestapp.R
import alex.balyas.mycontactstestapp.db.Contact
import alex.balyas.mycontactstestapp.mvvm.details.DetailsActivity
import alex.balyas.mycontactstestapp.mvvm.filter.FilterSingleton
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_contact.view.*

/**
 * Created by Alex Balyas on 09.02.2019.
 */
class ContactListAdapter(private val context: Context) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    var contactList: List<Contact>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ContactViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.card_contact, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun getItemCount(): Int = contactList?.size ?: 0


    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        contactList?.get(position)?.let { holder.bind(it) }
    }


    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(contact: Contact) {
            val filterPosition = FilterSingleton.filterPosition
            val name = if (filterPosition in 0..2)
                "${contact.firstName} ${contact.secondName}"
            else "${contact.secondName} ${contact.firstName}"
            itemView.tv_name.text = name

            itemView.rl_holder.setOnClickListener {
                val intent = Intent(itemView.context, DetailsActivity::class.java)
                intent.putExtra("contact", contact)
                itemView.context.startActivity(intent)
            }
        }
    }
}