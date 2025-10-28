package agent;

import model.Allergen;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AllergenChecker {

    private static final String TOKEN_URL = "https://www.nyckel.com/connect/token";
    private static final String FUNCTION_URL = "https://www.nyckel.com/v1/functions/food-allergens/invoke";
    private static final String CLIENT_ID = "wmevwn9kjnv6z41ifm9tl5s5nqiafjg8";
    private static final String CLIENT_SECRET = "52b8w9esxq14y22uio6ic52ru9764z406n1xtsl58hvv4n7knorrcxvzzken28oy";

    private static String getAccessToken() throws IOException {
        URL url = new URL(TOKEN_URL);
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

            String json = response.toString();
            int start = json.indexOf(":\"") + 2;
            int end = json.indexOf("\",", start);
            return json.substring(start, end);
        }
    }

    public static boolean checkAllergens(List<String> pUserAllergens, List<String> pIngredients) throws Exception {
        URL url = new URL(FUNCTION_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonAllergens = "{\"data\": \"" + String.join(",", pIngredients) + "\"}";

        List<Allergen> allergens = Allergen.fromJson(jsonAllergens);

        for (Allergen allergen : allergens) {
            if (pUserAllergens.contains(allergen.getLabelName())) {
                return false;
            }
        }
        return true;
    }
}
