package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DoubleScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.MinesScreen
import com.example.ui.screens.ProvablyFairScreen
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MinesAiTheme
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianStroke
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextMuted
import com.example.viewmodel.AssistantViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: AssistantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MinesAiTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: AssistantViewModel) {
    val context = LocalContext.current
    val selectedTab by viewModel.selectedTab.collectAsState()

    // Handle back button: if on sub-tab, return to Dashboard tab
    BackHandler(enabled = selectedTab != 0) {
        viewModel.selectTab(0)
    }

    // Android 13+ Notification Permission Launcher for Foreground Service
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val tabs = listOf(
        TabItem("HUD", Icons.Filled.Layers, Icons.Outlined.Layers),
        TabItem("Mines", Icons.Filled.GridOn, Icons.Outlined.GridOn),
        TabItem("Double", Icons.Filled.Casino, Icons.Outlined.Casino),
        TabItem("Fairness", Icons.Filled.Security, Icons.Outlined.Security),
        TabItem("History", Icons.Filled.History, Icons.Outlined.History)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = ObsidianBackground,
        bottomBar = {
            NavigationBar(
                containerColor = ObsidianSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .background(ObsidianSurface)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav")
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(index) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.filledIcon else tab.outlineIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBackground,
                            selectedTextColor = EmeraldPrimary,
                            indicatorColor = EmeraldPrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_$index")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> DashboardScreen(viewModel = viewModel)
                1 -> MinesScreen(viewModel = viewModel)
                2 -> DoubleScreen(viewModel = viewModel)
                3 -> ProvablyFairScreen(viewModel = viewModel)
                4 -> HistoryScreen(viewModel = viewModel)
            }
        }
    }
}

data class TabItem(
    val title: String,
    val filledIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val outlineIcon: androidx.compose.ui.graphics.vector.ImageVector
)
