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
* [Конфигурация](#конфигурация)
  * [API](#api)

# 🌐Обзор
**SpringOrderManagement** - это симуляция системы управления заказами.

## ⚡Функции
* Управление заказами.
* Управление клиентами.
* Управление продуктами.
* Экспорт клиентов в файл xlsx.
* REST API с JWT-аутентификацией.
* Поддержка PostgreSQL.
* Предустановленные роли (ROLE_USER, ROLE_ADMIN)

# ⚙️Конфигурация
#### 1️⃣ Клонируйте репозиторий:
```bash
git clone https://github.com/GnomeShift/SpringOrderManagement.git
```

#### 2️⃣ Сгенерируйте пару ключей:
```bash
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048 && openssl rsa -pubout -in private_key.pem -out public_key.pem
```
> [!WARNING]
> Ключи, сгенерированные SSH-клиентами (PuTTY и т.д.), начинающиеся с "---- BEGIN SSH", без расширения .pem, не поддерживаются!

#### 3️⃣ Откройте `application.properties` в текстовом редакторе и укажите значения переменных.

| Переменная                                                   | Значение                                                                                                                                                      |
|--------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| spring.datasource.url=jdbc:postgresql://IP_БД:ПОРТ_БД/ИМЯ_БД | IP, порт, имя БД                                                                                                                                              |
| spring.datasource.username=ЛОГИН                             | Пользователь БД                                                                                                                                               |
| spring.datasource.password=ПАРОЛЬ                            | Пароль пользователя БД                                                                                                                                        |
| spring.jpa.hibernate.ddl-auto=ЗНАЧЕНИЕ                       | create - создать структуру БД (**УДАЛИТ ВСЕ ДАННЫЕ**);<br/>update - обновить структуру БД;<br/>validate - проверить структуру БД;<br/>none - ничего не делать |
| jwt.expirationMs=ЗНАЧЕНИЕ                                    | Время жизни JWT-токена в миллисекундах (**по умолчанию - 86400000**)                                                                                          |
| jwt.privateKeyPath=/ПУТЬ/ДО/ФАЙЛА                            | Путь до файла приватного ключа (**по умолчанию - ./src/main/resources/private_key.pem**)                                                                      |
| jwt.publicKeyPath=/ПУТЬ/ДО/ФАЙЛА                             | Путь до файла публичного ключа (**по умолчанию - ./src/main/resources/public_key.pem**)                                                                       |

#### 4️⃣ Запустите сборку проекта:
```bash
mvn spring-boot:run
```
> [!NOTE]
> Убедитесь, что у Вас установлен Maven.

# 📡API
В таблице ниже приведены доступные API-Endpoints.

| API-Endpoint                          | Метод  | Описание                                                                       | Тело запроса                                                                                                                                                                         |
|---------------------------------------|--------|--------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **/api/auth/signup**                  | POST   | Зарегистрировать пользователя                                                  | `{ "name": "NAME", "surname": "SURNAME", "age": 18-99, "phone": "+7(123)456-78-90", "email": "mail@example.com", "password": "PASSWORD", "role": "USER/ADMIN" }`                     |
| **/api/auth/signin**                  | POST   | Авторизовать пользователя                                                      | `{ "email": "mail@example.com", "password": "PASSWORD" }`                                                                                                                            |
| **/api/customers**                    | GET    | Получить всех клиентов                                                         | -                                                                                                                                                                                    |
| **/api/customers/{id}**               | GET    | Получить клиента по ID                                                         | -                                                                                                                                                                                    |
| **/api/customers/{id}**               | PUT    | Обновить клиента                                                               | `{ "name": "NAME UPDATED", "surname": "SURNAME UPDATED", "age": 18, "phone": "+7(123)456-78-90", email": "mail.updated@example.com", "password": "PASSWORD", "role": "USER/ADMIN" }` |
| **/api/customers/{id}**               | DELETE | Удалить клиента                                                                | -                                                                                                                                                                                    |
| **/api/customers/excel**              | GET    | Экспортировать всех клиентов в Excel-файл и скачать                            | `fetch("http://localhost:8080/api/customers/excel", { "headers": { "Authorization": "Bearer ТОКЕН" }, "method": "GET" });`                                                           |
| **/api/orders**                       | GET    | Получить все заказы                                                            | -                                                                                                                                                                                    |
| **/api/orders?status={status}**       | GET    | Получить все заказы по статусу (PROCESSING, IN_PROGRESS, DELIVERED, CANCELLED) | -                                                                                                                                                                                    |
| **/api/orders/{id}**                  | GET    | Получить заказ по ID                                                           | -                                                                                                                                                                                    |
| **/api/orders**                       | POST   | Создать новый заказ                                                            | `{ "customer": { "id": 1 }, "orderDate": "2023-10-27", "status": "PROCESSING", "products": [ { "id": 1 }, { "id": 2 } ] }`                                                           |
| **/api/orders/{id}**                  | PUT    | Обновить заказ                                                                 | `{ "customer": { "id": 1 }, "orderDate": "2023-10-27", "status": "DELIVERED", "products": [ { "id": 2 }, { "id": 3 } ] }`                                                            |
| **/api/orders/{id}**                  | DELETE | Удалить заказ                                                                  | -                                                                                                                                                                                    |
| **/api/orders/customer/{customerId}** | GET    | Получить заказ клиента                                                         | -                                                                                                                                                                                    |
| **/api/products**                     | GET    | Получить все товары                                                            | -                                                                                                                                                                                    |
| **/api/products/{id}**                | GET    | Получить товар по ID                                                           | -                                                                                                                                                                                    |
| **/api/products**                     | POST   | Создать новый товар                                                            | `{ "name": "Product C", "price": 30.00 }`                                                                                                                                            |
| **/api/products/{id}**                | PUT    | Обновить товар                                                                 | `{ "name": "Product C Updated", "price": 35.00 }`                                                                                                                                    |
| **/api/products/{id}**                | DELETE | Удалить товар                                                                  | -                                                                                                                                                                                    |

IP-адрес для отправки API-запроса:
> http://localhost:8080/{api_endpoint}
