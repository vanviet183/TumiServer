package com.example.tumiweb.excel;

import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dao.Gift;
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

public class WriteExcelFileGift implements IExcelFile {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Gift> gifts;

    public WriteExcelFileGift(List<Gift> gifts) {
        this.gifts = gifts;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("gifts");
    }

    @Override
    public void writeHeader() {
        XSSFRow row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        List<String> headers = new ArrayList<>(Arrays.asList("Gift ID", "Name", "Mark", "Avatar", "Status"));

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
        for(Gift gift : gifts) {
            XSSFRow row = sheet.createRow(cnt);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(gift.getId().toString());
            cell = row.createCell(1);
            cell.setCellValue(gift.getName());
            cell = row.createCell(2);
            cell.setCellValue(gift.getMark().toString());
            cell = row.createCell(3);
            cell.setCellValue(gift.getAvatar());
            cell = row.createCell(4);
            cell.setCellValue(gift.getStatus().toString());

            cnt++;
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
            FileOutputStream out = new FileOutputStream(new File(Constants.PATH_GIFT_FILE));
            workbook.write(out);
            workbook.close();
            out.close();
        }

    }
}
