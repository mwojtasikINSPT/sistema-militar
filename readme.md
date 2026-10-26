# Sistema de Gestión Militar

Sistema de aplicación de consola en Java desarrollado bajo arquitectura limpia **N-Capas (MVC + Service Layer + DAO + DTO)** para el dominio de gestión militar. El proyecto incluye persistencia en archivos de texto, borrado lógico, validaciones de integridad referencial, codificación UTF-8 y un módulo de consultas relacionales.

---

## 🏗️ Arquitectura y Patrones Aplicados

El proyecto sigue una estricta separación de responsabilidades:
* **Models (`sistemaMilitar.models`)**: Entidades de dominio (`Cuartel`, `Persona`, `Soldado`, `Oficial`, `Reserva`, `Asignacion`).
* **DTOs (`sistemaMilitar.DTOs`)**: Transferencia limpia de datos hacia la vista sin exponer entidades del modelo.
* **DAOs (`sistemaMilitar.DAOs`)**: Persistencia atómica en archivos de texto (`cuarteles.txt`, `personas.txt`, `reservas.txt`, `asignaciones.txt`).
* **Services (`sistemaMilitar.services`)**: Lógica de negocio, validaciones y coordinación relacional.
* **Controllers (`sistemaMilitar.controllers`)**: Intermediario entre la interfaz de usuario y los servicios.
* **Views (`sistemaMilitar.views`)**: Menús de consola con entrada/salida centralizada en `ConsolaUtils`.
* **Utils & Exceptions (`sistemaMilitar.utils`, `sistemaMilitar.exceptions`)**: Normalización, generador de códigos secuenciales y jerarquía de excepciones runtime.

---

## 🔍 Consultas Obligatorias Implementadas

1. **Soldado y su Cuartel Asignado**: Muestra el nombre/DNI del soldado y el código/detalles de su cuartel actual (vía `Reserva`).
2. **Soldados Supervisados por un Oficial**: Obtiene la asignación 1:1 del oficial con su cuartel y lista a todos los soldados alojados en dicho cuartel.
3. **Oficial Asignado a un Soldado**: Identifica el cuartel del soldado y busca al oficial a cargo de ese mismo cuartel.

---

## 🚀 Ejecución

```bash
javac -encoding UTF-8 sistemaMilitar/*.java sistemaMilitar/*/*.java
java -Dfile.encoding=UTF-8 sistemaMilitar.Main
```
