# Baymak Backend Projesi - Mimari ve Kod Yapısı Rehberi

## 📁 Proje Yapısı (Paket Organizasyonu)

```
com.baymak.backend/
│
├── BaymakBackend1Application.java    ⭐ BAŞLANGIÇ NOKTASI
│
├── config/                            🔧 YAPILANDIRMA (Configuration)
│   ├── SecurityConfig.java           (Spring Security - JWT, Roller, İzinler)
│   └── OpenApiConfig.java            (Swagger/OpenAPI dokümantasyonu)
│
├── controller/                        🌐 HTTP İSTEKLERİ (REST API Katmanı)
│   ├── AppointmentController.java
│   ├── AuthController.java
│   ├── DeviceController.java
│   └── ... (diğer controller'lar)
│
├── service/                           💼 İŞ MANTIĞI (Business Logic)
│   ├── AppointmentService.java       (Interface - Sözleşme)
│   └── impl/
│       └── AppointmentServiceImpl.java (Implementation - Gerçek kod)
│
├── repository/                        💾 VERİTABANI ERİŞİMİ (Data Access)
│   ├── AppointmentRepository.java
│   └── ... (diğer repository'ler)
│
├── model/                             🗄️ VERİTABANI TABLOLARI (Entity/Model)
│   ├── Appointment.java
│   ├── User.java
│   └── ... (diğer entity'ler)
│
├── dto/                               📦 VERİ TRANSFER OBJELERİ (Data Transfer Objects)
│   ├── AppointmentRequestDto.java    (Client → Server)
│   ├── AppointmentResponseDto.java   (Server → Client)
│   └── ...
│
├── security/                          🔐 GÜVENLİK (Authentication & Authorization)
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
│
└── exception/                         ⚠️ HATA YÖNETİMİ
    ├── GlobalExceptionHandler.java
    └── ... (custom exception'lar)
```

---

## 🔄 Kod Akışı: Bir İstek Nasıl İşlenir?

### Örnek: Yeni Randevu Oluşturma İsteği

```
1. CLIENT (Frontend/Postman)
   ↓ HTTP POST /api/appointments
   ↓ Body: { "deviceId": 1, "date": "2024-12-15", ... }
   
2. SecurityConfig.java
   ↓ İsteği yakalar, JWT token kontrolü yapar
   ↓ Role kontrolü: CUSTOMER rolü gerekli
   ↓ Geçerliyse → Controller'a yönlendirir
   
3. AppointmentController.java
   ↓ @PostMapping("/api/appointments")
   ↓ AppointmentRequestDto alır
   ↓ Authentication'dan user email'i alır
   ↓ appointmentService.createAppointment() çağırır
   
4. AppointmentService.java (Interface)
   ↓ Sadece metod imzası (sözleşme)
   
5. AppointmentServiceImpl.java
   ↓ İŞ MANTIĞI burada!
   ↓ DTO'yu Model'e çevirir
   ↓ Validasyon yapar
   ↓ appointmentRepository.save() çağırır
   
6. AppointmentRepository.java
   ↓ JpaRepository metodlarını kullanır
   ↓ SQL sorgusu çalıştırır (Spring Data JPA otomatik yapar)
   
7. MySQL Veritabanı
   ↓ INSERT INTO appointments ...
   ↓ Kayıt oluşturulur
   
8. Geri Dönüş (Reverse)
   ↓ Appointment (Entity) → AppointmentResponseDto
   ↓ AppointmentController → JSON response
   ↓ Client'a döner
```

---

## 📚 Katmanların Detaylı Açıklaması

### 1. **Controller Katmanı** (`controller/`)
**Görevi:** HTTP isteklerini alır, cevaplar döner

**Özellikler:**
- `@RestController` → REST API endpoint'leri
- `@RequestMapping("/api/...")` → URL yolu
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` → HTTP metodları
- `@Operation` → Swagger dokümantasyonu
- `Authentication` parametresi → Giriş yapmış kullanıcı bilgisi

**Okuma Sırası:**
1. Önce bir Controller aç (örn: `AppointmentController.java`)
2. Endpoint'leri incele (hangi URL'ler, hangi HTTP metodları)
3. Hangi Service metodlarını çağırdığına bak

---

### 2. **Service Katmanı** (`service/`)
**Görevi:** İş mantığı (business logic) burada yazılır

**Yapı:**
- **Interface** (`AppointmentService.java`) → Sözleşme, ne yapılacağını söyler
- **Implementation** (`impl/AppointmentServiceImpl.java`) → Gerçek kod, nasıl yapılacağını gösterir

**Özellikler:**
- `@Service` → Spring Bean olarak kaydedilir
- `@Transactional` → Veritabanı işlemlerini yönetir
- DTO ↔ Entity dönüşümleri
- Validasyonlar, iş kuralları
- Repository metodlarını çağırır

**Okuma Sırası:**
1. Interface'i oku → Hangi metodlar var?
2. Implementation'ı oku → Nasıl çalışıyor?
3. Hangi Repository metodlarını kullandığına bak

---

### 3. **Repository Katmanı** (`repository/`)
**Görevi:** Veritabanı işlemleri (CRUD)

**Özellikler:**
- `extends JpaRepository<Entity, ID>` → Spring Data JPA
- Otomatik metodlar: `save()`, `findById()`, `findAll()`, `delete()`
- Custom query metodları: `findByUser()`, `findByStatus()`

**Okuma Sırası:**
1. Repository interface'ini aç
2. Hangi metodlar var? (Spring Data JPA otomatik SQL yazar)
3. Entity ile ilişkisine bak

---

### 4. **Model Katmanı** (`model/`)
**Görevi:** Veritabanı tablolarını temsil eder (Entity)

**Özellikler:**
- `@Entity` → JPA Entity
- `@Table(name = "...")` → Tablo adı
- `@Id`, `@GeneratedValue` → Primary key
- `@ManyToOne`, `@OneToMany` → İlişkiler
- `@Column` → Sütun özellikleri
- Lombok: `@Data`, `@Builder`, `@NoArgsConstructor`

**Okuma Sırası:**
1. Entity sınıfını aç (örn: `Appointment.java`)
2. Hangi alanlar var?
3. Diğer entity'lerle ilişkileri nedir? (`@ManyToOne`, `@OneToMany`)

---

### 5. **DTO Katmanı** (`dto/`)
**Görevi:** API'den gelen/giden verileri taşır

**Türler:**
- **RequestDto** → Client'tan gelen veri (örn: `AppointmentRequestDto`)
- **ResponseDto** → Client'a giden veri (örn: `AppointmentResponseDto`)

**Özellikler:**
- Entity'den ayrı (güvenlik, sadeleştirme için)
- `@Builder` → Obje oluşturma kolaylığı
- Validasyon: `@NotNull`, `@NotEmpty`, `@Valid`

**Okuma Sırası:**
1. RequestDto → Client ne gönderiyor?
2. ResponseDto → Client ne alıyor?
3. Entity ile farkları neler? (hangi alanlar gizli/sade)

---

### 6. **Security Katmanı** (`security/`)
**Görevi:** Kimlik doğrulama ve yetkilendirme

**Bileşenler:**
- `SecurityConfig.java` → Güvenlik kuralları, roller, izinler
- `JwtTokenProvider.java` → JWT token oluşturma/doğrulama
- `JwtAuthenticationFilter.java` → Her istekte token kontrolü
- `CustomUserDetailsService.java` → Kullanıcı bilgilerini yükleme

**Okuma Sırası:**
1. `SecurityConfig.java` → Hangi endpoint'ler kimlere açık?
2. Rolleri anla: ADMIN, TECHNICIAN, CUSTOMER
3. `JwtAuthenticationFilter` → Token nasıl kontrol ediliyor?

---

### 7. **Config Katmanı** (`config/`)
**Görevi:** Uygulama yapılandırmaları

**Dosyalar:**
- `SecurityConfig.java` → Güvenlik ayarları
- `OpenApiConfig.java` → API dokümantasyonu

---

### 8. **Exception Katmanı** (`exception/`)
**Görevi:** Hata yönetimi

**Bileşenler:**
- `GlobalExceptionHandler.java` → Tüm exception'ları yakalar, JSON döner
- Custom Exception'lar: `NotFoundException`, `BadRequestException`, vb.

---

## 🎯 Projeyi Nasıl Okumalısın?

### **1. Başlangıç Noktası:**
```
BaymakBackend1Application.java → Spring Boot uygulamasını başlatır
```

### **2. Güvenlik Yapısını Anla:**
```
SecurityConfig.java → Hangi endpoint'ler kimlere açık?
→ Rolleri öğren: ADMIN, TECHNICIAN, CUSTOMER
```

### **3. Bir Özelliği Takip Et (Örnek: Randevu Oluşturma):**

**Adım 1:** Controller'ı bul
```
AppointmentController.java
→ @PostMapping("/api/appointments")
→ createAppointment() metodu
```

**Adım 2:** Service'e git
```
AppointmentService.java (interface)
→ createAppointment() metod imzası

AppointmentServiceImpl.java (implementation)
→ createAppointment() gerçek kod
→ İş mantığı burada!
```

**Adım 3:** Repository'ye bak
```
AppointmentRepository.java
→ save() metodu → Veritabanına kaydeder
```

**Adım 4:** Model'i kontrol et
```
Appointment.java
→ Hangi alanlar var?
→ Hangi entity'lerle ilişkili? (User, Device, Technician)
```

**Adım 5:** DTO'ları incele
```
AppointmentRequestDto.java → Gelen veri
AppointmentResponseDto.java → Giden veri
```

---

## 🔍 Okuma Stratejisi

### **Yeni Başlayan İçin:**
1. ✅ `model/` → Veritabanı yapısını anla
2. ✅ `dto/` → API'den ne gelip gidiyor?
3. ✅ `controller/` → Hangi endpoint'ler var?
4. ✅ `service/impl/` → İş mantığını oku
5. ✅ `repository/` → Veritabanı sorguları

### **Gelişmiş Okuma:**
1. ✅ `SecurityConfig.java` → Güvenlik mimarisi
2. ✅ `security/` → JWT nasıl çalışıyor?
3. ✅ `exception/` → Hata yönetimi
4. ✅ `config/` → Yapılandırmalar

---

## 🛠️ Kullanılan Teknolojiler

- **Spring Boot 3.x** → Framework
- **Spring Security 6** → Güvenlik
- **Spring Data JPA** → Veritabanı erişimi
- **MySQL** → Veritabanı
- **JWT (JSON Web Token)** → Authentication
- **Lombok** → Kod azaltma (@Data, @Builder, vb.)
- **Swagger/OpenAPI** → API dokümantasyonu
- **Thymeleaf** → Server-side rendering (sadece landing page)

---

## 💡 İpuçları

1. **Katmanlar arası geçiş:** Controller → Service → Repository → Model
2. **İlişkileri anla:** Entity'lerdeki `@ManyToOne`, `@OneToMany` annotation'ları
3. **Rolleri takip et:** SecurityConfig'de hangi endpoint kimlere açık?
4. **DTO vs Entity:** DTO API için, Entity veritabanı için
5. **Transaction:** Service katmanında `@Transactional` → Veritabanı tutarlılığı

---

## 📖 Örnek: Bir Özelliği Anlamak İçin

**"Randevuya teknisyen atama" özelliğini anlamak istiyorsun:**

1. **Controller'da bul:**
   ```java
   AppointmentController.java
   → @PutMapping("/{id}/assign")
   → assignTechnician() metodu
   ```

2. **Service'e git:**
   ```java
   AppointmentServiceImpl.java
   → assignTechnician() metodu
   → İş mantığı: Teknisyen var mı? Randevu var mı?
   ```

3. **Repository'ye bak:**
   ```java
   AppointmentRepository.java
   → findById() → Randevuyu bul
   → save() → Güncellemeyi kaydet
   ```

4. **Model'i kontrol et:**
   ```java
   Appointment.java
   → technician alanı → Teknisyen bilgisi burada saklanıyor
   ```

Bu şekilde tüm özelliği anlayabilirsin! 🎯

