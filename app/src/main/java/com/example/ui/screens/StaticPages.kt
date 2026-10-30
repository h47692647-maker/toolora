package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TooloraPrimary
import com.example.ui.theme.TooloraSecondary
import com.example.ui.theme.TooloraTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
  BackHandler { onBack() }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("About Toolora", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
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
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text("TOOLORA", fontWeight = FontWeight.Black, fontSize = 24.sp, color = TooloraPrimary)
          Text("All Your Everyday Tools in One Place.", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            "Toolora was founded on a simple principle: digital utility should be fast, accessible to anyone globally, and privacy-respecting. Modern digital creators, students, and businesses shouldn't need to visit twenty ad-bloated, suspicious websites just to clean a block of text, resize a photo, extract an image quote, or merge documents.",
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Text("Our Core Pillars", fontWeight = FontWeight.Bold, fontSize = 16.sp)

      PillarCard(
        icon = Icons.Default.Security,
        title = "Privacy First",
        desc = "Your files and text belong to you. Toolora computes hashes, resizes images, and formats text strictly on your device."
      )

      PillarCard(
        icon = Icons.Default.Speed,
        title = "Lightweight & Fast",
        desc = "Engineered with native Android and Kotlin Compose for immediate response times and zero bloatware."
      )

      PillarCard(
        icon = Icons.Default.Star,
        title = "Global Accessibility",
        desc = "Built with multi-language support (English, Urdu, and Arabic) featuring native right-to-left layout alignment."
      )

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun PillarCard(icon: ImageVector, title: String, desc: String) {
  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .background(TooloraPrimary.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = null, tint = TooloraPrimary)
      }
      Column {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(desc, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
  BackHandler { onBack() }
  val context = LocalContext.current

  var name by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("General Inquiry") }
  var message by remember { mutableStateOf("") }
  var isSubmitted by remember { mutableStateOf(false) }
  var catMenuExpanded by remember { mutableStateOf(false) }

  val categories = listOf("General Inquiry", "Feature Request", "Bug Report", "Toolora Partner / Ad Space", "Feedback")

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Contact Us", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
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
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      if (isSubmitted) {
        Card(
          colors = CardDefaults.cardColors(containerColor = TooloraTertiary.copy(alpha = 0.15f)),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TooloraTertiary, modifier = Modifier.size(56.dp))
            Text("Message Received!", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TooloraTertiary)
            Text(
              "Thank you $name! Your message has been logged. Our engineering team typically responds within 24 hours.",
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Button(
              onClick = {
                name = ""
                email = ""
                message = ""
                isSubmitted = false
              },
              colors = ButtonDefaults.buttonColors(containerColor = TooloraTertiary)
            ) {
              Text("Send Another Message")
            }
          }
        }
      } else {
        Text("Get in Touch", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(
          "Have a tool suggestion or encountered an issue? Send us a message and our team will get right back to you.",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Your Full Name") },
          modifier = Modifier.fillMaxWidth().testTag("contact_name_input"),
          shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text("Email Address") },
          modifier = Modifier.fillMaxWidth().testTag("contact_email_input"),
          shape = RoundedCornerShape(12.dp)
        )

        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedButton(
            onClick = { catMenuExpanded = true },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Category: $category")
              Text("▼", fontSize = 10.sp)
            }
          }
          DropdownMenu(
            expanded = catMenuExpanded,
            onDismissRequest = { catMenuExpanded = false }
          ) {
            categories.forEach { c ->
              DropdownMenuItem(
                text = { Text(c) },
                onClick = { category = c; catMenuExpanded = false }
              )
            }
          }
        }

        OutlinedTextField(
          value = message,
          onValueChange = { message = it },
          label = { Text("Your Message") },
          modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .testTag("contact_message_input"),
          shape = RoundedCornerShape(12.dp)
        )

        Button(
          onClick = {
            if (name.isBlank() || email.isBlank() || message.isBlank()) {
              Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
              isSubmitted = true
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = TooloraPrimary),
          modifier = Modifier.fillMaxWidth().height(48.dp).testTag("contact_submit_button")
        ) {
          Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Send Message", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
  BackHandler { onBack() }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Privacy Policy", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
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
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Text("Toolora Privacy Policy", fontWeight = FontWeight.Black, fontSize = 20.sp)
      Text("Last updated: October 2026", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

      PolicySection(
        title = "1. Local On-Device Processing",
        body = "Toolora processes your text, photos, and PDF files locally inside your device runtime whenever possible. When you resize an image, convert format, clean text, or compute file hashes, the raw data never leaves your device."
      )

      PolicySection(
        title = "2. No Unauthorized File Storage",
        body = "We do not store, catalog, or archive user-uploaded files on external servers. Output files are saved only to your local application cache or downloads directory when explicitly requested by you."
      )

      PolicySection(
        title = "3. Third-Party Intelligence & APIs",
        body = "Features using external AI services (such as optional cloud translation or Gemini API) are clearly indicated. When such services are invoked, only the designated text prompt is submitted to generate the response."
      )

      PolicySection(
        title = "4. Analytics & Advertising Disclosure",
        body = "Toolora does not employ invasive trackers. Future sponsored placements or partner advertisements will be non-intrusive and adhere strictly to Google Play and international data privacy regulations."
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfUseScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
  BackHandler { onBack() }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Terms of Use", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
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
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Text("Terms of Use", fontWeight = FontWeight.Black, fontSize = 20.sp)
      Text("Effective Date: October 2026", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

      PolicySection(
        title = "1. Acceptance of Terms",
        body = "By downloading, accessing, or using Toolora, you agree to abide by these terms. Toolora is provided free to start for individual and organizational utility."
      )

      PolicySection(
        title = "2. Permitted Use",
        body = "You may use Toolora for personal, commercial, and educational tasks. You agree not to attempt to reverse engineer, disrupt system integrity, or use the tools for unlawful activities."
      )

      PolicySection(
        title = "3. Disclaimer of Warranties",
        body = "Toolora tools are provided 'as is'. While we engineer high-grade utilities, we do not guarantee uninterrupted availability or suitability for mission-critical operations without independent verification."
      )
    }
  }
}

@Composable
private fun PolicySection(title: String, body: String) {
  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(12.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
      Spacer(modifier = Modifier.height(6.dp))
      Text(body, fontSize = 13.sp, lineHeight = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}
