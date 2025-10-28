package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class OllamaResponse {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String model;

    private String created_at;

    private String response;

    /**
     * Creates a new {@link OllamaResponse} instance from a JSON string.
     * @param pJson the JSON string to parse
     * @return a {@link OllamaResponse} object parsed from the given JSON, or a fallback OllamaResponse
     *          if deserialization fails
     */
    public static OllamaResponse fromJson(String pJson) {

        try {
            return MAPPER.readValue(pJson, OllamaResponse.class);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
            return new OllamaResponse();
        }
    }

    public String getModel() {
        return model;
    }

    public void setModel(String pModel) {
        model = pModel;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String pCreated_at) {
        created_at = pCreated_at;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String pResponse) {
        response = pResponse;
    }

    @Override
    public String toString() {
        return "OllamaResponse{" +
                "model='" + model + '\'' +
                ", created_at='" + created_at + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
