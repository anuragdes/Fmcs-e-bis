package eBIS.Utility.Service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.eclipse.collections.impl.list.mutable.FastList;

import eBIS.Utility.Model.SITDocumentModel;

public interface SITDocumentService {
FastList<SITDocumentModel> getSITDocumentList(String standardNumber);
String getSITDocumentHTML(String standardNumber,String numId);
InputStream DownloadSITFile(FTPClient ftpClient, String fileName)throws FTPConnectionClosedException, IOException, Exception;
}
