package com.metacube.sageclarity.predictable.service.impl;

import com.metacube.sageclarity.predictable.service.XLSDataReader;
import com.metacube.sageclarity.predictable.util.ApplicationUtil;
import com.metacube.sageclarity.predictable.vo.DataUploadRowVO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApachePoiXLSDataReader implements XLSDataReader {
    private static final Logger logger = LoggerFactory.getLogger(ApachePoiXLSDataReader.class);

    @Override
    public DataUploadRowVO readData(InputStream inputStream) {
        DataUploadRowVO dataUploadRowsVO=null;
        try {
            Workbook workBook = WorkbookFactory.create(inputStream);
           // XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
            if(workBook != null){
                int sheetIndex = 0;
                Sheet sheetData = workBook.getSheetAt(sheetIndex);
                if(sheetData!=null)
                    dataUploadRowsVO = readSheet(sheetData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Failed to import Data", ex);
        }
        return dataUploadRowsVO;
    }

    private DataUploadRowVO readSheet(Sheet sheet){
        List<String> sheetHeaders = new ArrayList<>();
        List<List<Object>> rows = new ArrayList<List<Object>>();
        DataUploadRowVO dataUploadRowsVO = new DataUploadRowVO();

        int noOfRows = sheet.getPhysicalNumberOfRows();
        boolean firstrow = Boolean.TRUE;
        int cells = 0;
        for (int r = 0; r < noOfRows; r++) {
            Row row = sheet.getRow(r);

            if (row == null) {
                continue;
            }

            if(firstrow){
                firstrow = Boolean.FALSE;
                cells = row.getLastCellNum();
                for(int c = 0; c < cells; c++){
                    Cell cell = row.getCell(c);
                    sheetHeaders.add(cell.getStringCellValue().trim());
                }
                dataUploadRowsVO.setListOfHeaders(sheetHeaders);
                continue;
            }
            if(cells <= 0 )
                continue;

            List<Object> cols = new ArrayList<Object>();
            rows.add(cols);
            //retrieve cells
            for (int c = 0; c < cells; c++) {
                Cell cell = row.getCell(c,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                if(cell==null){
                    cols.add("");
                    continue;
                }
                SimpleDateFormat sdf = null;
                Object value = "";
                switch (cell.getCellType()) {
                    case FORMULA:
                        value = cell.getCellFormula();
                        break;

                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            value = cell.getDateCellValue();
                           // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
                            //LocalDateTime localDateTime = LocalDateTime.parse(time1);
                           // System.out.println(localDateTime.format(formatter));
                            sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                            value = sdf.format(cell.getDateCellValue());

                        } else {
                            value =  cell.getNumericCellValue();
//                            if(value instanceof Long){
//                                value = ApplicationUtil.getLong(value);
//                            }
                           // value = ApplicationUtil.getLong(value);
                        }
                        break;

                    case STRING:
                        value = cell.getStringCellValue();

                        break;

                    case BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;

                    default:
                }
                if(value instanceof String){
                    value = ((String)value).trim();
                }
                cols.add(value);}
        }
        dataUploadRowsVO.setRowOfDataList(rows);
        return dataUploadRowsVO;
    }
}
