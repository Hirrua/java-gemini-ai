package com.hirrua.CvAi.dto;

import java.util.List;

public record AnaliseResponse(List<String> pontos_fortes,
                              List<String> pontos_fracos,
                              Integer percentual_adequacao,
                              String explicacao_adequacao,
                              List<String> correspondencias_chave,
                              String melhorias,
                              List<String> alertas) {
}