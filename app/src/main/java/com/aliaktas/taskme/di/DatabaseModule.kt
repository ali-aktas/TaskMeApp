package com.aliaktas.taskme.di

import android.content.Context
import androidx.room.Room
import com.aliaktas.taskme.data.local.AppDatabase
import com.aliaktas.taskme.data.local.DayDao
import com.aliaktas.taskme.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Bu bağımlılıkların uygulama yaşam döngüsü boyunca tekil (singleton) olacağını belirtir
object DatabaseModule {

    @Provides
    @Singleton // AppDatabase'in tek bir örneğinin olmasını sağlar
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "taskme_database" // Veritabanı dosyasının adı
        ).build()
    }

    @Provides
    @Singleton // TaskDao'nun tek bir örneğinin olmasını sağlar
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao {
        // AppDatabase örneğini kullanarak TaskDao'yu elde et
        return appDatabase.taskDao() //
    }

    @Provides
    @Singleton // DayDao'nun tek bir örneğinin olmasını sağlar
    fun provideDayDao(appDatabase: AppDatabase): DayDao {
        // AppDatabase örneğini kullanarak DayDao'yu elde et
        return appDatabase.dayDao() //
    }
}