# LyraApp — MVI Contract Kuralları

> Bu dosya, LyraApp projesindeki her ekran için zorunlu olan MVI Contract yapısının
> kurallarını ve şablonunu tanımlar.
> Bu dosya, Contract yazım standartları için **tek doğruluk kaynağıdır (single source of truth)**.

---

## 1. Temel Kural

Her ekranın `ui/<ekranAdi>/` paketi altında bir `<EkranAdi>Contract.kt` dosyası bulunmak **zorundadır**.
Bu dosya; `UiState`, `UiIntent` ve `UiEffect` olmak üzere üç yapıyı **tek dosyada** toplar.

---

## 2. UiState Kuralları

- `data class` olarak tanımlanır.
- Tüm alanlar `val` (salt okunur) olmalıdır.
- Zorunlu alanlar (her UiState'de bulunmak zorundadır):
  - `val isLoading: Boolean = false` → Yükleme göstergesi
  - `val errorMessage: String? = null` → Hata mesajı (null ise hata yok)
- Ekrana özgü form değerleri ve kullanıcı girdileri buraya eklenir.
- Varsayılan değerler her alanda tanımlanmalıdır; bu sayede `<EkranAdi>UiState()` ile boş başlangıç durumu elde edilebilir.
- Türetilmiş boolean'lar (örn. `isFormValid`) UiState'e **eklenmez**; bunlar `ScreenContent` içinde `val` olarak hesaplanır.

### UiState Örneği
```kotlin
data class LoginUiState(
    val phoneNumber: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
```

---

## 3. UiIntent Kuralları

- `sealed interface` olarak tanımlanır.
- Her kullanıcı eylemi ayrı bir `data class` veya `data object` olarak ifade edilir.
- Parametre taşıması gereken eylemler `data class`, parametresiz eylemler `data object` kullanır.
- Adlandırma: Kullanıcının yaptığı eylemi açıklayan fiil + isim formatında. (örnek: `LoginClicked`, `PhoneNumberChanged`)
- UI katmanında hiçbir iş mantığı hesaplaması yapılmaz; ham değer olduğu gibi ViewModel'e iletilir.

### UiIntent Örneği
```kotlin
sealed interface LoginUiIntent {
    data class PhoneNumberChanged(val value: String) : LoginUiIntent
    data class PasswordChanged(val value: String) : LoginUiIntent
    data object TogglePasswordVisibility : LoginUiIntent
    data object LoginClicked : LoginUiIntent
    data object ForgotPasswordClicked : LoginUiIntent
    data object RegisterClicked : LoginUiIntent
}
```

---

## 4. UiEffect Kuralları

- `sealed interface` olarak tanımlanır.
- Yalnızca **tek seferlik** olaylar için kullanılır: navigasyon, Snackbar, Toast.
- `UiState` içinde tutulması mümkün olan ancak birden fazla kez tetiklenmemesi gereken her şey buraya aittir.
- ViewModel'de `Channel<UiEffect>(Channel.BUFFERED)` ile yönetilir ve UI'da `LaunchedEffect(Unit)` ile dinlenir.
- Parametre taşıması gereken etkiler `data class`, parametresiz etkiler `data object` kullanır.

### UiEffect Örneği
```kotlin
sealed interface LoginUiEffect {
    data object NavigateToHome : LoginUiEffect
    data object NavigateToRegister : LoginUiEffect
    data object NavigateToForgotPassword : LoginUiEffect
    data class ShowError(val message: String) : LoginUiEffect
}
```

---

## 5. Tam Contract Dosyası Şablonu

Aşağıdaki şablon, implementasyonda birebir kullanılan yapıyı yansıtır.
`<EkranAdi>` yerine Pascal Case ekran adı yazılır (örnek: `Login`, `Register`).

```kotlin
package com.turkcell.lyraapp.ui.<ekranadi>

// Kural: mvi-contracts.md dosyasındaki standartlara uygun olarak yazılmıştır.

// ── State ──────────────────────────────────────────────────────────────────
// Ekranın anlık, kalıcı görünüm durumunu temsil eder.
// Tüm alanlar varsayılan değere sahip olmalıdır; böylece <EkranAdi>UiState()
// ile boş başlangıç durumu elde edilebilir.
data class <EkranAdi>UiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // Ekrana özgü alanlar buraya eklenir.
)

// ── Intent ─────────────────────────────────────────────────────────────────
// Kullanıcının ekranla gerçekleştirdiği her eylemi temsil eder.
// Parametre taşıyanlar data class, parametresizler data object kullanır.
sealed interface <EkranAdi>UiIntent {
    // Kullanıcı eylemleri buraya eklenir.
}

// ── Effect ─────────────────────────────────────────────────────────────────
// Yalnızca tek seferlik olaylar için kullanılır: navigasyon, hata mesajı vb.
// UiState içine konamaz; Channel aracılığıyla iletilir.
sealed interface <EkranAdi>UiEffect {
    // Tek seferlik olaylar buraya eklenir.
}
```

---

## 6. Yasak Kullanımlar

| Yasak | Gerekçe |
| :--- | :--- |
| `UiState` içine navigasyon flag'i koymak (`isNavigateToHome: Boolean`) | Recomposition'da tekrar tekrar tetiklenir; Effect kullanılmalıdır. |
| `UiState` içine `isFormValid` gibi türetilmiş bool koymak | `ScreenContent` içinde `uiState` alanlarından hesaplanmalıdır. |
| `UiIntent` içinde iş mantığı hesaplaması yapmak | ViewModel sorumluluğudur. |
| `UiEffect` dışından tek seferlik olay tetiklemek | Akış tutarsızlığına yol açar. |
| `sealed class` kullanmak | `sealed interface` tercih edilir; daha az bellek kullanır. |
