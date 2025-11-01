package lamagent.agent;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

class AllergenCheckerToolTest {

    @Test
    void testSafeRecipe_noAllergensDetected() {
        AllergenCheckerTool tool = new AllergenCheckerTool() {
            protected InputStream retrieveIngredientAllergens(List<String> ingredients) {
                String mockJson = "{\"labelName\": \"water\", \"confidence\": 0.2}\n";
                return new ByteArrayInputStream(mockJson.getBytes());
            }
        };

        String result = tool.checkAllergens(List.of("peanut", "milk"), List.of("water"));
        assertTrue(result.contains("Safe recipe"));
        assertTrue(result.contains("no allergen detected"));
    }

    @Test
    void testUnsafeRecipe_allergenDetected() {
        AllergenCheckerTool tool = new AllergenCheckerTool() {
            protected InputStream retrieveIngredientAllergens(List<String> ingredients) {
                String mockJson = "{\"labelName\": \"peanut\", \"confidence\": 0.91}\n";
                return new ByteArrayInputStream(mockJson.getBytes());
            }
        };

        String result = tool.checkAllergens(List.of("peanut"), List.of("butter"));
        assertTrue(result.contains("Unsafe recipe"));
        assertTrue(result.contains("peanut"));
    }

}