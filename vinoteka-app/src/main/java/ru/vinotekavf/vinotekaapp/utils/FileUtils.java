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
        if (isNotBlank(column)) {
            XSSFCell cell = row.getCell(ExcelColumns.valueOf(column).ordinal());
            if (isNotEmpty(cell)) {
                if (isNotEmpty(cell) && cell.getCellType() == CELL_TYPE_STRING) {
                    return cell.getStringCellValue();
                } else if (isNotEmpty(cell) && cell.getCellType() == CELL_TYPE_NUMERIC) {
                    return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                } else if (isNotEmpty(cell) && cell.getCellType() == CELL_TYPE_FORMULA) {
                    switch (cell.getCachedFormulaResultType()) {
                        case CELL_TYPE_STRING:
                            return cell.getRichStringCellValue().getString();
                        case CELL_TYPE_NUMERIC:
                            return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                    }
                } else {
                    return "";
                }
            }
        }
        return "";
    }

    public static String getValueFromXLSColumn(String column, HSSFRow row) {
        if (isNotBlank(column)) {
            HSSFCell cell = row.getCell(ExcelColumns.valueOf(column).ordinal());
            if (isNotEmpty(cell)) {
                if (isNotEmpty(cell) && cell.getCellType() == CELL_TYPE_STRING) {
                    return cell.getStringCellValue();
                } else if (isNotEmpty(cell) && cell.getCellType() == CELL_TYPE_NUMERIC) {
                    return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                } else if (isNotEmpty(cell) && cell.getCellType() == CELL_TYPE_FORMULA) {
                    switch (cell.getCachedFormulaResultType()) {
                        case CELL_TYPE_STRING:
                            return cell.getRichStringCellValue().getString();
                        case CELL_TYPE_NUMERIC:
                            return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                    }
                } else {
                    return "";
                }
            }
        }
        return "";
    }
}
