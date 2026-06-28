# Trello REST API Enterprise Automation Framework

An enterprise-grade, production-ready API test automation framework engineered to validate the core routing, state mutations, and structural contracts of the Trello REST API ecosystem. Built using Java 17, Rest Assured 6.0.0, JUnit 5 (Jupiter), and the Gradle 9.0.0 build automation engine, this architecture acts as a pipeline-ready validation harness. It applies strict software engineering patterns, decoupled modular configurations, and decoupled data layers to eliminate basic scripting inefficiencies.  

Rather than relying on static, thread-unsafe configurations or duplicated assertions, this codebase demonstrates custom map-based parameterisation models, dynamic request-method specification builders, strict JSON Schema contract enforcement, and multi-stage lifecycle test isolates.  

---

## Technical Architecture & Core Design Patterns

The automation engine implements several high-performance design patterns modeled after backend software standards:

* **OOP Request Specification Inheritance:** Implemented via a dynamic, method-level specification factory (`requestWithAuth()` and `requestWithoutAuth()`) inside a core `BaseTest` layer. This design avoids shared static bottlenecks, making the suite safe for concurrent and parallel test execution engines.
* **Decoupled Domain Constant Isolation:** All routing paths, resource endpoints, and test parameters are fully abstracted from the test logic into independent, read-only configuration directories (`BoardsEndpoints`, `CardsEndpoints`, and `UrlParamValues`).  
* **Complex Map-Based Parameterisation:** Leverages custom JUnit 5 `ArgumentsProvider` and `ArgumentsHolder` classes to deliver dynamic parameter maps. This isolates test data from validation blocks, allowing assertions to run against dynamic maps containing varying paths, queries, payloads, status codes, and error messages.  
* **Strict Structural Contract Testing:** Integrates Rest Assured's `json-schema-validator` module to validate response body payloads against draft-04 JSON Schema templates. This ensures the structure, data types, and required fields of Trello's APIs match the structural contract regardless of changing values.  
* **Zero-Trust Environment Isolation:** Keeps confidential credentials out of the codebase. It uses Gradle properties mapping and JVM system properties to inject environment variables dynamically.  

---

## Repository Package Structure

The repository is organized using clean architecture conventions, keeping test cases separate from configuration logic and reusable data structures:

```text
trello-api-automation-framework/
├── .github/
│   └── workflows/
│       └── ci.yml                   # CI/CD pipeline configuration
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar       # Gradle wrapper binary execution artifact [11]
│       └── gradle-wrapper.properties# Gradle wrapper properties version specification [11]
├── src/
│   └── test/
│       ├── java/
│       │   ├── arguments/
│       │   │   ├── holders/         # Reusable POJO data carriers for parameterized map streams 
│       │   │   │   ├── AuthValidationArgumentsHolder.java
│       │   │   │   ├── BoardIdValidationArgumentsHolder.java
│       │   │   │   ├── CardBodyValidationArgumentsHolder.java
│       │   │   │   └── CardIdValidationArgumentsHolder.java
│       │   │   └── providers/       # Advanced JUnit 5 ArgumentsProviders supplying map contexts 
│       │   │       ├── AuthValidationArgumentsProvider.java
│       │   │       ├── BoardIdValidationArgumentsProvider.java
│       │   │       ├── BoardNameValidationArgumentProvider.java
│       │   │       ├── CardBodyValidationArgumentsProvider.java
│       │   │       └── CardIdValidationArgumentsProvider.java
│       │   ├── base/
│       │   │   ├── BaseTest.java    # Core test parent managing dynamic request specifications
│       │   │   └── ConfigLoader.java# Dynamic JVM properties and system environment resolver 
│       │   ├── consts/
│       │   │   ├── BoardsEndpoints.java # Target Board endpoint routes 
│       │   │   ├── CardsEndpoints.java  # Target Card endpoint routes 
│       │   │   └── UrlParamValues.java  # Read-only test parameters, keys, and token definitions 
│       │   └── test/                # Domain-isolated test cases organized by CRUD operations
│       │       ├── create/
│       │       │   ├── CreateBoardTest.java
│       │       │   ├── CreateBoardValidationTest.java
│       │       │   ├── CreateCardTest.java
│       │       │   └── CreateCardValidationTest.java
│       │       ├── delete/
│       │       │   ├── deleteBoardTest.java
│       │       │   └── deleteCardTest.java
│       │       ├── get/
│       │       │   ├── GetBoardsTest.java
│       │       │   └── GetBoardsValidationTest.java
│       │       └── update/          # Update test packages validating state modifications
│       └── resources/
│           └── schemas/             # Classpath-resolved draft-04 JSON Schema contract files 
│               ├── get_board.json
│               ├── get_boards.json
│               └── get_card.json
├── .gitignore                       # Multi-IDE and environment properties ignore profile [12]
├── build.gradle                     # Gradle build configurations and runtime system property injections 
└── settings.gradle                  # Gradle root project initialization descriptor [13]
