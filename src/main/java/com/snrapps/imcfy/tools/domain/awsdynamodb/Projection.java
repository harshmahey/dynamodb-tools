
package com.snrapps.imcfy.tools.domain.awsdynamodb;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Projection {

    @SerializedName("NonKeyAttributes")
    private List<String> nonKeyAttributes = new ArrayList<String>();

    @SerializedName("ProjectionType")
    private String projectionType;


}
