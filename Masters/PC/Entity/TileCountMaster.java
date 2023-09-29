package eBIS.Masters.PC.Entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name = "tilecountmaster", schema = "bis_masters")
public class TileCountMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TileCountMasterGenerator")
	@SequenceGenerator(name = "TileCountMasterGenerator",schema="bis_masters", sequenceName = "bis_masters.TileCountMasterGeneratorSequence", initialValue = 1, allocationSize = 1)
	private Long numid;
	private Date entrydate=new Date();
	private String url="";
	private long count=0;
}
