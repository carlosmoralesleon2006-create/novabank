# NovaBank Digital Services - Módulo 1

## 📖 Descripción del proyecto
NovaBank es un sistema de gestión bancaria. En este primer módulo, se ha desarrollado una versión inicial de consola para los empleados del banco. Esta primera versión funciona completamente en memoria y permite realizar operaciones clave sin depender de bases de datos ni interfaces gráficas.

Las funcionalidades principales incluyen:
- Registro y búsqueda de clientes (con validaciones de datos).
- Creación y consulta de cuentas bancarias (generación de IBAN).
- Operaciones financieras: depósitos, retiradas y transferencias.
- Consultas de saldos e historial de movimientos.

## 🛠️ Tecnologías utilizadas
- **Java 17**: Lenguaje de programación principal.
- **Apache Maven**: Gestión de dependencias y automatización del ciclo de construcción.
- **JUnit 5**: Framework para pruebas unitarias.
- **Mockito**: Herramienta para la creación de objetos simulados (mocks) y el aislamiento de dependencias en las pruebas.
- **Git y GitHub**: Control de versiones y alojamiento del repositorio.

## ⚙️ Requisitos del sistema
Para compilar y ejecutar este proyecto en tu entorno local, necesitarás tener instalado:
- **Java Development Kit (JDK) 17** o superior.
- **Apache Maven 3.6** o superior.

## 📁 Estructura del proyecto
El código fuente sigue una arquitectura estructurada por capas para separar responsabilidades. El paquete principal es `com.novabank`, dentro del cual encontramos:

* **`modelo/`**: Contiene las clases que representan las entidades del dominio de negocio (`Cliente`, `Cuenta`, `Movimiento` y el enumerador `TipoMovimiento`). Son objetos puramente de datos.
* **`repositorio/`**: Capa de acceso a datos. En este módulo incluye la clase `Memoria`, encargada de almacenar temporalmente la información utilizando colecciones estándar de Java (Maps) y gestionar los contadores de IDs.
* **`servicio/`**: Contiene la lógica de negocio de la aplicación (`ClienteService`, `CuentaService`, `OperacionService`). Aquí se realizan las validaciones, la generación de IBANs y las operaciones matemáticas de los saldos.
* **`Main.java`**: Es el punto de entrada de la aplicación. Gestiona la interfaz interactiva por consola y conecta la entrada del usuario con los servicios correspondientes.

### Otros directorios importantes:
* **`src/main/resources/`**: Contiene el archivo `schema.sql` con el diseño relacional de la base de datos para futuras implementaciones.
* **`src/test/java/`**: Contiene la suite de pruebas unitarias automatizadas (JUnit 5 + Mockito) organizadas en paralelo a la estructura principal.

## 🚀 Instrucciones de ejecución

Abre tu terminal en el directorio raíz del proyecto y utiliza los siguientes comandos de Maven:

### Cómo compilar
Para compilar el código fuente y descargar las dependencias:
```bash
mvn clean compile
```

### Cómo ejecutar
Para ejecutar el programa necesitas usar el siguiente comando
```bash
mvn exec:java
```

### Cómo ejecutar los tests
Para realizar los tests necesitas usar el siguiente comando
```bash
mvn test
```


## 🔗 Enlace al repositorio
Puedes consultar todo el código fuente y el historial de versiones aquí:
[Repositorio de NovaBank Digital Services](https://github.com/carlosmoralesleon2006-create/novabank.git)
