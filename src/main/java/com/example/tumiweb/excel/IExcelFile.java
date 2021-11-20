package com.example.tumiweb.excel;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IExcelFile {
    void writeHeader();
    void writeData();
    void export(HttpServletResponse res) throws IOException;
}
