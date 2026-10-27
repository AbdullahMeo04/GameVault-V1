# GameVault — PR1 AEA1

**Module:** 0486 — Accés a dades
**Project:** GameVault
**Assignment:** PR1_AEA1

## 1. Project description

GameVault is a Java application for managing video game information and storing game data in different file formats. The project implements CSV and JSON data access, file backups, operation logging, Java object serialization, and random-access files.

The project uses Maven to manage dependencies and run automated tests.

## 2. Questions

### Question 1: What is the difference between sequential access and random access? When is each method more suitable?

Sequential access reads data in order, from the beginning of a file to the end. It is useful when processing an entire file, such as importing all games from a CSV file.

Random access allows a program to move directly to a specific position in a file without reading all the preceding data. In GameVault, `RandomAccessFile` is used to read or update the hours played by a game in `hores.dat`.

Random access is especially useful when records have a fixed size and only a particular record needs to be accessed or modified. Sequential access is generally more suitable when the application needs to process most or all of the file.

### Question 2: If you were developing an online store, would you choose JSON or Java serialization to store product information? Explain your decision.

I would choose JSON to store and exchange product information in an online store. JSON is human-readable, widely supported, and compatible with applications written in different programming languages. This makes it suitable for communicating with web applications, mobile applications, and external services.

Java serialization can be useful for temporarily storing Java objects, such as in a cache, when they need to be restored by a Java application. However, it is more dependent on Java classes and their versions, and it is less suitable for interoperability with other systems.

Therefore, I would use JSON for persistent product data and communication between services, while considering Java serialization only for specific internal caching needs.

### Question 3: What happens when a CSV file contains an invalid date? How should the application handle this situation?

When a CSV row contains a date that cannot be parsed, the application should detect the invalid value and handle the error without terminating the entire import process.

The invalid row should be reported or recorded in the operation log, where supported by the implementation. The application can then continue processing valid rows, allowing as much valid data as possible to be imported.

This approach makes the application more robust and helps users identify and correct problems in the source file.

## 3. Testing

The project includes automated tests for the implemented functionality, including two additional tests in `ProvesPropiesTest`.

To run the tests, execute the following command from the project root:

```bash
mvn clean test
```

The tests check the expected behaviour of the project and help identify implementation errors.

## 4. Use of artificial intelligence

I used ChatGPT as a support tool to understand the assignment requirements, implement parts of the code, and troubleshoot errors. I used it for assistance with CSV reading and invalid data handling (PR1-06), JSON reading and writing with Jackson (PR1-08 and PR1-09), conversion between CSV and JSON (PR1-10), Java serialization (PR1-11), random-access files (PR1-12 and PR1-13), and timestamped backups (PR1-03).

I also used the tool to review explanations and improve my understanding of the implementation. I ran the Maven tests to check the code and resolve problems found during development. I reviewed the final implementation and README before submission.
