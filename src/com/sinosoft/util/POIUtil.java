package com.sinosoft.util;

import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class POIUtil {
	/**
	 * 复制一个单元格样式到目的单元格样式
	 * @param fromStyle
	 * @param toStyle
	 */
	public static void copyCellStyle(XSSFCellStyle fromStyle,
			XSSFCellStyle toStyle) {
		toStyle.setAlignment(fromStyle.getAlignment());
		//边框和边框颜色
		toStyle.setBorderBottom(fromStyle.getBorderBottom());
		toStyle.setBorderLeft(fromStyle.getBorderLeft());
		toStyle.setBorderRight(fromStyle.getBorderRight());
		toStyle.setBorderTop(fromStyle.getBorderTop());
		toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
		toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
		toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
		toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());
		
		//背景和前景
		toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
		toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());
		
		toStyle.setDataFormat(fromStyle.getDataFormat());
		toStyle.setFillPattern(fromStyle.getFillPattern());
//		toStyle.setFont(fromStyle.getFont(null));
		toStyle.setHidden(fromStyle.getHidden());
		toStyle.setIndention(fromStyle.getIndention());//首行缩进
		toStyle.setLocked(fromStyle.getLocked());
		toStyle.setRotation(fromStyle.getRotation());//旋转
		toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
		toStyle.setWrapText(fromStyle.getWrapText());
		
	}
	
	/**
	 * 行复制功能
	 * @param fromRow
	 * @param toRow
	 */
	public static void copyRow(XSSFWorkbook wb,XSSFRow fromRow,XSSFRow toRow,boolean copyValueFlag){
		for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
			XSSFCell tmpCell = (XSSFCell) cellIt.next();
			XSSFCell newCell = toRow.createCell(tmpCell.getColumnIndex());
			copyCell(wb,tmpCell, newCell, copyValueFlag);
		}
	}
	
	/**
	 * 复制单元格
	 * 
	 * @param srcCell
	 * @param distCell
	 * @param copyValueFlag
	 *            true则连同cell的内容一起复制
	 */
	public static void copyCell(XSSFWorkbook wb,XSSFCell srcCell, XSSFCell distCell,
			boolean copyValueFlag) {
		XSSFCellStyle newstyle=wb.createCellStyle();
		copyCellStyle(srcCell.getCellStyle(), newstyle);
		//distCell.setEncoding(srcCell.getEncoding());
		//样式
		distCell.setCellStyle(newstyle);
		//评论
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		// 不同数据类型处理
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);
		if (copyValueFlag) {
			if (srcCellType == XSSFCell.CELL_TYPE_NUMERIC) {
				distCell.setCellValue(srcCell.getNumericCellValue());
			} else if (srcCellType == XSSFCell.CELL_TYPE_STRING) {
				distCell.setCellValue(srcCell.getRichStringCellValue());
			} else if (srcCellType == XSSFCell.CELL_TYPE_BLANK) {
				// nothing21
			} else if (srcCellType == XSSFCell.CELL_TYPE_BOOLEAN) {
				distCell.setCellValue(srcCell.getBooleanCellValue());
			} else if (srcCellType == XSSFCell.CELL_TYPE_ERROR) {
				distCell.setCellErrorValue(srcCell.getErrorCellValue());
			} else if (srcCellType == XSSFCell.CELL_TYPE_FORMULA) {
				distCell.setCellFormula(srcCell.getCellFormula());
			} else { // nothing29
			}
		}
	}
	
	
	/**
	 * @param wb
	 * @param sheet
	 * @param starRow 插入起始行，如 starRow = 1，则插入行index以2开始
	 * @param rows 插入的行数
	 */
	public static void insertRow(XSSFWorkbook wb, XSSFSheet sheet, int starRow, int rows) {

		sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows, true, false);

		for (int i = 0; i < rows; i++) {
			XSSFRow targetRow = null;
			XSSFRow upRow = null;
			XSSFCell upCell = null;
			XSSFCell targetCell = null;
			short m;

			upRow = sheet.getRow(starRow - 1);
			targetRow = sheet.createRow(starRow);

			for (m = upRow.getFirstCellNum(); m < upRow.getLastCellNum(); m++) {
				targetCell = targetRow.createCell(m);
				upCell = upRow.getCell(m);
				targetCell.setCellStyle(upCell.getCellStyle());
				targetCell.setCellType(upCell.getCellType());
			}
		}
	}
}
