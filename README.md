# directory-app

## Описание

**directory-app** — это веб-приложение, разработанное с использованием Spring Boot на стороне сервера и Vue 3 с Vite и TypeScript на стороне клиента. Это приложение предоставляет функциональность для управления и отображения информации о пользователях.

## Требования

- **Java**: версия 21 или выше
- **Node.js**: версия 20 или выше
- **Maven**: для сборки и управления зависимостями сервера

## Структура проекта

- `directory-app-api`: серверная часть приложения на Spring Boot
- `directory-app-client`: клиентская часть приложения на Vue 3 с Vite и TypeScript

## Запуск приложения

### 1. Клонирование репозитория

```bash
git clone https://github.com/SHOWTERsan/directory-app.git
cd directory-app
```

### 2. Запуск серверной части

Выполните следующие команды:

```bash
cd directory-app-api
mvn clean install
mvn spring-boot:run
```
После успешного запуска сервер будет доступен по адресу http://localhost:8080.

### 3. Запуск клиентской части

В новом терминале, в корневой директории выполните следующие команды:

```bash
cd directory-app-client
npm install
npm run dev
```

После успешного запуска клиентское приложение будет доступно по адресу http://localhost:5173/

## Примечания

- Во время разработки клиентское приложение (directory-app-client) работает на порту 5173 и взаимодействует с серверной частью (directory-app-api), работающей на порту 8080. Для корректной работы убедитесь, что оба сервиса запущены одновременно.
