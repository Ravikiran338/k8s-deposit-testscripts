/**
 * @author Jp
 *
 */
package com.radiant.microservices.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.radiant.microservices.model.WebElementDataDetails;

public class JExcelParser {
	protected transient final Log log = LogFactory.getLog(getClass());
	private static JExcelParser jExcelParser = null;

	// ==========================================================================

	private JExcelParser() {
	}

	// ==========================================================================

	public static synchronized JExcelParser getInstance() {
		if (jExcelParser == null) {
			jExcelParser = new JExcelParser();
		}
		return jExcelParser;
	}

	// ==========================================================================

	public List<WebElementDataDetails> getDataSet(String fileName, String sheetName) {
		log.info("START of the method getDataSet");
		FileInputStream file = null;
		Workbook workbook = null;
		List<WebElementDataDetails> webElementsData = null;
		int rowCount = 0;
		int cellCount = 0;

		try {
			file = new FileInputStream(new File(AppUtil.getExcelFilePath(fileName)));
			workbook = WorkbookFactory.create(file);
			webElementsData = new ArrayList<WebElementDataDetails>();
			if (sheetName.contains("TestScript")) {
				sheetName = sheetName.replace("TestScript", "");
			}
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet != null) {
				for (Row row : sheet) {
					if (rowCount > 0) {

						WebElementDataDetails dataDetails = new WebElementDataDetails();
						List<String> dataSet = new ArrayList<String>();
						for (Cell cell : row) {
							if (cellCount == 0) {
								if (getCellValue(cell) != null) {
									Double dataSetNo = Double.parseDouble(getCellValue(cell));
									dataDetails.setDataSetNo(dataSetNo.intValue());
								}
							} else if (cellCount == 1) {
								dataDetails.setExecute(Boolean.parseBoolean(getCellValue(cell)));
							} else {
								dataSet.add(getCellValue(cell));
							}
							cellCount++;
						}
						cellCount = 0;

						dataDetails.setDataSet(dataSet);
						webElementsData.add(dataDetails);
					}
					rowCount++;
				}
			} else {
				log.error("---- Given Data sheet not present in Excel");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		log.info("END of the method getDataSet");
		return webElementsData;
	}

	// ==========================================================================

	private String getCellValue(Cell cell) {
		String cellValue = null;

		try {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				Double dataSetNo = cell.getNumericCellValue();
				cellValue = String.valueOf(dataSetNo.intValue());
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cellValue;
	}

	// ==========================================================================
}