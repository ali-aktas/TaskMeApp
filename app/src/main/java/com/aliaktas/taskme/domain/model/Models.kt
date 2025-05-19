package com.aliaktas.taskme.domain.model

/**
 * Görev veri sınıfı - UI katmanında kullanılır
 *
 * @param id Görevi tanımlayan benzersiz id
 * @param title Görevin başlığı
 * @param dayId Görevin hangi güne ait olduğu
 * @param isCompleted Görevin tamamlanma durumu
 */
data class Task(
    val id: Long = 0,
    val title: String,
    val dayId: Long,
    val isCompleted: Boolean = false
)

/**
 * Gün veri sınıfı - UI katmanında kullanılır
 *
 * @param id Günü tanımlayan benzersiz id
 * @param name Günün adı (Pazartesi, 1. Gün vb.)
 * @param order Günün sıralama numarası (0-6)
 */
data class Day(
    val id: Long = 0,
    val name: String,
    val order: Int
)