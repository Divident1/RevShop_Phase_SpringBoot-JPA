# RevShop - E-Commerce Console Application

RevShop is a Java-based console application designed to simulate an e-commerce platform. It supports different user roles (Buyer, Seller) and provides functionalities like product management, shopping cart, order processing, and reviews.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Build Instructions](#build-instructions)
- [Running the Application](#running-the-application)

## Features

- **User Authentication**: Registration and Login for Buyers and Sellers.
- **Product Management**: Sellers can add, update, and manage products.
- **Browsing**: Buyers can browse products by category and search by name.
- **Cart & Orders**: Full shopping cart functionality and order placement.
- **Reviews & Favorites**: Buyers can review products and add them to favorites.
- **Notifications**: Automated notifications for order updates and low inventory.

## Technologies Used

- **Language**: Java 17
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **Libraries**:
  - MySQL Connector/J
  - JUnit 5 (Testing)
  - Log4j2 (Logging)

## Prerequisites

Ensure you have the following installed on your system:

- [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Apache Maven](https://maven.apache.org/download.cgi)
- [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

## Setup Instructions

### 1. Database Setup

1.  Make sure your MySQL server is running.
2.  Open your terminal or MySQL client (like Workbench or Command Line).
3.  Run the provided SQL script to create the database and tables:

    ```bash
    mysql -u root -p < database_setup.sql
    ```

    *Alternatively, you can copy the contents of `database_setup.sql` and execute them in your MySQL query editor.*

### 2. Configure Database Connection

Open `src/main/java/com/revshop/util/DBUtil.java` and ensure the database credentials match your local setup:

```java
private static final String URL = "jdbc:mysql://localhost:3306/revshop";
private static final String USERNAME = "root"; // Update if different
private static final String PASSWORD = "root"; // Update if different
```

## Build Instructions

To build the project and download all dependencies, navigate to the project root directory and run:

```bash
mvn clean install
```

## Running the Application

You can run the application directly using Maven:

```bash
mvn exec:java
```

Or, if you want to run it from the compiled classes manually:

```bash
# Compile
mvn compile

# Run
java -cp target/classes:target/dependency/* com.revshop.Main
```

*(Note: The `exec:java` command is configured in `pom.xml` to run `com.revshop.Main`)*
# RevShop_Phase_SpringBoot-JPA
