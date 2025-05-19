package com.aliaktas.taskme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aliaktas.taskme.ui.theme.TaskMeTheme

/**
 * Gün kartı bileşeni
 *
 * @param dayName Gün adı (Pazartesi, 1. Gün vb.)
 * @param taskCount Gündeki görev sayısı
 * @param isSelected Günün seçili olup olmadığı
 * @param onDayClick Gün kartına tıklandığında çalışacak fonksiyon
 */
@Composable
fun DayCard(
    dayName: String,
    taskCount: Int,
    isSelected: Boolean,
    onDayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(100.dp)
            .padding(horizontal = 4.dp)
            .clickable { onDayClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        // Column, LinearLayout vertical orientation'ın Compose karşılığı
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Gün adı
            Text(
                text = dayName,
                style = MaterialTheme.typography.titleLarge,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Görev sayısı göstergesi - yuvarlak bir işaret
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected)
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = taskCount.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected)
                        Color.White
                    else
                        MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DayCardPreview() {
    TaskMeTheme {
        Row {
            DayCard(
                dayName = "Pzt",
                taskCount = 3,
                isSelected = false,
                onDayClick = {}
            )
            DayCard(
                dayName = "Sal",
                taskCount = 5,
                isSelected = true,
                onDayClick = {}
            )
        }
    }
}