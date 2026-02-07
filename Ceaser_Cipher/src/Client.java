import java.net.*;

import java.io.*;
import java.util.Scanner;

class Client {
    public String Ceaser_Encrypt(String plain,int key){
        String encrypt="";
        key = ((key % 26) + 26) % 26;
        for(char i:plain.toCharArray()){
            int c=i-97+key;
            if(c<0){
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
            int c=i-97-key;
            while(c<0){
                c+=26;
            }
            decrypt+=((char)((c)%26+97));
        }
        return decrypt;
    }
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

        catch(UnknownHostException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        String line="";
        int key;
        boolean flag=false;
        try {
            while (true) {

                    String encrypted=dis.readUTF();
                    if(encrypted.equals("end")){
                        break;
                    }
                System.out.println("Received from server encrypted: "+encrypted);
                System.out.print("Enter key for decryption: ");
                    int k=sc.nextInt();
                    sc.nextLine();
                    String decrypt=Ceaser_Decrypt(encrypted,k);
                  System.out.println("decrypted: "+decrypt);

                    System.out.print("Enter plaintext: ");

                line=sc.nextLine();
                if(line.equals("end")){
                    System.out.println("Client Closed");
                    dos.writeUTF("end");
                    break;
                }
                System.out.print("Enter key for encryption: ");
                key=sc.nextInt();
                sc.nextLine();
                String encrypt=Ceaser_Encrypt(line,key);
                System.out.println("Encrypted by client: "+encrypt);
                dos.writeUTF(encrypt);



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

        catch(IOException e){

            System.out.println(e.getMessage());

        }

    }

    public static void main(String args[]){

        Client c=new Client("127.0.0.1",3000);

    }

}