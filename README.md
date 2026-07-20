# Payment Service — SOLID and Clean Architecture

A payment processing API built with **Java 25** and **Spring Boot 4** to demonstrate the practical evolution of a codebase from an intentionally coupled implementation to a solution based on:

* SOLID principles
* Clean Architecture
* Ports and Adapters
* Dependency Inversion
* Use cases
* Automated testing
* Framework-independent application rules

The repository preserves each important refactoring stage in a separate Git branch, making it possible to compare the architectural evolution of the project.

---

## Project Goals

This project was created to demonstrate:

* How common design problems appear in a working application
* How responsibilities can be separated using SRP
* How new payment methods can be added using OCP
* How implementations can respect the Liskov Substitution Principle
* How focused contracts support ISP
* How high-level components can depend on abstractions using DIP
* How to organize an application using Clean Architecture
* How to keep the domain and application layers independent from Spring
* How to preserve behavior while refactoring architecture

---

## Technologies

* Java 25
* Spring Boot 4
* Spring Web MVC
* Bean Validation
* Maven
* JUnit 5
* Lombok

Current persistence:

* In-memory storage using `ConcurrentHashMap`

Planned additions:

* Springdoc OpenAPI
* Swagger UI
* PostgreSQL
* Spring Data JPA
* Flyway
* Testcontainers
* Docker Compose
* GitHub Actions

---

## Business Context

The application represents a simplified payment processing service.

It currently allows clients to:

* Process a payment
* Retrieve a payment by ID
* Use different payment methods
* Store the payment in memory
* Simulate a customer notification

Supported payment methods:

```text
PIX
CREDIT_CARD
BANK_TRANSFER
```

Payment statuses:

```text
PENDING
APPROVED
REJECTED
```

Current processing behavior:

```text
PIX           → APPROVED
CREDIT_CARD   → APPROVED
BANK_TRANSFER → PENDING
```

---

## API Endpoints

| Method | Endpoint                       | Description                     |
| ------ | ------------------------------ | ------------------------------- |
| `POST` | `/api/v1/payments`             | Creates and processes a payment |
| `GET`  | `/api/v1/payments/{paymentId}` | Retrieves a payment by ID       |

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

### Response

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
  "createdAt": "2026-07-20T10:30:00Z"
}
```

---

## Get Payment

### Request

```http
GET /api/v1/payments/48ca7858-a184-412d-9e84-a29e93ca7464
```

### Response

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
  "createdAt": "2026-07-20T10:30:00Z"
}
```

---

## Architectural Evolution

The project started with a functional but intentionally coupled implementation.

The original `PaymentService` was responsible for:

```text
Request validation
Payment creation
Payment method selection
Payment processing
Status changes
Persistence
Notification
Payment retrieval
```

The code was then refactored gradually, with each branch focusing on a specific architectural improvement.

---

## Branch Strategy

### `solid-violations`

Contains the initial implementation with intentional design problems.

Examples:

* Multiple responsibilities inside `PaymentService`
* Payment selection using a `switch`
* Direct dependencies on concrete implementations
* Public status setter
* Persistence and notification details mixed with application flow

### `refactor/srp`

Applies the Single Responsibility Principle.

Responsibilities were separated into components such as:

```text
PaymentValidator
PaymentProcessingService
PaymentNotificationService
PaymentRepository
```

The `PaymentService` became primarily responsible for coordinating the payment flow.

### `refactor/ocp`

Applies the Open/Closed Principle.

The payment method `switch` was replaced by a contract:

```java
public interface PaymentProcessor {

    boolean supports(PaymentMethod paymentMethod);

    void process(Payment payment);
}
```

Each payment method received its own implementation:

```text
PixPaymentProcessor
CreditCardPaymentProcessor
BankTransferPaymentProcessor
```

New processors can be introduced without modifying the central processing flow.

### `refactor/lsp`

Demonstrates the Liskov Substitution Principle through contract tests.

All implementations of `PaymentProcessor` are tested against common expectations:

* They support the declared payment method
* They reject unsupported methods
* They process a compatible payment
* They preserve payment identity and unrelated data
* They return the expected payment status

### Interface Segregation Principle

The project uses small and focused contracts.

Examples:

```text
PaymentProcessor
PaymentRepository
PaymentNotifier
ProcessPayment
GetPayment
```

Implementations are not forced to depend on unrelated operations.

### `refactor/dip`

Applies the Dependency Inversion Principle.

High-level application components depend on abstractions:

```java
private final PaymentRepository paymentRepository;
private final PaymentNotifier paymentNotifier;
```

Concrete implementations are external details:

```text
InMemoryPaymentRepository
ConsolePaymentNotifier
```

The application does not need to know whether payments are stored in memory, PostgreSQL or another external system.

### `refactor/clean-architecture`

Reorganizes the application around architectural boundaries.

The generic `PaymentService` was replaced by independent use cases:

```text
ProcessPaymentUseCase
GetPaymentUseCase
```

The REST controller now depends on input ports instead of a generic service.

---

## Current Architecture

```text
Presentation
     ↓
Input Ports
     ↓
Use Cases
     ↓
Domain

Use Cases
     ↓
Output Ports
     ↑
Infrastructure Adapters
```

The dependency direction points toward the application core.

---

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── com
│   │       └── alexcammarota
│   │           └── payment
│   │               ├── domain
│   │               │   └── model
│   │               │       ├── Payment.java
│   │               │       ├── PaymentMethod.java
│   │               │       └── PaymentStatus.java
│   │               │
│   │               ├── application
│   │               │   ├── command
│   │               │   │   └── ProcessPaymentCommand.java
│   │               │   ├── port
│   │               │   │   ├── input
│   │               │   │   │   ├── ProcessPayment.java
│   │               │   │   │   └── GetPayment.java
│   │               │   │   └── output
│   │               │   │       ├── PaymentRepository.java
│   │               │   │       ├── PaymentNotifier.java
│   │               │   │       └── PaymentProcessor.java
│   │               │   ├── usecase
│   │               │   │   ├── ProcessPaymentUseCase.java
│   │               │   │   └── GetPaymentUseCase.java
│   │               │   ├── validation
│   │               │   │   └── PaymentValidator.java
│   │               │   └── processing
│   │               │       └── PaymentProcessingService.java
│   │               │
│   │               ├── infrastructure
│   │               │   ├── persistence
│   │               │   │   └── InMemoryPaymentRepository.java
│   │               │   ├── notification
│   │               │   │   └── ConsolePaymentNotifier.java
│   │               │   └── payment
│   │               │       ├── PixPaymentProcessor.java
│   │               │       ├── CreditCardPaymentProcessor.java
│   │               │       └── BankTransferPaymentProcessor.java
│   │               │
│   │               ├── presentation
│   │               │   └── rest
│   │               │       ├── PaymentController.java
│   │               │       ├── request
│   │               │       │   └── PaymentRequest.java
│   │               │       └── response
│   │               │           └── PaymentResponse.java
│   │               │
│   │               └── configuration
│   │                   └── BeanConfiguration.java
│   │
│   └── resources
│       └── application.properties
│
└── test
    └── java
        └── com
            └── alexcammarota
                └── payment
```

The exact package names may evolve as the project is refined.

---

## Layer Responsibilities

### Domain

Contains the central business objects:

```text
Payment
PaymentMethod
PaymentStatus
```

The domain does not depend on:

* Spring
* HTTP
* Controllers
* Repositories
* Console output
* Persistence frameworks

### Application

Contains the operations performed by the system.

Input ports:

```text
ProcessPayment
GetPayment
```

Use cases:

```text
ProcessPaymentUseCase
GetPaymentUseCase
```

Output ports:

```text
PaymentRepository
PaymentNotifier
PaymentProcessor
```

The application layer describes what the system needs without depending on technical implementations.

### Infrastructure

Contains implementations of output ports.

Examples:

```text
InMemoryPaymentRepository
ConsolePaymentNotifier
PixPaymentProcessor
CreditCardPaymentProcessor
BankTransferPaymentProcessor
```

Infrastructure components can be replaced without changing the use cases.

### Presentation

Contains the REST API.

Responsibilities:

* Receive HTTP requests
* Validate request formats
* Convert requests into application commands
* Invoke input ports
* Convert domain results into HTTP responses

The presentation layer contains:

```text
PaymentController
PaymentRequest
PaymentResponse
```

### Configuration

Contains the Spring composition root.

`BeanConfiguration` creates and connects application objects without adding Spring annotations to the use cases.

Example:

```java
@Bean
ProcessPayment processPayment(
        PaymentValidator paymentValidator,
        PaymentProcessingService paymentProcessingService,
        PaymentRepository paymentRepository,
        PaymentNotifier paymentNotifier
) {
    return new ProcessPaymentUseCase(
            paymentValidator,
            paymentProcessingService,
            paymentRepository,
            paymentNotifier
    );
}
```

---

## Request and Application Command

The REST request belongs to the presentation layer:

```text
PaymentRequest
```

The use case receives an application command:

```text
ProcessPaymentCommand
```

Flow:

```text
JSON
  ↓
PaymentRequest
  ↓
ProcessPaymentCommand
  ↓
ProcessPaymentUseCase
```

This prevents the application layer from depending on HTTP-specific models.

---

## Use Cases

### Process Payment

`ProcessPaymentUseCase` is responsible for coordinating the complete payment flow:

```text
Validate command
Create payment
Select payment processor
Process payment
Persist payment
Send notification
Return payment
```

### Get Payment

`GetPaymentUseCase` retrieves a payment through the repository abstraction.

It does not know whether the implementation uses:

```text
ConcurrentHashMap
PostgreSQL
MongoDB
External API
```

---

## Ports and Adapters

### Input Ports

Represent operations exposed by the application:

```text
ProcessPayment
GetPayment
```

The REST controller invokes these ports.

### Output Ports

Represent external capabilities required by the use cases:

```text
PaymentRepository
PaymentNotifier
PaymentProcessor
```

### Input Adapter

```text
PaymentController
```

### Output Adapters

```text
InMemoryPaymentRepository
ConsolePaymentNotifier
PixPaymentProcessor
CreditCardPaymentProcessor
BankTransferPaymentProcessor
```

---

## Dependency Rule

Allowed dependencies:

```text
presentation → application
application → domain
infrastructure → application
infrastructure → domain
configuration → all layers
```

Dependencies that must not exist:

```text
domain → application
domain → infrastructure
domain → presentation

application → infrastructure
application → presentation
```

The domain and application layers must remain independent from Spring whenever possible.

---

## Running the Application

### Requirements

* Java 25
* Git

The project includes Maven Wrapper, so a local Maven installation is not required.

### Clone

```bash
git clone https://github.com/YOURalexcammarota/payment-service-clean-architecture-java.git
cd payment-service-clean-architecture-java
```

### Run on Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Run on Linux or macOS

```bash
./mvnw spring-boot:run
```

The application will be available at:

```text
http://localhost:8080
```

---

## Running the Tests

### Windows

```powershell
.\mvnw.cmd test
```

### Linux or macOS

```bash
./mvnw test
```

---

## Testing Strategy

The current test suite includes contract tests for payment processor implementations.

Examples:

```text
PIX processor must support PIX
Credit card processor must support CREDIT_CARD
Bank transfer processor must support BANK_TRANSFER
Processors must reject unsupported methods
Processors must preserve payment identity
Processors must produce the expected status
```

Planned tests:

* Domain rule tests
* Process payment use case tests
* Get payment use case tests
* Controller tests
* Repository integration tests
* PostgreSQL tests with Testcontainers

---

## Current Limitations

The current project is intentionally simplified.

Current limitations include:

* In-memory persistence
* Console-based notifications
* Simulated payment processing
* Generic exception handling
* No authentication
* No idempotency control
* No database transactions
* No external payment provider
* No refund operation

These limitations keep the initial focus on architecture and design principles.

---

## Roadmap

### Completed

* [x] Create initial Spring Boot project
* [x] Implement payment creation
* [x] Implement payment retrieval
* [x] Add in-memory persistence
* [x] Create intentional SOLID violations
* [x] Apply SRP
* [x] Apply OCP
* [x] Add LSP contract tests
* [x] Use focused contracts following ISP
* [x] Apply DIP to repository and notification
* [x] Replace the generic service with independent use cases
* [x] Separate presentation models from application commands
* [x] Organize the project using Clean Architecture
* [x] Configure application dependencies through Spring configuration

### Next Steps

* [ ] Move payment invariants into the domain
* [ ] Replace public `setStatus()` with domain methods
* [ ] Add `approve()` and `reject()` behavior
* [ ] Add domain unit tests
* [ ] Add use case unit tests
* [ ] Add controller tests
* [ ] Add consistent exception responses
* [ ] Add Springdoc OpenAPI and Swagger UI
* [ ] Add PostgreSQL persistence adapter
* [ ] Add Flyway migrations
* [ ] Add Testcontainers
* [ ] Add Docker Compose
* [ ] Add GitHub Actions
* [ ] Add architecture diagrams
* [ ] Implement a Python version of the same domain

---

## Next Domain Refactoring

The current `Payment` model still exposes a public status setter:

```java
payment.setStatus(PaymentStatus.APPROVED);
```

A future refactoring will replace it with domain behavior:

```java
payment.approve();
payment.reject();
```

This will prevent invalid state transitions and keep payment rules inside the domain.

---

## Architectural Decisions

### Why replace `PaymentService`?

The original `PaymentService` contained multiple application operations.

It was replaced by:

```text
ProcessPaymentUseCase
GetPaymentUseCase
```

Each use case now represents one application operation and one main reason to change.

### Why use input ports?

The controller depends on application operations:

```text
ProcessPayment
GetPayment
```

This keeps the controller separated from concrete use case implementations.

### Why use output ports?

Use cases require persistence, notifications and payment processing, but they do not depend on technical implementations.

### Why keep requests separate from commands?

`PaymentRequest` represents an HTTP contract.

`ProcessPaymentCommand` represents an application operation.

The separation prevents HTTP concerns from leaking into the application layer.

### Why configure use cases manually?

Use cases do not require Spring annotations.

`BeanConfiguration` acts as the composition root and connects the application to infrastructure implementations.

---

## Java and Python Implementations

The same payment domain is planned in two repositories:

```text
payment-service-clean-architecture-java
payment-service-clean-architecture-python
```

The Python version will apply the same principles while respecting the characteristics of Python and FastAPI.

The goal is not to reproduce Java code line by line, but to compare:

* Dependency injection
* Contracts
* Structural typing
* Domain modeling
* Framework isolation
* Testing strategies
* Boilerplate
* Runtime validation

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
