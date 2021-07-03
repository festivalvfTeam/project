package ru.vinotekavf.vinotekaapp.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

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


}
