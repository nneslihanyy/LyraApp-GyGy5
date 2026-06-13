package com.turkcell.lyraapp.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.lyraapp.ui.theme.LyraAppTheme

// ── Ekran Bileşeni (Stateful) ──────────────────────────────────────────────
// ViewModel'e bağlı, lifecycle-aware katman.
// Navigasyon lambdalar dışarıdan enjekte edilir; ekran navigasyon
// kütüphanesine doğrudan bağımlı değildir.
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
) {
    // State: collectAsStateWithLifecycle lifecycle-aware olarak dinler.
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    // Effect: Tek seferlik olaylar LaunchedEffect ile toplanır.
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                RegisterUiEffect.NavigateToHome  -> onNavigateToHome()
                RegisterUiEffect.NavigateToLogin -> onNavigateToLogin()
                is RegisterUiEffect.ShowError    -> { /* Snackbar entegrasyonu ilerleyen aşamada eklenecektir. */ }
            }
        }
    }

    RegisterScreenContent(
        modifier = modifier,
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

// ── Saf İçerik Bileşeni (Stateless) ───────────────────────────────────────
// Bu Composable hiçbir ViewModel veya state yönetimi içermez;
// yalnızca uiState'i render eder ve intent'leri üste iletir.
// Önizleme (Preview) fonksiyonları bu bileşeni kullanır.
@Composable
private fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    uiState: RegisterUiState,
    onIntent: (RegisterUiIntent) -> Unit,
) {
    // Kural: Türetilmiş boolean değerler UiState'e dahil edilmez;
    // ScreenContent içinde yerel val olarak hesaplanır.
    val isFormValid = uiState.firstName.isNotBlank() &&
            uiState.lastName.isNotBlank() &&
            uiState.phoneNumber.isNotBlank() &&
            uiState.password.length >= 8 &&
            uiState.password.any { it.isDigit() } &&
            uiState.isTermsAccepted

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Spacer(modifier = Modifier.height(52.dp))

        // Geri Butonu
        IconButton(
            onClick = { onIntent(RegisterUiIntent.LoginClicked) },
            modifier = Modifier.size(40.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Geri",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Başlık
        Text(
            text = "Hesap oluştur",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Alt Başlık
        Text(
            text = "Birkaç adımda Lyra'ya katıl ve çalma listeni oluştur.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Ad — Soyad Satırı
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Ad Alanı
            OutlinedTextField(
                value = uiState.firstName,
                onValueChange = { onIntent(RegisterUiIntent.FirstNameChanged(it)) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ad") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )

            // Soyad Alanı
            OutlinedTextField(
                value = uiState.lastName,
                onValueChange = { onIntent(RegisterUiIntent.LastNameChanged(it)) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Soyad") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Telefon Numarası Alanı
        OutlinedTextField(
            value = uiState.phoneNumber,
            onValueChange = { input ->
                if (input.all { it.isDigit() }) {
                    onIntent(RegisterUiIntent.PhoneNumberChanged(input))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Telefon numarası") },
            placeholder = { Text("5XX XXX XX XX") },
            prefix = {
                Text(
                    text = "+90  ",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Smartphone,
                    contentDescription = null,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Şifre Alanı
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { onIntent(RegisterUiIntent.PasswordChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Şifre") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
                IconButton(onClick = { onIntent(RegisterUiIntent.TogglePasswordVisibility) }) {
                    Icon(
                        imageVector = if (uiState.isPasswordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        contentDescription = if (uiState.isPasswordVisible) "Şifreyi gizle"
                        else "Şifreyi göster",
                    )
                }
            },
            visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Şifre Hint Metni — Tasarım görselinden alınan açıklama satırı
        Text(
            text = "En az 8 karakter, bir rakam içermeli.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        // Genel Hata Mesajı
        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = uiState.errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Kullanım Koşulları Satırı
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = uiState.isTermsAccepted,
                onCheckedChange = { onIntent(RegisterUiIntent.ToggleTermsAccepted) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )

            Spacer(modifier = Modifier.width(4.dp))

            // Kullanım Koşulları — vurgulu metin
            val termsText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    append("Kullanım Koşulları")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                    append(" ve ")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    append("Gizlilik Politikası")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                    append("'nı okudum, kabul ediyorum.")
                }
            }

            Text(
                text = termsText,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable { onIntent(RegisterUiIntent.ToggleTermsAccepted) },
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Kayıt Ol Butonu
        Button(
            onClick = { onIntent(RegisterUiIntent.RegisterClicked) },
            enabled = isFormValid && !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                disabledContentColor = MaterialTheme.colorScheme.outline,
            ),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Kayıt ol",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer — Zaten hesabın var mı? Giriş yap
        val footerText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                append("Zaten hesabın var mı?  ")
            }
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append("Giriş yap")
            }
        }

        Text(
            text = footerText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onIntent(RegisterUiIntent.LoginClicked) }
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ── Önizlemeler ────────────────────────────────────────────────────────────
// Önizlemeler stateless RegisterScreenContent bileşenini kullanır;
// ViewModel bağımlılığı yoktur.
@Preview(showBackground = true)
@Composable
fun RegisterScreenDarkPreview() {
    LyraAppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            RegisterScreenContent(
                uiState = RegisterUiState(),
                onIntent = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenLightPreview() {
    LyraAppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            RegisterScreenContent(
                uiState = RegisterUiState(),
                onIntent = {},
            )
        }
    }
}
