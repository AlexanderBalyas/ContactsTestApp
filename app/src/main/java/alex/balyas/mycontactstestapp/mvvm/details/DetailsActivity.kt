package alex.balyas.mycontactstestapp.mvvm.details

import alex.balyas.mycontactstestapp.R
import alex.balyas.mycontactstestapp.db.Contact
import alex.balyas.mycontactstestapp.mvvm.filter.FilterSingleton
import alex.balyas.mycontactstestapp.mvvm.newContact.NewContactActivity
import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity() {

    var model: DetailsViewModel? = null

    var contact: Contact? = null
    private val MY_PERMISSIONS_REQUEST_CALL_PHONE = 100

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        model = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        contact = intent.getParcelableExtra("contact")

        showContact()
    }


    @SuppressLint("SetTextI18n")
    private fun showContact() {
        contact?.let {
            tv_name.text = "${it.firstName} ${it.secondName}"

            it.phones?.let { phones ->
                for (item in phones) {
                    val lparams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    val textView = TextView(this)
                    textView.layoutParams = lparams
                    textView.text = item
                    textView.setPadding(12, 8, 12, 8)
                    textView.textSize = 18f
                    textView.gravity = Gravity.CENTER
                    textView.setTextColor(resources.getColor(R.color.coloTextGrey))
                    textView.setOnClickListener {
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:$item")

                        if (ContextCompat.checkSelfPermission(
                                        this,
                                        Manifest.permission.CALL_PHONE
                                ) != PackageManager.PERMISSION_GRANTED
                        ) {


                            ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(Manifest.permission.CALL_PHONE),
                                    MY_PERMISSIONS_REQUEST_CALL_PHONE
                            )

                        } else {
                            try {
                                startActivity(callIntent)
                            } catch (e: SecurityException) {
                                e.printStackTrace()
                            }

                        }
                    }
                    ll_phones_holder.addView(textView)

                    val lpForView = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 1
                    )
                    val view = View(this)
                    view.layoutParams = lpForView
                    view.setBackgroundColor(resources.getColor(R.color.coloTextGrey))
                    ll_phones_holder.addView(view)
                }
            }

            it.emails?.let { emails ->
                for (item in emails) {
                    val lp = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    val textView = TextView(this)
                    textView.layoutParams = lp
                    textView.text = item
                    textView.setPadding(12, 8, 12, 8)
                    textView.textSize = 18f
                    textView.gravity = Gravity.CENTER
                    textView.setTextColor(resources.getColor(R.color.coloTextGrey))
                    textView.setOnClickListener {
                        val emailIntent = Intent(
                                Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", item, null
                        )
                        )
                        startActivity(Intent.createChooser(emailIntent, "Send email..."))
                    }
                    ll_emails_holder.addView(textView)

                    val lpForView = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 1
                    )
                    val view = View(this)
                    view.layoutParams = lpForView
                    view.setBackgroundColor(resources.getColor(R.color.coloTextGrey))
                    ll_emails_holder.addView(view)
                }
            }


            bt_delete.setOnClickListener { _ ->
                model?.deleteContact(it)
                model?.deleteFlag?.observe(this, Observer { t ->
                    if (t == true) {
                        Toast.makeText(this, getString(R.string.success_delete_label), Toast.LENGTH_SHORT).show()
                        onBackPressed()
                        finish()
                    } else {
                        Toast.makeText(this, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
                    }
                })
            }

            bt_edit.setOnClickListener {
                val intent = Intent(this, NewContactActivity::class.java)
                intent.putExtra("edit_mode", true)
                intent.putExtra("contact", contact)
                startActivity(intent)
            }

        }
    }

}
