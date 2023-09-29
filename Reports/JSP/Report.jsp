<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.Connection" %>
<%@page import="java.sql.DriverManager" %>
<%@page import="java.sql.SQLException" %>
<%@page import="java.sql.PreparedStatement" %>
<%@page import="java.sql.ResultSet" %>
<%@page import="java.sql.ResultSetMetaData" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.LinkedHashMap" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.LinkedHashSet" %>
<%@page import="java.util.Set" %>
<%@page import="java.util.TreeSet" %>
<%@page import="java.util.SortedSet" %>
<%@page import="java.sql.Types" %>
<%@page import="javax.naming.InitialContext" %>
<%@page import="javax.naming.Context" %>
<%@page import="javax.sql.DataSource" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Report</title>

<!-- media="print" means these styles will only be used by printing devices -->
<style type="text/css" media="print">
    .printable{ 
      page-break-after: always;
    }
    .no_print{
      display: none;
    }
</style>

<style>
svg {
  display: block;
}

#barChart svg{
  height: 300px;
  min-width: 100px;
  min-height: 100px;
}

#multiBarChart {
  height: 300px;
  margin: 10px;
  min-width: 100px;
  min-height: 100px;
}

</style>

<link href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/Report/nv.d3.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/dragtable.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/multiLevelSort.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/sortingWithGroup.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/groupByTable.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/multiLevelFilterTable.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/jquery-1.8.2.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/Ajax.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/swfobject.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/downloadify.min.js"></script>

<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/createPieChart.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/jquery.flot.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/Js/Report/jquery.flot.pie.js"></script>


<!--[if lte IE 8]><script language="javascript" type="text/javascript" src="js/excanvas.min.js"></script><![endif]-->

<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/d3.v2.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/nv.d3.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/tooltip.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/utils.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/axis.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/discreteBar.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/discreteBarChart.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/legend.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/multiBar.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/Report/multiBarChart.js"></script>


<script type="text/javascript">

	var isGrouped = 0;
	var groupedObj;
	
	function hideLegend(divId){
	
		if (stopFetch == 0){
			alert ("Please wait for all the Records to be Fetched \nOR \nPress 'Stop Fetching Records' button.");
			return false;
		}
		$('#'+divId).slideToggle("slow");
	}
	
	function upLegend(divId){
		$('#'+divId).slideUp("slow");
	}

	var stopFetch = 1;
	var nextStart = 0;
		
	function addOptionToCombo (combo, text, value){
		var option = document.createElement("option");
	    option.text = text;
	    option.value = value;
	    try {
	    	combo.add(option, null); //Standard
	    }catch(error) {
	    	combo.add(option); // IE only
	    }
	}
	
	function moveItemInCombo(leftId, rightId){
		var leftCombo = document.getElementById(leftId);
		var rightCombo = document.getElementById(rightId);
		for (var i=0, len=leftCombo.options.length;i<len;++i){			
			if (leftCombo.options[i].selected == true){
				var option = document.createElement("option");
			    option.text = leftCombo.options[i].value;
			    option.value = leftCombo.options[i].value;
			    try {
			    	rightCombo.add(option, null); //Standard
			    }catch(error) {
			    	rightCombo.add(option); // IE only
			    }
			    leftCombo.remove(i--);
			}
		}
	}
	
	function moveOptionsDown(selectId) {
		var selectList = document.getElementById(selectId);
	 	var selectOptions = selectList.getElementsByTagName('option');
	 	for (var i = selectOptions.length - 2; i >= 0; i--) {
	  	var opt = selectOptions[i];
	  	if (opt.selected) {
	   		var nextOpt = selectOptions[i + 1];
	   		opt = selectList.removeChild(opt);
	   		nextOpt = selectList.replaceChild(opt, nextOpt);
	   		selectList.insertBefore(nextOpt, opt);
	     	}
	 	}
	}

	function moveOptionsUp(selectId) {
 		var selectList = document.getElementById(selectId);
 		var selectOptions = selectList.getElementsByTagName('option');
 		for (var i = 1, len=selectOptions.length; i < len; i++) {
	  		var opt = selectOptions[i];
	  		if (opt.selected) {
	   			selectList.removeChild(opt);
	   			selectList.insertBefore(opt, selectOptions[i - 1]);
	     	}
    	}
	}
	
	function hideCol(obj){		
		var tab = document.getElementById('RESULT');
		var rows = tab.rows;
		var indexToHide = -1;
		for (var i=1, len=rows[0].cells.length;i<len;++i){
			if (rows[0].cells[i].id == obj.value){
				indexToHide = i;
				break;
			}
		}
		
		var i=-1;
		document.getElementById('wait').style.display = "";
		obj.disabled = true;
		if (obj.checked == true && indexToHide != -1){
			function loop(){
				if (i !=- 1)
					for (var j=0, len=rows.length;j<len;++j)
						rows[j].cells[indexToHide].style.display = 'none';
				if (i++ == -1)
					setTimeout(loop, 0);
				else{
					document.getElementById('wait').style.display = "none";
					obj.disabled=false;
				}
			}
			loop();
		}
		else if (obj.checked == false && indexToHide != -1){
			function loop1(){
				if (i != -1)
					for (var j=0, len=rows.length;j<len;++j)
						rows[j].cells[indexToHide].style.display = '';
				if (i++ == -1)
					setTimeout(loop1, 0);
				else{
					document.getElementById('wait').style.display = "none";
					obj.disabled=false;
				}
			}
			loop1();
		}
	}
	
	function doPrint(){		
		//window.print();
		printTable();	
	}
		
	function printTable() {
	    var disp_setting = "toolbar=yes,location=no,directories=yes,menubar=yes,";
	    disp_setting += "scrollbars=yes,width=1350, height=800";
	    var content_vlue = $("#RESULT").parent().html();
	    var headers = $("#Report_Header").html();
	    var docprint = window.open("", "", disp_setting);
	    docprint.document.open();
	    docprint.document.write('<link href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/print.css" type="text/css" rel="stylesheet" />');  
	    docprint.document.write('</head><body onLoad="self.print()"><center>');
	    docprint.document.write(headers);
	    docprint.document.write(content_vlue);
	    docprint.document.write('</center></body></html>');
	    docprint.document.close();
	    docprint.focus(); 
	}		
		
	function reset(){
		var tab = document.getElementById('RESULT');
		var rows = tab.rows;
		var countSno = 1;
		for (var i=1, len=rows.length;i<len;++i){
			if (rows[i].className.search(/\bsorttable_dontsort\b/) == 0)continue;		//check
			rows[i].style.display = "";
			rows[i].cells[0].innerHTML = "<b>" + countSno + ".</b>";
			countSno++;
		}
		document.getElementById('TOTAL').innerHTML = "<font face='verdana' size='4'>Total Records " + (countSno-1) + "</font>";
		document.getElementById('filterHeader').innerHTML = "";
		var filterCombo = document.getElementsByName('filterCombo');
		for (var i=0, len=filterCombo.length;i<len;++i)
			filterCombo[i].value = "-1";
			
		if (document.getElementById('isTotalDIV')){
			for (var k=1, len=rows[0].cells.length;k<len;++k){
				var elm = document.getElementById("textTotal-" + trim(rows[0].cells[k].id));
				if (elm){						
					elm.innerHTML = totalArray["textTotal-" + trim(rows[0].cells[k].id)];
				}
			}
		}
	}	
	
	function loadSwfFile(){
		Downloadify.create('downloadify',{
			filename: function(){
				return "Report.csv";
			},
			data: function(){
				var data = "";
				var tab = document.getElementById('RESULT');
				var rows = tab.rows;
				data += "S.No.,";
				for (var j=1;j<rows[0].cells.length;++j){
					if (rows[0].cells[j].style.display == "none") continue;
					data += "\"" + rows[0].cells[j].id + "\",";
				}
				data += "\n";
				for (var i=1;i<rows.length;++i){
					if (rows[i].className.search(/\bsorttable_dontsort\b/) == 0)continue;		//check
					if (rows[i].style.display == "none") continue;
					var cells = rows[i].cells;
					data += i + ",";
					for (var j=1;j<cells.length;++j){
						if (cells[j].style.display == "none") continue;
						data += "\"" + cells[j].innerHTML.replace(/^\s+|\s+$/g, '') + "\",";
					}
					data += "\n";
				}
				return data;
			},
			onComplete: function(){ alert('Your File Has Been Saved!'); },
			onCancel: function(){},
			onError: function(){ alert('You must put something in the File Contents or there will be nothing to save!'); },
			swf: '/MANAK/resources/app_srv/GlobalPages/CommonUtility/media/downloadify.swf',
			downloadImage: '/MANAK/resources/app_srv/GlobalPages/CommonUtility/Images/download.png',
			width: 120,
			height: 30,
			transparent: true,
			append: false
		});
	}
	
	var totalArray = [];
	function initializeBody(){
		loadSwfFile();
		initMultiSort();
		if (document.getElementById('isGroupDIV'))
			initOriginal();
		//creatingChart();
		var tab = document.getElementById('RESULT');
		var rows = tab.rows;
		if (document.getElementById('isTotalDIV')){
			for (var k=1, len=rows[0].cells.length;k<len;++k){
				var elm = document.getElementById("textTotal-" + trim(rows[0].cells[k].id));
				if (elm){						
					totalArray["textTotal-" + trim(rows[0].cells[k].id)] = trim(elm.innerHTML);
				}
			}
		}
	}
	
</script>

</head>
<body style="overflow: visible;" onload="initializeBody()">

<%
    
	Connection con = null;
	String errorMsg = "";
int dataSource = 1;
//String dataSourceName = "jdbc/dataSourceTarget";
String query = "Select * from gblt_designation_mst_log";
String queryDate = "select to_char(now(), 'DD Mon YYYY HH24:MI:SS')";
String reportHeader1 = "";
String reportHeader2 = "";
String reportHeader3 = "";
String[] paramName = null;
String[] paramGroupName = null;
String[] paramTotalName = null;

	String formParamNames = request.getParameter("formParamNames");
	if (formParamNames != null && !formParamNames.equals("")){
		Map<Integer, String> oprMap = new HashMap<Integer, String>();
		oprMap.put(1, "=");
		oprMap.put(2, "<>");
		oprMap.put(3, ">");
		oprMap.put(4, ">=");
		oprMap.put(5, "<");
		oprMap.put(6, "<=");
		oprMap.put(7, "Like");
		oprMap.put(8, "Between");
		oprMap.put(9, "In");
		oprMap.put(10, "Not In");
		
		String newQuery = "select z_z.* from (" + query + ") z_z where ";
		String[] pNames = formParamNames.split("##");
		for (int i=0;i<pNames.length;++i){
			String name = "z_z." + pNames[i].split("#")[0].trim().replaceAll(" ", "_");
			if (pNames[i].split("#")[1].equals("3"))
				newQuery +=  name +" >= " + " to_date('" + pNames[i].split("#")[2] + "','dd-MM-yyyy') and " + name + " <=  to_date('" + pNames[i].split("#")[3] + "','dd-MM-yyyy') and ";
			else{ 
				String opr = oprMap.get(Integer.valueOf(pNames[i].split("#")[3]));
				String val = pNames[i].split("#")[2];
				String type = pNames[i].split("#")[1];
				if (opr.equalsIgnoreCase("like") && type.equals("2")){
					val = "'%" + val.toUpperCase() + "%'";
				}
				if (opr.equalsIgnoreCase("like") && type.equals("1")){
					val = "%" + val + "%";
				}
				if (type.equals("2"))
					newQuery += name + " " + opr + " " + val + " and ";
				else if (type.equals("1"))
					newQuery += " upper(" + name + ") " + opr + " '" + val.toUpperCase() + "' and ";
			}
		}
		newQuery = newQuery.substring(0, newQuery.length()-5);
		query = newQuery;
	}	
	try {
		 Class.forName("org.postgresql.Driver");
		 con=DriverManager.getConnection("jdbc:postgresql://10.226.1.10:5444/BIS_development","bisdev","bisdev");
		/* DataSource ds = null;
		InitialContext ic = new InitialContext();
		try {
			 ds = (DataSource) ic.lookup(dataSourceName);
		} catch (Exception e){
			Context xmlContext = (Context) ic.lookup("java:comp/env");
			ds = (DataSource) xmlContext.lookup(dataSourceName);	
		}		
		con = ds.getConnection();*/	
	} catch (Exception e){
		con = null;
		errorMsg = e.getMessage(); 
	}
%>


<%if (con == null) { %>

<font face="verdana" size="4" color="red"> Could not establish connection to the Server --- <%=errorMsg %>
</font>

<%} else {
	boolean queryRes = true;	
	PreparedStatement stmt = null;
	PreparedStatement stmtDate = null;
	ResultSet rs = null;
	ResultSet rsDate = null;
	ResultSetMetaData rsmd = null;
	String sysdate = "";
	try{
		stmt = con.prepareStatement(query);
		rs =  stmt.executeQuery();
		rsmd = rs.getMetaData();
		stmtDate = con.prepareStatement(queryDate);
		rsDate = stmtDate.executeQuery();
		if (rsDate.next())
			sysdate = rsDate.getString(1);
	} catch (Exception e){
		queryRes = false;
		errorMsg = e.getMessage();
	} 
	String[] colList = null;
	String[] colName = null;
	List<Map<String, String>> queryResult = null;
	Map<String, SortedSet<String>> uniqueColValues = new LinkedHashMap<String, SortedSet<String>>();
	Map<String, SortedSet<String>> uniqueGroupValues = new LinkedHashMap<String, SortedSet<String>>();
	Map<String, Double> totalColValues = new LinkedHashMap<String, Double>();
	List<String> chartXAxis = new ArrayList<String>();
	List<String> chartYAxis = new ArrayList<String>();
	
	int totalResult = 0;
	int colCount = 0;
	boolean filter = false;
	boolean group = false;
	boolean total = false;
	boolean[] isNumeric = null;	
	if (queryRes == true){
		colCount = rsmd.getColumnCount();
		isNumeric = new boolean[colCount];
		 colList = new String[rsmd.getColumnCount()];
		for (int i=1;i<=rsmd.getColumnCount();++i){
			isNumeric[i-1] = false;
			colName = rsmd.getColumnName(i).split("_");
			String cName = colName[0].substring(0,1).toUpperCase() + colName[0].substring(1).toLowerCase();
			for (int j=1;j<colName.length;++j)
				cName += " " + colName[j].substring(0,1).toUpperCase() + colName[j].substring(1).toLowerCase();
			colList[i-1] = cName;
			
			if (rsmd.getColumnType(i) == Types.VARCHAR || rsmd.getColumnType(i) == Types.TIME || rsmd.getColumnType(i) == Types.TIMESTAMP)
				chartXAxis.add(cName);
			if (rsmd.getColumnType(i) == Types.DATE)
				chartXAxis.add(0, cName);
			if (rsmd.getColumnType(i) == Types.INTEGER || rsmd.getColumnType(i) == Types.NUMERIC) {
				chartYAxis.add(cName);
				isNumeric[i-1] = true;
			}
		}		
		
		queryResult = new ArrayList<Map<String, String>>();		
		if (paramName!=null && paramName.length > 0){
			filter = true;
			for (int i=0;i<paramName.length;++i){
				SortedSet<String> colSet = new TreeSet<String>();
				uniqueColValues.put(paramName[i], colSet);
			}
		}
		
		if (paramGroupName!=null && paramGroupName.length > 0){
			group = true;
			for (int i=0;i<paramGroupName.length;++i){
				SortedSet<String> colSet = new TreeSet<String>();
				uniqueGroupValues.put(paramGroupName[i], colSet);
			}
		}
				
		if (paramTotalName != null && paramTotalName.length > 0){
			total = true;
			for (int i=0;i<paramTotalName.length;++i){
				totalColValues.put(paramTotalName[i], 0.0);
			}
		}
				
		while (rs.next()){
			totalResult++;
			Map<String, String> row = new LinkedHashMap<String, String>();
			for (int i=1;i<=colCount;++i){
				row.put(colList[i-1], rs.getString(i));
				if (filter){
					if (uniqueColValues.containsKey(colList[i-1]))
						uniqueColValues.get(colList[i-1]).add(rs.getString(i)==null?"":rs.getString(i));
				}
				if (group){
					if (uniqueGroupValues.containsKey(colList[i-1]))
						uniqueGroupValues.get(colList[i-1]).add(rs.getString(i)==null?"":rs.getString(i));
				}
				if (total){
					if (totalColValues.containsKey(colList[i-1])){
						try {
							totalColValues.put(colList[i-1], totalColValues.get(colList[i-1]) + Double.parseDouble(rs.getString(i)));
						}catch (NumberFormatException e){
							e.printStackTrace();
						}
					}
				}
			}
			queryResult.add(row);
		}
		if (con != null){
			try {
				con.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
%>

<% if (queryRes == false){
%>
<font face="verdana" size="4" color="red"> Error in Executing Query ---  <%=errorMsg %>
</font>
<%} else{ %>

	<table width="98%" align="center">
	<tr>
	<td width="100%"  class="no_print">
	<fieldset style="border-color:  #575757;">
		<legend><font  style="font-family: Trebuchet MS;color: #575757; font-weight: bold;"><span onclick="hideLegend('controlPanel');" style="cursor: pointer;text-decoration: underline;">Report Control panel</span></font></legend>
		<div id="controlPanel" style="display: none">
		<table width="100%" align="center">
		<tr id="removeColumnRow">
			<td width="100%">
			<fieldset style="border-color:  #575757;">
			<legend><font  style="font-family: Trebuchet MS;color: #575757; font-weight: bold;"><span onclick="hideLegend('divRemoveColumnRow');" style="cursor: pointer;text-decoration: underline;">Remove Column(s)</span></font></legend>
				<div id="divRemoveColumnRow"  style="display: none;">
				<table width="100%">
				<tr  style="font-family: verdana; font-size: 0.9em">
				  <%for (int i=0;i<colCount;i++){
  				  %>
					<td>
						<input type="checkbox" value="<%=colList[i] %>" onclick="hideCol(this)"/>
						<font face="verdana" size="2"><%=colList[i] %>
						</font>
					</td>
				  <%} %>
				</tr>
				</table>
				</div>
			</fieldset>
			</td>
		</tr>
		
		<tr id="multiLevelSort" class="no_print">
			<td width="100%">
			<fieldset style="border-color:  #575757;">
			<legend><font  style="font-family: Trebuchet MS;color: #575757; font-weight: bold;"><span onclick="hideLegend('divMultiLevelSort');" style="cursor: pointer;text-decoration: underline;">Sort Column(s)</span></font></legend>
			<div id="divMultiLevelSort" style="display: none;">
				<table width="100%">
					<tr align="center">
					<td width="30%" align="right"><font face="verdana" size="2" style="font-weight: bold;">All Columns</font>
					</td>
					<td width="10%" align="center">
					</td>
					<td width="30%" align="left"><font face="verdana" size="2" style="font-weight: bold;">Columns To Sort</font>
					</td>
					</tr>
					<tr align="center">
					<td width="15%" align="right"><font face="verdana" size="2"> 
						<select id="multiLevelSortLeftCombo" multiple="multiple" size="8" style="width: 150px;">
						<%for (int i=0;i<colCount;i++){
  				  		%>
							<option value='<%=colList[i] %>'><%=colList[i] %></option>
				  		<%} %>
						</select>
						</font>
					</td>
					<td width="10%" align="center">
						<input type="button" value="&gt;" style="width: 50px;" onclick="moveItemInCombo('multiLevelSortLeftCombo', 'multiLevelSortRightCombo')"/><br>
						<input type="button" value="&lt;" style="width: 50px;" onclick="moveItemInCombo('multiLevelSortRightCombo', 'multiLevelSortLeftCombo')"/><br>
					</td>
					<td width="15%" align="left">
						<div style="vertical-align: middle;">
						<div style="text-align: left;float: left;">
						<font face="verdana" size="2">
						<select id="multiLevelSortRightCombo" multiple="multiple" size="8"  style="width: 150px;">
						</select>
						</font>
						</div>
						<div style="vertical-align: middle;float: left;padding-left: 10px;padding-top: 40px">
						<input type="button" value="&#x25B2" style="width: 50px;" onclick="moveOptionsUp('multiLevelSortRightCombo')"/><br>
						<input type="button" value="&#x25BC" style="width: 50px;" onclick="moveOptionsDown('multiLevelSortRightCombo')"/>
						</div>
						</div>						
					</td>
					</tr>
					<tr align="center">
						<td colspan="3" style="padding-top: 10px">
							<input type="button" name="multiLevelSortBtn" style="width: 120px;height: 30px;" onclick="sortMultiColumn('multiLevelSortRightCombo');" value="Sort" />
						</td>
					</tr>
				</table>
				</div>
			</fieldset>
			</td>
		</tr>
		
		<%if (filter == true) {%>
		<tr id="filterRow" class="no_print">
			<td valign="top" width="100%">
				<fieldset style="border-color:  #575757;">
				<legend><font  style="font-family: Trebuchet MS;color: #575757; font-weight: bold;"><span onclick="hideLegend('divFilterRow');" style="cursor: pointer;text-decoration: underline;">Filter Result</span></font></legend>
					<div id="divFilterRow"  style="display: none;">
					<table width="100%">
					<%int count1 = 0; int comboNo = 0;%>
						<%  for (Map.Entry<String, SortedSet<String>> entry: uniqueColValues.entrySet()){
						%>
						<%if (count1%5 == 0){ %>
						<tr style="font-family: verdana; font-size: 0.9em">
						<%} %>
							<td style="text-align: left;" width="25%">
								<b><%=entry.getKey() %></b><br>
								<select name="filterCombo" multiple="multiple" size="8" onclick="hideRows()" id="<%=entry.getKey() %>" style="width: 150px;">
									<option selected="selected" value="-1">All</option>
									<% for (String val: entry.getValue()) { %>
										<option value="<%=val %>"> <%=val %> </option>
									<%} %>
								</select>
							</td>						
						<%count1++; %> 
						<%} %>
						<tr>
							<td align="center" style="height: 5px">
							</td>
						</tr>
						<tr>
							<td colspan="4" style="text-align: center">
								<input type="button" style="width: 120px;height: 30px;" onclick="reset()" value="Reset" />
							</td>
						</tr>
					</table>
					</div>
				</fieldset>
			</td>
		</tr>
		<%} %>
		
		<%if (group == true) {%>
		<tr id = "groupByRow" class="no_print">
			<td valign="top" width="100%">
				<fieldset style="border-color:  #575757;">
				<legend><font  style="font-family: Trebuchet MS;color: #575757; font-weight: bold;"><span onclick="hideLegend('divGroupByRow');" style="cursor: pointer;text-decoration: underline;">Group Result</span></font></legend>
					<div id="divGroupByRow"  style="display: none;">
					<table width="100%">
					<%int count1 = 0; %>
						<%for (int i=0;i<paramGroupName.length;++i) { %>
						<%if (count1%4 == 0){ %>
						<tr style="font-family: verdana; font-size: 0.9em">
						<%} %>
							<td style="text-align: left;margin-right: 5px" width="25%">								
								<input type="checkbox" name="chkGroup-<%=paramGroupName[i] %>" value="<%=paramGroupName[i] %>" style="margin-right: 5px" onclick="groupBy(this)"/><%=paramGroupName[i] %>
								<%count1++; %> 
							</td>
						<%} %>
						<tr>
							<td align="center" style="height: 5px">
							</td>
						</tr>
						<tr>
							<td colspan="4" style="text-align: center">
								<input type="button" style="width: 120px;height: 30px;" onclick="restoreOriginal()" value="Reset" />
							</td>
						</tr>
					</table>
					</div>
				</fieldset>
			</td>
		</tr>
		<%} %>
		
		<tr id = "chartRow" class="no_print">
			<td valign="top" width="100%">				
				<fieldset style="border-color:  #575757;">
				<legend><font  style="font-family: Trebuchet MS;color: #575757; font-weight: bold;"><span onclick="hideLegend('divchartRow');" style="cursor: pointer;text-decoration: underline;">Analytics</span></font></legend>
					<div id="divchartRow" style="display: none">
					<table width="100%">
					<tr><td width="100%">
						<div id="divChartXAxis" style="float: left;">
						<table width="100%"> 
							<tr style="font-family: verdana; font-size: 0.9em">
								<th>								
									X Axis
								</th>
							<%boolean first = true; %>
							<%for (int i=0;i<chartXAxis.size();++i){ %>
								<td style="text-align: left;margin-right: 5px">	
									<%if (i==0){ %>							
									<input type="radio" checked="checked" name="radioChartXAxis" value="<%=chartXAxis.get(i) %>" onclick="creatingChart()" style="margin-right: 5px"/><%=chartXAxis.get(i) %>
									<%}else{ %>
									<input type="radio" name="radioChartXAxis" value="<%=chartXAxis.get(i) %>" onclick="creatingChart()" style="margin-right: 5px"/><%=chartXAxis.get(i) %>
									<%} %>
								</td>
							<%}%>
						</table>
						</div>
					</tr>
					<tr><td width="100%">
						<div id="divChartYAxis" style="float: left;">
						<table width="100%">
							<tr style="font-family: verdana; font-size: 0.9em">
								<th>								
									Y Axis
								</th>							
								<td style="text-align: left;margin-right: 5px">								
									<input type="radio" name="radioChartYAxis" checked="checked" value="0" style="margin-right: 5px" onclick="creatingChart()"/>Count
								</td>
							<%for (int i=0;i<chartYAxis.size();++i){ %>
								<td style="text-align: left;margin-right: 5px">
									<input type="radio" name="radioChartYAxis" value="<%=chartYAxis.get(i) %>" onclick="creatingChart()" style="margin-right: 5px"/><%=chartYAxis.get(i) %>
								</td>
							<%} %>
						</table>
						</div>			
						<div id="divCompChartYAxis" style="float: left;display: none;">
						<table width="100%">
							<tr style="font-family: verdana; font-size: 0.9em">
								<th>								
									Y Axis
								</th>							
								<td style="text-align: left;margin-right: 5px">								
									<input type="checkbox" name="chkChartYAxis" value="0" style="margin-right: 5px" onclick="creatingChart()"/>Count
								</td>
							<%for (int i=0;i<chartYAxis.size();++i){ 
								if ((1+1) % 9 == 0) {%>
									<tr><td>
								<%} %>
								<td style="text-align: left;margin-right: 5px">
									<input type="checkbox" name="chkChartYAxis" value="<%=chartYAxis.get(i) %>" style="margin-right: 5px"/><%=chartYAxis.get(i) %>
								</td>
							<%} %>
							<td style="text-align: left;">
								<input type="button" name="btnCompChart" value="Compare" onclick="createComparisionChart()" style="margin-right: 5px; margin-left: 20px;"/>
							</td>
						</table>
						</div>			
					</td>
					</tr>
					<tr>
						<td width="100%">
							<div id="CHART_TYPE_DIV" style="float: left;">
								<b>Chart Type</b>	
								<input type="radio" name="chartType" onclick="creatingChart()" value="pie" style="margin-left: 20px">Pie Chart
								<input type="radio" name="chartType" onclick="creatingChart()" value="bar" style="margin-left: 20px">Bar Chart
								<input type="radio" name="chartType" onclick="switchRadio()" value="multibar" style="margin-left: 20px">Comparision Bar Chart
							</div>
						</td>						
					</tr>
					<tr><td width="100%">
						<div id="Pie_Info" style="float: left; margin-right: 20px; display: none;">					
							<div id="divPieChart" class="graph" style="margin-left: 50px; width: 600px; height: 400px; float: left; border: 1px solid gainsboro;">
							</div>
							<div id="blank" style="float: bottom;">&nbsp;
							</div>							
						</div>
						<div id="legend_container" style="display: none; border: 1px solid gainsboro; float: left; background-color: #F8F8F8; max-width: 500px;">
						</div>
					</td></tr>
					
					<tr><td width="100%">
					<div id="barChart" style="display: none;">
						    <svg></svg>
						 </div>
					</td></tr>
					
					<tr><td width="100%">
					<div id="multiBarChart"  style="display: none;">
						    <svg></svg>
						 </div>
					</td></tr>
					
					<tr><td width="100%">
						<div id="hover" style="float: bottom; margin-left: 50px;">
							</div>
					</td></tr>
					
					</table>
					</div>					
				</fieldset>
			</td>
		</tr>
		
		<tr class="no_print">
			<td valign="top" width="100%">
				<div id='wait' style="display: none;font-weight: bold;color: green;font-size: 1.5em">Processing Please Wait...</div>
			</td>
		</tr>
		
		</table>
		</div></fieldset>
		</td></tr>
		
		<tr>
			<td valign="top" width="100%">
				<fieldset style="border-color:  #575757;">
				<legend id="reportLegend" class="no_print"><font  style="font-family: Trebuchet MS;color: #575757;"><b>Report</b></font></legend>
					<div style="text-align: left;" id="TOTAL">
						<font face="verdana" size="4">Total Records &nbsp;<%=totalResult %></font>
					</div>
					<div style="text-align: right;" class="no_print">
						<input type="button" style="width: 120px;height: 30px;" name="print" value="Print" onclick="doPrint()">
					</div>
					
					<div style="text-align: right;" class="no_print">
						<p id="downloadify">
							You must have Flash 10 installed to save file in CSV format.
						</p>
					</div>
					
					<div id="Report_Header">
						<table width="100%">
							<tr>
								<td align="left" valign="top" rowspan="8" id="logoFile">
								</td>
							</tr>
							<tr>
								<td align="center" id="rpt_head1">
									<font face="verdana" size="5"><%=reportHeader1 %></font>
								</td>
							</tr>
							<tr>
								 <td align="center" id="rpt_head2">
								 	<font face="verdana" size="4"><%=reportHeader2 %></font>
								</td>
							</tr>
							<tr>
								 <td align="center" style="height: 5px">
								</td>
							</tr>
							<tr>
								<td align="center" id="rpt_head3">
									<font style="font-family: verdana; font-size: 0.9em"><b><%=reportHeader3 %></b></font>
								</td>
							</tr>
							<tr>
								<td align="center">
								<div id="filterHeader">
									<font style="font-family: verdana; font-size: 0.9em"><b></b></font>
								</div>
								</td>
							</tr>
							<tr>
								 <td align="center" style="height: 5px">
								</td>
							</tr>
							<tr>
								<td align="right"> 
									<font style="font-family: verdana; font-size: 0.9em">Report Date : <%=sysdate %></font>
								</td>
							</tr>
						</table>
					</div>
					
					
					<div style="overflow: visible;" align="center">
					<table class="sortable draggable forget-ordering printable" width="100%" align="center" id="RESULT" style="overflow: visible;" border="1px" cellspacing="0" cellpadding="2">
			 		<%int rowCount = 0; %>
			 		 <thead>
			 		 <tr>	
			 		 <td style="background-color: #B0DCE2">
			 		 	<font face="verdana" size="2" style="background-color: #B0DCE2; cursor: pointer;" title="Click to Sort the Column"><b>S.No.</b></font>
			 		 </td>		 		 
			 		 <%for (int i=0;i<colCount;i++){
	  				  %>
						<td id="<%=colList[i] %>" style="background-color: #B0DCE2; cursor: pointer;" title="Click to Sort the Column" >
							<font face="verdana" size="2"><b><%=colList[i] %></b>
							</font>
						</td>
					  <%} %>
			 		 </tr>
			 		 </thead>			 		 	
					 <%for (int i=0;i<queryResult.size();i++){
						 Map<String, String> row = queryResult.get(i);
	  				  %>
						 <tr style="font-family: verdana; font-size: 0.9em">
						 	<td><%=(++rowCount) %>.
						 	</td>
					 		<%for (int j=0;j<colCount;j++){
 		  				    %><%if (isNumeric[j]) { %>
 		  				    <td style="text-align: right;">
 		  				    <%} else {%>
					 			<td>
					 			<%} %>
					 				<%=row.get(colList[j]) == null?"":row.get(colList[j]) %>
					 			</td>
					 		<%} %>
					 	</tr>
					 <%} %>
			 		</table>
			 		</div>
			 	</fieldset>
			</td>
		</tr>
		
	</table>
	<%if(group) {%>
	<div id="isGroupDIV" style="display: none;"></div>
	<%  for (Map.Entry<String, SortedSet<String>> entry: uniqueGroupValues.entrySet()){
	%>
	<select name="<%=entry.getKey() %>" style="width: 150px;display: none">
		<% for (String val: entry.getValue()) { %>
			<option value="<%=val %>"> <%=val %> </option>
		<%} %>
	</select>
	<%} }%>
	
	<%if(total) {%>
	<div id="isTotalDIV">
	<fieldset style="border-color:  #575757;">
		<legend id="totalLegend"><font  style="font-family: Trebuchet MS;color: #575757; font-weight: bold;">Total</font></legend>
			<table width="100%">
			<%  for (Map.Entry<String, Double> entry : totalColValues.entrySet()){
			%>
				<tr>		
					<td width="100%">
						<div id="labelTotal-<%=entry.getKey() %>" style="float:left; font-family: Trebuchet MS; font-weight: bold;"><%=entry.getKey() %> = </div> 
						<div id="textTotal-<%=entry.getKey() %>" style="float:left; font-family: Trebuchet MS; font-weight: bold; margin-left: 5px;"><%=entry.getValue() %></div>
					</td>
				</tr>
			<%} %>
			</table>
		</fieldset>
	</div>
	<%}%>
	
<%} %>
<%} %>
<input type="hidden" name="multiLevelSortIndex" />
<input type="hidden" name="sortClass" value="s0" />
<input type="hidden" name="sortingOrder" value="" />
</body>
</html>
