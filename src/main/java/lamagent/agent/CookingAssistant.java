package lamagent.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface CookingAssistant {
    @SystemMessage("""
        You are a culinary assistant AI.
        Your main objective is to generate safe recipes that avoid user allergens and match their preferences.
        If you propose a recipe, always call the allergen-checking tool to verify the ingredients.
        If the tool indicates that an allergen is present or unknown, apologize and refuse to provide the recipe.
        If the tool confirms safety, provide the recipe.
    """)
    @UserMessage("""
        The user has the following allergies: {{allergies}}.
        They prefer: {{preferences}}.
        Please suggest one suitable recipe.
    """)
    String suggestRecipe(@V("allergies") List<String> pAllergies, @V("preferences") List<String> pPreferences);
}
