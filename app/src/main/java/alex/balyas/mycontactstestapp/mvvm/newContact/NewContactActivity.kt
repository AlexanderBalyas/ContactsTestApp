package alex.balyas.mycontactstestapp.mvvm.newContact

import alex.balyas.mycontactstestapp.R
import alex.balyas.mycontactstestapp.db.Contact
import alex.balyas.mycontactstestapp.mvvm.main.MainActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_new_contact.*

class NewContactActivity : AppCompatActivity() {

    private lateinit var model: NewContactViewModel

    private var isEditMode: Boolean = false
    private var editContact: Contact? = null

    private var phoneEditTexts = ArrayList<EditText>()
    private var emailEditTexts = ArrayList<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_contact)
        model = ViewModelProviders.of(this).get(NewContactViewModel::class.java)

        initClickLogic()
        initResultListeners()

        phoneEditTexts.add(et_phone_default)
        emailEditTexts.add(et_email_default)

        isEditMode = intent.getBooleanExtra("edit_mode", false)
        editContact = intent.getParcelableExtra("contact")

        if (isEditMode) {
            initEditUserLogic()
        }
    }

    private fun initClickLogic() {

        bt_add_phone.setOnClickListener {
            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val editText = EditText(this)
            params.setMargins(8, 4, 8, 4)
            editText.layoutParams = params
            editText.inputType = InputType.TYPE_CLASS_PHONE


            phoneEditTexts.add(editText)
            ll_phone_holder.addView(editText)
        }

        bt_delete_phone.setOnClickListener {
            if (phoneEditTexts.size > 1) {
                val editText = phoneEditTexts[phoneEditTexts.size - 1]
                phoneEditTexts.removeAt(phoneEditTexts.size - 1)
                ll_phone_holder.removeView(editText)
            }
        }


        bt_add_email.setOnClickListener {
            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val editText = EditText(this)
            params.setMargins(8, 4, 8, 4)
            editText.layoutParams = params
            editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS


            emailEditTexts.add(editText)
            ll_email_holder.addView(editText)
        }

        bt_delete_email.setOnClickListener {
            if (emailEditTexts.size > 1) {
                val editText = emailEditTexts[emailEditTexts.size - 1]
                emailEditTexts.removeAt(emailEditTexts.size - 1)
                ll_email_holder.removeView(editText)
            }
        }

        bt_create.setOnClickListener {
            val account = GoogleSignIn.getLastSignedInAccount(this)
            var userID = ""
            account?.let { userID = it.email ?: "" }

            if (isEditMode) {
                model.updateContact(
                        userID,
                        editContact?.id ?: "",
                        et_name.text.toString(),
                        et_surname.text.toString(),
                        getInfoFromEditTexts(phoneEditTexts),
                        getInfoFromEditTexts(emailEditTexts)
                )
            } else {
                model.createContact(
                        userID,
                        et_name.text.toString(),
                        et_surname.text.toString(),
                        getInfoFromEditTexts(phoneEditTexts),
                        getInfoFromEditTexts(emailEditTexts)
                )
            }
        }
    }

    private fun initEditUserLogic() {
        editContact?.let {
            et_name.setText(editContact?.firstName)
            et_surname.setText(editContact?.secondName)

            editContact?.phones?.let {
                for (pos in 0 until it.size) {
                    if (pos == 0) et_phone_default.setText(it[pos])
                    else {
                        val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        val editText = EditText(this)
                        params.setMargins(8, 4, 8, 4)
                        editText.layoutParams = params
                        editText.inputType = InputType.TYPE_CLASS_PHONE

                        editText.setText(it[pos])

                        phoneEditTexts.add(editText)
                        ll_phone_holder.addView(editText)
                    }
                }
            }

            editContact?.emails?.let {
                for (pos in 0 until it.size) {
                    if (pos == 0) et_email_default.setText(it[pos])
                    else {
                        val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        val editText = EditText(this)
                        params.setMargins(8, 4, 8, 4)
                        editText.layoutParams = params
                        editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

                        editText.setText(it[pos])

                        emailEditTexts.add(editText)
                        ll_email_holder.addView(editText)
                    }
                }
            }

            bt_create.text = getString(R.string.edit_user)
        }
    }

    private fun initResultListeners() {
        model.createFlag.observe(this, Observer { t ->
            if (t == true) {
                Toast.makeText(this, getString(R.string.success_create_label), Toast.LENGTH_SHORT).show()
                onBackPressed()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.validation_error), Toast.LENGTH_SHORT).show()
            }
        })

        model.editFlag.observe(this, Observer { t ->
            if (t == true) {
                Toast.makeText(this, getString(R.string.success_edit_label), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, getString(R.string.validation_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getInfoFromEditTexts(editTexts: List<EditText>): List<String> {
        val list = ArrayList<String>()
        for (item in editTexts)
            if (item.text.isNotEmpty())
                list.add(item.text.toString())

        return list
    }
}
