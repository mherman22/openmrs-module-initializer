/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.initializer.api.reports;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.openmrs.module.initializer.DomainBaseModuleContextSensitiveTest;
import org.openmrs.module.initializer.api.loaders.ReportsLoader;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlFileDataSetDefinition;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.renderer.CsvReportRenderer;
import org.openmrs.module.reporting.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;

public class ReportsLoaderIntegrationTest extends DomainBaseModuleContextSensitiveTest {
	
	@Autowired
	private ReportsLoader loader;
	
	@Autowired
	private ReportDefinitionService reportDefinitionService;
	
	@Autowired
	private ReportService reportService;
	
	@Test
	public void loadUnsafe_shouldSaveReportDefinitionsFromDescriptors() throws Exception {
		loader.loadUnsafe(Collections.emptyList(), true);
		
		ReportDefinition rd = reportDefinitionService.getDefinitionByUuid("b7f5a4f4-9d8e-4a5b-8c3e-2f6d1e0a9c11");
		assertNotNull(rd);
		assertEquals("Sample Persons Report", rd.getName());
		assertEquals(1, rd.getParameters().size());
		assertEquals("startDate", rd.getParameters().get(0).getName());
		
		DataSetDefinition dsd = rd.getDataSetDefinitions().get("persons").getParameterizable();
		assertThat(dsd, instanceOf(SqlFileDataSetDefinition.class));
		assertThat(((SqlFileDataSetDefinition) dsd).getSqlFile(),
		    endsWith("reportdescriptors" + File.separator + "sql" + File.separator + "persons.sql"));
		
		List<ReportDesign> designs = reportService.getReportDesigns(rd, CsvReportRenderer.class, false);
		assertEquals(1, designs.size());
	}
}
