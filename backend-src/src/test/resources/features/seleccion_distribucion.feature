# language: es
Característica: US-28 Distribución de macronutrientes
  Como nutricionista
  quiero definir y ajustar la distribución de macronutrientes en base al VCT del paciente
  para personalizar el plan alimentario según objetivos, necesidades y restricciones

  Antecedentes:
    Dado que inicie sesion con un usuario existente
    Y que tengo registrado al paciente de nombre "Andres" y apellido "Paz" de genero "masculino" con peso "75" kg, talla "170" cm y fecha de nacimiento "2000-05-05"

  Escenario: 28.01 Armado de plan con ecuacion ADA y distribucion TRADICIONAL
    Dado que estoy en la pantalla de armado de plan para el paciente
    Y que selecciono la ecuacion para VCT "ADA"
    Y que selecciono la distribucion de macronutrientes "TRADICIONAL"
    Y selecciono las 4 comidas
    Cuando toco el boton de guardar el plan
    Entonces veo un mensaje de plan guardado exitosamente
    Y la cantidad de carbohidratos recomendadas es "420.75" g por dia
    Y la cantidad de proteinas recomendadas es "114.75" g por dia
    Y la cantidad de grasas recomendadas es "102.00" g por dia

  Escenario: 28.02 Armado de plan con ecuacion ADA y distribucion PROTEICA
    Dado que estoy en la pantalla de armado de plan para el paciente
    Y que selecciono la ecuacion para VCT "ADA"
    Y que selecciono la distribucion de macronutrientes "PROTEICA"
    Y selecciono las 4 comidas
    Cuando toco el boton de guardar el plan
    Entonces veo un mensaje de plan guardado exitosamente
    Y la cantidad de carbohidratos recomendadas es "382.50" g por dia
    Y la cantidad de proteinas recomendadas es "153.00" g por dia
    Y la cantidad de grasas recomendadas es "102.00" g por dia

  Escenario: 28.03 Armado de plan con ecuacion Harris-Benedict y distribucion TRADICIONAL
    Dado que estoy en la pantalla de armado de plan para el paciente
    Y que selecciono la ecuacion para VCT "HARRIS_BENEDICT"
    Y que selecciono la distribucion de macronutrientes "TRADICIONAL"
    Y selecciono las 4 comidas
    Cuando toco el boton de guardar el plan
    Entonces veo un mensaje de plan guardado exitosamente
    Y la cantidad de carbohidratos recomendadas es "414.56" g por dia
    Y la cantidad de proteinas recomendadas es "113.06" g por dia
    Y la cantidad de grasas recomendadas es "100.50" g por dia

  Escenario: 28.04 Armado de plan con ecuacion Harris-Benedict y distribucion PROTEICA
    Dado que estoy en la pantalla de armado de plan para el paciente
    Y que selecciono la ecuacion para VCT "HARRIS_BENEDICT"
    Y que selecciono la distribucion de macronutrientes "PROTEICA"
    Y selecciono las 4 comidas
    Cuando toco el boton de guardar el plan
    Entonces veo un mensaje de plan guardado exitosamente
    Y la cantidad de carbohidratos recomendadas es "376.87" g por dia
    Y la cantidad de proteinas recomendadas es "150.75" g por dia
    Y la cantidad de grasas recomendadas es "100.50" g por dia

  Escenario: 28.05 Armado de plan con ecuacion FAO/OMS y distribucion TRADICIONAL
    Dado que estoy en la pantalla de armado de plan para el paciente
    Y que selecciono la ecuacion para VCT "FAO_OMS"
    Y que selecciono la distribucion de macronutrientes "TRADICIONAL"
    Y selecciono las 4 comidas
    Cuando toco el boton de guardar el plan
    Entonces veo un mensaje de plan guardado exitosamente
    Y la cantidad de carbohidratos recomendadas es "425.77" g por dia
    Y la cantidad de proteinas recomendadas es "116.12" g por dia
    Y la cantidad de grasas recomendadas es "103.22" g por dia

  Escenario: 28.06 Armado de plan con ecuacion FAO/OMS y distribucion PROTEICA
    Dado que estoy en la pantalla de armado de plan para el paciente
    Y que selecciono la ecuacion para VCT "FAO_OMS"
    Y que selecciono la distribucion de macronutrientes "PROTEICA"
    Y selecciono las 4 comidas
    Cuando toco el boton de guardar el plan
    Entonces veo un mensaje de plan guardado exitosamente
    Y la cantidad de carbohidratos recomendadas es "387.06" g por dia
    Y la cantidad de proteinas recomendadas es "154.83" g por dia
    Y la cantidad de grasas recomendadas es "103.22" g por dia