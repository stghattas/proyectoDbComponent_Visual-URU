# Componente de Base de Datos Desacoplado

**Materia:** Programación Visual  
**Proyecto:** Implementación de un componente de base de datos con patrón Adapter y Pool de Conexiones en Java.

## Descripción del Proyecto

Este proyecto implementa una librería genérica (`DbComponent`) para la gestión y ejecución de consultas a bases de datos relacionales. Su arquitectura está diseñada para priorizar el rendimiento (mediante el reciclaje de conexiones) y la seguridad (ocultando el código SQL), aplicando buenas prácticas de ingeniería de software.

---

## Cumplimiento de la Consigna (Observaciones)

El desarrollo cumple estrictamente con los 4 requerimientos solicitados:

### 1. Pool de Conexiones Interno y Reciclaje
El sistema no abre y cierra conexiones por cada consulta. Utiliza la clase `InternalConnectionPool` para inicializar un "set" de conexiones al instanciar el componente. 
* En operaciones normales (`query()`), el sistema solicita una conexión a la cola (`borrowConnection`) y, mediante un bloque `finally`, garantiza que la conexión sea devuelta (`returnConnection`) para su reutilización inmediata.

### 2. Desacoplamiento del Driver (Patrón Adapter)
La clase principal `DbComponent<T extends IAdapter>` es completamente agnóstica respecto al motor de base de datos. 
* Se implementó la interfaz `IAdapter` que dicta el contrato de conexión.
* Se incluyen dos implementaciones funcionales: `PostgresAdapter` y `MysqlAdapter`. Para cambiar de base de datos, el código interno del componente no se modifica; simplemente se inyecta un adaptador distinto.

### 3. Prevención de SQL Crudo
Por seguridad y mantenimiento, los métodos públicos del componente **no aceptan sentencias SQL**. 
* La ejecución depende de la clase `QueryManager`, la cual carga un diccionario de consultas predefinidas desde el archivo externo `queries.properties`.
* El usuario de la librería solo envía el nombre identificador de la consulta (ej. `"getEstudiantes"` o `"insertEstudiante"`).

### 4. Inicialización Directa
Cumpliendo con la restricción de diseño, el constructor de `DbComponent` recibe la totalidad de los parámetros de conexión de forma directa e imperativa en su firma, en lugar de delegar su búsqueda a variables de entorno o archivos de configuración ajenos a la instancia.

---

## Estructura de Directorios

El proyecto sigue una estructura estándar de Java sin herramientas de empaquetado complejas:

```text
proyectoDbComponent/
├── bin/                      # Archivos .class compilados
├── lib/                      # Dependencias (Driver JDBC de PostgreSQL/MySQL)
├── src/                      # Código fuente .java
│   ├── DbComponent.java
│   ├── IAdapter.java
│   ├── InternalConnectionPool.java
│   ├── Main.java
│   ├── MysqlAdapter.java
│   ├── PostgresAdapter.java
│   ├── QueryManager.java
│   └── TransactionContext.java
├── queries.properties        # Diccionario de sentencias SQL externalizadas
└── README.md                 # Documentación del proyecto
```

### Hecho por: <br>

- Samer Ghattas - 31.887.714 <br>

- Diego Rojas - 31.326.600 <br>