package com.buildndeploy.herflow.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun HeaderWithAction(title: String, subtitle: String, action: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(subtitle, color = TextPrimary, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(10.dp))
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPink)
        ) {
            Icon(Icons.Rounded.Add, null, tint = Color.White)
            Text(action, modifier = Modifier.padding(start = 8.dp), color = Color.White)
        }
    }
}

@Composable
internal fun CardContainer(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
            content()
        }
    }
}

@Composable
internal fun FeatureTile(
    icon: ImageVector,
    title: String,
    description: String,
    badge: String,
    badgeColor: Color,
    onClick: () -> Unit = {}
) {
    CardContainer {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onClick() }) {
            Icon(icon, null, tint = BrandPink)
            Spacer(Modifier.weight(1f))
            Text(badge, color = badgeColor, fontWeight = FontWeight.Bold)
        }
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp), color = TextPrimary)
        Text(description, style = MaterialTheme.typography.bodyLarge, color = TextMuted)
    }
}

@Composable
internal fun FieldPill(text: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .background(Color(0xFFF2F2F5), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
        Spacer(Modifier.weight(1f))
        Icon(icon, null, tint = Color(0xFF6D7482), modifier = Modifier.size(18.dp))
    }
}

@Composable
internal fun SmallChip(label: String) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontWeight = FontWeight.Medium)
    }
}

@Composable
internal fun OutlinedAction(label: String, icon: ImageVector, tint: Color = Color(0xFF111927)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(16.dp))
        Text(label, modifier = Modifier.padding(start = 12.dp), color = tint, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
internal fun ToggleSwitch(enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .width(34.dp)
            .height(22.dp)
            .background(if (enabled) DarkAction else Color(0xFFD0D4DC), CircleShape)
            .clickable { onToggle(!enabled) }
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .align(if (enabled) Alignment.CenterEnd else Alignment.CenterStart)
                .size(18.dp)
                .background(Color.White, CircleShape)
        )
    }
}

@Composable
internal fun ToggleToast(modifier: Modifier = Modifier, message: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F9)),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E3E7))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(18.dp).background(Color(0xFF111217), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", color = Color.White, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = message,
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}
