package com.electrofire.playpkm.ui.Components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.Data.UserData
import com.electrofire.playpkm.R

@Composable
fun UserDetailDialog(user: UserData, onDismiss: () -> Unit) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Image(
                    painter = rememberAsyncImagePainter(user.imagen),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(3.dp)
                        .border(3.dp, MaterialTheme.colorScheme.onSecondary, CircleShape)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre
                Box {
                    Text(
                        text = user.userName?.uppercase() ?: "ENTRENADOR",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 26.sp,
                            color = MaterialTheme.colorScheme.primary,
                            drawStyle = Stroke(width = 4f)
                        )
                    )
                    Text(
                        text = user.userName?.uppercase() ?: "ENTRENADOR",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 26.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Principales
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(label = "VICTORIAS", value = user.victorias.toString(), color = MaterialTheme.colorScheme.outline)

                    // RACHA
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = null,
                            tint = if (user.rachaActual > 0) Color(0xFFFF9800) else Color.Gray,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = "${user.rachaActual} DÍAS",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (user.rachaActual > 0) Color(0xFFFF9800) else Color.Gray
                        )
                    }

                    StatItem(label = "DERROTAS", value = user.derrotas.toString(), color = MaterialTheme.colorScheme.onSurface)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Records Minijuegos
                Text(
                    text = "RECORDS MÁXIMOS",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MiniGameStat(label = "GC", value = user.maxPoints)
                    MiniGameStat(label = "TS", value = user.maxPointsDos)
                    MiniGameStat(label = "BA", value = user.maxPointsTres)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Instagram (si existe)
                if (!user.instagram.isNullOrBlank()) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/${user.instagram}/"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE1306C)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(45.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.instagram),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("VER INSTAGRAM", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Botón Cerrar
                Button(
                    onClick = onDismiss,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.width(150.dp)
                ) {
                    Text("VOLVER", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.headlineMedium, color = color)
    }
}

@Composable
fun MiniGameStat(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        Text(text = value.toString(), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
    }
}
