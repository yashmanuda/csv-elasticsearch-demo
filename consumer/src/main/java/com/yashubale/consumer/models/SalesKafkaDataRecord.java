package com.yashubale.consumer.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@Valid
@NoArgsConstructor
@AllArgsConstructor
public class SalesKafkaDataRecord {

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
    private Date orderDate;

    @JsonProperty("orderId")
    private Long orderId;

    @JsonProperty("shipDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
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
