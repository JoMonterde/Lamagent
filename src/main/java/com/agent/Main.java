package com.agent;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String endpoint = "http://localhost:11434"; 
        String model = "llama3";                   

        OllamaClient client = new OllamaClient(endpoint);
        AgentIA agent = new AgentIA(client, model);

        Scanner scanner = new Scanner(System.in);
        System.out.println("🤖 Bonjour, je suis ton agent IA via Ollama !");
        System.out.println("Pose-moi une question (ou tape 'quit' pour quitter).");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if ("quit".equalsIgnoreCase(input)) {
                break;
            }
            String r = agent.repondre(input);
            System.out.println(r);
        }
        scanner.close();
        System.out.println("👋 À bientôt !");
    }
}