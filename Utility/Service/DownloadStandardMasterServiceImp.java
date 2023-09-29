package eBIS.Utility.Service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eBIS.AppConfig.FileUploadPathPropertiesBundleFile;
import eBIS.AppConfig.FtpConfiguration;
import lombok.var;

@Service
public class DownloadStandardMasterServiceImp implements DownloadStandardMasterService {
	final String standardMasterFile = FileUploadPathPropertiesBundleFile.getValueFromKey("standard.master.file");
	@Autowired 
	FtpConfiguration ftpConfiguration;
	@Override
	public InputStream DownloadStandardMasterFile(FTPClient ftpClient ,String fileName)
			throws FTPConnectionClosedException, IOException, Exception {
		String path=standardMasterFile;
		var istream=ftpConfiguration.downloadfromFtp(path, fileName);//removed argument type ftpClient on 5/12/22 because argument no declare in method is 2 only
		return istream;
	}

}
