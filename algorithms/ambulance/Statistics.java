package ambulance;

import java.util.List;

/**
 * Class statistics computes some statistics about simulating one day in the 
 * ambulance. 
 * 
 * @author  till zopppke
 */
public class Statistics {

	// reference to ambulance object this statistics is about
	private Ambulance _ambulance;

	// statistics values we want to compute
	private int _maxPatientWaitingTime;
	private int _averagePatientWaitingTime;
	private int _numberOfPatients;
	private int[] _doctorsWaitingTime;
	private int _xrayDoctorWaitingTime;
	private int _totalDoctorsWaitingTime;
	private int _effectiveClosingTime;

	/**
	 * Constructs a new ambulance
	 * @param ambulance the ambulance this statistics is about
	 */
	public Statistics(Ambulance ambulance) {
		_ambulance = ambulance;
	}

	/**
	 * Computes all values. Call this method to prepare the get...() methods
	 */
	public void compute() {
		// compute number of patients
		List l = _ambulance.getGonePatients();
		_numberOfPatients = l.size();

		// init values
		_maxPatientWaitingTime = 0;
		_averagePatientWaitingTime = 0;
		_totalDoctorsWaitingTime = 0;
		_doctorsWaitingTime = new int[_ambulance.getDoctors().length];

		// check that we don't get a divisionByZero
		if (_numberOfPatients > 0) {
			// compute maximum and average waiting time of patients
			int sum = 0;
			for (int i = 0; i < l.size(); ++i) {
				int t = ((Person) l.get(i)).getWaitingTime();
				_maxPatientWaitingTime = Math.max(t, _maxPatientWaitingTime);
				sum += t;
			}
			_averagePatientWaitingTime = sum / l.size();
		}

		// compute waiting time of all doctors
		Person[] doctors = _ambulance.getDoctors();
		for (int i = 0; i < doctors.length; ++i) {
			_doctorsWaitingTime[i] = doctors[i].getWaitingTime();
			_totalDoctorsWaitingTime += _doctorsWaitingTime[i];
		}
		_xrayDoctorWaitingTime = _ambulance.getXrayDoctor().getWaitingTime();
		_totalDoctorsWaitingTime += _xrayDoctorWaitingTime;

		// leisure time
		_effectiveClosingTime = Ambulance.time;
	}

	/**
	 * Returns a String representation containing all statistics values.
	 */
	public String toString() {
		String s =
			"Number of patients:         " + (_numberOfPatients < 10
				? " "
				: "")
					+ _numberOfPatients
					+ "\n"
					+ "effective closing time:       "
					+ Ambulance.getTimeString(_effectiveClosingTime)
					+ "\n"
					+ "maximum patient waiting time: "
					+ Ambulance.getTimeString(_maxPatientWaitingTime)
					+ "\n"
					+ "average patient waiting time: "
					+ Ambulance.getTimeString(_averagePatientWaitingTime)
					+ "\n";
		for (int i = 0; i < _doctorsWaitingTime.length; ++i) {
			s =
				s
					+ "doctor"
					+ (i + 1)
					+ " waiting time:         "
					+ Ambulance.getTimeString(_doctorsWaitingTime[i])
					+ "\n";
		}
		s =
			s
				+ "xray doctor waiting time:     "
				+ Ambulance.getTimeString(_xrayDoctorWaitingTime)
				+ "\n"
				+ "total doctors waiting time:   "
				+ Ambulance.getTimeString(_totalDoctorsWaitingTime);
		return s;
	}

	/**
	 * Returns the number of patients the ambulance served this day
	 * @return the number of patients
	 */
	public int getNumberOfPatients() {
		return _numberOfPatients;
	}

	/**
	 * returns the maximum waiting time of all patients
	 * @return the maximum waiting time of all patients
	 */
	public int getMaxPatientWaitingTime() {
		return _maxPatientWaitingTime;
	}

	/**
	 * returns the average waiting time of all patients
	 * @return the average waiting time of all patients
	 */
	public int getAveragePatientWaitingTime() {
		return _averagePatientWaitingTime;
	}

	/**
	 * returns the total waiting time of all doctors
	 * @return the total waiting time of all doctors
	 */
	public int getTotalDoctorsWaitingTime() {
		return _totalDoctorsWaitingTime;
	}

	/**
	 * returns the waiting time of the xray-doctor
	 * @return the waiting time of the xray-doctor
	 */
	public int getXrayDoctorWaitingTime() {
		return _xrayDoctorWaitingTime;
	}

	/**
	 * returns the waiting time of the doctors
	 * @return the waiting time of the doctors
	 */
	public int[] getDoctorsWaitingTime() {
		return _doctorsWaitingTime;
	}

	/**
	 * returns the effective closing time of the ambulance
	 * @return the effective closing time of the ambulance
	 */
	public int getEffectiveClosingTime() {
		return _effectiveClosingTime;
	}
}
