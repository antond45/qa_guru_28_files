package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;

import com.fasterxml.jackson.databind.ObjectMapper;



import com.opencsv.CSVReader;
import model.ExampleJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class FilesParsingTest {
    private ClassLoader cl = FilesParsingTest.class.getClassLoader();

   @Test
    void pdfFileContainsZipTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("samples.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("sample_pdf.pdf")) {
                    PDF pdfFile = new PDF(zis);
                    assertThat(pdfFile.text).contains("Insert Your Image");
                }
            }
        }
    }

    @Test
    void xlsFileContainsZipTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("samples.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("file_example_XLS_10.xls")) {
                    XLS xls = new XLS(zis);
                    String actualValue= xls.excel.getSheetAt(0).getRow(1).getCell(2).getStringCellValue();
                    assertThat(actualValue).isEqualTo("Abril");
                }
            }
        }
    }

    @Test
    void cvsFileContainsZipTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("samples.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("annual-enterprise-survey-2023-financial-year-provisional.csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> data = csvReader.readAll();
                    Assertions.assertArrayEquals(
                            new String[]{"Year",	"Industry_aggregation_NZSIOC",	"Industry_code_NZSIOC",	"Industry_name_NZSIOC",
                                    "Units",	"Variable_code",	"Variable_name",	"Variable_category",	"Value",
                                    "Industry_code_ANZSIC06"}, data.get(0));
                    Assertions.assertArrayEquals(
                            new String[]{"2023",	"Level 1",	"99999",	"All industries",	"Dollars (millions)", "H01",
                                    "Total income",	"Financial performance",	"930995",
                                    "ANZSIC06 divisions A-S (excluding classes K6330, L6711, O7552, O760, O771, O772, S9540, S9601, S9602, and S9603)"}, data.get(1));
                }
                }
            }
        }

    @Test
    @DisplayName("JsonParcingDataTest")
    void jsonParcingDataTest() throws Exception {
        try (InputStream inputStream = cl.getResourceAsStream("exampleJson.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ExampleJson user = objectMapper.readValue(inputStream, ExampleJson.class);
            Assertions.assertEquals("Anton", user.getFirstname());
            Assertions.assertEquals("Test", user.getLastname());
            Assertions.assertEquals("worker", user.getRole());
        }
    }
}
