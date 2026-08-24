# Telekom Satış & Hat Aktivasyon

Bir müşterinin paket seçip, telefon numarası ve opsiyonel cihaz alarak yeni hat başvurusu yapabildiği, backend'in bu başvuruyu doğrulayıp harici bir aktivasyon servisiyle (simüle) canlıya aldığı uçtan uca bir sistem.

## Teknolojiler

**Backend**
- Java 17, Spring Boot 4.1.0
- Spring Data JPA (PostgreSQL)
- Lombok, MapStruct
- Maven

**Frontend**
- React + Vite

**Aktivasyon Simülatörü**
- Node.js + Express

## Proje Yapısı

```
telekom-aktivasyon/
├── src/                    # Backend (Spring Boot) kaynak kodu
├── frontend/                # React arayüzü
└── activation-simulator/    # Aktivasyon servisini simüle eden Node.js uygulaması
```

## Kurulum

### Ön Gereksinimler
- Java 17
- Node.js (v18+)
- PostgreSQL (Postgres.app veya başka bir kurulum)

### 1. Veritabanı

PostgreSQL'de `telekom_aktivasyon` adında bir veritabanı oluştur, içinde `telekom` şemasını aç. Tablo yapısı için `src/main/resources/sql/` altındaki script'leri sırayla çalıştır.

`src/main/resources/application.properties` içindeki veritabanı kullanıcı adı/şifresini kendi ortamına göre güncelle.

### 2. Backend'i Çalıştırma

```bash
cd telekom-aktivasyon
./mvnw spring-boot:run
```

Uygulama `http://localhost:8080` adresinde ayağa kalkar.

### 3. Aktivasyon Simülatörünü Çalıştırma

```bash
cd activation-simulator
npm install
node index.js
```

`http://localhost:3000` adresinde ayağa kalkar. Backend, hat aktivasyonu sırasında bu servisi çağırır.

### 4. Frontend'i Çalıştırma

```bash
cd frontend
npm install
npm run dev
```

`http://localhost:5173` adresinde açılır.

## API Uçları (Özet)

| Metod | Endpoint | Açıklama |
|---|---|---|
| GET | /api/packages | Aktif paketleri listeler |
| GET | /api/devices | Stokta olan cihazları listeler |
| GET | /api/sim-cards | Müsait numaraları listeler |
| POST | /api/cart | Yeni sepet oluşturur |
| POST | /api/cart/{cartId}/items/package/{packageId} | Sepete paket ekler |
| POST | /api/cart/{cartId}/items/device/{deviceId} | Sepete cihaz ekler |
| POST | /api/cart/{cartId}/items/sim/{simId} | Sepete numara ekler (rezerve eder) |
| POST | /api/checkout | Sepeti siparişe dönüştürür |
| POST | /api/orders/{orderId}/activate | Siparişi aktive eder |

## Notlar

Bu proje bir staj kapsamında geliştirilmektedir.