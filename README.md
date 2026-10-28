A Spring Boot backend that fetches cryptocurrency prices from CoinGecko (in INR) and is designed to grow into a portfolio tracker with price alerts and client accounts.

## Tech Stack

- Java 17+ and Spring Boot 4.1.1
- Spring `RestClient` for calling the CoinGecko API
- Caffeine cache to reduce API calls
- PostgreSQL for storage
- JUnit 5 and Mockito for tests
- Maven

## Planned Features

- Price lookup
- Holdings CRUD
- Portfolio value and profit/loss
- Coin search
- Analytics
- Price alerts
- Client accounts with API keys
- Docker support
- Deployment on AWS

## Prerequisites

- JDK 17 or newer
- PostgreSQL installed and running
- A free CoinGecko Demo API key (create one at coingecko.com)
- IntelliJ IDEA (optional, any editor works)

## Setup

1. Clone the project and open the `Market_Data_Aggregator` folder.
2. Create an empty PostgreSQL database:

   ```sql
   CREATE DATABASE market_data;
   ```

3. Open `src/main/resources/application.properties` and set your values:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/market_data
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update

   coingecko.base-url=https://api.coingecko.com/api/v3
   coingecko.api-key=YOUR_DEMO_API_KEY
   ```

   The `coingecko.*` names must match the property names used in your code.

4. Do not commit real passwords or API keys. Use environment variables or a local, git-ignored config file instead.

## Running the Application

With Maven from the project folder:

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Or in IntelliJ: open `MarketDataAggregatorApplication` and click the green Run button.

The app starts on `http://localhost:8080` by default.

## Running Tests

```bash
./mvnw test
```

## Project Structure

```
com.example.Market_Data_Aggregator
├── config       configuration classes (beans, cache, security)
├── controller   REST endpoints
├── exceptions   custom exceptions and global error handling
├── provider     market data providers
├── service      business logic
├── client       CoinGecko RestClient
├── repository   database access
└── model        entities and DTOs
```

## Roadmap

1. Finish price lookup, holdings, portfolio and alerts
2. Add client accounts with API key authentication
3. Add security hardening
4. Containerize with Docker
5. Deploy to AWS
