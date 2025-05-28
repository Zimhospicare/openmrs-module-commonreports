package org.openmrs.module.commonreports.reports;

import org.openmrs.module.commonreports.ActivatedReportManager;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.openmrs.module.commonreports.common.Helper.getStringFromResource;

@Component
public class TariffPriceChangeLogReportManager extends ActivatedReportManager {
	
	@Autowired
	private InitializerService inizService;
	
	@Override
	public boolean isActivated() {
		return inizService.getBooleanFromKey("report.TariffPriceChangeLog.active", true);
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
	
	@Override
	public String getUuid() {
		return "36a48e17-727a-44c6-8424-e1eb6a426cfc";
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("commonreports.report.TariffPriceChangeLog.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("commonreports.report.TariffPriceChangeLog.reportDescription");
	}
	
	@Override
	public List<Parameter> getParameters() {
		List<Parameter> params = new ArrayList<>();
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
		sqlDsd.setName(MessageUtil.translate("commonreports.report.TariffPriceChangeLog.datasetName"));
		sqlDsd.setDescription(MessageUtil.translate("commonreports.report.TariffPriceChangeLog.datasetDescription"));
		
		String sql = getStringFromResource("org/openmrs/module/commonreports/sql/tariffPriceChangeLogReport.sql");
		
		sqlDsd.setSqlQuery(sql);
		sqlDsd.addParameters(getParameters());
		
		Map<String, Object> parameterMappings = new HashMap<>();
		rd.addDataSetDefinition(getName(), sqlDsd, parameterMappings);
		return rd;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign reportDesign = ReportManagerUtil.createCsvReportDesign("5f05823c-37d8-4520-83e1-aba55ca7feb0",
		    reportDefinition);
		return Arrays.asList(reportDesign);
	}
	
}
