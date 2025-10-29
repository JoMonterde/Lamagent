package lamagent.agent;

import dev.langchain4j.agent.tool.Tool;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AllergenCheckerTool {

    private static final String TOKEN_URL = "https://www.nyckel.com/connect/token";

    private static final String FUNCTION_URL = "https://www.nyckel.com/v1/functions/food-allergens/invoke";

    private static final String CLIENT_ID = "wmevwn9kjnv6z41ifm9tl5s5nqiafjg8";

    private static final String CLIENT_SECRET = "52b8w9esxq14y22uio6ic52ru9764z406n1xtsl58hvv4n7knorrcxvzzken28oy";

    /** Authorized allergen confidence rate */
    public static final double RATE_ALLERGEN_CONFIDENCE_AUTHORIZED = 0.7;

    @Tool("Check if the ingredient list contains any allergens from those provided by the user, calling an external API.")
    public String checkAllergens(List<String> pListUserAllergen, List<String> pListIngredient) {
        try {
            InputStream ingredientAllergens = retrieveIngredientAllergens(pListIngredient);

            StringBuilder foundAllergens = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(ingredientAllergens))) {
                String line;
                while ((line = br.readLine()) != null) {
                    JSONObject obj = new JSONObject(line);
                    String label = obj.getString("labelName").toLowerCase();
                    double confidence = obj.getDouble("confidence");

                    if (confidence > RATE_ALLERGEN_CONFIDENCE_AUTHORIZED) {
                        for (String allergy : pListUserAllergen) {
                            if (label.contains(allergy.toLowerCase())) {
                                foundAllergens.append(label).append(" (").append(confidence).append("), ");
                            }
                        }
                    }
                }
            }

            if (foundAllergens.isEmpty()) {
                return "Unsafe recipe: allergens detected: " + foundAllergens;
            }
            return "Safe recipe: no allergen detected among " + pListUserAllergen;

        } catch (IOException e) {
            return "Error: unable to check allergens (" + e.getMessage() + ")";
        }
    }


    private static InputStream retrieveIngredientAllergens(List<String> pListIngredient) throws IOException {
        String ingredientsText = String.join(", ", pListIngredient);

        URL url = URI.create(FUNCTION_URL).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = "{\"data\": \"" + ingredientsText + "\"}";
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
        }

        return conn.getInputStream();
    }

    private static String getAccessToken() throws IOException {
        URL url = URI.create(TOKEN_URL).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String params = "grant_type=client_credentials&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET;
        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes(StandardCharsets.UTF_8));
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) {
                response.append(line);
            }

            int start = response.indexOf(":\"") + 2;
            int end = response.indexOf("\",", start);
            return response.substring(start, end);
        }
    }
}
