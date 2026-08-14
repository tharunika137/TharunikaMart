# TharunikaMart

A multi-seller e-commerce marketplace built for the Anna University R2025 Semester 3 capstone specification.

## Stack
- JDK 17
- Maven
- Apache Tomcat 9
- Java Servlets / JSP / JSTL
- H2 + HikariCP
- JDBC with PreparedStatement
- BCrypt password hashing
- Gson, SLF4J + Logback
- JUnit 5 + Mockito

## Features
F1 registration/login with Buyer, Seller and seeded Administrator login
F2 seller listing management
F3 buyer search/filter
F4 cart
F5 mock-payment checkout
F6 buyer order history and admin order view/status
F7 admin user/order/listing moderation
F8 reviews schema is included; review UI can be added in the next feature branch

## 15-minute local setup
1. Install JDK 17, Maven and Tomcat 9.
2. Open this folder in VS Code.
3. In the VS Code terminal run:
   `mvn clean package`
4. Copy `target/tharunikamart.war` into Tomcat's `webapps` folder.
5. Start Tomcat with `bin/startup.bat`.
6. Open `http://localhost:8080/tharunikamart/app/home`.

The app creates `data/tharunikamart.mv.db` automatically on first start.

## Demo accounts
All demo accounts use password: `password`
- Administrator: admin@tharunikamart.local
- Buyer: buyer@tharunikamart.local
- Seller: seller@tharunikamart.local

## Architecture
Browser -> Filters -> AppServlet -> Service -> DAO -> HikariCP -> H2.

SQL is isolated in DAO classes and uses PreparedStatement. Sessions use HttpSession and session ID regeneration on login. Passwords are BCrypt-hashed.

## Important
For the final submission, add the remaining F8 review workflow, API v1 endpoints, automated security/DAO tests, diagrams, CI, deployment, and the Phase 3 chatbot exactly against the supplied project specification.
