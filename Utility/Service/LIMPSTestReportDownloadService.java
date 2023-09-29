package eBIS.Utility.Service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

public interface LIMPSTestReportDownloadService {

	InputStream downloadTestReportLIMPS(FTPClient ftpClient,String qrcode, String fileName) throws FTPConnectionClosedException, IOException, Exception;
	
}
