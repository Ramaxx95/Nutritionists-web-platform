# language: es

Característica: US-17 Busqueda de alimento
  Como nutricionista
  quiero poder buscar un alimento del catálogo por su nombre y/o categoría
  para agilizar la selección sin tener que pasar por todos los demás alimentos primero

  Antecedentes:
    Dado que inicie sesion con un usuario existente
    Y que ingrese al catálogo de alimentos

  Escenario: 17.01 Búsqueda por nombre completo
    Dado que existe actualmente el alimento "Chorizo seco" de categoría "Carnes"
    Cuando ingreso en el buscador "chorizo seco"
    Entonces veo el alimento "Chorizo seco" como resultado de búsqueda

  Escenario: 17.02 Búsqueda por partes del nombre
    Dado que existe actualmente el alimento "Galleta marinera" de categoría "Legumbres, cereales, papa, pan y pastas"
    Y el alimento "Grisines" de categoría "Legumbres, cereales, papa, pan y pastas"
    Y el alimento "Chinchulines, tripagorda (horno/parrilla)" de categoría "Carnes"
    Cuando ingreso en el buscador "ine"
    Entonces veo el alimento "Galleta marinera" como resultado de búsqueda
    Y el alimento "Grisines" como resultado de búsqueda
    Y el alimento "Chinchulines, tripagorda (horno/parrilla)" como resultado de búsqueda

  Escenario: 17.03 Filtrado por categoría
    Dado que existe actualmente el alimento "Merluza" de categoría "Pescados y mariscos"
    Y el alimento "Pejerrey" de categoría "Pescados y mariscos"
    Cuando filtro los alimentos por la categoría "Pescados y mariscos"
    Entonces veo el alimento "Merluza" como resultado de búsqueda
    Y el alimento "Pejerrey" como resultado de búsqueda

  Escenario: 17.04 Búsqueda por nombre y filtrado por categoría combinados
    Dado que existe actualmente el alimento "Galleta marinera" de categoría "Legumbres, cereales, papa, pan y pastas"
    Y el alimento "Grisines" de categoría "Legumbres, cereales, papa, pan y pastas"
    Y el alimento "Chinchulines, tripagorda (horno/parrilla)" de categoría "Carnes"
    Cuando ingreso en el buscador "ine"
    Y filtro los alimentos por la categoría "Carnes"
    Entonces veo el alimento "Chinchulines, tripagorda (horno/parrilla)" como resultado de búsqueda
    Y no veo el alimento "Grisines" como resultado de búsqueda
    Y no veo el alimento "Galleta marinera" como resultado de búsqueda