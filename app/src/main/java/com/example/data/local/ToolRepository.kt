package com.example.data.local

import com.example.data.model.FavoriteToolEntity
import com.example.data.model.RecentToolEntity
import com.example.data.model.ToolId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ToolRepository(private val dao: ToolDao) {

  val recentTools: Flow<List<ToolId>> = dao.getRecentTools().map { list ->
    list.mapNotNull { entity ->
      try { ToolId.valueOf(entity.toolId) } catch (e: Exception) { null }
    }
  }

  val favoriteTools: Flow<Set<ToolId>> = dao.getFavoriteTools().map { list ->
    list.mapNotNull { entity ->
      try { ToolId.valueOf(entity.toolId) } catch (e: Exception) { null }
    }.toSet()
  }

  suspend fun recordToolUsage(tool: ToolId) {
    dao.insertRecentTool(RecentToolEntity(toolId = tool.name, timestamp = System.currentTimeMillis()))
  }

  suspend fun toggleFavorite(tool: ToolId, isCurrentlyFav: Boolean) {
    if (isCurrentlyFav) {
      dao.removeFavoriteTool(tool.name)
    } else {
      dao.insertFavoriteTool(FavoriteToolEntity(toolId = tool.name, addedAt = System.currentTimeMillis()))
    }
  }
}
