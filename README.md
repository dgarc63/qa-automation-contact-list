Reto de Automatización QA – Automatización UI y API
1. Descripción del Proyecto
Este proyecto contiene la solución de automatización para el reto técnico de QA. Incluye automatización End-to-End (UI) utilizando Playwright y automatización de API utilizando Serenity BDD con Gherkin.
2. Stack Tecnológico
•	Automatización UI: Playwright (JavaScript)
•	Automatización API: Serenity BDD + Rest Assured
•	Lenguaje BDD: Gherkin (Feature Files)
•	Patrón de Diseño UI: Page Object Model (POM)
•	Integración Continua: GitHub Actions
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
├── package.json
│
API Serenity/
│
├── src/test/java/
├── src/test/resources/features/
├── pom.xml
5. Prerrequisitos
•	Node.js versión 18 o superior
•	Java JDK 11 o superior
•	Maven instalado
•	Git
6. Instalación del Proyecto
UI (Playwright)
npm install
npx playwright install
API (Serenity)
mvn clean install
7. Ejecución de Pruebas
7.1 Ejecutar Pruebas UI

npx playwright test --project=chromium --headed
Abrir reporte HTML:
npx playwright show-report
7.2 Ejecutar Pruebas API
Ejecutar pruebas Serenity:
mvn clean verify
Abrir reporte Serenity:
open target/site/serenity/index.html
8. Escenarios Automatizados
UI:
•	Login
•	Crear Contacto
•	Actualizar Contacto
•	Eliminar Contacto
•	Cerrar Sesión
API:
•	Autenticación
•	Crear Contacto
•	Consultar Contacto
•	Actualizar Contacto
•	Eliminar Contacto
9. Observaciones
La base de datos de la aplicación demo puede reiniciarse periódicamente, ya que se trata de un entorno de pruebas.

