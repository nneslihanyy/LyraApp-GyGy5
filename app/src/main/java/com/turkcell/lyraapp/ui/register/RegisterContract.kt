package com.turkcell.lyraapp.ui.register

// Kural: mvi-contracts.md dosyasindaki standartlara uygun olarak yazilmistir.

// ── State ──────────────────────────────────────────────────────────────────
// Eкranın anlık, kalıcı görünüm durumunu temsil eder.
// Tüm alanlar varsayılan değere sahip olmalıdır; böylece RegisterUiState()
// ile boş başlangıç durumu elde edilebilir.
data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isTermsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

// ── Intent ─────────────────────────────────────────────────────────────────
// Kullanıcının ekranla gerçekleştirdiği her eylemi temsil eder.
// Parametre taşıyanlar data class, parametresizler data object kullanır.
sealed interface RegisterUiIntent {
    data class FirstNameChanged(val value: String)   : RegisterUiIntent
    data class LastNameChanged(val value: String)    : RegisterUiIntent
    data class PhoneNumberChanged(val value: String) : RegisterUiIntent
    data class PasswordChanged(val value: String)    : RegisterUiIntent
    data object TogglePasswordVisibility             : RegisterUiIntent
    data object ToggleTermsAccepted                  : RegisterUiIntent
    data object RegisterClicked                      : RegisterUiIntent
    data object LoginClicked                         : RegisterUiIntent
}

// ── Effect ─────────────────────────────────────────────────────────────────
// Yalnızca tek seferlik olaylar için kullanılır: navigasyon, hata mesajı vb.
// UiState içine konamaz; Channel aracılığıyla iletilir.
sealed interface RegisterUiEffect {
    data object NavigateToHome              : RegisterUiEffect
    data object NavigateToLogin             : RegisterUiEffect
    data class ShowError(val message: String) : RegisterUiEffect
}
