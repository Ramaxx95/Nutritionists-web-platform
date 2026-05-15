# language: es
Característica: US-34 Armado de plan de alimentacion
  Como nutricionista
  quiero poder armar un plan de alimentación
  para indicar al paciente la dieta que debe seguir para alcanzar su objetivo

  Antecedentes:
    Dado que inicie sesion con un usuario existente
    Y que tengo registrado al paciente de nombre "Daniela" y apellido "Montana" de genero "femenino" con peso "80" kg, talla "180" cm y fecha de nacimiento "1998-10-03"

  Escenario: 34.01 Armado de plan exitoso
    Dado que estoy en la pantalla de armado de plan para el paciente
    Y agrego el alimento "Leche descremada fluida, con 50% más de proteínas" en la comida "DESAYUNO" con cantidad "100" gramos
    Y agrego el alimento "Tostadas light" en la comida "DESAYUNO" con cantidad "200" gramos
    Y agrego el alimento "Pollo con piel (horno/parrilla)" en la comida "ALMUERZO" con cantidad "200" gramos
    Y agrego el alimento "Zapallito, hervido" en la comida "ALMUERZO" con cantidad "200" gramos
    Y agrego el alimento "Manzana con piel" en la comida "MERIENDA" con cantidad "300" gramos
    Y agrego el alimento "Arroz blanco, hervido" en la comida "CENA" con cantidad "200" gramos
    Y agrego el alimento "Hamburguesa de carne vacuna (carnicería)" en la comida "CENA" con cantidad "250" gramos
    Cuando toco el boton de guardar el plan
    Entonces veo un mensaje de plan guardado exitosamente

    Escenario: 34.02 Armado de plan fallido por comida faltante
    Dado que estoy en la pantalla de armado de plan para el paciente
    Y agrego el alimento "Tostadas light" en la comida "DESAYUNO" con cantidad "100" gramos
    Y agrego el alimento "Leche descremada fluida, con 50% más de proteínas" en la comida "MERIENDA" con cantidad "100" gramos
    Y agrego el alimento "Pollo con piel (horno/parrilla)" en la comida "CENA" con cantidad "100" gramos
    Cuando toco el boton de guardar el plan
    Entonces veo un mensaje de comida faltante