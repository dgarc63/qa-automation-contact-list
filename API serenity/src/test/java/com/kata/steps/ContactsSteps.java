package com.kata.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ContactsSteps {

    private static final String BASE_URL = "https://thinking-tester-contact-list.herokuapp.com";

    private static final String TOKEN = "TOKEN";
    private static final String CONTACT_ID = "CONTACT_ID";

    @Given("existe un contacto creado")
    public void existe_un_contacto_creado() {
        // Asegura auth
        if (!Serenity.hasASessionVariableCalled(TOKEN)) {
            new AuthSteps().tengo_un_usuario_autenticado();
        }

        // Si ya hay contacto, no lo recreamos
        if (Serenity.hasASessionVariableCalled(CONTACT_ID)) return;

        creo_un_contacto_con_datos_validos();
        el_contacto_se_crea_correctamente();
    }

    @When("creo un contacto con datos válidos")
    public void creo_un_contacto_con_datos_validos() {
        String token = Serenity.sessionVariableCalled(TOKEN);

        String email = "contact_" + UUID.randomUUID() + "@mail.com";

        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "Contacto");
        body.put("lastName", "Prueba");
        body.put("birthdate", "2000-01-01");
        body.put("email", email);
        body.put("phone", "3001234567");
        body.put("street1", "Calle 1 # 2-3");
        body.put("street2", "Apto 101");
        body.put("city", "Bogota");
        body.put("stateProvince", "Cundinamarca");
        body.put("postalCode", "110111");
        body.put("country", "Colombia");

        String contactId = SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(body)
                .when()
                .post("/contacts")
                .then()
                .statusCode(anyOf(is(201), is(200)))
                .extract()
                .path("_id");

        Serenity.setSessionVariable(CONTACT_ID).to(contactId);
    }

    @Then("el contacto se crea correctamente")
    public void el_contacto_se_crea_correctamente() {
        String contactId = Serenity.sessionVariableCalled(CONTACT_ID);
        assertThat(contactId, notNullValue());
        assertThat(contactId.trim(), not(isEmptyString()));
    }

    @When("consulto el contacto por id")
    public void consulto_el_contacto_por_id() {
        String token = Serenity.sessionVariableCalled(TOKEN);
        String contactId = Serenity.sessionVariableCalled(CONTACT_ID);

        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/contacts/" + contactId)
                .then()
                .statusCode(200);
    }

    @Then("veo el detalle del contacto")
    public void veo_el_detalle_del_contacto() {
        SerenityRest.lastResponse().then()
                .body("_id", equalTo(Serenity.sessionVariableCalled(CONTACT_ID)))
                .body("firstName", notNullValue())
                .body("lastName", notNullValue());
    }

    @When("actualizo el contacto con nuevos datos")
    public void actualizo_el_contacto_con_nuevos_datos() {
        String token = Serenity.sessionVariableCalled(TOKEN);
        String contactId = Serenity.sessionVariableCalled(CONTACT_ID);

        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "ContactoEditado");
        body.put("lastName", "PruebaEditada");
        body.put("birthdate", "1999-12-31");
        body.put("email", "updated_" + UUID.randomUUID() + "@mail.com");
        body.put("phone", "3110000000");
        body.put("street1", "Carrera 7 # 10-20");
        body.put("street2", "Oficina 202");
        body.put("city", "Bogota");
        body.put("stateProvince", "Cundinamarca");
        body.put("postalCode", "110221");
        body.put("country", "Colombia");

        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(body)
                .when()
                .put("/contacts/" + contactId)
                .then()
                .statusCode(200);
    }

    @Then("el contacto se actualiza correctamente")
    public void el_contacto_se_actualiza_correctamente() {
        SerenityRest.lastResponse().then()
                .body("firstName", equalTo("ContactoEditado"))
                .body("lastName", equalTo("PruebaEditada"));
    }

    @When("elimino el contacto")
    public void elimino_el_contacto() {
        String token = Serenity.sessionVariableCalled(TOKEN);
        String contactId = Serenity.sessionVariableCalled(CONTACT_ID);

        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/contacts/" + contactId)
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }

    @Then("el contacto ya no aparece en la lista")
    public void el_contacto_ya_no_aparece_en_la_lista() {
        String token = Serenity.sessionVariableCalled(TOKEN);
        String contactId = Serenity.sessionVariableCalled(CONTACT_ID);

        List<String> ids = SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/contacts")
                .then()
                .statusCode(200)
                .extract()
                .path("_id"); // lista de ids

        assertThat(ids, not(hasItem(contactId)));
    }
}