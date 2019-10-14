package com.example.showimage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.showimage.database.dao.ImageDAO
import com.example.showimage.database.dao.ImageMarkerDAO
import com.example.showimage.database.dao.MarkerDAO
import com.example.showimage.database.model.ImageModel
import com.example.showimage.database.model.ImageMarkerModel
import com.example.showimage.database.model.MarkerDBModel
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = arrayOf(MarkerDBModel::class, ImageModel::class, ImageMarkerModel::class),
    version = 1,
    exportSchema = false
)
abstract class RoomDB : RoomDatabase() {
    abstract fun ImageDAO(): ImageDAO
    abstract fun MarkerDAO(): MarkerDAO
    abstract fun ImageMarkerDAO(): ImageMarkerDAO

    companion object {
        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): RoomDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "mapdb"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}