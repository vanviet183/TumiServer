package com.example.tumiweb.excel;

import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dao.Course;
import com.example.tumiweb.services.ICourseService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

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
public class WriteExcelFileCourse implements IExcelFile {
    //Course(Long id, String name, Long price, String description, String avatar, Long process, Boolean status)
    @Autowired private ICourseService courseService;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Course> courses;

    public WriteExcelFileCourse(List<Course> courses) {
        this.courses = courses;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("courses");
    }

    @Override
    public void writeHeader() {
        XSSFRow row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        List<String> headers = new ArrayList<>(Arrays.asList("Course ID", "Name", "Price", "Description", "Avatar", "Process", "Status", "CategoryID"));

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
        for(Course course : courses) {
            XSSFRow row = sheet.createRow(cnt);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(course.getId().toString());
            cell = row.createCell(1);
            cell.setCellValue(course.getName());
            cell = row.createCell(2);
            cell.setCellValue(course.getPrice().toString());
            cell = row.createCell(3);
            cell.setCellValue(course.getDescription());
            cell = row.createCell(4);
            cell.setCellValue(course.getAvatar());
            cell = row.createCell(5);
            cell.setCellValue(course.getProcess().toString());
            cell = row.createCell(6);
            cell.setCellValue(course.getStatus().toString());
            cell = row.createCell(7);
            cell.setCellValue(courseService.findCategoryByCourseId(course.getId()).getId().toString());

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
            FileOutputStream out = new FileOutputStream(new File(Constants.PATH_COURSE_FILE));
            workbook.write(out);
            workbook.close();
            out.close();
        }

    }
}
