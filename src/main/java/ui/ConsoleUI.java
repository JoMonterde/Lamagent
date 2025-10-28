package ui;

import agent.ChatAgent;

import java.util.*;

public class ConsoleUI {

    private final ChatAgent agent;

    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(ChatAgent agent) {
        this.agent = agent;
    }

    public void run() {
        System.out.println("Assistant culinaire — tape 'exit' pour quitter.");
        while (true) {
            System.out.print("\nEntrez ingrédients (virgule séparés) : ");
            String line = scanner.nextLine();
            if (line == null || line.trim().equalsIgnoreCase("exit")) break;
            List<String> ingredients = Arrays.stream(line.split(",")).map(String::trim).toList();

            System.out.print("Allergies (virgule séparés, ou 'none') : ");
            String al = scanner.nextLine();
            List<String> allergies = al.trim().equalsIgnoreCase("none") ? List.of() :
                    Arrays.stream(al.split(",")).map(String::trim).toList();

            System.out.print("Préférences (ex: végétarien, sans gluten, rapide) : ");
            String pref = scanner.nextLine();

            String result = agent.handleRecipeRequest(ingredients, allergies, pref);
            System.out.println("\n=== Réponse ===\n" + result);
        }
        System.out.println("Au revoir !");
    }
}
