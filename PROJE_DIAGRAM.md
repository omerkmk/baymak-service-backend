# Baymak Backend Projesi - Detaylı Yapı Diagramı

## 📊 Proje Yapısı - Klasör Hiyerarşisi

```
baymak-backend1/
│
├── src/main/java/com/baymak/backend/
│   │
│   ├── 🚀 BaymakBackend1Application.java
│   │   └── Spring Boot ana sınıfı (başlangıç noktası)
│   │
│   ├── 📁 config/
│   │   ├── SecurityConfig.java
│   │   │   ├── JWT Filter yapılandırması
│   │   │   ├── Role-based access control (ADMIN, TECHNICIAN, CUSTOMER)
│   │   │   ├── CORS ayarları
│   │   │   └── Endpoint güvenlik kuralları
│   │   │
│   │   └── OpenApiConfig.java
│   │       └── Swagger/OpenAPI dokümantasyon yapılandırması
│   │
│   ├── 🌐 controller/ (REST API Endpoints)
│   │   ├── AppointmentController.java
│   │   │   ├── POST   /api/appointments (Customer)
│   │   │   ├── GET    /api/appointments/my (Customer)
│   │   │   ├── GET    /api/appointments/assigned (Technician)
│   │   │   ├── GET    /api/appointments/all (Admin)
│   │   │   ├── PUT    /api/appointments/{id}/assign (Admin)
│   │   │   └── PUT    /api/appointments/{id}/status (Technician)
│   │   │
│   │   ├── AuthController.java
│   │   │   ├── POST   /api/auth/login
│   │   │   ├── POST   /api/auth/register
│   │   │   └── GET    /api/auth/me
│   │   │
│   │   ├── DeviceController.java
│   │   │   ├── GET    /api/devices (Customer)
│   │   │   ├── POST   /api/devices (Customer)
│   │   │   ├── GET    /api/devices/{id} (Customer)
│   │   │   ├── PUT    /api/devices/{id} (Customer)
│   │   │   └── DELETE /api/devices/{id} (Customer)
│   │   │
│   │   ├── ServiceReportController.java
│   │   │   ├── POST   /api/service-reports (Technician)
│   │   │   ├── GET    /api/service-reports/my (Technician)
│   │   │   ├── GET    /api/service-reports/all (Admin)
│   │   │   ├── GET    /api/service-reports/{id} (Admin, Technician)
│   │   │   └── GET    /api/service-reports/appointment/{id} (Admin, Technician)
│   │   │
│   │   ├── ServiceRequestController.java
│   │   │   ├── POST   /api/requests (Customer)
│   │   │   ├── GET    /api/requests (Customer)
│   │   │   └── GET    /api/requests/{id} (Customer)
│   │   │
│   │   ├── TechnicianController.java
│   │   │   ├── GET    /api/technicians (Admin)
│   │   │   ├── POST   /api/technicians (Admin)
│   │   │   ├── GET    /api/technicians/{id} (Admin)
│   │   │   ├── PUT    /api/technicians/{id} (Admin)
│   │   │   └── DELETE /api/technicians/{id} (Admin)
│   │   │
│   │   ├── UserController.java
│   │   │   ├── GET    /api/users (Admin)
│   │   │   ├── GET    /api/users/me (Authenticated)
│   │   │   └── PUT    /api/users/{id} (Admin)
│   │   │
│   │   └── PageController.java
│   │       └── GET    / (Public - Thymeleaf landing page)
│   │
│   ├── 💼 service/ (Business Logic Layer)
│   │   │
│   │   ├── Interfaces (Sözleşmeler):
│   │   ├── AppointmentService.java
│   │   ├── AuthService.java
│   │   ├── DeviceService.java
│   │   ├── ServiceReportService.java
│   │   ├── ServiceRequestService.java
│   │   ├── TechnicianService.java
│   │   └── UserService.java
│   │
│   │   └── impl/ (Implementations - Gerçek Kod):
│   │       ├── AppointmentServiceImpl.java
│   │       ├── AuthServiceImpl.java
│   │       ├── DeviceServiceImpl.java
│   │       ├── ServiceReportServiceImpl.java
│   │       ├── ServiceRequestServiceImpl.java
│   │       ├── TechnicianServiceImpl.java
│   │       └── UserServiceImpl.java
│   │
│   ├── 💾 repository/ (Data Access Layer)
│   │   ├── AppointmentRepository.java
│   │   │   └── extends JpaRepository<Appointment, Long>
│   │   │   └── Custom: findByUser(), findByTechnician(), findByStatus()
│   │   │
│   │   ├── DeviceRepository.java
│   │   │   └── extends JpaRepository<Device, Long>
│   │   │   └── Custom: findByUser()
│   │   │
│   │   ├── ServiceReportRepository.java
│   │   │   └── extends JpaRepository<ServiceReport, Long>
│   │   │   └── Custom: findByAppointment(), findByTechnician()
│   │   │
│   │   ├── ServiceRequestRepository.java
│   │   │   └── extends JpaRepository<ServiceRequest, Long>
│   │   │   └── Custom: findByUser()
│   │   │
│   │   ├── TechnicianRepository.java
│   │   │   └── extends JpaRepository<Technician, Long>
│   │   │   └── Custom: findByEmail()
│   │   │
│   │   └── UserRepository.java
│   │       └── extends JpaRepository<User, Long>
│   │       └── Custom: findByEmail()
│   │
│   ├── 🗄️ model/ (Database Entities)
│   │   ├── User.java
│   │   │   ├── @Entity @Table(name = "users")
│   │   │   ├── id, name, email, password, role (ADMIN/CUSTOMER)
│   │   │   ├── @OneToMany → Device, Appointment, ServiceRequest
│   │   │   └── @ManyToMany → Technician (through Appointment)
│   │   │
│   │   ├── Technician.java
│   │   │   ├── @Entity @Table(name = "technicians")
│   │   │   ├── id, name, email, phone, specialty
│   │   │   ├── @OneToMany → Appointment, ServiceReport
│   │   │   └── @ManyToMany → User (through Appointment)
│   │   │
│   │   ├── Device.java
│   │   │   ├── @Entity @Table(name = "devices")
│   │   │   ├── id, brand, model, serialNumber, type, status
│   │   │   ├── @ManyToOne → User
│   │   │   └── @OneToMany → Appointment
│   │   │
│   │   ├── Appointment.java
│   │   │   ├── @Entity @Table(name = "appointments")
│   │   │   ├── id, date, time, problemDescription, status
│   │   │   ├── @ManyToOne → User
│   │   │   ├── @ManyToOne → Device
│   │   │   ├── @ManyToOne → Technician (nullable)
│   │   │   └── @OneToOne → ServiceReport
│   │   │
│   │   ├── ServiceReport.java
│   │   │   ├── @Entity @Table(name = "service_reports")
│   │   │   ├── id, description, partsUsed, price, createdAt
│   │   │   ├── @ManyToOne → Appointment
│   │   │   └── @ManyToOne → Technician
│   │   │
│   │   ├── ServiceRequest.java
│   │   │   ├── @Entity @Table(name = "service_requests")
│   │   │   ├── id, description, status, createdAt
│   │   │   └── @ManyToOne → User
│   │   │
│   │   ├── AppointmentStatus.java (Enum)
│   │   │   └── PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
│   │   │
│   │   └── Status.java (Enum)
│   │       └── ACTIVE, INACTIVE
│   │
│   ├── 📦 dto/ (Data Transfer Objects)
│   │   │
│   │   ├── Request DTOs (Client → Server):
│   │   ├── AppointmentRequestDto.java
│   │   ├── AppointmentAssignDto.java
│   │   ├── AppointmentStatusUpdateDto.java
│   │   ├── AuthRequestDto.java
│   │   ├── DeviceRequestDto.java
│   │   ├── ServiceReportRequestDto.java
│   │   ├── ServiceRequestRequestDto.java
│   │   ├── TechnicianRequestDto.java
│   │   ├── UserRequestDto.java
│   │   └── PasswordResetRequestDto.java
│   │
│   │   └── Response DTOs (Server → Client):
│   │       ├── AppointmentResponseDto.java
│   │       ├── AuthResponseDto.java
│   │       ├── DeviceResponseDto.java
│   │       ├── ServiceReportResponseDto.java
│   │       ├── ServiceRequestResponseDto.java
│   │       ├── TechnicianResponseDto.java
│   │       └── UserResponseDto.java
│   │
│   ├── 🔐 security/
│   │   ├── JwtTokenProvider.java
│   │   │   ├── generateToken()
│   │   │   ├── validateToken()
│   │   │   └── getEmailFromToken()
│   │   │
│   │   ├── JwtAuthenticationFilter.java
│   │   │   ├── doFilterInternal()
│   │   │   └── Her HTTP isteğinde JWT token kontrolü
│   │   │
│   │   └── CustomUserDetailsService.java
│   │       └── loadUserByUsername() → UserDetails yükler
│   │
│   └── ⚠️ exception/
│       ├── GlobalExceptionHandler.java
│       │   ├── @ControllerAdvice
│       │   ├── Tüm exception'ları yakalar
│       │   └── JSON error response döner
│       │
│       ├── NotFoundException.java
│       ├── BadRequestException.java
│       ├── AlreadyExistsException.java
│       └── ApiError.java (Error response model)
│
└── src/main/resources/
    ├── application.properties
    │   ├── Database bağlantı bilgileri
    │   ├── JWT secret ve expiration
    │   └── Server port (8080)
    │
    ├── templates/
    │   └── index.html (Thymeleaf landing page)
    │
    └── static/
        └── baymak.png (Logo)
```

---

## 🔗 Katmanlar Arası İlişki Diagramı

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENT (Frontend)                        │
│                    React App (localhost:5173)                    │
└────────────────────────────┬────────────────────────────────────┘
                             │ HTTP Request (JSON)
                             │ JWT Token (Header)
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      SecurityConfig.java                         │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  JwtAuthenticationFilter                                  │  │
│  │  ├─ Token doğrulama                                       │  │
│  │  ├─ User bilgisi çıkarma                                 │  │
│  │  └─ SecurityContext'e ekleme                             │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Role-based Access Control                                │  │
│  │  ├─ ADMIN: /api/technicians/**, /api/users/**           │  │
│  │  ├─ TECHNICIAN: /api/service-reports/**                 │  │
│  │  └─ CUSTOMER: /api/devices/**, /api/appointments        │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      CONTROLLER LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │ Appointment  │  │ Auth         │  │ Device       │         │
│  │ Controller   │  │ Controller   │  │ Controller   │  ...    │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘         │
│         │                 │                  │                  │
│         │ Service çağrısı │                  │                  │
└─────────┼─────────────────┼──────────────────┼──────────────────┘
          │                 │                  │
          ▼                 ▼                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                       SERVICE LAYER                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │ Appointment  │  │ Auth         │  │ Device       │         │
│  │ Service      │  │ Service      │  │ Service      │  ...    │
│  │ Impl         │  │ Impl         │  │ Impl         │         │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘         │
│         │                 │                  │                  │
│         │ İş mantığı:     │                  │                  │
│         │ - Validasyon    │                  │                  │
│         │ - DTO ↔ Entity  │                  │                  │
│         │ - İş kuralları  │                  │                  │
│         │                 │                  │                  │
│         │ Repository çağrısı                 │                  │
└─────────┼─────────────────┼──────────────────┼──────────────────┘
          │                 │                  │
          ▼                 ▼                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                     REPOSITORY LAYER                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │ Appointment  │  │ User         │  │ Device       │         │
│  │ Repository   │  │ Repository   │  │ Repository   │  ...    │
│  │              │  │              │  │              │         │
│  │ JpaRepository│  │ JpaRepository│  │ JpaRepository│         │
│  │ + Custom     │  │ + Custom     │  │ + Custom     │         │
│  │   Methods    │  │   Methods    │  │   Methods    │         │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘         │
│         │                 │                  │                  │
│         │ SQL Query (Spring Data JPA otomatik oluşturur)       │
└─────────┼─────────────────┼──────────────────┼──────────────────┘
          │                 │                  │
          ▼                 ▼                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                      DATABASE (MySQL)                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │ appointments │  │ users        │  │ devices      │         │
│  │              │  │              │  │              │         │
│  │ - id         │  │ - id         │  │ - id         │         │
│  │ - user_id    │  │ - name       │  │ - user_id    │         │
│  │ - device_id  │  │ - email      │  │ - brand      │         │
│  │ - technician │  │ - password   │  │ - model      │         │
│  │ - status     │  │ - role       │  │ - status     │         │
│  └──────────────┘  └──────────────┘  └──────────────┘         │
│                                                                  │
│  ┌──────────────┐  ┌──────────────┐                            │
│  │ technicians  │  │ service_     │                            │
│  │              │  │ reports      │                            │
│  └──────────────┘  └──────────────┘                            │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Veri Akış Diagramı (Örnek: Randevu Oluşturma)

```
┌──────────────┐
│   CLIENT     │
│  (Frontend)  │
└──────┬───────┘
       │
       │ POST /api/appointments
       │ Headers: Authorization: Bearer <JWT_TOKEN>
       │ Body: {
       │   "deviceId": 1,
       │   "date": "2024-12-15",
       │   "time": "14:00",
       │   "problemDescription": "..."
       │ }
       ▼
┌─────────────────────────────────────┐
│     SecurityConfig.java             │
│  ┌───────────────────────────────┐ │
│  │ JwtAuthenticationFilter       │ │
│  │ 1. Token'ı al                 │ │
│  │ 2. Token'ı doğrula            │ │
│  │ 3. User bilgisini çıkar       │ │
│  │ 4. SecurityContext'e ekle     │ │
│  └───────────────────────────────┘ │
│  ┌───────────────────────────────┐ │
│  │ Role Check: CUSTOMER? ✓       │ │
│  └───────────────────────────────┘ │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  AppointmentController.java         │
│  ┌───────────────────────────────┐ │
│  │ @PostMapping("/api/appointments") │
│  │ createAppointment(             │ │
│  │   @RequestBody AppointmentRequestDto, │
│  │   Authentication auth          │ │
│  │ )                              │ │
│  │                                │ │
│  │ String email = auth.getName()  │ │
│  │ → appointmentService.create...│ │
│  └───────────────────────────────┘ │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  AppointmentServiceImpl.java        │
│  ┌───────────────────────────────┐ │
│  │ createAppointment() {         │ │
│  │   1. User'ı bul (email'den)   │ │
│  │   2. Device'ı bul (id'den)    │ │
│  │   3. Validasyon yap           │ │
│  │   4. Appointment entity oluştur│ │
│  │   5. appointmentRepository.save│ │
│  │   6. Entity → DTO dönüştür    │ │
│  │   7. Return AppointmentResponseDto│
│  │ }                              │ │
│  └───────────────────────────────┘ │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  AppointmentRepository.java         │
│  ┌───────────────────────────────┐ │
│  │ save(Appointment entity)      │ │
│  │ → JPA otomatik SQL oluşturur: │ │
│  │ INSERT INTO appointments...   │ │
│  └───────────────────────────────┘ │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│      MySQL Database                 │
│  ┌───────────────────────────────┐ │
│  │ INSERT INTO appointments      │ │
│  │ (user_id, device_id, date,    │ │
│  │  time, status, ...)           │ │
│  │ VALUES (1, 1, '2024-12-15',   │ │
│  │         '14:00', 'PENDING',...)│ │
│  │                                │ │
│  │ → Kayıt oluşturuldu           │ │
│  └───────────────────────────────┘ │
└──────────────┬──────────────────────┘
               │
               │ Response (Reverse Flow)
               ▼
┌─────────────────────────────────────┐
│  AppointmentResponseDto             │
│  {                                  │
│    "id": 123,                       │
│    "date": "2024-12-15",            │
│    "time": "14:00",                 │
│    "status": "PENDING",             │
│    ...                              │
│  }                                  │
└──────────────┬──────────────────────┘
               │
               ▼
┌──────────────┐
│   CLIENT     │
│  (Frontend)  │
│  Response    │
│  alındı ✓    │
└──────────────┘
```

---

## 🗄️ Entity İlişki Diagramı (ERD)

```
┌─────────────┐
│    User     │
│─────────────│
│ id (PK)     │
│ name        │
│ email (UK)  │
│ password    │
│ role        │
└──────┬──────┘
       │ 1
       │
       │ N
       │ @OneToMany
       ├──────────────────┐
       │                  │
       ▼                  ▼
┌─────────────┐    ┌──────────────┐
│   Device    │    │  Appointment │
│─────────────│    │──────────────│
│ id (PK)     │    │ id (PK)      │
│ user_id(FK) │    │ user_id (FK) │
│ brand       │    │ device_id(FK)│
│ model       │    │ technician_id│
│ serialNumber│    │   (FK, nullable)│
│ type        │    │ date         │
│ status      │    │ time         │
└──────┬──────┘    │ status       │
       │ 1         │ problemDesc  │
       │           └──────┬───────┘
       │                  │ 1
       │ N                │
       │ @OneToMany       │
       │                  │ 1
       │                  │
       │                  ▼
       │           ┌──────────────┐
       │           │ServiceReport │
       │           │──────────────│
       │           │ id (PK)      │
       │           │ appointment_id│
       │           │ technician_id│
       │           │ description  │
       │           │ partsUsed    │
       │           │ price        │
       │           └──────────────┘
       │
       │                  ▲
       │                  │ N
       │                  │ @OneToMany
       │                  │
       │            ┌─────┴──────┐
       │            │ Technician │
       │            │────────────│
       │            │ id (PK)    │
       │            │ name       │
       │            │ email (UK) │
       │            │ phone      │
       │            │ specialty  │
       │            └────────────┘
       │
       │ N
       │ @OneToMany
       │
       ▼
┌──────────────┐
│ServiceRequest│
│──────────────│
│ id (PK)      │
│ user_id (FK) │
│ description  │
│ status       │
│ createdAt    │
└──────────────┘
```

---

## 🔐 Güvenlik ve Yetkilendirme Akışı

```
User Login Request
    │
    ▼
┌─────────────────────┐
│  AuthController     │
│  /api/auth/login    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  AuthService        │
│  1. Email/Password  │
│     doğrula         │
│  2. JWT token oluştur│
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  JwtTokenProvider   │
│  generateToken()    │
│  → JWT oluştur      │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Response:          │
│  {                  │
│    "token": "...",  │
│    "user": {...}    │
│  }                  │
└─────────────────────┘

─────────────────────────────────

API Request (with JWT)
    │
    ▼
┌─────────────────────┐
│  JwtAuthentication  │
│  Filter             │
│  1. Token'ı header'dan│
│     al              │
│  2. Token'ı doğrula │
│  3. User bilgisini  │
│     çıkar           │
│  4. SecurityContext │
│     'e ekle         │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  SecurityConfig     │
│  Role Check:        │
│  - URL path kontrol │
│  - User role kontrol│
│  - İzin var mı?     │
└──────────┬──────────┘
           │
           ├─ Admin? → Admin endpoints
           ├─ Technician? → Technician endpoints
           └─ Customer? → Customer endpoints
```

---

## 📝 Dosya Sayıları Özeti

- **Controller:** 8 dosya
- **Service (Interface + Impl):** 14 dosya (7 interface + 7 implementation)
- **Repository:** 6 dosya
- **Model (Entity):** 8 dosya (5 entity + 3 enum)
- **DTO:** 18 dosya (9 Request + 9 Response)
- **Security:** 3 dosya
- **Config:** 2 dosya
- **Exception:** 5 dosya

**Toplam:** ~64 Java dosyası

---

## 🎯 Önemli Notlar

1. **Katman Sorumlulukları:**
   - Controller → HTTP işlemleri
   - Service → İş mantığı
   - Repository → Veritabanı erişimi
   - Model → Veri yapısı

2. **Güvenlik:**
   - Tüm istekler JWT ile korunur
   - Role-based access control (RBAC)
   - SecurityConfig'de endpoint izinleri

3. **Veri Akışı:**
   - Request: Client → Controller → Service → Repository → DB
   - Response: DB → Repository → Service → Controller → Client

4. **DTO Pattern:**
   - Entity'ler veritabanı için
   - DTO'lar API için
   - Güvenlik ve sadeleştirme sağlar

