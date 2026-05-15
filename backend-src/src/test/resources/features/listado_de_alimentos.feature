# language: es
Característica: US-14 Listado de alimentos
  Como nutricionista
  quiero consultar el listado de alimentos
  para poder ver las distintas opciones alimentarias que podría ofrecer a los pacientes

  Antecedentes:
    Dado que inicie sesion con un usuario existente

  Escenario: 14.01 Consulta de catálogo de alimentos
    Cuando consulto el catalogo de alimentos
    Entonces veo los alimentos existentes con su categoria, nombre, cantidad de Kcal, proteinas, carbohidratos, grasas, colesterol y sodio