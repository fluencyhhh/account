package com.zhangjingbo.account.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
}
