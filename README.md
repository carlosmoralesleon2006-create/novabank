# NovaBank Digital Services - Módulo 2

## 📖 Descripción del proyecto
NovaBank es un sistema de gestión bancaria interactivo por consola. En este segundo módulo, la aplicación ha sido refactorizada para aproximarse a una arquitectura empresarial real. Se ha implementado una estricta separación por capas y se ha sustituido el almacenamiento temporal en memoria por una persistencia de datos definitiva utilizando una base de datos relacional.

Las funcionalidades principales incluyen:
- Registro, búsqueda y listado de clientes (con validaciones rigurosas).
- Creación y consulta de cuentas bancarias (generación automática de IBAN secuencial).
- Operaciones financieras transaccionales: depósitos, retiradas y transferencias seguras.
- Consultas de saldos e historial de movimientos filtrables por rango de fechas.

## 🛠️ Tecnologías utilizadas
- **Java 17**: Lenguaje de programación principal.
- **PostgreSQL**: Sistema de gestión de bases de datos relacional.
- **JDBC**: API de Java para la conexión y ejecución de consultas SQL.
- **Apache Maven**: Gestión de dependencias y automatización del ciclo de construcción.
- **JUnit 5 y Mockito**: Frameworks para pruebas unitarias y simulación de dependencias (Mocks) aisladas de la base de datos.
- **Git y GitHub**: Control de versiones y alojamiento del repositorio.

## ⚙️ Requisitos del sistema
Para compilar y ejecutar este proyecto en tu entorno local, necesitarás tener instalado:
- **Java Development Kit (JDK) 17** o superior.
- **Apache Maven 3.6** o superior.
- **PostgreSQL 12** o superior.

## 🗄️ Configuración de la Base de Datos
Antes de ejecutar la aplicación, debes preparar la base de datos local:
1. Crea una base de datos en PostgreSQL llamada `novabank_db`.
2. Ejecuta el script SQL que se encuentra en `src/main/resources/schema.sql` para crear las tablas de `clientes`, `cuentas` y `movimientos` con todas sus restricciones.
3. Asegúrate de que las credenciales en la clase `ConexionDB` coinciden con tu usuario y contraseña locales de PostgreSQL.

## 📁 Estructura del proyecto (Arquitectura por Capas)
El código fuente sigue el patrón de diseño de Arquitectura por Capas, garantizando la Separación de Responsabilidades:

* **`modelo/`** (Capa de Dominio): Contiene las entidades puras de negocio (`Cliente`, `Cuenta`, `Movimiento` y `TipoMovimiento`).
* **`repositorio/`** (Capa de Datos): Implementa el **Patrón DAO** (`ClienteDAO`, `CuentaDAO`, `OperacionDAO`) para abstraer las operaciones CRUD con JDBC. Incluye la clase `ConexionDB` que aplica el **Patrón Singleton** para gestionar eficientemente la conexión a PostgreSQL.
* **`servicio/`** (Capa de Negocio): Contiene la lógica de la aplicación. Realiza validaciones y orquesta los DAOs. Está totalmente desacoplada de la interfaz y de la base de datos directa.
* **`ui/`** (Capa de Presentación): Contiene `MenuConsola`, encargada de la interacción con el usuario.
* **`Main.java`**: Punto de entrada minimalista que inicializa la aplicación.


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
