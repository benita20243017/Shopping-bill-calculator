# Shopping Bill Calculator

A Java Spring Boot application for managing shopping items and calculating a final bill with tax and discount support.

## Features

- CRUD operations for shopping items
- Product list management
- Bill summary calculation using tax and discount
- Simple frontend UI with HTML, CSS, and JavaScript
- H2 in-memory/file database for local persistence

## Tech Stack

- Java 17
- Spring Boot 3.3.x
- Spring Data JPA
- H2 Database
- HTML/CSS/JavaScript frontend

## Run the project

1. Clone the repository.
2. Open a terminal in the project root.
3. Run:

```bash
mvn spring-boot:run
```

4. Open the application in the browser:

```text
http://localhost:8080
```

## API Endpoints

### Items

- `GET /api/items` - Get all items
- `GET /api/items/{id}` - Get an item by ID
- `POST /api/items` - Create a new item
- `PUT /api/items/{id}` - Update an item
- `DELETE /api/items/{id}` - Delete an item

### Bill

- `GET /api/bill?taxRate=0.10&discountRate=0.05` - Calculate bill summary
- `POST /api/bill` - Calculate bill summary using request body

## Example JSON payload for item creation

```json
{
  "name": "Laptop",
  "quantity": 1,
  "price": 1200.00,
  "category": "Electronics",
  "description": "Gaming laptop"
}
```

## Example bill request

```json
{
  "taxRate": 0.10,
  "discountRate": 0.05
}
```

## Project structure

```text
shopping-bill-calculator/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/shoppingbill/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── ShoppingBillApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html
│   │       │   ├── styles.css
│   │       │   └── app.js
│   │       └── application.properties
├── .gitignore
├── pom.xml
└── README.md
```
