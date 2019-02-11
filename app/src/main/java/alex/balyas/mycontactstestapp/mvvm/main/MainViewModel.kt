package alex.balyas.mycontactstestapp.mvvm.main

import alex.balyas.mycontactstestapp.db.Contact
import alex.balyas.mycontactstestapp.db.ContactRepository
import alex.balyas.mycontactstestapp.mvvm.filter.FilterSingleton
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

/**
 * Created by Alex Balyas on 09.02.2019.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val contactRepository: ContactRepository = ContactRepository(application)

    fun getContacts(userID: String): LiveData<List<Contact>> {
        val position = FilterSingleton.filterPosition
        return when (position) {
            0 -> contactRepository.getAllContacts(userID)
            1 -> contactRepository.getContactsSortedByNameAsc(userID)
            2 -> contactRepository.getContactsSortedByNameDesc(userID)
            3 -> contactRepository.getContactsSortedBySurnameAsc(userID)
            4 -> contactRepository.getContactsSortedBySurnameDesc(userID)
            else -> contactRepository.getAllContacts(userID)
        }

    }
}