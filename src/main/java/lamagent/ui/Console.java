package lamagent.ui;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import lamagent.agent.OllamaCookingAgent;

/**
 * The {@code Console} class provides a simple command-line interface (CLI) for interacting
 * with the {@link OllamaCookingAgent}.
 * <p>
 * Users can input their food allergens and culinary preferences, and the agent will
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
     * {@link OllamaCookingAgent}, and prints the recipe suggestions or relevant messages.
     * The loop continues until the user types {@code exit}.
     * </p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        OllamaCookingAgent agent = new OllamaCookingAgent();
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.println("Welcome to your intelligent cooking assistant!");
        System.out.println("Type 'exit' to leave.\n");

        while (true) {
            System.out.print("Question: ");
            String question = scanner.nextLine();
            if (question.equalsIgnoreCase("exit")) {
                break;
            }
            System.out.println("Search");

            String response = agent.suggestRecipe(question);
            System.out.println("\nResponse :\n" + response);
            System.out.println("\n---------------------------------\n");
        }

        System.out.println("See you!");
    }
}
