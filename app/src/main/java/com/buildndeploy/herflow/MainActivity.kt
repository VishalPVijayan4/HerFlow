package com.buildndeploy.herflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buildndeploy.herflow.presentation.dashboard.DashboardIntent
import com.buildndeploy.herflow.presentation.dashboard.DashboardRoute
import com.buildndeploy.herflow.presentation.dashboard.DashboardViewModel
import com.buildndeploy.herflow.ui.theme.HerFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HerFlowTheme {
                HerFlowApp()
            }
        }
    }
}

@Composable
private fun HerFlowApp(viewModel: DashboardViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    DashboardRoute(
        state = state,
        onIntent = viewModel::onIntent,
        onRefresh = { viewModel.onIntent(DashboardIntent.Refresh) }
    )
}

@Preview(showBackground = true)
@Composable
private fun DashboardPreview() {
    HerFlowTheme {
        DashboardRoute()
    }
}
