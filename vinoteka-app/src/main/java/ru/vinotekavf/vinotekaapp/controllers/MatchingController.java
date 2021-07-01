package ru.vinotekavf.vinotekaapp.controllers;


import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class MatchingController {

    @Value("${upload.path}")
    private String uploadPath;

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

        if (ObjectUtils.isNotEmpty(file.getOriginalFilename())) {
            File uploadDirectory = new File(uploadPath);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdir();
            }
            file.transferTo(new File(uploadPath + "/" + file.getOriginalFilename()));
            if (file.getOriginalFilename().contains("xlsx") || file.getOriginalFilename().contains("xlsm")) {
                XSSFWorkbook book = new XSSFWorkbook(new FileInputStream(uploadPath + "/" + file.getOriginalFilename()));
            } else if (file.getOriginalFilename().contains("xls")) {
                HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(uploadPath + "/" + file.getOriginalFilename()));
            } else if (file.getOriginalFilename().contains("csv")) {
                //Todo csv handling
            }

        }
        return "main";
    }
}
