package com.example.tumiweb.excel;

import com.example.tumiweb.dao.Question;
import com.example.tumiweb.dao.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class WriteExcelFileUser implements IExcelFile{
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<User> users;

    public WriteExcelFileUser(List<User> users) {
        this.users = users;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("users");
    }

    @Override
    public void writeHeader() {
        XSSFRow row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        List<String> headersUser = new ArrayList<>(Arrays.asList("User ID", "Username", "Password", "Email", "Phone", "Avatar", "Mark", "Status"));

        for(int i=0; i<headersUser.size(); i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(headersUser.get(i));
            cell.setCellStyle(style);
            sheet.autoSizeColumn(0);
        }
    }

    @Override
    public void writeData() {
        int cnt = 1;
        for(User user : users) {
            XSSFRow row = sheet.createRow(cnt);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(user.getId().toString());
            cell = row.createCell(1);
            cell.setCellValue(user.getUsername());
            cell = row.createCell(2);
            cell.setCellValue(user.getPassword());
            cell = row.createCell(3);
            cell.setCellValue(user.getEmail());
            cell = row.createCell(4);
            cell.setCellValue(user.getPhone());
            cell = row.createCell(5);
            cell.setCellValue(user.getAvatar());
            cell = row.createCell(6);
            cell.setCellValue(user.getMark() == null ? "0" : user.getMark().toString());
            cell = row.createCell(7);
            cell.setCellValue(user.getStatus().toString());

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
