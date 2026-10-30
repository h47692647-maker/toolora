package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.FavoriteToolEntity
import com.example.data.model.RecentToolEntity

@Database(
  entities = [RecentToolEntity::class, FavoriteToolEntity::class],
  version = 1,
  exportSchema = false
)
abstract class ToolDatabase : RoomDatabase() {
  abstract fun toolDao(): ToolDao

  companion object {
    @Volatile
    private var INSTANCE: ToolDatabase? = null

    fun getDatabase(context: Context): ToolDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          ToolDatabase::class.java,
          "toolora_db"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
