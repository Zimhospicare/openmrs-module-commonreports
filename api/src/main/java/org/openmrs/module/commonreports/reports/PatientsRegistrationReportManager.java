package org.openmrs.module.commonreports.reports;

import org.openmrs.Location;
import org.openmrs.Provider;
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
public class PatientsRegistrationReportManager extends ActivatedReportManager {
	
	@Autowired
	private InitializerService inizService;
	
	@Override
	public boolean isActivated() {
		return inizService.getBooleanFromKey("report.patients.registration.active", true);
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
	
	@Override
	public String getUuid() {
		return "9a462e4d-8b31-493e-b36e-2d2c75287a01";
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("commonreports.report.patients.registration.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("commonreports.report.patients.registration.reportDescription");
	}
	
	private Parameter getGenderParameter() {
		return new Parameter("gender", "Gender", String.class, null, null);
	}
	
	private Parameter getStartDateParameter() {
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return new Parameter("startDate", "From", Date.class, null, DateUtil.parseDate(today, "yyyy-MM-dd"));
	}
	
	private Parameter getEndDateParameter() {
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return new Parameter("endDate", "To", Date.class, null, DateUtil.parseDate(today, "yyyy-MM-dd"));
	}
	
	private Parameter getMinAgeParameter() {
		return new Parameter("minAge", "Min Age", Integer.class, null, null);
	}
	
	private Parameter getMaxAgeParameter() {
		return new Parameter("maxAge", "Max Age", Integer.class, null, null);
	}
	
	@Override
	public List<Parameter> getParameters() {
		List<Parameter> params = new ArrayList<>();
		params.add(getStartDateParameter());
		params.add(getEndDateParameter());
		params.add(getGenderParameter());
		params.add(getMinAgeParameter());
		params.add(getMaxAgeParameter());
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
		sqlDsd.setName(MessageUtil.translate("commonreports.report.patients.registration.datasetName"));
		sqlDsd.setDescription(MessageUtil.translate("commonreports.report.patients.registration.datasetDescription"));
		
		String sql = getStringFromResource("org/openmrs/module/commonreports/sql/patientsRegistrationReport.sql");
		
		sqlDsd.setSqlQuery(sql);
		sqlDsd.addParameters(getParameters());
		
		Map<String, Object> parameterMappings = new HashMap<>();
		parameterMappings.put("startDate", "${startDate}");
		parameterMappings.put("endDate", "${endDate}");
		parameterMappings.put("gender", "${gender}");
		parameterMappings.put("minAge", "${minAge}");
		parameterMappings.put("maxAge", "${maxAge}");
		rd.addDataSetDefinition(getName(), sqlDsd, parameterMappings);
		return rd;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign reportDesign = ReportManagerUtil.createCsvReportDesign("8d0af26d-3a1b-4a3f-a2ca-22c9419af0b7",
		    reportDefinition);
		return Arrays.asList(reportDesign);
	}
	
}
