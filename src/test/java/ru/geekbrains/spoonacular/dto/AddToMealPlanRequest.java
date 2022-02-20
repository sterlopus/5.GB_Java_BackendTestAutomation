package ru.geekbrains.spoonacular.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "slot",
        "position",
        "type",
        "value"
})
@Data
public class AddToMealPlanRequest {
    @JsonProperty("date")
    private Integer date;
    @JsonProperty("slot")
    private Integer slot;
    @JsonProperty("position")
    private Integer position;
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private Value value;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({"name"})
    @Data
    public static class Ingredient {
        @JsonProperty("name")
        private String name;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({"ingredients"})
    @Data
    public static class Value {
        @JsonProperty("ingredients")
        private List<Ingredient> ingredients = new ArrayList<>();
    }

}