/**
 * Created On 30-Jul-2015
 * Copyright 2010 by Primeleaf Consulting (P) Ltd.,
 * #29,784/785 Hendre Castle,
 * D.S.Babrekar Marg,
 * Gokhale Road(North),
 * Dadar,Mumbai 400 028
 * India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Primeleaf Consulting (P) Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Primeleaf Consulting (P) Ltd.
 */

package com.primeleaf.krystal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentRevision;

/**
 * This file converts given document into PDF format with or without password for viewing of document in mobile device
 * or protects with users password on download.
 * Author Rahul.Kubadia
 * @since 10.0 (2016)
 * Modified By Saumil Shah
 */

public class PDFConverter {

	public InputStream getConvertedFile (DocumentRevision documentRevision, Document document, String password) throws Exception{
		InputStream inStream = documentRevision.getDocumentFile();
		if("TIF".equalsIgnoreCase(document.getExtension()) || "TIFF".equalsIgnoreCase(document.getExtension())){
			try{
				File tempFile = File.createTempFile("temp", ".PDF");
				OutputStream oStream = new FileOutputStream(tempFile.getAbsolutePath());
				FileUtils.copyInputStreamToFile(inStream, tempFile);
				com.itextpdf.text.Document pdf = new com.itextpdf.text.Document();
				PdfWriter.getInstance(pdf, oStream);
				pdf.open();
				pdf.setMargins(0, 0, 0, 0);
				RandomAccessFileOrArray file = new RandomAccessFileOrArray(new FileInputStream(tempFile));
				int pages = TiffImage.getNumberOfPages(file);
				for (int page = 1; page <= pages; page++){
					Image img = TiffImage.getTiffImage(file, page);
					img.setAbsolutePosition(0f,0f);
					img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
					pdf.setMargins(0, 0, 0, 0);
					pdf.add(img);
					pdf.newPage();
				}
				document.setExtension("PDF");
				inStream = new FileInputStream(tempFile);
				pdf.close();
				tempFile.delete();
			}catch(Exception e){
				throw new Exception (e+"\nUnable to convert TIFF Document to PDF");
			}
		}else  if("JPG".equalsIgnoreCase(document.getExtension()) || "JPEG".equalsIgnoreCase(document.getExtension())
				|| "PNG".equalsIgnoreCase(document.getExtension()) 
				|| "BMP".equalsIgnoreCase(document.getExtension()) || "GIF".equalsIgnoreCase(document.getExtension() )){
			try{
				File tempFile = File.createTempFile("temp", ".PDF");
				OutputStream outStream = new FileOutputStream(tempFile);
				FileUtils.copyInputStreamToFile(inStream, tempFile);
				Image img = Image.getInstance(tempFile.getAbsolutePath());
				com.itextpdf.text.Document pdf = new com.itextpdf.text.Document(new Rectangle(img.getWidth(), img.getHeight()),0,0,0,0);
				img.setAbsolutePosition(0f,0f);
				PdfWriter.getInstance(pdf, outStream);
				pdf.open();
				pdf.add(img);
				pdf.close();
				document.setExtension("PDF");
				inStream = new FileInputStream(tempFile);
				tempFile.delete();
			}catch(Exception e){
				throw new Exception ("Unable to convert Image Document to PDF");
			}
		}else if("HTML".equalsIgnoreCase(document.getExtension())==true) {
			File tempFile = File.createTempFile("temp", ".PDF");
			OutputStream outStream = new FileOutputStream(tempFile);
			FileUtils.copyInputStreamToFile(inStream, tempFile);
			HtmlConverter.convertToPdf(new FileInputStream(tempFile), outStream);		
			inStream = new FileInputStream(tempFile);
			tempFile.delete();
		}else if("TXT".equalsIgnoreCase(document.getExtension())==true) {
			try{
			        com.itextpdf.text.Document document2 = new com.itextpdf.text.Document(PageSize.A4, 36, 72, 108, 180);
			        File tempFile = File.createTempFile("temp", ".PDF");
					OutputStream outStream = new FileOutputStream(tempFile);
					FileUtils.copyInputStreamToFile(inStream, tempFile);
			        PdfWriter.getInstance(document2,outStream);
			        document2.open();
			        String textString = FileUtils.readFileToString(tempFile,"UTF-8");
			        document2.add(new Paragraph(textString));
			        inStream = new FileInputStream(tempFile);
			        tempFile.delete();
			        document2.close();
			    }catch(Exception e){}
		}else  if("PDF".equalsIgnoreCase(document.getExtension())==false) {
			String tempFilePath = "";
			String KRYSTAL_HOME = System.getProperty("krystal.home");
			if (KRYSTAL_HOME == null) {
				KRYSTAL_HOME = System.getProperty("user.dir");
				System.setProperty("krystal.home", KRYSTAL_HOME);
			}
			tempFilePath=KRYSTAL_HOME+File.separator+"/webapps/DMC/images/unsupport.pdf";
			inStream = new FileInputStream(tempFilePath);
		}
		return inStream;
	}

	
}
