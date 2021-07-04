package ru.vinotekavf.vinotekaapp.controllers;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import ru.vinotekavf.vinotekaapp.entities.Position;
import ru.vinotekavf.vinotekaapp.entities.Provider;
import ru.vinotekavf.vinotekaapp.enums.ExcelColumns;
import ru.vinotekavf.vinotekaapp.repos.PositionRepository;
import ru.vinotekavf.vinotekaapp.repos.ProviderRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

@Controller
public class InitDBController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    ProviderRepository providerRepository;

    @Autowired
    PositionRepository positionRepository;

    @PostMapping("testingMatch")
    public String matchTestFile(@RequestParam("file") MultipartFile file,
        @RequestParam("provider") String provider,
        @RequestParam("phone") String phone,
        @RequestParam("managerName") String managerName,
        @RequestParam("productName") String productName,
        @RequestParam("vendorCode") String vendorCode,
        @RequestParam("price") String price,
        @RequestParam("promotionalPrice") String promotionalPrice,
        @RequestParam("remainder") String remainder,
        @RequestParam("volume") String volume,
        @RequestParam("releaseYear") String releaseYear,
        @RequestParam("maker") String maker,
        @RequestParam("fvVendorCode") String fvVendorCode,
        @RequestParam("fvProductName") String fvProductName
    ) throws IOException {

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

                    XSSFRow row = (XSSFRow) rowIterator.next();

                    if (isNotBlank(provider)) {
                        XSSFCell providerCell = row.getCell(ExcelColumns.valueOf(provider).ordinal());
                        if (isNotEmpty(providerCell)) {
                            if (ObjectUtils.isEmpty(providerRepository.findByName(providerCell.getStringCellValue()))) {
                                Provider curProvider = new Provider();
                                Position position = new Position();
                                if (isNotEmpty(providerCell) && providerCell.getCellType() == CELL_TYPE_STRING) {
                                    curProvider.setName(providerCell.getStringCellValue());
                                } else {
                                    curProvider.setName("");
                                }

                                if (isNotBlank(phone)) {
                                    XSSFCell phoneCell = row.getCell(ExcelColumns.valueOf(phone).ordinal());
                                    if (isNotEmpty(phoneCell)) {
                                        if (isNotEmpty(phoneCell) && phoneCell.getCellType() == CELL_TYPE_STRING) {
                                            curProvider.setPhone(phoneCell.getStringCellValue());
                                        } else if (isNotEmpty(phoneCell) && phoneCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            curProvider.setPhone(BigDecimal.valueOf(phoneCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            curProvider.setPhone("");
                                        }
                                    }
                                }

                                if (isNotBlank(managerName)) {
                                    XSSFCell managerNameCell = row.getCell(ExcelColumns.valueOf(managerName).ordinal());
                                    if (isNotEmpty(managerNameCell)) {
                                        if (isNotEmpty(managerNameCell) && managerNameCell.getCellType() == CELL_TYPE_STRING) {
                                            curProvider.setManagerName(managerNameCell.getStringCellValue());
                                        } else {
                                            curProvider.setPhone("");
                                        }
                                    }
                                }

                                if (isNotBlank(productName)) {
                                    XSSFCell productNameCell = row.getCell(ExcelColumns.valueOf(productName).ordinal());
                                    if (isNotEmpty(productNameCell)) {
                                        if (isNotEmpty(productNameCell) && productNameCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setProductName(productNameCell.getStringCellValue());
                                        } else {
                                            position.setProductName("");
                                        }
                                    }
                                }

                                if (isNotBlank(vendorCode)) {
                                    XSSFCell vendorCodeCell = row.getCell(ExcelColumns.valueOf(vendorCode).ordinal());
                                    if (isNotEmpty(vendorCodeCell)) {
                                        if (isNotEmpty(vendorCodeCell) && vendorCodeCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setVendorCode(vendorCodeCell.getStringCellValue());
                                        } else if (isNotEmpty(vendorCodeCell) && vendorCodeCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setVendorCode(BigDecimal.valueOf(vendorCodeCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setVendorCode("");
                                        }
                                    }
                                }

                                if (isNotBlank(price)) {
                                    XSSFCell priceCell = row.getCell(ExcelColumns.valueOf(price).ordinal());
                                    if (isNotEmpty(priceCell)) {
                                        if (isNotEmpty(priceCell) && priceCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setPrice(priceCell.getStringCellValue());
                                        } else if (isNotEmpty(priceCell) && priceCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setPrice(BigDecimal.valueOf(priceCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setPrice("");
                                        }
                                    }
                                }

                                if (isNotBlank(promotionalPrice)) {
                                    XSSFCell promotionalPriceCell = row.getCell(ExcelColumns.valueOf(promotionalPrice).ordinal());
                                    if (isNotEmpty(promotionalPriceCell)) {
                                        if (isNotEmpty(promotionalPriceCell) && promotionalPriceCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setPromotionalPrice(promotionalPriceCell.getStringCellValue());
                                        } else if (isNotEmpty(promotionalPriceCell) && promotionalPriceCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setPromotionalPrice(BigDecimal.valueOf(promotionalPriceCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setPromotionalPrice("");
                                        }
                                    }
                                }

                                if (isNotBlank(remainder)) {
                                    XSSFCell remainderCell = row.getCell(ExcelColumns.valueOf(remainder).ordinal());
                                    if (isNotEmpty(remainderCell)) {
                                        if (isNotEmpty(remainderCell) && remainderCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setRemainder(remainderCell.getStringCellValue());
                                        } else if (isNotEmpty(remainderCell) && remainderCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setRemainder(BigDecimal.valueOf(remainderCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setRemainder("");
                                        }
                                    }
                                }

                                if (isNotBlank(volume)) {
                                    XSSFCell volumeCell = row.getCell(ExcelColumns.valueOf(volume).ordinal());
                                    if (isNotEmpty(volumeCell)) {
                                        if (isNotEmpty(volumeCell) && volumeCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setVolume(volumeCell.getStringCellValue());
                                        } else if (isNotEmpty(volumeCell) && volumeCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setVolume(BigDecimal.valueOf(volumeCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setVolume("");
                                        }
                                    }
                                }

                                if (isNotBlank(releaseYear)) {
                                    XSSFCell releaseYearCell = row.getCell(ExcelColumns.valueOf(releaseYear).ordinal());
                                    if (isNotEmpty(releaseYearCell)) {
                                        if (isNotEmpty(releaseYearCell) && releaseYearCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setReleaseYear(releaseYearCell.getStringCellValue());
                                        } else if (isNotEmpty(releaseYearCell) && releaseYearCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setReleaseYear(BigDecimal.valueOf(releaseYearCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setReleaseYear("");
                                        }
                                    }
                                }

                                if (isNotBlank(maker)) {
                                    XSSFCell makerCell = row.getCell(ExcelColumns.valueOf(maker).ordinal());
                                    if (isNotEmpty(makerCell)) {
                                        if (isNotEmpty(makerCell) && makerCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setMaker(makerCell.getStringCellValue());
                                        } else {
                                            position.setMaker("");
                                        }
                                    }
                                }

                                if (isNotBlank(fvProductName)) {
                                    XSSFCell fvProductNameCell = row.getCell(ExcelColumns.valueOf(fvProductName).ordinal());
                                    if (isNotEmpty(fvProductNameCell)) {
                                        if (isNotEmpty(fvProductNameCell) && fvProductNameCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setFvProductName(fvProductNameCell.getStringCellValue());
                                        } else {
                                            position.setFvProductName("");
                                        }
                                    }
                                }

                                if (isNotBlank(fvVendorCode)) {
                                    XSSFCell fvVendorCodeCell = row.getCell(ExcelColumns.valueOf(fvVendorCode).ordinal());
                                    if (isNotEmpty(fvVendorCodeCell)) {
                                        if (isNotEmpty(fvVendorCodeCell) && fvVendorCodeCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setFvVendorCode(fvVendorCodeCell.getStringCellValue());
                                        } else if (isNotEmpty(fvVendorCodeCell) && fvVendorCodeCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setFvVendorCode(BigDecimal.valueOf(fvVendorCodeCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setFvVendorCode("");
                                        }
                                    }
                                }

                                providerRepository.save(curProvider);
                                position.setProvider(curProvider);
                                positionRepository.save(position);

                            } else {
                                Provider curProvider = providerRepository.findByName(providerCell.getStringCellValue());
                                Position position = new Position();

                                if (isNotBlank(phone)) {
                                    XSSFCell phoneCell = row.getCell(ExcelColumns.valueOf(phone).ordinal());
                                    if (isNotEmpty(phoneCell)) {
                                        if (isNotEmpty(phoneCell) && phoneCell.getCellType() == CELL_TYPE_STRING) {
                                            curProvider.setPhone(phoneCell.getStringCellValue());
                                        } else if (isNotEmpty(phoneCell) && phoneCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            curProvider.setPhone(BigDecimal.valueOf(phoneCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            curProvider.setPhone("");
                                        }
                                    }
                                }

                                if (isNotBlank(managerName)) {
                                    XSSFCell managerNameCell = row.getCell(ExcelColumns.valueOf(managerName).ordinal());
                                    if (isNotEmpty(managerNameCell)) {
                                        if (isNotEmpty(managerNameCell) && managerNameCell.getCellType() == CELL_TYPE_STRING) {
                                            curProvider.setManagerName(managerNameCell.getStringCellValue());
                                        } else {
                                            curProvider.setPhone("");
                                        }
                                    }
                                }

                                providerRepository.save(curProvider);

                                if (isNotBlank(productName)) {
                                    XSSFCell productNameCell = row.getCell(ExcelColumns.valueOf(productName).ordinal());
                                    if (isNotEmpty(productNameCell)) {
                                        if (isNotEmpty(productNameCell) && productNameCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setProductName(productNameCell.getStringCellValue());
                                        } else {
                                            position.setProductName("");
                                        }
                                    }
                                }

                                if (isNotBlank(vendorCode)) {
                                    XSSFCell vendorCodeCell = row.getCell(ExcelColumns.valueOf(vendorCode).ordinal());
                                    if (isNotEmpty(vendorCodeCell)) {
                                        if (isNotEmpty(vendorCodeCell) && vendorCodeCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setVendorCode(vendorCodeCell.getStringCellValue());
                                        } else if (isNotEmpty(vendorCodeCell) && vendorCodeCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setVendorCode(BigDecimal.valueOf(vendorCodeCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setVendorCode("");
                                        }
                                    }
                                }

                                if (isNotBlank(price)) {
                                    XSSFCell priceCell = row.getCell(ExcelColumns.valueOf(price).ordinal());
                                    if (isNotEmpty(priceCell)) {
                                        if (isNotEmpty(priceCell) && priceCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setPrice(priceCell.getStringCellValue());
                                        } else if (isNotEmpty(priceCell) && priceCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setPrice(BigDecimal.valueOf(priceCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setPrice("");
                                        }
                                    }
                                }

                                if (isNotBlank(promotionalPrice)) {
                                    XSSFCell promotionalPriceCell = row.getCell(ExcelColumns.valueOf(promotionalPrice).ordinal());
                                    if (isNotEmpty(promotionalPriceCell)) {
                                        if (isNotEmpty(promotionalPriceCell) && promotionalPriceCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setPromotionalPrice(promotionalPriceCell.getStringCellValue());
                                        } else if (isNotEmpty(promotionalPriceCell) && promotionalPriceCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setPromotionalPrice(BigDecimal.valueOf(promotionalPriceCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setPromotionalPrice("");
                                        }
                                    }
                                }

                                if (isNotBlank(remainder)) {
                                    XSSFCell remainderCell = row.getCell(ExcelColumns.valueOf(remainder).ordinal());
                                    if (isNotEmpty(remainderCell)) {
                                        if (isNotEmpty(remainderCell) && remainderCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setRemainder(remainderCell.getStringCellValue());
                                        } else if (isNotEmpty(remainderCell) && remainderCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setRemainder(BigDecimal.valueOf(remainderCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setRemainder("");
                                        }
                                    }
                                }

                                if (isNotBlank(volume)) {
                                    XSSFCell volumeCell = row.getCell(ExcelColumns.valueOf(volume).ordinal());
                                    if (isNotEmpty(volumeCell)) {
                                        if (isNotEmpty(volumeCell) && volumeCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setVolume(volumeCell.getStringCellValue());
                                        } else if (isNotEmpty(volumeCell) && volumeCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setVolume(BigDecimal.valueOf(volumeCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setVolume("");
                                        }
                                    }
                                }

                                if (isNotBlank(releaseYear)) {
                                    XSSFCell releaseYearCell = row.getCell(ExcelColumns.valueOf(releaseYear).ordinal());
                                    if (isNotEmpty(releaseYearCell)) {
                                        if (isNotEmpty(releaseYearCell) && releaseYearCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setReleaseYear(releaseYearCell.getStringCellValue());
                                        } else if (isNotEmpty(releaseYearCell) && releaseYearCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setReleaseYear(BigDecimal.valueOf(releaseYearCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setReleaseYear("");
                                        }
                                    }
                                }

                                if (isNotBlank(maker)) {
                                    XSSFCell makerCell = row.getCell(ExcelColumns.valueOf(maker).ordinal());
                                    if (isNotEmpty(makerCell)) {
                                        if (isNotEmpty(makerCell) && makerCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setMaker(makerCell.getStringCellValue());
                                        } else {
                                            position.setMaker("");
                                        }
                                    }
                                }

                                if (isNotBlank(fvProductName)) {
                                    XSSFCell fvProductNameCell = row.getCell(ExcelColumns.valueOf(fvProductName).ordinal());
                                    if (isNotEmpty(fvProductNameCell)) {
                                        if (isNotEmpty(fvProductNameCell) && fvProductNameCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setFvProductName(fvProductNameCell.getStringCellValue());
                                        } else {
                                            position.setFvProductName("");
                                        }
                                    }
                                }

                                if (isNotBlank(fvVendorCode)) {
                                    XSSFCell fvVendorCodeCell = row.getCell(ExcelColumns.valueOf(fvVendorCode).ordinal());
                                    if (isNotEmpty(fvVendorCodeCell)) {
                                        if (isNotEmpty(fvVendorCodeCell) && fvVendorCodeCell.getCellType() == CELL_TYPE_STRING) {
                                            position.setFvVendorCode(fvVendorCodeCell.getStringCellValue());
                                        } else if (isNotEmpty(fvVendorCodeCell) && fvVendorCodeCell.getCellType() == CELL_TYPE_NUMERIC) {
                                            position.setFvVendorCode(BigDecimal.valueOf(fvVendorCodeCell.getNumericCellValue()).toPlainString());
                                        } else {
                                            position.setFvVendorCode("");
                                        }
                                    }
                                }

                                position.setProvider(curProvider);
                                positionRepository.save(position);
                            }
                        }
                    }
                }

            }
        }
        return "main";
    }

    @GetMapping("testingMatch")
    public String getTestingMatch() {
        return "testingMatch";
    }
}
