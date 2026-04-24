package sr.ice.server;

import DynamicApp.BirthdayCheck;
import DynamicApp.Employee;
import com.zeroc.Ice.Current;

public class BirthdayCheckInterface implements BirthdayCheck {

	@Override
	public boolean isTodayEmployeeBirthday(Employee emp, Current current) {
		// assume date format YYYY-MM-DD
		if (emp.birthday == null || emp.birthday.length() < 10) {
			System.err.println("Passed birthday string is null, too short, or too long (" + emp.birthday + " expected YYYY-MM-DD");
			return false;
		}

		// parse date
		// YYYY-MM-DD -> 0123 (Y), 4 (-), 56 (M), 7 (-), 89 (D)
		String monthStr = emp.birthday.substring(5, 7);
		String dayStr = emp.birthday.substring(8, 10);

		// compare to today
		java.time.LocalDate today = java.time.LocalDate.now();
		return Integer.parseInt(monthStr) == today.getMonthValue()
				&& Integer.parseInt(dayStr) == today.getDayOfMonth();
	}

	@Override
	public String getBirthdayWishes(String name, Current current) {
		return "Happy birthday to you, " + name + " \n - xoxo BirthdayCheckSystem  :)";
	}

	@Override
	public int predictNextBirthdayAge(int[] previousAgesList, Current current) {
		return previousAgesList[previousAgesList.length - 1] + 1; // proof of this equation simplification is left as an exercise to the reader
	}
}