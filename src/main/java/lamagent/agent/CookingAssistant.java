package lamagent.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import java.util.List;

/**
 * The {@code CookingAssistant} interface defines a conversational AI service designed to
 * generate safe and personalized recipe suggestions for users based on their allergies
 * and culinary preferences.
 * <p>
 * This assistant uses natural language interaction to understand user inputs, combine
 * allergy information with taste preferences, and generate suitable recipes accordingly.
 * </p>
 *
 * <p>
 * Before suggesting any recipe, the assistant must invoke the allergen-checking tool
 * to ensure that the proposed ingredients do not contain any user-specified allergens.
 * If allergens are detected or if the safety of the ingredients cannot be confirmed,
 * the assistant must apologize and refuse to provide the recipe.
 * </p>
 *
 * <p>
 * Example interaction:
 * </p>
 * <pre>{@code
 * CookingAssistant assistant = ...; // Implementation provided by LangChain4j
 * List<String> allergies = List.of("peanuts", "milk");
 * List<String> preferences = List.of("vegan", "high protein");
 * String recipe = assistant.suggestRecipe(allergies, preferences);
 * System.out.println(recipe);
 * }</pre>
 *
 * <p>
 * This interface is designed to be used with LangChain4j’s {@code @SystemMessage} and
 * {@code @UserMessage} annotations, which define the system’s behavioral context and
 * user input template, respectively.
 * </p>
 *
 * @see dev.langchain4j.service.SystemMessage
 * @see dev.langchain4j.service.UserMessage
 * @see lamagent.agent.AllergenCheckerTool
 */
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
