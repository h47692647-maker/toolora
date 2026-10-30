package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.ToolCategory
import com.example.ui.theme.TooloraPrimary
import com.example.ui.theme.TooloraSecondary

sealed class ScreenDestination {
  object Home : ScreenDestination()
  object AllTools : ScreenDestination()
  data class Category(val category: ToolCategory) : ScreenDestination()
  data class ToolDetail(val toolId: com.example.data.model.ToolId) : ScreenDestination()
  object Chatbot : ScreenDestination()
  object AboutUs : ScreenDestination()
  object ContactUs : ScreenDestination()
  object PrivacyPolicy : ScreenDestination()
  object TermsOfUse : ScreenDestination()
}

@Composable
fun TooloraDrawerContent(
  currentScreen: ScreenDestination,
  currentLanguage: AppLanguage,
  onNavigate: (ScreenDestination) -> Unit,
  onCloseDrawer: () -> Unit
) {
  ModalDrawerSheet(
    modifier = Modifier.width(300.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
      // Header
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(bottom = 20.dp)
      ) {
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
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
            fontSize = 26.sp
          )
        }
        Column {
          Text(
            text = "TOOLORA",
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.primary
          )
          Text(
            text = "Global Online Tools",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.5f))
      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "TOOLS NAVIGATION",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
      )

      DrawerItem(
        label = "Home",
        icon = Icons.Default.Home,
        selected = currentScreen is ScreenDestination.Home,
        onClick = { onNavigate(ScreenDestination.Home); onCloseDrawer() }
      )

      DrawerItem(
        label = "All Tools",
        icon = Icons.Default.Apps,
        selected = currentScreen is ScreenDestination.AllTools,
        onClick = { onNavigate(ScreenDestination.AllTools); onCloseDrawer() }
      )

      DrawerItem(
        label = "Text Tools",
        icon = Icons.Default.TextFields,
        selected = (currentScreen as? ScreenDestination.Category)?.category == ToolCategory.TEXT,
        onClick = { onNavigate(ScreenDestination.Category(ToolCategory.TEXT)); onCloseDrawer() }
      )

      DrawerItem(
        label = "Image Tools",
        icon = Icons.Default.Image,
        selected = (currentScreen as? ScreenDestination.Category)?.category == ToolCategory.IMAGE,
        onClick = { onNavigate(ScreenDestination.Category(ToolCategory.IMAGE)); onCloseDrawer() }
      )

      DrawerItem(
        label = "PDF Tools",
        icon = Icons.Default.PictureAsPdf,
        selected = (currentScreen as? ScreenDestination.Category)?.category == ToolCategory.PDF,
        onClick = { onNavigate(ScreenDestination.Category(ToolCategory.PDF)); onCloseDrawer() }
      )

      DrawerItem(
        label = "Language Tools",
        icon = Icons.Default.Language,
        selected = (currentScreen as? ScreenDestination.Category)?.category == ToolCategory.LANGUAGE,
        onClick = { onNavigate(ScreenDestination.Category(ToolCategory.LANGUAGE)); onCloseDrawer() }
      )

      DrawerItem(
        label = "Toolora AI Assistant",
        icon = Icons.Default.ChatBubble,
        selected = currentScreen is ScreenDestination.Chatbot,
        badge = "AI",
        onClick = { onNavigate(ScreenDestination.Chatbot); onCloseDrawer() }
      )

      Spacer(modifier = Modifier.height(16.dp))
      HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.5f))
      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "COMPANY & LEGAL",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
      )

      DrawerItem(
        label = "About Us",
        icon = Icons.Default.Info,
        selected = currentScreen is ScreenDestination.AboutUs,
        onClick = { onNavigate(ScreenDestination.AboutUs); onCloseDrawer() }
      )

      DrawerItem(
        label = "Contact Us",
        icon = Icons.Default.ContactMail,
        selected = currentScreen is ScreenDestination.ContactUs,
        onClick = { onNavigate(ScreenDestination.ContactUs); onCloseDrawer() }
      )

      DrawerItem(
        label = "Privacy Policy",
        icon = Icons.Default.PrivacyTip,
        selected = currentScreen is ScreenDestination.PrivacyPolicy,
        onClick = { onNavigate(ScreenDestination.PrivacyPolicy); onCloseDrawer() }
      )

      DrawerItem(
        label = "Terms of Use",
        icon = Icons.Default.Gavel,
        selected = currentScreen is ScreenDestination.TermsOfUse,
        onClick = { onNavigate(ScreenDestination.TermsOfUse); onCloseDrawer() }
      )

      Spacer(modifier = Modifier.weight(1f, fill = false))
      Spacer(modifier = Modifier.height(24.dp))

      // Footer plan badge
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = "Toolora Free Edition",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Text(
            text = "100% Free • Private On-Device",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
          )
        }
      }
    }
  }
}

@Composable
private fun DrawerItem(
  label: String,
  icon: ImageVector,
  selected: Boolean,
  onClick: () -> Unit,
  badge: String? = null
) {
  NavigationDrawerItem(
    label = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
        if (badge != null) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = TooloraSecondary,
            modifier = Modifier.padding(start = 8.dp)
          ) {
            Text(
              text = badge,
              fontSize = 10.sp,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }
    },
    icon = { Icon(imageVector = icon, contentDescription = label) },
    selected = selected,
    onClick = onClick,
    modifier = Modifier.padding(vertical = 2.dp),
    shape = RoundedCornerShape(12.dp)
  )
}
