Reto de Automatización QA – UI y API
1. Descripción del Proyecto

Este repositorio contiene la solución desarrollada para el reto técnico de Automatización QA.

La solución incluye:

Automatización End-to-End (UI) utilizando Playwright.

Automatización de API utilizando Serenity BDD con Rest Assured.

Enfoque basado en Behavior-Driven Development (BDD) mediante Gherkin.

Implementación del patrón de diseño Page Object Model (POM) para la capa UI.

El objetivo es validar los flujos de autenticación y las operaciones CRUD (Crear, Consultar, Actualizar y Eliminar) sobre la gestión de contactos, tanto desde la interfaz gráfica como desde los servicios REST.

2. Stack Tecnológico
Automatización UI

Playwright (JavaScript)

Page Object Model (POM)

Automatización API

Serenity BDD

Rest Assured

Gherkin (Feature Files)

Maven

Control de Versiones

Git

3. Aplicación Bajo Prueba

Aplicación Web:
https://thinking-tester-contact-list.herokuapp.com/

Documentación API:
https://documenter.getpostman.com/view/4012288/TzK2bEa8

4. Estructura del Proyecto
UI Playwright/
│
├── tests/
├── pages/
├── playwright.config.js
└── package.json

API Serenity/
│
├── src/test/java/
├── src/test/resources/features/
└── pom.xml
5. Prerrequisitos

Antes de ejecutar el proyecto, asegúrese de tener instaladas las siguientes herramientas:

Node.js versión 18 o superior

Java JDK versión 11 o superior

Maven

Git

6. Instalación
6.1 Instalación UI (Playwright)

Instalar dependencias:

npm install

Instalar navegadores de Playwright:

npx playwright install
6.2 Instalación API (Serenity)

Compilar el proyecto:

mvn clean install
7. Ejecución de Pruebas
7.1 Ejecutar Pruebas UI

Ejecutar todas las pruebas:

npx playwright test

Ejecutar pruebas en modo visible (navegador abierto):

npx playwright test --project=chromium --headed

Abrir el reporte HTML de Playwright:

npx playwright show-report
7.2 Ejecutar Pruebas API

Ejecutar pruebas Serenity:

mvn clean verify

Abrir reporte Serenity:

target/site/serenity/index.html
8. Escenarios Automatizados
Escenarios UI

Inicio de sesión

Crear contacto

Actualizar contacto

Eliminar contacto

Cerrar sesión

Escenarios API

Autenticación

Crear contacto

Consultar contacto

Actualizar contacto

Eliminar contacto

9. Consideraciones

La base de datos de la aplicación demo puede reiniciarse periódicamente, dado que se trata de un entorno público de pruebas.
Por esta razón, las pruebas generan datos dinámicos cuando es necesario para evitar conflictos durante la ejecución.

