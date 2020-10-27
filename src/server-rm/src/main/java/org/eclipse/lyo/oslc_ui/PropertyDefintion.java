
package org.eclipse.lyo.oslc_ui;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data",
    "representationType"
})
public class PropertyDefintion {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("data")
    private Object data;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("representationType")
    private RepresentationType representationType;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("data")
    public Object getData() {
        return data;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("data")
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("representationType")
    public RepresentationType getRepresentationType() {
        return representationType;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("representationType")
    public void setRepresentationType(RepresentationType representationType) {
        this.representationType = representationType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PropertyDefintion.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("data");
        sb.append('=');
        sb.append(((this.data == null)?"<null>":this.data));
        sb.append(',');
        sb.append("representationType");
        sb.append('=');
        sb.append(((this.representationType == null)?"<null>":this.representationType));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.data == null)? 0 :this.data.hashCode()));
        result = ((result* 31)+((this.representationType == null)? 0 :this.representationType.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PropertyDefintion) == false) {
            return false;
        }
        PropertyDefintion rhs = ((PropertyDefintion) other);
        return (((this.data == rhs.data)||((this.data!= null)&&this.data.equals(rhs.data)))&&((this.representationType == rhs.representationType)||((this.representationType!= null)&&this.representationType.equals(rhs.representationType))));
    }

    public enum RepresentationType {

        TEXT("Text"),
        LINK("Link");
        private final String value;
        private final static Map<String, RepresentationType> CONSTANTS = new HashMap<String, RepresentationType>();

        static {
            for (RepresentationType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private RepresentationType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static RepresentationType fromValue(String value) {
            RepresentationType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
