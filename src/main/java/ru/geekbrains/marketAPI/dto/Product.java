package ru.geekbrains.marketAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Product {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("title")
    public String title;
    @JsonProperty("price")
    public Integer price;
    @JsonProperty("categoryTitle")
    public String categoryTitle;

}
