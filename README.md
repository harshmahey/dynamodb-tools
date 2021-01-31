## aws dynamodb tool to generate serverless file for table creations.
    Tool helps in generating complex serverless yml file that creates tables 
    in aws dynamodb by passing simple json file.

### To build the code
    mvn clean install

This will generate 
### To run the tool from within intellj or such editors
    mvn exec:java -Dexec.mainClass="com.snrapps.imcfy.tools.utils.DynamodbObjGenerator" 
    
### Steps to generate the serverless project file (serverless.yml).
1. Download the jar file from  [here](https://github.com/harshmahey/dynamodb-tools).
2. Create json file which has table details. Here is a sample file.(restaurantSummary.json) 
 
``` 
{"restaurantSummary": 
 {
   "restaurantId": "S",
   "restaurantNumber": "S__LSI",
   "restaurantLogoUrl": "S",
   "creationDate": "N__SK",
   "lastUpdateOn": "N",
   "lastUpdatedBy": "S",
   "restaurantName": "S",
   "foodPickUpAddress": "M",
   "addressLat__long": "S__LSI",
   "chefId": "S__LSI",
   "servingStartDate": "N",
   "recipeCount": "N",
   "totalOrdersProcessed": "N",
   "totalReviewCount": "N",
   "currentReviewStars": "S__GSI",
   "chatId": "S",
   "isAvailableForChatNow": "S__LSI",
   "displayLanguage": "S__PK",
   "activeMenuItemSummary": "S"
  }
}
```
3. Here are the possible values you add in the attribute value.
```
 - S : String
 - N : Number
 - B : Binary
 - S__PK : String with Primary Key
 - N__PK : Number with Primary Key
 - S__SK : String with Sort Key
 - N__SK : Number with Sort Key
 - S__LSI : String with LSI. 
 - S__GSI : String with GSI.
```    
4. Create 1 file per table in dynamodb.
5. Create a wrapper file which has reference to all those json files- ddl-summary.txt (absolute path). example.
```
/opt/ddl-tables/restaurantSummary.json
/opt/ddl-tables/restaurantHotel.json
```
6. Execute the command 
```    
    java -jar dynamodb-tools-1.0.0-SNAPSHOT.jar /opt/ddl-summary.txt
 ```
7. This generates a file - serverless.yml in the root directory
```

service: "ddl-handler"
frameworkVersion: "2"
provider:
  name: "aws"
  runtime: "nodejs12.x"
  stage: "dev"
  region: "us-east-1"
resources:
  Resources:
    restaurantSummary:
      Type: "AWS::DynamoDB::Table"
      Properties:
        AttributeDefinitions:
        - AttributeName: "displayLanguage"
          AttributeType: "S"
        - AttributeName: "creationDate"
          AttributeType: "N"
        - AttributeName: "currentReviewStars"
          AttributeType: "S"
        - AttributeName: "restaurantNumber"
          AttributeType: "S"
        - AttributeName: "addressLat__long"
          AttributeType: "S"
        - AttributeName: "chefId"
          AttributeType: "S"
        - AttributeName: "isAvailableForChatNow"
          AttributeType: "S"
        KeySchema:
        - AttributeName: "displayLanguage"
          KeyType: "HASH"
        - AttributeName: "creationDate"
          KeyType: "RANGE"
        ProvisionedThroughput:
          ReadCapacityUnits: "5"
          WriteCapacityUnits: "5"
        TableName: "restaurantSummary"
        GlobalSecondaryIndexes:
        - IndexName: "GSI_currentReviewStars"
          KeySchema:
          - AttributeName: "currentReviewStars"
            KeyType: "HASH"
          Projection:
            NonKeyAttributes: []
            ProjectionType: "ALL"
          ProvisionedThroughput:
            ReadCapacityUnits: "5"
            WriteCapacityUnits: "5"
        LocalSecondaryIndexes:
        - IndexName: "LSI_restaurantNumber"
          KeySchema:
          - AttributeName: "displayLanguage"
            KeyType: "HASH"
          - AttributeName: "restaurantNumber"
            KeyType: "RANGE"
          Projection:
            NonKeyAttributes: []
            ProjectionType: "ALL"
        - IndexName: "LSI_addressLat__long"
          KeySchema:
          - AttributeName: "displayLanguage"
            KeyType: "HASH"
          - AttributeName: "addressLat__long"
            KeyType: "RANGE"
          Projection:
            NonKeyAttributes: []
            ProjectionType: "ALL"
        - IndexName: "LSI_chefId"
          KeySchema:
          - AttributeName: "displayLanguage"
            KeyType: "HASH"
          - AttributeName: "chefId"
            KeyType: "RANGE"
          Projection:
            NonKeyAttributes: []
            ProjectionType: "ALL"
        - IndexName: "LSI_isAvailableForChatNow"
          KeySchema:
          - AttributeName: "displayLanguage"
            KeyType: "HASH"
          - AttributeName: "isAvailableForChatNow"
            KeyType: "RANGE"
          Projection:
            NonKeyAttributes: []
            ProjectionType: "ALL"
```

### Please feel free to contact me [harshmahey@gmail.com]()  for any questions/concerns.
