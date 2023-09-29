package eBIS.Utility.Controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eBIS.AppConfig.FtpConfiguration;
import eBIS.Utility.Service.SITDocumentService;

@Controller
public class DownloadSITController {
	@Autowired
	SITDocumentService sitDocumentService;
	@Autowired 
	FtpConfiguration ftpConfiguration;
	@RequestMapping(value = "/DownloadSITFile/{checksum}/{extension}", method = {RequestMethod.GET })
	public @ResponseBody void DownloadSITFile( @PathVariable("checksum") String checksum,
			@PathVariable("extension") String extension, HttpServletRequest request, HttpServletResponse response)
					throws FTPConnectionClosedException, IOException, Exception {
		FTPClient ftpClient=null;
		try {
			String fileName = checksum + "." + extension;
			ftpClient = ftpConfiguration.connectToServer();
			InputStream inputStream = sitDocumentService.DownloadSITFile(ftpClient,fileName);
			if (inputStream != null) {
				response.setHeader("Content-Transfer-Encoding", "binary");
				if (extension.equalsIgnoreCase("xlsx")) {
					response.setContentType("application/xlsx");
					response.setHeader("Content-Disposition", "inline; filename=\"" + "downloadFile.xlsx");
				} else {
					if (extension.equalsIgnoreCase("xls")) {
						response.setContentType("application/xls");
						response.setHeader("Content-Disposition", "inline; filename=\"" + "downloadFile.xls");
					} else {
						response.setContentType("application/pdf");
						response.setHeader("Content-Disposition", "inline; filename=\"" + "downloadFile.pdf");
					}

				}
			}
			ServletOutputStream out = response.getOutputStream();
			int i;
			while ((i = inputStream.read()) != -1) {
				out.write(i);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			ftpConfiguration.closeConnect(); //removed argument type ftpClient on 5/12/22 because argument no declarein method is 2 only
		}
	}
}
