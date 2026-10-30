package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.ToolId
import com.example.ui.components.getToolIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolDetailScreen(
  tool: ToolId,
  currentLanguage: AppLanguage,
  isFavorite: Boolean,
  onToggleFavorite: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val title = when (currentLanguage) {
    AppLanguage.URDU -> tool.urduTitle
    AppLanguage.ARABIC -> tool.arabicTitle
    else -> tool.title
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = getToolIcon(tool),
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column {
              Text(title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
              Text(tool.category.title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("tool_detail_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(
            onClick = onToggleFavorite,
            modifier = Modifier.testTag("tool_detail_favorite_button")
          ) {
            Icon(
              imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Toggle Favorite",
              tint = if (isFavorite) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    }
  ) { innerPadding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
    ) {
      // Header Description Banner
      Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = tool.shortDesc,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
          )
        }
      }

      // Actual Working Tool Component
      when (tool) {
        ToolId.TEXT_CLEANER -> TextCleanerTool()
        ToolId.WORD_COUNTER -> WordCounterTool()
        ToolId.CASE_CONVERTER -> CaseConverterTool()
        ToolId.TEXT_DIFF -> TextDiffCheckerTool()
        ToolId.IMAGE_RESIZER -> ImageResizerTool()
        ToolId.IMAGE_COMPRESSOR -> ImageCompressorTool()
        ToolId.IMAGE_FORMAT_CONVERTER -> ImageFormatConverterTool()
        ToolId.IMAGE_TO_TEXT -> ImageToTextOcrTool()
        ToolId.PDF_MERGER -> PdfMergerTool()
        ToolId.PDF_SPLITTER -> PdfSplitterTool()
        ToolId.PDF_COMPRESSOR -> PdfCompressorTool()
        ToolId.DOCUMENT_HELPER -> DocumentHelperTool()
        ToolId.LANGUAGE_HELPER -> LanguageHelperTool()
        ToolId.EASY_READER -> EasyReaderTool()
        ToolId.TEXT_SUMMARIZER -> TextSummarizerTool()
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}
