package com.example.tumiweb.excel;

import com.example.tumiweb.dao.Help;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class WriteExcelFileHelp implements IExcelFile {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Help> helps;

    public WriteExcelFileHelp(List<Help> helps) {
        this.helps = helps;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("helps");
    }

    @Override
    public void writeHeader() {
        XSSFRow row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        List<String> headers = new ArrayList<>(Arrays.asList("Help ID", "Title", "Status", "User ID"));

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
        for(Help help : helps) {
            XSSFRow row = sheet.createRow(cnt);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(help.getId().toString());
            cell = row.createCell(1);
            cell.setCellValue(help.getTitle());
            cell = row.createCell(2);
            cell.setCellValue(help.getStatus().toString());
            cell = row.createCell(3);
            cell.setCellValue(help.getUser().getId().toString());

            cnt++;
        }
    }

    @Override
    public void export(HttpServletResponse res) throws IOException {
        writeHeader();
        writeData();

        ServletOutputStream outputStream = res.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
