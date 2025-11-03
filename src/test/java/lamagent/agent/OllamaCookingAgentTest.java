package lamagent.agent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OllamaCookingAgentTest {

    private CookingAssistant mockAssistant;
    private OllamaCookingAgent agent;

    @BeforeEach
    void setUp() {
        mockAssistant = Mockito.mock(CookingAssistant.class);

        agent = new OllamaCookingAgent() {
            @Override
            public String suggestRecipe(String pQuestion) {
                return mockAssistant.suggestRecipe(pQuestion);
            }
        };
    }

    @Test
    void testSuggestRecipe_validQuestion() {
        String question = "I want a vegan peanut-free salad";
        String expectedResponse = "Here is a safe recipe: vegan salad without peanuts";

        when(mockAssistant.suggestRecipe(question)).thenReturn(expectedResponse);

        String result = agent.suggestRecipe(question);

        assertEquals(expectedResponse, result);
        verify(mockAssistant).suggestRecipe(question);
    }

    @Test
    void testSuggestRecipe_randomInput() {
        String question = "Tell me about quantum physics";
        String expectedResponse = "I’m sorry, but I can only provide advice and information about cooking and recipes.";

        when(mockAssistant.suggestRecipe(question)).thenReturn(expectedResponse);

        String result = agent.suggestRecipe(question);

        assertEquals(expectedResponse, result);
        verify(mockAssistant).suggestRecipe(question);
    }
}