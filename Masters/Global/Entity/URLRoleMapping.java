package eBIS.Masters.Global.Entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name = "URLRoleMapping", schema = "bis_masters")
public class URLRoleMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "URLRoleMappingGenerator")
	@SequenceGenerator(name = "URLRoleMappingGenerator", schema = "bis_masters", sequenceName = "bis_masters.URLRoleMappingGeneratorSequence", initialValue = 1, allocationSize = 1)
	Integer numId;
	String isValid = "1";
	Date entryDate = new Date();
	String urlName = "";
	String roleId = "";
	@Version
	private long version;
}
