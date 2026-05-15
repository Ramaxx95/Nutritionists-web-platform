# language: es
Característica: US-07 Registro de paciente
  Como nutricionista
  quiero registrar a un paciente
  para gestionar su alimentación

  Antecedentes:
    Dado que inicie sesion con un usuario existente

  Escenario: 7.01 Registro exitoso de nuevo paciente
    Dado una persona con nombre "Manuel" y apellido "Sevilla", de sexo "masculino", fecha de nacimiento "2001-06-14", talla "170" cm, peso "100" kg, objetivo "Bajar de peso", peso objetivo "75" y ninguna condición de salud
    Cuando lo registro como nuevo paciente
    Entonces veo un mensaje de paciente registrado
    Y veo la pantalla de gestión del paciente

  Escenario: 7.02 Registro fallido por campos obligatorios no informados
    Dado una persona con nombre " " y apellido " ", de sexo "masculino", fecha de nacimiento "2005-08-20", talla "185" cm, peso "70" kg, objetivo " ", peso objetivo "80" y ninguna condición de salud
    Cuando lo registro como nuevo paciente
    Entonces veo un mensaje de campos obligatorios no informados