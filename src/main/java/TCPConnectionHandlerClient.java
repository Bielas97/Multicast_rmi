import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPConnectionHandlerClient {
    Socket socket;
    final int PORT = 5000;

    public TCPConnectionHandlerClient() {
        try {
            this.socket = new Socket(InetAddress.getByName("localhost"), PORT);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void getFile(String filename) {
        try {
            makeRequestToDownload(filename);
            byte[] contents = new byte[10000];
            FileOutputStream fos = new FileOutputStream(filename);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            InputStream is = null;

            is = socket.getInputStream();
            int bytesRead = 0;

            while ((bytesRead = is.read(contents)) != -1)
                bos.write(contents, 0, bytesRead);
            bos.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File saved successfully!");

    }

    private void makeRequestToSend(String filename) throws IOException {
        PrintWriter pw = new PrintWriter(this.socket.getOutputStream(), true);
        pw.println("send|" + filename);
    }

    private void makeRequestToDownload(String filename) throws IOException {
        PrintWriter pw = new PrintWriter(this.socket.getOutputStream(), true);
        pw.println("get|" + filename);
    }

    public void sendFile(String filename) throws IOException {
        makeRequestToSend(filename);
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        OutputStream os;

        os = socket.getOutputStream();

        byte[] contents;
        long fileLength = file.length();
        long current = 0;

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
            System.out.print("Sending file ... " + (current * 100) / fileLength + "% complete!");
        }
        os.flush();
        socket.close();
    }
}
