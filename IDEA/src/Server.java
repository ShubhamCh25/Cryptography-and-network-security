import java.net.*;
import java.io.*;
import java.util.*;

class Server {
    private Socket socket=null;
    private ServerSocket ss=null;
    private DataInputStream dis=null;
    private DataOutputStream dos=null;
    private Scanner sc=null;

    Server(int port){
        try{
            sc=new Scanner(System.in);
            ss=new ServerSocket(port);
            System.out.println("Server started");
            socket=ss.accept();
            dis=new DataInputStream(socket.getInputStream());
            dos=new DataOutputStream(socket.getOutputStream());
            System.out.println("Client accepted");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }

        try{
            while(true){
                System.out.print("Enter plaintext block: ");
                String plain=sc.nextLine();
                if(plain.equals("end")){
                    dos.writeUTF("end");
                    System.out.println("Server Closed");
                    break;
                }

                System.out.print("Enter key: ");
                String key=sc.nextLine();

                String enc=IDEA.IDEA_encrypt(plain,key);
                System.out.println("Encrypted by server: "+enc);
                dos.writeUTF(enc);

                String encrypted=dis.readUTF();
                if(encrypted.equals("end")){
                    System.out.println("Server Closed");
                    break;
                }

                System.out.println("Received from client encrypted: "+encrypted);
                System.out.print("Enter key: ");
                key=sc.nextLine();

                String dec=IDEA.IDEA_decrypt(encrypted,key);
                System.out.println("Decrypted by server: "+dec);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        try{
            socket.close();
            dis.close();
            dos.close();
            ss.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String args[]){
        new Server(3000);
    }
}
