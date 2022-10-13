package myLearn.fileController;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * 静态类，提供csv和xls相互转换的方法
 * @author:hqz
 * @createDate:2022/10/11
 *
 */
public class CsvAndXls {
    private CsvAndXls(){}

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    /**
     *将 CSV 文件转换为 XLS/XLSX 格式
     * @param oldFileName:原始csv文件路径
     * @param newFileName:要生成的excel文件路径
     *
     */
    public static void CsvToExcel(String oldFileName,String newFileName){
        //将csv内容存入列表
        ArrayList<String[]> list = new ArrayList();
        DataInputStream in = null;
        try {
            in = new DataInputStream(new FileInputStream(new File(oldFileName)));
            CSVReader csvReader = null;
            csvReader = new CSVReader(new InputStreamReader(in, "UTF-8"));
            String[] strs;
            while (true) {
                if (!((strs = csvReader.readNext()) != null)) break;
                //System.out.println(Arrays.deepToString(strs));
                list.add(strs);
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //存入excel文件
        String fileType = newFileName.substring(newFileName.lastIndexOf(".") + 1, newFileName.length());
        try {
            Workbook workbook=getWorkbook(null,fileType);
            Sheet sheet=workbook.createSheet();
            for(int i=0;i<list.size();i++)
            {
                Row row=sheet.createRow(i);
                String[] strs=list.get(i);
                for(int j=0;j<strs.length;j++)
                {
                    Cell cell=row.createCell(j);
                    cell.setCellValue(strs[j]);
                }
            }
            File outPutFile=new File(newFileName);
            FileOutputStream fileOut=new FileOutputStream(newFileName);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *将 XLS/XLSX文件转换为 excel 格式
     * @param oldFileName:原始excel文件路径
     * @param newFileName:生成csv文件路径
     *
     */
    public static void ExcelToCsv(String oldFileName,String newFileName){
        // 在 Java 中将 CSV 文件转换为 XLS/XLSX 格式
        // 在 Java 中将 Excel 电子表格转换为逗号分隔值 CSV 格式
        Workbook workbook = null;
        FileInputStream inputStream = null;

        try {
            // 获取Excel后缀名
            String fileType = oldFileName.substring(oldFileName.lastIndexOf(".") + 1, oldFileName.length());
            // 获取Excel文件
            File excelFile = new File(oldFileName);
            if (!excelFile.exists()) {
                System.out.println("指定的Excel文件不存在！");
            }

            // 获取Excel工作簿
            inputStream = new FileInputStream(excelFile);
            workbook = getWorkbook(inputStream, fileType);
            Sheet sheet =workbook.getSheetAt(0);
            //用于存储数据的列表
            ArrayList<String[]> list = new ArrayList();
            System.out.println(sheet.getPhysicalNumberOfRows());
            for(int i=0;i<sheet.getPhysicalNumberOfRows();i++)
            {
                Row row=sheet.getRow(i);
                int count=row.getPhysicalNumberOfCells();
                //System.out.println(count);
                String[] strs=new String[count];
                for(int j=0;j<count;j++)
                {
                    Cell cell=row.getCell(j);
                    switch(cell.getCellType())
                    {
                        case 0:
                            strs[j]=String.valueOf(cell.getNumericCellValue());
                            break;
                        default:
                            strs[j]=cell.getStringCellValue();
                    }
                }
                list.add(strs);
            }
            //将list中内容写入csv
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(newFileName);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            CSVWriter writer = new CSVWriter(osw);
            writer.writeAll(list);
            writer.flush();
            writer.close();
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     *根据xls和xlsx的不同返回不同的workbook
     * @param inputStream:输入流
     * @param fileType:文件类型
     * @return:用来读写excel的workbook
     * @throws IOException
     *
     */
    private static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        Workbook workbook = null;
        if (fileType.equalsIgnoreCase(XLS)) {
            if(inputStream==null)
                workbook = new HSSFWorkbook();
            else
                workbook = new HSSFWorkbook(inputStream);
        } else if (fileType.equalsIgnoreCase(XLSX)) {
            if(inputStream==null)
                workbook = new XSSFWorkbook();
            else
                workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

}
