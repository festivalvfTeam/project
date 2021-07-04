package ru.vinotekavf.vinotekaapp.services;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vinotekavf.vinotekaapp.entities.Position;
import ru.vinotekavf.vinotekaapp.entities.Provider;
import ru.vinotekavf.vinotekaapp.repos.PositionRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Service
public class PositionService {
    @Autowired
    private PositionRepository positionRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void save(Position position) {
        positionRepository.save(position);
    }

    public List<Position> findAll() {
        return positionRepository.findAll();
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
