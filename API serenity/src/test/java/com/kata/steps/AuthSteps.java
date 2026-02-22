package com.kata.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AuthSteps {

    private static final String BASE_URL = "https://thinking-tester-contact-list.herokuapp.com";

    // Endpoints (para no tener strings sueltos por todo lado)
    private static final String USERS = "/users";
    private static final String LOGIN = "/users/login";
    private static final String ME = "/users/me";

    // Session keys (Serenity session variables)
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static final String TOKEN = "TOKEN";
    private static final String USER_ID = "USER_ID";

    @Given("creo un usuario nuevo por la API")
    public void creo_un_usuario_nuevo_por_la_api() {

        // Tip: email random para evitar choques si la BD se reinicia o si ya existe el usuario
        String email = "qa_" + UUID.randomUUID() + "@mail.com";
        String password = "Pass1234!";

        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "Daniel");
        body.put("lastName", "QA");
        body.put("email", email);
        body.put("password", password);

        // Ojo: esta API puede responder 201 o 200 (depende del backend)
        String userId =
                SerenityRest.given()
                        .baseUri(BASE_URL)
                        .contentType("application/json")
                        .body(body)
                        .when()
                        .post(USERS)
                        .then()
                        .statusCode(anyOf(is(201), is(200)))
                        .extract()
                        .path("_id"); // normalmente viene _id

        // Guardamos para reutilizar en otros escenarios
        Serenity.setSessionVariable(EMAIL).to(email);
        Serenity.setSessionVariable(PASSWORD).to(password);

        // Si el API no devuelve _id (a veces pasa), no rompemos el flujo
        if (userId != null && !userId.trim().isEmpty()) {
            Serenity.setSessionVariable(USER_ID).to(userId);
        }
    }

    @When("inicio sesión por la API")
    public void inicio_sesion_por_la_api() {
        String email = Serenity.sessionVariableCalled(EMAIL);
        String password = Serenity.sessionVariableCalled(PASSWORD);

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        String token =
                SerenityRest.given()
                        .baseUri(BASE_URL)
                        .contentType("application/json")
                        .body(body)
                        .when()
                        .post(LOGIN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("token");

        Serenity.setSessionVariable(TOKEN).to(token);
    }

    @Then("obtengo un token válido")
    public void obtengo_un_token_valido() {
        String token = Serenity.sessionVariableCalled(TOKEN);

        //
        assertThat("Token no debería ser null", token, notNullValue());
        assertThat("Token no debería estar vacío", token.trim(), not(isEmptyString()));


    }

    // --------- Escenario: Consultar perfil ---------

    @Given("tengo un usuario autenticado")
    public void tengo_un_usuario_autenticado() {
        // Si ya tenemos token en sesión, no repetimos llamadas (más rápido y menos flakey)
        if (Serenity.hasASessionVariableCalled(TOKEN)) return;

        // Si no hay token, hacemos el flujo completo
        creo_un_usuario_nuevo_por_la_api();
        inicio_sesion_por_la_api();
        obtengo_un_token_valido(); // deja trazabilidad + falla rápido si algo viene mal
    }

    @When("consulto mi perfil")
    public void consulto_mi_perfil() {
        String token = Serenity.sessionVariableCalled(TOKEN);

        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ME)
                .then()
                .statusCode(200);
    }

    @Then("veo mi información de usuario")
    public void veo_mi_informacion_de_usuario() {
        String expectedEmail = Serenity.sessionVariableCalled(EMAIL);

        // Si el API devuelve el userId, también lo validamos (cuando exista)
        String expectedUserId = Serenity.hasASessionVariableCalled(USER_ID)
                ? Serenity.sessionVariableCalled(USER_ID)
                : null;

        SerenityRest.lastResponse().then()
                .body("_id", notNullValue())
                .body("email", equalTo(expectedEmail))
                .body("firstName", not(isEmptyOrNullString()))
                .body("lastName", not(isEmptyOrNullString()));

        // Extra (no obligatorio): valida que el perfil coincide con el usuario creado
        if (expectedUserId != null && !expectedUserId.trim().isEmpty()) {
            SerenityRest.lastResponse().then().body("_id", equalTo(expectedUserId));
        }
    }

    // --------- Escenario: Login inválido ---------

    @Given("existe un usuario registrado")
    public void existe_un_usuario_registrado() {
        // Garantiza que existe un usuario para probar el login inválido (email real, password mala)
        if (Serenity.hasASessionVariableCalled(EMAIL) && Serenity.hasASessionVariableCalled(PASSWORD)) return;
        creo_un_usuario_nuevo_por_la_api();
    }

    @When("intento iniciar sesión con password incorrecto")
    public void intento_iniciar_sesion_con_password_incorrecto() {
        String email = Serenity.sessionVariableCalled(EMAIL);

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", "wrong_password_123");

        SerenityRest.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(body)
                .when()
                .post(LOGIN)
                .then()
                // depende del API: algunos devuelven 401, otros 400
                .statusCode(anyOf(is(401), is(400)));
    }

    @Then("recibo un error de autenticación")
    public void recibo_un_error_de_autenticacion() {

        // 1) Lo importante de negocio: NO es 200 y debe ser 4xx
        SerenityRest.lastResponse()
                .then()
                .statusCode(anyOf(is(401), is(400)));

        // 2) Ojo: a veces el backend devuelve body vacío o texto plano.
        // No nos casamos con JSON para no meter flakiness.
        String rawBody = SerenityRest.lastResponse().asString();

        if (rawBody == null || rawBody.trim().isEmpty()) {
            // Está OK: 401 sin body es un patrón común
            return;
        }

        // Si hay contenido, mínimo que sugiera error (sin depender del campo exacto)
        // Nota: evitamos "hasKey" porque si no es JSON, explota el assertion.
        assertThat("El body debería mencionar error/invalid o similar",
                rawBody.toLowerCase(),
                anyOf(
                        containsString("error"),
                        containsString("invalid"),
                        containsString("unauthorized"),
                        containsString("password"),
                        containsString("credentials")
                ));
    }
}