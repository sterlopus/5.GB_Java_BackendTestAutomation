package ru.geekbrains.marketAPI.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Category {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("products")
    public List<Product> products = null;
    @JsonProperty("title")
    public String title;

}