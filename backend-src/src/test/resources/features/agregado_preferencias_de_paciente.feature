# language: es
Característica: US-09 Agregado preferencias de paciente
  Como nutricionista
  quiero registrar las preferencias alimentarias del paciente
  para poder armar planes de alimentación personalizados que sean más faciles de asimilar

  Antecedentes:
    Dado que inicie sesion con un usuario existente
    Y estoy en la pantalla de gestión del paciente "Alan Rivas" sin preferencias cargadas

  Escenario: 9.01 Registro exitoso de preferencias del paciente
    Dado que no le gusta el alimento "Chorizo seco"
    Y que le gusta el alimento "Merluza"
    Y que le gusta el alimento "Arroz blanco, hervido"
    Cuando actualizo los gustos del paciente
    Entonces veo un mensaje de paciente actualizado
    Y veo que en sus gustos tiene "Merluza"
    Y veo que en sus gustos tiene "Arroz blanco, hervido"
    Y veo que en sus desagrados tiene "Chorizo seco"

  Escenario: 9.02 Borrado exitoso de preferencias del paciente
    Dado que dejo las preferencias del paciente como una lista vacia
    Cuando actualizo los gustos del paciente
    Entonces veo un mensaje de paciente actualizado
    Y veo que sus gustos estan vacios
    Y veo que sus desagrados estan vacios

  Escenario: 9.03 Cambio de preferencias del paciente
    Dado que no le gusta el alimento "Chorizo seco"
    Y que le gusta el alimento "Merluza"
    Pero se desea cambiar sus gustos por "Arroz blanco, hervido" y ningun desagrado
    Cuando actualizo los gustos del paciente
    Entonces veo un mensaje de paciente actualizado
    Y veo que en sus gustos tiene "Arroz blanco, hervido"
    Y veo que sus desagrados estan vacios
