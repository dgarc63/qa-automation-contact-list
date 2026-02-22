Feature: Users API - Contact List

  Scenario: Registro y login exitoso
    Given creo un usuario nuevo por la API
    When inicio sesión por la API
    Then obtengo un token válido

  Scenario: Consultar perfil de usuario
    Given tengo un usuario autenticado
    When consulto mi perfil
    Then veo mi información de usuario

  Scenario: Login inválido
    Given existe un usuario registrado
    When intento iniciar sesión con password incorrecto
    Then recibo un error de autenticación