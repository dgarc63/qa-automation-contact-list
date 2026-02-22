Feature: Contacts API - Contact List

  Scenario: Crear contacto exitosamente
    Given tengo un usuario autenticado
    When creo un contacto con datos válidos
    Then el contacto se crea correctamente

  Scenario: Consultar contacto por id
    Given existe un contacto creado
    When consulto el contacto por id
    Then veo el detalle del contacto

  Scenario: Actualizar contacto (PUT)
    Given existe un contacto creado
    When actualizo el contacto con nuevos datos
    Then el contacto se actualiza correctamente

  Scenario: Eliminar contacto
    Given existe un contacto creado
    When elimino el contacto
    Then el contacto ya no aparece en la lista