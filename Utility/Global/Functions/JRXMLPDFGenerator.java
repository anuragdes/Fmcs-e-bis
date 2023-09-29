package eBIS.Utility.Global.Functions;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import eBIS.AppConfig.FtpConfiguration;
import eBIS.AppConfig.PrimaryDaoHelper;
import lombok.var;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Component
public class JRXMLPDFGenerator {
	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	PrimaryDaoHelper daoHelper;
	
	@Autowired
	FtpConfiguration ftpConfiguration;

	public String generatePaymentReceipt(HashMap<String, Object> parameters, String fileName,String JRXMLFile,String jrxmlPCPath,String JRXMLPdfPath) throws Exception {
		String classpath = jrxmlPCPath + JRXMLFile;
		System.out.println("JRXML Path: " + classpath);
		String path = resourceLoader.getResource(classpath).getURI().getPath();
		JasperReport jasperReport = JasperCompileManager.compileReport(path);
		Connection connenction = daoHelper.getconnectName();
		JasperPrint printFileName = JasperFillManager.fillReport(jasperReport, parameters, connenction);
		byte[] content = JasperExportManager.exportReportToPdf(printFileName);
		String name = "jrxml.pdf";
		String originalFileName = "jrxml.pdf";
		String contentType = "application/pdf";
		MultipartFile UploadFile = new MockMultipartFile(name, originalFileName, contentType, content);
		String checksum = ftpConfiguration.uploadToFtpwithCustomFileName(UploadFile, JRXMLPdfPath, fileName);
		return checksum;
	}
	public int CheckPaymentReceipt(String filePath, String fileName, String fileExtension) {
		int flag = -1;
		FTPClient ftpClient=null;
		try {
			
			fileName = fileName + "." + fileExtension;
			ftpClient=ftpConfiguration.connectToServer();
			var inputStream = ftpConfiguration.downloadfromFtp(filePath, fileName);//removed argument type ftpClient on 5/12/22 
			if (inputStream != null) {
				flag=1;
			}else {
				flag=0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ftpConfiguration.closeConnect();//removed argument type ftpClient on 5/12/22 
		}
		return flag;
	}
	public ServletOutputStream DownloadPaymentReceipt(String filePath, String fileName, String fileExtension,HttpServletResponse response) {
		ServletOutputStream out = null;
		FTPClient ftpClient=null;
		try {
			fileName = fileName + "." + fileExtension;
			ftpClient=ftpConfiguration.connectToServer();
			var inputStream = ftpConfiguration.downloadfromFtp(filePath, fileName);//removed argument type ftpClient on 5/12/22 because argument no declare in method is 2 only
			if (inputStream != null) {
				out  = response.getOutputStream();
				int i;
				while ((i = inputStream.read()) != -1) {
					out.write(i);
				}
				inputStream.close();
				out.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ftpConfiguration.closeConnect();//removed argument type ftpClient on 5/12/22
		}
		return out;
	}
}
