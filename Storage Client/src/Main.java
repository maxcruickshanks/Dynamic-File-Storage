import java.awt.Desktop;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void doHandshake(DataInputStream in, DataOutputStream out) throws Exception {
    	Security.SALT = Tools.receiveBytes(in); Security.IV = Tools.receiveBytes(in);
        Security.Generate();
        Tools.sendBytes(Security.Encrypt(in.readUTF()), out);
    }
    public static void main(String[] args) {
        try {
            Socket cnct = new Socket(); cnct.setSoTimeout(Constants.TIMEOUT_MS);
            cnct.connect(new InetSocketAddress(Constants.HOST_ADDRESS, Constants.HOST_PORT), Constants.TIMEOUT_MS);
            DataInputStream from_server = new DataInputStream(cnct.getInputStream());
            DataOutputStream to_server = new DataOutputStream(cnct.getOutputStream());
            //receive and send a handshake:
            doHandshake(from_server, to_server);
            File txt = new File(Constants.FILE_LOCATION);
            Tools.receiveFile(txt, from_server);
            Set<Integer> previous = new HashSet<Integer>();
            //get the process list and detect new pid for notepad.exe:
            for (Integer pid : Tools.getNotepads()) previous.add(pid);
            Desktop.getDesktop().open(txt);
            int check = -1;
            for (Integer pid : Tools.getNotepads()) if (!previous.contains(pid)) check = pid;
            while (Tools.checkNotepads(check)) Thread.sleep(100);
            Tools.sendFile(txt, to_server);
            cnct.close();
        } 
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
}
