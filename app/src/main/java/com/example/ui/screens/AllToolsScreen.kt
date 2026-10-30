package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.ToolCategory
import com.example.data.model.ToolId
import com.example.ui.components.ToolCard
import com.example.ui.theme.TooloraPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllToolsScreen(
  initialCategory: ToolCategory = ToolCategory.ALL,
  currentLanguage: AppLanguage,
  favoriteTools: Set<ToolId>,
  onSelectTool: (ToolId) -> Unit,
  onToggleFavorite: (ToolId) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(initialCategory) }

  val allTools = remember { ToolId.values().toList() }

  val filteredTools = remember(searchQuery, selectedCategory) {
    allTools.filter { tool ->
      val matchesCategory = (selectedCategory == ToolCategory.ALL) || (tool.category == selectedCategory)
      val matchesSearch = searchQuery.isBlank() ||
        tool.title.contains(searchQuery, ignoreCase = true) ||
        tool.urduTitle.contains(searchQuery) ||
        tool.arabicTitle.contains(searchQuery) ||
        tool.shortDesc.contains(searchQuery, ignoreCase = true)
      matchesCategory && matchesSearch
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            if (initialCategory == ToolCategory.ALL) "All Tools Catalog" else initialCategory.title,
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("all_tools_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    }
  ) { innerPadding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Spacer(modifier = Modifier.height(2.dp))

      // Search Bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Filter tools by name...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TooloraPrimary) },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("all_tools_search"),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
      )

      // Category Chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(ToolCategory.values()) { cat ->
          FilterChip(
            selected = selectedCategory == cat,
            onClick = { selectedCategory = cat },
            label = { Text(cat.title, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = TooloraPrimary,
              selectedLabelColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp)
          )
        }
      }

      Text(
        text = "Showing ${filteredTools.size} Tools",
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(filteredTools) { tool ->
          ToolCard(
            tool = tool,
            currentLanguage = currentLanguage,
            isFavorite = favoriteTools.contains(tool),
            onToolClick = { onSelectTool(tool) },
            onToggleFavorite = { onToggleFavorite(tool) }
          )
        }
        item {
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}
