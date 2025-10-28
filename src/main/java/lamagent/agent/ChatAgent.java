package lamagent.agent;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

import java.time.Duration;
import java.util.List;

public class ChatAgent {

    private final ChatLanguageModel model;

    public ChatAgent() {
        this.model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3")
                .timeout(Duration.ofSeconds(60))
                .build();
    }

    public String suggestRecipe(List<String> allergies, List<String> preferences) {
        String prompt = """
                You are a culinary assistant.
                Give me a simple recipe respecting the user preferences,
                while avoiding their allergies: %s.
                
                Favorite ingredients: %s.
                """.formatted(String.join(", ", allergies), String.join(", ", preferences));

        return model.generate(prompt);
    }
}
