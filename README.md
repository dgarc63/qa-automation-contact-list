QA Automation – API y UI
Descripción General

Este repositorio contiene la automatización de pruebas para la aplicación Contact List disponible en:

https://thinking-tester-contact-list.herokuapp.com/

El proyecto está estructurado en dos componentes principales:

Automatización de API utilizando Serenity BDD, RestAssured y Cucumber (Gherkin).

Automatización de UI utilizando Playwright.

El objetivo es validar de manera automatizada el flujo completo de autenticación y la gestión de contactos, cubriendo operaciones CRUD y escenarios positivos y negativos.

Arquitectura del Proyecto

El repositorio está organizado de la siguiente manera:

qa-automation-contact-list/
│
├── API serenity/
│   ├── src/test/java
│   ├── src/test/resources
│   └── pom.xml
│
├── UI Playwright/
│
└── README.md
API serenity

Contiene la automatización de servicios REST utilizando:

Java 17

Maven

Serenity BDD

RestAssured

Cucumber (Gherkin)

JUnit

La automatización ejecuta llamadas HTTP reales contra la API y valida:

Registro de usuario

Inicio de sesión y obtención de token

Consulta de perfil autenticado

Creación de contacto

Consulta de contacto por ID

Actualización de contacto

Eliminación de contacto

Escenarios negativos de autenticación

Los reportes se generan automáticamente con Serenity.

UI Playwright

Contendrá la automatización de la interfaz gráfica utilizando:

Playwright

Node.js

Cucumber (si se implementa con Gherkin)

Este módulo validará el comportamiento del sistema desde la perspectiva del usuario final.

Ejecución de Pruebas API

Desde la carpeta API serenity ejecutar:

mvn clean verify

Al finalizar la ejecución, el reporte estará disponible en:

target/site/serenity/index.html

Los reportes incluyen:

Resumen de ejecución

Resultado por escenario

Evidencia de request y response

Métricas de duración

Cobertura funcional por feature

Uso de Postman

La documentación de la API se encuentra publicada en Postman Documenter.

Postman fue utilizado únicamente como herramienta de referencia para:

Analizar la estructura de los endpoints

Verificar payloads

Confirmar códigos de estado esperados

La ejecución automatizada no depende de Postman y se realiza completamente mediante código.

Buenas Prácticas Implementadas

Uso de Gherkin para definición clara de escenarios

Separación de responsabilidades mediante Step Definitions

Manejo de sesión mediante variables compartidas

Validaciones explícitas de status code y body

Generación automática de datos dinámicos (UUID)

Reportería profesional con Serenity

Mejoras Futuras

Externalizar la URL base en archivo de configuración

Soporte para múltiples ambientes (dev, qa, prod)

Integración con pipeline CI/CD

Implementación de patrón de diseño más desacoplado

Integración de pruebas UI con manejo de datos dinámicos
