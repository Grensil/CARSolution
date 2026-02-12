package com.grensil.carinfo.feature.vehiclespec.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grensil.carinfo.core.common.UiState
import com.grensil.carinfo.domain.model.VehicleSpec
import com.grensil.carinfo.feature.vehiclespec.viewmodel.VehicleSpecHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSpecHomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
) {
    val viewModel: VehicleSpecHomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    var vinInput by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("차량스펙") }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text("VIN 번호로 차량 조회", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = vinInput,
                    onValueChange = { vinInput = it },
                    label = { Text("VIN") },
                    placeholder = { Text("17자리 VIN 입력") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.searchVin(vinInput) },
                    enabled = vinInput.isNotBlank(),
                ) {
                    Text("조회")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            when (val state = uiState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) { CircularProgressIndicator() }
                }

                is UiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) { Text(state.message, color = MaterialTheme.colorScheme.error) }
                }

                is UiState.Success -> {
                    VehicleSpecCard(
                        spec = state.data,
                        onDetailClick = { onNavigateToDetail(state.data.vin) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onNavigateToSearch,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("제조사/모델 검색")
            }
        }
    }
}

@Composable
private fun VehicleSpecCard(
    spec: VehicleSpec,
    onDetailClick: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "${spec.year} ${spec.make} ${spec.model}",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            SpecRow("VIN", spec.vin)
            SpecRow("차체", spec.bodyClass)
            SpecRow("구동", spec.driveType)
            SpecRow("연료", spec.fuelType)
            SpecRow("엔진", "${spec.engineCylinders}기통 ${spec.displacementL}L ${spec.engineHp}hp")
            SpecRow("변속기", spec.transmissionStyle)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onDetailClick) {
                Text("상세 보기")
            }
        }
    }
}

@Composable
private fun SpecRow(
    label: String,
    value: String,
) {
    if (value.isNotBlank()) {
        Row(modifier = Modifier.padding(vertical = 2.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(60.dp),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
