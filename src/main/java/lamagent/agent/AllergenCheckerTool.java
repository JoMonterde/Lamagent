package lamagent.agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.json.JSONObject;

import dev.langchain4j.agent.tool.Tool;

/**
 * The {@code AllergenCheckerTool} class provides functionality to check whether
 * a list of food ingredients
 * contains potential allergens based on a user’s specified allergens. It
 * integrates with the
 * <a href="https://www.nyckel.com/">Nyckel API</a> to analyze ingredients and
 * identify allergens
 * with a certain confidence threshold.
 * <p>
 * This tool authenticates via OAuth2 using a client ID and secret to obtain an
 * access token,
 * then calls a remote classification function to detect allergens in ingredient
 * descriptions.
 * </p>
 *
 * <p>
 * Authorized allergen confidence threshold:
 * {@link #THRESHOLD_ALLERGEN_CONFIDENCE_AUTHORIZED} = 0.7
 * </p>
 */
public class AllergenCheckerTool {

    private static final String TOKEN_URL = "https://www.nyckel.com/connect/token";

    private static final String FUNCTION_URL = "https://www.nyckel.com/v1/functions/food-allergens/invoke";

    private static final String CLIENT_ID = "wmevwn9kjnv6z41ifm9tl5s5nqiafjg8";

    private static final String CLIENT_SECRET = "52b8w9esxq14y22uio6ic52ru9764z406n1xtsl58hvv4n7knorrcxvzzken28oy";

    /** Authorized allergen confidence threshold. */
    public static final double THRESHOLD_ALLERGEN_CONFIDENCE_AUTHORIZED = 0.7;

    /**
     * Checks whether the given list of ingredients contains any allergens specified
     * by the user.
     * This method sends the ingredient data to the Nyckel API, which returns
     * potential allergens
     * with confidence scores. Any allergens with a confidence score above
     * {@link #THRESHOLD_ALLERGEN_CONFIDENCE_AUTHORIZED} and matching a
     * user-provided allergen name
     * are flagged as detected.
     * <p>
     * The result indicates whether the recipe is considered safe or unsafe
     * based on detected allergens.
     * </p>
     *
     * @param pListUserAllergen a list of allergens that the user wants to avoid
     * @param pListIngredient   a list of ingredients to be analyzed for potential
     *                          allergens
     * @return a message indicating whether allergens were found in the ingredients.
     *         If an error occurs during the API call, the error message is
     *         returned.
     *
     *         <p>
     *         <b>Returns examples:</b>
     *         </p>
     *         <ul>
     *         <li>
     *         {@code "Safe recipe: no allergen detected among [peanut, milk]"}
     *         — if no matching allergens were found.
     *         </li>
     *         <li>
     *         {@code "Unsafe recipe: allergens detected: peanut (0.91), milk (0.85), "}
     *         — if allergens were found.
     *         </li>
     *         <li>
     *         {@code "Error: unable to check allergens (Network error)"} — if the
     *         API call fails.
     *         </li>
     *         </ul>
     *
     * @see #THRESHOLD_ALLERGEN_CONFIDENCE_AUTHORIZED
     */
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
                    System.out.println(label + ": " + confidence);
                    if (confidence > THRESHOLD_ALLERGEN_CONFIDENCE_AUTHORIZED) {
                        for (String allergy : pListUserAllergen) {
                            if (label.contains(allergy.toLowerCase())) {
                                foundAllergens.append(label).append(" (").append(confidence).append("), ");
                            }
                        }
                    }
                }
            }

            if (!foundAllergens.isEmpty()) {
                return "Unsafe recipe: allergens detected: " + foundAllergens;
            }
            return "Safe recipe: no allergen detected among " + pListUserAllergen;

        } catch (IOException e) {
            return "Error: unable to check allergens (" + e.getMessage() + ")";
        }
    }

    protected InputStream retrieveIngredientAllergens(List<String> pListIngredient) throws IOException {
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

    protected static String getAccessToken() throws IOException {
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
            for (String line; (line = br.readLine()) != null;) {
                response.append(line);
            }

            int start = response.indexOf(":\"") + 2;
            int end = response.indexOf("\",", start);
            return response.substring(start, end);
        }
    }
}
