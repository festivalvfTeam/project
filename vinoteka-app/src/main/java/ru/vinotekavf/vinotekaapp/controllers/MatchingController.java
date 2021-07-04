package ru.vinotekavf.vinotekaapp.controllers;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.vinotekavf.vinotekaapp.entities.Provider;
import ru.vinotekavf.vinotekaapp.enums.ExcelColumns;
import ru.vinotekavf.vinotekaapp.services.PositionService;
import ru.vinotekavf.vinotekaapp.services.ProviderService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Controller
public class MatchingController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private PositionService positionService;

    @Autowired
    private ProviderService providerService;

    public Path writeInDirectoryAndGetPath(MultipartFile file) {
        File uploadDirectory = new File(uploadPath);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdir();
        }
        Path filepath = Paths.get(uploadPath, file.getOriginalFilename());
        try {
            file.transferTo(filepath);
            //file.transferTo(new File(uploadPath + "/" + file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filepath;
    }

    @PostMapping("/match_provider")
    public String matchProvider(@RequestParam("providerName") String providerName,
                                @RequestParam("phone") String phone,
                                @RequestParam("managerName") String managerName
    ) {
        providerService.save(new Provider(providerName, phone, managerName));
        return "main";
    }

    @PostMapping("match_positions/{id_provider}")
    public String matchProducts(@PathVariable Long id_provider,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam("productName") String productName,
                                @RequestParam("vendorCode") String vendorCode,
                                @RequestParam("price") String price,
                                @RequestParam("promotionalPrice") String promotionalPrice,
                                @RequestParam("remainder") String remainder,
                                @RequestParam("volume") String volume,
                                @RequestParam("releaseYear") String releaseYear,
                                @RequestParam("maker") String maker
        ) throws IOException {

        if (isNotEmpty(file)) {

            Integer[] productNameCols = Arrays.stream(productName.split(",")).map(str -> {
                if (StringUtils.isNotBlank(str))
                    return ExcelColumns.valueOf(str).ordinal();
                return -1;
            }).toArray(Integer[]::new);
            Integer[] vendorCodeCols = Arrays.stream(vendorCode.split(",")).map(str -> {
                if (StringUtils.isNotBlank(str))
                    return ExcelColumns.valueOf(str).ordinal();
                return -1;
            }).toArray(Integer[]::new);
            Integer[] priceCols = Arrays.stream(price.split(",")).map(str -> {
                if (StringUtils.isNotBlank(str))
                    return ExcelColumns.valueOf(str).ordinal();
                return -1;
            }).toArray(Integer[]::new);
            Integer[] promotionalPriceCols = Arrays.stream(promotionalPrice.split(",")).map(str -> {
                if (StringUtils.isNotBlank(str))
                    return ExcelColumns.valueOf(str).ordinal();
                return -1;
            }).toArray(Integer[]::new);
            Integer[] remainderCols = Arrays.stream(remainder.split(",")).map(str -> {
                if (StringUtils.isNotBlank(str))
                    return ExcelColumns.valueOf(str).ordinal();
                return -1;
            }).toArray(Integer[]::new);
            Integer[] volumeCols = Arrays.stream(volume.split(",")).map(str -> {
                if (StringUtils.isNotBlank(str))
                    return ExcelColumns.valueOf(str).ordinal();
                return -1;
            }).toArray(Integer[]::new);
            Integer[] releaseYearCols = Arrays.stream(releaseYear.split(",")).map(str -> {
                if (StringUtils.isNotBlank(str))
                    return ExcelColumns.valueOf(str).ordinal();
                return -1;
            }).toArray(Integer[]::new);
            Integer[] makerCols = Arrays.stream(maker.split(",")).map(str -> {
                if (StringUtils.isNotBlank(str))
                    return ExcelColumns.valueOf(str).ordinal();
                return -1;
            }).toArray(Integer[]::new);

            Path path = writeInDirectoryAndGetPath(file);
            Provider provider = providerService.getProviderById(id_provider);

            file.transferTo(new File(uploadPath + "/" + file.getOriginalFilename()));
            if (file.getOriginalFilename().contains("xlsx") || file.getOriginalFilename().contains("xlsm")) {
                XSSFWorkbook book = new XSSFWorkbook(new FileInputStream(uploadPath + "/" + file.getOriginalFilename()));
            } else if (file.getOriginalFilename().contains("xls")) {
                HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(uploadPath + "/" + file.getOriginalFilename()));
            } else if (file.getOriginalFilename().contains(".csv")) {
                positionService.readCSVAndWriteInDb(path.toString(), "windows-1251", provider,
                        vendorCodeCols, productNameCols, volumeCols, releaseYearCols, priceCols, promotionalPriceCols, remainderCols, makerCols);
            }
        }
        return "testingMatch";
    }

    @GetMapping("testingMatch")
    public String getTestingMatch() {
        return "testingMatch";
    }


}
