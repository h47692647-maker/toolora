package com.example.ui.screens

import android.content.Context
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.intelligence.IntelligenceEngine
import com.example.ui.theme.TooloraPrimary
import com.example.ui.theme.TooloraSecondary
import com.example.ui.theme.TooloraTertiary
import kotlinx.coroutines.launch
import java.util.Locale

// -------------------------------------------------------------
// TOOL 13: LANGUAGE HELPER
// -------------------------------------------------------------
@Composable
fun LanguageHelperTool(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  var sourceText by remember {
    mutableStateOf("Welcome to Toolora. All your everyday tools in one place.")
  }
  var translatedText by remember { mutableStateOf("") }
  var sourceLang by remember { mutableStateOf("English") }
  var targetLang by remember { mutableStateOf("Urdu") }
  var isTranslating by remember { mutableStateOf(false) }

  var tts by remember { mutableStateOf<TextToSpeech?>(null) }

  DisposableEffect(Unit) {
    var speechInstance: TextToSpeech? = null
    speechInstance = TextToSpeech(context) { status ->
      if (status == TextToSpeech.SUCCESS) {
        speechInstance?.language = Locale.US
      }
    }
    tts = speechInstance
    onDispose {
      speechInstance?.stop()
      speechInstance?.shutdown()
    }
  }

  val languages = listOf("English", "Urdu", "Arabic", "Spanish", "French", "German", "Chinese", "Japanese", "Hindi")

  var sourceMenuExpanded by remember { mutableStateOf(false) }
  var targetMenuExpanded by remember { mutableStateOf(false) }

  fun executeTranslate() {
    isTranslating = true
    coroutineScope.launch {
      val res = IntelligenceEngine.translate(sourceText, sourceLang, targetLang)
      translatedText = res
      isTranslating = false
    }
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text("Instructions:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(
          "Translate text across international languages. Select your source and target languages, input your text, and tap 'Translate'. Use the audio button to hear pronunciation.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Language Selector Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Box(modifier = Modifier.weight(1f)) {
        OutlinedButton(
          onClick = { sourceMenuExpanded = true },
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(sourceLang, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
        }
        DropdownMenu(expanded = sourceMenuExpanded, onDismissRequest = { sourceMenuExpanded = false }) {
          languages.forEach { l ->
            DropdownMenuItem(text = { Text(l) }, onClick = { sourceLang = l; sourceMenuExpanded = false })
          }
        }
      }

      IconButton(
        onClick = {
          val temp = sourceLang
          sourceLang = targetLang
          targetLang = temp
          val tempText = sourceText
          sourceText = translatedText
          translatedText = tempText
        },
        modifier = Modifier.padding(horizontal = 4.dp)
      ) {
        Icon(Icons.Default.SwapHoriz, contentDescription = "Swap Languages", tint = TooloraPrimary)
      }

      Box(modifier = Modifier.weight(1f)) {
        OutlinedButton(
          onClick = { targetMenuExpanded = true },
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(targetLang, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
        }
        DropdownMenu(expanded = targetMenuExpanded, onDismissRequest = { targetMenuExpanded = false }) {
          languages.forEach { l ->
            DropdownMenuItem(text = { Text(l) }, onClick = { targetLang = l; targetMenuExpanded = false })
          }
        }
      }
    }

    OutlinedTextField(
      value = sourceText,
      onValueChange = { sourceText = it },
      label = { Text("Source Text ($sourceLang)") },
      modifier = Modifier
        .fillMaxWidth()
        .height(130.dp)
        .testTag("translate_input_text"),
      shape = RoundedCornerShape(12.dp)
    )

    Button(
      onClick = { executeTranslate() },
      enabled = !isTranslating,
      colors = ButtonDefaults.buttonColors(containerColor = TooloraPrimary),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("execute_translate_button")
    ) {
      if (isTranslating) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
      } else {
        Icon(Icons.Default.Translate, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Translate Now")
      }
    }

    if (translatedText.isNotEmpty()) {
      Text("Translation Result ($targetLang)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
      OutlinedTextField(
        value = translatedText,
        onValueChange = { translatedText = it },
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .testTag("translate_output_text"),
        shape = RoundedCornerShape(12.dp)
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = {
            tts?.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, "tts_id")
          },
          colors = ButtonDefaults.buttonColors(containerColor = TooloraSecondary),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.VolumeUp, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Listen")
        }

        Button(
          onClick = { copyToClipboard(context, translatedText, "Translation") },
          colors = ButtonDefaults.buttonColors(containerColor = TooloraTertiary),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Copy")
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TOOL 14: EASY READER
// -------------------------------------------------------------
@Composable
fun EasyReaderTool(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  var rawText by remember {
    mutableStateOf(
      "In order to facilitate the optimal implementation of our paradigm, users should endeavor to utilize simplified vocabulary and eliminate subsequent obfuscations."
    )
  }
  var simplifiedResult by remember { mutableStateOf("") }
  var readabilityScore by remember { mutableDoubleStateOf(0.0) }

  fun executeSimplify() {
    val (result, score) = IntelligenceEngine.simplifyText(rawText)
    simplifiedResult = result
    readabilityScore = score
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text("Instructions:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(
          "Paste complex, academic, legal, or jargon-heavy text. Easy Reader rewrites the prose into clear, everyday language and computes the Flesch reading score.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    OutlinedTextField(
      value = rawText,
      onValueChange = { rawText = it },
      label = { Text("Complex Text to Simplify") },
      modifier = Modifier
        .fillMaxWidth()
        .height(130.dp)
        .testTag("easy_reader_input"),
      shape = RoundedCornerShape(12.dp)
    )

    Button(
      onClick = { executeSimplify() },
      colors = ButtonDefaults.buttonColors(containerColor = TooloraPrimary),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("execute_easy_reader")
    ) {
      Icon(Icons.Default.MenuBook, contentDescription = null)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Rewrite in Plain Language")
    }

    if (simplifiedResult.isNotEmpty()) {
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = TooloraTertiary.copy(alpha = 0.15f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Readability Index Score:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
          Text(
            "${"%.1f".format(readabilityScore)} / 100",
            fontWeight = FontWeight.Black,
            fontSize = 16.sp,
            color = TooloraTertiary
          )
        }
      }

      OutlinedTextField(
        value = simplifiedResult,
        onValueChange = { simplifiedResult = it },
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp),
        shape = RoundedCornerShape(12.dp)
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        Button(
          onClick = { copyToClipboard(context, simplifiedResult, "Easy Reader") },
          colors = ButtonDefaults.buttonColors(containerColor = TooloraSecondary)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Copy Simplified Text")
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TOOL 15: TEXT SUMMARIZER
// -------------------------------------------------------------
@Composable
fun TextSummarizerTool(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  var text by remember {
    mutableStateOf(
      "Toolora is an international digital utilities platform built to bring together essential everyday tools for modern digital workers. Traditionally, users are forced to visit dozens of ad-heavy websites for simple tasks like resizing an image, counting words, converting formats, or cleaning text. Toolora solves this fragmentation by offering a unified, privacy-focused portal. All core tools operate directly on the client side with zero cloud tracking. Future iterations will include automated team workflows and enterprise batch operations."
    )
  }
  var summaryMode by remember { mutableStateOf("bullets") }
  var summaryResult by remember { mutableStateOf("") }

  fun executeSummarize() {
    summaryResult = IntelligenceEngine.summarizeText(text, summaryMode)
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text("Instructions:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(
          "Paste long articles, essays, or documents. Choose your summary format (Key Bullets, TL;DR, or Executive Summary) and tap 'Generate Summary'.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    OutlinedTextField(
      value = text,
      onValueChange = { text = it },
      label = { Text("Full Article / Long Text") },
      modifier = Modifier
        .fillMaxWidth()
        .height(140.dp)
        .testTag("summarizer_input"),
      shape = RoundedCornerShape(12.dp)
    )

    Text("Summary Format", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      val modes = listOf("bullets" to "Key Bullets", "tldr" to "TL;DR", "executive" to "Executive Brief")
      modes.forEach { (key, label) ->
        Button(
          onClick = { summaryMode = key },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (summaryMode == key) TooloraPrimary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (summaryMode == key) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
          ),
          modifier = Modifier.weight(1f)
        ) {
          Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
      }
    }

    Button(
      onClick = { executeSummarize() },
      colors = ButtonDefaults.buttonColors(containerColor = TooloraSecondary),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("execute_summarize_button")
    ) {
      Icon(Icons.Default.Summarize, contentDescription = null)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Generate Instant Summary")
    }

    if (summaryResult.isNotEmpty()) {
      Text("Summary Output", fontWeight = FontWeight.Bold, fontSize = 14.sp)
      OutlinedTextField(
        value = summaryResult,
        onValueChange = { summaryResult = it },
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp),
        shape = RoundedCornerShape(12.dp)
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        Button(
          onClick = { copyToClipboard(context, summaryResult, "Toolora Summary") },
          colors = ButtonDefaults.buttonColors(containerColor = TooloraTertiary)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Copy Summary")
        }
      }
    }
  }
}
