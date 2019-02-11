package alex.balyas.mycontactstestapp.mvvm.main

import alex.balyas.mycontactstestapp.R
import alex.balyas.mycontactstestapp.db.Contact
import alex.balyas.mycontactstestapp.mvvm.adapters.ContactListAdapter
import alex.balyas.mycontactstestapp.mvvm.filter.FilterActivity
import alex.balyas.mycontactstestapp.mvvm.login.LoginActivity
import alex.balyas.mycontactstestapp.mvvm.newContact.NewContactActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val FILTER_REQUEST = 120

    //    private var contactRepository: ContactRepository? = null
    private lateinit var model: MainViewModel
    lateinit var adapter: ContactListAdapter
    lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    override fun onResume() {
        super.onResume()
        getUserContacts()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILTER_REQUEST) {
            getUserContacts()
        }
    }

    private fun initUI() {
        adapter = ContactListAdapter(this)
        rv_contacts.adapter = adapter

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (null != account)
            userID = account.email ?: ""

        model = ViewModelProviders.of(this).get(MainViewModel::class.java)

        fab.setOnClickListener {
            startActivity(Intent(this, NewContactActivity::class.java))
        }
    }

    private fun getUserContacts() {
        model.getContacts(userID).observe(this,
                Observer<List<Contact>> { t ->
                    if (t?.isNotEmpty() == true) {
                        adapter.contactList = t
                        tv_empty.visibility = View.GONE
                    } else {
                        tv_empty.visibility = View.VISIBLE
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_filter -> {
                startActivityForResult(Intent(this, FilterActivity::class.java), FILTER_REQUEST)
                true
            }
            R.id.menu_logout -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInClient.signOut()
                .addOnCompleteListener(this) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
    }

    override fun onBackPressed() {

    }
}
