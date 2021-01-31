package com.snrapps.imcfy.tools.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Component
public class FileHandler {

    public void writeToFile(String finalYml, String fileName) throws Exception {
        InputStream inputStream = FileHandler.class.getClassLoader().getResourceAsStream("serverless-template.txt");
        String templateData=readFromInputStream(inputStream);
        BufferedWriter writer = new BufferedWriter(new FileWriter("serverless.yml"));
        StringBuffer sb = new StringBuffer();
        //sb.append(templateData);

        sb.append(finalYml);
        writer.write(sb.toString());

        writer.close();
    }


    public String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }


    public String readContent(String filePath) throws Exception {
        String content = new String();
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        }
        catch (IOException e)
        {
            throw new Exception(e);
        }

        return content;
    }


    public String convertJsonToYml(String jsonStrings ) throws Exception {

        InputStream inputStream = FileHandler.class.getClassLoader().getResourceAsStream("serverless-template.txt");
        String templateData=readFromInputStream(inputStream);
        String finalString = templateData.replace("__APPEND_GEN_DATA__",jsonStrings);

       // log.info("jsonStrings====>"+finalString);
        JsonNode jsonNodeTree = new ObjectMapper().readTree(finalString);
        // save it as YAML
        String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);
       // log.info(jsonAsYaml);
        return jsonAsYaml.replaceFirst("---","");

    }

}
