package lamagent.agent;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;

import java.time.Duration;
import java.util.List;

public class OllamaCookingAgent {

    public static final String MODEL_NAME = "llama3.1";

    public static final String BASE_URL = "http://localhost:11434";

    private final CookingAssistant assistant;

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

    public String suggestRecipe(List<String> pAllergies, List<String> pPreferences) {
        return assistant.suggestRecipe(pAllergies, pPreferences);
    }
}
