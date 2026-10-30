package com.example

import com.example.data.intelligence.IntelligenceEngine
import com.example.data.model.ToolCategory
import com.example.data.model.ToolId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testTooloraCatalogCompleteness() {
    val allTools = ToolId.values()
    assertEquals("Toolora must have exactly 15 tools", 15, allTools.size)

    val textTools = allTools.filter { it.category == ToolCategory.TEXT }
    assertEquals("Text tools count must be 4", 4, textTools.size)

    val imageTools = allTools.filter { it.category == ToolCategory.IMAGE }
    assertEquals("Image tools count must be 4", 4, imageTools.size)

    val pdfTools = allTools.filter { it.category == ToolCategory.PDF }
    assertEquals("PDF tools count must be 4", 4, pdfTools.size)

    val languageTools = allTools.filter { it.category == ToolCategory.LANGUAGE }
    assertEquals("Language tools count must be 3", 3, languageTools.size)
  }

  @Test
  fun testIntelligenceEngineSummarization() {
    val input = "Android is an operating system. It powers billions of handheld devices worldwide. Kotlin is the preferred language."
    val summary = IntelligenceEngine.summarizeText(input, "bullets")
    assertTrue(summary.contains("Key Takeaways"))
    assertTrue(summary.contains("•"))
  }

  @Test
  fun testIntelligenceEngineSimplify() {
    val input = "We will endeavor to utilize this optimal paradigm."
    val (result, score) = IntelligenceEngine.simplifyText(input)
    assertTrue("Should replace utilize with use", result.contains("use"))
    assertTrue("Should replace endeavor with try", result.contains("try"))
    assertTrue("Score should be positive", score > 0)
  }
}
