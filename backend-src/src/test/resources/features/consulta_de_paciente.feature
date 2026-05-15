# language: es
Característica: US-33 Consulta de paciente
  Como nutricionista
  quiero consultar la información de un paciente
  para poder analizar su estado actual y progreso

  Antecedentes:
    Dado que inicie sesion con un usuario existente

  Escenario: 33.01 Consulta de información de paciente
    Dado que tengo registrado al paciente "Gustavo Pacheco" con peso "100" kg, talla "200" cm y fecha de nacimiento "2000-01-01"
    Cuando entro a la pantalla de gestion del paciente
    Entonces veo que su nombre es "Gustavo Pacheco"
    Y veo que su peso es "100" kg
    Y veo que su talla es "200" cm
    Y veo que su fecha de nacimiento es "2000-01-01"