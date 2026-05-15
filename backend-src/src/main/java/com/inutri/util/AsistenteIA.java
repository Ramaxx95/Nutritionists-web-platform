package com.inutri.util;

import io.github.sashirestela.cleverclient.client.JavaHttpClientAdapter;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.Getter;

import java.util.*;

public class AsistenteIA {

    private final SimpleOpenAI asistente;
    private List<ChatMessage> mensajes;
    @Getter
    private String archivoEnJson;
    private Integer tokens;

    public AsistenteIA() {
        asistente = SimpleOpenAI.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .clientAdapter(new JavaHttpClientAdapter())
                .build();
        mensajes = new ArrayList<>();
        tokens = 0;
    }

    public void prepararConversacion() {

        try {
            archivoEnJson = ParseadorCsvAJson.parsearArchivo(System.getenv("CSV_FILE_PATH"));
            mensajes.add(ChatMessage.SystemMessage.of("Eres un nutricionista profesional que debe sugerir alimentos que cumplan con los requerimientos de los usuarios."));
        } catch (NullPointerException e) {
            System.out.println("[DEBUG - AsistenteIA] ERROR - El parseador de Json devolvio un objeto nulo.");
        }

    }

    public String hacerConsulta(String prompt, Class<?> formatoRespuesta) {
        String respuesta;

        mensajes.add(ChatMessage.UserMessage.of(prompt));
        var request = ChatRequest.builder()
                .model("gpt-4o-mini")
                .messages(mensajes)
                .responseFormat(ResponseFormat.jsonSchema(ResponseFormat.JsonSchema.builder()
                        .name("FormatoRespuesta")
                        .schemaClass(formatoRespuesta)
                        .strict(true)
                        .build()))
                .temperature(0.7)
                .build();
        var respuestaIA = this.asistente.chatCompletions().create(request).join();
        this.tokens = respuestaIA.getUsage().getTotalTokens();

        respuesta = respuestaIA.getChoices().getFirst().getMessage().getContent();
        System.out.println("[DEBUG - AsistenteIA] Esto respondio la IA: " + respuesta);
        System.out.println();
        System.out.println("[DEBUG - AsistenteIA] Cantidad de tokens usados: " + this.tokens);
        System.out.println();
        return respuesta;
    }

    public String refinarConsulta(String respuestaAnterior, String nuevaPrompt) {
        mensajes.add(ChatMessage.AssistantMessage.of(respuestaAnterior));
        return hacerConsulta(nuevaPrompt, FormatoRespuesta.RespuestaAlimentoParaPlan.class);
    }

    public void finalizarConversacion(){
        asistente.shutDown();
    }

    public Integer getTokensUsados(){
        return tokens;
    }

}
