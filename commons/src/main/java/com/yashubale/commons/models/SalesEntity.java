package com.yashubale.commons.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(
        {"region", "country", "itemType", "salesChannel", "orderPriority",
                "orderDate", "orderId", "shipDate", "unitsSold", "unitPrice",
                "unitCost", "totalRevenue", "totalCost", "totalProfit"}
)
public class SalesEntity implements Serializable {

    @JsonProperty("region")
    private String region;

    @JsonProperty("country")
    private String country;

    @JsonProperty("itemType")
    private String itemType;

    @JsonProperty("salesChannel")
    private String salesChannel;

    @JsonProperty("orderPriority")
    private String orderPriority;

    @JsonProperty("orderDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date orderDate;

    @JsonProperty("orderId")
    private Long orderId;

    @JsonProperty("shipDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date shipDate;

    @JsonProperty("unitsSold")
    private Double unitsSold;

    @JsonProperty("unitPrice")
    private Double unitPrice;

    @JsonProperty("unitCost")
    private Double unitCost;

    @JsonProperty("totalRevenue")
    private Double totalRevenue;

    @JsonProperty("totalCost")
    private Double totalCost;

    @JsonProperty("totalProfit")
    private Double totalProfit;
}
