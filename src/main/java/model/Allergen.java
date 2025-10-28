package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Allergen {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String labelName;

    private Double confidence;

    public static List<Allergen> fromJson(String jsonResponse) {
        try {
            return MAPPER.readValue(jsonResponse, new TypeReference<List<Allergen>>() {});
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return List.of();
        }
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String pLabelName) {
        labelName = pLabelName;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double pConfidence) {
        confidence = pConfidence;
    }

    @Override
    public String toString() {
        return "Allergen{" +
                "labelName='" + labelName + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
