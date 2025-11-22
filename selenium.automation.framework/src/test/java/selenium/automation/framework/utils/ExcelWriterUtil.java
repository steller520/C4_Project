package selenium.automation.framework.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Utility class to write test results back to Excel.
 * Features:
 *  - Auto-detect / create 'Result' column
 *  - Color coding for PASS (green) / FAIL (red)
 *  - Convenience batch write method
 * Design choices:
 *  - Opens/closes workbook per write for simplicity; could be optimized.
 */
public class ExcelWriterUtil {
    
    /**
     * Writes single test result (PASS/FAIL) into Excel, creating 'Result' header if absent.
     * @param sheetName sheet to update
     * @param testCaseName row identifier in first column
     * @param result PASS or FAIL (case-insensitive)
     */
    public static void writeTestResult(String sheetName, String testCaseName, String result) {
        String excelPath = "src/test/resources/testData.xlsx";
        
        try (FileInputStream fis = new FileInputStream(excelPath)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            
            if (sheet == null) {
                System.out.println("Sheet '" + sheetName + "' not found");
                return;
            }
            
            // Locate existing Result column or append new one
            Row headerRow = sheet.getRow(0);
            int resultColumnIndex = -1;
            
            // Check if Result column exists
            for (Cell cell : headerRow) {
                if ("Result".equalsIgnoreCase(cell.getStringCellValue())) {
                    resultColumnIndex = cell.getColumnIndex();
                    break;
                }
            }
            
            // If Result column doesn't exist, create it
            if (resultColumnIndex == -1) {
                resultColumnIndex = headerRow.getLastCellNum();
                Cell resultHeaderCell = headerRow.createCell(resultColumnIndex);
                resultHeaderCell.setCellValue("Result");
            }
            
            // Iterate rows to match testCaseName in first cell
            boolean found = false;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell testCaseCell = row.getCell(0);
                    if (testCaseCell != null && testCaseName.equals(testCaseCell.getStringCellValue())) {
                        // Write result text into determined column
                        Cell resultCell = row.createCell(resultColumnIndex);
                        resultCell.setCellValue(result);
                        
                        // Apply conditional color formatting (green PASS, red FAIL)
                        CellStyle style = workbook.createCellStyle();
                        if ("PASS".equalsIgnoreCase(result)) {
                            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                        } else if ("FAIL".equalsIgnoreCase(result)) {
                            style.setFillForegroundColor(IndexedColors.RED.getIndex());
                        }
                        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        resultCell.setCellStyle(style);
                        
                        found = true;
                        break;
                    }
                }
            }
            
            if (!found) {
                System.out.println("Test case '" + testCaseName + "' not found in sheet '" + sheetName + "'");
            }
            
            // Persist workbook changes
            try (FileOutputStream fos = new FileOutputStream(excelPath)) {
                workbook.write(fos);
                System.out.println("Test result written to Excel: " + testCaseName + " = " + result);
            }
            
            workbook.close();
            
        } catch (Exception e) {
            System.out.println("Error writing test result to Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Batch helper to write multiple results sequentially.
     * @param sheetName sheet to update
     * @param results array of [testCaseName, result]
     */
    public static void writeMultipleResults(String sheetName, String[][] results) {
        for (String[] result : results) {
            if (result.length >= 2) {
                writeTestResult(sheetName, result[0], result[1]);
            }
        }
    }
}
