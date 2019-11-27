package com.vitovalov.exchangerateslive.data.local

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vitovalov.exchangerateslive.data.local.model.UserChangesEntity
import io.reactivex.schedulers.Schedulers

@Database(
        entities = [UserChangesEntity::class],
        version = 1,
        exportSchema = false
)
abstract class RoomUserChangesDb : RoomDatabase() {

    abstract fun userChangesDao(): UserChangesDao

    companion object {

        private const val DB_NAME = "user_changes_db"

        @Volatile
        private var INSTANCE: RoomUserChangesDb? = null

        fun getInstance(context: Context): RoomUserChangesDb =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: create(context).also { INSTANCE = it }
                }

        fun create(context: Context): RoomUserChangesDb {
            return Room.databaseBuilder(
                    context,
                    RoomUserChangesDb::class.java,
                    DB_NAME)
                    .addCallback(object : Callback() {
                        @SuppressLint("CheckResult")
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE
                                    ?.userChangesDao()
                                    ?.insert(UserChangesEntity(0, "EUR", "100.0"))
                                    ?.subscribeOn(Schedulers.io())
                                    ?.subscribe()
                        }
                    })
                    .build()
        }
    }
}
