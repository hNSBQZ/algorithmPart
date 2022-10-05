package myLearn.utils;


import com.groupdocs.conversion.Converter;
import com.groupdocs.conversion.filetypes.SpreadsheetFileType;
import com.groupdocs.conversion.options.convert.SpreadsheetConvertOptions;
import com.groupdocs.conversion.options.load.CsvLoadOptions;

public class CsvAndXls {
    private CsvAndXls(){}
    public static void CsvToXlsx(String oldFileName,String newFileName){
        // 在 Java 中将 CSV 文件转换为 XLS/XLSX 格式
        CsvLoadOptions loadOptions = new CsvLoadOptions();
        loadOptions.setSeparator(',');
        Converter converter = new Converter(oldFileName, loadOptions);

        SpreadsheetConvertOptions options = new SpreadsheetConvertOptions();
        options.setFormat(SpreadsheetFileType.Xlsx);

        converter.convert(newFileName, options);

    }
    public static void XlsxToCsv(String oldFileName,String newFileName){
        // 在 Java 中将 CSV 文件转换为 XLS/XLSX 格式
        // 在 Java 中将 Excel 电子表格转换为逗号分隔值 CSV 格式
        Converter converter = new Converter(oldFileName);
        SpreadsheetConvertOptions options = new SpreadsheetConvertOptions();
        options.setFormat(SpreadsheetFileType.Csv); // Specify the conversion format
        converter.convert(newFileName, options);
    }
}
