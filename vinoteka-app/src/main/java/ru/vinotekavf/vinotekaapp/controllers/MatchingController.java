package ru.vinotekavf.vinotekaapp.controllers;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.vinotekavf.vinotekaapp.entities.Provider;
import ru.vinotekavf.vinotekaapp.services.PositionService;
import ru.vinotekavf.vinotekaapp.services.ProviderService;
import ru.vinotekavf.vinotekaapp.utils.ControllerUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Controller
public class MatchingController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private PositionService positionService;

    @Autowired
    private ProviderService providerService;

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

            Integer[] productNameCols = ControllerUtils.getIntCols(productName);
            Integer[] vendorCodeCols = ControllerUtils.getIntCols(vendorCode);
            Integer[] priceCols = ControllerUtils.getIntCols(price);
            Integer[] promotionalPriceCols = ControllerUtils.getIntCols(promotionalPrice);
            Integer[] remainderCols = ControllerUtils.getIntCols(remainder);
            Integer[] volumeCols = ControllerUtils.getIntCols(volume);
            Integer[] releaseYearCols = ControllerUtils.getIntCols(releaseYear);
            Integer[] makerCols = ControllerUtils.getIntCols(maker);

            Path path = ControllerUtils.writeInDirectoryAndGetPath(file, uploadPath);
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
}
