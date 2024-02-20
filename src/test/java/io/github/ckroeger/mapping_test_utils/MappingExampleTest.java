package io.github.ckroeger.mapping_test_utils;

import io.github.ckroeger.mapping_test_utils.model.mapper.AddressMapper;
import io.github.ckroeger.mapping_test_utils.model.source.SourceAddress;
import io.github.ckroeger.mapping_test_utils.model.target.TargetAddress;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MappingExampleTest {

   @MethodSource("getTestDataAsRowsFromSpecification")
   @ParameterizedTest(name = "{index}: Zeile: {0}")
   void testAddressMappingWithExcel(List<Map<String, String>> testData) throws IOException, JAXBException, ParserConfigurationException, SAXException, XPathExpressionException {
      boolean assertMapping = Boolean.parseBoolean(MappingTestComparisonUtil.getCellValueByColumnName(testData, "Assert mapping rule"));

      if (assertMapping) {
         // Given
         // The cell values for the current row by column name
         String sourceJsonPath = MappingTestComparisonUtil.getCellValueByColumnName(testData, "Source (Json Path)");
         String targetXpath = MappingTestComparisonUtil.getCellValueByColumnName(testData, "Target (XPath)");
         String constant = MappingTestComparisonUtil.getCellValueByColumnName(testData, "DEFAULT");

         // A source address from a JSON file
         SourceAddress sourceAddress = MappingTestDataProvider.loadObjectFromJsonFile("source.json", SourceAddress.class);
         assertThat(sourceAddress).isNotNull();

         // When
         // The source address is mapped to the target address
         TargetAddress targetAddress = AddressMapper.mapToTargetAddress(sourceAddress);

         // Then
         // Create an XML document from the targetObject
         Document xmlDocument = MappingTestDataProvider.getDocument(targetAddress, "Address", TargetAddress.class);

         // Compare the source value with the target value, if no default constant is given
         // Else compare the target value with the constant value
         MappingTestComparisonUtil.assertSpecification(xmlDocument, sourceAddress, sourceJsonPath, targetXpath, constant);
      }
   }

   Object[][] getTestDataAsRowsFromSpecification() throws IOException {
      return MappingTestDataProvider.provideTestDataAsRowsFromSpecification("src/test/resources/example_mapping_spec.xlsx", "Address");
   }


}
