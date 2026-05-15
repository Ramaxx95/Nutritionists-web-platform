# language: es
Característica: US-06 Listado de pacientes
  Como nutricionista
  quiero consultar mi listado de pacientes registrados
  para poder elegir con cual trabajar

  Antecedentes:
    Dado que inicie sesion con un usuario existente

  Escenario: 6.01 Consulta de listado de pacientes de un nutricionista sin pacientes
    Dado que no tengo ningún paciente registrado
    Cuando consulto el listado de pacientes
    Entonces veo que no hay ningun elemento en el mismo
    Y veo la opción de registrar a un paciente

  Escenario: 6.02 Consulta de listado de pacientes de un nutricionista con 2 pacientes
    Dado que tengo registrado al paciente "Juan Perez"
    Y que tengo registrado al paciente "Maria Lopez"
    Cuando consulto el listado de pacientes
    Entonces veo al paciente "Maria Lopez" en el listado
    Y veo al paciente "Juan Perez" en el listado