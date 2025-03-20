# Imagen base para el contenedor de compilación
FROM maven:3.6.3-openjdk-17 as builder
WORKDIR /volteretaCroqueta

# Copia las dependencias del proyecto
COPY ../backend/pom.xml ./pom.xml

# Descarga las dependencias del proyecto
#RUN mvn clean verify

# Copia el código del proyecto
COPY ../backend/src ./src

# Compila el proyecto
RUN mvn clean package -DskipTests=true

# Imagen base para el contenedor de la aplicación
FROM openjdk:17.0.2 AS runtime
WORKDIR /volteretaCroqueta

# Copia el JAR del contenedor de compilación
COPY --from=builder /volteretaCroqueta/target/*.jar .

# Indica el puerto que expone el contenedor
EXPOSE 8443

# Comando que se ejecuta al hacer docker run
CMD [ "java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]
