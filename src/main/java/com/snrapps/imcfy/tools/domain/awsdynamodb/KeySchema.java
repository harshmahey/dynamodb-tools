
package com.snrapps.imcfy.tools.domain.awsdynamodb;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeySchema {

    @SerializedName("AttributeName")
    private String attributeName;

    @SerializedName("KeyType")
    private String keyType;



}
