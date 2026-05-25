# API Endpoints

Base URL: `http://localhost:8080`

There is no controller-level base path or application context path configured, so all endpoints are rooted at `/`.

## Authentication

`POST /login` and `POST /register` are public.

All other endpoints require authentication. Protected endpoints should be called with a JWT token returned by `POST /login`:

```http
Authorization: Bearer <token>
```

Several user-specific endpoints also require the `{id}` path value to match the authenticated username.

## Endpoint List

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/` | Required | Returns `Hello World`. |
| `POST` | `/register` | Public | Registers a new user. |
| `POST` | `/login` | Public | Authenticates a user and returns a JWT token. |
| `GET` | `/users` | Required | Returns all user tracker records. |
| `GET` | `/users/{id}` | Required, `{id}` must match authenticated username | Returns one user tracker record by username. |
| `POST` | `/addDailyProgress/{id}` | Required, `{id}` must match authenticated username | Adds a daily tracker record for the user. |
| `PUT` | `/editDailyProgress/{id}/{date}` | Required, `{id}` must match authenticated username | Edits a daily tracker record for the given date. |
| `DELETE` | `/deleteUser/{id}` | Required, `{id}` must match authenticated username | Deletes the user. |
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

### Login

`POST /login`

```json
{
  "uname": "john",
  "password": "password123"
}
```

### Add or Edit Daily Progress

`POST /addDailyProgress/{id}`

`PUT /editDailyProgress/{id}/{date}`

For `PUT /editDailyProgress/{id}/{date}`, the `date` path variable uses `yyyy-MM-dd` format.

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

## Response Notes

- `POST /login` returns a JWT token string when authentication succeeds.
- `POST /register` returns a plain text message such as `User saved successfully` or `Username or email already exists`.
- `POST /addDailyProgress/{id}` returns a plain text message describing whether the record was added or already exists.
- `DELETE /deleteUser/{id}` returns `204 No Content` on success.
