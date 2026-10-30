package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.ToolCategory
import com.example.data.model.ToolId
import com.example.ui.components.AdBannerPlaceholder
import com.example.ui.components.ScreenDestination
import com.example.ui.components.ToolCard
import com.example.ui.components.getToolIcon
import com.example.ui.theme.TooloraPrimary
import com.example.ui.theme.TooloraSecondary
import com.example.ui.theme.TooloraTertiary

@Composable
fun HomeScreen(
  currentLanguage: AppLanguage,
  favoriteTools: Set<ToolId>,
  recentTools: List<ToolId>,
  onSelectTool: (ToolId) -> Unit,
  onToggleFavorite: (ToolId) -> Unit,
  onNavigate: (ScreenDestination) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(ToolCategory.ALL) }

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

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Hero Banner
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.linearGradient(
              listOf(
                TooloraPrimary.copy(alpha = 0.12f),
                TooloraSecondary.copy(alpha = 0.08f)
              )
            )
          )
          .padding(20.dp)
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = TooloraPrimary.copy(alpha = 0.2f)
          ) {
            Text(
              text = "GLOBAL UTILITY HUB",
              fontSize = 10.sp,
              fontWeight = FontWeight.Black,
              color = TooloraPrimary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              letterSpacing = 1.sp
            )
          }

          Text(
            text = when (currentLanguage) {
              AppLanguage.URDU -> "ٹولورا: تمام روزمرہ کے اوزار ایک ہی جگہ"
              AppLanguage.ARABIC -> "تولورا: جميع أدواتك اليومية في مكان واحد"
              else -> "All Your Everyday Tools in One Place."
            },
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          Text(
            text = "Clean text, edit images, manage PDFs, and translate languages fast, lightweight, and 100% on-device.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(4.dp))

          // Search Field inside Hero
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search 15 tools by name or purpose...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TooloraPrimary) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("home_search_bar"),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
          )
        }
      }
    }

    // Category Tabs Row
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(ToolCategory.values()) { cat ->
        FilterChip(
          selected = selectedCategory == cat,
          onClick = { selectedCategory = cat },
          label = { Text(cat.title, fontSize = 12.sp, fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = TooloraPrimary,
            selectedLabelColor = Color.White
          ),
          shape = RoundedCornerShape(20.dp)
        )
      }
    }

    // Ad Banner / Sponsorship Slot
    AdBannerPlaceholder()

    // Favorites Section (if any)
    if (favoriteTools.isNotEmpty() && searchQuery.isBlank()) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
            Text("Favorite Tools", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          }
          Text("${favoriteTools.size} saved", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(favoriteTools.toList()) { tool ->
            Box(modifier = Modifier.width(260.dp)) {
              ToolCard(
                tool = tool,
                currentLanguage = currentLanguage,
                isFavorite = true,
                onToolClick = { onSelectTool(tool) },
                onToggleFavorite = { onToggleFavorite(tool) }
              )
            }
          }
        }
      }
    }

    // Recently Used Section
    if (recentTools.isNotEmpty() && searchQuery.isBlank()) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.History, contentDescription = null, tint = TooloraPrimary, modifier = Modifier.size(20.dp))
            Text("Recently Used", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          items(recentTools.take(4)) { tool ->
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.surface,
              modifier = Modifier
                .clickable { onSelectTool(tool) }
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(getToolIcon(tool), contentDescription = null, tint = TooloraPrimary, modifier = Modifier.size(18.dp))
                Text(tool.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
              }
            }
          }
        }
      }
    }

    // Popular Tools Highlight (if no search)
    if (searchQuery.isBlank() && selectedCategory == ToolCategory.ALL) {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Popular & Essential Tools", fontWeight = FontWeight.Bold, fontSize = 18.sp)
          Text(
            text = "View All (${allTools.size}) →",
            color = TooloraPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onNavigate(ScreenDestination.AllTools) }
          )
        }

        val popular = allTools.filter { it.isPopular }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          popular.forEach { tool ->
            ToolCard(
              tool = tool,
              currentLanguage = currentLanguage,
              isFavorite = favoriteTools.contains(tool),
              onToolClick = { onSelectTool(tool) },
              onToggleFavorite = { onToggleFavorite(tool) }
            )
          }
        }
      }
    } else {
      // Filtered Grid / List
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
          text = "Matching Tools (${filteredTools.size})",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )

        filteredTools.forEach { tool ->
          ToolCard(
            tool = tool,
            currentLanguage = currentLanguage,
            isFavorite = favoriteTools.contains(tool),
            onToolClick = { onSelectTool(tool) },
            onToggleFavorite = { onToggleFavorite(tool) }
          )
        }
      }
    }

    // FAQ Section
    FaqSection()

    // Future Business / Monetization Preview Card
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Toolora Free Edition", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = TooloraTertiary.copy(alpha = 0.2f)
          ) {
            Text(
              "ACTIVE",
              color = TooloraTertiary,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
        Text(
          "All 15 tools are free for daily use. Upcoming Pro subscription will support automated batch directory processing and team workspaces.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Footer Links
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Text("About Us", fontSize = 12.sp, color = TooloraPrimary, modifier = Modifier.clickable { onNavigate(ScreenDestination.AboutUs) })
        Text("•", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Contact", fontSize = 12.sp, color = TooloraPrimary, modifier = Modifier.clickable { onNavigate(ScreenDestination.ContactUs) })
        Text("•", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Privacy", fontSize = 12.sp, color = TooloraPrimary, modifier = Modifier.clickable { onNavigate(ScreenDestination.PrivacyPolicy) })
        Text("•", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Terms", fontSize = 12.sp, color = TooloraPrimary, modifier = Modifier.clickable { onNavigate(ScreenDestination.TermsOfUse) })
      }
      Text(
        "© 2026 TOOLORA. All rights reserved.",
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
private fun FaqSection() {
  var expandedIndex by remember { mutableStateOf<Int?>(null) }

  val faqs = listOf(
    "Are my files and documents private?" to "Yes. All basic tools in Toolora (image resizing, compression, PDF splitting, text cleaning) run 100% locally on your device runtime. Your files are never uploaded to our servers.",
    "Is Toolora completely free?" to "Yes, all 15 core tools are accessible with no mandatory payment or hidden subscriptions.",
    "Can I use Toolora offline?" to "Yes! Because the tools process data on-device, text tools, image resizers, and PDF operations operate seamlessly without an internet connection."
  )

  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Text("Frequently Asked Questions", fontWeight = FontWeight.Bold, fontSize = 16.sp)

    faqs.forEachIndexed { index, (q, a) ->
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { expandedIndex = if (expandedIndex == index) null else index }
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(q, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
            Icon(
              imageVector = if (expandedIndex == index) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = null
            )
          }

          AnimatedVisibility(visible = expandedIndex == index) {
            Column {
              Spacer(modifier = Modifier.height(8.dp))
              Text(a, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
            }
          }
        }
      }
    }
  }
}
