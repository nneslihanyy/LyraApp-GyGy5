package com.turkcell.lyraapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Kural: mvi-viewmodel-rules.md dosyasındaki standartlara uygun olarak yazılmıştır.
@HiltViewModel
class LoginViewModel @Inject constructor(
    // Bağımlılıklar (UseCase vb.) ilerleyen aşamalarda buraya enjekte edilecektir.
) : ViewModel() {

    // ── State ─────────────────────────────────────────────────────────────
    // MutableStateFlow daima private; dışa StateFlow olarak açılır.
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    // ── Effect ────────────────────────────────────────────────────────────
    // Channel.BUFFERED: UI hazır olmasa bile olaylar kaybolmaz.
    private val _effect = Channel<LoginUiEffect>(Channel.BUFFERED)
    val effect: Flow<LoginUiEffect> = _effect.receiveAsFlow()

    // ── Intent İşleme ────────────────────────────────────────────────────
    // Tüm kullanıcı niyetleri tek bir giriş noktasından geçer.
    fun onIntent(intent: LoginUiIntent) {
        when (intent) {
            is LoginUiIntent.PhoneNumberChanged    -> handlePhoneNumberChanged(intent.value)
            is LoginUiIntent.PasswordChanged       -> handlePasswordChanged(intent.value)
            LoginUiIntent.TogglePasswordVisibility -> handleTogglePasswordVisibility()
            LoginUiIntent.LoginClicked             -> handleLogin()
            LoginUiIntent.ForgotPasswordClicked    -> sendEffect(LoginUiEffect.NavigateToForgotPassword)
            LoginUiIntent.RegisterClicked          -> sendEffect(LoginUiEffect.NavigateToRegister)
        }
    }

    // ── Private Handlers ─────────────────────────────────────────────────
    private fun handlePhoneNumberChanged(value: String) {
        _state.update { it.copy(phoneNumber = value, errorMessage = null) }
    }

    private fun handlePasswordChanged(value: String) {
        _state.update { it.copy(password = value, errorMessage = null) }
    }

    private fun handleTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    private fun handleLogin() {
        val currentState = _state.value

        // Temel form doğrulaması (validasyon)
        if (currentState.phoneNumber.isBlank() || currentState.password.isBlank()) {
            _state.update { it.copy(errorMessage = "Telefon numarası ve şifre boş olamaz.") }
            return
        }

        // Yükleme durumu başlatılır.
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        // Not: Gerçek network çağrısı UseCase/Repository katmanından gelecektir.
        // Bu aşamada iş mantığı simüle edilmektedir.
        viewModelScope.launch {
            // Simüle edilmiş gecikme — ileride UseCase ile değiştirilecektir.
            kotlinx.coroutines.delay(1500)
            _state.update { it.copy(isLoading = false) }
            sendEffect(LoginUiEffect.NavigateToHome)
        }
    }

    // ── Effect Gönderimi ─────────────────────────────────────────────────
    private fun sendEffect(effect: LoginUiEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
