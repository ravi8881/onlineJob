
package com.main.interconnection.deals.grouponLat;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "currencyCode",
    "formattedAmount",
    "amount"
})
public class Value {

    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("formattedAmount")
    private String formattedAmount;
    @JsonProperty("amount")
    private int amount;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Value withCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    @JsonProperty("formattedAmount")
    public String getFormattedAmount() {
        return formattedAmount;
    }

    @JsonProperty("formattedAmount")
    public void setFormattedAmount(String formattedAmount) {
        this.formattedAmount = formattedAmount;
    }

    public Value withFormattedAmount(String formattedAmount) {
        this.formattedAmount = formattedAmount;
        return this;
    }

    @JsonProperty("amount")
    public int getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Value withAmount(int amount) {
        this.amount = amount;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
