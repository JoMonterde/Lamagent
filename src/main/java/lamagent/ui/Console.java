package lamagent.ui;

import lamagent.agent.OllamaCookingAgent;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Console {

    public static void main(String[] args) {
        OllamaCookingAgent agent = new OllamaCookingAgent();
        Scanner scanner = new Scanner(System.in);

        System.out.println("👩‍🍳 Bienvenue dans ton assistant culinaire intelligent !");
        System.out.println("Tape 'exit' pour quitter.\n");

        while (true) {
            System.out.print("Allergies connues : ");
            String allergyInput = scanner.nextLine();
            if (allergyInput.equalsIgnoreCase("exit")) break;

            System.out.print("Préférences : ");
            String prefInput = scanner.nextLine();
            if (prefInput.equalsIgnoreCase("exit")) break;

            List<String> allergies = Arrays.asList(allergyInput.split(","));
            List<String> prefs = Arrays.asList(prefInput.split(","));

            String response = agent.suggestRecipe(allergies, prefs);
            System.out.println("\n🍽️ Recette proposée :\n" + response);
            System.out.println("\n---------------------------------\n");
        }

        System.out.println("👋 À bientôt !");
    }
}
