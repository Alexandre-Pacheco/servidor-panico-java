# Usar uma imagem base oficial do Maven para compilar o projeto
FROM maven:3.8.5-openjdk-11 AS build

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o ficheiro de configuração do Maven
COPY pom.xml .

# Copiar todo o código-fonte do projeto
COPY src ./src

# Executar o comando de build do Maven para compilar e empacotar a aplicação
# -DskipTests para pular a execução de testes
RUN mvn clean package -DskipTests

# --- Estágio 2: Execução ---
# Usar uma imagem base leve do Java para executar a aplicação
FROM openjdk:11-jre-slim

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o ficheiro .jar que foi gerado no estágio de build
COPY --from=build /app/target/servidor-panico-1.0-SNAPSHOT.jar .

# Expor a porta 8080 para que o mundo exterior possa comunicar com o nosso servidor
EXPOSE 8080

# O comando para iniciar o servidor quando o contêiner for executado
CMD ["java", "-jar", "servidor-panico-1.0-SNAPSHOT.jar"]
