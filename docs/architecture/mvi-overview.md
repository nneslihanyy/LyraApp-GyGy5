# LyraApp — MVI Genel Bakış

> Bu dosya, LyraApp projesinde uygulanan MVI (Model-View-Intent) mimarisinin
> genel prensiplerini, veri akışını ve katman sorumluluklarını açıklar.
> Bu dosya, mimari kararlar için **tek doğruluk kaynağıdır (single source of truth)**.

---

## 1. Temel Kavramlar

### Model
Ekranın anlık durumunu (UI State) temsil eder. Değiştirilemez (immutable) bir `data class`'tır.
ViewModel tarafından yönetilir; UI tarafından hiçbir zaman doğrudan değiştirilemez.

### View
Jetpack Compose `@Composable` fonksiyonlarından oluşur. İki katmana ayrılır:

- **`<EkranAdi>Screen`** — ViewModel'e bağlı, stateful katman. `hiltViewModel()` ile bağlanır,
  state'i `collectAsStateWithLifecycle()` ile toplar, effect'leri `LaunchedEffect` ile dinler.
- **`<EkranAdi>ScreenContent`** — Stateless, saf UI katmanı. `uiState` ve `onIntent` alır.
  Hiçbir ViewModel veya state yönetimi içermez. Preview'lar bu bileşeni kullanır.

### Intent
Kullanıcının ekranla gerçekleştirdiği her eylemi temsil eder.
`sealed interface` ile tanımlanır. ViewModel bu niyetleri işleyerek state veya effect üretir.

---

## 2. Tek Yönlü Veri Akışı (Unidirectional Data Flow)

```
Kullanıcı Eylemi
      │
      ▼  onIntent(UiIntent)
┌─────────────────────────────────────────────────────┐
│              <EkranAdi>Screen (Stateful)             │
│   viewModel: <EkranAdi>ViewModel = hiltViewModel()  │
│   uiState by viewModel.state                        │
│       .collectAsStateWithLifecycle()                │
│   LaunchedEffect → viewModel.effect.collect { }     │
└───────────────────┬───────────────┬─────────────────┘
                    │  UiIntent     │  UiState
                    ▼               ▲
┌─────────────────────────────────────────────────────┐
│              <EkranAdi>ViewModel                     │
│   onIntent(intent) → private handler'lar            │
│   _state: MutableStateFlow<UiState>                 │
│   _effect: Channel<UiEffect>(Channel.BUFFERED)      │
└───────────────────┬─────────────────────────────────┘
                    │ UiEffect (LaunchedEffect → lambda)
                    ▼
┌─────────────────────────────────────────────────────┐
│      <EkranAdi>ScreenContent (Stateless)            │
│   uiState: <EkranAdi>UiState                        │
│   onIntent: (<EkranAdi>UiIntent) -> Unit            │
│   (Preview'lar bu bileşeni kullanır)                │
└─────────────────────────────────────────────────────┘
```

---

## 3. State, Intent ve Effect Sorumlulukları

| Katman | Tür | Sorumluluk |
| :--- | :--- | :--- |
| `UiState` | `data class` | Ekranın anlık, kalıcı görünüm durumu. (form değerleri, loading, hata mesajı) |
| `UiIntent` | `sealed interface` | Kullanıcının gerçekleştirdiği eylem. (buton tıklaması, metin girişi) |
| `UiEffect` | `sealed interface` | Tek seferlik olaylar. (navigasyon, toast mesajı) State'e dahil edilemez. |

### UiState vs UiEffect Ayrım Kuralı
- Ekran yeniden oluşturulduğunda (recomposition) tekrar görünmesi **gereken** veriler → `UiState`
- Yalnızca **bir kez** tetiklenmesi gereken olaylar → `UiEffect`

---

## 4. Paket ve Dosya Yapısı

Her ekran `ui/<ekranAdi>/` paketi altında **tam olarak üç dosyadan** oluşur:

```
ui/
└── login/
    ├── LoginContract.kt     # UiState + UiIntent + UiEffect
    ├── LoginViewModel.kt    # @HiltViewModel, StateFlow, Channel
    └── LoginScreen.kt       # LoginScreen (stateful) + LoginScreenContent (stateless) + Preview'lar
```

### Dosya Sorumlulukları

| Dosya | Sorumluluk |
| :--- | :--- |
| `<EkranAdi>Contract.kt` | MVI sözleşmesi. Yalnızca veri yapıları içerir, iş mantığı yoktur. |
| `<EkranAdi>ViewModel.kt` | Intent işleme, state güncelleme, effect gönderme. |
| `<EkranAdi>Screen.kt` | Stateful `<EkranAdi>Screen`, stateless `<EkranAdi>ScreenContent`, dark/light Preview'lar. |

---

## 5. Screen Bileşeni — Navigasyon Kuralı

`<EkranAdi>Screen` composable'ı, navigasyon kütüphanesine doğrudan bağımlı değildir.
Navigasyon, dışarıdan lambda olarak enjekte edilir:

```kotlin
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {},
)
```

Bu sayede ekran, farklı navigasyon kütüphanelerine bağımlı olmadan test edilebilir ve yeniden kullanılabilir.

---

## 6. Preview Standardı

Her `<EkranAdi>Screen.kt` dosyasının sonunda **hem dark hem light** tema için iki Preview bulunmalıdır.
Preview'lar stateless `<EkranAdi>ScreenContent` bileşenini kullanır; `hiltViewModel()` Preview'da çalışmaz.

```kotlin
@Preview(showBackground = true)
@Composable
fun <EkranAdi>ScreenDarkPreview() {
    LyraAppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            <EkranAdi>ScreenContent(
                uiState = <EkranAdi>UiState(),
                onIntent = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun <EkranAdi>ScreenLightPreview() {
    LyraAppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            <EkranAdi>ScreenContent(
                uiState = <EkranAdi>UiState(),
                onIntent = {}
            )
        }
    }
}
```

---

## 7. Genel Kurallar

- Her ekranın kendi `Contract.kt` dosyası vardır.
- ViewModel, doğrudan `Context` veya Android framework sınıflarına bağımlı olamaz.
- UI katmanında `remember { mutableStateOf(...) }` ile lokal state tutulmaz; tüm state ViewModel'den gelir.
- Ham `Color(0xFF...)` veya elle `TextStyle` tanımı yapılamaz; `MaterialTheme` kullanılır.
- Coroutine scope olarak yalnızca `viewModelScope` kullanılır.
- `<EkranAdi>ScreenContent` her zaman `private` olarak tanımlanır.
- Türetilmiş boolean değerler (örn. `isFormValid`) `UiState`'e değil, `ScreenContent` içinde `uiState` alanlarından hesaplanarak yerel bir `val` olarak tanımlanır.

---

## 8. Domain ve Data Katmanları (Gelecek Aşama)

```
ui/<ekran>/          ← Mevcut aşama
domain/usecase/      ← UseCase sınıfları (iş mantığı)
domain/repository/   ← Repository arayüzleri
data/repository/     ← RepositoryImpl (API / yerel veritabanı)
```