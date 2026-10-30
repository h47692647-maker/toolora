package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TooloraPrimary
import com.example.ui.theme.TooloraSecondary
import com.example.ui.theme.TooloraTertiary

fun copyToClipboard(context: Context, text: String, label: String = "Toolora") {
  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  val clip = ClipData.newPlainText(label, text)
  clipboard.setPrimaryClip(clip)
  Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
}

// -------------------------------------------------------------
// TOOL 1: TEXT CLEANER
// -------------------------------------------------------------
@Composable
fun TextCleanerTool(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  var inputText by remember {
    mutableStateOf(
      "   This is a sample    text  with\n\n\nexcessive   line   breaks\nand    unwanted   spacing.  \t   Clean me!  "
    )
  }
  var outputText by remember { mutableStateOf("") }
  var trimLines by remember { mutableStateOf(true) }
  var removeBlankLines by remember { mutableStateOf(true) }
  var singleSpacesOnly by remember { mutableStateOf(true) }
  var stripTabs by remember { mutableStateOf(true) }
  var successMessage by remember { mutableStateOf<String?>(null) }

  fun executeClean() {
    var result = inputText
    if (stripTabs) {
      result = result.replace("\t", " ")
    }
    if (singleSpacesOnly) {
      result = result.replace(Regex(" +"), " ")
    }
    val lines = result.split("\n").map { if (trimLines) it.trim() else it }
    val filtered = if (removeBlankLines) lines.filter { it.isNotBlank() } else lines
    outputText = filtered.joinToString("\n").trim()
    val savedChars = inputText.length - outputText.length
    successMessage = "Cleaned! Reduced by $savedChars characters."
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Instructions Card
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text("Instructions:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(
          "Paste text with messy spaces, repeated tabs, or extra empty lines. Configure the cleanup toggles below and tap 'Clean Text'.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Input Area
    OutlinedTextField(
      value = inputText,
      onValueChange = { inputText = it; successMessage = null },
      label = { Text("Input Text") },
      placeholder = { Text("Paste your raw text here...") },
      modifier = Modifier
        .fillMaxWidth()
        .height(140.dp)
        .testTag("text_cleaner_input"),
      shape = RoundedCornerShape(12.dp)
    )

    // Cleaning Options
    Text("Cleanup Options", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = singleSpacesOnly, onCheckedChange = { singleSpacesOnly = it })
        Text("Normalize duplicate spaces into single spaces", fontSize = 13.sp)
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = removeBlankLines, onCheckedChange = { removeBlankLines = it })
        Text("Strip empty and whitespace-only lines", fontSize = 13.sp)
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = trimLines, onCheckedChange = { trimLines = it })
        Text("Trim leading and trailing line margins", fontSize = 13.sp)
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = stripTabs, onCheckedChange = { stripTabs = it })
        Text("Convert tabs to standard spaces", fontSize = 13.sp)
      }
    }

    // Actions
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Button(
        onClick = { executeClean() },
        modifier = Modifier
          .weight(1f)
          .testTag("clean_text_button"),
        colors = ButtonDefaults.buttonColors(containerColor = TooloraPrimary)
      ) {
        Icon(Icons.Default.CleaningServices, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Clean Text")
      }

      OutlinedButton(
        onClick = {
          inputText = ""
          outputText = ""
          successMessage = null
        },
        modifier = Modifier.testTag("clear_text_button")
      ) {
        Icon(Icons.Default.Clear, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text("Clear")
      }
    }

    if (successMessage != null) {
      Surface(
        color = TooloraTertiary.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = successMessage!!,
          color = TooloraTertiary,
          fontWeight = FontWeight.Medium,
          fontSize = 13.sp,
          modifier = Modifier.padding(10.dp)
        )
      }
    }

    // Output Area
    if (outputText.isNotEmpty()) {
      Text("Cleaned Output", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
      OutlinedTextField(
        value = outputText,
        onValueChange = { outputText = it },
        readOnly = false,
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp)
          .testTag("text_cleaner_output"),
        shape = RoundedCornerShape(12.dp)
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        Button(
          onClick = { copyToClipboard(context, outputText) },
          colors = ButtonDefaults.buttonColors(containerColor = TooloraSecondary),
          modifier = Modifier.testTag("copy_cleaned_button")
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Copy Cleaned Text")
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TOOL 2: WORD COUNTER
// -------------------------------------------------------------
@Composable
fun WordCounterTool(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  var text by remember {
    mutableStateOf(
      "Toolora is an all-in-one global online platform designed to empower creators, developers, students, and professionals with instant digital utility tools."
    )
  }

  val words by remember(text) {
    derivedStateOf {
      if (text.isBlank()) 0 else text.trim().split(Regex("\\s+")).filter { it.isNotBlank() }.size
    }
  }
  val charsWithSpaces by remember(text) { derivedStateOf { text.length } }
  val charsWithoutSpaces by remember(text) { derivedStateOf { text.replace(" ", "").replace("\n", "").length } }
  val sentences by remember(text) {
    derivedStateOf {
      if (text.isBlank()) 0 else text.split(Regex("(?<=[.!?])\\s+")).filter { it.isNotBlank() }.size
    }
  }
  val paragraphs by remember(text) {
    derivedStateOf {
      if (text.isBlank()) 0 else text.split(Regex("\n+")).filter { it.isNotBlank() }.size
    }
  }
  val readingMinutes by remember(words) {
    derivedStateOf {
      val min = (words / 200.0)
      if (min < 0.1) "< 10 sec" else "%.1f min".format(min)
    }
  }
  val speakingMinutes by remember(words) {
    derivedStateOf {
      val min = (words / 130.0)
      if (min < 0.1) "< 10 sec" else "%.1f min".format(min)
    }
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Instructions Card
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text("Instructions:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(
          "Type or paste your content in the editor below. Key metrics like word count, characters, sentences, and estimated speaking/reading times update instantly.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Live Metrics Grid
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      MetricBox(label = "Words", value = "$words", modifier = Modifier.weight(1f), color = TooloraPrimary)
      MetricBox(label = "Characters", value = "$charsWithSpaces", modifier = Modifier.weight(1f), color = TooloraSecondary)
      MetricBox(label = "No Spaces", value = "$charsWithoutSpaces", modifier = Modifier.weight(1f), color = TooloraTertiary)
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      MetricBox(label = "Sentences", value = "$sentences", modifier = Modifier.weight(1f))
      MetricBox(label = "Paragraphs", value = "$paragraphs", modifier = Modifier.weight(1f))
      MetricBox(label = "Reading Time", value = readingMinutes, modifier = Modifier.weight(1f))
    }

    // Text Editor
    OutlinedTextField(
      value = text,
      onValueChange = { text = it },
      label = { Text("Your Content") },
      placeholder = { Text("Start typing or paste text...") },
      modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
        .testTag("word_counter_editor"),
      shape = RoundedCornerShape(12.dp)
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      OutlinedButton(
        onClick = { text = "" },
        modifier = Modifier.weight(1f)
      ) {
        Icon(Icons.Default.Clear, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text("Clear Text")
      }

      Button(
        onClick = { copyToClipboard(context, text) },
        colors = ButtonDefaults.buttonColors(containerColor = TooloraPrimary),
        modifier = Modifier.weight(1f)
      ) {
        Icon(Icons.Default.ContentCopy, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text("Copy Text")
      }
    }
  }
}

@Composable
private fun MetricBox(
  label: String,
  value: String,
  modifier: Modifier = Modifier,
  color: Color = MaterialTheme.colorScheme.primary
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = value,
        fontWeight = FontWeight.Black,
        fontSize = 18.sp,
        color = color
      )
      Text(
        text = label,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

// -------------------------------------------------------------
// TOOL 3: CASE CONVERTER
// -------------------------------------------------------------
@Composable
fun CaseConverterTool(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  var text by remember { mutableStateOf("the quick BROWN fox jumps OVER the Lazy Dog") }
  var convertedText by remember { mutableStateOf("") }
  var currentFormat by remember { mutableStateOf("UPPERCASE") }

  fun convert(format: String) {
    currentFormat = format
    convertedText = when (format) {
      "UPPERCASE" -> text.uppercase()
      "lowercase" -> text.lowercase()
      "Title Case" -> {
        text.split(Regex("\\s+")).joinToString(" ") { word ->
          word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
      }
      "Sentence case" -> {
        val sentences = text.split(Regex("(?<=[.!?])\\s+"))
        sentences.joinToString(" ") { s ->
          s.trim().lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
      }
      "camelCase" -> {
        val words = text.split(Regex("[^a-zA-Z0-9]+")).filter { it.isNotBlank() }
        words.mapIndexed { index, w ->
          if (index == 0) w.lowercase() else w.lowercase().replaceFirstChar { it.titlecase() }
        }.joinToString("")
      }
      "snake_case" -> {
        text.split(Regex("[^a-zA-Z0-9]+")).filter { it.isNotBlank() }.joinToString("_") { it.lowercase() }
      }
      "CONSTANT_CASE" -> {
        text.split(Regex("[^a-zA-Z0-9]+")).filter { it.isNotBlank() }.joinToString("_") { it.uppercase() }
      }
      "kebab-case" -> {
        text.split(Regex("[^a-zA-Z0-9]+")).filter { it.isNotBlank() }.joinToString("-") { it.lowercase() }
      }
      "aLtErNaTiNg" -> {
        text.mapIndexed { index, c ->
          if (index % 2 == 0) c.lowercaseChar() else c.uppercaseChar()
        }.joinToString("")
      }
      else -> text
    }
  }

  // Initial trigger
  remember {
    convert("Title Case")
    true
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
          "Type or paste your text, then tap any of the formatting options to convert the casing in one click.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    OutlinedTextField(
      value = text,
      onValueChange = {
        text = it
        convert(currentFormat)
      },
      label = { Text("Input Text") },
      modifier = Modifier
        .fillMaxWidth()
        .height(110.dp)
        .testTag("case_converter_input"),
      shape = RoundedCornerShape(12.dp)
    )

    Text("Conversion Formats", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

    // Formats row/grid
    val formats = listOf(
      "UPPERCASE", "lowercase", "Title Case", "Sentence case",
      "camelCase", "snake_case", "CONSTANT_CASE", "kebab-case", "aLtErNaTiNg"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      formats.chunked(3).forEach { chunk ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          chunk.forEach { fmt ->
            Button(
              onClick = { convert(fmt) },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (currentFormat == fmt) TooloraPrimary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (currentFormat == fmt) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
              ),
              modifier = Modifier
                .weight(1f)
                .height(40.dp)
            ) {
              Text(text = fmt, fontSize = 11.sp, maxLines = 1, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }
    }

    Text("Converted Output ($currentFormat)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

    OutlinedTextField(
      value = convertedText,
      onValueChange = { convertedText = it },
      modifier = Modifier
        .fillMaxWidth()
        .height(110.dp)
        .testTag("case_converter_output"),
      shape = RoundedCornerShape(12.dp)
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      OutlinedButton(
        onClick = {
          text = ""
          convertedText = ""
        },
        modifier = Modifier.weight(1f)
      ) {
        Icon(Icons.Default.Clear, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text("Clear")
      }

      Button(
        onClick = { copyToClipboard(context, convertedText) },
        colors = ButtonDefaults.buttonColors(containerColor = TooloraSecondary),
        modifier = Modifier.weight(1f)
      ) {
        Icon(Icons.Default.ContentCopy, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text("Copy Result")
      }
    }
  }
}

// -------------------------------------------------------------
// TOOL 4: TEXT DIFF CHECKER
// -------------------------------------------------------------
@Composable
fun TextDiffCheckerTool(modifier: Modifier = Modifier) {
  var originalText by remember {
    mutableStateOf(
      "Toolora is an online tool.\nIt helps you edit text and files.\nEasy to use for everyone."
    )
  }
  var modifiedText by remember {
    mutableStateOf(
      "Toolora is a global online platform.\nIt helps you edit text, photos, and PDF files.\nEasy and fast for everyone."
    )
  }

  val origLines = remember(originalText) { originalText.split("\n") }
  val modLines = remember(modifiedText) { modifiedText.split("\n") }

  var diffCalculated by remember { mutableStateOf(false) }

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
          "Paste the original version in box 1 and the modified version in box 2. Tap 'Check Differences' to inspect added and removed text line by line.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    OutlinedTextField(
      value = originalText,
      onValueChange = { originalText = it; diffCalculated = false },
      label = { Text("Original Text (Version A)") },
      modifier = Modifier
        .fillMaxWidth()
        .height(110.dp)
        .testTag("diff_original_input"),
      shape = RoundedCornerShape(12.dp)
    )

    OutlinedTextField(
      value = modifiedText,
      onValueChange = { modifiedText = it; diffCalculated = false },
      label = { Text("Modified Text (Version B)") },
      modifier = Modifier
        .fillMaxWidth()
        .height(110.dp)
        .testTag("diff_modified_input"),
      shape = RoundedCornerShape(12.dp)
    )

    Button(
      onClick = { diffCalculated = true },
      colors = ButtonDefaults.buttonColors(containerColor = TooloraPrimary),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("check_diff_button")
    ) {
      Text("Compare & Highlight Differences", fontWeight = FontWeight.Bold)
    }

    if (diffCalculated) {
      Text("Comparison Results", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

      val maxLines = maxOf(origLines.size, modLines.size)
      var addedCount = 0
      var removedCount = 0

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.surface)
          .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        for (i in 0 until maxLines) {
          val orig = origLines.getOrNull(i)
          val mod = modLines.getOrNull(i)

          when {
            orig == null && mod != null -> {
              addedCount++
              DiffLineRow(sign = "+", text = mod, bgColor = Color(0xFF10B981).copy(alpha = 0.15f), textColor = Color(0xFF047857))
            }
            orig != null && mod == null -> {
              removedCount++
              DiffLineRow(sign = "-", text = orig, bgColor = Color(0xFFEF4444).copy(alpha = 0.15f), textColor = Color(0xFFB91C1C))
            }
            orig != mod -> {
              removedCount++
              addedCount++
              DiffLineRow(sign = "-", text = orig ?: "", bgColor = Color(0xFFEF4444).copy(alpha = 0.15f), textColor = Color(0xFFB91C1C))
              DiffLineRow(sign = "+", text = mod ?: "", bgColor = Color(0xFF10B981).copy(alpha = 0.15f), textColor = Color(0xFF047857))
            }
            else -> {
              DiffLineRow(sign = " ", text = orig ?: "", bgColor = Color.Transparent, textColor = MaterialTheme.colorScheme.onSurface)
            }
          }
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFF10B981).copy(alpha = 0.15f),
          modifier = Modifier.weight(1f)
        ) {
          Text(
            text = "+ $addedCount Additions",
            color = Color(0xFF047857),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(8.dp)
          )
        }
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFEF4444).copy(alpha = 0.15f),
          modifier = Modifier.weight(1f)
        ) {
          Text(
            text = "- $removedCount Deletions",
            color = Color(0xFFB91C1C),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(8.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun DiffLineRow(
  sign: String,
  text: String,
  bgColor: Color,
  textColor: Color
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(4.dp))
      .background(bgColor)
      .padding(horizontal = 8.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = sign,
      fontFamily = FontFamily.Monospace,
      fontWeight = FontWeight.Bold,
      color = textColor,
      fontSize = 13.sp,
      modifier = Modifier.width(20.dp)
    )
    Text(
      text = text,
      fontFamily = FontFamily.Monospace,
      color = textColor,
      fontSize = 13.sp
    )
  }
}
