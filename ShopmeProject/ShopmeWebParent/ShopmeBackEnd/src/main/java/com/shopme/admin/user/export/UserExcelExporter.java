package com.shopme.admin.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.admin.user.AbstractExporter;
import com.shopme.common.entity.User;

public class UserExcelExporter extends AbstractExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
	}
	
	public void writeHeaderRow() {
		sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		cellStyle.setFont(font);
		
		String headers[] = {"ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
		for (int i=0; i < headers.length; i++) {
			createCell(row,i,headers[i], cellStyle);
		}
	}
	
	public void writeDataRows(List<User> listUsers) {
		int rowIndex = 1;
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		cellStyle.setFont(font);
		
		for (User user : listUsers) {
			XSSFRow row = sheet.createRow(rowIndex++);
			createCell(row,0,user.getId(), cellStyle);
			createCell(row,1,user.getEmail(), cellStyle);
			createCell(row,2,user.getFirstName(), cellStyle);
			createCell(row,3,user.getLastName(), cellStyle);
			createCell(row,4,user.getRoles().toString(), cellStyle);
			createCell(row,5,user.isEnabled(), cellStyle);
		}
	}
	
	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		
		if (value instanceof Integer) {
			cell.setCellValue((Integer)value);
			cell.setCellStyle(style);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
			cell.setCellStyle(style);
		} else {
			cell.setCellValue((String) value);
			cell.setCellStyle(style);
		}
	}
	
	
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		setResponseHeader(response, "application/octet-stream", ".xlsx");
		
		writeHeaderRow();
		writeDataRows(listUsers);
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
		
	
		
	}
}
