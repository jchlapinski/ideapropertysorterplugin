package net.wehrens.intellij.plugins.propertysorter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.testng.Assert;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = "convertTest")
public class StringPropertyConverterTest
{

    private StringPropertyConverter stringPropertyConverter = new StringPropertyConverter();

    @Test
    public void testConvertString()
    {

        String convertString = "A=1" + StringPropertyConverter.ideaLineSeperator
                + "C=1" + StringPropertyConverter.ideaLineSeperator + "D=2";

        Properties properties = stringPropertyConverter.convertString(convertString);
        assertTrue(properties.containsKey("A"));
        assertTrue(properties.containsKey("C"));
        assertTrue(properties.containsKey("D"));
    }

    @Test
    public void testConvertProperties()
    {
        Properties properties = new Properties();
        properties.put("B", "2");
        properties.put("A", "1");

        List<String> sortedKeys = new ArrayList<String>();
        sortedKeys.add("A");
        sortedKeys.add("B");

        String convertedString =
                stringPropertyConverter.convertSortedProperties(properties, sortedKeys);

        Assert.assertEquals(convertedString, "A=1" + StringPropertyConverter.ideaLineSeperator + "B=2" + StringPropertyConverter.ideaLineSeperator);
    }

    @Test
    public void testSortStringMultipleEquals()
    {
        String convertString = "A=1=1" +
                StringPropertyConverter.ideaLineSeperator + "C=1=2" +
                StringPropertyConverter.ideaLineSeperator + "B=1=2";
        Properties properties = stringPropertyConverter.convertString(convertString);
        Assert.assertTrue(properties.get("B").equals("1=2"));
    }

    @Test(expectedExceptions = ConvertException.class)
    public void testNonPropertiesContent()
    {
        String convertString = "A=1" + StringPropertyConverter.ideaLineSeperator +
                "This is not a property in a line.";
        stringPropertyConverter.convertString(convertString);
    }

    @Test
    public void testConvertWithComments()
    {
        String convertString = "A=1" + StringPropertyConverter.ideaLineSeperator +
                "# Comment" + StringPropertyConverter.ideaLineSeperator + "C=1";
        Properties properties = stringPropertyConverter.convertString(convertString);
        Assert.assertTrue(properties.get("A").equals("1"));
        Assert.assertEquals(properties.size(), 2);

    }

    @Test
    public void testConvertAPropertyWitJustAKey()
    {
        String convertString = "A=";
        Properties properties = stringPropertyConverter.convertString(convertString);
        Assert.assertTrue(properties.get("A").equals(""));
    }

    @Test(dataProvider = "convertTestDataProvider")
    public void testConvert(String property, String result)
    {
        Properties properties = stringPropertyConverter.convertString(property);
        Assert.assertTrue(properties.get("A").equals(result));
    }

    @DataProvider(name = "convertTestDataProvider")
    public Object[][] convertTestDataProvider()
    {
        return new Object[][]{
                {"A=", ""},
                {"A=1", "1"},
                {"A=2=3", "2=3"},
                {"A=2" + StringPropertyConverter.ideaLineSeperator +
                        "# Comment" + StringPropertyConverter.ideaLineSeperator + "C=1", "2"},
        };

    }

}
