# Payment Service — SOLID and Clean Architecture

A payment processing API developed with **Java 25** and **Spring Boot 4** to demonstrate the practical application of:

* SOLID principles
* Clean Architecture
* Ports and Adapters
* Dependency Inversion
* Domain modeling
* Automated testing
* REST API design
* OpenAPI contract documentation

This repository also demonstrates the architectural evolution from an intentionally coupled implementation to a modular and testable solution.

---

## Project Overview

The application represents a simplified payment processing service.

It allows clients to:

* Create and process payments
* Retrieve payment information
* Process different payment methods
* Reject invalid payments
* Refund approved payments
* Track the payment lifecycle
* Receive payment notifications

The project focuses primarily on architecture and software design rather than building a production-ready financial platform.

---

## Repository

```text
payment-service-clean-architecture-java
```

The same business problem will also be implemented in Python:

```text
payment-service-clean-architecture-python
```

The objective is to demonstrate how SOLID and Clean Architecture can be applied in different languages without simply translating one implementation line by line.

---

## Technologies

### Core

* Java 25
* Spring Boot 4
* Spring Web MVC
* Bean Validation
* Maven

### API documentation

* OpenAPI 3
* Springdoc OpenAPI 3
* Swagger UI

### Testing

* JUnit 5
* Mockito
* Spring Boot Test
* MockMvc

### Planned infrastructure

* PostgreSQL
* Spring Data JPA
* Flyway
* Testcontainers
* Docker Compose
* GitHub Actions

---

## Business Context

A client sends a payment request containing:

* Customer identification
* Payment amount
* Currency
* Payment method
* Payment method information

The application then:

1. Validates the request
2. Validates the business rules
3. Creates the payment
4. Selects the appropriate payment processor
5. Processes the payment
6. Updates its status
7. Persists the result
8. Sends a notification

Example:

```text
Payment request
       ↓
Request validation
       ↓
Business rule validation
       ↓
Payment processor selection
       ↓
External payment processing
       ↓
Payment persistence
       ↓
Customer notification
```

---

## Supported Payment Methods

The initial implementation may support:

```text
PIX
CREDIT_CARD
BANK_TRANSFER
```

Each method is processed through its own implementation of a payment processing contract.

New payment methods should be added without modifying the central payment use case.

---

## Payment Statuses

A payment can have the following statuses:

```text
PENDING
PROCESSING
APPROVED
REJECTED
REFUNDED
```

Example lifecycle:

```text
PENDING
   ↓
PROCESSING
   ├──→ APPROVED
   └──→ REJECTED

APPROVED
   ↓
REFUNDED
```

Not every transition is valid.

For example:

* A rejected payment cannot be refunded
* A refunded payment cannot be refunded again
* An approved payment cannot return to pending
* A payment must have a positive amount

These rules belong to the domain layer.

---

## API Endpoints

| Method | Endpoint                               | Description                     |
| ------ | -------------------------------------- | ------------------------------- |
| `POST` | `/api/v1/payments`                     | Creates and processes a payment |
| `GET`  | `/api/v1/payments/{paymentId}`         | Retrieves a payment by ID       |
| `POST` | `/api/v1/payments/{paymentId}/refunds` | Refunds an approved payment     |

---

## Create Payment

### Request

```http
POST /api/v1/payments
Content-Type: application/json
```

```json
{
  "customerId": "84c32967-11d8-4c50-b92a-77289ce5765a",
  "amount": 150.50,
  "currency": "BRL",
  "paymentMethod": "PIX"
}
```

### Successful response

```http
HTTP/1.1 201 Created
```

```json
{
  "paymentId": "48ca7858-a184-412d-9e84-a29e93ca7464",
  "customerId": "84c32967-11d8-4c50-b92a-77289ce5765a",
  "amount": 150.50,
  "currency": "BRL",
  "paymentMethod": "PIX",
  "status": "APPROVED",
  "createdAt": "2026-07-15T10:30:00Z"
}
```

---

## Get Payment

### Request

```http
GET /api/v1/payments/48ca7858-a184-412d-9e84-a29e93ca7464
```

### Successful response

```http
HTTP/1.1 200 OK
```

```json
{
  "paymentId": "48ca7858-a184-412d-9e84-a29e93ca7464",
  "customerId": "84c32967-11d8-4c50-b92a-77289ce5765a",
  "amount": 150.50,
  "currency": "BRL",
  "paymentMethod": "PIX",
  "status": "APPROVED",
  "createdAt": "2026-07-15T10:30:00Z"
}
```

---

## Refund Payment

### Request

```http
POST /api/v1/payments/48ca7858-a184-412d-9e84-a29e93ca7464/refunds
```

### Successful response

```http
HTTP/1.1 200 OK
```

```json
{
  "paymentId": "48ca7858-a184-412d-9e84-a29e93ca7464",
  "status": "REFUNDED",
  "refundedAt": "2026-07-15T11:00:00Z"
}
```

---

## API Documentation

The API contracts are generated using Springdoc OpenAPI.

After starting the application, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

The OpenAPI specification in JSON format is available at:

```text
http://localhost:8080/v3/api-docs
```

The specification can be used by:

* Swagger UI
* Postman
* API clients
* Contract validation tools
* Automated client generators

---

## Architecture

The project follows Clean Architecture and Ports and Adapters principles.

```text
                    External world
                          │
                          ▼
                  ┌─────────────────┐
                  │  Presentation   │
                  │ REST Controllers│
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │   Application   │
                  │    Use Cases    │
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │     Domain      │
                  │ Business Rules  │
                  └─────────────────┘

                  ┌─────────────────┐
                  │ Infrastructure  │
                  │ DB / Providers  │
                  └────────┬────────┘
                           │
                    Implements ports
```

The central dependency rule is:

```text
Dependencies must point toward the business rules.
```

The domain does not depend on:

* Spring
* Controllers
* JPA
* Databases
* OpenAPI
* Swagger
* External payment providers
* Notification libraries
* HTTP clients

---

## Dependency Flow

```text
presentation → application → domain

infrastructure → application
infrastructure → domain
```

The domain must not import infrastructure or presentation components.

The application layer defines the contracts required to perform its operations. Infrastructure provides concrete implementations of those contracts.

---

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── com
│   │       └── alexcammarota
│   │           └── payment_service
│   │               ├── PaymentServiceApplication.java
│   │               │
│   │               ├── domain
│   │               │   ├── entity
│   │               │   │   └── Payment.java
│   │               │   ├── valueobject
│   │               │   │   ├── PaymentId.java
│   │               │   │   ├── CustomerId.java
│   │               │   │   └── Money.java
│   │               │   ├── enumeration
│   │               │   │   ├── PaymentMethod.java
│   │               │   │   └── PaymentStatus.java
│   │               │   └── exception
│   │               │
│   │               ├── application
│   │               │   ├── usecase
│   │               │   │   ├── ProcessPaymentUseCase.java
│   │               │   │   ├── GetPaymentUseCase.java
│   │               │   │   └── RefundPaymentUseCase.java
│   │               │   ├── command
│   │               │   ├── result
│   │               │   └── port
│   │               │       ├── input
│   │               │       └── output
│   │               │           ├── PaymentRepository.java
│   │               │           ├── PaymentProcessor.java
│   │               │           └── NotificationGateway.java
│   │               │
│   │               ├── infrastructure
│   │               │   ├── persistence
│   │               │   │   ├── entity
│   │               │   │   ├── repository
│   │               │   │   └── adapter
│   │               │   ├── processor
│   │               │   │   ├── PixPaymentProcessor.java
│   │               │   │   ├── CreditCardPaymentProcessor.java
│   │               │   │   └── BankTransferPaymentProcessor.java
│   │               │   └── notification
│   │               │       └── EmailNotificationAdapter.java
│   │               │
│   │               ├── presentation
│   │               │   └── rest
│   │               │       ├── controller
│   │               │       ├── request
│   │               │       ├── response
│   │               │       └── exception
│   │               │
│   │               └── configuration
│   │                   └── BeanConfiguration.java
│   │
│   └── resources
│       └── application.yml
│
└── test
    └── java
        └── com
            └── alexcammarota
                └── payment
```

---

## Layer Responsibilities

### Domain

The domain contains the core business concepts and rules.

Examples:

```text
Payment
PaymentId
CustomerId
Money
PaymentMethod
PaymentStatus
```

Responsibilities include:

* Creating a valid payment
* Validating payment amounts
* Controlling status transitions
* Approving a payment
* Rejecting a payment
* Refunding an approved payment

The domain is implemented with plain Java and does not require Spring to be tested.

---

### Application

The application layer contains the use cases that coordinate the domain.

Examples:

```text
ProcessPaymentUseCase
GetPaymentUseCase
RefundPaymentUseCase
```

It defines input and output ports.

Examples of output ports:

```text
PaymentRepository
PaymentProcessor
NotificationGateway
```

The application layer determines what must happen but does not know the technical details of how it happens.

---

### Infrastructure

The infrastructure layer contains technical implementations.

Examples:

```text
JpaPaymentRepositoryAdapter
PixPaymentProcessor
CreditCardPaymentProcessor
BankTransferPaymentProcessor
EmailNotificationAdapter
```

This layer may depend on:

* Spring
* JPA
* PostgreSQL
* HTTP clients
* External SDKs
* Notification providers

Infrastructure can be replaced without changing the domain rules.

---

### Presentation

The presentation layer exposes the application through HTTP.

Responsibilities include:

* Receiving HTTP requests
* Validating request formats
* Converting requests into application commands
* Invoking use cases
* Converting application results into HTTP responses
* Mapping exceptions to HTTP status codes
* Exposing OpenAPI documentation

Controllers must not contain business rules.

---

### Configuration

The configuration layer assembles dependencies.

Example:

```java
@Configuration
public class BeanConfiguration {

    @Bean
    ProcessPaymentUseCase processPaymentUseCase(
            PaymentRepository paymentRepository,
            PaymentProcessorResolver paymentProcessorResolver,
            NotificationGateway notificationGateway
    ) {
        return new ProcessPaymentUseCase(
                paymentRepository,
                paymentProcessorResolver,
                notificationGateway
        );
    }
}
```

This allows the application and domain classes to remain independent from Spring annotations.

---

## SOLID Principles

### Single Responsibility Principle

A class should have one primary reason to change.

An intentionally coupled service might:

* Validate requests
* Apply business rules
* Select payment methods
* Call payment providers
* Persist payments
* Send notifications
* Build HTTP responses

The final architecture separates those responsibilities:

```text
PaymentController
    → HTTP communication

ProcessPaymentUseCase
    → Application workflow

Payment
    → Business rules

PaymentProcessor
    → Payment processing contract

PaymentRepository
    → Persistence contract

NotificationGateway
    → Notification contract
```

---

### Open/Closed Principle

The application must be open for extension but closed for modification.

Payment methods follow a common contract:

```java
public interface PaymentProcessor {

    boolean supports(PaymentMethod paymentMethod);

    PaymentProcessingResult process(Payment payment);
}
```

Implementations:

```text
PixPaymentProcessor
CreditCardPaymentProcessor
BankTransferPaymentProcessor
```

A new processor can be created without adding another conditional block to the central use case.

For example:

```text
CryptocurrencyPaymentProcessor
```

The existing use case should not need to be modified to support it.

---

### Liskov Substitution Principle

Implementations of an abstraction must respect the behavior expected by its clients.

Every `PaymentProcessor` implementation must:

* Support only its declared payment method
* Return a valid processing result
* Preserve domain invariants
* Use the expected exception model
* Avoid changing unrelated application state

The use case must be able to use any valid processor without depending on its concrete type.

---

### Interface Segregation Principle

Clients should not depend on operations they do not use.

Instead of creating one large interface:

```java
public interface PaymentService {

    void processPayment();

    void refundPayment();

    void sendEmail();

    void sendSms();

    void savePayment();

    void generateReport();
}
```

The application defines focused contracts:

```text
PaymentProcessor
RefundProcessor
PaymentRepository
NotificationGateway
```

Each implementation depends only on the operations it requires.

---

### Dependency Inversion Principle

High-level business policies must not depend directly on low-level technical details.

The use case depends on abstractions:

```java
public final class ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentProcessorResolver processorResolver;
    private final NotificationGateway notificationGateway;

    public ProcessPaymentUseCase(
            PaymentRepository paymentRepository,
            PaymentProcessorResolver processorResolver,
            NotificationGateway notificationGateway
    ) {
        this.paymentRepository = paymentRepository;
        this.processorResolver = processorResolver;
        this.notificationGateway = notificationGateway;
    }
}
```

It does not depend directly on:

```text
JpaPaymentRepository
StripeClient
PixProviderClient
JavaMailSender
```

Those technical components implement contracts defined closer to the application core.

---

## Ports and Adapters

### Input ports

Input ports represent operations exposed by the application.

Examples:

```text
ProcessPayment
GetPayment
RefundPayment
```

REST controllers invoke these ports.

### Output ports

Output ports represent external capabilities required by the application.

Examples:

```text
PaymentRepository
PaymentProcessor
NotificationGateway
```

Infrastructure adapters implement these ports.

### Input adapters

Examples:

```text
PaymentController
RefundController
```

### Output adapters

Examples:

```text
JpaPaymentRepositoryAdapter
PixPaymentProcessor
EmailNotificationAdapter
```

---

## Intentional SOLID Violations

The repository contains an intentionally coupled version of the application.

This implementation may include:

* One service with several responsibilities
* Direct dependency on concrete classes
* Payment selection through large `if` or `switch` blocks
* Framework annotations inside business objects
* Persistence entities used directly as API responses
* Business rules inside controllers
* Large and generic interfaces
* Difficult-to-isolate unit tests

Example:

```java
@Service
public class PaymentService {

    private final JpaPaymentRepository repository;
    private final PixClient pixClient;
    private final CreditCardClient creditCardClient;
    private final JavaMailSender mailSender;

    public PaymentResponse process(PaymentRequest request) {
        // Validation
        // Domain rules
        // Processor selection
        // External provider call
        // Persistence
        // Notification
        // Response mapping
    }
}
```

The purpose is to show why the architecture is refactored, not to present this code as a recommended solution.

---

## Branch Strategy

### `main`

Contains the final implementation following SOLID and Clean Architecture.

### `solid-violations`

Contains the initial implementation with intentional design problems.

### Suggested refactoring branches

```text
refactor/srp
refactor/ocp
refactor/lsp
refactor/isp
refactor/dip
refactor/clean-architecture
```

Each branch can isolate one stage of the architectural evolution.

---

## Suggested Commit History

```text
chore: initialize Spring Boot project

feat: add initial payment processing endpoint
test: add tests for initial payment service

refactor: separate payment responsibilities following SRP
refactor: introduce extensible processors following OCP
test: verify processor substitutability following LSP
refactor: split payment contracts following ISP
refactor: invert infrastructure dependencies following DIP

refactor: organize application using clean architecture
test: add domain unit tests
test: add use case unit tests
test: add controller integration tests

docs: document architecture and SOLID decisions
ci: add GitHub Actions workflow
```

---

## Getting Started

### Requirements

* Java 25
* Git

Maven does not need to be installed because the repository includes Maven Wrapper.

Verify the Java installation:

```bash
java -version
```

Expected major version:

```text
25
```

---

## Clone the Repository

```bash
git clone https://github.com/YOURalexcammarota/payment-service-clean-architecture-java.git
cd payment-service-clean-architecture-java
```

## Run the Application

### Linux or macOS

```bash
./mvnw spring-boot:run
```

### Windows PowerShell

```powershell
.\mvnw.cmd spring-boot:run
```

The application will be available at:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

---

## Run the Tests

### Linux or macOS

```bash
./mvnw test
```

### Windows PowerShell

```powershell
.\mvnw.cmd test
```

---

## Build the Application

### Linux or macOS

```bash
./mvnw clean verify
```

### Windows PowerShell

```powershell
.\mvnw.cmd clean verify
```

The generated JAR will be available inside:

```text
target/
```

Run it with:

```bash
java -jar target/payment-service-*.jar
```

---

## Springdoc OpenAPI

For Spring Boot 4, the project uses Springdoc OpenAPI 3.

Maven dependency:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.0.3</version>
</dependency>
```

The OpenAPI integration belongs to the presentation layer.

Domain and application classes must not depend on Swagger annotations.

---

## Testing Strategy

The test suite is divided according to the architectural boundaries.

### Domain unit tests

Examples:

```text
Should create a payment with a positive amount
Should reject a payment with a zero amount
Should reject a payment with a negative amount
Should approve a pending payment
Should reject a pending payment
Should refund an approved payment
Should not refund a rejected payment
Should not refund a payment twice
```

These tests do not require Spring.

---

### Application unit tests

Examples:

```text
Should select the PIX processor
Should select the credit card processor
Should persist an approved payment
Should persist a rejected payment
Should notify the customer after approval
Should not process an unsupported payment method
Should return an existing payment
Should throw an error when the payment does not exist
```

Infrastructure dependencies are replaced with test doubles.

---

### Controller tests

Examples:

```text
Should return 201 when a payment is created
Should return 400 for an invalid request
Should return 404 when a payment does not exist
Should return 200 when a payment is refunded
Should return 409 for an invalid payment transition
```

---

### Integration tests

Planned integration tests include:

```text
PostgreSQL persistence with Testcontainers
Spring dependency configuration
REST endpoint integration
OpenAPI endpoint availability
Database migrations
```

---

## Error Responses

The API uses a consistent error structure.

Example:

```json
{
  "timestamp": "2026-07-15T10:35:00Z",
  "status": 400,
  "error": "Bad Request",
  "code": "INVALID_PAYMENT_AMOUNT",
  "message": "Payment amount must be greater than zero",
  "path": "/api/v1/payments"
}
```

Possible errors:

| HTTP status | Code                     | Description                             |
| ----------- | ------------------------ | --------------------------------------- |
| `400`       | `INVALID_PAYMENT_AMOUNT` | Payment amount is invalid               |
| `400`       | `INVALID_PAYMENT_METHOD` | Payment method is invalid               |
| `404`       | `PAYMENT_NOT_FOUND`      | Payment was not found                   |
| `409`       | `INVALID_PAYMENT_STATUS` | Requested status transition is invalid  |
| `422`       | `PAYMENT_REJECTED`       | Payment provider rejected the operation |
| `500`       | `INTERNAL_ERROR`         | Unexpected application error            |

---

## Architectural Decisions

### Why Java 25?

Java 25 was selected as the language version for the project so the implementation can use the current Java platform while demonstrating modern backend development practices.

The architecture does not depend on language novelty. The focus remains on clear boundaries, business rules and dependency management.

---

### Why Spring Boot?

Spring Boot is responsible for:

* Starting the application
* Exposing HTTP endpoints
* Dependency injection
* Configuration
* Framework integrations

Spring Boot is treated as an external delivery and composition mechanism.

The domain does not require Spring to function.

---

### Why Maven?

Maven provides:

* Standardized project structure
* Dependency management
* Build lifecycle
* Test execution
* Maven Wrapper
* Wide adoption in Java projects

---

### Why not annotate domain classes with Spring?

Domain objects should describe the business independently from the framework used to execute the application.

Avoiding Spring dependencies in the domain improves:

* Testability
* Portability
* Maintainability
* Business rule clarity
* Infrastructure replacement
* Architectural boundaries

---

### Why separate API models from domain models?

HTTP requests and responses are external contracts.

Domain objects represent internal business concepts.

Separating them prevents changes in the API from unnecessarily changing the domain and prevents transport concerns from leaking into business rules.

---

### Why avoid Lombok initially?

The project avoids Lombok during the initial implementation to keep generated behavior explicit and make the object design easier to understand during the architectural study.

Lombok may be evaluated later if it provides a clear advantage without hiding relevant design decisions.

---

## Roadmap

### Foundation

* [ ] Generate the project with Spring Initializr
* [ ] Configure Java 25
* [ ] Add Spring Web
* [ ] Add Bean Validation
* [ ] Add Springdoc OpenAPI
* [ ] Configure Swagger UI
* [ ] Add the MIT License

### Initial implementation

* [ ] Implement the intentionally coupled payment service
* [ ] Add payment creation
* [ ] Add payment retrieval
* [ ] Add payment refund
* [ ] Add basic automated tests
* [ ] Publish the implementation in `solid-violations`

### SOLID refactoring

* [ ] Apply the Single Responsibility Principle
* [ ] Apply the Open/Closed Principle
* [ ] Demonstrate the Liskov Substitution Principle
* [ ] Apply the Interface Segregation Principle
* [ ] Apply the Dependency Inversion Principle
* [ ] Document each violation and refactoring

### Clean Architecture

* [ ] Separate domain, application, presentation and infrastructure
* [ ] Create input and output ports
* [ ] Implement infrastructure adapters
* [ ] Configure dependency composition
* [ ] Protect dependency direction with tests

### Testing and infrastructure

* [ ] Add domain unit tests
* [ ] Add use case unit tests
* [ ] Add controller tests
* [ ] Add PostgreSQL
* [ ] Add Flyway migrations
* [ ] Add Testcontainers
* [ ] Add Docker Compose
* [ ] Add GitHub Actions

### Documentation

* [ ] Add an architecture diagram
* [ ] Add request and response examples
* [ ] Document architectural decisions
* [ ] Add a comparison between the two implementations
* [ ] Link the Python repository

---

## Java and Python Implementations

The same payment domain will be implemented in two repositories:

```text
payment-service-clean-architecture-java
payment-service-clean-architecture-python
```

Both projects should expose equivalent business capabilities:

```text
Process payment
Retrieve payment
Refund payment
Support multiple payment methods
Persist payment state
Send notifications
```

The implementations will respect the characteristics of each ecosystem.

### Java

Expected technologies:

```text
Java 25
Spring Boot
JUnit
Mockito
Maven
```

Java will use explicit contracts, interfaces and dependency composition.

### Python

Expected technologies:

```text
Python
FastAPI
Pydantic
Pytest
SQLAlchemy
```

Python may use:

* Protocols
* Abstract base classes
* Structural typing
* Dataclasses
* Constructor injection
* Callable dependencies

The Python implementation should not reproduce unnecessary Java verbosity.

---

## Comparing the Implementations

When both repositories are available, include links here:

```markdown
- [Java implementation](JAVA_REPOSITORY_URL)
- [Python implementation](PYTHON_REPOSITORY_URL)
```

Topics to compare:

* Interface definition
* Dependency injection
* Domain modeling
* Type safety
* Framework integration
* Testing strategies
* Error handling
* Boilerplate
* Runtime validation
* Extension of payment methods

---

## Contributing

This is primarily an educational and portfolio project.

Suggestions, discussions and pull requests related to architecture, design and testing are welcome.

Before opening a pull request:

1. Run the tests
2. Keep the domain independent from frameworks
3. Respect the dependency direction
4. Add tests for new behavior
5. Explain relevant architectural decisions

---

## License

This project is licensed under the MIT License.

See the [`LICENSE`](LICENSE) file for details.

---

## Author

**Alex Cammarota**

Backend software developer working with:

* Java
* Python
* REST APIs
* Microservices
* AWS
* Event-driven systems
* Software architecture
* Automated testing
