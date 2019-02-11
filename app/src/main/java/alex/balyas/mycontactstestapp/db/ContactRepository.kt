package alex.balyas.mycontactstestapp.db

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

/**
 * Created by Alex Balyas on 07.02.2019.
 */
class ContactRepository(application: Application) {
    private val contactDAO: ContactDAO
    private lateinit var listLiveData: LiveData<List<Contact>>

    init {
        val db = AppDatabase.getDatabase(application)
        contactDAO = db.contactDao()
    }

    fun getAllContacts(userID: String): LiveData<List<Contact>> {
        listLiveData = contactDAO.getAllContacts(userID)
        return listLiveData
    }

    fun getContactsSortedByNameAsc(userID: String): LiveData<List<Contact>> {
        listLiveData = contactDAO.getAllContactsSortingNameAsc(userID)
        return listLiveData
    }

    fun getContactsSortedByNameDesc(userID: String): LiveData<List<Contact>> {
        listLiveData = contactDAO.getAllContactsSortingNameDesc(userID)
        return listLiveData
    }

    fun getContactsSortedBySurnameAsc(userID: String): LiveData<List<Contact>> {
        listLiveData = contactDAO.getAllContactsSortingSurnameAsc(userID)
        return listLiveData
    }

    fun getContactsSortedBySurnameDesc(userID: String): LiveData<List<Contact>> {
        listLiveData = contactDAO.getAllContactsSortingSurnameDesc(userID)
        return listLiveData
    }

    fun insert(contact: Contact) {
        InsertAsyncTask(contactDAO).execute(contact)
    }

    fun updateContact(contact: Contact) {
        UpdateAsyncTask(contactDAO).execute(contact)
    }

    fun deleteContact(contact: Contact) {
        DeleteAsyncTask(contactDAO).execute(contact)
    }

    private class InsertAsyncTask internal constructor(private val asyncTaskDao: ContactDAO) :
        AsyncTask<Contact, Void, Void>() {

        override fun doInBackground(vararg params: Contact): Void? {
            try {
                asyncTaskDao.insertContact(params[0])
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

    private class DeleteAsyncTask internal constructor(private val asyncTaskDao: ContactDAO) :
        AsyncTask<Contact, Void, Void>() {

        override fun doInBackground(vararg params: Contact): Void? {
            try {
                asyncTaskDao.deleteContact(params[0])
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

    private class UpdateAsyncTask internal constructor(private val asyncTaskDao: ContactDAO) :
        AsyncTask<Contact, Void, Void>() {

        override fun doInBackground(vararg params: Contact): Void? {
            try {
                asyncTaskDao.editContact(params[0])
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}