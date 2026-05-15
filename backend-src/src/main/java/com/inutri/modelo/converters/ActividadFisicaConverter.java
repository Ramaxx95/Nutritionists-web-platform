package com.inutri.modelo.converters;

import com.inutri.modelo.enums.ActividadFisica;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ActividadFisicaConverter implements AttributeConverter<ActividadFisica, String> {

    @Override
    public String convertToDatabaseColumn(ActividadFisica attribute) {
        return attribute != null ? attribute.getDescripcion() : null;
    }

    @Override
    public ActividadFisica convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (ActividadFisica af : ActividadFisica.values()) {
            if (af.getDescripcion().equalsIgnoreCase(dbData.trim())) {
                return af;
            }
        }
        throw new IllegalArgumentException("Valor inválido para ActividadFisica: " + dbData);
    }
}