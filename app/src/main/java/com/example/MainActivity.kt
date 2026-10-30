package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.data.local.ToolDatabase
import com.example.data.local.ToolRepository
import com.example.data.model.AppLanguage
import com.example.data.model.ToolCategory
import com.example.data.model.ToolId
import com.example.ui.components.ScreenDestination
import com.example.ui.components.TooloraDrawerContent
import com.example.ui.components.TooloraTopBar
import com.example.ui.screens.AboutUsScreen
import com.example.ui.screens.AllToolsScreen
import com.example.ui.screens.ChatbotScreen
import com.example.ui.screens.ContactUsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.TermsOfUseScreen
import com.example.ui.screens.ToolDetailScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = ToolDatabase.getDatabase(this)
    val repository = ToolRepository(database.toolDao())

    setContent {
      TooloraApp(repository)
    }
  }
}

@Composable
fun TooloraApp(repository: ToolRepository) {
  val systemDark = isSystemInDarkTheme()
  var isDarkTheme by remember { mutableStateOf(systemDark) }
  var currentLanguage by remember { mutableStateOf(AppLanguage.ENGLISH) }
  var currentScreen by remember { mutableStateOf<ScreenDestination>(ScreenDestination.Home) }

  val coroutineScope = rememberCoroutineScope()
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

  val recentTools by repository.recentTools.collectAsState(initial = emptyList())
  val favoriteTools by repository.favoriteTools.collectAsState(initial = emptySet())

  val layoutDirection = if (currentLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

  CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    MyApplicationTheme(darkTheme = isDarkTheme) {
      ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
          TooloraDrawerContent(
            currentScreen = currentScreen,
            currentLanguage = currentLanguage,
            onNavigate = { destination ->
              currentScreen = destination
            },
            onCloseDrawer = {
              coroutineScope.launch { drawerState.close() }
            }
          )
        }
      ) {
        when (val screen = currentScreen) {
          is ScreenDestination.Home -> {
            Scaffold(
              modifier = Modifier.fillMaxSize(),
              topBar = {
                TooloraTopBar(
                  currentLanguage = currentLanguage,
                  isDarkTheme = isDarkTheme,
                  onLanguageSelected = { currentLanguage = it },
                  onToggleTheme = { isDarkTheme = !isDarkTheme },
                  onOpenChat = { currentScreen = ScreenDestination.Chatbot },
                  onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                )
              }
            ) { innerPadding ->
              HomeScreen(
                currentLanguage = currentLanguage,
                favoriteTools = favoriteTools,
                recentTools = recentTools,
                onSelectTool = { tool ->
                  coroutineScope.launch { repository.recordToolUsage(tool) }
                  currentScreen = ScreenDestination.ToolDetail(tool)
                },
                onToggleFavorite = { tool ->
                  coroutineScope.launch {
                    repository.toggleFavorite(tool, favoriteTools.contains(tool))
                  }
                },
                onNavigate = { dest -> currentScreen = dest },
                modifier = Modifier.padding(innerPadding)
              )
            }
          }

          is ScreenDestination.AllTools -> {
            AllToolsScreen(
              initialCategory = ToolCategory.ALL,
              currentLanguage = currentLanguage,
              favoriteTools = favoriteTools,
              onSelectTool = { tool ->
                coroutineScope.launch { repository.recordToolUsage(tool) }
                currentScreen = ScreenDestination.ToolDetail(tool)
              },
              onToggleFavorite = { tool ->
                coroutineScope.launch {
                  repository.toggleFavorite(tool, favoriteTools.contains(tool))
                }
              },
              onBack = { currentScreen = ScreenDestination.Home }
            )
          }

          is ScreenDestination.Category -> {
            AllToolsScreen(
              initialCategory = screen.category,
              currentLanguage = currentLanguage,
              favoriteTools = favoriteTools,
              onSelectTool = { tool ->
                coroutineScope.launch { repository.recordToolUsage(tool) }
                currentScreen = ScreenDestination.ToolDetail(tool)
              },
              onToggleFavorite = { tool ->
                coroutineScope.launch {
                  repository.toggleFavorite(tool, favoriteTools.contains(tool))
                }
              },
              onBack = { currentScreen = ScreenDestination.Home }
            )
          }

          is ScreenDestination.ToolDetail -> {
            ToolDetailScreen(
              tool = screen.toolId,
              currentLanguage = currentLanguage,
              isFavorite = favoriteTools.contains(screen.toolId),
              onToggleFavorite = {
                coroutineScope.launch {
                  repository.toggleFavorite(screen.toolId, favoriteTools.contains(screen.toolId))
                }
              },
              onBack = { currentScreen = ScreenDestination.Home }
            )
          }

          is ScreenDestination.Chatbot -> {
            ChatbotScreen(
              onBack = { currentScreen = ScreenDestination.Home }
            )
          }

          is ScreenDestination.AboutUs -> {
            AboutUsScreen(
              onBack = { currentScreen = ScreenDestination.Home }
            )
          }

          is ScreenDestination.ContactUs -> {
            ContactUsScreen(
              onBack = { currentScreen = ScreenDestination.Home }
            )
          }

          is ScreenDestination.PrivacyPolicy -> {
            PrivacyPolicyScreen(
              onBack = { currentScreen = ScreenDestination.Home }
            )
          }

          is ScreenDestination.TermsOfUse -> {
            TermsOfUseScreen(
              onBack = { currentScreen = ScreenDestination.Home }
            )
          }
        }
      }
    }
  }
}
