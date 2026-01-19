# 💱 Currency Converter API

A robust RESTful API built with **Spring Boot** that performs real-time currency conversion and tracks transaction history.

This project demonstrates a professional backend architecture using **Spring Data JPA**, **Spring Security**, and external API integration via **RestTemplate**.

## 🚀 Key Features

* **Real-time Conversion:** Fetches live exchange rates using [ExchangeRate-API](https://www.exchangerate-api.com/).
* **Transaction History:** Automatically saves every conversion transaction to an in-memory database (H2).
* **Secure Configuration:** Uses external configuration files (`secrets.properties`) to protect sensitive API keys.
* **Basic Authentication:** Secures endpoints using Spring Security.

## 🛠️ Tech Stack

* **Language:** Java 17 (or 21)
* **Framework:** Spring Boot 3
* **Database:** H2 Database (In-Memory SQL)
* **ORM:** Spring Data JPA (Hibernate)
* **HTTP Client:** RestTemplate
* **Security:** Spring Security (Basic Auth)
* **Build Tool:** Maven

---

## ⚙️ Getting Started

Follow these steps to run the project locally.

### 1. Configure the API Key (⚠️ Important)
This project uses **ExchangeRate-API**. You need a free key to run it.

1.  Get a free API Key at [exchangerate-api.com](https://www.exchangerate-api.com/).
2.  Create a file named `secrets.properties` in the **root** folder of the project (same level as `pom.xml`).
3.  Add your key inside that file:


### 2. Run the Application
./mvnw spring-boot:run

## 🔐 Authentication
The API is protected by Basic Authentication. You must include these credentials in your requests (Postman/cURL):

* **Username**: admin
* **Password**: admin

(Note: These credentials are configured in application.properties for development purposes).


## 🔌 API Endpoints

### 1. Convert Currency

Calculates the exchange rate and saves the transaction to the database

* **Method**: Post
* **Url**: http://localhost:8080/api/exchange/convert
* **Body (Json)**:
{
    "fromCurrency": "USD",
    "toCurrency": "EUR",
    "amount": 100.00
}
* **Success Response (200 OK)**:
{
    "id": 1,
    "fromCurrency": "USD",
    "toCurrency": "EUR",
    "originalAmount": 100.00,
    "convertedAmount": 92.50,
    "exchangeRate": 0.925,
    "timestamp": "2026-01-19T12:00:00"
}

### 2. View History

Retrieves all past conversions from the database.

* **Method**: GET

* **URL**: http://localhost:8080/api/exchange/history

* **Response**: A JSON array containing all transaction objects.

## 🗄️ Database Console (H2)

Since this project uses H2 (In-Memory), you can access the database directly via the browser while the application is running to verify data.

* **URL**: http://localhost:8080/h2-console

* **JDBC** URL: jdbc:h2:mem:currency_db

* **User**: sa

* **Password**: (Leave empty)


## 📂 Project Structure

The project follows the standard layered architecture:

src/main/java/com/devlearning/currencyconverter
├── config/       # Security and App configurations
├── controller/   # REST Controllers (API Layer)
├── dto/          # Data Transfer Objects (Requests/Responses)
├── model/        # Database Entities
├── repository/   # Data Access Layer (JPA/Hibernate)
└── service/      # Business Logic & External API Calls
