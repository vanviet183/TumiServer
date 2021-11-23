package com.example.tumiweb.excel;

import com.example.tumiweb.dao.Question;
import com.example.tumiweb.dao.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadExcelFile {
    public static List<User> readFileUser(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        Iterator<Row> iterator = datatypeSheet.iterator();

        List<User> users = new ArrayList<>();

        while(iterator.hasNext()) {
            Row curRow = iterator.next();
            User user = new User(
                    Long.parseLong(formatter.formatCellValue(curRow.getCell(0))),
                    curRow.getCell(1).getStringCellValue(),
                    curRow.getCell(2).getStringCellValue(),
                    curRow.getCell(3).getStringCellValue(),
                    curRow.getCell(4).getStringCellValue(),
                    curRow.getCell(5).getStringCellValue(),
                    curRow.getCell(6).getStringCellValue(),
                    Long.parseLong(curRow.getCell(7).getStringCellValue()),
                    curRow.getCell(8).getStringCellValue(),
                    Boolean.parseBoolean(curRow.getCell(9).getStringCellValue())
            );
            users.add(user);
        }

        return users;
    }

    public static List<Question> readFileQuestion(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        Iterator<Row> iterator = datatypeSheet.iterator();

        List<Question> questions = new ArrayList<>();

        while(iterator.hasNext()) {
            Row curRow = iterator.next();
            Question question = new Question(
                    Long.parseLong(formatter.formatCellValue(curRow.getCell(0))),
                    curRow.getCell(1).getStringCellValue(),
                    curRow.getCell(2).getStringCellValue(),
                    curRow.getCell(3).getStringCellValue(),
                    Boolean.parseBoolean(curRow.getCell(4).getStringCellValue())
            );
            questions.add(question);
        }

        return questions;
    }
}
