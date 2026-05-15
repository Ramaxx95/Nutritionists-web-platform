package com.inutri.validacion;

import com.inutri.exception.suscripcion.SoloPremiumException;
import com.inutri.modelo.Usuario;
import com.inutri.modelo.enums.TipoSuscripcion;

public class SuscripcionValidator {

    public static void validarEsPremium(Usuario usuario) {
        if (usuario.getTipoSubscripcion() == TipoSuscripcion.FREE) {
            throw new SoloPremiumException();
        }
    }
}