package com.inutri.modelo.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class AlergenosPersonalizadosConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> alergenos) {
        if (alergenos == null || alergenos.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(alergenos);
        } catch (Exception e) {
            throw new RuntimeException("Error convirtiendo lista de alergenos a JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo JSON de alergenos personalizados", e);
        }
    }
}