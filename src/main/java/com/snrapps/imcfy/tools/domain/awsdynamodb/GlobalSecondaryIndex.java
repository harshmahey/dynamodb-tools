
package com.snrapps.imcfy.tools.domain.awsdynamodb;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalSecondaryIndex {

    @SerializedName("IndexName")
    private String indexName;

    @SerializedName("KeySchema")
    private List<KeySchema> keySchema = null;

    @SerializedName("Projection")
    private Projection projection;

    @SerializedName("ProvisionedThroughput")
    private ProvisionedThroughput provisionedThroughput;


}
