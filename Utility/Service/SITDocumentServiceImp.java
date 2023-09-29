package eBIS.Utility.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eBIS.AppConfig.FileUploadPathPropertiesBundleFile;
import eBIS.AppConfig.FtpConfiguration;
import eBIS.Utility.DAO.SITDocumentDAO;
import eBIS.Utility.Model.SITDocumentModel;
import lombok.var;

@Service
public class SITDocumentServiceImp implements SITDocumentService {

	@Autowired
	SITDocumentDAO dao;

	final String SITFilePath = FileUploadPathPropertiesBundleFile.getValueFromKey("sti.file.upload.path");
	@Autowired 
	FtpConfiguration ftpConfiguration;
	@Override
	public FastList<SITDocumentModel> getSITDocumentList(String standardNumber) {
		var list = dao.getSITDocumentList(standardNumber);
		return ConvertEntity2Model(list);
	}

	private FastList<SITDocumentModel> ConvertEntity2Model(List<Map<String, Object>> list) {
		FastList<SITDocumentModel> model = new FastList<SITDocumentModel>();
		for (int i = 0; i < list.size(); i++) {
			var tempmodel = new SITDocumentModel();
			var domain = list.get(i);
			tempmodel.setStandardNumber(domain.get("str_standard_no").toString());
			tempmodel.setSitDocumentName(domain.get("sti_doc_name").toString());
			tempmodel.setStandardYear(domain.get("num_standard_year").toString());
			tempmodel.setSitDocumentCheckSum(domain.get("str_doc_chksum_name").toString());
			tempmodel.setSitNumId(domain.get("num_id").toString());
			model.add(tempmodel);
		}
		return model;
	}

	@Override
	public String getSITDocumentHTML(String standardNumber,String numId) {
		String sit = "";
		FastList<SITDocumentModel> list = getSITDocumentList(standardNumber);
		if (list.size() > 0) {
			sit += "<table id='sittable' class='footable pad-top bg-head table table-bordered table-striped table-hover dataTable'>";
			sit += "<thead>";
			sit += "<tr>";
			sit += "<th></th>";
			sit += "<th>S.No</th>";
			sit += "<th>Document Name</th>";
			sit += "<th>Download</th>";
			sit += "</tr>";
			sit += "<tbody>";
			for (int j = 0; j < list.size(); j++) {
				String docName = list.get(j).getSitDocumentName();
				String checksum = list.get(j).getSitDocumentCheckSum();
				String sitNumId=list.get(j).getSitNumId();
				sit += "<tr>";
				if(numId.equalsIgnoreCase(sitNumId))
				{
					sit+="<td><input type='radio' id='sitselect"+j+"' checked name='sitselect' value='"+sitNumId+"@"+checksum+"' class='sitradio' onchange='sitchange(this.value)'/> </td>";
				}else {
					sit+="<td><input type='radio' id='sitselect"+j+"' name='sitselect' value='"+sitNumId+"@"+checksum+"' class='sitradio' onchange='sitchange(this.value)'/></td>";
				}
				sit += "<td>" + (j + 1) + "</td>";
				sit += "<td>" + docName + "</td>";
				sit += "<td><button class='glyphicon glyphicon-download-alt btn btn-default' onclick='downloadSIT(this.id)' id='"
						+ checksum + "' type='button' ></button></td>";
				sit += "</tr>";
			}
			sit += "</tbody>";
			sit += "</thead>";
			sit += "</table> ";
		} else {
			sit = "SIT Details will be further notified.";
		}
		return sit;
	}

	@Override
	public InputStream DownloadSITFile(FTPClient ftpClient,String fileName) throws FTPConnectionClosedException, IOException, Exception {
		String path=SITFilePath;
		var istream=ftpConfiguration.downloadfromFtp(path, fileName);//removed argument type ftpClient on 5/12/22 because argument no declare in method is 2 only
		return istream;
	}


}
