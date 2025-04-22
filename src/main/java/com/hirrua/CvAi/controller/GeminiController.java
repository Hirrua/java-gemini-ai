package com.hirrua.CvAi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirrua.CvAi.dto.AnaliseResponse;
import com.hirrua.CvAi.util.JsonParser;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

@RestController
@RequestMapping("/gemini")
public class GeminiController {

    private final VertexAiGeminiChatModel chatModel;

    public GeminiController(VertexAiGeminiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "query", defaultValue = "Conte uma piada") String query) {
        var prompt = new Prompt(query);

        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    @PostMapping(value = "/analisar", consumes = "multipart/form-data")
    public AnaliseResponse analyzeCandidate(
            @RequestPart("description") String description,
            @RequestPart("file") MultipartFile file) throws IOException {

        String resumeText = extractTextFromPdf(file.getBytes());

        String promptText = String.format("""
        Faça uma análise detalhada do candidato para esta vaga. Retorne APENAS UM JSON válido SEM FORMATAÇÃO com:
        {
            "pontos_fortes": ["string"],
            "pontos_fracos": ["string"],
            "percentual_adequacao": 0,
            "explicacao_adequacao": "string",
            "correspondencias_chave": ["string"],
            "alertas": ["string"],
            "melhorias": "string"
        }
        
        Regras:
              - Pontos fortes: lista de até 10 competências alinhadas à vaga
              - Pontos fracos: lista de até 5 deficiências em relação à vaga
              - Percentual: 0-100 baseado na adequação técnica
              - Explicação: 2-3 frases resumindo a avaliação
              - Correspondências: termos técnicos relevantes encontrados
              - Alertas: pontos críticos para verificação
              - Sugestões: palavras-chave faltantes para incluir
              - Melhorias: sugestões práticas para o currículo
        
        Dados para análise:
        DESCRIÇÃO DA VAGA: %s
        TEXTO DO CURRÍCULO: %s
        """, description, resumeText);

        String geminiResponse = chatModel.call(new Prompt(promptText)).getResult().getOutput().getText();
        return JsonParser.parserJson(geminiResponse);
    }

    private String extractTextFromPdf(byte[] pdfBytes) throws IOException {
        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            return new PDFTextStripper().getText(doc);
        }
    }
}
