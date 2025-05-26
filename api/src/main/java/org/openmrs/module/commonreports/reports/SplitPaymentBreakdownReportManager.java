package org.openmrs.module.commonreports.reports;

import org.openmrs.module.commonreports.ActivatedReportManager;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.openmrs.module.commonreports.common.Helper.getStringFromResource;

@Component
public class SplitPaymentBreakdownReportManager extends ActivatedReportManager {
	
	@Autowired
	private InitializerService inizService;
	
	@Override
	public boolean isActivated() {
		return inizService.getBooleanFromKey("report.SplitPayment.active", true);
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
	
	@Override
	public String getUuid() {
		return "76e45af8-dd62-4df1-81b0-00c74d876c26";
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("commonreports.report.SplitPayment.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("commonreports.report.SplitPayment.reportDescription");
	}
	
	private Parameter getFromParameter() {
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return new Parameter("fromDate", "From Date", Date.class, null, DateUtil.parseDate(today, "yyyy-MM-dd"));
	}
	
	private Parameter getToParameter() {
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return new Parameter("toDate", "To Date", Date.class, null, DateUtil.parseDate(today, "yyyy-MM-dd"));
	}
	
	@Override
	public List<Parameter> getParameters() {
		List<Parameter> params = new ArrayList<>();
		//        params.add(getFromParameter());
		//        params.add(getToParameter());
		return params;
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		
		ReportDefinition rd = new ReportDefinition();
		
		rd.setName(getName());
		rd.setDescription(getDescription());
		rd.setParameters(getParameters());
		rd.setUuid(getUuid());
		
		SqlDataSetDefinition sqlDsd = new SqlDataSetDefinition();
		sqlDsd.setName(MessageUtil.translate("commonreports.report.SplitPayment.datasetName"));
		sqlDsd.setDescription(MessageUtil.translate("commonreports.report.SplitPayment.datasetDescription"));
		
		String sql = getStringFromResource("org/openmrs/module/commonreports/sql/SplitPaymentBreakdownReport.sql");
		
		sqlDsd.setSqlQuery(sql);
		sqlDsd.addParameters(getParameters());
		
		Map<String, Object> parameterMappings = new HashMap<>();
		//        parameterMappings.put("fromDate", "${fromDate}");
		//        parameterMappings.put("toDate", "${toDate}");
		rd.addDataSetDefinition(getName(), sqlDsd, parameterMappings);
		return rd;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign reportDesign = ReportManagerUtil.createCsvReportDesign("c4efafa2-fe39-40f4-870d-309f8b51e8dc",
		    reportDefinition);
		return Arrays.asList(reportDesign);
	}
	
}
