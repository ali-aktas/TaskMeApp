package com.aliaktas.taskme.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aliaktas.taskme.domain.model.Day
import com.aliaktas.taskme.domain.model.Task
import com.aliaktas.taskme.ui.components.DayCard
import com.aliaktas.taskme.ui.components.TaskCard
import com.aliaktas.taskme.viewmodel.TaskViewModel

/**
 * Ana ekran composable bileşeni
 * Gün kartlarını, görevleri ve ekle butonunu içerir
 *
 * @param taskViewModel Görev ve gün verileri için ViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(taskViewModel: TaskViewModel) {
    // ViewModel'den UI state'ini alıyoruz
    val uiState by taskViewModel.uiState.collectAsState()

    // Ekle butonuna tıklandığında dialog göstermek için state
    var showAddTaskDialog by remember { mutableStateOf(false) }

    // Yeni görev metnini tutmak için state
    var newTaskText by remember { mutableStateOf("") }

    // Scaffold, Material Design temel ekran düzenini sağlar
    // TopBar, Content ve FloatingActionButton içerir
    Scaffold(
        // Üst çubuk
        topBar = {
            TopAppBar(
                title = { Text("TaskMe") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        // Ekranın sağ alt köşesinde görev ekle butonu
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Görev Ekle"
                )
            }
        }
    ) { paddingValues ->
        // Ana içerik - Column ile elemanları dikey olarak sıralıyoruz
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Gün kartları - LazyRow ile yatay kaydırma sağlıyoruz (RecyclerView horizontal)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(uiState.days) { day ->
                    DayCard(
                        dayName = day.name,
                        taskCount = uiState.tasks.count { it.dayId == day.id },
                        isSelected = day.id == uiState.selectedDayId,
                        onDayClick = { taskViewModel.selectDay(day.id) }
                    )
                }
            }

            // Seçili günün başlığı
            Text(
                text = uiState.selectedDay?.name ?: "Görevler",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )

            // Seçili güne ait görevler - LazyColumn ile dikey kaydırma (RecyclerView vertical)
            if (uiState.filteredTasks.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.filteredTasks) { task ->
                        TaskCard(
                            taskTitle = task.title,
                            isCompleted = task.isCompleted,
                            onTaskClick = { /* Task detayları için gelecekte kullanılacak */ },
                            onCheckClick = { taskViewModel.toggleTaskCompletion(task.id) }
                        )
                    }
                }
            } else {
                // Görev yoksa boş durum mesajı
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    Text(
                        text = "Bu gün için görev yok. Yeni görev ekleyin!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Görev ekleme dialog'u
        if (showAddTaskDialog) {
            AlertDialog(
                onDismissRequest = { showAddTaskDialog = false },
                title = { Text("Yeni Görev Ekle") },
                text = {
                    // Dialog içeriği - TextField ile görev adı girişi
                    OutlinedTextField(
                        value = newTaskText,
                        onValueChange = { newTaskText = it },
                        label = { Text("Görev Adı") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    // Onay butonu - Görevi ekler
                    Button(
                        onClick = {
                            if (newTaskText.isNotBlank()) {
                                taskViewModel.addTask(newTaskText, uiState.selectedDayId)
                                newTaskText = ""
                                showAddTaskDialog = false
                            }
                        }
                    ) {
                        Text("Ekle")
                    }
                },
                dismissButton = {
                    // İptal butonu
                    TextButton(
                        onClick = { showAddTaskDialog = false }
                    ) {
                        Text("İptal")
                    }
                }
            )
        }
    }
}