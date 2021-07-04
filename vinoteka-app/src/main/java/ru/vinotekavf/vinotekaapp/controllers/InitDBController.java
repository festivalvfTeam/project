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
import ru.vinotekavf.vinotekaapp.utils.ControllerUtils;
import ru.vinotekavf.vinotekaapp.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            ControllerUtils.writeInDirectoryAndGetPath(file, uploadPath);

            if (file.getOriginalFilename().contains("xlsx") || file.getOriginalFilename().contains("xlsm")) {
                XSSFWorkbook book = new XSSFWorkbook(new FileInputStream(uploadPath + "/" + file.getOriginalFilename()));
                XSSFSheet sheet = book.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();
                while (rowIterator.hasNext()) {

                    XSSFRow row = (XSSFRow) rowIterator.next();

                    if (isNotBlank(provider)) {
                        XSSFCell providerCell = row.getCell(ExcelColumns.valueOf(provider).ordinal());
                        if (isNotEmpty(providerCell)) {
                            Provider curProvider = new Provider();
                            Position position = new Position();

                            if (ObjectUtils.isEmpty(providerRepository.findByName(providerCell.getStringCellValue()))) {
                                if (isNotEmpty(providerCell) && providerCell.getCellType() == CELL_TYPE_STRING) {
                                    curProvider.setName(providerCell.getStringCellValue());
                                } else {
                                    curProvider.setName("");
                                }
                            } else {
                                curProvider = providerRepository.findByName(providerCell.getStringCellValue());
                            }

                            curProvider.setPhone(FileUtils.getValueFromXLSXColumn(phone, row));
                            curProvider.setManagerName(FileUtils.getValueFromXLSXColumn(managerName, row));

                            position.setProductName(FileUtils.getValueFromXLSXColumn(productName, row));
                            position.setVendorCode(FileUtils.getValueFromXLSXColumn(vendorCode, row));
                            position.setPrice(FileUtils.getValueFromXLSXColumn(price, row));
                            position.setPromotionalPrice(FileUtils.getValueFromXLSXColumn(promotionalPrice, row));
                            position.setRemainder(FileUtils.getValueFromXLSXColumn(remainder, row));
                            position.setVolume(FileUtils.getValueFromXLSXColumn(volume, row));
                            position.setReleaseYear(FileUtils.getValueFromXLSXColumn(releaseYear, row));
                            position.setMaker(FileUtils.getValueFromXLSXColumn(maker, row));
                            position.setFvProductName(FileUtils.getValueFromXLSXColumn(fvProductName, row));
                            position.setFvVendorCode(FileUtils.getValueFromXLSXColumn(fvVendorCode, row));

                            providerRepository.save(curProvider);
                            position.setProvider(curProvider);
                            positionRepository.save(position);
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
