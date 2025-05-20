package com.aliaktas.taskme.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete // YENİ IMPORT
import androidx.compose.material.icons.filled.Edit // YENİ IMPORT
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// import com.aliaktas.taskme.domain.model.Day // Kullanılmıyor [cite: 1]
// import com.aliaktas.taskme.domain.model.Task // Kullanılmıyor [cite: 1]
import com.aliaktas.taskme.ui.components.DayCard
import com.aliaktas.taskme.ui.components.TaskCard
import com.aliaktas.taskme.ui.theme.TextSecondaryLight
import com.aliaktas.taskme.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(taskViewModel: TaskViewModel) {
    val uiState by taskViewModel.uiState.collectAsState()
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var newTaskText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = { // [cite: 2]
            FloatingActionButton(
                onClick = { showAddTaskDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon( // [cite: 3]
                    imageVector = Icons.Default.Add,
                    contentDescription = "Görev Ekle"
                )
            }
        }
    ) { paddingValues -> // [cite: 4]
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp)
        ) {
            Text( // [cite: 5]
                text = "TaskMe",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier // [cite: 6]
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            LazyRow( // [cite: 7]
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp) // [cite: 8]
            ) {
                items(uiState.days) { day ->
                    DayCard(
                        dayName = day.name,
                        taskCount = uiState.tasks.count { it.dayId == day.id && !it.isCompleted }, // [cite: 9]
                        isSelected = day.id == uiState.selectedDayId,
                        onDayClick = { taskViewModel.selectDay(day.id) },
                        onLongClick = { taskViewModel.onDayCardLongPressed(day.id) } // YENİ
                    )
                }
            }

            val selectedDayName = uiState.selectedDay?.name ?: "Görevler" // [cite: 11]
            Text(
                text = selectedDayName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier // [cite: 11, 12]
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                textAlign = TextAlign.Center
            )

            if (uiState.filteredTasks.isNotEmpty()) {
                LazyColumn( // [cite: 13]
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp), // [cite: 14]
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = uiState.filteredTasks, // [cite: 15]
                        key = { task -> task.id }
                    ) { task ->
                        AnimatedVisibility( // [cite: 16]
                            visible = true,
                            enter = fadeIn(animationSpec = tween(300)) + slideInVertically( // [cite: 17]
                                animationSpec = tween(300),
                                initialOffsetY = { it / 2 }
                            ),
                            exit = fadeOut(animationSpec = tween(300)) // [cite: 18]
                        ) {
                            TaskCard( // [cite: 19]
                                taskTitle = task.title,
                                isCompleted = task.isCompleted,
                                onTaskClick = { /* Detay ekranı için gelecekte */ }, // [cite: 20]
                                onCheckClick = { taskViewModel.toggleTaskCompletion(task.id) },
                                onLongClick = { taskViewModel.onTaskLongPressed(task) } // YENİ
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier // [cite: 21, 22]
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text( // [cite: 23]
                        text = "Harika! Bu gün için hiç görevin yok.\nYeni bir görev ekleyerek gününü planla.", // [cite: 24]
                        style = MaterialTheme.typography.bodyLarge.copy(color = TextSecondaryLight),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth() // [cite: 24]
                    )
                }
            }
        }

        // --- YENİ DIALOGLAR ---

        // 1. Görev Eylem Seçim Dialogu
        if (uiState.showTaskActionDialog && uiState.taskToTakeActionOn != null) {
            AlertDialog(
                onDismissRequest = { taskViewModel.dismissTaskActionDialog() },
                title = { Text("Görev: ${uiState.taskToTakeActionOn?.title}", style = MaterialTheme.typography.titleMedium) },
                text = { Text("Bu görev için ne yapmak istersin?") },
                confirmButton = {
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        TextButton(onClick = { taskViewModel.requestEditTask() }) {
                            Icon(Icons.Default.Edit, contentDescription = "Düzenle", modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Düzenle")
                        }
                        Spacer(Modifier.width(8.dp))
                        TextButton(onClick = { taskViewModel.requestDeleteTask() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Sil", modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Sil", color = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { taskViewModel.dismissTaskActionDialog() }) {
                        Text("İptal")
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        // 2. Görev Silme Onay Dialogu
        if (uiState.showDeleteTaskConfirmDialog && uiState.taskToTakeActionOn != null) {
            AlertDialog(
                onDismissRequest = { taskViewModel.dismissDeleteTaskConfirmDialog() },
                icon = { Icon(Icons.Default.Delete, contentDescription = "Silme Onayı", tint = MaterialTheme.colorScheme.error) },
                title = { Text("Görevi Sil") },
                text = { Text("'${uiState.taskToTakeActionOn?.title}' başlıklı görevi silmek istediğinizden emin misiniz? Bu işlem geri alınamaz.") },
                confirmButton = {
                    Button(
                        onClick = { taskViewModel.confirmDeleteTask() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Sil")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { taskViewModel.dismissDeleteTaskConfirmDialog() }) {
                        Text("İptal")
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        // 3. Görev Düzenleme Dialogu
        if (uiState.showEditTaskDialog && uiState.taskToTakeActionOn != null) {
            // Düzenleme dialogu için geçici metin state'i HomeScreen içinde tutulabilir veya ViewModel'den alınabilir.
            // ViewModel'deki uiState.taskToEditText kullanılıyor.
            AlertDialog(
                onDismissRequest = { taskViewModel.dismissEditTaskDialog() },
                title = { Text("Görevi Düzenle") },
                text = {
                    OutlinedTextField(
                        value = uiState.taskToEditText, // ViewModel'den gelen değer
                        onValueChange = { taskViewModel.onEditTaskTextChanged(it) }, // ViewModel'i güncelle
                        label = { Text("Yeni görev başlığı") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (uiState.taskToEditText.isNotBlank()) { // Boş görev adı engelleme
                            taskViewModel.confirmEditTask()
                        }
                    }) {
                        Text("Kaydet")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { taskViewModel.dismissEditTaskDialog() }) {
                        Text("İptal")
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        // 4. Günün Tüm Görevlerini Silme Onay Dialogu
        if (uiState.showClearDayTasksConfirmDialog && uiState.dayIdToClearTasks != null) {
            val dayName = uiState.days.find { it.id == uiState.dayIdToClearTasks }?.name ?: "bu günün"
            AlertDialog(
                onDismissRequest = { taskViewModel.dismissClearDayTasksConfirmDialog() },
                icon = { Icon(Icons.Default.Delete, contentDescription = "Toplu Silme Onayı", tint = MaterialTheme.colorScheme.error) },
                title = { Text("Tüm Görevleri Temizle") },
                text = { Text("'$dayName' için tüm görevleri silmek istediğinizden emin misiniz? Bu işlem geri alınamaz.") },
                confirmButton = {
                    Button(
                        onClick = { taskViewModel.confirmClearDayTasks() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Hepsini Sil")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { taskViewModel.dismissClearDayTasksConfirmDialog() }) {
                        Text("İptal")
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        // Mevcut Görev Ekleme Dialogu
        AnimatedVisibility(
            visible = showAddTaskDialog,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)) + scaleIn(animationSpec = tween(durationMillis = 300), initialScale = 0.8f), // [cite: 26]
            exit = fadeOut(animationSpec = tween(durationMillis = 200)) + scaleOut(animationSpec = tween(durationMillis = 200), targetScale = 0.8f)
        ) {
            AlertDialog(
                onDismissRequest = { showAddTaskDialog = false },
                title = { // [cite: 27]
                    Text(
                        "Yeni Görev Ekle",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                text = {
                    OutlinedTextField(
                        value = newTaskText,
                        onValueChange = { newTaskText = it }, // [cite: 29]
                        label = { Text("Görev başlığı...", style = MaterialTheme.typography.bodyMedium) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp), // [cite: 30]
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline, // [cite: 31]
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                },
                confirmButton = { // [cite: 32]
                    Button(
                        onClick = {
                            if (newTaskText.isNotBlank() && uiState.selectedDayId != 0L) {
                                taskViewModel.addTask(newTaskText, uiState.selectedDayId) // [cite: 33]
                                newTaskText = ""
                                coroutineScope.launch { // [cite: 34]
                                    showAddTaskDialog = false // [cite: 35]
                                }
                            } else if (uiState.selectedDayId == 0L) { // [cite: 35, 36]
                                // Kullanıcıya bir gün seçmesi gerektiğini belirten bir mesaj gösterilebilir
                            }
                        },
                        shape = RoundedCornerShape(8.dp), // [cite: 37]
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Ekle", style = MaterialTheme.typography.labelLarge) // [cite: 38]
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showAddTaskDialog = false }, // [cite: 39]
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("İptal", style = MaterialTheme.typography.labelLarge) // [cite: 40]
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp) // [cite: 40, 41]
            )
        }
    }
}