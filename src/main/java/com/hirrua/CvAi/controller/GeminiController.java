package com.hirrua.CvAi.controller;

import com.hirrua.CvAi.dto.AnaliseResponse;
import com.hirrua.CvAi.util.JsonParser;
import com.hirrua.CvAi.util.PdfParser;
import net.sourceforge.lept4j.util.LoadLibs;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/gemini")
public class GeminiController {

    private final VertexAiGeminiChatModel chatModel;
    private static String result;

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
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException, TesseractException {

        Tesseract tesseract = new Tesseract();
        File tessDataFolder = LoadLibs.extractNativeResources("tessdata");
        tesseract.setDatapath(tessDataFolder.getAbsolutePath());
        tesseract.setLanguage("por");

        if (file != null) {
            if (Objects.requireNonNull(file.getContentType()).equalsIgnoreCase("application/pdf")) {
                result = PdfParser.extractTextFromPdf(file.getBytes());
            } else if (file.getContentType().startsWith("image/")) {
                try {
                    BufferedImage image = ImageIO.read(file.getInputStream());
                    result = tesseract.doOCR(image);
                } catch (TesseractException e) {
                    e.printStackTrace();
                }
            }
        }

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
        """, description, result);

        String geminiResponse = chatModel.call(new Prompt(promptText)).getResult().getOutput().getText();
        return JsonParser.parserJson(geminiResponse);
    }
}
