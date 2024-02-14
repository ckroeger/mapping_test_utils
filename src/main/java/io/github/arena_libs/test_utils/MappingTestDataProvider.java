package io.github.arena_libs.test_utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class MappingTestDataProvider {

   private MappingTestDataProvider() {
   }

   static Logger log = Logger.getLogger(MappingTestDataProvider.class.getName());

   public static Object[][] provideTestDataAsRowsFromSpecification(String pathToFile, String sheetName) throws IOException { //convertExcelTestDataAsArray
      if (pathToFile == null || pathToFile.isEmpty()
            || sheetName == null || sheetName.isEmpty()) {
         return new Object[0][0];
      }
      List<Map<String, Object>> testDataList = createArgsFromExcelAsMap(pathToFile, sheetName);

      return testDataList.stream()
            .map(testDataMap -> new Object[] {List.of(testDataMap)})
            .toArray(Object[][]::new);
   }

   private static <T> JAXBElement<T> getJaxbElement(Class<T> targetType, String elementName, T targetElement) {
      final QName qname = new QName(null, elementName);
      return new JAXBElement<>(qname, targetType, null, targetElement);
   }


   private static <T> Marshaller createMarshaller(Class<T> clazz) throws JAXBException {
      JAXBContext context = JAXBContext.newInstance(clazz);
      Marshaller mar = context.createMarshaller();
      mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      return mar;
   }

   private static <T> Document createXmlDocument(Class<T> marshallingClass, JAXBElement<T> jaxbElement) throws ParserConfigurationException, JAXBException, IOException, SAXException {
      // Put JAXB element into StringWriter
      StringWriter stringWriter = new StringWriter();
      createMarshaller(marshallingClass)
            .marshal(jaxbElement, stringWriter);

      // Create Document based on xml string
      String xml = stringWriter.toString();
      InputSource source = new InputSource(new StringReader(xml));
      return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(source);
   }

   public static <T> Document getDocument(T targetObject, String jaxbElementName, Class<T> typeClass) throws ParserConfigurationException, JAXBException, IOException, SAXException {
      JAXBElement<T> targetJaxbElement = getJaxbElement(typeClass, jaxbElementName, targetObject);
      return createXmlDocument(typeClass, targetJaxbElement);
   }

   private static List<Map<String, Object>> createArgsFromExcelAsMap(String pathToFile, String sheetName) throws IOException {
      List<Map<String, Object>> testDataList = new ArrayList<>();

      try (FileInputStream fis = new FileInputStream(pathToFile);
           Workbook workbook = new XSSFWorkbook(fis)) {
         Sheet sheet = workbook.getSheet(sheetName);

         Row headerRow = sheet.getRow(0);
         List<String> columnNames = new ArrayList<>();

         for (Cell cell : headerRow) {
            columnNames.add(cell.getStringCellValue());
         }

         IntStream.range(1, sheet.getPhysicalNumberOfRows())
               .mapToObj(sheet::getRow)
               .forEach(row -> {
                  Map<String, Object> testDataMap = new HashMap<>();
                  IntStream.range(0, columnNames.size())
                        .forEach(index -> {
                           Cell cell = row.getCell(index);
                           String columnName = columnNames.get(index);
                           Object cellValue = (cell == null) ? "" : convertToType(cell);
                           testDataMap.put(columnName, cellValue);
                        });
                  testDataList.add(testDataMap);
               });

         return testDataList;
      }
   }

   private static Object convertToType(Cell cell) {
      if (cell == null) {
         return null;
      } else {
         switch (cell.getCellType()) {
            case NUMERIC:
               if (DateUtil.isCellDateFormatted(cell)) {
                  return cell.getDateCellValue();
               }

               return cell.getNumericCellValue();
            case BOOLEAN:
               return cell.getBooleanCellValue();
            case STRING:
               return cell.getStringCellValue();
            case FORMULA:
               return evaluateFormula(cell);
            default:
               return null;
         }
      }
   }

   private static Object evaluateFormula(Cell cell) {
      FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
      CellValue cellValue = evaluator.evaluate(cell);
      CellType resultType = cellValue.getCellType();
      switch (resultType) {
         case NUMERIC:
            return cellValue.getNumberValue();
         case BOOLEAN:
            return cellValue.getBooleanValue();
         case STRING:
            return cellValue.getStringValue();
         default:
            return null;
      }
   }

   public static <T> T loadObjectFromJsonFile(String classPathUrl, Class<T> sourceDocumentClass) throws IOException {
      ObjectMapper objectMapper = new ObjectMapper();
      String json = readFileToString(classPathUrl);
      return objectMapper.readValue(json, sourceDocumentClass);
   }

   private static String readFileToString(String path) {
      try {
         ClassLoader classLoader = MappingTestDataProvider.class.getClassLoader();
         File file = new File(classLoader.getResource(path).getFile());
         String absolutePath = file.getAbsolutePath();

         return Files.readString(Path.of(absolutePath));
      } catch (IOException e) {
         log.info(String.format("Error while reading file contents of %s", path));
         return null;
      }
   }

}
