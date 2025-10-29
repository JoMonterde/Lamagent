package lamagent.agent;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import java.time.Duration;
import java.util.List;

/**
 * The {@code OllamaCookingAgent} class provides an AI-powered cooking assistant that suggests
 * safe and personalized recipes based on user allergies and culinary preferences.
 * <p>
 * This agent uses the {@link OllamaChatModel} as its underlying chat language model
 * and integrates with {@link AllergenCheckerTool} to ensure that suggested recipes do not
 * contain allergens specified by the user.
 * </p>
 *
 * <p>
 * Configuration constants:
 * <ul>
 *     <li>{@link #MODEL_NAME} — Name of the LLM model used ("llama3.1").</li>
 *     <li>{@link #BASE_URL} — Base URL for the Ollama API server ("http://localhost:11434").</li>
 * </ul>
 * </p>
 *
 * @see OllamaChatModel
 * @see AllergenCheckerTool
 * @see CookingAssistant
 * @see AiServices
 */
public class OllamaCookingAgent {

    public static final String MODEL_NAME = "llama3.1";

    public static final String BASE_URL = "http://localhost:11434";

    private final CookingAssistant assistant;

    /**
     * Constructs a new {@code OllamaCookingAgent} and initializes the underlying
     * {@link ChatLanguageModel} and {@link AllergenCheckerTool}.
     */
    public OllamaCookingAgent() {
        ChatLanguageModel model = OllamaChatModel.builder()
                .baseUrl(BASE_URL)
                .modelName(MODEL_NAME)
                .timeout(Duration.ofSeconds(60))
                .build();

        AllergenCheckerTool checker = new AllergenCheckerTool();

        assistant = AiServices.builder(CookingAssistant.class)
                .chatLanguageModel(model)
                .tools(checker)
                .build();
    }

    /**
     * Suggests a safe and personalized recipe for the user based on their allergies and
     * preferences.
     * <p>
     * Before providing a recipe, the method ensures that all ingredients are safe with
     * respect to the user-specified allergens. If no recipe can be found that satisfies
     * the allergy constraints, the method may return a message indicating that a suitable
     * recipe could not be provided.
     * </p>
     *
     * @param pAllergies  the list of user allergens to avoid
     * @param pPreferences the list of user culinary preferences
     * @return a recipe suggestion as a string, or a message indicating that no safe recipe
     *         could be found
     */
    public String suggestRecipe(List<String> pAllergies, List<String> pPreferences) {
        return assistant.suggestRecipe(pAllergies, pPreferences);
    }
}
