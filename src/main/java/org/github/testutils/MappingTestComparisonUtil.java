package org.github.testutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.assertj.core.api.Assertions;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
            .map(testData -> testData.get(columnName))
            .orElseThrow(NoSuchElementException::new);
   }

   public static void checkTargetValueMatchesExpectedValue(Document xmlDocument, String expectedTargetValue, String targetXpath, Object targetValue) {
      if (targetXpath.endsWith("/text()")) {
         MappingTestComparisonUtil.checkValueMatchesSpecification(targetValue, expectedTargetValue);
      } else if (targetXpath.matches(".*/@.+")) {
         String xPathWithoutAttribute = MappingTestComparisonUtil.removeAttributeFromXPath(targetXpath);
         MappingTestComparisonUtil.elementExistsInXmlDocument(xmlDocument, xPathWithoutAttribute);
         MappingTestComparisonUtil.checkValueMatchesSpecification(targetValue, expectedTargetValue);
      } else {
         MappingTestComparisonUtil.elementExistsInXmlDocument(xmlDocument, targetXpath);
      }
   }

   public static String determineExpectedTargetValue(String sourceJsonPath, Object sourceObject, String constant) throws JsonProcessingException {
      if (("CONSTANT".equals(sourceJsonPath) || sourceJsonPath == null || "null".equals(sourceJsonPath))
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

   public static <T> Document createXmlDocument(Class<T> marshallingClass, JAXBElement<T> jaxbElement) throws ParserConfigurationException, JAXBException, IOException, SAXException {
      // Put JAXB element into StringWriter
      StringWriter stringWriter = new StringWriter();
      createMarshaller(marshallingClass)
            .marshal(jaxbElement, stringWriter);

      // Create Document based on xml string
      String xml = stringWriter.toString();
      InputSource source = new InputSource(new StringReader(xml));
      return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(source);
   }

   private static <T> Marshaller createMarshaller(Class<T> clazz) throws JAXBException {
      JAXBContext context = JAXBContext.newInstance(clazz);
      Marshaller mar = context.createMarshaller();
      mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      return mar;
   }

   public static <T> JAXBElement<T> getJaxbElement(Class<T> targetType, String elementName, T targetElement) {
      final QName qname = new QName(null, elementName);
      return new JAXBElement<>(qname, targetType, null, targetElement);
   }

   public static Object evaluateXpathExpression(Document xmlDocument, String xPathExpression) throws XPathExpressionException {
      XPath xPath = XPathFactory.newInstance().newXPath();
      return xPath.evaluate(xPathExpression, xmlDocument);
   }

   public static void checkValueMatchesSpecification(Object valueFromXPath, String valueFromSource) {
      assertThat(valueFromXPath).isEqualTo(valueFromSource);
   }

   public static void elementExistsInXmlDocument(Object xmlDocument, String xPath) {
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

   public static String removeAttributeFromXPath(String xPath) {
      return xPath.substring(0, xPath.lastIndexOf("/@"));
   }

}
