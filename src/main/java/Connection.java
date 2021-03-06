
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class, which handles the TCP request and sends/receives the files.
 */
public class Connection extends Thread {
    protected Socket socket;

    public Connection(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {

        try {
            System.out.println("polaczony");
            //The InetAddress specification
            InetAddress IA = InetAddress.getByName("localhost");
            BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String fileRequest = br.readLine();

            String[] tokens = fileRequest.split("[|]");
            System.out.println(tokens[0]+" "+tokens[1]);

            DBOperations dbOperations = new DBOperations();
            if (tokens[0].equals("send")) {
                getFile(tokens[1]);
            } else if (tokens[0].equals("get")) {
                sendFile(tokens[1]);
            } else {
                socket.close();
                System.out.println("sth went wrong...");
                return;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File sent succesfully!");
    }

    /**
     * Metod, which receives a MP3 file.
     * @param filename
     * @throws IOException
     */
    private void getFile(String filename) throws IOException {
        byte[] contents = new byte[10000];
        System.out.println("dostaję");
        //Initialize the FileOutputStream to the output file's full path.
        FileOutputStream fos = new FileOutputStream("ServerMP3s/" +filename);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        InputStream is = socket.getInputStream();

        //No of bytes read in one read() call
        int bytesRead = 0;

        while ((bytesRead = is.read(contents)) != -1)
            bos.write(contents, 0, bytesRead);

        bos.flush();
        socket.close();

        System.out.println("File saved successfully!");

    }
    /**
     * Metod, which sends a MP3 file to the client.
     * @param filename
     * @throws IOException
     */
    private void sendFile(String filename) throws IOException {
        System.out.println("wysylam");
        File file = new File("ServerMP3s/" + filename);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        OutputStream os = null;

        os = socket.getOutputStream();

        byte[] contents;
        long fileLength = file.length();
        long current = 0;

        long start = System.nanoTime();
        while (current != fileLength) {
            int size = 10000;
            if (fileLength - current >= size)
                current += size;
            else {
                size = (int) (fileLength - current);
                current = fileLength;
            }
            contents = new byte[size];
            bis.read(contents, 0, size);
            os.write(contents);
        }

        os.flush();
    }

    /**
     * Method, which returns message from the client
     * @return String, with information regarding file name and decision about upload or download.
     * @throws IOException
     */
    private String getClientMessage() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        String s = br.readLine();
        return s;
    }
}
