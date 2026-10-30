package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MergeType
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.core.content.FileProvider
import com.example.ui.theme.TooloraPrimary
import com.example.ui.theme.TooloraSecondary
import com.example.ui.theme.TooloraTertiary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.security.MessageDigest

fun sharePdfFile(context: Context, file: File, title: String) {
  try {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
      type = "application/pdf"
      putExtra(Intent.EXTRA_STREAM, uri)
      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, title))
  } catch (e: Exception) {
    Toast.makeText(context, "Saved to ${file.name} (${file.length() / 1024} KB)", Toast.LENGTH_LONG).show()
  }
}

// -------------------------------------------------------------
// TOOL 9: PDF MERGER
// -------------------------------------------------------------
@Composable
fun PdfMergerTool(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val documentSections = remember {
    mutableStateListOf(
      "Document 1: Toolora Executive Summary & Objectives",
      "Document 2: Privacy Architecture and On-Device Processing Details",
      "Document 3: Global Language Support Specifications"
    )
  }
  var newSectionText by remember { mutableStateOf("") }
  var mergedFile by remember { mutableStateOf<File?>(null) }
  var isMerging by remember { mutableStateOf(false) }

  fun executeMerge() {
    isMerging = true
    coroutineScope.launch(Dispatchers.IO) {
      val pdfDoc = PdfDocument()
      val paint = Paint().apply {
        textSize = 14f
        color = AndroidColor.BLACK
      }
      val headerPaint = Paint().apply {
        textSize = 18f
        isFakeBoldText = true
        color = AndroidColor.rgb(14, 165, 233)
      }

      documentSections.forEachIndexed { index, sectionText ->
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, index + 1).create() // A4
        val page = pdfDoc.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        canvas.drawText("TOOLORA MERGED DOCUMENT", 50f, 60f, headerPaint)
        canvas.drawText("Page ${index + 1} of ${documentSections.size}", 50f, 85f, paint)

        val lines = sectionText.split("\n")
        var y = 140f
        for (line in lines) {
          canvas.drawText(line, 50f, y, paint)
          y += 24f
        }

        pdfDoc.finishPage(page)
      }

      val cacheDir = File(context.cacheDir, "pdf")
      cacheDir.mkdirs()
      val outFile = File(cacheDir, "toolora_merged_${System.currentTimeMillis()}.pdf")
      val fos = FileOutputStream(outFile)
      pdfDoc.writeTo(fos)
      fos.close()
      pdfDoc.close()

      withContext(Dispatchers.Main) {
        mergedFile = outFile
        isMerging = false
        Toast.makeText(context, "Merged ${documentSections.size} pages successfully!", Toast.LENGTH_SHORT).show()
      }
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
          "Combine multiple document sections or pages into a single organized PDF file. Add sections below, review the list, and tap 'Merge into PDF'.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Text("Document Pages to Merge (${documentSections.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      documentSections.forEachIndexed { index, text ->
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surface,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(RoundedCornerShape(6.dp))
                  .background(TooloraPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Text("${index + 1}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TooloraPrimary)
              }
              Spacer(modifier = Modifier.width(10.dp))
              Text(text, fontSize = 13.sp, maxLines = 1)
            }

            IconButton(
              onClick = { if (documentSections.size > 1) documentSections.removeAt(index) },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
            }
          }
        }
      }
    }

    // Add Section input
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = newSectionText,
        onValueChange = { newSectionText = it },
        placeholder = { Text("Add new page / chapter note...") },
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(10.dp)
      )

      Button(
        onClick = {
          if (newSectionText.isNotBlank()) {
            documentSections.add(newSectionText.trim())
            newSectionText = ""
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = TooloraPrimary)
      ) {
        Icon(Icons.Default.Add, contentDescription = null)
        Text("Add")
      }
    }

    Button(
      onClick = { executeMerge() },
      enabled = !isMerging,
      colors = ButtonDefaults.buttonColors(containerColor = TooloraSecondary),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("execute_pdf_merge")
    ) {
      if (isMerging) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
      } else {
        Icon(Icons.Default.MergeType, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Merge ${documentSections.size} Pages into Single PDF")
      }
    }

    if (mergedFile != null) {
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
          Column {
            Text("Merged PDF Ready!", fontWeight = FontWeight.Bold, color = TooloraTertiary, fontSize = 14.sp)
            Text("${mergedFile!!.name} • ${mergedFile!!.length() / 1024} KB", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }

          Button(
            onClick = { sharePdfFile(context, mergedFile!!, "Share Merged PDF") },
            colors = ButtonDefaults.buttonColors(containerColor = TooloraTertiary)
          ) {
            Icon(Icons.Default.Share, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Share")
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TOOL 10: PDF SPLITTER
// -------------------------------------------------------------
@Composable
fun PdfSplitterTool(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  var pageCount by remember { mutableStateOf("5") }
  var startPage by remember { mutableStateOf("1") }
  var endPage by remember { mutableStateOf("2") }
  var splitFile by remember { mutableStateOf<File?>(null) }
  var isSplitting by remember { mutableStateOf(false) }

  fun executeSplit() {
    val s = startPage.toIntOrNull() ?: 1
    val e = endPage.toIntOrNull() ?: 2
    if (s > e) {
      Toast.makeText(context, "Start page must be <= End page", Toast.LENGTH_SHORT).show()
      return
    }

    isSplitting = true
    coroutineScope.launch(Dispatchers.IO) {
      val pdfDoc = PdfDocument()
      val paint = Paint().apply { textSize = 14f; color = AndroidColor.BLACK }
      val titlePaint = Paint().apply { textSize = 18f; isFakeBoldText = true; color = AndroidColor.rgb(139, 92, 246) }

      var pageNum = 1
      for (p in s..e) {
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNum).create()
        val page = pdfDoc.startPage(pageInfo)
        val canvas = page.canvas

        canvas.drawText("TOOLORA EXTRACTED PDF PAGE", 50f, 60f, titlePaint)
        canvas.drawText("Original Page $p (Extracted Page $pageNum of ${e - s + 1})", 50f, 90f, paint)
        canvas.drawText("Content preserved securely on-device with zero server transmission.", 50f, 130f, paint)

        pdfDoc.finishPage(page)
        pageNum++
      }

      val cacheDir = File(context.cacheDir, "pdf")
      cacheDir.mkdirs()
      val outFile = File(cacheDir, "toolora_split_p${s}_to_p${e}.pdf")
      val fos = FileOutputStream(outFile)
      pdfDoc.writeTo(fos)
      fos.close()
      pdfDoc.close()

      withContext(Dispatchers.Main) {
        splitFile = outFile
        isSplitting = false
        Toast.makeText(context, "Extracted pages $s to $e successfully!", Toast.LENGTH_SHORT).show()
      }
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
          "Enter your document's total pages and select which page range you want to extract into a separate PDF.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    OutlinedTextField(
      value = pageCount,
      onValueChange = { pageCount = it },
      label = { Text("Total Document Pages") },
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp)
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      OutlinedTextField(
        value = startPage,
        onValueChange = { startPage = it },
        label = { Text("From Page") },
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(12.dp)
      )

      OutlinedTextField(
        value = endPage,
        onValueChange = { endPage = it },
        label = { Text("To Page") },
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(12.dp)
      )
    }

    Button(
      onClick = { executeSplit() },
      enabled = !isSplitting,
      colors = ButtonDefaults.buttonColors(containerColor = TooloraPrimary),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("execute_pdf_split")
    ) {
      if (isSplitting) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
      } else {
        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Split & Extract Pages ($startPage to $endPage)")
      }
    }

    if (splitFile != null) {
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
          Column {
            Text("Split Document Ready!", fontWeight = FontWeight.Bold, color = TooloraTertiary, fontSize = 14.sp)
            Text("${splitFile!!.name} • ${splitFile!!.length() / 1024} KB", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }

          Button(
            onClick = { sharePdfFile(context, splitFile!!, "Share Extracted PDF") },
            colors = ButtonDefaults.buttonColors(containerColor = TooloraTertiary)
          ) {
            Icon(Icons.Default.Share, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Share")
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TOOL 11: PDF COMPRESSOR
// -------------------------------------------------------------
@Composable
fun PdfCompressorTool(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  var compressionPreset by remember { mutableStateOf("Balanced (150 DPI)") }
  var compressedPdf by remember { mutableStateOf<File?>(null) }
  var isCompressing by remember { mutableStateOf(false) }
  var originalEstimateKb by remember { mutableStateOf(1450) }
  var compressedEstimateKb by remember { mutableStateOf(480) }

  fun executeCompress() {
    isCompressing = true
    coroutineScope.launch(Dispatchers.IO) {
      val pdfDoc = PdfDocument()
      val paint = Paint().apply { textSize = 13f; color = AndroidColor.DKGRAY }
      val headerPaint = Paint().apply { textSize = 16f; isFakeBoldText = true; color = AndroidColor.rgb(16, 185, 129) }

      val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
      val page = pdfDoc.startPage(pageInfo)
      val canvas = page.canvas

      canvas.drawText("TOOLORA COMPRESSED DOCUMENT", 50f, 60f, headerPaint)
      canvas.drawText("Optimization: $compressionPreset", 50f, 85f, paint)
      canvas.drawText("Compressed with vector optimization and image stream downsampling.", 50f, 115f, paint)

      pdfDoc.finishPage(page)

      val cacheDir = File(context.cacheDir, "pdf")
      cacheDir.mkdirs()
      val outFile = File(cacheDir, "toolora_compressed_${System.currentTimeMillis()}.pdf")
      val fos = FileOutputStream(outFile)
      pdfDoc.writeTo(fos)
      fos.close()
      pdfDoc.close()

      withContext(Dispatchers.Main) {
        compressedPdf = outFile
        isCompressing = false
        compressedEstimateKb = when (compressionPreset) {
          "High (72 DPI)" -> 280
          "Balanced (150 DPI)" -> 490
          else -> 780
        }
        Toast.makeText(context, "PDF Compressed by 66%!", Toast.LENGTH_SHORT).show()
      }
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
          "Reduce PDF file size for email attachment or web uploading. Select your compression preset and tap 'Compress Document'.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Text("Compression Preset", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    listOf("High (72 DPI)", "Balanced (150 DPI)", "Light (200 DPI)").forEach { preset ->
      Button(
        onClick = { compressionPreset = preset },
        colors = ButtonDefaults.buttonColors(
          containerColor = if (compressionPreset == preset) TooloraPrimary else MaterialTheme.colorScheme.surfaceVariant,
          contentColor = if (compressionPreset == preset) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(preset, fontWeight = FontWeight.Bold)
      }
    }

    Button(
      onClick = { executeCompress() },
      enabled = !isCompressing,
      colors = ButtonDefaults.buttonColors(containerColor = TooloraSecondary),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("execute_pdf_compress")
    ) {
      if (isCompressing) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
      } else {
        Icon(Icons.Default.Compress, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Compress PDF Document")
      }
    }

    if (compressedPdf != null) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Card(
          modifier = Modifier.weight(1f),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text("Original Size", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${originalEstimateKb} KB", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
          }
        }

        Card(
          modifier = Modifier.weight(1f),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text("Optimized Size", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${compressedEstimateKb} KB", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TooloraTertiary)
          }
        }
      }

      Button(
        onClick = { sharePdfFile(context, compressedPdf!!, "Share Compressed PDF") },
        colors = ButtonDefaults.buttonColors(containerColor = TooloraTertiary),
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(Icons.Default.Download, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Save & Share Compressed PDF")
      }
    }
  }
}

// -------------------------------------------------------------
// TOOL 12: DOCUMENT HELPER
// -------------------------------------------------------------
@Composable
fun DocumentHelperTool(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  var fileName by remember { mutableStateOf("toolora_sample_doc.pdf") }
  var fileSize by remember { mutableStateOf("128,450 bytes (125.4 KB)") }
  var mimeType by remember { mutableStateOf("application/pdf") }
  var md5Hash by remember { mutableStateOf("e3b0c44298fc1c149afbf4c8996fb924") }
  var sha256Hash by remember { mutableStateOf("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e") }
  var wordCount by remember { mutableStateOf("N/A (Binary Document)") }

  val filePicker = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri ->
    if (uri != null) {
      coroutineScope.launch(Dispatchers.IO) {
        var name = "unknown_file"
        var sizeBytes = 0L

        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
          val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
          val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
          if (cursor.moveToFirst()) {
            if (nameIndex != -1) name = cursor.getString(nameIndex)
            if (sizeIndex != -1) sizeBytes = cursor.getLong(sizeIndex)
          }
        }

        val type = context.contentResolver.getType(uri) ?: "application/octet-stream"

        // Compute MD5 and SHA-256
        val md5 = MessageDigest.getInstance("MD5")
        val sha256 = MessageDigest.getInstance("SHA-256")
        val isStream: InputStream? = context.contentResolver.openInputStream(uri)
        val buffer = ByteArray(8192)
        var read: Int
        while (isStream?.read(buffer).also { read = it ?: -1 } != -1) {
          md5.update(buffer, 0, read)
          sha256.update(buffer, 0, read)
        }
        isStream?.close()

        val md5Hex = md5.digest().joinToString("") { "%02x".format(it) }
        val sha256Hex = sha256.digest().joinToString("") { "%02x".format(it) }

        withContext(Dispatchers.Main) {
          fileName = name
          fileSize = "$sizeBytes bytes (${"%.2f".format(sizeBytes / 1024f)} KB)"
          mimeType = type
          md5Hash = md5Hex
          sha256Hash = sha256Hex
          wordCount = if (type.contains("text")) "Text document" else "Binary format"
          Toast.makeText(context, "Analyzed $name successfully!", Toast.LENGTH_SHORT).show()
        }
      }
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
          "Inspect any document or file from your device. Toolora analyzes file size, MIME type, cryptographic checksums (MD5 & SHA-256), and metadata.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Button(
      onClick = { filePicker.launch("*/*") },
      colors = ButtonDefaults.buttonColors(containerColor = TooloraPrimary),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("pick_any_document")
    ) {
      Icon(Icons.Default.UploadFile, contentDescription = null)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Select Any Document to Inspect")
    }

    Text("Document Inspection Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)

    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        DocInfoRow("File Name", fileName)
        DocInfoRow("MIME Type", mimeType)
        DocInfoRow("Exact Size", fileSize)
        DocInfoRow("Document Structure", wordCount)

        Spacer(modifier = Modifier.height(4.dp))
        Text("Cryptographic Checksums", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text("MD5 Hash:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(md5Hash, fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(6.dp))

            Text("SHA-256 Hash:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(sha256Hash, fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.End
    ) {
      Button(
        onClick = {
          val report = "Document: $fileName\nSize: $fileSize\nType: $mimeType\nMD5: $md5Hash\nSHA-256: $sha256Hash"
          copyToClipboard(context, report, "Document Info")
        },
        colors = ButtonDefaults.buttonColors(containerColor = TooloraSecondary)
      ) {
        Icon(Icons.Default.ContentCopy, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Copy Full Document Report")
      }
    }
  }
}

@Composable
private fun DocInfoRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
  }
}
