package ru.vinotekavf.vinotekaapp.services;

import au.com.bytecode.opencsv.*;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vinotekavf.vinotekaapp.dto.PositionProviderDto;
import ru.vinotekavf.vinotekaapp.entities.Position;
import ru.vinotekavf.vinotekaapp.entities.Provider;
import ru.vinotekavf.vinotekaapp.repos.PositionRepository;
import ru.vinotekavf.vinotekaapp.repos.ProviderRepository;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Service
public class PositionService {
    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Value("${spring.datasource.url}")
    private String URL_DB;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String QUERY = "select pr.name, pr.phone, po.maker, po.vendor_code, po.product_name, " +
            "po.volume, po.release_year, po.price, po.remainder, po.fv_vendor_code, po.fv_product_name " +
            "from provider pr " +
            "join posit po on pr.id = po.provider_id";

    public void save(Position position) {
        Position positionFromDb = positionRepository.findByFvVendorCode(position.getFvVendorCode());

        if (isNotEmpty(positionFromDb)) {
            positionFromDb.setProvider(position.getProvider());
            positionFromDb.setVendorCode(position.getVendorCode());
            positionFromDb.setProductName(position.getProductName());
            positionFromDb.setVolume(position.getVolume());
            positionFromDb.setReleaseYear(position.getReleaseYear());
            positionFromDb.setPrice(position.getPrice());
            positionFromDb.setPromotionalPrice(position.getPromotionalPrice());
            positionFromDb.setRemainder(position.getRemainder());
            positionFromDb.setMaker(position.getMaker());
            positionFromDb.setFvVendorCode(position.getFvVendorCode());
            positionFromDb.setFvProductName(position.getFvProductName());
            positionRepository.save(positionFromDb);
        } else {
            positionRepository.save(position);
        }
    }

    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    public void writeAllPositionsInCSV() {
        String CSV_FILE_PATH = "src/main/resources/files/data.csv";
        try (Connection connection = DriverManager.getConnection(URL_DB, "postgres", "5842");
             Statement stmt = connection.createStatement();
             CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(CSV_FILE_PATH), "windows-1251"), ';'))
        {
            ResultSet myResultSet = stmt.executeQuery(QUERY);
            writer.writeAll(myResultSet, true);
        } catch (SQLException | IOException exception) {
            logger.error(exception.getMessage());
        }
    }

    public void writeAllPositionsInCSVByDTO() {
        String CSV_FILE_PATH = "src/main/resources/files/data.csv";
        List<PositionProviderDto> list = positionRepository.getJoinInformation();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(CSV_FILE_PATH), "windows-1251"), ';')) {
            writer.writeNext(PositionProviderDto.getColumnsNames());
            list.forEach(x -> writer.writeNext(x.toStrArray()));
        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }
    }

    private ColumnPositionMappingStrategy<PositionProviderDto> setColumnMapping() {
        ColumnPositionMappingStrategy<PositionProviderDto> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(PositionProviderDto.class);
        String[] columns = new String[] {"providerName", "providerPhone", "maker", "vendorCode"
                , "productName", "volume", "releaseYear", "price", "remainder", "fvVendorCode", "fvProductName"};
        strategy.setColumnMapping(columns);
        return strategy;
    }

    public void readCSVAndWriteInDb(String csvFilename, String encoding, Provider provider, Integer[] vendorCodeIndexes, Integer[] productNameIndexes, Integer[] volumeIndexes,
                                    Integer[] releaseYearIndexes, Integer[] priceIndexes, Integer[] promotionalPriceIndexes, Integer[] remainderIndexes, Integer[] makerIndexes) {
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilename, Charset.forName(encoding)), ';', '\n', '"')) {
            String[] currentRow = csvReader.readNext();
            while (currentRow != null) {
                Position position = new Position();
                position.setProvider(provider);
                for (int index : vendorCodeIndexes) {
                    if (index != -1)
                        position.addVendorCode(currentRow[index]);
                }
                for (int index : productNameIndexes) {
                    if (index != -1)
                        position.addProductName(currentRow[index]);
                }
                for (int index : volumeIndexes) {
                    if (index != -1)
                        position.addVolume(currentRow[index]);
                }
                for (int index : releaseYearIndexes) {
                    if (index != -1)
                        position.addReleaseYear(currentRow[index]);
                }
                for (int index : priceIndexes) {
                    if (index != -1)
                        position.addPrice(currentRow[index]);
                }
                for (int index : promotionalPriceIndexes) {
                    if (index != -1)
                        position.addPromotionalPrice(currentRow[index]);
                }
                for (int index : remainderIndexes) {
                    if (index != -1)
                        position.addRemainder(currentRow[index]);
                }
                for (int index : makerIndexes) {
                    if (index != -1)
                        position.addMaker(currentRow[index]);
                }

                if (!position.getPrice().isEmpty() && !position.getProductName().isEmpty() && NumberUtils.isParsable(position.getPrice().replaceAll("\\s+","")))
                    save(position);

                currentRow = csvReader.readNext();
            }
        } catch (FileNotFoundException exception) {
            logger.info("File not found");
            logger.error(exception.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
