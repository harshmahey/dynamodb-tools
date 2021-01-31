
package com.snrapps.imcfy.tools.domain.awsdynamodb;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeDefinition {

    @SerializedName("AttributeName")
    private String attributeName;

    @SerializedName("AttributeType")
    private String attributeType;


}
