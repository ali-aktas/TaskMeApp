package com.aliaktas.taskme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.aliaktas.taskme.ui.screen.HomeScreen
import com.aliaktas.taskme.ui.theme.TaskMeTheme
import com.aliaktas.taskme.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Uygulama ana aktivitesi
 * Compose tabanlı UI'ı başlatır ve gerekli ViewModel'leri dahil eder
 */
@AndroidEntryPoint // Hilt DI için gerekli annotation
class MainActivity : ComponentActivity() {

    // ViewModel'i Hilt ile enjekte ediyoruz
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Compose içeriğini ayarlıyoruz
        setContent {
            // Temayı tüm uygulamaya uyguluyoruz
            TaskMeTheme {
                // Ana yüzey, tüm ekranı kaplar ve tema renklerini kullanır
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Ana ekranı gösteriyoruz ve ViewModel'i veriyoruz
                    HomeScreen(taskViewModel = taskViewModel)
                }
            }
        }
    }
}