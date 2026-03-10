## Reflection 1

In implementing eshop's product management features, I focused on several Clean Code principles to ensure long-term maintainability. I used meaningful, intention-revealing names for methods such as findById and delete to ensure the code remains readable for other developers. I also applied the Single Responsibility Principle by separating the logic into four distinct packages: controller, model, repository, and service. In this structure:
- The Model layer represents the problem domain and data.
- The Controller manages HTTP requests/responses and forwards tasks to the service layer.
- The Service layer implements the core business logic.
- The Repository layer handles data operations, currently using an ArrayList to simulate a database.

Additionally, I strictly adhered to the Model-View-Controller (MVC) pattern to maintain a clean separation of concerns. By placing the data structure in the model package, I ensured that the problem domain remains isolated from the application logic. I applied Lombok's @Getter and @Setter annotations to the Product class, which significantly reduced boilerplate code and improved scannability.

Regarding Secure Coding, I utilized Spring's @PathVariable and @ModelAttribute to safely map user-provided data from URLs and forms directly into the model. This approach is more secure than manual string parsing, as it leverages Spring's built-in handling of request parameters. However, I identified a potential area for improvement: implementing input validation in the Product model to prevent invalid data, such as negative quantities or empty names, from being saved. Furthermore, by implementing the Edit and Delete features on separate Git branches, I maintained a clean and traceable project history, which is essential for collaborative development and fulfills the module's version control requirements.

## Reflection 2

1. Writing unit tests for this project has provided a clear perspective on how isolated testing can fortify the reliability of specific features like the product repository. In terms of quantity, a single class should contain enough unit tests to verify every distinct logical path, including both successful "happy paths" and edge cases where input might be invalid or missing. For example, when testing the update functionality, it is necessary to include a positive scenario where a product is found and modified, as well as a negative scenario where the system attempts to update a non-existent ID.

   To ensure our suite is robust enough, we utilize code coverage as a metric to visualize exactly which lines of our source code are executed during the tests. This allows us to identify "blind spots" in our logic that have not yet been verified. However, it is vital to acknowledge that achieving 100% code coverage does not mean the code is free of bugs or errors. High coverage simply indicates that the code was executed; it does not guarantee that the assertions are logically sound, that performance is optimized, or that the system will behave correctly when integrated with external components or databases.

2. If I were to develop a new functional test suite, such as one verifying the count of items in a list, while reusing the exact same setup procedures and instance variables from the previous suite, the cleanliness and quality of the code would likely decline. Creating redundant setup logic violates the DRY (Don't Repeat Yourself) principle, leading to a codebase that is difficult to manage and prone to inconsistencies. If the application’s configuration or the base URL changes, a developer would be forced to update every individual test file manually, which is a significant maintenance burden.

   To improve this and maintain high code quality, several refactoring techniques could be applied. First, common setup logic, such as the baseUrl assignment and the @LocalServerPort declaration, should be moved into a Base Test Class that other functional suites can extend. Additionally, implementing a Page Object Model (POM) would allow us to encapsulate page-specific interactions, like finding a table row or clicking a button, within dedicated classes. This separation of concerns makes the test scripts more readable and ensures that any change in the HTML structure only needs to be updated in one place.

## Reflection 3

1. During the exercise, I fixed several code quality issues identified by the PMD code analysis tool. The most significant issue was related to naming conventions and access modifiers in JUnit 5 tests. PMD flagged that test classes and methods should be package-private rather than public, as JUnit 5 does not require the public modifier. My strategy for fixing this was to refactor the test code in ProductTest.java and ProductRepositoryTest.java by removing the public keyword from the class definitions and test methods. Additionally, I addressed issues regarding unused imports and unnecessary variables by reviewing the code analysis report in the GitHub Actions logs and removing the redundant code. This strategy ensured that the codebase remained clean, maintainable, and compliant with the project's defined coding standards.

2. Yes, the current implementation meets the definitions of Continuous Integration and Continuous Deployment.
- Continuous Integration (CI): The setup successfully implements CI because every time a push or pull request is made, the GitHub Actions workflows defined in ci.yml and pmd.yml are triggered automatically. This process compiles the Java code, runs the entire test suite using Gradle, and performs static code analysis to verify code quality before any merge occurs.
- Continuous Deployment (CD): The implementation qualifies as CD because the process of releasing the software to production is fully automated through Heroku. By connecting the GitHub repository to Heroku, any changes that pass the CI pipeline and are merged into the main branch (that was merged from module-2-exercise branch) are automatically detected, built (using the buildpacks), and deployed to the live environment without requiring manual server configuration or file uploads.

Deployment link: https://shelia-eshop-074875ae89f3.herokuapp.com/

## Reflection 4

1. Explain what principles you apply to your project.

      In this project, I applied all five SOLID principles to improve the code's structure and maintainability. 
- For the Single Responsibility Principle (SRP), I separated the CarController from the ProductController file into its own dedicated class, ensuring each controller is only responsible for handling HTTP requests for its specific entity. 
- For the Open-Closed Principle (OCP), I refactored the update method in the CarRepository to replace the entire Car object within the list rather than manually mapping individual attributes, keeping the method closed to modification if the Car model gets new fields.
- To resolve the Liskov Substitution Principle (LSP), I removed the extends ProductController inheritance from CarController, as a car controller does not truly fulfill an "IS-A" relationship with a product controller.
- The Interface Segregation Principle (ISP) was already met, as the CarService interface is lean and only dictates the methods clients actually use.
- Finally, I applied the Dependency Inversion Principle (DIP) in the CarController by injecting the CarService interface rather than the concrete CarServiceImpl class.

2. Explain the advantages of applying SOLID principles to your project with examples.

      Applying SOLID principles makes the application significantly more flexible, scalable, and easier to test. For instance, by applying OCP to the CarRepository, if we decide to add new properties like carPrice or carEngineType to the model in the future, the repository's update logic automatically handles the new object structure without requiring us to write new setter methods. Applying DIP in the controller layer brings a massive advantage to testing because CarController depends on an abstraction (CarService), we can easily inject a mock service during unit tests without needing to boot up a real database or repository. Furthermore, by adhering to SRP and keeping our controllers isolated, multiple developers can work on the Product and Car features simultaneously without running into constant merge conflicts in a single bloated file.

3. Explain the disadvantages of not applying SOLID principles to your project with examples.

      Conversely, failing to apply SOLID principles creates a rigid, fragile codebase where small changes often trigger cascading bugs. For instance, before applying LSP and SRP, CarController extended ProductController. This false inheritance meant the car controller absorbed dependencies it didn't need, which can cause severe AmbiguousMappingException errors in Spring Boot if routes overlap, ultimately crashing the application. If we had ignored OCP in the repository, adding a single new attribute to the Car model would require us to manually hunt down and update the setter methods in the update logic; forgetting even one line would lead to silent data loss when a user tries to edit a car. Lastly, without DIP, tightly coupling the CarController directly to CarServiceImpl means that if we ever wanted to swap out our in-memory list for a real database implementation, we would be forced to modify the controller code as well, violating the separation of concerns.

## Reflection Module 04

1. I found the TDD workflow useful in this exercise because it gave me a clear sequence of writing failing tests first, implementing the minimum code to make them pass, and then refactoring. This helped me focus on behavior and requirements before implementation. The tests also made it easier to catch mistakes early, especially for invalid input cases and update behavior.

      Based on Percival’s self reflective questions, I think the workflow was useful, but I still need improvement. I mostly followed the scenarios listed in the tutorial, so my tests covered the required cases but not many additional edge cases. Next time, I want to think more deeply about possible boundary conditions, write tests that express one behavior more clearly, and use the refactoring stage more actively to improve code quality.

2. My tests mostly followed the F.I.R.S.T. principles. They were fast because they only tested local logic and used mocks instead of external systems. They were independent and repeatable because each test used controlled setup data and did not depend on execution order. They were self-validating because assertions clearly determined pass or fail. They were also timely because I wrote the tests before implementing the code.

      However, I can still improve. In the future, I want to make tests more focused, add more edge-case coverage, and improve naming and structure so that each test is even easier to understand and maintain.
