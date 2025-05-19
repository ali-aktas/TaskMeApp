package com.aliaktas.taskme.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aliaktas.taskme.data.local.DayDao
import com.aliaktas.taskme.data.local.TaskDao
import com.aliaktas.taskme.data.local.DayEntity
import com.aliaktas.taskme.data.local.TaskEntity

/**
 * TaskMe uygulaması için Room veritabanı
 * Task ve Day entitylerini içerir
 */
@Database(
    entities = [TaskEntity::class, DayEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Task verilerine erişim sağlayan DAO'yu döndürür
     */
    abstract fun taskDao(): TaskDao

    /**
     * Day verilerine erişim sağlayan DAO'yu döndürür
     */
    abstract fun dayDao(): DayDao
}