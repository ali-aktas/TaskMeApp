package com.aliaktas.taskme.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Görev veritabanı entity sınıfı - Room ile kullanılır
 *
 * @param id Görevi tanımlayan benzersiz id (otomatik oluşturulur)
 * @param title Görevin başlığı
 * @param dayId Görevin hangi güne ait olduğu
 * @param isCompleted Görevin tamamlanma durumu
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val dayId: Long,
    val isCompleted: Boolean = false
)

/**
 * Gün veritabanı entity sınıfı - Room ile kullanılır
 *
 * @param id Günü tanımlayan benzersiz id (otomatik oluşturulur)
 * @param name Günün adı (Pazartesi, 1. Gün vb.)
 * @param order Günün sıralama numarası (0-6)
 */
@Entity(tableName = "days")
data class DayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val order: Int
)