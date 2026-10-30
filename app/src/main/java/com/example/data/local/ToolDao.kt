package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.FavoriteToolEntity
import com.example.data.model.RecentToolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {
  @Query("SELECT * FROM recent_tools ORDER BY timestamp DESC LIMIT 10")
  fun getRecentTools(): Flow<List<RecentToolEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRecentTool(entity: RecentToolEntity)

  @Query("DELETE FROM recent_tools WHERE toolId = :toolId")
  suspend fun removeRecentTool(toolId: String)

  @Query("SELECT * FROM favorite_tools ORDER BY addedAt DESC")
  fun getFavoriteTools(): Flow<List<FavoriteToolEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavoriteTool(entity: FavoriteToolEntity)

  @Query("DELETE FROM favorite_tools WHERE toolId = :toolId")
  suspend fun removeFavoriteTool(toolId: String)

  @Query("SELECT EXISTS(SELECT 1 FROM favorite_tools WHERE toolId = :toolId)")
  fun isFavorite(toolId: String): Flow<Boolean>
}
