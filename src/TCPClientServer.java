import java.util.concurrent.*;
import java.net.*;
import java.io.*;

public class TCPClientServer {

    public static void server()

    {

        String clientSentence;
        String capitalizedSentence;
        try {
            ServerSocket welcomeSocket = new ServerSocket (12001);

            while (true)
            {

                Socket connectionSocket=welcomeSocket.accept();
                BufferedReader inFromClient=new BufferedReader (new InputStreamReader(
                        connectionSocket.getInputStream()));
                DataOutputStream outToClient= new DataOutputStream (
                        connectionSocket.getOutputStream());
                clientSentence=inFromClient.readLine();
                System.out.println("this is what I got from client  " +clientSentence);
                capitalizedSentence=clientSentence.toUpperCase()+"\n";
                outToClient.writeBytes(capitalizedSentence);
            }
        }
        catch(IOException e) {
            System.out.println(e);

        }


    }


    public static void client() {

        String sentence;
        String modifiedSentence;

        BufferedReader inFromUser=new BufferedReader ( new InputStreamReader(
                System.in));
        Socket clientSocket=null;
        int i=0;
        try {
            while (i<3) {
                try{
                    clientSocket= new Socket("127.0.0.1",12001);
                    //clientSocket= new Socket("10.0.0.15",12001);
                }
                catch (ConnectException e){
                    Thread.sleep(5000);
                    i=i+1;
                    continue;

                }
                break;
            }


            System.out.println("Client Socket's port number is" +clientSocket.getLocalPort());
            System.out.println("Client is connected to " +clientSocket.getPort());

            DataOutputStream outToServer=new DataOutputStream(
                    clientSocket.getOutputStream());
            BufferedReader inFromServer=new BufferedReader (new InputStreamReader (
                    clientSocket.getInputStream()));
            sentence=inFromUser.readLine();
            //sentence = "Connected!";
            outToServer.writeBytes(sentence+"\n");
            modifiedSentence=inFromServer.readLine();
            System.out.println("FROM SERVER: "+ modifiedSentence);
            clientSocket.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}
