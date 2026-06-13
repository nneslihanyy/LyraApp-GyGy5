package com.turkcell.lyraapp.ui.register

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

// Kural: mvi-viewmodel-rules.md dosyasindaki standartlara uygun olarak yazilmistir.
@HiltViewModel
class RegisterViewModel @Inject constructor(
    // Bagımlılıklar (UseCase vb.) ilerleyen aşamalarda buraya enjekte edilecektir.
) : ViewModel() {

    // ── State ─────────────────────────────────────────────────────────────
    // MutableStateFlow daima private; dışa StateFlow olarak açılır.
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    // ── Effect ────────────────────────────────────────────────────────────
    // Channel.BUFFERED: UI hazır olmasa bile olaylar kaybolmaz.
    private val _effect = Channel<RegisterUiEffect>(Channel.BUFFERED)
    val effect: Flow<RegisterUiEffect> = _effect.receiveAsFlow()

    // ── Intent İşleme ────────────────────────────────────────────────────
    // Tüm kullanıcı niyetleri tek bir giriş noktasından geçer.
    fun onIntent(intent: RegisterUiIntent) {
        when (intent) {
            is RegisterUiIntent.FirstNameChanged    -> handleFirstNameChanged(intent.value)
            is RegisterUiIntent.LastNameChanged     -> handleLastNameChanged(intent.value)
            is RegisterUiIntent.PhoneNumberChanged  -> handlePhoneNumberChanged(intent.value)
            is RegisterUiIntent.PasswordChanged     -> handlePasswordChanged(intent.value)
            RegisterUiIntent.TogglePasswordVisibility -> handleTogglePasswordVisibility()
            RegisterUiIntent.ToggleTermsAccepted    -> handleToggleTermsAccepted()
            RegisterUiIntent.RegisterClicked        -> handleRegister()
            RegisterUiIntent.LoginClicked           -> sendEffect(RegisterUiEffect.NavigateToLogin)
        }
    }

    // ── Private Handlers ─────────────────────────────────────────────────
    private fun handleFirstNameChanged(value: String) {
        _state.update { it.copy(firstName = value, errorMessage = null) }
    }

    private fun handleLastNameChanged(value: String) {
        _state.update { it.copy(lastName = value, errorMessage = null) }
    }

    private fun handlePhoneNumberChanged(value: String) {
        _state.update { it.copy(phoneNumber = value, errorMessage = null) }
    }

    private fun handlePasswordChanged(value: String) {
        _state.update { it.copy(password = value, errorMessage = null) }
    }

    private fun handleTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    private fun handleToggleTermsAccepted() {
        _state.update { it.copy(isTermsAccepted = !it.isTermsAccepted) }
    }

    private fun handleRegister() {
        val currentState = _state.value

        // Form doğrulaması: tüm alanlar dolu olmalıdır.
        if (currentState.firstName.isBlank() ||
            currentState.lastName.isBlank() ||
            currentState.phoneNumber.isBlank() ||
            currentState.password.isBlank()
        ) {
            _state.update { it.copy(errorMessage = "Tüm alanları doldurunuz.") }
            return
        }

        // Şifre doğrulaması: en az 8 karakter ve bir rakam içermeli.
        // Kural: Tasarım görseli "En az 8 karakter, bir rakam içermeli." ibaresini içermektedir.
        if (currentState.password.length < 8 || currentState.password.none { it.isDigit() }) {
            _state.update {
                it.copy(errorMessage = "Şifre en az 8 karakter olmalı ve bir rakam içermelidir.")
            }
            return
        }

        // Kullanım Koşulları kabul edilmeli.
        if (!currentState.isTermsAccepted) {
            _state.update { it.copy(errorMessage = "Kullanım Koşullarını kabul etmeniz gerekmektedir.") }
            return
        }

        // Yükleme durumu başlatılır.
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        // Not: Gerçek network çağrısı UseCase/Repository katmanından gelecektir.
        // Bu aşamada iş mantığı simüle edilmektedir.
        viewModelScope.launch {
            // Simüle edilmiş gecikme — ilerleyen aşamada UseCase ile değiştirilecektir.
            kotlinx.coroutines.delay(1500)
            _state.update { it.copy(isLoading = false) }
            sendEffect(RegisterUiEffect.NavigateToHome)
        }
    }

    // ── Effect Gönderimi ─────────────────────────────────────────────────
    private fun sendEffect(effect: RegisterUiEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
