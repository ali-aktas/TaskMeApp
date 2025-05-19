package com.aliaktas.taskme.ui.states

import com.aliaktas.taskme.domain.model.Day
import com.aliaktas.taskme.domain.model.Task

/**
 * Ana ekranın UI durumunu temsil eden veri sınıfı
 * ViewModel tarafından güncellenir ve UI'ya aktarılır
 *
 * @param days Tüm günlerin listesi
 * @param tasks Tüm görevlerin listesi
 * @param selectedDayId Seçili günün ID'si
 */
data class HomeUiState(
    val days: List<Day> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val selectedDayId: Long = 0,
) {
    // Seçili günü hesaplar
    val selectedDay: Day? = days.find { it.id == selectedDayId }

    // Seçili güne ait görevleri filtreler
    val filteredTasks: List<Task> = tasks.filter { it.dayId == selectedDayId }
}