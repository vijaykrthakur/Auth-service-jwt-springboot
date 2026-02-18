# 🔐 Production Ready Auth Backend (Spring Boot + JWT)

A production-ready Authentication & Authorization backend built using **Spring Boot**, **Spring Security**, **JWT**, and **MySQL**, following clean layered architecture and industry best practices.

---

# 🚀 Features

* 🔐 JWT Authentication & Authorization
* 🔁 Refresh Token with Rotation (high security)
* 👤 Role-Based Access Control (ADMIN & USER)
* 🚪 Secure Logout with Token Blacklisting
* 🧱 Clean Layered Architecture (Controller → Service → Repository)
* 📦 DTO Pattern + MapperStruct
* ❌ Global Exception Handling (clean JSON errors)
* 📄 Swagger OpenAPI Documentation
* 🗄️ MySQL Database Integration
* ⚙️ application.yml based configuration (no hardcoding)

---

# 🛠️ Tech Stack

**Backend**

* Java 17/21+
* Spring Boot 3/4
* Spring Security
* Spring Data JPA (Hibernate)
* JWT (JSON Web Token)

**Database**

* MySQL

**Tools**

* Maven
* Swagger OpenAPI
* Lombok
* MapperStruct

---

# 🧠 Architecture

Clean layered architecture used:

```
Controller → Service → Repository → Database
        ↓
       DTO
        ↓
   MapperStruct
```

Ensures:

* Clean code
* Separation of concerns
* Easy scalability
* Production-ready structure

---

# 🔐 Authentication Flow

### 🟢 Register

User registers with email & password.

### 🟢 Login

Returns:

* Access Token (JWT)
* Refresh Token

### 🟢 Access Protected APIs

Use JWT in header:

```
Authorization: Bearer <token>
```

### 🔁 Refresh Token

Generates new access token when expired.

### 🚪 Logout

JWT added to blacklist → cannot be reused.

---

# 👥 Roles

Two roles supported:

```
ROLE_USER
ROLE_ADMIN
```

Role-based endpoint protection using Spring Security.

---

# 📄 API Documentation (Swagger)

After running project:

```
http://localhost:8080/swagger-ui/index.html
```

Test all APIs directly from browser.

---

# 🗄️ Database Tables

* users
* roles
* refresh_token
* token_blacklist

---

# ⚙️ Configuration (application.yml)

Example:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/authdb
    username: root
    password: root

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: your_secret_key_here
  expiration: 3600000
```

---

# ▶️ Run Project Locally

### 1️⃣ Clone project

```
git clone https://github.com/yourusername/auth-backend.git
```

### 2️⃣ Configure MySQL

Create database:

```
authdb
```

Update credentials in `application.yml`.

### 3️⃣ Run project

```
mvn spring-boot:run
```

---

# 🌍 Deployment Ready

Project can be deployed on:

* Render
* AWS EC2
* Docker
* Railway

---

# 👨‍💻 Author

**Vijay Kumar Thakur**
Java Backend Developer (Spring Boot)

Looking for opportunities in:

* Java Backend Development
* Spring Boot Developer Roles

---

# ⭐ If you like this project

Give a ⭐ on GitHub and connect on LinkedIn!
