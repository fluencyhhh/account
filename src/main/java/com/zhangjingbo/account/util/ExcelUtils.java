package com.zhangjingbo.account.util;

import com.zhangjingbo.account.entity.AccountInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.io.*;
import java.util.*;

@Component
public class ExcelUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    public static void buildExcel(SXSSFWorkbook workbook, String[] titles, String sheetName, int sheetNum, List<Map<String, Object>> list,File csvFile) throws Exception {
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(sheetName);
        Row sheetRow = sheet.createRow(0);
        if (titles != null) {
            for (int i = 0; i < titles.length; i++) {
                sheetRow.createCell(i).setCellValue(titles[i]);
            }
        }
        if (list != null && list.size() > 0) {
            logger.info("===buildExcel====22222222222222========="+list.size());
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                Row listRow = sheet.createRow(i + 1);
                int num = 0;
                for (String key : map.keySet()) {
                    listRow.createCell(num++).setCellValue(String.valueOf(map.get(key)));
                }
            }
            list.clear();
        }
        logger.info("===buildExcel====33333333333333333=========");
        writeToFile(workbook, csvFile);

    }

    public static void createExcel(SXSSFWorkbook workbook, String filePath, String fileName) {
        try {
            FileOutputStream fout = new FileOutputStream(filePath + fileName);
            workbook.write(fout);
            fout.flush();
            fout.close();
            workbook.dispose();
            Process pro = Runtime.getRuntime().exec("chmod 777 " + filePath + fileName);
            pro.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }




    /**
     * ??????????????????
     *
     * @param in
     * @param fileName
     * @return
     */
    private static Workbook getWorkbook(InputStream in, String fileName) throws Exception {


        Workbook book = null;
        String filetype = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(filetype)) {
            book = new HSSFWorkbook(in);
        } else if (".xlsx".equals(filetype)) {
            book = new XSSFWorkbook(in);
        } else {
            throw new Exception("?????????excel?????????");
        }


        return book;
    }

    /**
     * ??????excel???????????????sheet??????????????????????????????
     *
     * @param fileName
     * @param titles
     * @param dataList
     * @return
     */
    public static void toExport(String filePath, String fileName, final String[] titles,
            final List<String[]> dataList) {
        try {
            toExport(filePath, fileName, null, titles, null, dataList);
        } catch (Exception e) {
            logger.error("??????Excel???????????????", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * ????????????excel??????
     *
     * @return
     */
    private static File createFile(String filePath, String fileName) {
        if (StringUtils.isBlank(fileName)) {
            synchronized (ExcelUtils.class) {
                fileName = new Long(System.currentTimeMillis()).toString();
            }
        }
        File excelFile = new File(filePath + fileName);
        if (excelFile != null && !excelFile.exists()) {
            try {
                File fileParent = excelFile.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                excelFile.createNewFile();
            } catch (IOException e) {
                logger.error("??????Excel???????????????", e);
            }
        }
        return excelFile;
    }

    /**
     * ??????sheet???
     *
     * @param workbook
     * @param sheetName
     * @return
     */
    private static Sheet createSheet(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);
        return sheet;
    }

    /**
     * ????????????
     *
     * @param workbook
     * @param sheet
     * @param titles
     */
    private static void setTitleCells(Workbook workbook, Sheet sheet, String[] titles, int[] titlesWidth) {
        if (workbook != null) {
            if (titlesWidth == null || titlesWidth.length != titles.length) {
                titlesWidth = new int[titles.length];
                for (int i = 0; i < titles.length; i++) {
                    titlesWidth[i] = 20;
                }
            }

            Row header = sheet.createRow(0);// ???????????????????????????0??????
            //			CellStyle style = getTitleCellStyle(workbook);
            for (int i = 0; i < titles.length; i++) {
                sheet.setColumnWidth(i, titlesWidth[i] * 256);// in units of 1/256th of a character width),?????????20?????????
                Cell cell = header.createCell(i);
                //				cell.setCellStyle(style);
                cell.setCellValue(titles[i]);
            }
        }
    }



    /**
     * ??????????????????
     *
     * @param workbook
     * @param sheet
     * @param dataList
     */
    private static void setDataCells(Workbook workbook, Sheet sheet, List<String[]> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(i + 1);// ???????????????
            String[] data = dataList.get(i);
            for (int j = 0; j < data.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(data[j]);
            }
        }
    }


    private static String divide(String num, String row) {
        String orderRate = "";
        if (StringUtils.isEmpty(num) || "0".equals(num)) {
            return orderRate;
        } else {
            row = StringUtils.isEmpty(row) ? "0" : row;
            Integer numint = Integer.parseInt(num);
            Integer rowint = Integer.parseInt(row);
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2); //???????????????????????????????????????????????????
            df.setMinimumFractionDigits(2); //???????????????????????????????????????????????????
            orderRate = df.format(rowint * 100.00 / numint) + "%";
        }
        return orderRate;
    }

    /**
     * ?????????????????????excel?????????
     *
     * @param workbook
     * @return
     */
    public static void writeToFile(Workbook workbook, File excelFile) {
        FileOutputStream fileOut = null;
        logger.info("===buildExcel====6666666666666=========");
        try {
            fileOut = new FileOutputStream(excelFile);
            workbook.write(fileOut);
            logger.info("===buildExcel====999999999999999999=========");
        } catch (FileNotFoundException e) {
            logger.error("Excel??????????????????");
        } catch (IOException e) {
            logger.error("???????????????Excel???????????????");
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.flush();
                    fileOut.close();
                } catch (IOException e) {
                    logger.error("??????????????????????????????");
                }
            }
        }
    }

    /**
     * ??????Excel, fileName????????????????????????????????????????????????????????????
     * sheetName???????????????fileName?????????????????????fileName??????sheet?????????????????????????????????sheet?????????
     *
     * @param fileName  excel?????????
     * @param sheetName excel sheet?????????
     * @param titles    sheet???????????????
     * @param dataList  sheet????????????????????????title
     * @return ??????excel???????????????????????????????????????
     */
    public static void toExport(String filePath, String fileName, String sheetName, final String[] titles,
            final int[] titlesWidth, final List<String[]> dataList) {
        FileInputStream myStream = null;
        try {
            //            Workbook workbook = new HSSFWorkbook(); // or new XSSFWorkbook();
            Workbook workbook = null;
            String filetype = fileName.substring(fileName.lastIndexOf("."));
            if (".xls".equals(filetype)) {
                workbook = new HSSFWorkbook();
            } else if (".xlsx".equals(filetype)) {
                workbook = new XSSFWorkbook();
            } else {
                workbook = new HSSFWorkbook();
            }

            if (fileName != null && sheetName == null) {
                sheetName = fileName;
            }
            Sheet sheet = createSheet(workbook, sheetName);
            // ??????
            setTitleCells(workbook, sheet, titles, titlesWidth);

            setDataCells(workbook, sheet, dataList);

            File excelFile = createFile(filePath, fileName);

            if (excelFile != null) {
                writeToFile(workbook, excelFile);
            }
        } catch (Exception e) {
            logger.error("??????Excel???????????????", e);
            throw new RuntimeException(e);
        } finally {
            if (myStream != null) {
                try {
                    myStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 1??????????????????????????????????????????
     *
     * @param cell ?????????
     * @return ???????????????????????????
     */
    private static String convertCellValueToString(Cell cell) {
        //1.1???????????????????????????????????????
        if (cell == null) {
            return null;
        }
        //1.2????????????????????????????????????
        String cellValue = null;
        //1.3?????????????????????????????????
        switch (cell.getCellType()) {
            case NUMERIC:
                //1.3.1????????????????????????????????????
                short dataFormat = cell.getCellStyle().getDataFormat();
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = null;
                    //1.3.1.1?????????????????????????????????????????????????????????
                    switch (dataFormat) {
                        case 14:
                            sdf = new SimpleDateFormat("yyyy/MM/dd");
                            break;
                        case 21:
                            sdf = new SimpleDateFormat("HH:mm:ss");
                            break;
                        case 22:
                            sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            break;
                    }
                    //1.3.1.2?????????????????????
                    Date date = cell.getDateCellValue();
                    assert sdf != null;
                    cellValue = sdf.format(date);
                } else if (dataFormat == 0) {
                    //1.3.2???????????????????????????
                    DecimalFormat format = new DecimalFormat("0");
                    double numericCellValue = cell.getNumericCellValue();
                    cellValue = format.format(numericCellValue);
                }
                break;
            case STRING:
                //?????????????????????
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                //??????????????????
                boolean booleanCellValue = cell.getBooleanCellValue();
                cellValue = Boolean.toString(booleanCellValue);
                break;
            case FORMULA:
                //??????????????????
                cellValue = cell.getCellFormula();
                break;
            case ERROR:
                byte errorCellValue = cell.getErrorCellValue();
                cellValue = Byte.toString(errorCellValue);
                break;
            default:
                break;
        }
        return cellValue;
    }

    /**
     * 2???????????????????????????????????????
     *
     * @param sheet ?????????
     * @return ???????????????????????????????????????
     */
    public static List<CellRangeAddress> getCombineCell(Sheet sheet) {
        List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
        //2.1??????????????? sheet ???????????????????????????
        int sheetMergerCount = sheet.getNumMergedRegions();
        //2.2????????????????????????
        for (int i = 0; i < sheetMergerCount; i++) {
            //2.2.1??????????????????????????????list???
            CellRangeAddress rangeAddress = sheet.getMergedRegion(i);
            list.add(rangeAddress);
        }
        return list;
    }

    /**
     * 3??????????????????????????????????????????
     *
     * @param listCombineCell ????????????????????????list
     * @param cell            ????????????????????????
     * @param sheet           sheet
     */
    public static String isCombineCell(List<CellRangeAddress> listCombineCell, Cell cell, Sheet sheet) {
        //3.1?????????????????????????????????????????????????????????
        int firstColumn = 0;
        int lastColumn = 0;
        //3.2???????????????????????????????????????????????????
        int firstRow = 0;
        int lastRow = 0;
        //3.3????????????????????????
        String cellValue = null;
        for (CellRangeAddress rangeAddress : listCombineCell) {
            //3.3.1????????????????????????????????????, ?????????, ?????????, ?????????
            firstColumn = rangeAddress.getFirstColumn();
            lastColumn = rangeAddress.getLastColumn();
            firstRow = rangeAddress.getFirstRow();
            lastRow = rangeAddress.getLastRow();
            //3.3.2?????????????????????????????????
            if (cell.getRowIndex() >= firstRow && cell.getRowIndex() <= lastRow) {
                if (cell.getColumnIndex() >= firstColumn && cell.getColumnIndex() <= lastColumn) {
                    //3.3.2.1??????????????????
                    Row fRow = sheet.getRow(firstRow);
                    //3.3.2.2????????????????????????
                    Cell fCell = fRow.getCell(firstColumn);
                    //3.3.2.3???????????????????????????????????????????????????
                    cellValue = convertCellValueToString(fCell);
                    break;
                }
            } else {
                //3.3.3??????????????????????????????????????????????????????
                cellValue = convertCellValueToString(cell);
            }
        }
        //3.4????????????????????????????????????
        return cellValue;
    }

    /**
     * 4?????????sheet??????????????????????????????
     *
     * @param sheet sheet
     * @return ?????????
     */
    private static boolean hasMerged(Sheet sheet) {
        int numMergedRegions = sheet.getNumMergedRegions();
        if (numMergedRegions > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 5?????????excel????????????
     *
     * @param inputStream ?????????
     * @return ?????????
     */
    public static List<AccountInfo> importExcel(InputStream inputStream) {
        //5.1?????????????????????????????????Object??????
        List<AccountInfo> list = new ArrayList<>();
        try {
            //5.2??????????????????
            Workbook workbook = WorkbookFactory.create(inputStream);
            //5.3????????????????????????sheet?????????
            int sheetNum = workbook.getNumberOfSheets();
            //5.4??????????????????sheet
            for (int i = 0; i < sheetNum; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                //5.4.1?????????sheet?????????????????????
                int rows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < rows; j++) {
                    //5.4.1.1????????????????????????????????????????????????????????????
                    if (i == 1 || j == 0) {
                        continue;
                    }
                    //5.4.1.2???????????????????????????
                    Row row = sheet.getRow(j);
                    if (row == null) {
                        System.out.println("row is null");
                    } else {
                        //5.4.1.3?????????????????????????????????????????????
                        int cells = row.getPhysicalNumberOfCells();
                        //5.4.1.4???????????????Object??????????????????????????????????????????
                        AccountInfo accountInfo = new AccountInfo();
                        //5.4.1.5?????????????????????????????????
                        int index = 0;
                        //5.4.1.6??????????????????????????????????????????
                        Cell cell = row.getCell(0);
                        Date accountTime = null;
                        try {
                            accountTime = (cell.getDateCellValue()) ;
                        }catch (Exception e){
                            accountTime = DateUtils.stringToDate(cell.getStringCellValue()) ;
                        }
                        accountInfo.setAccountTime(accountTime);
                        cell = row.getCell(1);
                        String accountName = cell.getStringCellValue();
                        accountInfo.setAccountName(accountName);
                        cell = row.getCell(2);
                        String accountItem = cell.getStringCellValue();
                        accountInfo.setAccountItem(accountItem);
                        cell = row.getCell(3);
                        String itemDetail = cell.getStringCellValue();
                        accountInfo.setItemDetail(itemDetail);
                        cell = row.getCell(4);
                        String itemName = cell.getStringCellValue();
                        accountInfo.setItemName(itemName);
                        cell = row.getCell(5);
                        String operator = cell.getStringCellValue();
                        accountInfo.setOperator(operator);
                        cell = row.getCell(6);
                        String AccountType = cell.getStringCellValue();
                        accountInfo.setAccountType(AccountType);
                        cell = row.getCell(7);
                        String accountVoucher = cell.getStringCellValue();
                        accountInfo.setAccountVoucher(accountVoucher);
                        cell = row.getCell(8);
                        String accountNumber = cell.getStringCellValue();
                        accountInfo.setAccountNumber(accountNumber);
                        cell = row.getCell(9);
                        Integer accountDebit = Integer.valueOf(cell.getStringCellValue());
                        accountInfo.setAccountDebit(new BigDecimal(accountDebit));
                        cell = row.getCell(10);
                        Integer accountCredit = Integer.valueOf(cell.getStringCellValue());
                        accountInfo.setAccountCredit(new BigDecimal(accountCredit));
                        cell = row.getCell(11);
                        Integer balance = Integer.valueOf(cell.getStringCellValue());
                        accountInfo.setBalance(new BigDecimal(balance));
//                        for (int k = 0; k < cells; k++) {
//                            //5.4.1.6.1????????????????????????????????????
//                            Cell cell = row.getCell(k);
//                            //5.4.1.6.2???????????????sheet???????????????????????????
//                            boolean b = hasMerged(sheet);
//                            if (b) {
//                                //5.4.1.6.2.1?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                                List<CellRangeAddress> listCombineCell = getCombineCell(sheet);
//                                String combineCell = isCombineCell(listCombineCell, cell, sheet);
//                                //5.4.1.6.2.1.2????????????????????????????????????
//                                objects[index] = combineCell;
//                            } else {
//                                String cellValueToString = convertCellValueToString(cell);
//                                objects[index] = cellValueToString;
//                            }
//                            //5.4.1.6.3???????????????
//                            index++;
//                        }
                        //5.4.1.7??????????????????????????????????????????list????????????
                        list.add(accountInfo);
                    }
                }
            }
            logger.info("???????????????????????????");
        } catch (Exception e) {
            logger.info("???????????????????????????");
            e.printStackTrace();
            return null;
        };
        //5.5?????????List??????
        return list;
    }
}
