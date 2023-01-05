import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Tools {
    public static ArrayList<String> Execute(String... execute) throws Exception {
	ArrayList<String> ret = new ArrayList<String>();
	BufferedReader BR = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(execute).getInputStream()));
	String line;
	while ((line = BR.readLine()) != null && ret.size() < Constants.MAX_LINES) ret.add(line);
    	return ret;
    }
    public static void receiveFile(File file, DataInputStream in) throws Exception {
        int lines = in.readInt();
        ArrayList<String> hold = new ArrayList<String>();
        for (int i = 0; i < lines; i++) hold.add(Security.Decrypt(receiveBytes(in)));
        BufferedWriter BW = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < hold.size(); i++) BW.write(hold.get(i) + ((i == hold.size() - 1) ? "" : "\n"));
        BW.close();
    }
    public static void sendFile(File file, DataOutputStream out) throws Exception {
    	BufferedReader BR = new BufferedReader(new FileReader(file));
    	ArrayList<String> hold = new ArrayList<String>();
    	String line;
    	while ((line = BR.readLine()) != null) hold.add(line);
    	BR.close();
    	out.writeInt(hold.size());
    	for (String ln : hold) sendBytes(Security.Encrypt(ln), out);
    }
    public static ArrayList<Integer> getNotepads() throws Exception {
    	ArrayList<Integer> ret = new ArrayList<Integer>();
    	for (String line : Tools.Execute("tasklist")) {
            StringTokenizer ST = new StringTokenizer(line);
            ArrayList<String> comp = new ArrayList<String>();
            while (ST.hasMoreTokens()) comp.add(ST.nextToken());
            if (comp.size() == 6 && comp.get(0).equals("notepad.exe")) ret.add(Integer.parseInt(comp.get(1)));
        }
    	return ret;
    }
    public static boolean checkNotepads(int chk) throws Exception {
    	for (String line : Tools.Execute("tasklist")) {
            StringTokenizer ST = new StringTokenizer(line);
            ArrayList<String> comp = new ArrayList<String>();
            while (ST.hasMoreTokens()) comp.add(ST.nextToken());
            if (comp.size() == 6 && comp.get(0).equals("notepad.exe") && Integer.parseInt(comp.get(1)) == chk) return true;
        }
    	return false;
    }
    public static void sendBytes(byte[] arr, DataOutputStream out) throws Exception {
        out.writeInt(arr.length); out.write(arr);
    }
    public static byte[] receiveBytes(DataInputStream in) throws Exception {
        return in.readNBytes(in.readInt());
    }
    public static void Out(byte[] arr) {
    	for (int i = 0; i < arr.length; i++) System.out.print(arr[i] + ((i == arr.length - 1) ? "\n" : " "));
    }
}
