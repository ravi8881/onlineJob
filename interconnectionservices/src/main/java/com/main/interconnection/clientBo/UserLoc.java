
package com.main.interconnection.clientBo;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "loc_id",
    "lat",
    "longitude"
})
public class UserLoc {

    @JsonProperty("loc_id")
    private String locId;
    @JsonProperty("lat")
    private String lat;
    @JsonProperty("longitude")
    private String longitude;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The locId
     */
    @JsonProperty("loc_id")
    public String getLocId() {
        return locId;
    }

    /**
     * 
     * @param locId
     *     The loc_id
     */
    @JsonProperty("loc_id")
    public void setLocId(String locId) {
        this.locId = locId;
    }

    /**
     * 
     * @return
     *     The lat
     */
    @JsonProperty("lat")
    public String getLat() {
        return lat;
    }

    /**
     * 
     * @param lat
     *     The lat
     */
    @JsonProperty("lat")
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * 
     * @return
     *     The longitude
     */
    @JsonProperty("longitude")
    public String getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude
     *     The longitude
     */
    @JsonProperty("longitude")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
