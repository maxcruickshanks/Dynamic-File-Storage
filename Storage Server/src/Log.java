import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Log {
	static ArrayList<String> messages = new ArrayList<String>();
	static BufferedWriter BW;
	public static void createLog() throws Exception {
		for (int i = 1;; i++) {
			File file = new File(Constants.LOGS_FOLDER + i + ".txt");
			if (!file.exists()) {
				BW = new BufferedWriter(new FileWriter(file)); break;
			}
		}
	}
	public static void Add(String hold) {
		if (messages == null) messages = new ArrayList<String>();
		messages.add("[" + new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss").format(new java.util.Date()) + "] " + hold);
		try {
			if (BW == null) createLog();
			BW.write(messages.get(messages.size() - 1) + "\n"); BW.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(messages.get(messages.size() - 1));
	}
}
