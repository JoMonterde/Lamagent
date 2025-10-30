package lamagent.ui;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import lamagent.agent.OllamaCookingAgent;

/**
 * The {@code Console} class provides a simple command-line interface (CLI) for
 * interacting
 * with the {@link OllamaCookingAgent}.
 * <p>
 * Users can input their food allergens and culinary preferences, and the agent
 * will
 * attempt to suggest safe recipes that respect the specified allergies.
 * </p>
 *
 * @see OllamaCookingAgent
 */
public class Console {

    /**
     * Main entry point of the console application.
     * <p>
     * Continuously prompts the user for allergens and preferences, queries the
     * {@link OllamaCookingAgent}, and prints the recipe suggestions or relevant
     * messages.
     * The loop continues until the user types {@code exit}.
     * </p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        OllamaCookingAgent agent = new OllamaCookingAgent();
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.println("======================================");
        System.out.println("   Welcome to your Cooking Assistant   ");
        System.out.println("======================================\n");
        System.out.println("You can ask for recipes based on your ingredients.");
        System.out.println("Type 'exit' at any time to quit.\n");

        while (true) {

            // Ingredients
            System.out.print("Enter your available ingredients (comma separated): ");
            String ingredientsLine = scanner.nextLine();
            if (ingredientsLine.equalsIgnoreCase("exit"))
                break;
            List<String> ingredients = Arrays.stream(ingredientsLine.split(","))
                    .map(String::trim)
                    .toList();

            // Allergens
            System.out.print("Enter any allergens to avoid (comma separated, or leave empty): ");
            String allergensLine = scanner.nextLine();
            if (allergensLine.equalsIgnoreCase("exit"))
                break;
            List<String> allergens = Arrays.stream(allergensLine.split(","))
                    .map(String::trim)
                    .toList();

            StringBuilder questionBuilder = new StringBuilder();
            questionBuilder.append("I have these ingredients: ")
                    .append(String.join(", ", ingredients))
                    .append(". ");
            if (!allergens.isEmpty() && !allergens.get(0).isEmpty()) {
                questionBuilder.append("Avoid these allergens: ")
                        .append(String.join(", ", allergens))
                        .append(". ");
            }

            String question = questionBuilder.toString();

            System.out.println("\nFetching recipe...\n");
            String response = agent.suggestRecipe(question);

            System.out.println("---------- Recipe Suggestion ----------");
            System.out.println(response);
            System.out.println("---------------------------------------\n");
        }

        System.out.println("Goodbye! Happy cooking!");
    }
}
