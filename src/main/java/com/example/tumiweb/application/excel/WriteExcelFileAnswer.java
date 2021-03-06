package com.example.tumiweb.application.excel;

import com.example.tumiweb.application.constants.CommonConstant;
import com.example.tumiweb.application.constants.EmailConstant;
import com.example.tumiweb.domain.entity.Answer;
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
public class WriteExcelFileAnswer implements IExcelFile {
  private XSSFWorkbook workbook;
  private XSSFSheet sheet;

  private List<Answer> answers;

  public WriteExcelFileAnswer(List<Answer> answers) {
    this.answers = answers;
    workbook = new XSSFWorkbook();
    sheet = workbook.createSheet("answers");
  }

  @Override
  public void writeHeader() {
    XSSFRow row = sheet.createRow(0);
    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(16);
    style.setFont(font);

    List<String> headersAnswer = new ArrayList<>(Arrays.asList("Answer ID", "Title", "IsTrue", "Image", "Question ID"));

    for (int i = 0; i < headersAnswer.size(); i++) {
      XSSFCell cell = row.createCell(i);
      cell.setCellType(CellType.STRING);
      cell.setCellValue(headersAnswer.get(i));
      cell.setCellStyle(style);
      sheet.autoSizeColumn(0);
    }
  }

  @Override
  public void writeData() {
    int cnt = 1;
    for (Answer answer : answers) {
      XSSFRow row = sheet.createRow(cnt);

      XSSFCell cell = row.createCell(0);
      cell.setCellValue(answer.getId().toString());
      cell = row.createCell(1);
      cell.setCellValue(answer.getTitle());
      cell = row.createCell(2);
      cell.setCellValue(answer.getIsTrue().toString());
      cell = row.createCell(3);
      cell.setCellValue(answer.getImage());
      cell = row.createCell(4);
      cell.setCellValue(answer.getQuestion().getId().toString());

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
      FileOutputStream out = new FileOutputStream(new File(CommonConstant.PATH_ANSWER_FILE));
      workbook.write(out);
      workbook.close();
      out.close();
    }

  }
}
