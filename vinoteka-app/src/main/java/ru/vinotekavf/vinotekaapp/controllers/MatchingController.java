package ru.vinotekavf.vinotekaapp.controllers;


import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.vinotekavf.vinotekaapp.entities.MatchedTable;
import ru.vinotekavf.vinotekaapp.entities.Position;
import ru.vinotekavf.vinotekaapp.entities.Provider;
import ru.vinotekavf.vinotekaapp.enums.ExcelColumns;
import ru.vinotekavf.vinotekaapp.repos.MatchedTableRepository;
import ru.vinotekavf.vinotekaapp.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Controller
public class MatchingController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    MatchedTableRepository tableRepository;

    @PostMapping("match")
    public String matchProducts(@RequestParam("file") MultipartFile file,
                                @RequestParam("provider") String provider,
                                @RequestParam("phone") String phone,
                                @RequestParam("managerName") String managerName,
                                @RequestParam("productName") String productName,
                                @RequestParam("vendorCode") String vendorCode,
                                @RequestParam("price") String price,
                                @RequestParam("promotionalPrice") String promotionalPrice,
                                @RequestParam("remainder") String remainder,
                                @RequestParam("volume") String volume,
                                @RequestParam("releaseYear") String releaseYear
        ) throws IOException {

        String[] productNameCols = productName.split(",");
        String[] vendorCodeCols = vendorCode.split(",");
        String[] priceCols = price.split(",");
        String[] promotionalPriceCols = promotionalPrice.split(",");
        String[] remainderCols = remainder.split(",");
        String[] volumeCols = volume.split(",");
        String[] releaseYearCols = releaseYear.split(",");

        if (isNotEmpty(file.getOriginalFilename())) {
            File uploadDirectory = new File(uploadPath);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdir();
            }
            file.transferTo(new File(uploadPath + "/" + file.getOriginalFilename()));
            if (file.getOriginalFilename().contains("xlsx") || file.getOriginalFilename().contains("xlsm")) {
                XSSFWorkbook book = new XSSFWorkbook(new FileInputStream(uploadPath + "/" + file.getOriginalFilename()));
                XSSFSheet sheet = book.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();
                while (rowIterator.hasNext()) {
                    MatchedTable table = new MatchedTable();
                    table.setProvider(provider);
                    table.setPhone(phone);
                    table.setManagerName(managerName);

                    XSSFRow row = (XSSFRow) rowIterator.next();

                    for (String str : productNameCols) {
                        if (isNotBlank(str)) {
                            XSSFCell curCell = row.getCell(ExcelColumns.valueOf(str).ordinal());
                            if (isNotEmpty(curCell)) {
                                XSSFCell cellWithValue = FileUtils.getValuableXSSFCellFromMerged(sheet, curCell);
                                if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                    table.setProductName(cellWithValue.getStringCellValue());
                                } else {
                                    table.setProductName("");
                                }
                            }
                        }
                    }

                    for (String str : vendorCodeCols) {
                        if (isNotBlank(str)) {
                            XSSFCell curCell = row.getCell(ExcelColumns.valueOf(str).ordinal());
                            if (isNotEmpty(curCell)) {
                                XSSFCell cellWithValue = FileUtils.getValuableXSSFCellFromMerged(sheet, curCell);
                                if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                    table.setFvProductName(cellWithValue.getStringCellValue());
                                } else if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                    table.setVendorCode(BigDecimal.valueOf(cellWithValue.getNumericCellValue()).toPlainString());
                                } else {
                                    table.setVendorCode("");
                                }
                            }
                        }
                    }

                    for (String str : priceCols) {
                        if (isNotBlank(str)) {
                            XSSFCell curCell = row.getCell(ExcelColumns.valueOf(str).ordinal());
                            if (isNotEmpty(curCell)) {
                                XSSFCell cellWithValue = FileUtils.getValuableXSSFCellFromMerged(sheet, curCell);
                                if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                    table.setFvProductName(cellWithValue.getStringCellValue());
                                } else if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                    table.setPrice(BigDecimal.valueOf(cellWithValue.getNumericCellValue()).toPlainString());
                                } else {
                                    table.setPrice("");
                                }
                            }
                        }
                    }

                    for (String str : promotionalPriceCols) {
                        if (isNotBlank(str)) {
                            XSSFCell curCell = row.getCell(ExcelColumns.valueOf(str).ordinal());
                            if (isNotEmpty(curCell)) {
                                XSSFCell cellWithValue = FileUtils.getValuableXSSFCellFromMerged(sheet, curCell);
                                if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                    table.setFvProductName(cellWithValue.getStringCellValue());
                                } else if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                    table.setPromotionalPrice(BigDecimal.valueOf(cellWithValue.getNumericCellValue()).toPlainString());
                                } else {
                                    table.setPromotionalPrice("");
                                }
                            }
                        }
                    }

                    for (String str : remainderCols) {
                        if (isNotBlank(str)) {
                            XSSFCell curCell = row.getCell(ExcelColumns.valueOf(str).ordinal());
                            if (isNotEmpty(curCell)) {
                                XSSFCell cellWithValue = FileUtils.getValuableXSSFCellFromMerged(sheet, curCell);
                                if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                    table.setFvProductName(cellWithValue.getStringCellValue());
                                } else if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                    table.setRemainder(BigDecimal.valueOf(cellWithValue.getNumericCellValue()).toPlainString());
                                } else {
                                    table.setRemainder("");
                                }
                            }
                        }
                    }

                    for (String str : volumeCols) {
                        if (isNotBlank(str))  {
                            XSSFCell curCell = row.getCell(ExcelColumns.valueOf(str).ordinal());
                            if (isNotEmpty(curCell)) {
                                XSSFCell cellWithValue = FileUtils.getValuableXSSFCellFromMerged(sheet, curCell);
                                if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                    table.setFvProductName(cellWithValue.getStringCellValue());
                                } else if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                    table.setVolume(BigDecimal.valueOf(cellWithValue.getNumericCellValue()).toPlainString());
                                } else {
                                    table.setVolume("");
                                }
                            }
                        }

                    }

                    for (String str : releaseYearCols) {
                        if (isNotBlank(str)) {
                            XSSFCell curCell = row.getCell(ExcelColumns.valueOf(str).ordinal());
                            if (isNotEmpty(curCell)) {
                                XSSFCell cellWithValue = FileUtils.getValuableXSSFCellFromMerged(sheet, curCell);
                                if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                    table.setFvProductName(cellWithValue.getStringCellValue());
                                } else if (isNotEmpty(cellWithValue) && cellWithValue.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                    table.setReleaseYear(BigDecimal.valueOf(cellWithValue.getNumericCellValue()).toPlainString());
                                } else {
                                    table.setReleaseYear("");
                                }
                            }
                        }
                    }
                    tableRepository.save(table);
                }
            } else if (file.getOriginalFilename().contains("xls")) {
                HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(uploadPath + "/" + file.getOriginalFilename()));
            } else if (file.getOriginalFilename().contains("csv")) {
                //Todo csv handling
            }
        }
        return "main";
    }
}
