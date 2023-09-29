package Applicant.DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;

@Repository
public class DGdashboardDAO {

	@Autowired
    @Qualifier(value="jdbcTemplateReadonly")
	JdbcTemplate jdbcTemplate;

	@Autowired
	DaoHelper daoHelper;

	private final ExecutorService pool = Executors.newFixedThreadPool(100);

	public long gettotalnooflicences(String fromDate, String toDate, String selectedBranchId) {

		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='51'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;

	}

	public ArrayList mazorIScml() {

		String query = " select (select substr(str_tech_comitee,1,3) as name from bis_dev.gblt_standard_mst c where c.str_standard_no=str_is_no and num_isvalid=1) as name, count(*) y from bis_dev.cml_licence_detail a,bis_dev.cml_licence_status_dtl b "
				+ " where a.str_cml_no=b.str_cml_no " + " and a.num_branch_id=b.num_branch_id "
				+ " and num_cml_status_id not in (205,206) group by 1 " + " order by 1 desc";

		return (ArrayList) jdbcTemplate.queryForList(query);

	}

	public ArrayList revenueyearwise() {

		String query = "select to_char(dt,'YYYY-Mon'),(select round(sum(amount)) from gblt_payment_gateway_dtls  where  trunc(to_date(tr_date))>dt-1 and trunc(to_date(tr_date))<add_months(dt,1)"
				+ " and payment_status='0300') Application_received" + " from"
				+ " (select to_date ('01-'||to_char(trunc(sysdate),'MM-YYYY')) - (interval '1' month * generate_series(0,11)) dt ) where trunc(dt)<=trunc(sysdate)  order by dt ";

		return (ArrayList) jdbcTemplate.queryForList(query);
	}

	public ArrayList applicationYearwise() {

		String query = " select to_char(dt,'YYYY-Mon'),(select count(*) from pc_application_submission a,pc_application_tracking b where a.str_application_id=b.str_application_id and a.num_location_id=b.num_branch_id and trunc(dt_registration_date)>dt-1 and trunc(dt_registration_date)<add_months(dt,12) "
				+ " and num_app_status=27) Application_received,"
				+ " (select count(*) from cml_renewal_tracking where num_renewal_tracking=89 and  trunc(dt_renewal_tracking)>dt-1 and trunc(dt_renewal_tracking)<add_months(dt,12))Renewal_received,"
				+ " (select count(*) from cml_inclusion_tracking where num_app_status=144 and  trunc(dt_entry_date)>dt-1 and trunc(dt_entry_date)<add_months(dt,12))inclusion_received"
				+ " from "
				+ " (select to_date ('01-Apr-'||to_char(trunc(sysdate),'YYYY')) - (interval '12' month * generate_series(0,14)) dt ) where trunc(dt)<=trunc(sysdate) and dt >'31-Mar-2017' order by dt ";

		return (ArrayList) jdbcTemplate.queryForList(query);
	}

	public ArrayList applicationMonthwise() {

		String query = " select to_char(dt,'YYYY-Mon'),(select count(*) from pc_application_submission a,pc_application_tracking b where a.str_application_id=b.str_application_id and a.num_location_id=b.num_branch_id and trunc(dt_registration_date)>dt-1 and trunc(dt_registration_date)<add_months(dt,1)"
				+ " and num_app_status=27) Application_received,"
				+ " (select count(*) from cml_renewal_tracking where num_renewal_tracking=89 and  trunc(dt_renewal_tracking)>dt-1 and trunc(dt_renewal_tracking)<add_months(dt,1))Renewal_received,"
				+ " (select count(*) from cml_inclusion_tracking where num_app_status=144 and  trunc(dt_entry_date)>dt-1 and trunc(dt_entry_date)<add_months(dt,1))inclusion_received"
				+ " from "
				+ " (select to_date ('01-'||to_char(trunc(sysdate),'MM-YYYY')) - (interval '1' month * generate_series(0,11)) dt ) where trunc(dt)<=trunc(sysdate) order by dt ";

		return (ArrayList) jdbcTemplate.queryForList(query);
	}

	public long gettotalnooflicencesThismonth(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='41'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getpendingapplication(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;
try {
		String query = "select count_query from bis_report_mst where num_id='10'";
		query = jdbcTemplate.queryForObject(query, String.class);
		query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
				selectedBranchId);
		Count = jdbcTemplate.queryForLong(query);
}
catch(Exception e) {
	
	e.printStackTrace();
	Count=-1;
}
		return Count;
	}

	public long getpendingapplicationThismonth(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='1'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getRenewalAppPendingatbranch(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='1036'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getRenewalAppPendingatbranchDue(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='64'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public Future<Long> getIncslusionApplication(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getInclsuionapplication(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getInclusionApplicationThisMonth(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getInclsuionapplicationThismonth(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getTotalExpirys(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getTotalExpiry(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getExpiryDues(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getExpiryDue(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getTRAwaiteds(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getTRAwaited(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getTRUploadeds(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getTRUploaded(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getTotalCancelations(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getTotalCancelation(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getTotalCancelationDUEs(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getTotalCancelationDUE(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getStandardUnderCertMANs(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getStandardUnderCertMAN(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getStandardUnderCertVOLs(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getStandardUnderCertVOL(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getAllIndiaFirstApp1(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getAllIndiaFirstApp(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getAllIndiaFirstAppthismonth1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getAllIndiaFirstAppthismonth(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getTotalDeferment1(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getTotalDeferment(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getDefermentMoreThan90days1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getDefermentMoreThan90days(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getMarketSurAssigned1(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getMarketSurAssigned(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getMarketSurCompleted1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getMarketSurCompleted(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getROPUnderprocess1(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getROPUnderprocess(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getROPUnderprocessdue1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getROPUnderprocessdue(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getRevenueFY1(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getRevenueFY(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getRevenueThisMonth1(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getRevenueThisMonth(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getpendingapplication1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getpendingapplication(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getpendingapplicationThismonth1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getpendingapplicationThismonth(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getRenewalAppPendingatbranch1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getRenewalAppPendingatbranch(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getRenewalAppPendingatbranchDue1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getRenewalAppPendingatbranchDue(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getInspectionAssigned1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getInspectionAssigned(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getInspectionCompleted1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getInspectionCompleted(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getCMLunderSOM1(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getCMLunderSOM(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> CMLunderSOM1801(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return CMLunderSOM180(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> gettotalnooflicences1(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return gettotalnooflicences(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> gettotalnooflicencesThismonth1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return gettotalnooflicencesThismonth(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public long getInclsuionapplication(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='168'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getInclsuionapplicationThismonth(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='169'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getInspectionAssigned(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='166'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getInspectionCompleted(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='73'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getCMLunderSOM(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='54'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long CMLunderSOM180(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='167'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getAllIndiaFirstApp(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='114'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getAllIndiaFirstAppthismonth(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='44'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getTotalDeferment(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='57'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getDefermentMoreThan90days(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='69'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getMarketSurAssigned(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='170'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getMarketSurCompleted(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='74'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getROPUnderprocess(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='171'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getROPUnderprocessdue(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		String query = "select 0";

		Count = jdbcTemplate.queryForLong(query);
		return Count;
	}

	public long getRevenueFY(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='178'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getRevenueThisMonth(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='104'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getTotalExpiry(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='172'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getExpiryDue(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='174'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getTRAwaited(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='177'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getTRUploaded(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='176'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getTotalCancelation(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='173'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getTotalCancelationDUE(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='175'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getStandardUnderCertMAN(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='1011'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getStandardUnderCertVOL(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='1012'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public String getAllBranchIds() {

		List queryresult = new ArrayList();

		String query = "select BM.numBranchId from Branch_Master_Domain BM where IsValid=1";

		String queryresults = "";
		queryresult = daoHelper.findByQuery(query);
		for (int j = 0; j < queryresult.size(); j++) {
			queryresults += String.valueOf(queryresult.get(j));
		}
		queryresults = queryresults.replaceAll("..(?!$)", "$0,");

		return queryresults;

	}

	public String getBranchIdsbylocationId(String locationId) {

		List queryresult = new ArrayList();

		String query = "select BM.numBranchId from Branch_Master_Domain BM where BM.numRoId='" + locationId
				+ "' and BM.IsValid = 1";

		String queryresults = "";
		System.out.println("queryresultsqueryresults" + query);
		queryresult = daoHelper.findByQuery(query);
		for (int j = 0; j < queryresult.size(); j++) {
			queryresults += String.valueOf(queryresult.get(j));
		}
		queryresults = queryresults.replaceAll("..(?!$)", "$0,");

		return queryresults;

	}

	public List<Map<String, Object>> getBranch(int locationId) {

		String query = "select num_branch_id as bid,str_branc_short_name as name from gblt_branch_mst where num_ro_id='"
				+ locationId + "' and num_isvalid=1 order by str_branch_detail";

		System.out.println("Query: for branches " + query);
		List<Map<String, Object>> runQry = new ArrayList<Map<String, Object>>();
		runQry = jdbcTemplate.queryForList(query);
		return runQry;
	}

	public List getMonthwiseApp() {

		String query = " SELECT count(*) count,to_char(dt_registration_date,'MONTH') monthname,"
				+ " to_char(dt_registration_date,'YYYY-MM') dateformat "
				+ " FROM pc_application_submission where trunc(dt_registration_date)>to_date('01-Apr-'||extract (year from add_months(trunc(sysdate),-3))) "
				+ " and num_application_status_id=27 and trunc(dt_registration_date)<trunc(sysdate)+1 "
				+ " and num_location_id<>41 group by 2,3 order by 3";

		return jdbcTemplate.queryForList(query);

	}

	public List getMonthwiseCML() {

		String query = " SELECT count(*)count, to_char(dt_granted_date,'MONTH') monthname, "
				+ " to_char(dt_granted_date,'YYYY-MM')date2 " + " FROM cml_licence_detail where " +

				"  trunc(dt_granted_date)>to_date('01-Apr-'||extract (year from add_months(trunc(sysdate),-3))) " +

				" and trunc(dt_granted_date)<trunc(sysdate)+1 " + " and num_branch_id<>41 " + " group by 2,3 "
				+ " order by 3 ";

		return jdbcTemplate.queryForList(query);
	}

	public ArrayList monthwiseapp() {

		String query = " select to_char(dt,'YYYY-Mon'),(select count(*) from pc_application_submission a,pc_application_tracking b where a.str_application_id=b.str_application_id and a.num_location_id=b.num_branch_id and trunc(dt_registration_date)>dt-1 and trunc(dt_registration_date)<add_months(dt,1)"
				+ " and num_app_status=27)"
				+ " Application_reeived,(select count(*) from cml_renewal_tracking where num_renewal_tracking=89 and  trunc(dt_renewal_tracking)>dt-1 and trunc(dt_renewal_tracking)<add_months(dt,1))Renewal_received,"
				+ " (select count(*) from cml_inclusion_tracking where num_app_status=144 and  trunc(dt_entry_date)>dt-1 and trunc(dt_entry_date)<add_months(dt,1))inclusion_received"
				+ " from "
				+ " (select to_date ('01-Apr-'||to_char(trunc(sysdate),'YYYY')) - (interval '1' month * generate_series(0,14)) dt ) where trunc(dt)<=trunc(sysdate) order by dt";

		return (ArrayList) jdbcTemplate.queryForList(query);

	}

	public String getBranchIds() {

		List queryresult = new ArrayList();

		String query = "select BM.numBranchId from Branch_Master_Domain BM ";

		String queryresults = "";
		queryresult = daoHelper.findByQuery(query);
		for (int j = 0; j < queryresult.size(); j++) {
			queryresults += String.valueOf(queryresult.get(j));
		}
		queryresults = queryresults.replaceAll("..(?!$)", "$0,");

		return queryresults;

	}

	public List<Map<String, Object>> getallRegions() {

		System.out.println("in regions");

		String query = "select  num_ro_id ,str_ro_name from gblt_regional_mst where num_isvalid=1 and num_ro_id not in (4)";
		System.out.println("in regions2");
		return jdbcTemplate.queryForList(query);

	}

	public List getStage(String headerid) {

		String query = "select num_id,header_id, stage_name,status_wise ,pendency_wise,status_code from bis_dashboard_mst where header_id='"
				+ headerid + "' and pendency_wise='left' order by num_id";

		return jdbcTemplate.queryForList(query);
	}

	public List getPendencywise(String headerid) {

		String query = "select num_id,header_id,status_wise from bis_dashboard_mst where header_id='" + headerid
				+ "' and pendency_wise='right' order by num_id ";

		return jdbcTemplate.queryForList(query);

	}

	public String getStageNamebyValue() {

		String queryresult = "";
		String query = "select DISTINCT stage_name from bis_dashboard_mst where num_id='1'";
		queryresult = jdbcTemplate.queryForObject(query, String.class);

		return queryresult;

	}

	public String getStageName(String headerid) {
		// select stage_name from bis_dashboard_mst where num_id='1'

		String queryresult = "";
		String query = "select distinct stage_name from bis_dashboard_mst where  num_id=(select min(num_id) from bis_dashboard_mst where header_id="+headerid+" )";
		queryresult = jdbcTemplate.queryForObject(query, String.class);
		System.out.println("headerid result is " + queryresult);
		return queryresult;

	}

	public String getHeaderReportListQueryId(String value) {

		String queryresult = "";
		String query = "select list_query from bis_dashboard_mst where num_id='" + value + "'";
		queryresult = jdbcTemplate.queryForObject(query, String.class);
		// System.out.println("query result is "+queryresult);
		return queryresult;

	}

	public List<Map<String, Object>> getAllHeaderReportQueryListDtl(String listQuery, String loc_id, String to_date,
			String FYFrom) {

		List<Map<String, Object>> listBisReportQueryList = new ArrayList<Map<String, Object>>();

		String query1 = listQuery.replaceAll("loc_id", loc_id).replaceAll("replaceToDate", to_date.trim())
				.replaceAll("replaceFromDate", FYFrom.trim());

		System.out.println("query for application:::::;;;;;;" + query1);

		try {
			listBisReportQueryList = jdbcTemplate.queryForList(query1);

		} catch (Exception e) {
			// : handle exception
			e.printStackTrace();
		}

		return listBisReportQueryList;
	}

	public List getIScmlDetailsList(String isno, String manvol, String loc_id) {

		System.out.println("String manvol...." + manvol);
		String query = " select  a.str_cml_no ,"
				+ " (select str_ro_name from gblt_regional_mst rm where rm.num_ro_id=bm.num_ro_id)as str_ro_name,str_branc_short_name,b.str_firm_name ,"
				+ " to_char(b.dt_Granted_date,'YYYY-MM-DD') as granted_date,to_char(b.dt_validity_date,'YYYY-MM-DD') as validity_date,"
				+ " b.str_is_no ||' : '||is_year as is_no,rr.str_status_name ,to_char(a.dt_status_date,'YYYY-MM-DD') as dt_status_date ,"
				+ " (select decode(num_scale_id,1,'MSME',3,'Large') from user_profile_details pd where  c.num_gbl_user_id=pd.num_entry_user_id and pd.num_id=(select max(num_id) from user_profile_details pd1 where pd1.num_entry_user_id=pd.num_entry_user_id))"
				+ " scale,nvl(dd.str_mandat_vol_sdoc,'Voluntary') as man_vol,str_standard_title"
				+ " from bis_dev.cml_licence_status_dtl a,bis_dev.cml_licence_detail b,bis_dev.pc_application_submission c,"
				+ " gblt_branch_mst bm ,gblt_status_mst rr ,bis_dev.gblt_standard_mst dd" + " where "
				+ " a.str_cml_no=b.str_cml_no" + " and a.num_branch_id=b.num_branch_id"
				+ " and b.str_app_id=c.str_application_id" + " and b.num_branch_id=c.num_location_id"
				+ " and b.num_branch_id=bm.num_branch_id" + " and rr.num_status_id=a.num_cml_status_id"
				+ " and dd.str_standard_no=b.str_is_no and dd.num_isvalid=1 and nvl(str_filename,'A') not like 'B%'"
				+ " and trunc(dt_status_date) < trunc(sysdate)+1" + " and num_cml_status_id  not in (205,206,207)"
				+ " and str_is_no = '" + isno + "' " + " and nvl(dd.str_mandat_vol_sdoc,'Voluntary')='" + manvol + "' "
				+ " and a.num_branch_id in (" + loc_id + ")";

		System.out.println("query::::::" + query);
		return jdbcTemplate.queryForList(query);

	}

	public List<HashMap> getEfficiencyIndex(int userid, int roleId, String branch, String month1, String year1) {

		List listApplication = new ArrayList();
		String strQuery = "";
		strQuery = " select eff_index_id,str_branch_name,simplified_factor as simplified,normal_factor as normal,"
				+ " scrutiny_factor as scrutiny,pi_factor as pi,inclusion_factor as inclusion,ir_factor ,tr_factor, "
				+ " eff_index as effindex,batch_id,d_rank from dashboard.data_manak_eff_index where batch_id="
				+ " (select max(a.batch_id) from dashboard.data_manak_eff_index a where a.d_month=" + month1
				+ " and d_year=" + year1 + ")  ORDER BY eff_index desc ";

		System.out.println("strQuery::::" + strQuery);
		return listApplication = jdbcTemplate.queryForList(strQuery);
	}

	public List getmonth() {
		List l = new ArrayList();
		String strQuery = "";
		strQuery = " select distinct d_month, decode((d_month), 0,'Quarter',to_char(to_date('01-'||d_month||'-1901'),'Mon'))from dashboard.data_manak_eff_index order by d_month ";

		return l = jdbcTemplate.queryForList(strQuery);
	}

	public List getYear() {
		List l = new ArrayList();
		String strQuery = "";
		strQuery = " select distinct d_year from dashboard.data_manak_eff_index  ";

		return l = jdbcTemplate.queryForList(strQuery);
	}

	public List<HashMap> getEfficiencyIndexEmp(int userid, int roleId, String branch, String month1, String year1) {
		List listApplication = new ArrayList();
		String strQuery = "";
		strQuery = " select str_emp_name, str_branch_name,normal_factor,simplified_factor,scrutiny_factor,pi_factor,inclusion_factor,ir_factor ,tr_factor,eff_index,d_rank"
				+ " from dashboard.data_manak_eff_index_emp where batch_id=(select max(a.batch_id) from dashboard.data_manak_eff_index_emp a where a.d_month="
				+ month1 + " and d_year=" + year1 + ")  ORDER BY eff_index desc ";

		System.out.println("strQuery::::" + strQuery);
		return listApplication = jdbcTemplate.queryForList(strQuery);
	}
	
	public List<HashMap> getEfficiencyIndexEmpTwo(int userid, int roleId, String branch, String month1, String year1) {
		List listApplication = new ArrayList();
		String strQuery = "";
		strQuery = " select str_emp_name, str_branch_name,normal_factor,simplified_factor,scrutiny_factor,pi_factor,inclusion_factor,tr_factor,ir_factor,eff_index,d_rank"
				+ " from dashboard.data_manak_eff_index_emp where batch_id=(select max(a.batch_id) from dashboard.data_manak_eff_index_emp a where a.d_month="
				+ month1 + " and d_year=" + year1 + ") ORDER BY eff_index desc ";

		System.out.println("strQuery::::" + strQuery);
		return listApplication = jdbcTemplate.queryForList(strQuery);
	}

	public List getmonthEmp() {
		List l = new ArrayList();
		String strQuery = "";
		strQuery = " select distinct d_month, decode((d_month), 0,'Quarter',to_char(to_date('01-'||d_month||'-1901'),'Mon'))from dashboard.data_manak_eff_index_emp order by d_month";

		return l = jdbcTemplate.queryForList(strQuery);
	}

	public List getYearEmp() {
		List l = new ArrayList();
		String strQuery = "";
		strQuery = " select distinct d_year from dashboard.data_manak_eff_index_emp ";

		return l = jdbcTemplate.queryForList(strQuery);
	}

	public List<Map<String, Object>> getBranch(String locationId) {

		String query = "select num_branch_id as bid,str_branc_short_name as name from gblt_branch_mst where num_ro_id='"
				+ locationId + "' and num_isvalid=1 order by str_branch_detail";

		System.out.println("Query: " + query);
		List<Map<String, Object>> runQry = new ArrayList<Map<String, Object>>();
		runQry = jdbcTemplate.queryForList(query);
		return runQry;
	}

	public Future<Long> getFactorySurAssigned1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getFactorySurAssigned(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getFactorySurCompleted1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getFactorySurCompleted(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public long getFactorySurAssigned(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='351'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getFactorySurCompleted(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='352'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public Future<Long> getMarketSurPlanned1(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getMarketSurPlanned(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getMarketSurveCompleted1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getMarketSurveCompleted(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public long getMarketSurPlanned(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='354'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getMarketSurveCompleted(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='355'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public Future<Long> getFactorySamplesSent1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getFactorySamplesSent(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getMarketSamplesSent1(final String fromDate, final String toDate, final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getMarketSamplesSent(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public long getFactorySamplesSent(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='353'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getMarketSamplesSent(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='356'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public Future<Long> getLotInspectionsPlanned1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getLotInspectionsPlanned(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getLotInspectionsCompleted1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getLotInspectionsCompleted(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public long getLotInspectionsPlanned(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='357'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getLotInspectionsCompleted(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='358'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public Future<Long> getSpecialFactorySurveillancePlanned1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getSpecialFactorySurveillancePlanned(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getSpecialFactorySurveillanceCompleted1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getSpecialFactorySurveillanceCompleted(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public long getSpecialFactorySurveillancePlanned(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='359'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getSpecialFactorySurveillanceCompleted(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='360'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public Future<Long> getSpecialSamplesSentFactory1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getSpecialSamplesSentFactory(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getSpecialSamplesSentMarket1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getSpecialSamplesSentMarket(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public long getSpecialSamplesSentFactory(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='361'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public long getSpecialSamplesSentMarket(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='361'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	public List<Map<String, Object>> getahc(int regionID) {
		String query = "SELECT str_ahc_name as name,str_cml_no as bid"
				+ "	FROM bis_hall.licence_details_domain_ahcreg c where c.str_app_id not in(SELECT previous_application_number\r\n"
				+ "	FROM bis_hall.renewal_applications_mapping d where c.str_app_id=d.previous_application_number)\r\n"
				+ "		  AND c.num_region_id=" + regionID + "";
		System.out.println("Query: " + query);
		List<Map<String, Object>> runQry = new ArrayList<Map<String, Object>>();
		runQry = jdbcTemplate.queryForList(query);
		return runQry;
	}

	public List<Map<String, Object>> getBranchDtlsbyahc(int ahcId) {
		//  Auto-generated method stub gblt_branch_mst
		String query = "SELECT num_branch_id as bid,(Select str_branc_short_name from gblt_branch_mst g where g.num_branch_id=r.num_branch_id) from bis_uid.";
		System.out.println("Query: " + query);
		List<Map<String, Object>> runQry = new ArrayList<Map<String, Object>>();
		runQry = jdbcTemplate.queryForList(query);
		return runQry;
	}

//Author priya 
	public Future<Long> getReceivedarticleCount(final String fromDate, final String toDate, final String Regionid,final String selectedBranchId)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getreceivedarticles(fromDate, toDate, Regionid,selectedBranchId);
			}

		});
	}

	public long getreceivedarticles(String fromDate, String toDate, String Regionid, String selectedBranchId) {
		long Count = 0;
		String query = "";
		if (Regionid.equals("All")) {
			query = "SELECT sum(num_qunatity_jeweller) as hitem FROM bis_uid.ahc_uid_jeweller_request_item_declaration where \r\n"
					+ "dt_entry_date>='" + fromDate + " ' and dt_entry_date<='" + toDate + "' ";
		} else {
			query = "SELECT sum(num_qunatity_jeweller) as hitem FROM bis_uid.ahc_uid_jeweller_request_item_declaration where \r\n"
					+ "dt_entry_date>='" + fromDate + " ' and dt_entry_date<='" + toDate
					+ "' and num_ahc_id in (select str_cml_no from bis_hall.licence_details_domain_ahcreg where num_region_id="
					+ Regionid + ")";

		}
		if(selectedBranchId!=null)
		  query+="and  num_branch_id in ("+selectedBranchId+")";

		System.out.println(query + " dg dashbaord--");
		Count = jdbcTemplate.queryForLong(query);
		return Count;
	}

	public Future<Long> getArticlehallmarked(final String fromDate, final String toDate, final String Regionid)
			throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getarticleHallmarked(fromDate, toDate, Regionid);
			}
		});
	}

	public long getarticleHallmarked(String fromDate, String toDate, String Regionid) {
		long Count = 0;
		String query = "";
		if (Regionid.equals("All")) {
			query = "SELECT count(uid_id) as hitem FROM bis_uid.ahc_uid_jeweller_request_item_declaration where \r\n"
					+ "dt_entry_date_uid>='" + fromDate + " ' and dt_entry_date_uid<='" + toDate + "' and uid_id!='null'";
		} else {
			query = "SELECT count(uid_id) as hitem FROM bis_uid.ahc_uid_jeweller_request_item_declaration where \r\n"
					+ "dt_entry_date_uid>='" + fromDate + " ' and dt_entry_date_uid<='" + toDate + "' and uid_id!='null'"
					+ " and num_ahc_id in (select str_cml_no from bis_hall.licence_details_domain_ahcreg where num_region_id="
					+ Regionid + ")";
		}
		System.out.println("hallmarked ---- : " + query);

		Count = jdbcTemplate.queryForLong(query);
		System.out.println("hallmarked article count  :::::: " + Count);
		return Count;
	}
	
		public Future<Long> getpreApplicationTestRequestsReceived1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getpreApplicationTestRequestsReceived(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public Future<Long> getpreApplicationTestRequestsDisposed1(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getpreApplicationTestRequestsDisposed(fromDate, toDate, selectedBranchId);
			}
		});
	}

	public long getpreApplicationTestRequestsReceived(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='1037'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}
	public long getpreApplicationTestRequestsDisposed(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='1038'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}

	
	public Future<Long> getmetMarketSampleBOAssign(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getMarketSampleBOAssign(fromDate, toDate, selectedBranchId);
			}
		});
	}
	
	public Future<Long> getmetMarketSampleBOCompltd(final String fromDate, final String toDate,
			final String selectedBranchId) throws IOException {
		return pool.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getMarketSampleBOCompltd(fromDate, toDate, selectedBranchId);
			}
		});
		
	}
	
	
	public long getMarketSampleBOCompltd(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='1052'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}
	public long getMarketSampleBOAssign(String fromDate, String toDate, String selectedBranchId) {
		long Count = 0;

		try {
			String query = "select count_query from bis_report_mst where num_id='1051'";
			query = jdbcTemplate.queryForObject(query, String.class);
			query = query.replaceAll("replaceToDate", toDate).replaceAll("replaceFromDate", fromDate).replaceAll("loc_id",
					selectedBranchId);
			Count = jdbcTemplate.queryForLong(query);
	}
	catch(Exception e) {
		
		e.printStackTrace();
		Count=-1;
	}
			return Count;
	}
	
	
	public String getStageNamebyValueDgreports(String value) {
		String queryresult="";
		String query ="select stage_name from bis_report_mst where num_id='"+value+"'";
		queryresult=jdbcTemplate.queryForObject(query, String.class);
		//System.out.println("query result is "+queryresult);
		return queryresult;
	}

	
		public List<Map<String, Object>> getBranchnameandid(String locationId) {

			String query = "select num_branch_id as bid,str_branc_short_name as name from gblt_branch_mst where num_ro_id='"
					+ locationId + "' and num_isvalid=1 order by str_branch_detail";

			System.out.println("Query: " + query);
			List<Map<String, Object>> runQry = new ArrayList<Map<String, Object>>();
			runQry = jdbcTemplate.queryForList(query);
			return runQry;
		
	}

		public String getBisReportListQuerybyIdforDGreports(String value) {
			String queryresult="";
			String query ="select list_query from bis_report_mst where num_id='"+value+"'";
			queryresult=jdbcTemplate.queryForObject(query, String.class);
			//System.out.println("query result is "+queryresult);
			return queryresult;
		}

		public String getStageNamebyValueDgreportsAIF(String parentId, String currntid) {
			
			String queryresult="";
			String query ="select str_report_name from bis_dg_report_mst where num_id='"+currntid+"' and num_parent_report_id='"+parentId+"' ";
			System.out.println("qwerty::::::"+query);
			queryresult=jdbcTemplate.queryForObject(query, String.class);
			
			return queryresult;
		}

		public String getBisReportListQuerybyIdforDGreportsAIF(String parentId, String currntid) {
			
			String queryresult="";
			String query ="select str_list_query from bis_dg_report_mst where num_id='"+currntid+"' and num_parent_report_id='"+parentId+"'  ";
			System.out.println("qwerty::::::"+query);
			queryresult=jdbcTemplate.queryForObject(query, String.class);
			
			return queryresult;
		}

		public List<Map<String, Object>> getAllBisReportQueryListDtlAIF(String listQuery, String from_date, String branchid,
				String to_date) {
			
			
			List<Map<String,Object>> listBisReportQueryList = new ArrayList<Map<String,Object>>();
			
			String query1 =listQuery.replaceAll("replaceFromDate",from_date.trim()).replaceAll("replaceToDate",to_date.trim()).replaceAll("loc_id",branchid);
			System.out.println("list query after param::::"+query1);
			
			try{
				listBisReportQueryList=jdbcTemplate.queryForList(query1);
		
			   }
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return listBisReportQueryList;
		}

		public List<Map<String, Object>> getAllBisReportCons(String listQuery, String cmlno, String branchId) {
			
            List<Map<String,Object>> listBisReportQueryList = new ArrayList<Map<String,Object>>();
			
			String query1 =listQuery.replaceAll("strcmlnumber",cmlno.trim()).replaceAll("loc_id",branchId);
			System.out.println("list query after param::::"+query1);
			
			try{
				listBisReportQueryList=jdbcTemplate.queryForList(query1);
		
			   }
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return listBisReportQueryList;
		}

	

}