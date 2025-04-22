# Java com Gemini

Criei esse projeto com o intuito de estudar um pouco sobre integração com alguma LLM.
A ideia surgiu de uma disciplina da faculdade, onde resolvi desenvolver uma API para analisar o curriculum do candidato com base na descrição da vaga em questão.

O projeto da faculdade foi implementado em javascript e desafiei-me para reescrever em Java com Spring Boot.
Aproveitei o projeto para aprender como criar um parser em java, utilizando jackson, Pattern, Matcher e ObjectMapper!

> É necessária a configuração do VertexAi na sua conta do Google Cloud.

O body da requisição deve ser feita em **form-data**.

### Tecnologias

- Java
- Spring Boot
- Gemini model
- Vertex AI
- Jackson
- Apache PdfBox

### Estrutura JSON

`{
    "pontos_fortes": [
        "Experiência com Java e Spring Boot",
        "Conhecimento em bancos de dados relacionais (PostgreSQL, MySQL)",
        "Familiaridade com JavaScript e TypeScript",
        "Experiência com APIs RESTful",
        "Conhecimento em Git e Docker",
        "Experiência com desenvolvimento backend",
        "Conhecimento em POO",
        "Projetos práticos em Spring Boot e React Native",
        "Certificações em Java, JavaScript, TypeScript e AWS"
    ],
    "pontos_fracos": [
        "Falta de experiência profissional direta com Angular",
        "Experiência limitada com metodologias ágeis (mencionado apenas como diferencial)",
        "Pouca experiência com serviços em nuvem (AWS, Azure) além dos cursos"
    ],
    "percentual_adequacao": 75,
    "explicacao_adequacao": "O candidato demonstra um bom conhecimento técnico em Java, Spring Boot e bancos de dados, além de experiência com APIs RESTful. A falta de experiência profissional com Angular e metodologias ágeis diminui o percentual, mas o currículo mostra potencial.",
    "correspondencias_chave": [
        "Java",
        "Spring Boot",
        "JavaScript",
        "RESTful API",
        "PostgreSQL",
        "Git",
        "Docker",
        "Backend",
        "TypeScript"
    ],
    "melhorias": "Incluir projetos ou experiências com Angular para fortalecer o perfil full-stack. Detalhar o uso de metodologias ágeis em projetos (mesmo que acadêmicos).  Mencionar a experiência com microsserviços.",
    "alertas": [
        "Verificar o nível de proficiência em Angular durante a entrevista",
        "Aprofundar a experiência com metodologias ágeis e serviços em nuvem",
        "Avaliar a capacidade de trabalhar em equipe e resolver problemas"
    ]
}`