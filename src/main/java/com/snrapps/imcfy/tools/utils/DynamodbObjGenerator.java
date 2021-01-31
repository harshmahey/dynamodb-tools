package com.snrapps.imcfy.tools.utils;

import com.google.gson.Gson;
import com.snrapps.imcfy.tools.domain.awsdynamodb.*;
import com.snrapps.imcfy.tools.domain.awsdynamodb.Properties;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DynamodbObjGenerator {

    @Autowired
    FileHandler fileHandler;



    public String generate(String jsonObjAsString) throws Exception{
        String fullTableTag="";
        Gson gson = new Gson();
        Map map = gson.fromJson(jsonObjAsString, Map.class);
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        int counter=0;
        MyDynamoDBTable myDynamoDBTable =new MyDynamoDBTable();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String tableName="";
            //TBD: Clean up
            if (counter == 0 ) {
                 Map childDataMap = (Map) entry.getValue();
                tableName=entry.getKey();

                Map<String, List> realDataMap= getMyDynamoDBTableData(childDataMap);
                List<AttributeDefinition> attributeDefinitionList = getAttributeDefinitionList(realDataMap);

                List<KeySchema> keySchemaList = new ArrayList<KeySchema>();

                KeySchema keySchemaForHash = getKeySchemaForHash(realDataMap);
                keySchemaList.add(keySchemaForHash);

                KeySchema keySchemaForRange = getKeySchemaForRange(realDataMap);
                keySchemaList.add(keySchemaForRange);

                ProvisionedThroughput provisionedThroughput = getProvisionThroughput();

                List<GlobalSecondaryIndex> globalSecondaryIndices = getGlobalSecondaryIndexes(realDataMap);

                List<LocalSecondaryIndex> localSecondaryIndices = getLocalSecondaryIndexes(realDataMap);


                Properties properties = new Properties();
                properties.setProvisionedThroughput(provisionedThroughput);
                properties.setKeySchema(keySchemaList);
                properties.setAttributeDefinitions(attributeDefinitionList);
                properties.setTableName(tableName);
                properties.setGlobalSecondaryIndexes(globalSecondaryIndices);
                properties.setLocalSecondaryIndexes(localSecondaryIndices);

                myDynamoDBTable.setProperties(properties);
                myDynamoDBTable.setType("AWS::DynamoDB::Table");
                fullTableTag=appendTableNameToJson(myDynamoDBTable);


            }
            counter++;

        }
        return fullTableTag;
    }

    private String appendTableNameToJson(MyDynamoDBTable myDynamoDBTable) {
        Gson gson = new Gson();
        StringBuffer sb = new StringBuffer();
        sb.append("\"");
        sb.append(myDynamoDBTable.getProperties().getTableName());
        sb.append("\": ");
        sb.append(gson.toJson(myDynamoDBTable));
        return sb.toString();
    }


    private List<LocalSecondaryIndex> getLocalSecondaryIndexes(Map childDataMap ) throws Exception {

        List<LocalSecondaryIndex> localSecondaryIndexList = new ArrayList<LocalSecondaryIndex>();
        Iterator<Map.Entry<String, List>> innerIterator = childDataMap.entrySet().iterator();
        while (innerIterator.hasNext()) {
            Map.Entry<String, List> innerEntry = innerIterator.next();

            String key= innerEntry.getKey();
            //log.info("key=>"+key);
            if (key.contains("__LSI")) {
                String typeOfKey= geTypeFromKey(key);
                //log.info("typeOfKey=>"+typeOfKey);

                List<String> attribList= (List) innerEntry.getValue();
                for (String attrib: attribList) {
                    LocalSecondaryIndex localSecondaryIndex = new LocalSecondaryIndex();

                    localSecondaryIndex.setIndexName("LSI_"+attrib);
                    //log.info("attrib=>"+attrib);


                    //KEY SCHEMA
                    List<KeySchema> keySchemaList = new ArrayList<KeySchema>();

                    KeySchema keySchemaForHash = getKeySchemaForHash(childDataMap);


                    KeySchema keySchemaForRange = new KeySchema();
                    keySchemaForRange.setKeyType("RANGE");
                    keySchemaForRange.setAttributeName(attrib);

                    keySchemaList.add(keySchemaForHash);
                    keySchemaList.add(keySchemaForRange);
                    localSecondaryIndex.setKeySchema(keySchemaList);

                    Projection projection = new Projection();
                    projection.setProjectionType("ALL");
                   // projection.setNonKeyAttributes();
                    localSecondaryIndex.setProjection(projection);

                    localSecondaryIndexList.add(localSecondaryIndex);
                }
            }

        }

        return localSecondaryIndexList;
    }



    private List<GlobalSecondaryIndex> getGlobalSecondaryIndexes(Map childDataMap ) throws Exception {

        List<GlobalSecondaryIndex> globalSecondaryIndexList = new ArrayList<GlobalSecondaryIndex>();
        Iterator<Map.Entry<String, List>> innerIterator = childDataMap.entrySet().iterator();
        while (innerIterator.hasNext()) {
            Map.Entry<String, List> innerEntry = innerIterator.next();

            String key= innerEntry.getKey();
            //log.info("key=>"+key);
            if (key.contains("__GSI")) {
                String typeOfKey= geTypeFromKey(key);
                //log.info("typeOfKey=>"+typeOfKey);

                List<String> attribList= (List) innerEntry.getValue();
                for (String attrib: attribList) {
                    GlobalSecondaryIndex globalSecondaryIndex = new GlobalSecondaryIndex();

                    globalSecondaryIndex.setIndexName("GSI_"+attrib);
                    //log.info("attrib=>"+attrib);
                    globalSecondaryIndex.setProvisionedThroughput(getProvisionThroughput());

                    //KEY SCHEMA
                    KeySchema keySchema = new KeySchema();
                    keySchema.setKeyType("HASH");
                    keySchema.setAttributeName(attrib);
                    List<KeySchema> keySchemaList = new ArrayList<KeySchema>();
                    keySchemaList.add(keySchema);
                    globalSecondaryIndex.setKeySchema(keySchemaList);

                    Projection projection = new Projection();
                    projection.setProjectionType("ALL");
                    globalSecondaryIndex.setProjection(projection);

                    globalSecondaryIndexList.add(globalSecondaryIndex);
                }
            }

        }

        return globalSecondaryIndexList;
    }
    private ProvisionedThroughput getProvisionThroughput() {
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput();
        provisionedThroughput.setReadCapacityUnits("5");
        provisionedThroughput.setWriteCapacityUnits("5");
        return provisionedThroughput;
    }

    private KeySchema getKeySchemaForRange(Map childDataMap ) throws Exception {

        Iterator<Map.Entry<String, List>> innerIterator = childDataMap.entrySet().iterator();
        KeySchema keySchema = new KeySchema();
        while (innerIterator.hasNext()) {
            Map.Entry<String, List> innerEntry = innerIterator.next();

            String key= innerEntry.getKey();
            if (key.contains("__SK")) {
                List<String> attribList= (List) innerEntry.getValue();
                for (String attrib: attribList) {
                    keySchema.setKeyType("RANGE");
                    keySchema.setAttributeName(attrib);

                }
            }




        }

        return keySchema;

    }


    private KeySchema getKeySchemaForHash(Map childDataMap ) throws Exception {

        Iterator<Map.Entry<String, List>> innerIterator = childDataMap.entrySet().iterator();
        KeySchema keySchema = new KeySchema();
        while (innerIterator.hasNext()) {
            Map.Entry<String, List> innerEntry = innerIterator.next();

            String key= innerEntry.getKey();


            if (key.contains("__PK")) {
                List<String> attribList= (List) innerEntry.getValue();
                for (String attrib: attribList) {
                    keySchema.setKeyType("HASH");
                    keySchema.setAttributeName(attrib);

                }
            }


        }

        return keySchema;

    }

    private List<AttributeDefinition> getAttributeDefinitionList(Map childDataMap) {
        List<AttributeDefinition> attributeDefinitionList = new ArrayList<AttributeDefinition>();
        Iterator<Map.Entry<String, List>> innerIterator = childDataMap.entrySet().iterator();
        while (innerIterator.hasNext()) {
            Map.Entry<String, List> innerEntry = innerIterator.next();


            String key= innerEntry.getKey();
            String typeOfKey= geTypeFromKey(key);
            if (key.contains("__")) {
                List<String> attribList = (List) innerEntry.getValue();
                for (String attrib : attribList) {
                    AttributeDefinition attributeDefinition = new AttributeDefinition();
                    attributeDefinition.setAttributeName(attrib);
                    attributeDefinition.setAttributeType(typeOfKey);
                    attributeDefinitionList.add(attributeDefinition);
                }
            }
        }

        return attributeDefinitionList;
    }

    private String geTypeFromKey(String key) {
        String typeFromKey="";
        if (key.contains("__")) {
            typeFromKey = key.substring(0, key.indexOf("__"));
        }else {
            typeFromKey=key;
        }
       // log.info(typeFromKey);
        return typeFromKey;
    }


    private  Map<String, List> getMyDynamoDBTableData(Map childDataMap) {
        MyDynamoDBTable myDynamoDBTable;
        Iterator<Map.Entry<String, Object>> innerIterator = childDataMap.entrySet().iterator();
       // log.info("innerIterator....."+innerIterator.toString());
        myDynamoDBTable = new MyDynamoDBTable();
        myDynamoDBTable.setType("AWS::DynamoDB::Table");

        Map<String, List> uniqueValMap = new HashMap<String, List>();

        while (innerIterator.hasNext()) {
            Map.Entry<String, Object> innerEntry = innerIterator.next();
            List<String> allKeys = uniqueValMap.get(innerEntry.getValue());

                    if (innerEntry.getValue() instanceof String ) {
                      // log.info(innerEntry.getKey() + ":" + innerEntry.getValue());
                        if (allKeys==null ) {
                            allKeys = new ArrayList<String>();
                        }
                        allKeys.add(innerEntry.getKey());
                        uniqueValMap.put(innerEntry.getValue().toString(),allKeys);
                     //   log.info("allKeys=>"+allKeys);
                    } else {
                        log.info("!!!!!!!!!!!!!");
                        log.info("IGNORING FIELDS");
                        log.info(innerEntry.getKey() + ":" + innerEntry.getValue().toString());
                        log.info("Type=>"+innerEntry.getValue().getClass());

                    }
        }
       // log.debug("\n\n\n\n"+uniqueValMap);
        return uniqueValMap;
    }


}
