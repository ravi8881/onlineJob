
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
    "emailContentHtml",
    "websiteContentHtml",
    "emailContent",
    "id",
    "websiteContent",
    "title"
})
public class Says {

    @JsonProperty("emailContentHtml")
    private String emailContentHtml;
    @JsonProperty("websiteContentHtml")
    private String websiteContentHtml;
    @JsonProperty("emailContent")
    private String emailContent;
    @JsonProperty("id")
    private String id;
    @JsonProperty("websiteContent")
    private String websiteContent;
    @JsonProperty("title")
    private String title;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("emailContentHtml")
    public String getEmailContentHtml() {
        return emailContentHtml;
    }

    @JsonProperty("emailContentHtml")
    public void setEmailContentHtml(String emailContentHtml) {
        this.emailContentHtml = emailContentHtml;
    }

    public Says withEmailContentHtml(String emailContentHtml) {
        this.emailContentHtml = emailContentHtml;
        return this;
    }

    @JsonProperty("websiteContentHtml")
    public String getWebsiteContentHtml() {
        return websiteContentHtml;
    }

    @JsonProperty("websiteContentHtml")
    public void setWebsiteContentHtml(String websiteContentHtml) {
        this.websiteContentHtml = websiteContentHtml;
    }

    public Says withWebsiteContentHtml(String websiteContentHtml) {
        this.websiteContentHtml = websiteContentHtml;
        return this;
    }

    @JsonProperty("emailContent")
    public String getEmailContent() {
        return emailContent;
    }

    @JsonProperty("emailContent")
    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public Says withEmailContent(String emailContent) {
        this.emailContent = emailContent;
        return this;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public Says withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("websiteContent")
    public String getWebsiteContent() {
        return websiteContent;
    }

    @JsonProperty("websiteContent")
    public void setWebsiteContent(String websiteContent) {
        this.websiteContent = websiteContent;
    }

    public Says withWebsiteContent(String websiteContent) {
        this.websiteContent = websiteContent;
        return this;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Says withTitle(String title) {
        this.title = title;
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
