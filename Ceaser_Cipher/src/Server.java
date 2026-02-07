import java.net.*;
import java.util.*;
import java.io.*;

class Server{

    private Socket socket=null;
    private ServerSocket ss=null;
    private DataInputStream dis=null;
    private DataOutputStream dos=null;
    private Scanner sc=null;
    public String Ceaser_Encrypt(String plain,int key){
        String encrypt="";

        key = ((key % 26) + 26) % 26;
        for(char i:plain.toCharArray()){
            int c=i-97+key;
            while(c<0){
                c+=26;
            }
            encrypt+=((char)((c)%26+97));
        }
        return encrypt;
    }
    public String Ceaser_Decrypt(String plain,int key){
        String decrypt="";
        key = ((key % 26) + 26) % 26;
        for(char i:plain.toCharArray()){
            int c=(i-97)-key;
            if(c<0){
                c+=26;
            }
            decrypt+=((char)((c)%26+97));
        }
        return decrypt;
    }
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

        catch(UnknownHostException e){

            System.out.println(e.getMessage());

        }

        catch(IOException e){

            System.out.println(e.getMessage());

        }

        String line="";
        int key;
        boolean flag=false;


        while(true){

            try{
                System.out.print("Enter plaintext: ");
               line=sc.nextLine();
               if(line.equals("end")){
                   System.out.println("Server Closed");
                   dos.writeUTF("end");
                   break;
               }
                System.out.print("Enter key for encryption: ");
               key=sc.nextInt();
               sc.nextLine();
               String encrypt=Ceaser_Encrypt(line,key);
               System.out.println("Encrypted by server: "+encrypt);
               dos.writeUTF(encrypt);

               String encrypted=dis.readUTF();
                if(encrypted.equals("end")){
                    System.out.println("Server Closed");
                    break;
                }
                System.out.println("Received from client encrypted: "+encrypted);
               System.out.print("Enter Key for decryption: ");
               int k=sc.nextInt();
               sc.nextLine();
               String decrypt=Ceaser_Decrypt(encrypted,k);
               System.out.println("decrypted: "+decrypt);

            }

            catch(IOException e){

                System.out.println(e.getMessage());

            }

        }

        try{


            socket.close();
            dis.close();

            ss.close();


        }

        catch(IOException e){

            System.out.println(e.getMessage());

        }

    }

    public static void main(String args[]){

        Server s=new Server(3000);

    }

}