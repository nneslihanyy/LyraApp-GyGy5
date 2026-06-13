package com.turkcell.lyraapp.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turkcell.lyraapp.ui.theme.LyraAppTheme

@Composable
fun LyraLogo(modifier: Modifier = Modifier) {
    val isDark = isSystemInDarkTheme()
    
    // Tasarıma uygun renk geçişi (gradient)
    val gradientBrush = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.tertiary, // Peach
                MaterialTheme.colorScheme.primary   // Pink
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary, // Dusty Rose
                Color(0xFF5E1133)                  // Koyu Kırmızı/Pembe tonu
            )
        )
    }

    val barColor = if (isDark) {
        MaterialTheme.colorScheme.background // Koyu Tema Arka Planı
    } else {
        Color.White
    }

    Box(
        modifier = modifier
            .size(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Soundwave dikey barlar
            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 10.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(barColor)
            )
            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 18.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(barColor)
            )
            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 26.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(barColor)
            )
            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 18.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(barColor)
            )
            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 10.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(barColor)
            )
        }
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
) {
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val isFormValid = phoneNumber.isNotEmpty() && password.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        // Logo çizimi
        LyraLogo()
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Başlık
        Text(
            text = "Tekrar hoş geldin",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Alt başlık
        Text(
            text = "Hesabına giriş yap, kaldığın yerden dinlemeye devam et.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Telefon Numarası Giriş Alanı
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { input ->
                // Sadece rakam girişine izin verilmesi
                if (input.all { it.isDigit() }) {
                    phoneNumber = input
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Telefon numarası") },
            placeholder = { Text("5XX XXX XX XX") },
            prefix = {
                Text(
                    text = "+90 ",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Smartphone,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Şifre Giriş Alanı
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Şifre") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Şifreyi gizle" else "Şifreyi göster"
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Şifremi Unuttum Bağlantısı
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Şifremi unuttum",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    // Şifre sıfırlama akışı tetiklenecektir.
                }
            )
        }
        
        Spacer(modifier = Modifier.height(36.dp))
        
        // Giriş Yap Butonu
        Button(
            onClick = {
                // Giriş akışı tetiklenecektir.
            },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                disabledContentColor = MaterialTheme.colorScheme.outline
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Giriş yap",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Alt Bilgi / Kayıt Ol Bağlantısı
        val footerText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                append("Hesabın yok mu? ")
            }
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Kayıt ol")
            }
        }
        
        Text(
            text = footerText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Kayıt ol ekranına yönlendirme yapılacaktır.
                }
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenDarkPreview() {
    LyraAppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoginScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLightPreview() {
    LyraAppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoginScreen()
        }
    }
}
