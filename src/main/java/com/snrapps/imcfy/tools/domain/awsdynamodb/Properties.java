
package com.snrapps.imcfy.tools.domain.awsdynamodb;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Properties {

    @SerializedName("AttributeDefinitions")
    private List<AttributeDefinition> attributeDefinitions = null;

    @SerializedName("KeySchema")
    private List<KeySchema> keySchema = null;

    @SerializedName("ProvisionedThroughput")
    private ProvisionedThroughput provisionedThroughput;

    @SerializedName("TableName")
    private String tableName;

    @SerializedName("GlobalSecondaryIndexes")
    private List<GlobalSecondaryIndex> globalSecondaryIndexes = null;

    @SerializedName("LocalSecondaryIndexes")
    private List<LocalSecondaryIndex> localSecondaryIndexes = null;

}
