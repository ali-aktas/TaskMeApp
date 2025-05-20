package com.aliaktas.taskme.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable // YENİ IMPORT
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType // YENİ IMPORT
import androidx.compose.ui.platform.LocalHapticFeedback // YENİ IMPORT
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aliaktas.taskme.ui.theme.*

@OptIn(ExperimentalFoundationApi::class) // YENİ ANNOTATION
@Composable
fun TaskCard(
    taskTitle: String,
    isCompleted: Boolean,
    onTaskClick: () -> Unit,
    onCheckClick: () -> Unit,
    onLongClick: () -> Unit, // YENİ PARAMETRE
    modifier: Modifier = Modifier
) {
    val cardElevation by animateDpAsState(targetValue = if (isCompleted) 2.dp else 6.dp, label = "elevation")
    val textAlpha by animateFloatAsState(targetValue = if (isCompleted) 0.6f else 1f, label = "textAlpha") // [cite: 42]
    val titleColor by animateColorAsState(
        targetValue = if (isCompleted) TaskCompletedColor else MaterialTheme.colorScheme.onSurface,
        label = "titleColor"
    )
    val haptic = LocalHapticFeedback.current // YENİ

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            if (isCompleted) SurfaceLight.copy(alpha = 0.7f) else GradientStartBlue.copy(alpha = 0.8f),
            if (isCompleted) SurfaceLight.copy(alpha = 0.9f) else GradientEndBlue // [cite: 43]
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow( // [cite: 44]
                elevation = if (isCompleted) 2.dp else 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = if (isCompleted) Color.Transparent else GlowColorBlue.copy(alpha = 0.5f),
                ambientColor = if (isCompleted) Color.Transparent else GlowColorBlue.copy(alpha = 0.2f)
            )
            .combinedClickable( // MODIFIED
                onClick = { onTaskClick() },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick()
                }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = gradientBrush, shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 20.dp), // [cite: 46]
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // [cite: 47]
        ) {
            Text(
                text = taskTitle,
                style = MaterialTheme.typography.titleMedium,
                color = titleColor,
                textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None, // [cite: 47, 48]
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp) // [cite: 49]
                    .graphicsLayer(alpha = textAlpha) // [cite: 49]
            )

            val checkBgColor by animateColorAsState(
                targetValue = if (isCompleted) MaterialTheme.colorScheme.primary else Color.Transparent,
                label = "checkBg" // [cite: 50]
            )
            val checkBorderColor by animateColorAsState(
                targetValue = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                label = "checkBorder"
            )
            val checkIconColor by animateColorAsState(
                targetValue = if (isCompleted) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                label = "checkIcon" // [cite: 51]
            )

            Box(
                modifier = Modifier
                    .size(28.dp) // [cite: 52]
                    .clip(RoundedCornerShape(8.dp))
                    .background(checkBgColor)
                    .border(
                        width = 2.dp,
                        color = checkBorderColor, // [cite: 53]
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onCheckClick() },
                contentAlignment = Alignment.Center // [cite: 54]
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Tamamlandı", // [cite: 55]
                        tint = checkIconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4F8)
@Composable
fun TaskCardPreview() {
    TaskMeTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // [cite: 57]
        ) {
            TaskCard(
                taskTitle = "Yeni modern tasarımı Jetpack Compose ile entegre et",
                isCompleted = false,
                onTaskClick = {},
                onCheckClick = {},
                onLongClick = {} // YENİ (Preview için)
            )
            TaskCard(
                taskTitle = "E-postaları yanıtla ve toplantı notlarını düzenle",
                isCompleted = true,
                onTaskClick = {},
                onCheckClick = {},
                onLongClick = {} // YENİ (Preview için)
            )
        }
    }
}