package iabudiab.maven.plugins.dependencytrack.client.model;

import java.util.List;

import org.apache.maven.plugin.logging.Log;

public class FindingsReport {

	private List<Finding> findings;
	private Log log;

	public FindingsReport(List<Finding> findings, Log log) {
		this.findings = findings;
		this.log = log;
	}

	private int falsePositives = 0;
	private int notAffected = 0;
	private int suppressed = 0;

	public void print() {
		log.info("--- Findigns report ---");

		for (Finding finding : findings) {
			Analysis analysis = finding.getAnalysis();
			if (checkAnalysis(analysis)) {
				continue;
			}

			log.info("-  Component    : " + finding.getComponent().getPurl());
			log.info("   Vulnerability: " + finding.getVulnerability().reportSummary());
			log.info("   Analysis     : " + finding.getAnalysis().getState());
		}

		log.info("+ False positives : " + falsePositives);
		log.info("+ Not affected    : " + notAffected);
		log.info("+ Suppressed      : " + suppressed);
	}

	private boolean checkAnalysis(Analysis analysis) {
		if (analysis.getState() == State.FALSE_POSITIVE) {
			falsePositives++;
			return true;
		}

		if (analysis.getState() == State.NOT_AFFECTED) {
			notAffected++;
			return true;
		}

		if (analysis.isSuppressed()) {
			suppressed++;
			return true;
		}

		return false;
	}
}