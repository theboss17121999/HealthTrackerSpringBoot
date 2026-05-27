# API Endpoints

Base URL: `http://localhost:8080`

There is no controller-level base path or application context path configured, so all endpoints are rooted at `/`.

## Authentication

The current `SecurityConfig` permits these URL patterns without a JWT:

- `POST /login`
- `POST /register`
- `/search/**`

All other endpoints require authentication. Protected endpoints should be called with the JWT token returned by `POST /login`:

```http
Authorization: Bearer <token>
```

User-specific endpoints also require the `{id}` path value to match the authenticated username:

```java
@PreAuthorize("#id == authentication.name")
```

Login uses `uname`, not `username` or email.

## Endpoint List

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/` | Required | Returns `Hello World`. |
| `POST` | `/register` | Public | Registers a new user. The password is stored as a BCrypt hash. |
| `POST` | `/login` | Public | Authenticates using `uname` and `password`, then returns a JWT token string on success. |
| `GET` | `/search/{username}` | Public | Returns whether a valid username exists. Invalid names return `false`. |
| `GET` | `/users` | Required | Returns all user tracker records. |
| `GET` | `/users/{id}` | Required, `{id}` must match authenticated username | Returns one user tracker record by username. |
| `POST` | `/addDailyProgress/{id}` | Required, `{id}` must match authenticated username | Adds a daily tracker record for the user. |
| `PUT` | `/editDailyProgress/{id}` | Required, `{id}` must match authenticated username | Edits the nutrients for an existing daily tracker record. The date is read from the request body. |
| `DELETE` | `/deleteUser/{id}` | Required, `{id}` must match authenticated username | Deletes the user and related daily tracker records. |
| `GET` | `/DailyTracker/{id}` | Required, `{id}` must match authenticated username | Returns daily tracker records for the user. |

## Request Bodies

### Register User

`POST /register`

```json
{
  "uname": "john",
  "password": "password123",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "2000-01-01",
  "gender": "male",
  "height": 175,
  "weight": 70,
  "role": "USER"
}
```

`role` and `dailyTrackers` are optional. If `role` is missing, the service sets it to `USER`.

### Login

`POST /login`

```json
{
  "uname": "john",
  "password": "password123"
}
```

Successful response body:

```text
<jwt-token>
```

Failed response body:

```text
Invalid username or password
```

Failed login responses use HTTP `401 Unauthorized`.

### Search Username

`GET /search/{username}`

No request body.

Example:

```http
GET /search/john
```

Response:

```json
true
```

The response is `false` when the username is missing from the database, shorter than 4 characters, empty, `null`, or starts with a digit.

### Add Daily Progress

`POST /addDailyProgress/{id}`

```json
{
  "date": "2026-05-25",
  "nutrients": {
    "fat": 50,
    "protein": 120,
    "carbohydrate": 220,
    "fiber": 30
  }
}
```

### Edit Daily Progress

`PUT /editDailyProgress/{id}`

The route does not include a date path variable. The service reads the date from the request body and uses it to find the existing `DailyTracker` record.

```json
{
  "date": "2026-05-25",
  "nutrients": {
    "fat": 45,
    "protein": 130,
    "carbohydrate": 210,
    "fiber": 35
  }
}
```

## Response Notes

- `POST /register` returns plain text: `User saved successfully`, `Username or email already exists`, or an exception message.
- `POST /login` returns `200 OK` with a JWT token string on success, or `401 Unauthorized` with `Invalid username or password`.
- `POST /addDailyProgress/{id}` returns plain text describing whether the record was added or already exists.
- `PUT /editDailyProgress/{id}` returns an HTTP status enum value from the controller body, such as `ACCEPTED` or `NOT_FOUND`.
- `DELETE /deleteUser/{id}` returns `200 OK` with `User deleted successfully`, or `404 Not Found` with an error message.
- `GET /DailyTracker/{id}` returns a list of daily tracker DTOs, or `404 Not Found` with an error message.
