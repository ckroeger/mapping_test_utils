package org.github.testutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.platform.commons.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MappingTestComparisonUtil {

   private MappingTestComparisonUtil() {
   }

   static Logger log = Logger.getLogger(MappingTestComparisonUtil.class.getName());

   public static String getCellValueByColumnName(List<Map<String, String>> testDataList, String columnName) {
      return testDataList.stream()
            .filter(testData -> testData.containsKey(columnName))
            .findFirst()
            .map(testData -> {
               var value = testData.get(columnName);
               return StringUtils.isBlank(value) ? "" : String.valueOf(value);
            })
            .orElseThrow(NoSuchElementException::new);
   }

   public static void assertSpecification(Document xmlDocument, Object sourceObject, String sourceJsonPath, String targetXpath, String constant)
         throws IOException, XPathExpressionException {
      //  Get the source value according to its jsonPath or the defined constant
      String expectedTargetValue = MappingTestComparisonUtil.determineExpectedTargetValue(sourceJsonPath, sourceObject, constant);
      assertSpecification(xmlDocument, targetXpath, expectedTargetValue);
   }

   public static void assertSpecification(Document xmlDocument, String targetXpath, String expectedTargetValue)
         throws XPathExpressionException {
      // Get the target value according to its xPath
      Object targetValue = MappingTestComparisonUtil.evaluateXpathExpression(xmlDocument, targetXpath);
      MappingTestComparisonUtil.checkTargetValueMatchesExpectedValue(xmlDocument, expectedTargetValue, targetXpath, targetValue);
   }

   private static void checkTargetValueMatchesExpectedValue(Document xmlDocument, String expectedTargetValue, String targetXpath, Object targetValue) {
      if (targetXpath.endsWith("/text()")) {
         MappingTestComparisonUtil.checkValueMatchesSpecification(targetValue, expectedTargetValue);
      } else if (targetXpath.matches(".*/@.+")) {
         MappingTestComparisonUtil.checkValueMatchesSpecification(targetValue, expectedTargetValue);
      } else {
         MappingTestComparisonUtil.elementExistsInXmlDocument(xmlDocument, targetXpath);
      }
   }

   private static String determineExpectedTargetValue(String sourceJsonPath, Object sourceObject, String constant) throws JsonProcessingException {
      if (("CONSTANT".equals(sourceJsonPath) || sourceJsonPath == null || sourceJsonPath.isBlank() || "null".equals(sourceJsonPath))
            && (constant != null || !constant.isEmpty())) {
         return constant;
      }
      return evaluateExpressionFromJsonPath(sourceJsonPath, sourceObject);
   }

   private static String evaluateExpressionFromJsonPath(String expression, Object jsonDocument) throws JsonProcessingException {
      String jsonString = new ObjectMapper().writeValueAsString(jsonDocument);
      try {
         Object result = JsonPath.read(jsonString, expression);
         if (result != null) {
            return result.toString();
         }
      } catch (Exception e) {
         log.info(String.format("Error while evaluating JSONPath expression: %s", expression));
      }
      return null;
   }

   public static Object evaluateXpathExpression(Document xmlDocument, String xPathExpression) throws XPathExpressionException {
      XPath xPath = XPathFactory.newInstance().newXPath();
      return xPath.evaluate(xPathExpression, xmlDocument);
   }

   private static void checkValueMatchesSpecification(Object valueFromXPath, String valueFromSource) {
      assertThat(valueFromXPath).isEqualTo(valueFromSource);
   }

   private static void elementExistsInXmlDocument(Object xmlDocument, String xPath) {
      try {
         Document document = (Document) xmlDocument;
         XPathFactory xPathFactory = XPathFactory.newInstance();
         XPath xpath = xPathFactory.newXPath();
         XPathExpression expr = xpath.compile(xPath);
         NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
         assertTrue(nodeList.getLength() > 0);
      } catch (Exception var7) {
         Assertions.fail("Error while checking element existence in XML document for XPath %s", xPath);
      }
   }
}
