# Trello API Test Automation Framework

API test automation framework for the [Trello REST API](https://developer.atlassian.com/cloud/trello/rest/), built with **Java 17, REST Assured, JUnit 5 and Cucumber (BDD)**.

The suite covers the **Boards** and **Cards** resources end to end, with emphasis on negative and validation testing (missing or invalid credentials, malformed IDs, invalid request bodies) alongside the happy paths. Responses are checked for status codes, body values and structure (JSON Schema).

---

## Highlights

- **54 automated tests, 0 failures** (JUnit 5), plus **13 BDD scenarios / 76 steps, all passing** (Cucumber).
- **Two test styles on top of one shared core:** plain JUnit 5 tests and Gherkin scenarios reuse the same endpoint constants and request setup.
- **Data-driven validation:** custom JUnit 5 `ArgumentsProvider` + holder classes keep test data out of the test logic.
- **Contract testing:** response bodies are validated against JSON Schemas.
- **No secrets in the code:** API key and token are injected from environment variables.
- **Two HTML reports** per run: Gradle's JUnit report and a Cucumber HTML report.

---

## Coverage

| Resource | Operation | JUnit 5 | Cucumber |
|----------|-----------|:-------:|:--------:|
| Boards | Create | ✅ | ✅ |
| Boards | Get (all / by ID) | ✅ | ✅ |
| Boards | Update | ✅ (validation) | ✅ |
| Boards | Delete | ✅ | |
| Cards | Create | ✅ | |
| Cards | Get | ✅ | |
| Cards | Update | ✅ | |
| Cards | Delete | ✅ | |

**Test counts by operation (JUnit 5)**

| Operation | Tests |
|-----------|------:|
| Create | 13 |
| Get | 13 |
| Update | 9 |
| Delete | 2 |
| Cucumber runner | 17 (13 scenarios + 4 feature-level entries) |
| **Total** | **54** |

**Types of checks**

- **Positive flows:** status code, response body values, JSON Schema validation.
- **Authentication:** missing key, missing token, invalid key, invalid token.
- **Path parameters:** invalid, malformed and non-existent board and card IDs.
- **Request bodies:** blank names, invalid field types, missing required fields.

---

## Tech Stack

| Area | Technology |
|------|-----------|
| Language | Java 17 |
| Build tool | Gradle 9 |
| HTTP client / assertions | REST Assured 4.4.0 |
| Schema validation | REST Assured `json-schema-validator` 4.4.0 |
| Test framework | JUnit 5.10 (Jupiter API, Engine, Params) |
| BDD | Cucumber 7.15 (`cucumber-java`, `cucumber-junit-platform-engine`) |
| Dependency injection for steps | `cucumber-picocontainer` |
| Suite runner | JUnit Platform Suite Engine |
| Reporting | JUnit HTML report and Cucumber HTML report (Gradle plugin) |

---

## Project Structure

```text
rest_assured_test/
├── build.gradle
├── settings.gradle
└── src/test
    ├── java
    │   ├── arguments
    │   │   ├── holders        # Data carriers for parameterized tests
    │   │   └── providers      # JUnit 5 ArgumentsProviders (test data)
    │   ├── base
    │   │   └── BaseTest.java  # Shared request setup (with / without auth)
    │   ├── consts             # Endpoints and shared constants
    │   ├── steps              # Cucumber step definitions
    │   │   ├── TrelloApiActionSteps.java   # "When" steps: build and send requests
    │   │   ├── TrelloApiAssertSteps.java   # "Then" steps: assert responses
    │   │   ├── ScenarioContext.java        # Shares state between steps
    │   │   ├── MapTransformer.java         # DataTable to Map conversion
    │   │   └── StepArgumentsTransformer.java
    │   ├── test               # JUnit tests grouped by operation
    │   │   ├── create
    │   │   ├── get
    │   │   ├── update
    │   │   └── delete
    │   └── TestRunner.java    # Cucumber suite entry point
    └── resources
        ├── features           # Gherkin feature files (create / get / update)
        └── schemas            # JSON schemas for response validation
```

---

## Design Notes

**Separation of data and logic.** Test classes only describe *what* is verified. Request paths live in `BoardsEndpoints` / `CardsEndpoints`, shared values in `UrlParamValues`, and validation datasets in the `arguments` package. Adding a new negative case means adding one entry to a provider, not writing a new test.

**Parameterized validation tests.** Each validation test is fed by an `ArgumentsProvider` that returns holder objects containing the input, the expected status code and the expected error message. One test method covers an entire matrix of cases.

**Reusable BDD steps.** Cucumber steps are generic (`a request with authorization`, `the request is sent to endpoint`, `the response status code is`, `the response body is equal to`) and driven by data tables, so new scenarios can be written in Gherkin without new Java code. `ScenarioContext` is shared through PicoContainer to pass the response between steps.

**Credentials stay out of the repository.** `build.gradle` reads `TRELLO_API_KEY` and `TRELLO_API_TOKEN` from the environment and exposes them to the tests as JVM system properties (`trello.key`, `trello.token`).

---

## Running the Tests

**Prerequisites**

- JDK 17
- A Trello API key and token ([get them here](https://trello.com/power-ups/admin))

**Set credentials**

```powershell
# Windows PowerShell
$env:TRELLO_API_KEY   = "<your-api-key>"
$env:TRELLO_API_TOKEN = "<your-api-token>"
```

```bash
# macOS / Linux
export TRELLO_API_KEY="<your-api-key>"
export TRELLO_API_TOKEN="<your-api-token>"
```

**Run everything**

```bash
./gradlew test
```

The Cucumber HTML report is generated automatically after the test task finishes.

> Some tests operate on existing Trello resources (board, list and card IDs) defined in `UrlParamValues`. To run the suite against your own account, replace those IDs with ones from your workspace.

---

## Reports

| Report | Location |
|--------|----------|
| JUnit / Gradle test report | `build/reports/tests/test/index.html` |
| Cucumber HTML report | `build/cucumber-report/cucumber-html-reports/` |

### Latest run

| | Result |
|---|---|
| JUnit 5 tests | 54 executed, 0 failed |
| Cucumber features | 3 passed |
| Cucumber scenarios | 13 passed, 0 failed |
| Cucumber steps | 76 passed, 0 failed, 0 skipped, 0 undefined |
| Total duration | about 24 s |

**Cucumber report**

![Cucumber features report](docs/screenshots/cucumber-features.png)

**Gradle test summary**

![Gradle test summary](docs/screenshots/gradle-summary.png)

---

## Author

**Fady Riad**, QA Automation Engineer / SDET
[LinkedIn](https://www.linkedin.com/in/fadyriadshokrallah/)
