package net.wehrens.intellij.plugins.propertysorter;

import java.util.Properties;
import java.util.List;

public class StringPropertyConverter {

    public static final String ideaLineSeperator = "\n";

    public Properties convertString(String stringToBeConverted) throws ConvertException{


        String[] lines = stringToBeConverted.split("\n");

        Properties result = new Properties();

        for (String line : lines) {

            if (lineContainsProperty(line)) {
                String[] keyValuePair = line.split("=");
                String key = extractKey(keyValuePair);
                String value = extractValue(keyValuePair);
                result.put(key, value);
            } else if (!lineIsAComment(line) && !lineIsEmpty(line)) {
                throw new ConvertException("This is does not look like a properties text.");
            }
        }

        return result;
    }

    private boolean lineContainsProperty(String line) {
        return line.contains("=");
    }

    private String extractValue(String[] keyValuePair) {
        StringBuffer value = new StringBuffer();
        if (keyValuePair.length>=2) {
            value = value.append(keyValuePair[1].trim());
            for (int i=2;i<keyValuePair.length;i++)
            {
                value.append("=").append(keyValuePair[i]);
            }

        }
        return value.toString();
    }

    private String extractKey(String[] keyValuePair) {
        return keyValuePair[0].trim();
    }

    private boolean lineIsEmpty(String line) {
        return line.trim().equals("");
    }

    private boolean lineIsAComment(String line) {
        return line.trim().startsWith("#");
    }

    public String convertSortedProperties(Properties properties, List<String> orderedKeyList) {
        StringBuilder result = new StringBuilder();

        for (String key : orderedKeyList) {
            String value = properties.getProperty(key);
            result.append(key).append("=").append(value).append(ideaLineSeperator);

        }

        return result.toString();
    }


}
