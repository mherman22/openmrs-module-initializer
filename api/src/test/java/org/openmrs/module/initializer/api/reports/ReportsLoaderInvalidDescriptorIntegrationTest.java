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

import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Collections;

import org.junit.Test;
import org.openmrs.module.initializer.DomainBaseModuleContextSensitiveTest;
import org.openmrs.module.initializer.api.loaders.ReportsLoader;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;

public class ReportsLoaderInvalidDescriptorIntegrationTest extends DomainBaseModuleContextSensitiveTest {
	
	@Autowired
	private ReportsLoader loader;
	
	@Autowired
	private ReportDefinitionService reportDefinitionService;
	
	@Override
	protected String getAppDataDirPath() {
		return getClass().getClassLoader().getResource("testAppDataDirInvalidReports").getPath() + File.separator;
	}
	
	@Test(expected = RuntimeException.class)
	public void loadUnsafe_shouldThrowOnInvalidDescriptorWhenDoThrow() throws Exception {
		loader.loadUnsafe(Collections.emptyList(), true);
	}
	
	/**
	 * Reporting releases without {@code ReportLoader.loadReportsFromConfig(boolean)} parse every
	 * descriptor before saving any, so one unparsable descriptor keeps the valid one from loading.
	 */
	@Test
	public void loadUnsafe_shouldLogInvalidDescriptorWithoutSavingAnyWhenNotDoThrow() throws Exception {
		loader.loadUnsafe(Collections.emptyList(), false);
		
		assertNull(reportDefinitionService.getDefinitionByUuid("b7f5a4f4-9d8e-4a5b-8c3e-2f6d1e0a9c11"));
	}
}
