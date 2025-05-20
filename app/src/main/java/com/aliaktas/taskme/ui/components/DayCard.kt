package com.aliaktas.taskme.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable // YENİ IMPORT
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType // YENİ IMPORT
import androidx.compose.ui.platform.LocalHapticFeedback // YENİ IMPORT
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
// import com.aliaktas.taskme.ui.theme.PoppinsFamily // Kullanılmıyorsa kaldırılabilir, MaterialTheme.typography içinde tanımlı
import com.aliaktas.taskme.ui.theme.TaskMeTheme

@OptIn(ExperimentalFoundationApi::class) // YENİ ANNOTATION
@Composable
fun DayCard(
    dayName: String,
    taskCount: Int,
    isSelected: Boolean,
    onDayClick: () -> Unit,
    onLongClick: () -> Unit, // YENİ PARAMETRE
    modifier: Modifier = Modifier
) {
    val animatedContainerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, // [cite: 60]
        animationSpec = tween(durationMillis = 300)
    )
    val animatedContentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 300)
    )
    val animatedTaskCountBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
        animationSpec = tween(durationMillis = 300)
    )
    val animatedTaskCountTextColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary, // [cite: 61]
        animationSpec = tween(durationMillis = 300)
    )
    val haptic = LocalHapticFeedback.current // YENİ

    Card(
        modifier = modifier
            .width(90.dp)
            .height(120.dp)
            .combinedClickable( // MODIFIED
                onClick = { onDayClick() },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick()
                }
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp,
            pressedElevation = 4.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = animatedContainerColor // [cite: 63]
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround // [cite: 64]
        ) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = animatedContentColor,
                textAlign = TextAlign.Center // [cite: 65]
            )

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp)) // [cite: 66]
                    .background(animatedTaskCountBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = taskCount.toString(), // [cite: 67]
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = animatedTaskCountTextColor,
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
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DayCard(
                dayName = "Pzt",
                taskCount = 3,
                isSelected = false, // [cite: 69]
                onDayClick = {},
                onLongClick = {} // YENİ (Preview için)
            )
            DayCard(
                dayName = "Sal",
                taskCount = 5,
                isSelected = true, // [cite: 70]
                onDayClick = {},
                onLongClick = {} // YENİ (Preview için)
            )
            DayCard(
                dayName = "Çrş",
                taskCount = 0,
                isSelected = false, // [cite: 71]
                onDayClick = {},
                onLongClick = {} // YENİ (Preview için)
            )
        }
    }
}