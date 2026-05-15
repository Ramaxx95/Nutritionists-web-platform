# language: es
Característica: US-08 Actualizacion de paciente
  Como usuario de la aplicación nutricionista
  quiero actualizar los datos del paciente
  para llevar un seguimiento preciso de su evolución y necesidades

Antecedentes:
  Dado que inicie sesion con un usuario existente
  Y estoy en la pantalla de gestión del paciente "Sofia Pedraza" con peso "90" kg y talla "150" cm

Escenario: 8.01 Actualización exitosa de datos del paciente
  Dado que su talla actual es de "153" cm
  Y que su peso actual es de "85" kg
  Cuando actualizo los datos de gestión
  Entonces veo un mensaje de información actualizada
  Y veo que la talla ahora es de "153" cm
  Y veo que el peso ahora es de "85" kg

Escenario: 8.02 Actualización fallida con datos inválidos
  Dado que su peso actual es de "0" kg
  Cuando actualizo los datos de gestión
  Entonces veo un mensaje de datos ingresados no válidos