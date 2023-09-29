package eBIS.AppConfig;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FtpConfiguration {

	private String UATUserName=FTPProperties.getValueFromKey("uat.ftp.username");

	private String UATPassWord=FTPProperties.getValueFromKey("uat.ftp.password");

	private String UATIP=FTPProperties.getValueFromKey("uat.ftp.host");

	private int UATPort=Integer.parseInt(FTPProperties.getValueFromKey("uat.ftp.port"));

	private String UATFilePath=FTPProperties.getValueFromKey("uat.ftp.filepath");

	private String ProductionUserName=FTPProperties.getValueFromKey("production.ftp.username");
	
	private String ProductionPassWord=FTPProperties.getValueFromKey("production.ftp.password");

	private String ProductionIP=FTPProperties.getValueFromKey("production.ftp.host");

	private int ProductionPort=Integer.parseInt(FTPProperties.getValueFromKey("production.ftp.port"));

	private String ProductionFilePath=FTPProperties.getValueFromKey("production.ftp.filepath");

	private String LocalUserName=FTPProperties.getValueFromKey("local.ftp.username");

	private String LocalPassWord=FTPProperties.getValueFromKey("local.ftp.password");

	private String LocalIP=FTPProperties.getValueFromKey("local.ftp.host");

	private int LocalPort=Integer.parseInt(FTPProperties.getValueFromKey("local.ftp.port"));

	private String LocalFilePath=FTPProperties.getValueFromKey("local.ftp.filepath");
		
	@Autowired
	CheckhostIP checkhost;
	
	private FTPClient ftpClient = new FTPClient();

	public String uploadToFtp(MultipartFile upload_file, String FilePath)
			throws FTPConnectionClosedException, IOException, Exception {
		InputStream inputstream = upload_file.getInputStream();
		FTPClient ftpclient = connectToServer();
		setFileType(FTP.BINARY_FILE_TYPE);
		String checksum = GenerateCheckSum(inputstream);
		ftpclient = uploadPathCreator(FilePath, ftpclient);
		String[] FileExtensionTemp = upload_file.getOriginalFilename().split("\\.");
		String FileExtension = FileExtensionTemp[FileExtensionTemp.length - 1];
		boolean upload = ftpclient.storeFile(checksum + "." + FileExtension, upload_file.getInputStream());
		closeConnect();
		if (!upload) {
			checksum = "0";
		}
		return checksum;
	}

	public void closeConnect() {
		try {
			if (ftpClient != null) {
				ftpClient.logout();
				ftpClient.disconnect();
				System.out.println("close FTP connection!" );
			}
		} catch (Exception e) {
			System.out.println("Failed to close FTP connection!" + e);
		}
	}

	private void setFileType(int fileType) {
		try {
			ftpClient.setFileType(fileType);
		} catch (Exception e) {
			System.out.println("Failed to set the type of file transfer by FTP!" + e);
		}
	}

	public FTPClient connectToServer() throws FTPConnectionClosedException, Exception { //made method access public on 5/12/22
		System.out.println("ftpClient.isConnected():" + ftpClient.isConnected());
		if (!ftpClient.isConnected()) {
			int reply;
			String IP = "";
			try {
				ftpClient = new FTPClient();
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.enterLocalPassiveMode();

				int Port = 0;
				String UserName = "";
				String Passward = "";
				int flag = checkhost.gethost();
				if (flag==1) {
					IP = ProductionIP;
					Port = ProductionPort;
					UserName = ProductionUserName;
					Passward = ProductionPassWord;
				} else {
					if (flag==2) {
						IP = UATIP;
						Port = UATPort;
						UserName = UATUserName;
						Passward = UATPassWord;
					} else {
						IP = LocalIP;
						Port = LocalPort;
						UserName = LocalUserName;
						Passward = LocalPassWord;
					}
				}
				ftpClient.connect(IP, Port);
				boolean loginflag = ftpClient.login(UserName, Passward);
				reply = ftpClient.getReplyCode();
				System.out.println(loginflag);
				if (!FTPReply.isPositiveCompletion(reply)) {
					ftpClient.disconnect();
					System.out.println("connectToServer FTP server refused connection.");
				}

			} catch (FTPConnectionClosedException ex) {
				System.out.println(
						"Server:IP:" + IP + "No connection! There are too many connected users, please try later" + ex);
				throw ex;
			} catch (Exception e) {
				System.out.println("Login to ftp server [" + IP + "] failed" + e);
				throw e;
			}
		}
		return ftpClient;
	}

	private String GenerateCheckSum(InputStream inputstream) throws NoSuchAlgorithmException, IOException {
		String stChecksum = "";
		byte[] content = IOUtils.toByteArray(inputstream);
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] digest = md5.digest(content);
		stChecksum = new BigInteger(1, digest).toString(16);
		return stChecksum;
	}

	public FTPClient uploadPathCreator(String stFilePath, FTPClient ftpclient) {
		String[] pathElements = stFilePath.split("/");
		try {
			ftpclient.setFileType(FTP.BINARY_FILE_TYPE);
			String FilePath = "";
			int flag = checkhost.gethost();
			if (flag==1) {
				FilePath = ProductionFilePath;
			} else {
				if (flag==2) {
					FilePath = UATFilePath;
				} else {
					FilePath = LocalFilePath;
				}
			}
			ftpclient.changeWorkingDirectory(FilePath);
			System.out.println("changeWorkingDirectory: " + FilePath);
			System.out.println("pathElements: " + Arrays.toString(pathElements));
			if (pathElements != null && pathElements.length > 0) {
				for (String pathElement : pathElements) {
					ftpclient.changeWorkingDirectory(pathElement);

					int returnCode = ftpclient.getReplyCode();
					showServerReply(ftpClient);
					if (returnCode == 550) {
						ftpclient.makeDirectory(pathElement);
						ftpclient.changeWorkingDirectory(pathElement);
					}
				}
			} else {
				System.out.println("Invalid Path: " + stFilePath);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ftpclient;
	}

	public InputStream downloadfromFtp(String FilePath, String fileName)
			throws FTPConnectionClosedException, IOException, Exception {
		FTPClient ftpclient = connectToServer();
		System.out.println(FilePath);
		System.out.println(ftpclient.getReplyString());
		setFileType(FTP.BINARY_FILE_TYPE);
		ftpclient = uploadPathCreator(FilePath, ftpclient);
		FilePath = FilePath + fileName;
		System.out.println(FilePath);
		System.out.println(Arrays.toString(ftpclient.listFiles()));
		InputStream inputStream = ftpclient.retrieveFileStream(fileName);
		return inputStream;
	}
	
	public String uploadToFtpwithCustomFileName(MultipartFile upload_file, String FilePath,String FileName)
			throws FTPConnectionClosedException, IOException, Exception {
		InputStream inputstream = upload_file.getInputStream();
		FTPClient ftpclient = connectToServer();
		setFileType(FTP.BINARY_FILE_TYPE);
		String checksum = GenerateCheckSum(inputstream);
		ftpclient = uploadPathCreator(FilePath, ftpclient);
		String[] FileExtensionTemp = upload_file.getOriginalFilename().split("\\.");
		String FileExtension = FileExtensionTemp[FileExtensionTemp.length - 1];
		boolean upload = ftpclient.storeFile(FileName + "." + FileExtension, upload_file.getInputStream());
		closeConnect();
		if (!upload) {
			checksum = "0";
		}
		return checksum;
	}

	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}
}
