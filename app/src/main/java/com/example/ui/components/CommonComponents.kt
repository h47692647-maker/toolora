package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MergeType
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.ToolCategory
import com.example.data.model.ToolId
import com.example.ui.theme.TooloraPrimary
import com.example.ui.theme.TooloraSecondary
import com.example.ui.theme.TooloraTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooloraTopBar(
  currentLanguage: AppLanguage,
  isDarkTheme: Boolean,
  onLanguageSelected: (AppLanguage) -> Unit,
  onToggleTheme: () -> Unit,
  onOpenChat: () -> Unit,
  onOpenDrawer: () -> Unit,
  modifier: Modifier = Modifier
) {
  var langMenuExpanded by remember { mutableStateOf(false) }

  TopAppBar(
    modifier = modifier,
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface,
      titleContentColor = MaterialTheme.colorScheme.onSurface
    ),
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
              Brush.linearGradient(
                listOf(TooloraPrimary, TooloraSecondary)
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "T",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp
          )
        }
        Column {
          Text(
            text = "TOOLORA",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.primary
          )
          Text(
            text = when (currentLanguage) {
              AppLanguage.URDU -> "آپ کے تمام اوزار ایک جگہ"
              AppLanguage.ARABIC -> "أدواتك اليومية في مكان واحد"
              else -> "All in One Tools"
            },
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    },
    navigationIcon = {
      IconButton(
        onClick = onOpenDrawer,
        modifier = Modifier.testTag("drawer_menu_button")
      ) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = "Open Navigation Menu"
        )
      }
    },
    actions = {
      // Chatbot Quick Access Button
      IconButton(
        onClick = onOpenChat,
        modifier = Modifier.testTag("topbar_chat_button")
      ) {
        Icon(
          imageVector = Icons.Default.ChatBubbleOutline,
          contentDescription = "Toolora AI Assistant",
          tint = MaterialTheme.colorScheme.primary
        )
      }

      // Language Switcher
      Box {
        IconButton(
          onClick = { langMenuExpanded = true },
          modifier = Modifier.testTag("language_switch_button")
        ) {
          Icon(
            imageVector = Icons.Default.Language,
            contentDescription = "Change Language"
          )
        }
        DropdownMenu(
          expanded = langMenuExpanded,
          onDismissRequest = { langMenuExpanded = false }
        ) {
          AppLanguage.values().forEach { lang ->
            DropdownMenuItem(
              text = {
                Row(
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(lang.nativeName, fontWeight = if (lang == currentLanguage) FontWeight.Bold else FontWeight.Normal)
                  if (lang == currentLanguage) {
                    Text("✓", color = MaterialTheme.colorScheme.primary)
                  }
                }
              },
              onClick = {
                onLanguageSelected(lang)
                langMenuExpanded = false
              }
            )
          }
        }
      }

      // Dark / Light Toggle
      IconButton(
        onClick = onToggleTheme,
        modifier = Modifier.testTag("theme_toggle_button")
      ) {
        Icon(
          imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
          contentDescription = if (isDarkTheme) "Switch to Light Mode" else "Switch to Dark Mode"
        )
      }
    }
  )
}

fun getToolIcon(toolId: ToolId): ImageVector {
  return when (toolId) {
    ToolId.TEXT_CLEANER -> Icons.Default.CleaningServices
    ToolId.WORD_COUNTER -> Icons.Default.Spellcheck
    ToolId.CASE_CONVERTER -> Icons.Default.TextFields
    ToolId.TEXT_DIFF -> Icons.Default.Compare
    ToolId.IMAGE_RESIZER -> Icons.Default.Crop
    ToolId.IMAGE_COMPRESSOR -> Icons.Default.Compress
    ToolId.IMAGE_FORMAT_CONVERTER -> Icons.Default.Transform
    ToolId.IMAGE_TO_TEXT -> Icons.Default.Image
    ToolId.PDF_MERGER -> Icons.Default.MergeType
    ToolId.PDF_SPLITTER -> Icons.Default.PictureAsPdf
    ToolId.PDF_COMPRESSOR -> Icons.Default.Compress
    ToolId.DOCUMENT_HELPER -> Icons.Default.Description
    ToolId.LANGUAGE_HELPER -> Icons.Default.Translate
    ToolId.EASY_READER -> Icons.Default.MenuBook
    ToolId.TEXT_SUMMARIZER -> Icons.Default.Summarize
  }
}

@Composable
fun ToolCard(
  tool: ToolId,
  currentLanguage: AppLanguage,
  isFavorite: Boolean,
  onToolClick: () -> Unit,
  onToggleFavorite: () -> Unit,
  modifier: Modifier = Modifier
) {
  val title = when (currentLanguage) {
    AppLanguage.URDU -> tool.urduTitle
    AppLanguage.ARABIC -> tool.arabicTitle
    else -> tool.title
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onToolClick)
      .testTag("tool_card_${tool.name.lowercase()}"),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
              when (tool.category) {
                ToolCategory.TEXT -> Color(0xFF0EA5E9).copy(alpha = 0.15f)
                ToolCategory.IMAGE -> Color(0xFF8B5CF6).copy(alpha = 0.15f)
                ToolCategory.PDF -> Color(0xFFF59E0B).copy(alpha = 0.15f)
                ToolCategory.LANGUAGE -> Color(0xFF10B981).copy(alpha = 0.15f)
                else -> MaterialTheme.colorScheme.primaryContainer
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = getToolIcon(tool),
            contentDescription = tool.title,
            tint = when (tool.category) {
              ToolCategory.TEXT -> TooloraPrimary
              ToolCategory.IMAGE -> TooloraSecondary
              ToolCategory.PDF -> Color(0xFFD97706)
              ToolCategory.LANGUAGE -> TooloraTertiary
              else -> MaterialTheme.colorScheme.primary
            },
            modifier = Modifier.size(24.dp)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          if (tool.isPopular) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = TooloraSecondary.copy(alpha = 0.15f),
              modifier = Modifier.padding(end = 4.dp)
            ) {
              Text(
                text = "POPULAR",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TooloraSecondary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          IconButton(
            onClick = onToggleFavorite,
            modifier = Modifier
              .size(36.dp)
              .testTag("fav_button_${tool.name.lowercase()}")
          ) {
            Icon(
              imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
              tint = if (isFavorite) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = tool.shortDesc,
        fontSize = 13.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = tool.category.title,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }

        Text(
          text = "Open Tool →",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.primary
        )
      }
    }
  }
}

@Composable
fun AdBannerPlaceholder(
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        shape = RoundedCornerShape(12.dp)
      ),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        ) {
          Text(
            text = "SPONSORED",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
          )
        }
        Text(
          text = "Reserved for Toolora Partner / Ad Slot",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Text(
        text = "Advertise",
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
      )
    }
  }
}
