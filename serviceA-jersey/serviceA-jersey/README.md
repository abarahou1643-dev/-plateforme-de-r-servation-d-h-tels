# ServiceA Jersey REST API

## 🎯 Overview

A **production-ready** RESTful API built with Jersey (JAX-RS), Hibernate, and PostgreSQL, following **clean architecture principles** and **enterprise best practices**.

This project demonstrates a well-structured backend application with proper layered architecture, comprehensive error handling, validation, and documentation.

## ✨ Features

- ✅ **Clean Architecture** - 6-layer architecture with clear separation of concerns
- ✅ **RESTful APIs** - Category and Item management with full CRUD operations
- ✅ **DTOs** - Separate request/response objects from domain entities
- ✅ **Validation** - Multi-level validation (DTO, Service, Database)
- ✅ **Error Handling** - Global exception mappers with standardized error responses
- ✅ **Pagination** - Support for paginated results
- ✅ **Search** - Full-text search capabilities
- ✅ **Transaction Management** - Proper transaction handling with rollback
- ✅ **Documentation** - Comprehensive guides and JavaDoc

## 🏗️ Architecture

```
┌────────────────────────────────────────────────┐
│         REST Layer (CategoryResource)          │
│                (ItemResource)                  │
└──────────────────┬─────────────────────────────┘
                   │ DTOs
┌──────────────────▼─────────────────────────────┐
│       Service Layer (CategoryService)          │
│                 (ItemService)                  │
│  • Business Logic • Validation • Orchestration │
└──────────────────┬─────────────────────────────┘
                   │ Entities
┌──────────────────▼─────────────────────────────┐
│         DAO Layer (CategoryDAO)                │
│                 (ItemDAO)                      │
│  • Data Access • Transactions • Queries        │
└──────────────────┬─────────────────────────────┘
                   │ Hibernate/JPA
┌──────────────────▼─────────────────────────────┐
│             Database (PostgreSQL)              │
│        • category table • item table           │
└────────────────────────────────────────────────┘
```

## 📁 Project Structure

```
src/main/java/com/example/
├── dto/                    # Data Transfer Objects
│   ├── CategoryRequestDTO.java
│   ├── CategoryResponseDTO.java
│   ├── CategorySummaryDTO.java
│   ├── ItemRequestDTO.java
│   ├── ItemResponseDTO.java
│   └── ItemSummaryDTO.java
├── mapper/                 # Entity/DTO Converters
│   ├── CategoryMapper.java
│   └── ItemMapper.java
├── service/                # Business Logic
│   ├── CategoryService.java
│   └── ItemService.java
├── rest/                   # REST Endpoints
│   ├── CategoryResource.java
│   └── ItemResource.java
├── dao/                    # Data Access Objects
│   ├── CategoryDAO.java
│   └── ItemDAO.java
├── model/                  # Domain Entities
│   ├── Category.java
│   └── Item.java
├── exception/              # Error Handling
│   ├── ResourceNotFoundException.java
│   ├── ValidationException.java
│   ├── DatabaseException.java
│   ├── ErrorResponse.java
│   └── *ExceptionMapper.java (3 mappers)
├── config/
│   └── JerseyConfig.java
└── util/
    └── HibernateUtil.java
```

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.x
- PostgreSQL 12+

### Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd serviceA-jersey
```

2. **Configure database** (Edit `src/main/resources/hibernate.cfg.xml`)
```xml
<property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/yourdb</property>
<property name="hibernate.connection.username">postgres</property>
<property name="hibernate.connection.password">yourpassword</property>
```

3. **Build the project**
```bash
mvn clean package
```

4. **Run the application**
```bash
# Deploy the WAR file to your application server
# Or run with embedded server if configured
```

## 📚 API Endpoints

### Categories

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | Get all categories (paginated) |
| GET | `/api/categories/{id}` | Get category by ID |
| GET | `/api/categories/{id}/with-items` | Get category with items |
| GET | `/api/categories/count` | Get total count |
| POST | `/api/categories` | Create category |
| PUT | `/api/categories/{id}` | Update category |
| DELETE | `/api/categories/{id}` | Delete category |

### Items

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/items` | Get all items (paginated) |
| GET | `/api/items/{id}` | Get item by ID |
| GET | `/api/items/search?keyword=` | Search by name |
| GET | `/api/items/by-category/{categoryId}` | Filter by category |
| GET | `/api/items/count` | Get total count |
| POST | `/api/items` | Create item |
| PUT | `/api/items/{id}` | Update item |
| PATCH | `/api/items/{id}/stock?quantity=` | Update stock |
| DELETE | `/api/items/{id}` | Delete item |

## 💻 Usage Examples

### Create a Category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "code": "ELECTRONICS",
    "name": "Electronics"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "code": "ELECTRONICS",
  "name": "Electronics",
  "updatedAt": "2025-11-12T10:30:00",
  "items": null
}
```

### Create an Item
```bash
curl -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "LAPTOP-001",
    "name": "Dell XPS 15",
    "price": 1299.99,
    "stock": 50,
    "categoryId": 1
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "sku": "LAPTOP-001",
  "name": "Dell XPS 15",
  "price": 1299.99,
  "stock": 50,
  "updatedAt": "2025-11-12T10:35:00",
  "category": {
    "id": 1,
    "code": "ELECTRONICS",
    "name": "Electronics"
  }
}
```

### Get Items with Pagination
```bash
curl "http://localhost:8080/api/items?page=0&size=10"
```

### Search Items
```bash
curl "http://localhost:8080/api/items/search?keyword=laptop"
```

### Update Item Stock
```bash
curl -X PATCH "http://localhost:8080/api/items/1/stock?quantity=-5"
```

### Error Response Example
```json
{
  "timestamp": "2025-11-12T10:40:00",
  "status": 404,
  "error": "Not Found",
  "message": "Category with id 999 not found",
  "path": "/api/categories/999"
}
```

## 🛡️ Validation Rules

### Category
- **code**: Required, 2-50 characters, unique
- **name**: Required, 2-100 characters

### Item
- **sku**: Required, 2-50 characters, unique
- **name**: Required, 2-100 characters
- **price**: Required, must be > 0
- **stock**: Cannot be negative
- **categoryId**: Required, must reference existing category

## 🔒 Business Rules

1. Cannot delete a category that has associated items
2. Item SKU must be unique across all items
3. Category code must be unique across all categories
4. Item stock cannot go negative
5. All items must belong to an existing category

## 🎯 HTTP Status Codes

- **200 OK** - Successful GET, PUT, PATCH
- **201 Created** - Successful POST (includes Location header)
- **204 No Content** - Successful DELETE
- **400 Bad Request** - Validation errors
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server errors

## 🧪 Testing

The architecture supports multiple testing levels:

### Unit Tests
```java
@Test
void shouldCreateCategory() {
    CategoryDAO mockDAO = mock(CategoryDAO.class);
    CategoryService service = new CategoryService(mockDAO);
    // Test business logic
}
```

### Integration Tests
```java
@Test
void shouldSaveCategoryToDatabase() {
    CategoryDAO dao = new CategoryDAO();
    Category category = new Category("TEST", "Test");
    Category saved = dao.save(category);
    assertNotNull(saved.getId());
}
```

### API Tests
```java
@Test
void shouldReturnCreatedCategory() {
    Response response = target("/categories")
        .request()
        .post(Entity.json(requestDTO));
    assertEquals(201, response.getStatus());
}
```

## 🛠️ Technology Stack

- **Java 21** - Programming language
- **Jersey 3.1.1** - JAX-RS implementation
- **Hibernate 6.4.4** - ORM framework
- **PostgreSQL 42.7.3** - Database
- **HikariCP 5.1.0** - Connection pooling
- **Jackson** - JSON serialization
- **Maven** - Build tool

## 🎨 Design Patterns

- **Layered Architecture** - Clear separation of concerns
- **DTO Pattern** - Separate API contract from domain model
- **Mapper Pattern** - Entity/DTO conversion
- **Repository Pattern (DAO)** - Data access abstraction
- **Service Layer Pattern** - Business logic encapsulation
- **Exception Mapping** - Global error handling

## 📊 Code Quality

- ✅ SOLID principles
- ✅ DRY (Don't Repeat Yourself)
- ✅ KISS (Keep It Simple)
- ✅ Separation of Concerns
- ✅ Comprehensive JavaDoc
- ✅ No code duplication
- ✅ Proper exception handling
- ✅ Transaction management

## 🔄 Development Workflow

1. **Add new entity** → Create model class
2. **Add DAO** → Create DAO with CRUD operations
3. **Add DTOs** → Create Request/Response DTOs
4. **Add Mapper** → Create mapper for conversions
5. **Add Service** → Implement business logic
6. **Add Resource** → Create REST endpoints
7. **Add Tests** → Write unit and integration tests