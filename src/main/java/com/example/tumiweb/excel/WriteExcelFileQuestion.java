package com.example.tumiweb.excel;

import com.example.tumiweb.dao.Question;
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
public class WriteExcelFileQuestion implements IExcelFile {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Question> questions;

    public WriteExcelFileQuestion(List<Question> questions) {
        System.out.println();
        this.questions = questions;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("questions");
    }

    @Override
    public void writeHeader() {
        XSSFRow row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        List<String> headersQuestion = new ArrayList<>(Arrays.asList("Question ID", "Title", "Seo", "Avatar", "Status"));

        for(int i=0; i<headersQuestion.size(); i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(headersQuestion.get(i));
            cell.setCellStyle(style);
            sheet.autoSizeColumn(0);
        }
    }

    @Override
    public void writeData() {
        int cnt = 1;
        for(Question question : questions) {
            XSSFRow row = sheet.createRow(cnt);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(question.getId().toString());
            cell = row.createCell(1);
            cell.setCellValue(question.getTitle());
            cell = row.createCell(2);
            cell.setCellValue(question.getSeo());
            cell = row.createCell(3);
            cell.setCellValue(question.getAvatar());
            cell = row.createCell(4);
            cell.setCellValue(question.getStatus().toString());

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
