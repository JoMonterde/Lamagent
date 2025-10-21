package com.agent;

public class AgentIA {
    private final OllamaClient client;
    private final String model;

    public AgentIA(OllamaClient client, String model) {
        this.client = client;
        this.model = model;
    }

    public String repondre(String question) {
        try {
            // ici on utilise chat plutôt que simple generate
            return client.chat(model, question);
        } catch (Exception e) {
            e.printStackTrace();
            return "Désolé, je n'ai pas pu obtenir de réponse.";
        }
    }
}