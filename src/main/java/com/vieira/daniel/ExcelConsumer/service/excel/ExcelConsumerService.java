package com.vieira.daniel.ExcelConsumer.service.excel;

import com.vieira.daniel.ExcelConsumer.exceptions.InvalidHeaderException;
import com.vieira.daniel.ExcelConsumer.model.ExcelConsumer;
import com.vieira.daniel.ExcelConsumer.repository.ExcelConsumerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
public class ExcelConsumerService {

    @Autowired
    ExcelReader excelService;

    @Autowired
    ExcelConsumerRepository repository;

    @Transactional
    @SuppressWarnings("unchecked")
    public void process(Path path) throws InvalidHeaderException, Exception {
        try{
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(path.toFile()));
            log.info("Start process...");
            log.info("Archive: {}", path.getFileName());

            Sheet sheet = workbook.getSheet("PolicyData");


            List<ExcelConsumer> list = excelService.load(sheet);

            repository.deleteAll();
            log.info("Finishing process, Saving data...");
            repository.saveAll(list);
            log.info("Done!");
        }catch(Exception e){
            log.error("Error while read excel.");
        }

    }
}
