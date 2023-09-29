package eBIS.Utility.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SITDocumentDAO {
	@Autowired
    @Qualifier(value="jdbcTemplateReadonly")
	JdbcTemplate jdbcTemplate;
	
	public List<Map<String, Object>> getSITDocumentList(String standardNumber) {
		String sql="SELECT c.num_id,c.str_standard_no, c.str_sti_doc as sti_doc_name, replace(trim(nvl(c.str_doc_chksum_name,'0')),'','0') as str_doc_chksum_name,\r\n"
				+ "c.num_standard_year \r\n"
				+ "from bis_dev.gblt_sti_document_mst c  \r\n"
				+ "where trim(c.str_standard_no)=trim('"+standardNumber+"') \r\n"
				+ "and replace(trim(nvl(c.str_doc_chksum_name,'0')),'','0')!='0' and c.num_isvalid=1";
		System.out.println("getSITDocumentDAO: "+sql);
		return jdbcTemplate.queryForList(sql);
	}
}
