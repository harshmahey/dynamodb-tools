package com.snrapps.imcfy.tools;


        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.util.List;

        import com.snrapps.imcfy.tools.utils.DynamodbObjGenerator;
        import com.snrapps.imcfy.tools.utils.FileHandler;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.CommandLineRunner;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
@Slf4j
public class DynamoDbTableServerlessGenerator extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    DynamodbObjGenerator dynamodbObjGenerator;

    @Autowired
    FileHandler fileHandler;

    public static void main(String[] args) {
         SpringApplication.run(DynamoDbTableServerlessGenerator.class, args);
    }

    @Override
    public void run(String[] args) throws Exception {
        System.out.println("Application Started !!");


        try {
             String response = "";
          //   log.info("args=>"+args);
          //  log.info("args[1]=>"+args.length);
            if (args!=null && args.length>0 && args[0] != null ) {

               String ddlTablesFileLocation = args[0];
                //log.info(ddlTablesFileLocation);
                List<String> result = Files.readAllLines(Paths.get(ddlTablesFileLocation));

                StringBuffer sbLocal = new StringBuffer();
                for (String eachRowVal : result) {
                    if (eachRowVal!=null && eachRowVal.trim().length()>0) {
                        sbLocal.append(dynamodbObjGenerator.generate(fileHandler.readContent(eachRowVal)));
                        sbLocal.append(",");
                    }
                }
                String finalJsonVal = sbLocal.toString().substring(0, sbLocal.toString().length() - 1);

                sbLocal = new StringBuffer();
                sbLocal.append("{\"Resources\":{");
                sbLocal.append(finalJsonVal);
                sbLocal.append("}}");
              //  log.info("sbLocal.toString() =>"+sbLocal.toString());

                String finalYml = fileHandler.convertJsonToYml(sbLocal.toString());
                log.info("finalYml =>"+finalYml);
                fileHandler.writeToFile(finalYml, "serverless-template.txt");

                // log.info("finalYml =>"+finalYml);
                System.exit(0);
            } else {
                log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.error("There are 2 required input parameters.");
                log.error("args[0]=> File name that has the ddl-tablelist");
                log.error("args[1]=> Destination File Location");
                System.exit(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("process failed :(");
            System.exit(1);
        }


    }



}