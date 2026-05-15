# language: es
Característica: US-01 Registro de usuario
  Como usuario
  quiero registrarme usando mail y contraseña
  para poder comenzar a usar la aplicación

  Escenario: 1.01 Registro exitoso de usuario nuevo
    Dado un correo electronico que no esta registrado en el sistema
    Y un nombre y apellido
    Y una contraseña que cumple con los requisitos de seguridad
    Cuando intento registrarme usando esos datos
    Entonces veo un mensaje de exito

  Escenario: 1.02 Registro fallido con correo ya existente
    Dado un correo electronico que ya esta registrado en el sistema
    Y un nombre y apellido
    Y una contraseña que cumple con los requisitos de seguridad
    Cuando intento registrarme usando esos datos
    Entonces veo un mensaje de error que me informa que ese correo ya esta en uso

  Escenario: 1.03 Registro fallido por campos obligatorios vacíos
    Dado un correo electronico que no esta registrado en el sistema
    Y una contraseña que cumple con los requisitos de seguridad
    Cuando intento registrarme usando esos datos
    Entonces veo un mensaje de campos obligatorios faltantes

  Escenario: 1.04 Registro fallido por campos inválidos
    Dado un correo electronico que no esta registrado en el sistema
    Y un nombre y apellido
    Y una contraseña que no cumple con los requisitos de seguridad
    Cuando intento registrarme usando esos datos
    Entonces veo mensajes claros de validacion que me indican que debo corregir

  Escenario: 1.05 Registro fallido por confirmación de contraseña
    Dado un correo electronico que no esta registrado en el sistema
    Y un nombre y apellido
    Y una contraseña que cumple con los requisitos de seguridad
    Cuando intento registrarme poniendo una contraseña distinta en el campo de confirmacion
    Entonces veo un mensaje de que las contraseñas no coinciden