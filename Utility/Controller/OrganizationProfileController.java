package eBIS.Utility.Controller;

import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eBIS.AppConfig.FtpConfiguration;
import eBIS.Utility.Service.OrganizationProfileService;

@Controller
public class OrganizationProfileController {
	@Autowired
	OrganizationProfileService organizationProfileService;
	@Autowired
	FtpConfiguration ftpConfiguration;
	@RequestMapping(value = "/DownloadPCOrgProfileFile/{userID}/{checksum}/{extension}", method = {
			RequestMethod.GET })
	public @ResponseBody void DownloadPCOrgProfileFile(
			@PathVariable("userID") String userID, @PathVariable("checksum") String checksum,
			@PathVariable("extension") String extension, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		 FTPClient ftpClient = null;
		try {
			String fileName = checksum + "." + extension;
			ftpClient = ftpConfiguration.connectToServer();
			InputStream inputStream = organizationProfileService.DownloadPCOrgProfileFile(ftpClient,userID, fileName);
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
			ftpConfiguration.closeConnect();//removed argument type ftpClient on 5/12/22 because argument no declare in method is 2 only
		}
	}
}
