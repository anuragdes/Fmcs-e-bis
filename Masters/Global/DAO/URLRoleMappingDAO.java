package eBIS.Masters.Global.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import eBIS.AppConfig.PrimaryDaoHelper;
import eBIS.Masters.Global.Entity.URLRoleMapping;

@Repository
public class URLRoleMappingDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	PrimaryDaoHelper primaryDaoHelper;
	public int checkRoleAuthorization(String urlName, String roleId) {
		String sql="select trim(z.str_tile_url) as str_tile_url,z.tile_role from (\r\n"
				+ "select distinct  regexp_split_to_table(string_agg(y.tile_url,',') ,',') as str_tile_url ,y.tile_role from (\r\n"
				+ "select  distinct  k.str_tile_url ||','||c.l1||','||c.l2 as tile_url,c.role_id as tile_role from (\r\n"
				+ "select b.num_parent_id as p1,a.num_parent_id as p2,b.k1 as l1,b.k2 as l2,b.num_role_id as role_id ,* from (\r\n"
				+ "select gtd.num_role_id,gtd.str_tile_url as k1 ,gtm.num_parent_id,gtm.str_tile_url as k2\r\n"
				+ "from bis_dev.gblt_tile_mst gtm , bis_dev.gblt_tile_data gtd \r\n"
				+ "where gtd.num_tile_id = gtm.num_tile_id\r\n"
				+ ") b,bis_dev.gblt_tile_mst a\r\n"
				+ " where a.num_tile_id =b.num_parent_id\r\n"
				+ " ) c,\r\n"
				+ " bis_dev.gblt_tile_mst k\r\n"
				+ " where k.num_tile_id =c.p2\r\n"
				+ " and c.role_id='"+roleId+"'\r\n"
				+ " ) y\r\n"
				+ " group by y.tile_role\r\n"
				+ "  union \r\n"
				+ " select urlname as str_tile_url,roleid::int as tile_role  from bis_masters.urlrolemapping u where u.roleid in (0,"+roleId+")\r\n"
				+ " union \r\n"
				+ " select l1.str_form_url as str_tile_url, num_role_id as tile_role from(\r\n"
				+ "  select num_role_id ,regexp_split_to_table(str_form_id,',') str_form_id  from  bis_dev.gblt_role_form_mapping_new b where b.num_isvalid =1 and str_form_id !=''\r\n"
				+ "  and num_role_id="+roleId+" \r\n"
				+ " )k, bis_dev.gblt_form_mst_new l1\r\n"
				+ " where \r\n"
				+ " str_form_id=l1.num_form_id\r\n"
				+ " and \r\n"
				+ "l1.num_isvalid =1\r\n"
				+ ") z where z.str_tile_url='"+urlName+"' \r\n"
				+ "order by 1 asc";
		Integer flag=0;
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(list.size()>0) {
			for(int i=0;i<list.size();i++) {
				 Map<String, Object> entity = list.get(i);
				String tileUrl = entity.get("str_tile_url").toString();
				if(tileUrl.equalsIgnoreCase(urlName)) {
					flag=1;
					break;
				}
			}
		}else {
			flag=0;
		}
		if(flag==0) {
			String hql="select c from  URLRoleMapping c where c.urlName='"+urlName.trim()+"'";
			int size = primaryDaoHelper.findByQuery(hql).size();
			if(size==0) {
				URLRoleMapping entity=new URLRoleMapping();
				entity.setRoleId("0");
				entity.setUrlName(urlName);
				primaryDaoHelper.merge(entity);	
			}
			
		}
		list = jdbcTemplate.queryForList(sql);
		if(list.size()>0) {
			for(int i=0;i<list.size();i++) {
				 Map<String, Object> entity = list.get(i);
				String tileUrl = entity.get("str_tile_url").toString();
				if(tileUrl.equalsIgnoreCase(urlName)) {
					flag=1;
					break;
				}
			}
		}else {
			flag=0;
		}
		return flag;
	}
}
