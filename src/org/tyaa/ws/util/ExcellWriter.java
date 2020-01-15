/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.tyaa.ws.util.model.DayTaskItem;
import org.tyaa.ws.util.model.ShopPricesItem;

/**
 *
 * @author Юрий
 */
public class ExcellWriter
{
    private final String FILE_PATH_BASE =
        "D:/WaterSalesDoc/DayTasks/DayTask_";
    private final String FILE_PATH_SHOP_PRICES_BASE =
        "D:/WaterSalesDoc/ShopPrices/ShopPrice_";
    private Calendar mCalendar;
    
    public void writeDayTask(List<DayTaskItem> _dayTaskList) throws IOException{
    
        mCalendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        //try (Workbook book = new HSSFWorkbook()) {
        try (Workbook book = new XSSFWorkbook()) {
            
            Sheet sheet = book.createSheet("Задания на день");
            
            // Строка заголовка
            Row mainHeaderRow = sheet.createRow(0);
            
            CellStyle rowStyle = book.createCellStyle();
            rowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            rowStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
            
            Cell name = mainHeaderRow.createCell(0);
            name.setCellValue("Задания");
            
            Cell birthdate = mainHeaderRow.createCell(1);
            
            DataFormat format = book.createDataFormat();
            CellStyle dateStyle = book.createCellStyle();
            dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
            birthdate.setCellStyle(dateStyle);
            
            birthdate.setCellValue(Date.from(Instant.now()));
            
            //Строка заголовков колонок
            Row headersRow = sheet.createRow(1);
            
            Cell nameHeader = headersRow.createCell(0);
            nameHeader.setCellValue("Магазин");
            nameHeader.setCellStyle(rowStyle);
            
            Cell addressHeader = headersRow.createCell(1);
            addressHeader.setCellValue("Адрес");
            addressHeader.setCellStyle(rowStyle);
            /*Cell phoneHeader = headersRow.createCell(2);
            phoneHeader.setCellValue("Телефон");*/
            
            Cell barrelHeader = headersRow.createCell(2);
            barrelHeader.setCellValue("Бочка");
            barrelHeader.setCellStyle(rowStyle);
            /*Cell levelHeader = headersRow.createCell(4);
            levelHeader.setCellValue("Срочность");*/
            
            Cell needCleanHeader = headersRow.createCell(3);
            needCleanHeader.setCellValue("Мойка");
            needCleanHeader.setCellStyle(rowStyle);
            /*Cell noticeHeader = headersRow.createCell(6);
            noticeHeader.setCellValue("Примечание");*/
            
            Cell counterHeader = headersRow.createCell(4);
            counterHeader.setCellValue("Счетчик");
            counterHeader.setCellStyle(rowStyle);
            
            Cell priceHeader = headersRow.createCell(5);
            priceHeader.setCellValue("Цена");
            priceHeader.setCellStyle(rowStyle);
            
            Cell shopDebtHeader = headersRow.createCell(6);
            shopDebtHeader.setCellValue("Долг");
            shopDebtHeader.setCellStyle(rowStyle);
            
            //Строки данных
            int dataRowIdx = 2;
            for (DayTaskItem dayTask : _dayTaskList) {
                
                Row dataRow = sheet.createRow(dataRowIdx);
                
                Cell nameData = dataRow.createCell(0);
                nameData.setCellValue(dayTask.getShopName());
                
                Cell addressData = dataRow.createCell(1);
                addressData.setCellValue(dayTask.getShopAddress());
                
                /*Cell phoneData = dataRow.createCell(2);
                phoneData.setCellValue(dayTask.getShopPhone());*/
                
                Cell barrelData = dataRow.createCell(2);
                barrelData.setCellValue(dayTask.getBarrelInfo());
                
                /*Cell levelData = dataRow.createCell(4);
                String levelString = "";
                //Если уровень срочности "Срочно!"
                if (dayTask.getLevel() == 2) {
                    
                    levelString = "Срочно!";
                //Иначе, если уровень "Проверить"
                } else if (dayTask.getLevel() == 3) {
                
                    levelString = "Впервые";
                }
                levelData.setCellValue(levelString);*/
                
                Cell needCleanData = dataRow.createCell(3);
                String needCleanString = "";
                if (dayTask.isNeedClean()) {
                    
                    needCleanString = "Да";
                }
                needCleanData.setCellValue(needCleanString);
                
                Cell counterData = dataRow.createCell(4);
                counterData.setCellValue(dayTask.getCounter());
                
                Cell priceData = dataRow.createCell(5);
                priceData.setCellValue(dayTask.getPrice().doubleValue());
                
                Cell debtData = dataRow.createCell(6);
                debtData.setCellValue(dayTask.getShopDebt().doubleValue());
                
                dataRowIdx++;
            }
            
            // Меняем размер столбцов
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            
            // Записываем книгу в файл
            
            //try{
                
                File directory1 = new File(String.valueOf("D:/WaterSalesDoc"));
                if (!directory1.exists()){
                    directory1.mkdir();
                }
                
                File directory2 = new File(String.valueOf("D:/WaterSalesDoc/DayTasks"));
                if (!directory2.exists()){
                    directory2.mkdir();
                }
                
            try (FileOutputStream fileOutputStream = new FileOutputStream(
                    FILE_PATH_BASE
                            + simpleDateFormat.format(mCalendar.getTime())
                            + ".xlsx"
            )) {
                
                book.write(fileOutputStream);
            }
                
                //System.out.println("Невозможно создать директорию");
            //}*/
        } /*catch (IOException ex) {
            Logger.getLogger(ExcellWriter.class.getName()).log(Level.SEVERE, null, ex); /*catch (IOException ex) {
            Logger.getLogger(ExcellWriter.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    public void writeShopPrices(List<ShopPricesItem> _shopPricesList) throws IOException{
    
        mCalendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        try (Workbook book = new XSSFWorkbook()) {
            
            Sheet sheet = book.createSheet("Магазины-цены");
            
            // Строка заголовка
            Row mainHeaderRow = sheet.createRow(0);
            
            CellStyle rowStyle = book.createCellStyle();
            rowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            rowStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
            
            Cell name = mainHeaderRow.createCell(0);
            name.setCellValue("Цены");
            
            Cell birthdate = mainHeaderRow.createCell(1);
            
            DataFormat format = book.createDataFormat();
            CellStyle dateStyle = book.createCellStyle();
            dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
            birthdate.setCellStyle(dateStyle);
            
            birthdate.setCellValue(Date.from(Instant.now()));
            
            //Строка заголовков колонок
            Row headersRow = sheet.createRow(1);
            
            Cell nameHeader = headersRow.createCell(0);
            nameHeader.setCellValue("Магазин");
            nameHeader.setCellStyle(rowStyle);
            
            Cell barrelHeader = headersRow.createCell(1);
            barrelHeader.setCellValue("Бочка");
            barrelHeader.setCellStyle(rowStyle);
            
            Cell priceHeader = headersRow.createCell(2);
            priceHeader.setCellValue("Цена");
            priceHeader.setCellStyle(rowStyle);
            
            //Строки данных
            int dataRowIdx = 2;
            for (ShopPricesItem shopPrice : _shopPricesList) {
                
                Row dataRow = sheet.createRow(dataRowIdx);
                
                Cell nameData = dataRow.createCell(0);
                nameData.setCellValue(shopPrice.getShopName());
                
                Cell barrelData = dataRow.createCell(1);
                barrelData.setCellValue(shopPrice.getBarrelInfo());
                
                Cell priceData = dataRow.createCell(2);
                priceData.setCellValue(shopPrice.getPrice().doubleValue());
                
                dataRowIdx++;
            }
            
            // Меняем размер столбцов
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            
            // Записываем книгу в файл
            
            //try{
                
                File directory1 = new File(String.valueOf("D:/WaterSalesDoc"));
                if (!directory1.exists()){
                    directory1.mkdir();
                }
                
                File directory2 = new File(String.valueOf("D:/WaterSalesDoc/ShopPrices"));
                if (!directory2.exists()){
                    directory2.mkdir();
                }
                
            try (FileOutputStream fileOutputStream = new FileOutputStream(
                    FILE_PATH_SHOP_PRICES_BASE
                            + simpleDateFormat.format(mCalendar.getTime())
                            + ".xlsx"
            )) {
                
                book.write(fileOutputStream);
            }
        } /*catch (IOException ex) {
            Logger.getLogger(ExcellWriter.class.getName()).log(Level.SEVERE, null, ex); /*catch (IOException ex) {
            Logger.getLogger(ExcellWriter.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}
