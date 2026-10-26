
# Southbound Sync

## Descripción del proyecto

Southbound Sync es una aplicación móvil desarrollada
en Android Studio con Kotlin.

Su objetivo es apoyar la gestión de servicios turísticos,
facilitando la consulta de viajes asignados a conductores
y guías mediante una plataforma móvil.

## Problemática

Los trabajadores del sector turístico necesitan
consultar información de sus servicios y traslados.

En algunos sectores, la conexión a internet puede ser
inestable, lo que dificulta acceder permanentemente
a los datos de los viajes.

Southbound Sync busca facilitar el acceso y la
organización de esta información.

## Tecnologías utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- Arquitectura MVVM
- ViewModel y StateFlow
- Firebase Authentication
- Cloud Firestore
- Room (SQLite)
- Git y GitHub

## Funcionalidades implementadas

### Autenticación
- Registro de usuarios
- Inicio de sesión con correo y contraseña
- Verificación de correo electrónico
- Recuperación de contraseña
- Cierre de sesión

### Perfil
- Información del usuario autenticado
- Visualización de rol
- Estado de verificación del correo

### Servicios turísticos
- Consulta de servicios asignados
- Búsqueda por destino
- Filtros por estado
- Filtro mediante calendario
- Visualización de fecha, hora y tipo de servicio

### Persistencia
- Sincronización de servicios con Cloud Firestore
- Almacenamiento local mediante Room
- Consulta de servicios previamente guardados
  sin conexión a internet

### Recursos de Android
- Selección de fechas
- Selección de archivos PDF e imágenes
- Referencias locales de comprobantes

## Arquitectura del proyecto

- Model: representa los datos de la aplicación.
- View: pantallas creadas con Jetpack Compose.
- ViewModel: administra los estados de la interfaz.
- Repository: coordina el acceso a los datos.
- Room: almacena información localmente.
- Firebase: proporciona autenticación y datos
  en la nube.

## Requisitos de ejecución

- Android Studio
- SDK de Android compatible con el proyecto
- Proyecto Firebase configurado
- Emulador Android o dispositivo físico
- Archivo google-services.json válido

## Instrucciones de ejecución

1. Clonar el repositorio desde GitHub.
2. Abrir el proyecto en Android Studio.
3. Configurar Firebase para la aplicación.
4. Incorporar el archivo google-services.json
   correspondiente.
5. Sincronizar Gradle.
6. Ejecutar la aplicación en un emulador
   o dispositivo Android.
7. Crear una cuenta o iniciar sesión.

## Integrantes

- Agregar nombre de integrante 1
- Agregar nombre de integrante 2
- Agregar nombre de integrante 3

## Estado del proyecto

Avance funcional correspondiente a la
Evaluación Parcial 2 de Desarrollo de
Aplicaciones Móviles.

El proyecto continúa en desarrollo.

## Mejoras pendientes

- Completar la gestión de coordinadores
- Mejorar la administración de servicios
- Ampliar la gestión de comprobantes
- Incorporar pruebas adicionales
- Optimizar la experiencia de usuario
