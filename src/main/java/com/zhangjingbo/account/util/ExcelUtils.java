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
     * 判断文件格式
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
            throw new Exception("请上传excel文件！");
        }


        return book;
    }

    /**
     * 导出excel，文件名和sheet页名称相同使用该方法
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
            logger.error("导出Excel文件失败！", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 创建一个excel文件
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
                logger.error("创建Excel文件失败！", e);
            }
        }
        return excelFile;
    }

    /**
     * 新建sheet页
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
     * 设置标题
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

            Row header = sheet.createRow(0);// 第一行为标题行，从0开始
            //			CellStyle style = getTitleCellStyle(workbook);
            for (int i = 0; i < titles.length; i++) {
                sheet.setColumnWidth(i, titlesWidth[i] * 256);// in units of 1/256th of a character width),默认为20个字符
                Cell cell = header.createCell(i);
                //				cell.setCellStyle(style);
                cell.setCellValue(titles[i]);
            }
        }
    }



    /**
     * 设置数据信息
     *
     * @param workbook
     * @param sheet
     * @param dataList
     */
    private static void setDataCells(Workbook workbook, Sheet sheet, List<String[]> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(i + 1);// 第二行开始
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
            df.setMaximumFractionDigits(2); //设置数值的小数部分允许的最大位数。
            df.setMinimumFractionDigits(2); //设置数值的小数部分允许的最小位数。
            orderRate = df.format(rowint * 100.00 / numint) + "%";
        }
        return orderRate;
    }

    /**
     * 写入数据到本地excel文件中
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
            logger.error("Excel文件不存在！");
        } catch (IOException e) {
            logger.error("数据写入到Excel文件失败！");
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.flush();
                    fileOut.close();
                } catch (IOException e) {
                    logger.error("关闭文件输出流失败！");
                }
            }
        }
    }

    /**
     * 导出Excel, fileName为空根据当前系统时间自动生成一个文件名，
     * sheetName为空，如果fileName不为空，则根据fileName生成sheet页名称，否则生成默认的sheet页名称
     *
     * @param fileName  excel文件名
     * @param sheetName excel sheet页名称
     * @param titles    sheet页列表名称
     * @param dataList  sheet页列表数据，对应title
     * @return 返回excel下载路径。相对于前台根目录
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
            // 标题
            setTitleCells(workbook, sheet, titles, titlesWidth);

            setDataCells(workbook, sheet, dataList);

            File excelFile = createFile(filePath, fileName);

            if (excelFile != null) {
                writeToFile(workbook, excelFile);
            }
        } catch (Exception e) {
            logger.error("导出Excel文件失败！", e);
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
     * 1、将单元格的内容转换为字符串
     *
     * @param cell 单元格
     * @return 返回转换后的字符串
     */
    private static String convertCellValueToString(Cell cell) {
        //1.1、判断单元格的数据是否为空
        if (cell == null) {
            return null;
        }
        //1.2、设置单元格数据的初始值
        String cellValue = null;
        //1.3、获取单元格数据的类型
        switch (cell.getCellType()) {
            case NUMERIC:
                //1.3.1、获取到单元格数据的格式
                short dataFormat = cell.getCellStyle().getDataFormat();
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = null;
                    //1.3.1.1、处理日期格式，根据不同日期长度去判断
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
                    //1.3.1.2、处理时间格式
                    Date date = cell.getDateCellValue();
                    assert sdf != null;
                    cellValue = sdf.format(date);
                } else if (dataFormat == 0) {
                    //1.3.2、处理普通数字格式
                    DecimalFormat format = new DecimalFormat("0");
                    double numericCellValue = cell.getNumericCellValue();
                    cellValue = format.format(numericCellValue);
                }
                break;
            case STRING:
                //处理字符串类型
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                //处理布尔类型
                boolean booleanCellValue = cell.getBooleanCellValue();
                cellValue = Boolean.toString(booleanCellValue);
                break;
            case FORMULA:
                //处理函数类型
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
     * 2、处理合并单元格里面的数据
     *
     * @param sheet 工作薄
     * @return 返回合并单元格后里面的数据
     */
    public static List<CellRangeAddress> getCombineCell(Sheet sheet) {
        List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
        //2.1、获得一个 sheet 中合并单元格的数量
        int sheetMergerCount = sheet.getNumMergedRegions();
        //2.2、遍历合并单元格
        for (int i = 0; i < sheetMergerCount; i++) {
            //2.2.1、获得合并单元格加入list中
            CellRangeAddress rangeAddress = sheet.getMergedRegion(i);
            list.add(rangeAddress);
        }
        return list;
    }

    /**
     * 3、判断单元格是否为合并单元格
     *
     * @param listCombineCell 存放合并单元格的list
     * @param cell            需要判断的单元格
     * @param sheet           sheet
     */
    public static String isCombineCell(List<CellRangeAddress> listCombineCell, Cell cell, Sheet sheet) {
        //3.1、设置第一个单元格和最后一个单元格的值
        int firstColumn = 0;
        int lastColumn = 0;
        //3.2、设置第一个单元格和最后一个行的值
        int firstRow = 0;
        int lastRow = 0;
        //3.3、初始化单元格值
        String cellValue = null;
        for (CellRangeAddress rangeAddress : listCombineCell) {
            //3.3.1、获得合并单元格的起始行, 结束行, 起始列, 结束列
            firstColumn = rangeAddress.getFirstColumn();
            lastColumn = rangeAddress.getLastColumn();
            firstRow = rangeAddress.getFirstRow();
            lastRow = rangeAddress.getLastRow();
            //3.3.2、判断是不是合并单元格
            if (cell.getRowIndex() >= firstRow && cell.getRowIndex() <= lastRow) {
                if (cell.getColumnIndex() >= firstColumn && cell.getColumnIndex() <= lastColumn) {
                    //3.3.2.1、获取行数据
                    Row fRow = sheet.getRow(firstRow);
                    //3.3.2.2、获取单元格数据
                    Cell fCell = fRow.getCell(firstColumn);
                    //3.3.2.3、对有合并单元格的数据进行格式处理
                    cellValue = convertCellValueToString(fCell);
                    break;
                }
            } else {
                //3.3.3、对没有合并单元格的数据进行格式处理
                cellValue = convertCellValueToString(cell);
            }
        }
        //3.4、返回处理后的单元格数据
        return cellValue;
    }

    /**
     * 4、判断sheet页中是否有合并单元格
     *
     * @param sheet sheet
     * @return 返回值
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
     * 5、读取excel文件内容
     *
     * @param inputStream 输入流
     * @return 返回值
     */
    public static List<AccountInfo> importExcel(InputStream inputStream) {
        //5.1、定义一个集合用来存储Object数据
        List<AccountInfo> list = new ArrayList<>();
        try {
            //5.2、创建工作薄
            Workbook workbook = WorkbookFactory.create(inputStream);
            //5.3、获取工作薄里面sheet的个数
            int sheetNum = workbook.getNumberOfSheets();
            //5.4、遍历每一个sheet
            for (int i = 0; i < sheetNum; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                //5.4.1、获取sheet中有数据的行数
                int rows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < rows; j++) {
                    //5.4.1.1、过滤掉文件的表头（视文件表头情况而定）
                    if (i == 1 || j == 0) {
                        continue;
                    }
                    //5.4.1.2、获取每一行的数据
                    Row row = sheet.getRow(j);
                    if (row == null) {
                        System.out.println("row is null");
                    } else {
                        //5.4.1.3、得到每一行中有效单元格的数据
                        int cells = row.getPhysicalNumberOfCells();
                        //5.4.1.4、定义一个Object数组用来存储读取单元格的数据
                        AccountInfo accountInfo = new AccountInfo();
                        //5.4.1.5、初始化对象数组的下标
                        int index = 0;
                        //5.4.1.6、遍历每一个有效的单元格数据
                        Cell cell = row.getCell(0);
                        Date accountTime = DateUtils.stringToDate(cell.getStringCellValue()) ;
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
//                            //5.4.1.6.1、获取每一个单元格的数据
//                            Cell cell = row.getCell(k);
//                            //5.4.1.6.2、判断当前sheet页是否合并有单元格
//                            boolean b = hasMerged(sheet);
//                            if (b) {
//                                //5.4.1.6.2.1、判断当前单元格是不是合并单元格，如果是则输出合并单元格的数据，不是则直接输出
//                                List<CellRangeAddress> listCombineCell = getCombineCell(sheet);
//                                String combineCell = isCombineCell(listCombineCell, cell, sheet);
//                                //5.4.1.6.2.1.2、对单元格的数据进行处理
//                                objects[index] = combineCell;
//                            } else {
//                                String cellValueToString = convertCellValueToString(cell);
//                                objects[index] = cellValueToString;
//                            }
//                            //5.4.1.6.3、下标累加
//                            index++;
//                        }
                        //5.4.1.7、将对象数组里面的数据添加到list集合中去
                        list.add(accountInfo);
                    }
                }
            }
            logger.info("导入文件解析成功！");
        } catch (Exception e) {
            logger.info("导入文件解析失败！");
            e.printStackTrace();
            return null;
        };
        //5.5、返回List集合
        return list;
    }
}
