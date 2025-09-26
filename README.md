# cp1-products-api-felipe-carlos — Products API (Spring Boot + MySQL + Docker)

API simples de produtos (CRUD) em Java Spring Boot 3 + MySQL 8, empacotada com Docker.

======================================================================
1) LINKS (preencha antes de entregar)
- GitHub: https://github.com/<SEU_USUARIO>/cp1-products-api-felipe-carlos
- Docker Hub: https://hub.docker.com/r/felipescalesse/cp1-products-api-felipe-carlos
- Integrantes: Pedro Cavariani
======================================================================

REQUISITOS
- Docker + Docker Compose
- Porta 8080 (API) e 3306 (MySQL) livres

ARQUITETURA
- Serviço "db": MySQL 8.4
- Serviço "api": imagem Docker publicada (ou build local)
- API exposta em http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html

======================================================================
2) OPCÃO A — RODAR VIA IMAGEM PUBLICADA (SEM BUILD LOCAL)
======================================================================

2.1 Subir o MySQL
docker run -d --name cp1-mysql ^
  -e MYSQL_DATABASE=cp1 ^
  -e MYSQL_USER=cp1 ^
  -e MYSQL_PASSWORD=cp1 ^
  -e MYSQL_ROOT_PASSWORD=root ^
  -p 3306:3306 mysql:8.4

2.2 Subir a API (usando a imagem do Docker Hub)
docker run -d --name cp1-api --link cp1-mysql:db ^
  -p 8080:8080 ^
  -e DB_HOST=db -e DB_PORT=3306 ^
  -e DB_NAME=cp1 -e DB_USER=cp1 -e DB_PASS=cp1 ^
  felipescalesse/cp1-products-api-felipe-carlos:v1

2.3 Teste
Abra o Swagger: http://localhost:8080/swagger-ui/index.html

Parar/limpar:
docker stop cp1-api cp1-mysql
docker rm cp1-api cp1-mysql

======================================================================
3) OPÇÃO B — RODAR COM DOCKER COMPOSE (API + DB)
======================================================================

3.1 Arquivo docker-compose.yml (na raiz do projeto)
----------------------------------------------------------------------
services:
  db:
    image: mysql:8.4
    container_name: cp1-mysql
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
    container_name: cp1-api
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
----------------------------------------------------------------------

3.2 Subir/Verificar
docker compose up -d
docker compose ps
docker compose logs -f api
Swagger: http://localhost:8080/swagger-ui/index.html

3.3 Parar/derrubar
docker compose stop
docker compose down
docker compose down -v   (apaga também os dados do MySQL)

======================================================================
4) ENDPOINTS (CRUD)
======================================================================
GET     /products
GET     /products/{id}
POST    /products
PUT     /products/{id}
DELETE  /products/{id}

Exemplo de POST:
{
  "name": "Camiseta",
  "price": 49.9,
  "stock": 100
}

======================================================================
5) VARIÁVEIS DE AMBIENTE (API)
======================================================================
DB_HOST  (padrão: localhost ou "db" no compose)
DB_PORT  (padrão: 3306)
DB_NAME  (padrão: cp1)
DB_USER  (padrão: cp1)
DB_PASS  (padrão: cp1)

======================================================================
6) (OPCIONAL) BUILDAR E PUBLICAR A IMAGEM NO DOCKER HUB
======================================================================
# dentro da pasta do projeto (onde tem o Dockerfile e pom.xml)
docker login
docker build -t felipescalesse/cp1-products-api-felipe-carlos:v1 .
docker tag felipescalesse/cp1-products-api-felipe-carlos:v1 felipescalesse/cp1-products-api-felipe-carlos:latest
docker push felipescalesse/cp1-products-api-felipe-carlos:v1
docker push felipescalesse/cp1-products-api-felipe-carlos:latest

# (se quiser que o compose use build local, troque "image:" por "build: ." no serviço "api" e rode:)
docker compose up -d --build

======================================================================
7) PROBLEMAS COMUNS
======================================================================
- Porta 8080 em uso: pare a API local ou mude a porta (ex.: --server.port=8081).
- Docker não inicia: abra o Docker Desktop e confirme "Docker is running".
- Conexão ao MySQL: verifique DB_HOST=db no compose e status "healthy" do serviço db.
- Permissão no docker login (Windows): rode PowerShell como Administrador e tente de novo.

======================================================================
8) TEXTO PARA O PORTAL DO ALUNO (.TXT)
======================================================================
GitHub: https://github.com/FelipeCardosoS/cp1-products-api-felipe-carlos
Docker Hub: https://hub.docker.com/r/felipescalesse/cp1-products-api-felipe-carlos
Integrantes: Felipe Cardoso / Carlos Augusto
======================================================================
