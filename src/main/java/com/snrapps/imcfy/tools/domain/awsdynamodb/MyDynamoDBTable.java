
package com.snrapps.imcfy.tools.domain.awsdynamodb;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class MyDynamoDBTable {

    @SerializedName("Type")
    private String type="AWS::DynamoDB::Table";

    @SerializedName("Properties")
    private Properties properties;


}
