package eBIS.Utility.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.eclipse.collections.impl.list.mutable.FastList;

import Schemes.ProductCertification.ApplicationSubmission.Domain.user_profile_domain;
import eBIS.Utility.Model.OrganizationProfileModel;

public interface OrganizationProfileService {
	public List<user_profile_domain> getOrganizationProfileDetails(long userId);
	public FastList<OrganizationProfileModel> getOrganizationProfileModelDetails(long userId);
	InputStream DownloadPCOrgProfileFile(FTPClient ftpClient,String userID, String fileName) throws FTPConnectionClosedException, IOException, Exception;
	public int checkOrganizationProfile(long userId);
	
}
