# decisions.md

> Projede verilen bütün mimarisel-teknik kararları ve karar geçmişini içeren dökümantasyondur.

---

### Dependency Injection Kütüphanesi

- Seçim*: **Hilt**

- Son Güncelleme Tarihi*: 04.06.2026

- Alternatifler: **Koin, X**

- Sebep: **Opsiyonel**


### Navigasyon

- Seçim: **Compose Navigation**

- Son Güncelleme Tarihi: 04.06.2026

## İkon 

- Seçim : **material-icons-extended**

- Son Güncelleme Tarihi: 13.06.2026

### UI Mimarisi

- Seçim: **MVI (Model-View-Intent)**

- Son Güncelleme Tarihi: 13.06.2026

- Referans Dosyalar:
  - `docs/architecture/mvi-overview.md`
  - `docs/architecture/mvi-contracts.md`
  - `docs/architecture/mvi-viewmodel-rules.md`

- Sebep: Tek yönlü veri akışı (UDF), öngörülebilir state yönetimi ve Jetpack Compose ile doğal uyum.

### Annotation Processor

- Seçim: **KSP (Kotlin Symbol Processing)**

- Son Güncelleme Tarihi: 13.06.2026

- Alternatifler: kapt

- Sebep: AGP 9 ile kapt uyumsuz (`Android BaseExtension not found`). KSP daha hızlı derleme süresi sağlar ve AGP 9 ile uyumludur.

- Kullanılan Versiyon: `2.2.10-2.0.2` (Kotlin 2.2.10 ile eşleşmektedir)

- Not: `android.disallowKotlinSourceSets=false` flag'i `gradle.properties` içinde tanımlıdır; KSP'nin AGP 9 tam desteği sağlandığında kaldırılabilir.