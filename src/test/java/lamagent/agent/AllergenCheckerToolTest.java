package lamagent.agent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link AllergenCheckerTool}.
 */
@ExtendWith(MockitoExtension.class)
class AllergenCheckerToolTest {

    @Test
    void testCheckAllergens_whenAllergenDetected() throws Exception {
        String[] userAllergens = {"peanut"};
        String[] ingredients = {"butter"};

        String fakeJson = """
                {"labelName": "Peanut Butter", "confidence": 0.92}
                """;

        InputStream fakeResponse =
                new ByteArrayInputStream(fakeJson.getBytes(StandardCharsets.UTF_8));

        try (MockedStatic<AllergenCheckerTool> mocked = mockStatic(AllergenCheckerTool.class)) {
            mocked.when(() -> AllergenCheckerTool.retrieveIngredientAllergens(any()))
                    .thenReturn(fakeResponse);

            AllergenCheckerTool tool = new AllergenCheckerTool();
            String result = tool.checkAllergens(userAllergens, ingredients);

            assertEquals(
                    "Unsafe recipe: allergens detected: peanut butter (0.92),",
                    result.trim());
        }
    }

    @Test
    void testCheckAllergens_whenNoAllergenDetected() throws Exception {
        String[] userAllergens = {"peanut"};
        String[] ingredients = {"apple"};

        String fakeJson = """
                {"labelName": "Apple", "confidence": 0.85}
                """;

        InputStream fakeResponse =
                new ByteArrayInputStream(fakeJson.getBytes(StandardCharsets.UTF_8));

        try (MockedStatic<AllergenCheckerTool> mocked = mockStatic(AllergenCheckerTool.class)) {
            mocked.when(() -> AllergenCheckerTool.retrieveIngredientAllergens(any()))
                    .thenReturn(fakeResponse);

            AllergenCheckerTool tool = new AllergenCheckerTool();
            String result = tool.checkAllergens(userAllergens, ingredients);
            assertEquals(
                    "Safe recipe: no allergen detected among " + Arrays.toString(userAllergens),
                    result.trim());
        }
    }

    @Test
    void testCheckAllergens_whenIOExceptionOccurs() throws Exception {
        String[] userAllergens = {"milk"};
        String[] ingredients = {"chocolate"};

        try (MockedStatic<AllergenCheckerTool> mocked = mockStatic(AllergenCheckerTool.class)) {
            mocked.when(() -> AllergenCheckerTool.retrieveIngredientAllergens(any()))
                    .thenThrow(new java.io.IOException("API unreachable"));

            AllergenCheckerTool tool = new AllergenCheckerTool();
            String result = tool.checkAllergens(userAllergens, ingredients);
            assertEquals(
                    "Error: unable to check allergens (API unreachable)",
                    result.trim());
        }
    }
    
}
