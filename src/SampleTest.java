import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class SampleTest {

		public static void main(String[] args) throws IOException {
			
			FileInputStream ipstr = new FileInputStream("C:\\Users\\RB66452\\Desktop\\MyAutomation\\Testdata.xls");
			HSSFWorkbook wb = new HSSFWorkbook(ipstr);
			HSSFSheet ws = wb.getSheetAt(0);
		
		
		}

		private static String getCellValueAsString(HSSFCell cell) {
			String cellValue = null;
			switch (cell.getCellType()) {
			case BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case STRING:
				cellValue = String.valueOf(cell.getRichStringCellValue().toString());
				break;
			case NUMERIC:
				Double value = cell.getNumericCellValue();
				if (value != null) {
					String valueAsStr = value.toString();
					int indexOf = valueAsStr.indexOf(".");
					if (indexOf > 0) {
						cellValue = valueAsStr.substring(0, indexOf);// decimal numbers truncated
					} else {
						cellValue = value.toString();
					}
				}
				break;
			case FORMULA:
				// if the cell contains formula, this case will be executed.
				cellValue = cell.getStringCellValue();
				break;
			case BLANK:
				cellValue = "";
				break;
			case _NONE:
				cellValue = "";
				break;
			case ERROR:
				throw new RuntimeException("There is no support for this type of cell");
			default:
				cellValue = "";
			}
			return cellValue;
		}
	}


