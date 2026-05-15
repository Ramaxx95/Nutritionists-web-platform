# language: es
Característica: US-27 Selección de ecuación para VCT
  Como nutricionista
  quiero elegir una ecuación de VCT
  para personalizar los objetivos calóricos de un plan de alimentación

  Antecedentes:
    Dado que inicie sesion con un usuario existente
    Y que tengo registrado al paciente de nombre "Andres" y apellido "Paz" de genero "masculino" con peso "75" kg, talla "170" cm y fecha de nacimiento "2000-05-05"

  Escenario: 27.01 Armado de plan con ecuacion ADA
    Dado que estoy en la pantalla de armado de plan para el paciente
    Y que selecciono la ecuacion para VCT "ADA"
    Y selecciono las 4 comidas
    Cuando toco el boton de guardar el plan
    Entonces veo un mensaje de plan guardado exitosamente
    Y la cantidad de Kcal recomendadas es "3060.00" Kcal por dia

  Escenario: 27.02 Armado de plan con ecuacion Harris-Benedict
    Dado que estoy en la pantalla de armado de plan para el paciente
    Y que selecciono la ecuacion para VCT "HARRIS_BENEDICT"
    Y selecciono las 4 comidas
    Cuando toco el boton de guardar el plan
    Entonces veo un mensaje de plan guardado exitosamente
    Y la cantidad de Kcal recomendadas es "3014.95" Kcal por dia

  Escenario: 27.03 Armado de plan con ecuacion FAO/OMS
    Dado que estoy en la pantalla de armado de plan para el paciente
    Y que selecciono la ecuacion para VCT "FAO_OMS"
    Y selecciono las 4 comidas
    Cuando toco el boton de guardar el plan
    Entonces veo un mensaje de plan guardado exitosamente
    Y la cantidad de Kcal recomendadas es "3096.51" Kcal por dia