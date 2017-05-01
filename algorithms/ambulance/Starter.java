package ambulance;

import java.text.DecimalFormat;

/**
 * Class Starter is the class to start the ambulance simulation run.
 * 
 * @author  till zopppke
 */
public class Starter {

	// number of days to simulate
	private static final int DAYS_DETAIL = 1;
	private static final int DAYS_STATISTICS = 3;
	private static final int DAYS_AVERAGE = 10000;

	// formatting average number of patients
	private static final DecimalFormat PATIENTS_FORMAT =
		new DecimalFormat("##.00");
	/**
	 * Main method to run the application. No arguments required. 
	 * For every policy we simulate one day with detailed output and n=DAYS days
	 * for computing an average value.
	 * @param args no meaning
	 */
	public static void main(String[] args) {
		Ambulance a = new Ambulance();

		// recurse on several policies
		for (short p = 0; p < Ambulance.getPolicies().length; ++p) {
			// set and announce policy
			System.out.println(
				"//////////////// POLICY: "
					+ Ambulance.getPolicyString(p)
					+ " ///////////////");
			System.out.println();
			a.setPolicy(p);

			// DAYS_DETAIL
			System.out.println(
				"Simulating " + DAYS_DETAIL + " days with detailed output:");
			a.setVerbose(true);
			for (int i = 0; i < DAYS_DETAIL; ++i) {
				a.init();
				a.start();
				System.out.println();

				// view statistics
				Statistics s = new Statistics(a);
				s.compute();
				System.out.println(s);
				System.out.println(
					"--------------------------------------------");
				System.out.println();
			}

			// DAYS_STATISTICS
			System.out.println(
				"Simulating "
					+ DAYS_STATISTICS
					+ " days without detailed output:");
			System.out.println();
			a.setVerbose(false);
			for (int i = 0; i < DAYS_STATISTICS; ++i) {
				a.init();
				a.start();
				Statistics s = new Statistics(a);
				s.compute();
				System.out.println(s);
				System.out.println(
					"--------------------------------------------");
				System.out.println();
			}

			// DAYS_AVERAGE			
			System.out.println(
				"Simulating "
					+ DAYS_AVERAGE
					+ " days and computing average statistics:");
			System.out.println();

			// integers to accumulate 
			long numberOfPatients = 0;
			long effectiveClosingTime = 0;
			long averagePatientWaitingTime = 0;
			long maxPatientWaitingTime = 0;
			long[] doctorsWaitingTime = new long[a.getDoctors().length];
			for (int i = 0; i < doctorsWaitingTime.length; ++i) {
				doctorsWaitingTime[i] = 0;
			}
			long xrayDoctorWaitingTime = 0;
			long totalDoctorsWaitingTime = 0;

			// repeat simulation n=DAYS times
			for (int i = 0; i < DAYS_AVERAGE; ++i) {
				a.init();
				a.start();
				Statistics st = new Statistics(a);
				st.compute();

				// add values
				numberOfPatients += st.getNumberOfPatients();
				effectiveClosingTime += st.getEffectiveClosingTime();
				averagePatientWaitingTime += st.getAveragePatientWaitingTime();
				maxPatientWaitingTime += st.getMaxPatientWaitingTime();
				int[] dwt = st.getDoctorsWaitingTime();
				for (int j = 0; j < doctorsWaitingTime.length; ++j) {
					doctorsWaitingTime[j] += dwt[j];
				}
				xrayDoctorWaitingTime += st.getXrayDoctorWaitingTime();
				totalDoctorsWaitingTime += st.getTotalDoctorsWaitingTime();
			}

			// output statistics.
			System.out.println(
				"Number of patients:         "
					+ PATIENTS_FORMAT.format(
						((double) numberOfPatients) / DAYS_AVERAGE));
			System.out.println(
				"effective closing time:       "
					+ Ambulance.getTimeString(
						(int) (effectiveClosingTime / DAYS_AVERAGE)));
			System.out.println(
				"maximum patient waiting time: "
					+ Ambulance.getTimeString(
						(int) (maxPatientWaitingTime / DAYS_AVERAGE)));
			System.out.println(
				"average patient waiting time: "
					+ Ambulance.getTimeString(
						(int) (averagePatientWaitingTime / DAYS_AVERAGE)));
			for (int j = 0; j < doctorsWaitingTime.length; ++j) {
				System.out.println(
					"doctor"
						+ (j + 1)
						+ " waiting time:         "
						+ Ambulance.getTimeString(
							(int) (doctorsWaitingTime[j] / DAYS_AVERAGE)));
			}
			System.out.println(
				"xray doctor waiting time:     "
					+ Ambulance.getTimeString(
						(int) (xrayDoctorWaitingTime / DAYS_AVERAGE)));
			System.out.println(
				"total doctors waiting time:   "
					+ Ambulance.getTimeString(
						(int) (totalDoctorsWaitingTime / DAYS_AVERAGE)));
			System.out.println();
		}
	}
}
