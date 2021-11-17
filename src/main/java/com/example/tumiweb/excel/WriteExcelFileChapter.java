package com.example.tumiweb.excel;

import com.example.tumiweb.dao.Chapter;
import com.example.tumiweb.services.IChapterService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class WriteExcelFileChapter implements IExcelFile{
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Chapter> chapters;

    @Autowired private IChapterService chapterService;

    public WriteExcelFileChapter(List<Chapter> chapters) {
        this.chapters = chapters;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("chapters");
    }

    @Override
    public void writeHeader() {
        XSSFRow row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        List<String> headers = new ArrayList<>(Arrays.asList("Chapter ID", "Name", "Status", "Course ID"));

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
        for(Chapter chapter : chapters) {
            XSSFRow row = sheet.createRow(cnt);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(chapter.getId().toString());
            cell = row.createCell(1);
            cell.setCellValue(chapter.getName());
            cell = row.createCell(2);
            cell.setCellValue(chapter.getStatus().toString());
            cell = row.createCell(3);
            cell.setCellValue(chapterService.findCourseByChapterId(chapter.getId()).getId().toString());

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
