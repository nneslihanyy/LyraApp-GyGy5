package com.turkcell.lyraapp.ui.login

// Kural: mvi-contracts.md dosyasındaki standartlara uygun olarak yazılmıştır.

// ── State ──────────────────────────────────────────────────────────────────
// Ekranın anlık, kalıcı görünüm durumunu temsil eder.
// Tüm alanlar varsayılan değere sahip olmalıdır; böylece LoginUiState() ile
// boş başlangıç durumu elde edilebilir.
data class LoginUiState(
    val phoneNumber: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

// ── Intent ─────────────────────────────────────────────────────────────────
// Kullanıcının ekranla gerçekleştirdiği her eylemi temsil eder.
// Parametre taşıyanlar data class, parametresizler data object kullanır.
sealed interface LoginUiIntent {
    data class PhoneNumberChanged(val value: String) : LoginUiIntent
    data class PasswordChanged(val value: String) : LoginUiIntent
    data object TogglePasswordVisibility : LoginUiIntent
    data object LoginClicked : LoginUiIntent
    data object ForgotPasswordClicked : LoginUiIntent
    data object RegisterClicked : LoginUiIntent
}

// ── Effect ─────────────────────────────────────────────────────────────────
// Yalnızca tek seferlik olaylar için kullanılır: navigasyon, hata mesajı vb.
// UiState içine konamaz; Channel aracılığıyla iletilir.
sealed interface LoginUiEffect {
    data object NavigateToHome : LoginUiEffect
    data object NavigateToRegister : LoginUiEffect
    data object NavigateToForgotPassword : LoginUiEffect
    data class ShowError(val message: String) : LoginUiEffect
}
