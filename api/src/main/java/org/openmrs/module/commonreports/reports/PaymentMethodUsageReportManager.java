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
public class PaymentMethodUsageReportManager extends ActivatedReportManager {
	
	@Autowired
	private InitializerService inizService;
	
	@Override
	public boolean isActivated() {
		return inizService.getBooleanFromKey("report.paymentMethodUsage.active", true);
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
	
	@Override
	public String getUuid() {
		return "409f0c7a-1ba8-4015-899d-13ba0d108850";
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("commonreports.report.paymentMethodUsage.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("commonreports.report.paymentMethodUsage.reportDescription");
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
		params.add(getFromParameter());
		params.add(getToParameter());
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
		sqlDsd.setName(MessageUtil.translate("commonreports.report.paymentMethodUsage.datasetName"));
		sqlDsd.setDescription(MessageUtil.translate("commonreports.report.paymentMethodUsage.datasetDescription"));
		
		String sql = getStringFromResource("org/openmrs/module/commonreports/sql/PaymentMethodUsageReport.sql");
		
		sqlDsd.setSqlQuery(sql);
		sqlDsd.addParameters(getParameters());
		
		Map<String, Object> parameterMappings = new HashMap<>();
		parameterMappings.put("fromDate", "${fromDate}");
		parameterMappings.put("toDate", "${toDate}");
		rd.addDataSetDefinition(getName(), sqlDsd, parameterMappings);
		return rd;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign reportDesign = ReportManagerUtil.createCsvReportDesign("9075a8b6-6802-4212-a64f-28b131818a7a",
		    reportDefinition);
		return Arrays.asList(reportDesign);
	}
	
}
