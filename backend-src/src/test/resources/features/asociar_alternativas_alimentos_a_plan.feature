# language: es
Característica: US-33 Asociar alternativas de alimentos para un plan de alimentación
  Como nutricionista
  Quiero poder asociar multiples alternativas de alimentos a un mismo plan
  Para poder darle opciones de comidas diferentes a mis pacientes

  Antecedentes:
    Dado que inicie sesion con un usuario existente
    Y que tengo registrado al paciente de nombre "Daniela" y apellido "Montana" de genero "femenino" con peso "80" kg, talla "180" cm y fecha de nacimiento "1998-10-03"

  Escenario: 33.01 Crear una primera alternativa al crear un plan
    Dado que estoy armando un nuevo plan para el paciente
    Cuando guardo el plan
    Entonces veo que el plan fue guardado exitosamente
    Y veo que el plan posee 1 alternativa de alimentos

  Escenario: 33.02 Crear una nueva alternativa a un plan existente
    Dado que el paciente ya tiene un plan armado
    Cuando armo una nueva alternativa de alimentos
    Entonces veo un mensaje de alternativa guardada
    Y veo que el plan posee 2 alternativa de alimentos