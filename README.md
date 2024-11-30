# API Automation Project

## Description
An API automation framework designed to validate the functionality and performance of API endpoints from Mockapi. This framework leverages modern tools and libraries to ensure robust testing, seamless logging, and efficient handling of JSON-based resources and clients. It focuses on data-driven and scenario-based testing for comprehensive API validation.

## Disclaimers
1. **Gherkin for Test Scenarios**
    - Uses **Gherkin syntax** to define readable and structured test scenarios.
    - Scenarios are written to simulate real-world API interactions, ensuring clarity and maintainability.
2. **Cucumber Integration**
    - Integrates with **Cucumber** to bind Gherkin scenarios to executable Java code.
    - Provides a clear mapping between the test specifications and implementation.
3. **Automated POJOs**
    - Uses **Lombok** to generate boilerplate code for Plain Old Java Objects (POJOs), simplifying resource and client management.
4. **JSON Handling with Gson**
    - Leverages **Gson** to serialize and deserialize JSON data for seamless object handling.
    - Ensures accurate mapping of API payloads to Java objects.
5. **REST-Assured for API Testing**
    - Utilizes **REST-Assured** to interact with API endpoints.
    - Supports validations of response status, headers, and body content with concise and readable syntax.
6. **Data-Driven Testing with Faker**
    - Employs **JavaFaker** to generate random data for resources and clients, ensuring varied and realistic test inputs.
7. **Logging with Log4j**
    - Implements **Log4j** for comprehensive logging of API requests, responses, and test execution flow.
8. **JDK Version**
    - Built and tested using *OpenJDK 23*.

## Test Execution Flow
1. **Scenario Execution**:
    - The test suite runs scenarios defined in **feature files** using Gherkin syntax.
2. **Dynamic Data**:
    - Resources and clients are dynamically created with random values when applicable.
3. **Validations**:
    - Each test includes validations for response status, payload schema, and key business rules.

## Setup Instructions
1. **Clone the repository**:
   ```bash
   git clone https://github.com/AnthonyyHL/APIAutomationProject.git
2. Install the required dependencies using Maven or any other build tool.
3. Run the tests.