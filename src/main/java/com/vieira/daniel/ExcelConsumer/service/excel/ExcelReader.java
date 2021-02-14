package com.vieira.daniel.ExcelConsumer.service.excel;

import com.vieira.daniel.ExcelConsumer.exceptions.InvalidHeaderException;
import com.vieira.daniel.ExcelConsumer.model.ExcelConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ExcelReader {

    @Value("${app.spreadsheet.header.columns}")
    protected String headerColumns;

    public ExcelConsumer fillObject(Row row) {
        log.info("linha: {}", row.getRowNum());
        ExcelConsumer obj = new ExcelConsumer();

        obj.setPolicy(Objects.isNull(row.getCell(0)) ? "" : row.getCell(0).toString());
        obj.setExpiry(Objects.isNull(row.getCell(1)) ? "" : row.getCell(1).toString());
        obj.setLocation(Objects.isNull(row.getCell(2)) ? "" : row.getCell(2).toString());
        obj.setState(Objects.isNull(row.getCell(3)) ? "" : row.getCell(3).toString());
        obj.setRegion(Objects.isNull(row.getCell(4)) ? "" : row.getCell(4).toString());
        obj.setInsuredValue(Objects.isNull(row.getCell(5)) ? "" : row.getCell(5).toString());
        obj.setConstruction(Objects.isNull(row.getCell(6)) ? "" : row.getCell(6).toString());
        obj.setBusinessType(Objects.isNull(row.getCell(7)) ? "" : row.getCell(7).toString());
        obj.setEarthquake(Objects.isNull(row.getCell(8)) ? "" : row.getCell(8).toString());
        obj.setFlood(Objects.isNull(row.getCell(9)) ? "" : row.getCell(9).toString());

        return obj;
    }

    public List<ExcelConsumer> load(Sheet sheet) throws InvalidHeaderException, Exception {

        List<ExcelConsumer> objList = new ArrayList<>();

        try {
            log.info("Start Read Excel...");
            log.debug("Number of Rows to Read {}", sheet.getPhysicalNumberOfRows());
            if (validateHeader(sheet.getRow(0))) {
                Iterator<Row> iteratorRow = sheet.rowIterator();
                while (iteratorRow.hasNext()) {
                    Row row = iteratorRow.next();
                    if (row.getRowNum() > 0) { // Não carrega o header
                        ExcelConsumer objExcel = fillObject(row);
                        if (objExcel != null) {
                            objList.add(objExcel);
                        }
                    }
                }
            }
            log.info("Finish Read Excel...");
        } catch (Exception e) {
            log.error("Error while Loading excel File ", e);
            throw e;
        }
        return objList;
    }

    private boolean validateHeader(Row rowZero) throws InvalidHeaderException {
        List<String> headerFile = new ArrayList();
        List<String> headers = Arrays.asList(headerColumns.split(","));
        boolean validHeader = false;
        if (!headers.isEmpty()) {
            for (int i = 0; i < headers.size(); i++) {
                if (rowZero.getCell(i) != null) {
                    headerFile.add(rowZero.getCell(i).getStringCellValue().trim());
                }
            }
            validHeader = headers.toString().compareToIgnoreCase(headerFile.toString()) == 0;
        }


        if (!validHeader) {
            String messageError =
                    "Error: invalid header. Header is " + headerFile + " expected was " + headers;
            log.error(messageError);
            throw new InvalidHeaderException(messageError);
        }
        return validHeader;
    }


}
