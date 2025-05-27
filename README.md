# DSA-PROJECTE 

## Cliente android
### https://github.com/albertkushli/ANDROID-PROYECTO

**Ahora voy a explicar como usar este proyecto en una base de datos personal**


## Pre-requisitos 📋

### EDITOR DE CÓDIGO-> INTELLIJ o VISUAL STUDIO CODE
### INSTALAR MARIADB: https://mariadb.org/download/
Se aconseja usar MariaDB Server 11.4.7

## Comenzando🚀 - Instalación🔧

Si quieres obtener nuestro proyecto sigue estos pasos:

0. (Opcional) Crear un usuario en mariadb, para no usar root, y darle permisos
```
CREATE USER 'nuevo_usuario'@'localhost' IDENTIFIED BY 'tu_contraseña_segura';
GRANT ALL PRIVILEGES ON *.* TO 'nuevo_usuario'@'localhost' WITH GRANT OPTION;


```
1. Insertamos la base de datos en mariadb
```
"RutaOrigen\bin\mariadb.exe" -u root -p < "RutaOrigen\DSA-PROJECTE\src\main\java\edu\upc\dsa\db\juego.sql"
```
2. Primer clonamos el proyecto.
3. Con maven instalamos las dependencias
4. Compilamos con maven
5. Ejecutamos el proyecto en Main.java
6. Ruta localhost
```
http://localhost:8080/
```
7. Ruta Swagger
```
http://localhost:8080/swagger/#
```


### Acceso login

**Tanto en web como en swagger, tienen que registrarse y loguearse.**
**Una vez logueado tienen todas las funcionalidades disponibles**

## Construido con 🛠️

    Java
    Jersey (apis)
    Grizzly (servidor)
    JWT (seguridad rutas)
    HTML
    CSS
    JAVASCRIPT
    BOOTSTRAP
    AJAX
    JQUERY
    MARIADB

## Autores ✒️

    Cazorla Galindo Maria               -   Estudiante DSA
    Kushli Roman Albert                 -   Estudiante DSA
    Quiroga Herrera Miguel ángel        -   Estudiante DSA
    Soledispa Campozano Diego Javier    -   Estudiante DSA
    Wang Junjie                         -   Estudiante DSA
