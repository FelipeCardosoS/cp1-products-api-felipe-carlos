# cp1-products-api-felipe-carlos

API de **Produtos** (CRUD) em **Spring Boot 3** + **MySQL 8**, empacotada em **Docker**.

## Requisitos
- Docker e Docker Compose
- Portas livres: **8080** (API) e **3306** (MySQL)

---

## Execução a partir da imagem publicada no Docker Hub (sem build local)

1) Subir o MySQL:
```bash
docker run -d --name cp1-mysql   -e MYSQL_DATABASE=cp1   -e MYSQL_USER=cp1   -e MYSQL_PASSWORD=cp1   -e MYSQL_ROOT_PASSWORD=root   -p 3306:3306 mysql:8.4
```

2) Subir a API (imagem publicada):
```bash
docker run -d --name cp1-api --link cp1-mysql:db   -p 8080:8080   -e DB_HOST=db -e DB_PORT=3306   -e DB_NAME=cp1 -e DB_USER=cp1 -e DB_PASS=cp1   felipescalesse/cp1-products-api-felipe-carlos:v1
```

3) Swagger:
```
http://localhost:8080/swagger-ui/index.html
```

---

## Execução com Docker Compose (API + DB)

Arquivo `docker-compose.yml`:
```yaml
services:
  db:
    image: mysql:8.4
    environment:
      MYSQL_DATABASE: cp1
      MYSQL_USER: cp1
      MYSQL_PASSWORD: cp1
      MYSQL_ROOT_PASSWORD: root
    ports:
      - "3306:3306"
    healthcheck:
      test: ["CMD-SHELL", "mysqladmin ping -h localhost -uroot -proot || exit 1"]
      interval: 5s
      timeout: 3s
      retries: 20
    volumes:
      - dbdata:/var/lib/mysql

  api:
    image: felipescalesse/cp1-products-api-felipe-carlos:v1
    depends_on:
      db:
        condition: service_healthy
    environment:
      DB_HOST: db
      DB_PORT: 3306
      DB_NAME: cp1
      DB_USER: cp1
      DB_PASS: cp1
    ports:
      - "8080:8080"

volumes:
  dbdata:
```

Subir/validar:
```bash
docker compose up -d
docker compose ps
```

Swagger:
```
http://localhost:8080/swagger-ui/index.html
```

Parar/remover:
```bash
docker compose down
docker compose down -v   # remove também os dados do MySQL
```

---

## Endpoints
- `GET /products`
- `GET /products/{id}`
- `POST /products`
- `PUT /products/{id}`
- `DELETE /products/{id}`

Exemplo `POST`:
```json
{ "name": "Camiseta", "price": 49.9, "stock": 100 }
```

---

## Links
- Docker Hub: https://hub.docker.com/r/felipescalesse/cp1-products-api-felipe-carlos
- GitHub: https://github.com/FelipeCardosoS/cp1-products-api-felipe-carlos
