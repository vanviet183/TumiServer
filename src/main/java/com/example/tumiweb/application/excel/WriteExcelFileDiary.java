package com.example.tumiweb.application.excel;

import com.example.tumiweb.application.constants.CommonConstant;
import com.example.tumiweb.domain.entity.Diary;
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
public class WriteExcelFileDiary implements IExcelFile {
  private XSSFWorkbook workbook;
  private XSSFSheet sheet;

  private List<Diary> diaries;

  public WriteExcelFileDiary(List<Diary> diaries) {
    this.diaries = diaries;
    workbook = new XSSFWorkbook();
    sheet = workbook.createSheet("diaries");
  }

  @Override
  public void writeHeader() {
    XSSFRow row = sheet.createRow(0);
    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(16);
    style.setFont(font);

    List<String> headers = new ArrayList<>(Arrays.asList("Diary ID", "Start", "End", "Day", "User ID"));

    for (int i = 0; i < headers.size(); i++) {
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
    for (Diary diary : diaries) {
      XSSFRow row = sheet.createRow(cnt);

      XSSFCell cell = row.createCell(0);
      cell.setCellValue(diary.getId().toString());
      cell = row.createCell(1);
      cell.setCellValue(diary.getStart());
      cell = row.createCell(2);
      cell.setCellValue(diary.getEnd());
      cell = row.createCell(3);
      cell.setCellValue(diary.getDay());
      cell = row.createCell(4);
      cell.setCellValue(diary.getUser().getId().toString());

      cnt++;
    }
  }

  public void export(HttpServletResponse res) throws IOException {
    writeHeader();
    writeData();
    if (res != null) {
      ServletOutputStream out = res.getOutputStream();
      workbook.write(out);
      workbook.close();
      out.close();
    } else {
      FileOutputStream out = new FileOutputStream(new File(CommonConstant.PATH_DIARY_FILE));
      workbook.write(out);
      workbook.close();
      out.close();
    }
  }

}
