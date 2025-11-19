package selenium.automation.framework.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class ExcelExtractorUtil {

    @DataProvider(name = "excelDataProvider")
    public static Object[][] excelDataProvider(ITestContext context) {
        String sheet = context.getCurrentXmlTest().getParameter("sheetName");
        return extractData(sheet);
    }

    public static Object[][] extractData(String sheetName) {
        List<Object[]> rows = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        try (InputStream in = ExcelExtractorUtil.class.getClassLoader().getResourceAsStream("testData.xlsx")) {
            if (in == null) {
                throw new IllegalStateException("Resource testData.xlsx not found on classpath");
            }
            Workbook workbook = WorkbookFactory.create(in);
            if ("Registrations".equalsIgnoreCase(sheetName)){

                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("Sheet '" + sheetName + "' not found");
                }
                boolean headerSkipped = false;
                for (Row row : sheet) {
                    if (!headerSkipped) { // skip header row
                        headerSkipped = true;
                        continue;
                    }
                    String TestCaseName = row.getCell(0) != null ? formatter.formatCellValue(row.getCell(0)) : "";
                    String Title = row.getCell(1) != null ? formatter.formatCellValue(row.getCell(1)) : "";
                    String Name = row.getCell(2) != null ? formatter.formatCellValue(row.getCell(2)) : "";
                    String Email = row.getCell(3) != null ? formatter.formatCellValue(row.getCell(3)) : "";
                    String Password = row.getCell(4) != null ? formatter.formatCellValue(row.getCell(4)) : "";
                    String DateOfBirth = row.getCell(5) != null ? formatter.formatCellValue(row.getCell(5)) : "";
                    String FirstName = row.getCell(6) != null ? formatter.formatCellValue(row.getCell(6)) : "";
                    String LastName = row.getCell(7) != null ? formatter.formatCellValue(row.getCell(7)) : "";
                    String Company = row.getCell(8) != null ? formatter.formatCellValue(row.getCell(8)) : "";
                    String Address1 = row.getCell(9) != null ? formatter.formatCellValue(row.getCell(9)) : "";
                    String Address2 = row.getCell(10) != null ? formatter.formatCellValue(row.getCell(10)) : "";
                    String Country = row.getCell(11) != null ? formatter.formatCellValue(row.getCell(11)) : "";
                    String State = row.getCell(12) != null ? formatter.formatCellValue(row.getCell(12)) : "";
                    String City = row.getCell(13) != null ? formatter.formatCellValue(row.getCell(13)) : "";
                    String Zipcode = row.getCell(14) != null ? formatter.formatCellValue(row.getCell(14)) : "";
                    String MobileNumber = row.getCell(15) != null ? formatter.formatCellValue(row.getCell(15)) : "";
                    String ExpectedResult = formatter.formatCellValue(row.getCell(16));
                    if( (TestCaseName == null || TestCaseName.isBlank())) {
                        continue;
                    }
                    
                    rows.add(new Object[] { TestCaseName, Title, Name, Email, Password, DateOfBirth, FirstName, LastName, Company, Address1, Address2, Country, State, City, Zipcode, MobileNumber, ExpectedResult });

                }
            }else if("Login".equalsIgnoreCase(sheetName)){
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("Sheet '" + sheetName + "' not found");
                }
                boolean headerSkipped = false;
                for (Row row : sheet) {
                    if (!headerSkipped) { // skip header row
                        headerSkipped = true;
                        continue;
                    }
                    String testCaseName = row.getCell(0) != null ? formatter.formatCellValue(row.getCell(0)) : "";
                    String Email = row.getCell(1) != null ? formatter.formatCellValue(row.getCell(1)) : "";
                    String Password = row.getCell(2) != null ? formatter.formatCellValue(row.getCell(2)) : "";
                    String ExpectedResult = row.getCell(3) != null ? formatter.formatCellValue(row.getCell(3)) : "";
                    if( (testCaseName == null || testCaseName.isBlank())) {
                        continue;
                    }
                    
                    rows.add(new Object[] { testCaseName, Email, Password, ExpectedResult });
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
            // Provide at least one default row so callers can proceed based on sheet type
            if ("Registrations".equalsIgnoreCase(sheetName)) {
                rows.add(new Object[] { "DefaultName", "", "", "default@example.com", "password", "1990-01-01", "First", "Last", "Company", "Address1", "Address2", "United States", "State", "City", "12345", "1234567890", "Default" });
            } else if ("Login".equalsIgnoreCase(sheetName)) {
                rows.add(new Object[] { "DefaultName", "default@example.com", "password", "Default" });
            }
        }
        return rows.toArray(new Object[0][]);
    }

}
