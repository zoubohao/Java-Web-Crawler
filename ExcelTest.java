package Demo;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow row = sheet.createRow(1);
		XSSFCell cell = row.createCell(0,CellType.STRING);
		cell.setCellValue("hellword");
		try {
			FileOutputStream outputStream = new FileOutputStream(new File("d:\\test.xlsx"));
	        workbook.write(outputStream);
	        //¹Ø±Õ¹¤×÷²¾
	        workbook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
