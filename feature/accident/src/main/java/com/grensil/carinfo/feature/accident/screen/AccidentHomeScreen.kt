package com.grensil.carinfo.feature.accident.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grensil.carinfo.core.common.UiState
import com.grensil.carinfo.domain.model.Accident
import com.grensil.carinfo.feature.accident.viewmodel.AccidentHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccidentHomeScreen(
    onNavigateToReport: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
) {
    val viewModel: AccidentHomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("사고") }) },
    ) { padding ->
        when (val state = uiState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator() }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) { Text(state.message) }
            }

            is UiState.Success -> {
                AccidentListContent(
                    accidents = state.data,
                    onNavigateToReport = onNavigateToReport,
                    onNavigateToDetail = onNavigateToDetail,
                    modifier = Modifier.fillMaxSize().padding(padding),
                )
            }
        }
    }
}

@Composable
private fun AccidentListContent(
    accidents: List<Accident>,
    onNavigateToReport: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        item {
            Text(
                text = "사고 이력",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp),
            )
        }
        items(accidents) { accident ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onNavigateToDetail(accident.id) },
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(accident.description, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "${accident.date} · ${accident.location}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "사고 접수하기 →",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onNavigateToReport() },
            )
        }
    }
}
