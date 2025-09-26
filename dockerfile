# ===== STAGE 1: BUILD =====
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copia Maven Wrapper e POM primeiro (pra cachear dependências)
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Corrige quebras de linha do Windows e dá permissão de execução
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Baixa dependências em cache
RUN ./mvnw -q -DskipTests dependency:go-offline

# Copia o código-fonte e compila
COPY src src
RUN ./mvnw -q -DskipTests package

# ===== STAGE 2: RUNTIME =====
FROM eclipse-temurin:21-jre
WORKDIR /app
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport"
EXPOSE 8080

# Copia o .jar gerado (qualquer nome) e padroniza como app.jar
COPY --from=build /app/target/*.jar /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
    