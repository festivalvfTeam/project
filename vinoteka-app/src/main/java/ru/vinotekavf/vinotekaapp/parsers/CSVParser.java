package ru.vinotekavf.vinotekaapp.parsers;


import au.com.bytecode.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

public class CSVParser {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void read(String csvFilename, String encoding) {
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilename, Charset.forName(encoding)), ';', '\n', '"')) {
            String[] currentRow = csvReader.readNext();
            while (currentRow != null) {

            }
        } catch (FileNotFoundException exception) {
            logger.info("File not found");
            logger.error(exception.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
