package alex.balyas.mycontactstestapp.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

/**
 * Created by Alex Balyas on 06.02.2019.
 */
@Dao
interface ContactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: Contact)

    @Update
    fun editContact(contact: Contact)

    @Delete
    fun deleteContact(contact: Contact)

    @Query("SELECT * FROM contact where userHolder = :userId")
    fun getAllContacts(userId: String): LiveData<List<Contact>>

    @Query("SELECT * FROM contact where userHolder = :userId ORDER BY firstName ASC")
    fun getAllContactsSortingNameAsc(userId: String): LiveData<List<Contact>>

    @Query("SELECT * FROM contact where userHolder = :userId ORDER BY firstName DESC")
    fun getAllContactsSortingNameDesc(userId: String): LiveData<List<Contact>>

    @Query("SELECT * FROM contact where userHolder = :userId ORDER BY secondName ASC")
    fun getAllContactsSortingSurnameAsc(userId: String): LiveData<List<Contact>>

    @Query("SELECT * FROM contact where userHolder = :userId ORDER BY secondName DESC")
    fun getAllContactsSortingSurnameDesc(userId: String): LiveData<List<Contact>>
}