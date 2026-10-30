package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ToolCategory(val title: String, val description: String) {
  ALL("All Tools", "Browse our complete catalog of everyday digital tools"),
  TEXT("Text Tools", "Clean, format, count and inspect text content instantly"),
  IMAGE("Image Tools", "Resize, compress, convert and extract text from images"),
  PDF("PDF & Docs", "Merge, split, compress and inspect documents securely"),
  LANGUAGE("Language & AI", "Translate, simplify and summarize reading material")
}

enum class ToolId(
  val title: String,
  val urduTitle: String,
  val arabicTitle: String,
  val category: ToolCategory,
  val shortDesc: String,
  val instructions: String,
  val isPopular: Boolean = false,
  val isProPlanned: Boolean = false
) {
  // TEXT TOOLS (1-4)
  TEXT_CLEANER(
    title = "Text Cleaner",
    urduTitle = "ٹیکسٹ کلینر",
    arabicTitle = "منظف النصوص",
    category = ToolCategory.TEXT,
    shortDesc = "Remove duplicate spaces, unwanted line breaks, and whitespace clutter.",
    instructions = "Paste or type text below. Select cleaning options such as trimming whitespace, stripping empty lines, or normalising tabs, then click 'Clean Text'.",
    isPopular = true
  ),
  WORD_COUNTER(
    title = "Word Counter",
    urduTitle = "ورڈ کاؤنٹر",
    arabicTitle = "عداد الكلمات",
    category = ToolCategory.TEXT,
    shortDesc = "Count words, characters, sentences, paragraphs, and reading times live.",
    instructions = "Enter or paste your content in the input box. Metrics update in real time as you type.",
    isPopular = true
  ),
  CASE_CONVERTER(
    title = "Case Converter",
    urduTitle = "کیس کنورٹر",
    arabicTitle = "محول حالة الأحرف",
    category = ToolCategory.TEXT,
    shortDesc = "Switch between UPPERCASE, lowercase, Title Case, camelCase, and snake_case.",
    instructions = "Input your sentence or paragraph, then tap any case format button to convert instantly.",
    isPopular = false
  ),
  TEXT_DIFF(
    title = "Text Diff Checker",
    urduTitle = "متن کے فرق کی جانچ",
    arabicTitle = "فاحص الفروقات النصية",
    category = ToolCategory.TEXT,
    shortDesc = "Compare two text snippets side-by-side with added and removed highlights.",
    instructions = "Paste the original text in the left/first box and the modified version in the right/second box. Tap 'Compare' to inspect differences.",
    isPopular = false
  ),

  // IMAGE TOOLS (5-8)
  IMAGE_RESIZER(
    title = "Image Resizer",
    urduTitle = "تصویر ری سائزر",
    arabicTitle = "تغيير حجم الصور",
    category = ToolCategory.IMAGE,
    shortDesc = "Resize photos to custom pixel dimensions with aspect ratio lock.",
    instructions = "Pick an image from your device. Specify the desired width and height or pick a quick social preset, then process and save.",
    isPopular = true
  ),
  IMAGE_COMPRESSOR(
    title = "Image Compressor",
    urduTitle = "تصویر کمپریسر",
    arabicTitle = "ضغط الصور",
    category = ToolCategory.IMAGE,
    shortDesc = "Reduce image file size with an adjustable quality slider.",
    instructions = "Upload a photo or graphic. Adjust the compression slider to reduce bytes while preserving clarity, then export.",
    isPopular = true
  ),
  IMAGE_FORMAT_CONVERTER(
    title = "Image Format Converter",
    urduTitle = "تصویر کا فارمیٹ کنورٹر",
    arabicTitle = "محول صيغ الصور",
    category = ToolCategory.IMAGE,
    shortDesc = "Convert images between PNG, JPEG, and WebP format.",
    instructions = "Choose an image file. Pick your target output format and export the converted file directly to your device.",
    isPopular = false
  ),
  IMAGE_TO_TEXT(
    title = "Image to Text (OCR)",
    urduTitle = "تصویر سے متن (او سی آر)",
    arabicTitle = "استخراج النص من الصور",
    category = ToolCategory.IMAGE,
    shortDesc = "Extract readable text from photos and graphic documents.",
    instructions = "Select an image containing text or documentation. The scanner will parse characters and provide editable text.",
    isPopular = true
  ),

  // PDF & DOCUMENT TOOLS (9-12)
  PDF_MERGER(
    title = "PDF Merger",
    urduTitle = "پی ڈی ایف انضمام",
    arabicTitle = "دمج ملفات PDF",
    category = ToolCategory.PDF,
    shortDesc = "Combine multiple PDF pages or documents into a single file.",
    instructions = "Add or compose PDF documents. Arrange the sections, preview the combined file, and generate a downloadable merged document.",
    isPopular = true
  ),
  PDF_SPLITTER(
    title = "PDF Splitter",
    urduTitle = "پی ڈی ایف الگ کرنے والا",
    arabicTitle = "تقسيم ملفات PDF",
    category = ToolCategory.PDF,
    shortDesc = "Extract selected page ranges or separate pages from PDF files.",
    instructions = "Select or load a document. Specify the starting and ending page range to extract and download only the pages you need.",
    isPopular = false
  ),
  PDF_COMPRESSOR(
    title = "PDF Compressor",
    urduTitle = "پی ڈی ایف کمپریسر",
    arabicTitle = "ضغط ملفات PDF",
    category = ToolCategory.PDF,
    shortDesc = "Optimize and reduce document size for easy sharing.",
    instructions = "Load a PDF file and choose your desired compression level (Balanced, High, or Compact). Process to download the smaller file.",
    isPopular = false
  ),
  DOCUMENT_HELPER(
    title = "Document Helper",
    urduTitle = "دستاویز معاون",
    arabicTitle = "مساعد المستندات",
    category = ToolCategory.PDF,
    shortDesc = "Inspect file metadata, byte size, MIME type, and cryptographic checksums.",
    instructions = "Choose any file from your device. Toolora analyzes the stream and reports exact byte measurements, MD5, and SHA-256 hashes.",
    isPopular = false
  ),

  // LANGUAGE & READING TOOLS (13-15)
  LANGUAGE_HELPER(
    title = "Language Helper",
    urduTitle = "زبان کا معاون",
    arabicTitle = "مساعد اللغات والترجمة",
    category = ToolCategory.LANGUAGE,
    shortDesc = "Translate text across multiple international languages with TTS.",
    instructions = "Select your source and target languages. Enter text to translate, listen to the pronunciation, or copy the translation.",
    isPopular = true
  ),
  EASY_READER(
    title = "Easy Reader",
    urduTitle = "آسان قاری",
    arabicTitle = "القراءة السهلة والتبسيط",
    category = ToolCategory.LANGUAGE,
    shortDesc = "Rewrite difficult and jargon-heavy text into plain, clear language.",
    instructions = "Paste complex articles, contracts, or academic text. Easy Reader simplifies the vocabulary and outputs key takeaways.",
    isPopular = true
  ),
  TEXT_SUMMARIZER(
    title = "Text Summarizer",
    urduTitle = "متن کا خلاصہ نگار",
    arabicTitle = "ملخص النصوص الذكي",
    category = ToolCategory.LANGUAGE,
    shortDesc = "Generate concise bullet summaries and executive briefings from long text.",
    instructions = "Paste long documents, articles, or notes. Choose your preferred summary depth (Short, Medium, or Detailed) to generate insights.",
    isPopular = true
  )
}

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String, val isRtl: Boolean) {
  ENGLISH("en", "English", "English", false),
  URDU("ur", "Urdu", "اردو", true),
  ARABIC("ar", "Arabic", "العربية", true)
}

@Entity(tableName = "recent_tools")
data class RecentToolEntity(
  @PrimaryKey val toolId: String,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_tools")
data class FavoriteToolEntity(
  @PrimaryKey val toolId: String,
  val addedAt: Long = System.currentTimeMillis()
)
