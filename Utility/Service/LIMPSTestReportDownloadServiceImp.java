package eBIS.Utility.Service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Global.CommonUtility.ResourceBundleFile;
import eBIS.AppConfig.FtpConfiguration;
import lombok.var;

@Service
public class LIMPSTestReportDownloadServiceImp implements LIMPSTestReportDownloadService {
	String ftp_path_lims = ResourceBundleFile.getValueFromKey("ftp_path_lims");
	@Autowired 
	FtpConfiguration ftpConfiguration;
	@Override
	public InputStream downloadTestReportLIMPS(FTPClient ftpClient,String qrcode, String fileName) throws FTPConnectionClosedException, IOException, Exception {
		String path=ftp_path_lims+qrcode;
		var istream=ftpConfiguration.downloadfromFtp(path, fileName);//removed argument type ftpClient on 5/12/22 because argument no declare in method is 2 only
		return istream;
		
	}

}
