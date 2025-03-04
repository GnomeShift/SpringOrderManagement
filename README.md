<h1>
<p align="center">
<a href="https://github.com/GnomeShift/SpringOrderManagement" target="_blank" rel="noopener noreferrer">SpringOrderManagement</a>
</p>
</h1>

<p align="center">
  <a href="README.md">🇷🇺 Русский</a>
</p>

## 🚀Быстрая навигация
* [Обзор](#обзор)
    * [Функции](#функции)
* [Установка](#установка)
* [Конфигурация](#конфигурация)

# 🌐Обзор
**SpringOrderManagement** - это симуляция системы управления заказами. Используя встроенный API можно создавать клиентов, заказы, товары, редактировать и удалять их.

## ⚡Функции
* Управление заказами.
* Управление клиентами.
* Управление продуктами.
* REST API с JWT-аутентификацией.
* Поддержка PostgreSQL.
* Предустановленные роли (ROLE_USER, ROLE_ADMIN)

# ⬇️Установка
Клонируйте репозиторий:
```bash
git clone https://github.com/GnomeShift/SpringOrderManagement.git
```
> [!NOTE]
> Убедитесь, что у Вас установлен Maven.

Запустите сборку проекта:
```bash
mvn spring-boot:run
```

# ⚙️Конфигурация
Откройте `application.properties` в текстовом редакторе и укажите значения переменных.

| Переменная                                                   | Значение                                                                                                              |
|--------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| spring.datasource.url=jdbc:postgresql://IP_БД:ПОРТ_БД/ИМЯ_БД | IP, порт, имя БД                                                                                                      |
| spring.datasource.username=ЛОГИН                             | Пользователь БД                                                                                                       |
| spring.datasource.password=ПАРОЛЬ                            | Пароль пользователя БД                                                                                                |
| spring.jpa.hibernate.ddl-auto=ЗНАЧЕНИЕ                       | create - создает структуру БД, **УДАЛИТ ВСЕ ДАННЫЕ**; validate - проверяет структуру БД; none - ничего не делать с БД |
| jwt.expirationMs=ЗНАЧЕНИЕ                                    | Время жизни JWT-токена в миллисекундах, по умолчанию - 86400000                                                       |

# 📡API
В таблице ниже приведены доступные API-Endpoints.

| API-Endpoint                          | Метод  | Описание                                                                       | Тело запроса                                                                                                                                                                          |
|---------------------------------------|--------|--------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **/api/auth/signup**                  | POST   | Зарегистрировать пользователя                                                  | `{ "name": "NAME", "surname": "SURNAME", "age": 18-99, "phone": "+7(123)456-78-90", "email": "mail@example.com", "password": "PASSWORD", "role": "USER/ADMIN" }`                      |
| **/api/auth/signin**                  | POST   | Авторизовать пользователя                                                      | ` { "email": "mail@example.com", "password": "PASSWORD" } `                                                                                                                           |
| **/api/customers**                    | GET    | Получить всех клиентов                                                         | -                                                                                                                                                                                     |
| **/api/customers/{id}**               | GET    | Получить клиента по ID                                                         | -                                                                                                                                                                                     |
| **/api/customers/{id}**               | PUT    | Обновить клиента                                                               | `{ "name": "NAME UPDATED", "surname": "SURNAME UPDATED", "age": 18, "phone": "+7(123)456-78-90", email": "mail.updated@example.com", "password": "PASSWORD", "role": "USER/ADMIN"  }` |
| **/api/customers/{id}**               | DELETE | Удалить клиента                                                                | -                                                                                                                                                                                     |
| **/api/orders**                       | GET    | Получить все заказы                                                            | -                                                                                                                                                                                     |
| **/api/orders?status={status}**       | GET    | Получить все заказы по статусу (PROCESSING, IN_PROGRESS, DELIVERED, CANCELLED) | -                                                                                                                                                                                     |
| **/api/orders/{id}**                  | GET    | Получить заказ по ID                                                           | -                                                                                                                                                                                     |
| **/api/orders**                       | POST   | Создать новый заказ                                                            | `{ "customer": { "id": 1 }, "orderDate": "2023-10-27", "status": "PROCESSING", "products": [ { "id": 1 }, { "id": 2 } ] }`                                                            |
| **/api/orders/{id}**                  | PUT    | Обновить заказ                                                                 | `{ "customer": { "id": 1 }, "orderDate": "2023-10-27", "status": "DELIVERED", "products": [ { "id": 2 }, { "id": 3 } ] }`                                                             |
| **/api/orders/{id}**                  | DELETE | Удалить заказ                                                                  | -                                                                                                                                                                                     |
| **/api/orders/customer/{customerId}** | GET    | Получить заказ клиента                                                         | -                                                                                                                                                                                     |
| **/api/products**                     | GET    | Получить все товары                                                            | -                                                                                                                                                                                     |
| **/api/products/{id}**                | GET    | Получить товар по ID                                                           | -                                                                                                                                                                                     |
| **/api/products**                     | POST   | Создать новый товар                                                            | `{ "name": "Product C", "price": 30.00 }`                                                                                                                                             |
| **/api/products/{id}**                | PUT    | Обновить товар                                                                 | `{ "name": "Product C Updated", "price": 35.00 }`                                                                                                                                     |
| **/api/products/{id}**                | DELETE | Удалить товар                                                                  | -                                                                                                                                                                                     |

IP-адрес для отправки API-запроса:
> http://localhost:8080/{api_endpoint}
