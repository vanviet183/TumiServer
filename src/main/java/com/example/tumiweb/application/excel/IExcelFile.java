package com.example.tumiweb.application.excel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IExcelFile {

  void writeHeader();

  void writeData();

  void export(HttpServletResponse res) throws IOException;

}
