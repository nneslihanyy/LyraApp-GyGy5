# LyraApp — MVI ViewModel Kuralları

> Bu dosya, LyraApp projesindeki MVI ViewModel sınıflarının yazım standartlarını tanımlar.
> Bu dosya, ViewModel kuralları için **tek doğruluk kaynağıdır (single source of truth)**.

---

## 1. Zorunlu Anotasyon

Her MVI ViewModel sınıfı `@HiltViewModel` ile işaretlenmiş olmalı
ve yapıcı fonksiyonu (constructor) `@Inject` anotasyonu taşımalıdır.

```kotlin
@HiltViewModel
class <EkranAdi>ViewModel @Inject constructor(
    // Bağımlılıklar buraya enjekte edilir.
) : ViewModel()
```

---

## 2. Zorunlu Import Listesi

Aşağıdaki import'lar her ViewModel dosyasında bulunmak zorundadır:

```kotlin
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
```

---

## 3. State Yönetimi

- State tutmak için `MutableStateFlow` kullanılır; dışa `StateFlow` olarak açılır.
- `MutableStateFlow` daima `private` olmalıdır.
- İlk değer her zaman varsayılan `<EkranAdi>UiState()` olmalıdır.
- State güncellemesi için `_state.update { ... }` kullanılır. Doğrudan atama (`_state.value = ...`) yasaktır.

```kotlin
private val _state = MutableStateFlow(<EkranAdi>UiState())
val state: StateFlow<<EkranAdi>UiState> = _state.asStateFlow()
```

### State Güncelleme Kuralı
```kotlin
// DOGRU — Atomik güncelleme
_state.update { currentState ->
    currentState.copy(isLoading = true)
}

// YANLIS — Doğrudan atama yasaktır; race condition riski taşır.
_state.value = _state.value.copy(isLoading = true)
```

### Hata Durumunda errorMessage Temizleme Kuralı
Kullanıcı bir form alanını değiştirdiğinde `errorMessage` temizlenmelidir:

```kotlin
private fun handleFieldChanged(value: String) {
    _state.update { it.copy(fieldValue = value, errorMessage = null) }
}
```

---

## 4. Effect Yönetimi

- Tek seferlik olaylar için `Channel<UiEffect>` kullanılır.
- Kapasite `Channel.BUFFERED` olarak ayarlanır; bu sayede UI'ın hazır olmadığı anlarda olaylar kaybolmaz.
- Dışa `receiveAsFlow()` ile `Flow` olarak açılır.
- `sendEffect` yardımcı fonksiyonu zorunludur ve `viewModelScope` içinde çalışır.

```kotlin
private val _effect = Channel<<EkranAdi>UiEffect>(Channel.BUFFERED)
val effect: Flow<<EkranAdi>UiEffect> = _effect.receiveAsFlow()

private fun sendEffect(effect: <EkranAdi>UiEffect) {
    viewModelScope.launch {
        _effect.send(effect)
    }
}
```

---

## 5. Intent İşleme

- Tüm kullanıcı niyetleri tek bir `onIntent(intent: <EkranAdi>UiIntent)` fonksiyonundan geçer.
- Bu fonksiyon `public` olmalıdır; UI bu fonksiyonu `viewModel::onIntent` şeklinde iletir.
- Basit, tek satırlık intent'ler (örn. navigasyon effect'i) doğrudan `when` bloğu içinde çözümlenir.
- Karmaşık iş mantığı içeren intent'ler `private` handler fonksiyonlarına delege edilir.

```kotlin
fun onIntent(intent: <EkranAdi>UiIntent) {
    when (intent) {
        is <EkranAdi>UiIntent.FieldChanged   -> handleFieldChanged(intent.value)
        <EkranAdi>UiIntent.ActionClicked     -> handleAction()
        <EkranAdi>UiIntent.NavigateClicked   -> sendEffect(<EkranAdi>UiEffect.NavigateToX)
    }
}
```

### onIntent Aktarım Kuralı (UI → ViewModel)
UI katmanında `onIntent` her zaman referans (method reference) olarak iletilir:

```kotlin
// DOGRU
LoginScreenContent(
    uiState = uiState,
    onIntent = viewModel::onIntent,
)

// YANLIS
LoginScreenContent(
    uiState = uiState,
    onIntent = { intent -> viewModel.onIntent(intent) },
)
```

---

## 6. Coroutine Kullanım Kuralları

- Tüm asenkron işlemler `viewModelScope.launch { }` içinde gerçekleştirilir.
- `Dispatchers.IO` gerektiren işlemler UseCase/Repository katmanında yönetilir; ViewModel'de `withContext` kullanılmaz.
- `viewModelScope` dışında coroutine başlatılması yasaktır.

---

## 7. Bağımlılık Kuralları

- ViewModel, doğrudan Android framework sınıflarına (`Context`, `Activity`, `Fragment`) bağımlı olamaz.
- `Context` gerektiğinde `@ApplicationContext` anotasyonuyla enjekte edilir.
- UI bileşenlerine (Composable, View) doğrudan referans tutulamaz.

---

## 8. Tam ViewModel Şablonu

Aşağıdaki şablon, implementasyonda birebir kullanılan yapıyı yansıtır.
Dosyanın üstüne mutlaka `// Kural: mvi-viewmodel-rules.md dosyasındaki standartlara uygun olarak yazılmıştır.` yorumu eklenmelidir.

```kotlin
package com.turkcell.lyraapp.ui.<ekranadi>

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
class <EkranAdi>ViewModel @Inject constructor(
    // private val someUseCase: SomeUseCase
) : ViewModel() {

    // ── State ─────────────────────────────────────────────────────────────
    // MutableStateFlow daima private; dışa StateFlow olarak açılır.
    private val _state = MutableStateFlow(<EkranAdi>UiState())
    val state: StateFlow<<EkranAdi>UiState> = _state.asStateFlow()

    // ── Effect ────────────────────────────────────────────────────────────
    // Channel.BUFFERED: UI hazır olmasa bile olaylar kaybolmaz.
    private val _effect = Channel<<EkranAdi>UiEffect>(Channel.BUFFERED)
    val effect: Flow<<EkranAdi>UiEffect> = _effect.receiveAsFlow()

    // ── Intent İşleme ────────────────────────────────────────────────────
    // Tüm kullanıcı niyetleri tek bir giriş noktasından geçer.
    fun onIntent(intent: <EkranAdi>UiIntent) {
        when (intent) {
            // Her intent buradan ilgili private fonksiyona delege edilir.
        }
    }

    // ── Private Handlers ─────────────────────────────────────────────────
    // Her karmaşık intent için private bir handler yazılır.

    // ── Effect Gönderimi ─────────────────────────────────────────────────
    private fun sendEffect(effect: <EkranAdi>UiEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
```

---

## 9. Yasak Kullanımlar

| Yasak | Gerekçe |
| :--- | :--- |
| `LiveData` kullanmak | Proje genelinde `StateFlow` / `Flow` standardize edilmiştir. |
| `MutableStateFlow` dışa açmak | Dış katmanların state'i değiştirmesi engellenmelidir. |
| ViewModel içinde `remember`, `mutableStateOf` kullanmak | Bunlar yalnızca Composable kapsamına aittir. |
| `GlobalScope` kullanmak | Bellek sızıntısına (memory leak) yol açar. |
| `_state.value = ...` ile doğrudan atama yapmak | `update { }` fonksiyonu atomik güncelleme sağlar; doğrudan atama race condition riskine yol açar. |
| Form alanı değiştiğinde `errorMessage`'ı temizlememek | Eski hata mesajı ekranda kalmaya devam eder; kullanıcı deneyimini bozar. |
| `onIntent`'i lambda ile iletmek | `viewModel::onIntent` method reference her zaman tercih edilmelidir. |
