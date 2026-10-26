# 🎖️ Sistema de Gestión Militar — Java Console Application

Aplicación de consola orientada a objetos desarrollada en **Java** para la administración integral de **cuarteles, personal militar (soldados y oficiales), reservas y asignaciones**.

El proyecto implementa una arquitectura desacoplada en **N-Capas (MVC + Service + DAO + DTO)** con **persistencia segura en archivos de texto (`.txt`)**, baja lógica y validación estricta de reglas de negocio.

---

## 📐 Arquitectura y Patrones de Diseño

El sistema sigue un flujo unidireccional desacoplado mediante **Inyección de Dependencias** por constructor:
```
[ View ] ──► [ Controller ] ──► [ Service ] ──► [ DAO ] ──► [ Archivo .txt ]
```

### 🛠️ Principales Aplicaciones de POO y SOLID
* **Herencia y Abstracción**: Clase abstracta base `Persona` extendida por las entidades concretas `Soldado` y `Oficial`.
* **Polimorfismo**: DAO unificado `PersonaDAO` para el manejo centralizado de personas y la validación de **DNI único global** entre soldados y oficiales.
* **Encapsulamiento y DTOs**: La capa de presentación opera exclusivamente con objetos *Data Transfer Object* (`SoldadoDTO`, `OficialDTO`, `CuartelDTO`, `ReservaDTO`, `AsignacionDTO`), protegiendo la integridad del modelo de dominio.
* **Interface Segregation / Dependency Inversion**: `ICrud<T, ID>` define el contrato genérico para la persistencia, permitiendo sustituir la capa de datos sin afectar el resto del sistema.

---

## 💾 Persistencia de Datos
* **Archivos `.txt`**: Persistencia en disco mediante los archivos `cuarteles.txt`, `personas.txt`, `reservas.txt` y `asignaciones.txt`.
* **Creación Automática**: Los archivos se generan automáticamente al iniciar la aplicación si no existen previamente.
* **Escritura Atómica**: Las operaciones de guardado/actualización escriben primero en un archivo `.tmp` temporal y luego realizan un reemplazo atómico, evitando la corrupción de datos ante fallos.
* **Baja Lógica**: Ningún registro se elimina físicamente de los archivos; se utiliza el atributo `activo` (true/false) para permitir bajas y reactivaciones conservando el historial.
* **Identificadores Autogenerados**: Codificación secuencial sin reutilización de IDs (`CUA0001`, `SOL0001`, `OFI0001`, `RES0001`, `ASG0001`).

---

## 📋 Reglas de Negocio Implementadas

1. **DNI Único Global**: Se prohíbe el registro de soldados u oficiales con un DNI existente en el sistema (incluso si la persona está dada de baja).
2. **Relación 1 a N (Cuarteles ◄─► Soldados mediante `Reserva`)**:
   * Un soldado solo puede tener una reserva activa simultáneamente.
   * Un cuartel no puede exceder su capacidad máxima de soldados activos.
3. **Relación 1 a 1 (Oficiales ◄─► Cuarteles mediante `Asignacion`)**:
   * Un oficial solo puede estar asignado a un único cuartel activo.
   * Un cuartel solo puede alojar a un oficial asignado de manera simultánea.

---

## 📁 Estructura del Proyecto

```
src/
└── sistemaMilitar/
    ├── Main.java                        # Punto de entrada y configuración de Streams UTF-8
    ├── models/                          # Entidades del dominio
    │   ├── Persona.java (abstract)
    │   ├── Soldado.java
    │   ├── Oficial.java
    │   ├── Cuartel.java
    │   ├── Reserva.java
    │   └── Asignacion.java
    ├── DTOs/                            # Transferencia de datos hacia las vistas
    │   ├── SoldadoDTO.java
    │   ├── OficialDTO.java
    │   ├── CuartelDTO.java
    │   ├── ReservaDTO.java
    │   └── AsignacionDTO.java
    ├── DAOs/                            # Capa de persistencia
    │   ├── ICrud.java
    │   ├── CuartelDAO.java
    │   ├── PersonaDAO.java
    │   ├── ReservaDAO.java
    │   └── AsignacionDAO.java
    ├── services/                        # Lógica de negocio y validaciones
    │   ├── CuartelService.java
    │   ├── PersonaService.java
    │   ├── ReservaService.java
    │   └── AsignacionService.java
    ├── controllers/                     # Coordinación entre View y Service
    │   ├── CuartelController.java
    │   ├── PersonaController.java
    │   ├── ReservaController.java
    │   └── AsignacionController.java
    ├── views/                           # Interfaz de usuario en consola
    │   ├── MenuPrincipalView.java
    │   ├── CuartelView.java
    │   ├── SoldadoView.java
    │   ├── OficialView.java
    │   ├── ReservaView.java
    │   └── AsignacionView.java
    ├── utils/                           # Utilidades auxiliares
    │   ├── ConsolaUtils.java
    │   ├── GeneradorCodigo.java
    │   └── Validaciones.java
    └── exceptions/                      # Excepciones personalizadas de dominio
        ├── EntidadDuplicadaException.java
        ├── EntidadNoEncontradaException.java
        ├── OperacionNoPermitidaException.java
        ├── PersistenciaException.java
        └── RegistroInvalidoException.java
```

---

## ⚙️ Requisitos y Ejecución

### Requisitos
* **Java Development Kit (JDK)**: versión 17 o superior.
* IDE recomendado: **NetBeans**, IntelliJ IDEA, Eclipse o desde terminal con `javac`.

### Ejecución desde Terminal
```bash
# Compilar las clases
javac -d bin -encoding UTF-8 $(find src -name "*.java")

# Ejecutar la aplicación
java -cp bin sistemaMilitar.Main
```

---

## 🌐 Manejo de Caracteres (UTF-8)
El archivo `Main.java` configura de manera explícita el flujo de salida de la consola a **UTF-8** (`StandardCharsets.UTF_8`), garantizando la correcta visualización de tildes, acentos y caracteres especiales en cualquier sistema operativo.
