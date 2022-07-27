import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Server implements Runnable {
	static Thread base;
	public static void main(String[] args) throws Exception {
		purgeCurrent();
		base = new Thread(new Server()); base.start();
	}
	public static void purgeCurrent() throws Exception {
		//purge any current applications using the HOST_PORT:
		for (String line : Tools.Execute("netstat -ano")) {
			StringTokenizer ST = new StringTokenizer(line);
			ArrayList<String> comp = new ArrayList<String>();
			while (ST.hasMoreTokens()) comp.add(ST.nextToken());
			if (comp.size() == 5 && comp.get(0).equals("TCP") && comp.get(1).equals("[::]:" + Constants.HOST_PORT)) {
				for (String ret : Tools.Execute("taskkill /f /pid " + comp.get(4))) Log.Add(ret);
			}
		}
	}
	public static boolean validateClient(DataOutputStream out, DataInputStream in) throws Exception {
		//send the client the salt and iv and then get them to validate:
		Security.Generate();
		Tools.sendBytes(Security.SALT, out); Tools.sendBytes(Security.IV, out);
		String token = String.valueOf(Security.sr.nextLong());
		out.writeUTF(token);
		return Tools.Compare(Tools.receiveBytes(in), Security.Encrypt(token));
	}
	public void run() {
        while (true) {
        	Socket client = null; ServerSocket listen;
        	try {
                listen = new ServerSocket(Constants.HOST_PORT);
                Log.Add("Started server on " + listen.toString());
                //serve one user:
                client = listen.accept();
                Log.Add("New client (IP: " + client.getInetAddress() + ")");
                DataInputStream from_client = new DataInputStream(client.getInputStream());
                DataOutputStream to_client = new DataOutputStream(client.getOutputStream());
                //do a handshake with the client and then send the file and then receive the updated file:
                if (!validateClient(to_client, from_client)) {
                	Log.Add("INVALID ENCRYPTION (IP: " + client.getInetAddress() + ")");
                	continue;
                }
                Tools.sendFile(new File(Constants.FILE_LOCATION), to_client);
                Tools.receiveFile(new File(Constants.FILE_LOCATION), from_client);
                client.close(); listen.close();
            } catch (Exception e) {
            	e.printStackTrace();
            }
            Log.Add("Dropped client (IP: " + client.getInetAddress() + ")");
        }
    }
}
