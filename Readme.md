# btg-funds-api

Este proyecto permite conectarse a una API la cual los usuarios pueden gestionar sus transacciones hacia unos fondos de inversión

## Tecnologías utilizadas
- Java
- Spring Boot
- Gradle
- Docker
- JUnit
- Mockito
- AWS cloudfront

## Estructura del proyecto

El proyecto esta dividido en tres controladores, cada uno con su propia estructura de carpetas y archivos.
Basandose en la creación y autenticación de usuarios, la gestion de las transacciones y los fondos como tal.

## Explicación

### Autenticación
Endpoint de login: 

    /Auth/login

#### Método: POST

Cuerpo de la petición:

        {
        "email": "user@example.com",
        "password": "password123"
        }

### IMPORTANTE, ESTE TOKEN OBTENIDO SE DEBE AÑADIR EN BEARER TOKEN PARA HACER LAS PETICIONES A LOS ENDPOINTS DE USUARIOS, FONDOS E INVERSIONES
Respuesta:

    {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJsb24uYW5hY29uYTFAY29ycmVvdW5pdmFsbGUuZWR1LmNvIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNzc0NjMyOTY2LCJleHAiOjE3NzQ3MTkzNjZ9.7jNs-n6owUqBhHlJe-h-3QWB6WDMf4HnP2IRtKqr2zM"
    }
    
### Endpoints de Usuario
Endpoint para crear usuario
    /User/register

#### Método: POST

Cuerpo de la petición:

    {
    "email": "marlon.anacona1@correounivalle.edu.co",
    "name": "Marlon",
    "password": "20092001",
    "notificationPreference": "EMAIL",
    "celphone": "3001234567",
    "role": "ADMIN"
    }

Respuesta:

    {
    "message": "Usuario creado exitosamente"
    }


### Obtener todos los usuarios
    /User/

#### Método: GET

Descripción: Obtiene todos los usuarios.
Cuerpo de la petición: Ninguno
Respuesta: Lista de usuarios

## Endpoints de Fondos
### crear fondo

    /Funds/register

#### Método: POST

Cuerpo de la petición:

    {
    "name": "FPV_BTG_PACTUAL_DINAMICA",
    "minimumAmount": "100000",
    "category": "FPV"
    }

Respuesta:
    
    {
    "message": "Fondo creado exitosamente"
    }

### NOTA: por favor del archivo creation-funds.json se pueden crear los fondos que se encuentran ahi. Son los documentos, seria uno por uno

### Obtener listado de fondos

    /Funds/

#### Método: GET

Descripción: Obtiene todos los fondos.

Cuerpo de la petición: Ninguno

Respuesta: 
    
     {
        "id": "69c610502e09471b9aa86e77",
        "name": "FPV_BTG_PACTUAL_ECOPETROL",
        "minimumAmount": 125000,
        "category": "FPV"
    },
    {
        "id": "69c66dbeb20041cb8f29d612",
        "name": "DEUDAPRIVADA",
        "minimumAmount": 50000,
        "category": "FIC"
    },
    {
        "id": "69c66dc5b20041cb8f29d613",
        "name": "FDO-ACCIONES",
        "minimumAmount": 250000,
        "category": "FIC"
    },
    {
        "id": "69c66dc8b20041cb8f29d614",
        "name": "FPV_BTG_PACTUAL_DINAMICA",
        "minimumAmount": 100000,
        "category": "FPV"
    }
    

## Endpoints de Inversión

### Subscripción 
    /investment/subscribe

#### Método: POST

Cuerpo de la petición:

    {
    "fundId": "69c66dc5b20041cb8f29d613",
    "amount": "250000"
    }

Respuesta:

    {
    "message": "Suscripción exitosa"
    }

## cancelación a fondo
    /investment/cancel

#### Método: POST

Cuerpo de la petición:

    {
    "fundId": "IdFondo"
    }

Respuesta:

    {
    "message": "Cancelación exitosa"
    }

## Transacciones
### Obtener listado de transacciones
    /investment/transactions

#### Método: GET

Descripción: Obtiene todas las transacciones del usuario.
Cuerpo de la petición: Ninguno
Respuesta: 

    [
    {
        "id": "69c67c63a8ea31b421538270",
        "customerId": "69c67c4ba8ea31b42153826f",
        "fundId": "69c66dc5b20041cb8f29d613",
        "fundName": "FDO-ACCIONES",
        "type": "OPENING",
        "amount": 250000,
        "date": "2026-03-27T07:47:31.663"
    }
    ]


# Despliegue Spring Boot App en AWS Fargate con MongoDB Atlas

### NOTA: ´Para esta prueba utilicé un solo template por simplicidad. En un entorno real separaría infraestructura base (VPC, ECR) del servicio ECS y usaría CI/CD para el build y push de la imagen.
## Pre-requisitos
- Tener AWS CLI configurado con credenciales (aws configure)
- Tener Docker instalado y funcionando
- Tener una cuenta en MongoDB Atlas con cluster creado
- Crear un repositorio en AWS ECR
- Node: Opcional para scripts de automatización
- Spring Boot app lista y funcional (jar generado por Gradle o Maven)

## Contruir imagen Docker y subir imagen a ECR
En la consola bash ejecutar los comandos :

### Construir la imagen
    docker build -t SPRING_DATA_MONGODB_URI=URL_MONGO_DB   --build-arg SPRING_MAIL_USERNAME=CORREO_PRUEBAS   --build-arg SPRING_MAIL_PASSWORD=PASSWORD_APP_EMAIL   --build-arg SERVER_PORT="8080"   -t fondos-api:latest .


### Etiquetar para ECR
    docker tag fondos-api:latest <tu-account-id>.dkr.ecr.<tu-region>.amazonaws.com/fondos-api:latest

### Login en ECR
    aws ecr get-login-password --region <tu-region> | docker login --username AWS --password-stdin <tu-account-id>.dkr.ecr.<tu-region>.amazonaws.com

### Subir la imagen
    docker push <tu-account-id>.dkr.ecr.<tu-region>.amazonaws.com/fondos-api:latest

## Crear stack en AWS cloudformation


    aws cloudformation create-stack \
    --stack-name fondos-api-stack \
    --template-body file://cloudformation.yaml \
    --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM

## Verificación de logs ECS

    aws ecs list-tasks --cluster <ClusterName>

    aws ecs describe-tasks --cluster <ClusterName> --tasks <task-id>



**Se agregaron una colección de postman con ambiente**

**Copia la IP publica y desde postaman agregarla en el environment para hacer las peticiones a los endpoints de la API, recuerda que debes agregar el token de autenticación obtenido en el login para hacer las peticiones a los endpoints de usuarios, fondos e inversiones.****

