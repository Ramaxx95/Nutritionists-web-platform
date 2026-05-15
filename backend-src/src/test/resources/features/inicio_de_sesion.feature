# language: es
Característica: US-02 Inicio de sesion
  Como usuario
  quiero iniciar sesión usando mail y contraseña
  para poder acceder a la aplicación

  Escenario: 2.01 Inicio de sesión exitoso con credenciales válidas
    Dado un usuario que ya existe en el sistema
    Y su contraseña
    Cuando inicio sesión con esos datos
    Entonces veo el home de la aplicación

  Escenario: 2.02 Inicio de sesión fallido por contraseña incorrecta
    Dado un usuario que ya existe en el sistema
    Y una contraseña que no es correcta
    Cuando inicio sesión con esos datos
    Entonces veo un mensaje de credenciales incorrectas

  Escenario: 2.03 Inicio de sesión fallido por usuario no registrado
    Dado un usuario que no existe en el sistema
    Y una contraseña
    Cuando inicio sesión con esos datos
    Entonces veo un mensaje de usuario no existente