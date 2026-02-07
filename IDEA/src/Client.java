import java.net.*;
import java.io.*;
import java.util.*;

class Client {
    private Socket socket=null;
    private DataInputStream dis=null;
    private DataOutputStream dos=null;
    Scanner sc=null;
    Client(String ip, int port){
        try{
            sc=new Scanner(System.in);
            socket=new Socket(ip,port);
            dis=new DataInputStream(socket.getInputStream());
            dos=new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }

        try{
            while(true){
                String encrypted=dis.readUTF();
                if(encrypted.equals("end")){
                    System.out.println("Client Closed");
                    break;
                }

                System.out.println("Received from server encrypted: "+encrypted);
                System.out.print("Enter key: ");
                String key=sc.nextLine();

                String decrypted=IDEA.IDEA_decrypt(encrypted,key);
                System.out.println("Decrypted by client: "+decrypted);

                System.out.print("Enter plaintext block: ");
                String plain=sc.nextLine();
                if(plain.equals("end")){
                    dos.writeUTF("end");
                    System.out.println("Client Closed");
                    break;
                }

                System.out.print("Enter key(32 hex chars): ");
                key=sc.nextLine();

                String enc=IDEA.IDEA_encrypt(plain,key);
                System.out.println("Encrypted by client: "+enc);
                dos.writeUTF(enc);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        try{
            socket.close();
            dis.close();
            dos.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String args[]){
        new Client("127.0.0.1",3000);
    }
}
