package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String title;

    private List<String> ingredients = new ArrayList<>();

    private List<String> steps = new ArrayList<>();

    private int timeMinutes;

    private String notes;

    /**
     * Creates a new {@link Recipe} instance from a JSON string.
     * @param pJson the JSON string to parse
     * @return a {@link Recipe} object parsed from the given JSON, or a fallback recipe
     *          if deserialization fails
     */
    public static Recipe fromJson(String pJson) {

        try {
            return MAPPER.readValue(pJson, Recipe.class);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
            Recipe fallback = new Recipe();
            fallback.setTitle("Parsing error");
            return fallback;
        }
    }

//    public String toJson() {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
//        } catch (JsonProcessingException e) {
//            return "{}";
//        }
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> pIngredients) {
        ingredients = pIngredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> pSteps) {
        steps = pSteps;
    }

    public int getTimeMinutes() {
        return timeMinutes;
    }

    public void setTimeMinutes(int pTimeMinutes) {
        timeMinutes = pTimeMinutes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String pNotes) {
        notes = pNotes;
    }

    @Override
    public String toString() {
        return String.format("Recipe: %s (%d min)%nIngredients: %s%nSteps : %s%nNotes: %s",
                title, timeMinutes, ingredients, steps, notes);
    }
}
