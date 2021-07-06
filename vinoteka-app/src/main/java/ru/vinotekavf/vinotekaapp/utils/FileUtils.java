package ru.vinotekavf.vinotekaapp.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.vinotekavf.vinotekaapp.enums.ExcelColumns;

import java.math.BigDecimal;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.poi.ss.usermodel.Cell.*;

public class FileUtils {
    public static XSSFCell getValuableXSSFCellFromMerged (XSSFSheet sheet, XSSFCell cell) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i); //Region of merged cells
            if (region.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                return sheet.getRow(region.getFirstRow()).getCell(region.getFirstColumn());
            }
        }
        return cell;
    }

    public static HSSFCell getValuableHSSFCellFromMerged (HSSFSheet sheet, HSSFCell cell) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i); //Region of merged cells
            if (region.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                return sheet.getRow(region.getFirstRow()).getCell(region.getFirstColumn());
            }
        }
        return cell;
    }

    public static String getValueFromXLSXColumn(String column, XSSFRow row) {
        String[] columns = column.split(",");

        for (String str : columns) {
            if (isNotBlank(str)) {
                XSSFCell cell = row.getCell(ExcelColumns.valueOf(str).ordinal());
                if (isNotEmpty(cell)) {
                    XSSFCell cellWithValue = getValuableXSSFCellFromMerged(row.getSheet(), cell);
                    if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == CELL_TYPE_STRING) {
                        return cellWithValue.getStringCellValue();
                    } else if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == CELL_TYPE_NUMERIC) {
                        return BigDecimal.valueOf(cellWithValue.getNumericCellValue()).toPlainString();
                    } else if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == CELL_TYPE_FORMULA) {
                        switch (cellWithValue.getCachedFormulaResultType()) {
                            case CELL_TYPE_STRING:
                                return cellWithValue.getRichStringCellValue().getString();
                            case CELL_TYPE_NUMERIC:
                                return BigDecimal.valueOf(cellWithValue.getNumericCellValue()).toPlainString();
                            default:
                                return "";
                        }
                    } else {
                        return "";
                    }
                }
            }
        }
        return "";
    }

    public static String getValueFromXLSColumn(String column, HSSFRow row) {
        String[] columns = column.split(",");

        for (String str : columns) {
            if (isNotBlank(str)) {
                HSSFCell cell = row.getCell(ExcelColumns.valueOf(str).ordinal());
                if (isNotEmpty(cell)) {
                    HSSFCell cellWithValue = getValuableHSSFCellFromMerged(row.getSheet(), cell);
                    if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == CELL_TYPE_STRING) {
                        return cellWithValue.getStringCellValue();
                    } else if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == CELL_TYPE_NUMERIC) {
                        return BigDecimal.valueOf(cellWithValue.getNumericCellValue()).toPlainString();
                    } else if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == CELL_TYPE_FORMULA) {
                        switch (cellWithValue.getCachedFormulaResultType()) {
                            case CELL_TYPE_STRING:
                                return cellWithValue.getRichStringCellValue().getString();
                            case CELL_TYPE_NUMERIC:
                                return BigDecimal.valueOf(cellWithValue.getNumericCellValue()).toPlainString();
                            default:
                                return "";
                        }
                    } else {
                        return "";
                    }
                }
            }
        }
        return "";
    }
}
