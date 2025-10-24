package data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dataprovider {

    private static final String excel_path = "C:\\Users\\aryan.sinha\\selenium_QA\\bootcampoc\\src\\test\\resources\\TestData\\TestDataPOC.xlsx";
//    private static final String valid_sheet = "Happy Path";
//    private static final String invalid_sheet = "Negative Flow";
    private static final String single_sheet = "Test Data";

    //method to read data from excel
    private Object[][] getExcelData(String sheetName) {
        List<Object[]> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(excel_path));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            //iterate through all rows starting from the 2nd row 
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip the header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row == null) continue;
                int lastColumn = row.getLastCellNum();
                Object[] rowData = new Object[lastColumn];

                for (int i = 0; i < lastColumn; i++) {
                    Cell cell = row.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData[i] = getCellValue(cell);
                }
                dataList.add(rowData);
            }

        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + excel_path);
            e.printStackTrace();
        }

        return dataList.toArray(new Object[0][0]);
    }
    
    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                    return dateFormat.format(cell.getDateCellValue());
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            default:
                return cell.toString();
        }
    }
    
//    //data providers
//
//    @DataProvider(name = "validRegistrationData")
//    public Object[][] provideValidRegistrationData() {
//        return getExcelData(valid_sheet);
//    }
//    
//    @DataProvider(name = "invalidRegistrationData")
//    public Object[][] provideInvalidRegistrationData() {
//        return getExcelData(invalid_sheet);
//    }
    
 // Data providers - filter by test type
    @DataProvider(name = "validRegistrationData")
    public Object[][] provideValidRegistrationData() {
        return filterDataByTestType("valid");
    }
    
    @DataProvider(name = "invalidRegistrationData")
    public Object[][] provideInvalidRegistrationData() {
        return filterDataByTestType("invalid");
    }
    
    // Filter data based on test type
    private Object[][] filterDataByTestType(String testType) {
        Object[][] allData = getExcelData(single_sheet);
        List<Object[]> filteredData = new ArrayList<>();
        
        for (Object[] row : allData) {
            if (row.length > 0 && row[0] != null && 
                row[0].toString().equalsIgnoreCase(testType)) {
                filteredData.add(row);
            }
        }
        
        return filteredData.toArray(new Object[0][0]);
    }

}