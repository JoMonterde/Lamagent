package agent;

import api.OllamaClient;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.OllamaResponse;
import model.Recipe;

public record ChatAgent(OllamaClient ollama, String model) {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String ERROR_MESSAGE_RECIPE_NOT_FOUND = "Sorry, Lamagent is not able to provide a correct result, please try again.";

    public String handleRecipeRequest(List<String> pIngredients, List<String> pUserAllergens, String pPreference) {

        Set<String> filtered = pIngredients.stream()
                .filter(ingr -> pUserAllergens.stream()
                        .noneMatch(a -> ingr.toLowerCase().contains(a.toLowerCase())))
                .collect(Collectors.toSet());

        String prompt = buildPrompt(filtered, pUserAllergens, pPreference);

        try {
            String jsonResponse = ollama.generate(model, prompt);

            JsonNode root = MAPPER.readTree(jsonResponse);

            System.out.println(root.get("response").asText());

            Recipe recipe = Recipe.fromJson(root.get("response").asText());

            if (!AllergenChecker.checkAllergens(pUserAllergens, pIngredients)) {
                return ERROR_MESSAGE_RECIPE_NOT_FOUND;
            }

            return recipe.toString();
        } catch (Exception e) {
            return "Erreur interne lors de la génération : " + e.getMessage();
        }
    }

    private String buildPrompt(Set<String> ingredients, List<String> allergies, String preferences) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are a helpful cooking assistant. ");
        sb.append("User allergies: ").append(String.join(", ", allergies)).append(". ");
        sb.append("User preferences: ").append(preferences).append(". ");
        sb.append("Available ingredients (allergy-filtered): ").append(String.join(", ", ingredients)).append(". ");
        sb.append("Propose 1 simple recipe (max 6 ingredients each), each with a short step-by-step and estimated time. ");
        sb.append("Do NOT include any ingredients matching the user's allergies. ");
        sb.append("If you detect missing info, ask clarifying question instead of guessing.");
        sb.append("Request in JSON with attributes: title, ingredients (list), steps (list), time_minutes, notes.");
        sb.append("Give me all the JSON attributes in camelCase.");
        sb.append("Give me only the JSON without any other things.").;
        return sb.toString();
    }
}
