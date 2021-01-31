## aws dynamodb tool to generate serverless file for table creations.
    Tool helps in generating complex serverless yml file that creates tables 
    in aws dynamodb by passing simple json file.

### To build the code
    mvn clean install

This will generate 
### To run the tool from within intellj or such editors
    mvn exec:java -Dexec.mainClass="com.snrapps.imcfy.tools.utils.DynamodbObjGenerator" 
    
### Steps to generate the serverless project file (serverless.yml).
1. Download the jar file from  [here](https://www.github.com/).
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
    java -jar 
# Hello Markdown in VS Code!

This is a simple introduction to compiling Markdown in VS Code.

Things you'll need:

* [Node.js](https://nodejs.org)
* [markdown-it](https://www.npmjs.com/package/markdown-it)
* [tasks.json](/docs/editor/tasks)

## Section Title

> This block quote is here for your information.
