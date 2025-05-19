package com.aliaktas.taskme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aliaktas.taskme.ui.theme.TaskCompletedColor
import com.aliaktas.taskme.ui.theme.TaskMeTheme
import com.aliaktas.taskme.ui.theme.TaskPendingColor

/**
 * Görev kartı bileşeni
 *
 * @param taskTitle Görevin başlığı
 * @param isCompleted Görevin tamamlanma durumu
 * @param onTaskClick Görev kartına tıklandığında çalışacak fonksiyon
 * @param onCheckClick Görevin tamamlanma durumunu değiştiren checkbox'a tıklandığında çalışacak fonksiyon
 */
@Composable
fun TaskCard(
    taskTitle: String,
    isCompleted: Boolean,
    onTaskClick: () -> Unit,
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Card, MaterialDesign kart bileşenidir - CardView'in Compose karşılığı
    Card(
        // Kartın görünümü ve davranışı için Modifier kullanılır
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onTaskClick() },
        // Kart özellikleri
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        // Row, LinearLayout horizontal orientation'ın Compose karşılığı
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Görev metni
            Text(
                text = taskTitle,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isCompleted) TaskCompletedColor else TaskPendingColor,
                textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                // Text bileşeni, Row içinde ağırlığı 1f olarak ayarlanır
                // böylece mümkün olduğunca yer kaplar ama checkbox'a da yer bırakır
                modifier = Modifier.weight(1f)
            )

            // Tamamlama kutucuğu (checkbox)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isCompleted) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable { onCheckClick() },
                contentAlignment = Alignment.Center
            ) {
                // Eğer görev tamamlandıysa tik işareti göster
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Tamamlandı",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// Preview fonksiyonu, tasarım sırasında nasıl görüneceğini gösterir
@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    TaskMeTheme {
        Column {
            TaskCard(
                taskTitle = "Projeyi bitir",
                isCompleted = false,
                onTaskClick = {},
                onCheckClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            TaskCard(
                taskTitle = "E-postaları yanıtla",
                isCompleted = true,
                onTaskClick = {},
                onCheckClick = {}
            )
        }
    }
}