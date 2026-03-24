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
- **Git y GitHub**: Control de versiones y alojamiento del repositorio.

## ⚙️ Requisitos del sistema
Para compilar y ejecutar este proyecto en tu entorno local, necesitarás tener instalado:
- **Java Development Kit (JDK) 17** o superior.
- **Apache Maven 3.6** o superior.

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



