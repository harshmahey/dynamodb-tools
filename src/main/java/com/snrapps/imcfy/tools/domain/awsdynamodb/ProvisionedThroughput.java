
package com.snrapps.imcfy.tools.domain.awsdynamodb;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProvisionedThroughput {

    @SerializedName("ReadCapacityUnits")
    private String readCapacityUnits;

    @SerializedName("WriteCapacityUnits")
    private String writeCapacityUnits;

}
