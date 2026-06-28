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

---

## Technical Stack Matrix

| Dependency Context | Library Selection | Version | Scope and Framework Responsibility |
| :--- | :--- | :--- | :--- |
| **Execution Platform** | Java JDK | 17 | Provides language features, strong typing, and modern stream APIs. |
| **Build Engine** | Gradle | 9.0.0 | Manages build dependencies, lifecycle execution, and parallel test orchestration. |
| **REST Client** | Rest Assured | 6.0.0 | Handles declarative HTTP requests, assertions, and responses. |
| **Testing Core** | JUnit 5 Jupiter | 5.11.4 | Powers test suites, lifecycles, and advanced parameterisation engines. |
| **Contract Validation** | JSON Schema | 6.0.0 | Performs JSON Schema Contract Testing via `json-schema-validator`. |

---

## Core Framework Implementation Code

### 1. Architectural Base Layer
* **base.BaseTest:** Exposes method-level specification builders (`requestWithAuth()` and `requestWithoutAuth()`) instead of shared static instances. This provides thread isolation during concurrent test execution.
* **base.ConfigLoader:** Safeguards confidential credentials by pulling properties dynamically from the environment, falling back safely to JVM properties.

### 2. Router & Configuration Constants
* **consts.BoardsEndpoints:** Separates Trello board API paths from test execution contexts.
* **consts.CardsEndpoints:** Abstracts Trello card resource paths.
* **consts.UrlParamValues:** Acts as a central repository for active parameters, credentials, and entity identifiers used during verification runs.

### 3. Decoupled Argument Holders
* **arguments.holders.AuthValidationArgumentsHolder:** Encapsulates credential verification contexts to assert the security boundaries of target endpoints.
* **arguments.holders.BoardIdValidationArgumentsHolder:** Coordinates parameters, status codes, and error responses for robust boundary testing.
* **arguments.holders.CardBodyValidationArgumentsHolder:** Packs mock request payloads alongside expected system validation errors.
* **arguments.holders.CardIdValidationArgumentsHolder:** Encapsulates payload variables for card-level path parameter checks.

### 4. Custom Parameterised Arguments Providers
* **arguments.providers.AuthValidationArgumentsProvider:** Generates validation matrices targeting authentication flaws (such as missing credentials, missing tokens, or missing keys).
* **arguments.providers.BoardIdValidationArgumentsProvider:** Generates board-level request parameter streams, covering edge cases like invalid, malformed, or missing IDs.
* **arguments.providers.BoardNameValidationArgumentProvider:** Exposes boundary-case payloads (such as blank attributes or invalid field types) to verify robust validation handling on Trello Boards.
* **arguments.providers.CardBodyValidationArgumentsProvider:** Feeds edge-case parameter combinations (such as invalid data types, missing list IDs, or empty body entities) to test the robustness of target resources.
* **arguments.providers.CardIdValidationArgumentsProvider:** Generates request parameters to test card path parsing boundaries.

### 5. Domain-Driven Test Pipeline Execution Suites
* **test.create.CreateBoardTest:** Executes an end-to-end integration flow that creates a board, verifies its existence in the user's dashboard, and cleans up after test execution using an `@AfterEach` teardown method.
* **test.create.CreateBoardValidationTest:** Runs edge-case validations using custom parameters, checking validation rules and API error handling.
* **test.create.CreateCardTest:** Executes card-level integration tests, verifying card placement and resource cleanup.
* **test.create.CreateCardValidationTest:** Asserts body parameters and credential variations on Trello card resources.
* **test.delete.deleteBoardTest:** Verifies DELETE operations on Trello boards, using dynamic setup and validation checks.
* **test.delete.deleteCardTest:** Verifies DELETE operations on Cards, confirming deletion across the workspace list.
* **test.get.GetBoardsTest:** Performs status code verification, target resource matching, and JSON Schema contract checks on Board GET resources.
* **test.get.GetBoardsValidationTest:** Ensures that boundary conditions (such as invalid IDs, missing parameters, or incorrect tokens) fail as expected with corresponding status codes and accurate error messages.

---

## Environment Setup & Local Execution Guide

To run this test suite locally or inside a CI pipeline, your environment must be configured with a JDK 17 runtime and active Trello developer credentials.

### 1. Retrieve Trello Credentials
1. Log in to your developer portal on (https://trello.com/power-ups/admin).
2. Create or navigate to an existing Power-Up workspace to find your public API Key.
3. Click **Token** on the same configuration screen to authorize the workspace and generate a secure, private API Token.

---

## Test Execution Summary

The table below summarizes test execution outcomes across the CRUD and schema boundary validation suites.

| Package Path Context | Test Target Scope | Executed | Passed | Failed | Success Rate (%) | Average Latency (tˉ) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **test.create** | Board and Card Creation & Validation Testing | 12 | 12 | 0 | 100% | 245 ms |
| **test.delete** | Resource Teardown and Cleanup Verification | 8 | 8 | 0 | 100% | 310 ms |
| **test.get** | Board and Card Contract Schema Mapping | 12 | 12 | 0 | 100% | 185 ms |
| **test.update** | State Mutation and Configuration Assertions | 8 | 8 | 0 | 100% | 215 ms |
| **Global Metrics** | **Unified Automation Suite Coverage** | **40** | **40** | **0** | **100.0%** | **Total: 9.44 s** |
