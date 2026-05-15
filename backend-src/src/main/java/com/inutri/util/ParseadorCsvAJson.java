package com.inutri.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.*;
import java.util.List;
import java.util.Map;

public class ParseadorCsvAJson {

    private ParseadorCsvAJson() {}

    public static String parsearArchivo (String rutaArchivo) {

        try {
            File csvFile = new File(rutaArchivo);

            // Mapper específico para CSV
            CsvMapper csvMapper = new CsvMapper();
            csvMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Usamos la primera fila como nombres de campos
            CsvSchema schema = CsvSchema.builder().setColumnSeparator(';').setUseHeader(true).build();

            // Leemos todas las filas como Map<String, String>
            MappingIterator<Map<String, String>> iterador = csvMapper.readerFor(Map.class)
                    .with(schema)
                    .readValues(csvFile);
            List<Map<String, String>> data = iterador.readAll();

            // Lo convertimos a JSON string
            ObjectMapper jsonMapper = new ObjectMapper();
            return jsonMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(data);

        } catch (Exception e){
            System.out.println("[ERROR] Archivo no encontrado, " + e.toString());
            return null;
        }

    }

}
