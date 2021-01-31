
package com.snrapps.imcfy.tools.domain.awsdynamodb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Example {

    @SerializedName("myDynamoDBTable")
    @Expose
    private MyDynamoDBTable myDynamoDBTable;

    public MyDynamoDBTable getMyDynamoDBTable() {
        return myDynamoDBTable;
    }

    public void setMyDynamoDBTable(MyDynamoDBTable myDynamoDBTable) {
        this.myDynamoDBTable = myDynamoDBTable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("myDynamoDBTable", myDynamoDBTable).toString();
    }

}
