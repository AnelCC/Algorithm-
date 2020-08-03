package com.anelcc.simplerxjava.modellayer.persistencelayer

import androidx.room.*
import com.anelcc.simplerxjava.LearningRxJavaApplication
import com.anelcc.simplerxjava.common.disposedBy
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


typealias PhotoDescriptionLambda = (List<PhotoDescription>) -> Unit

class PersistenceLayer {

    companion object {
        val shared = PersistenceLayer()
    }

    val databaseReady = BehaviorRelay.createDefault(false)
    val bag = CompositeDisposable()

    fun prepareDb(photoDescriptions: List<PhotoDescription>) {

        CoroutineScope(Dispatchers.IO).launch {
            val database = LearningRxJavaApplication.database

//            database.openHelper.writableDatabase.execSQL("DELETE from photo_descriptions")
            database.photoDescriptionDao().nukeTable() //reset for examples
            database.photoDescriptionDao().insertPhotoDescriptions(*photoDescriptions.toTypedArray())

            databaseReady.accept(true)

            getDescriptions { photoDescriptions ->
                println("photoDescriptions:")
                println(photoDescriptions.count())
            }
        }
    }

    fun loadAllPhotoDescriptions(finished: PhotoDescriptionLambda) { //async api

        databaseReady.subscribe(onNext@ { isReady ->
            if (!isReady) return@onNext

            getDescriptionsWithCoroutines(finished)
        }).disposedBy(bag)
    }

    private fun getDescriptionsWithCoroutines(finished: PhotoDescriptionLambda) {
        //assume a large data set in backend
        CoroutineScope(Dispatchers.IO).launch {
            LearningRxJavaApplication.database.photoDescriptionDao()
                    .getDescriptions()
                    .subscribe { list ->
                        CoroutineScope(Dispatchers.IO).launch {
                            finished(list)
                        }
                    }
        }
    }

    private fun getDescriptions(finished: PhotoDescriptionLambda) {
        LearningRxJavaApplication.database.photoDescriptionDao()
                .getDescriptions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(finished)
    }
}

@Database(entities = arrayOf(PhotoDescription::class), version = 1, exportSchema = false)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun photoDescriptionDao(): PhotoDescriptionDao
}

@Entity(tableName = "photo_descriptions")
data class PhotoDescription(
        @PrimaryKey(autoGenerate = true)
        var uid: Long = 0,
        var albumId: Int?,
        var id: Int?,
        var title: String?,
        var url: String?,
        var thumbnailUrl: String?
)

@Dao
interface PhotoDescriptionDao {
    @Query("SELECT * FROM photo_descriptions LIMIT 100")
    fun getDescriptions(): Flowable<List<PhotoDescription>>

    @Insert
    fun insert(photoDescription: PhotoDescription)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotoDescriptions(vararg photoDescription: PhotoDescription)

    @Query("DELETE FROM photo_descriptions")
    fun nukeTable()
}
