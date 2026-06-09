# LyraApp - Typography System

> Bu dosya LyraApp uygulamasının tipografi sistemi için tek doğruluk kaynağıdır (single source of truth).

---

## 1. Temel Kurallar

* Uygulama genelinde yalnızca Roboto font ailesi kullanılır.
* Hiçbir Composable içinde doğrudan TextStyle tanımlanmaz.
* Tipografi değerleri yalnızca MaterialTheme.typography üzerinden okunur.
* Font boyutları ve ağırlıkları Type.kt içerisinde merkezi olarak tanımlanır.

---

## 2. Font Ailesi

```kotlin
FontFamily.Default
```

veya

```kotlin
FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)
```

---

## 3. Typography Tanımları

### Display

* displayLarge: 57sp / Regular
* displayMedium: 45sp / Regular
* displaySmall: 36sp / Regular

### Headline

* headlineLarge: 32sp / Regular
* headlineMedium: 28sp / Regular
* headlineSmall: 24sp / Regular

### Title

* titleLarge: 22sp / Regular
* titleMedium: 16sp / Medium
* titleSmall: 14sp / Medium

### Body

* bodyLarge: 16sp / Regular
* bodyMedium: 14sp / Regular
* bodySmall: 12sp / Regular

### Label

* labelLarge: 14sp / Medium
* labelMedium: 12sp / Medium
* labelSmall: 11sp / Medium

---

## 4. Type.kt

Type.kt dosyası Material 3 Typography nesnesini oluşturmalı ve LyraTypography adıyla dışarı açmalıdır.

Theme.kt içerisinde:

```kotlin
typography = LyraTygpography
```

kullanılmalıdır.
