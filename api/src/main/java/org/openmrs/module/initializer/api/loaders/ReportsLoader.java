package org.openmrs.module.initializer.api.loaders;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.openmrs.annotation.OpenmrsProfile;
import org.openmrs.module.initializer.Domain;
import org.openmrs.module.reporting.config.ReportLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads the Reporting module's report descriptors from {@code reports/reportdescriptors} through
 * its own {@link ReportLoader}, without checksums since a descriptor's SQL lives in files beside it.
 */
@OpenmrsProfile(modules = { "reporting:1.21.0-9.*" })
public class ReportsLoader extends BaseLoader {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	protected Domain getDomain() {
		return Domain.REPORTS;
	}
	
	@Override
	public void loadUnsafe(List<String> wildcardExclusions, boolean doThrow) throws Exception {
		try {
			loadReportsFromConfig(doThrow);
		}
		catch (Exception e) {
			log.error(e.getMessage());
			if (doThrow) {
				log.error("The loading of the '" + getDomainName() + "' configuration was aborted.", e);
				throw new RuntimeException(e);
			}
		}
	}
	
	private void loadReportsFromConfig(boolean doThrow) throws Exception {
		// Reporting 1.21.0 to 2.1.0 have no loadReportsFromConfig(boolean) and stop at the first invalid descriptor
		Method method;
		try {
			method = ReportLoader.class.getMethod("loadReportsFromConfig", boolean.class);
		}
		catch (NoSuchMethodException e) {
			ReportLoader.loadReportsFromConfig();
			return;
		}
		try {
			method.invoke(null, doThrow);
		}
		catch (InvocationTargetException e) {
			if (e.getCause() instanceof Error) {
				throw (Error) e.getCause();
			}
			throw (Exception) e.getCause();
		}
	}
}
