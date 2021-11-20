package com.example.tumiweb.excel;

import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dao.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class WriteExcelFileCategory implements IExcelFile{
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Category> categories;

    public WriteExcelFileCategory(List<Category> categories) {
        this.categories = categories;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("categories");
    }

    @Override
    public void writeHeader() {
        XSSFRow row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        List<String> headers = new ArrayList<>(Arrays.asList("Category ID", "Name", "Description", "Status"));

        for(int i=0; i<headers.size(); i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(style);
            sheet.autoSizeColumn(0);
        }
    }

    @Override
    public void writeData() {
        int cnt = 1;
        for(Category category : categories) {
            XSSFRow row = sheet.createRow(cnt);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(category.getId().toString());
            cell = row.createCell(1);
            cell.setCellValue(category.getName());
            cell = row.createCell(2);
            cell.setCellValue(category.getDescription());
            cell = row.createCell(3);
            cell.setCellValue(category.getStatus().toString());
        }
    }

    public void export(HttpServletResponse res) throws IOException {
        writeHeader();
        writeData();
        if(res != null) {
            ServletOutputStream out = res.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.close();
        }
        else {
            FileOutputStream out = new FileOutputStream(new File(Constants.PATH_CATEGORY_FILE));
            workbook.write(out);
            workbook.close();
            out.close();
        }

    }
}
