package alex.balyas.mycontactstestapp.mvvm.newContact

import alex.balyas.mycontactstestapp.db.Contact
import alex.balyas.mycontactstestapp.db.ContactRepository
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Base64
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Alex Balyas on 07.02.2019.
 */
class NewContactViewModel(application: Application) : AndroidViewModel(application) {

    val createFlag = MutableLiveData<Boolean>()
    val editFlag = MutableLiveData<Boolean>()

    private val contactRepository: ContactRepository = ContactRepository(application)

    fun createContact(userID: String, name: String, surname: String, phones: List<String>, emails: List<String>) {
        if (name.isNotEmpty() && surname.isNotEmpty() && phones.isNotEmpty() && phones[0].isNotEmpty()
            && emails.isNotEmpty() && emails[0].isNotEmpty()
        ) {

            val hash = name + surname + userID

            val data = hash.toByteArray()
            val base64 = Base64.encodeToString(data, Base64.DEFAULT)

            val contact = Contact(base64, name, surname, emails, phones, userID)

            Completable.fromAction {
                contactRepository.insert(contact)
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    object : CompletableObserver {
                        override fun onComplete() {
                            createFlag.value = true
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onError(e: Throwable) {
                            createFlag.value = false
                        }
                    }
                )
        } else {
            createFlag.value = false
        }
    }

    fun updateContact(
        userID: String,
        id: String,
        name: String,
        surname: String,
        phones: List<String>,
        emails: List<String>
    ) {
        if (name.isNotEmpty() && surname.isNotEmpty() && phones.isNotEmpty() && phones[0].isNotEmpty()
            && emails.isNotEmpty() && emails[0].isNotEmpty()
        ) {

            val contact = Contact(id, name, surname, emails, phones, userID)

            Completable.fromAction {
                contactRepository.updateContact(contact)
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    object : CompletableObserver {
                        override fun onComplete() {
                            editFlag.value = true
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onError(e: Throwable) {
                            editFlag.value = false
                        }
                    }
                )
        } else {
            createFlag.value = false
        }
    }
}