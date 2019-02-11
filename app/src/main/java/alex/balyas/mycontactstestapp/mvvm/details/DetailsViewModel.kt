package alex.balyas.mycontactstestapp.mvvm.details

import alex.balyas.mycontactstestapp.db.Contact
import alex.balyas.mycontactstestapp.db.ContactRepository
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Alex Balyas on 09.02.2019.
 */
class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    var deleteFlag = MutableLiveData<Boolean>()

    private val contactRepository: ContactRepository = ContactRepository(application)

    fun deleteContact(contact: Contact) {
        Completable.fromAction {
            contactRepository.deleteContact(contact)
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                object : CompletableObserver {
                    override fun onComplete() {
                        deleteFlag.value = true
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        deleteFlag.value = false
                    }
                }
            )
    }
}

