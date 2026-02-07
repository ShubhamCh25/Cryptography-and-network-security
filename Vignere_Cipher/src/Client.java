import java.net.*;

import java.io.*;
import java.util.Scanner;

class Client {
    public char[][] table=new char[26][26];

    public void Initialize_table(){
        for(int i=0;i<26;i++){
            for(int j=0;j<26;j++){
                table[i][j]=(char)((i+j)%26+97);

            }
        }
        System.out.println("Initializing Table for Vignrere Cipher");
        for(int i=0;i<26;i++){
            for(int j=0;j<26;j++){
                System.out.print(table[i][j]+" ");

            }
            System.out.println();
        }

    }
    public String Vignere_Encrypt(String plain, String key){
        String encrypt="";
        if(key.length()>plain.length()){
            key=key.substring(0,plain.length());
        }
        else if(key.length()<plain.length()){
            String t=key;
            while(key.length()<plain.length()){
                key+=t;
            }
            key=key.substring(0,plain.length());

        }
        for(int i=0;i<plain.length();i++){
            int row=plain.charAt(i)-97;
            int col=key.charAt(i)-97;
            encrypt+=table[row][col];
        }
        return encrypt;
    }
    public String Vignere_Decrypt(String encrypted, String key){
        String decrypt="";
        if(key.length()>encrypted.length()){
            key=key.substring(0,encrypted.length());
        }
        else if(key.length()<encrypted.length()){
            String t=key;
            while(key.length()<encrypted.length()){
                key+=t;
            }
            key=key.substring(0,encrypted.length());

        }
        int idx=0;
        for(int i=0;i<encrypted.length();i++){
            for(int j=0;j<26;j++){
             //   System.out.println(key.charAt(idx)+" "+table[i][j]);
                if(table[key.charAt(i)-97][j]==encrypted.charAt(idx)){

                    decrypt+=(char)(j+97);
                    break;
                }
            }
            //System.out.println();
            idx++;
        }
        return decrypt;
    }
    private Socket socket=null;
    private DataInputStream dis=null;
    private DataOutputStream dos=null;
    Scanner sc=null;

    Client(String ip, int port){

        try{
            Initialize_table();
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
        String key;
        boolean flag=false;
        try {
            while (true) {

                String encrypted=dis.readUTF();
                if(encrypted.equals("end")){
                    break;
                }
                System.out.println("Received from server encrypted: "+encrypted);
                System.out.print("Enter Key for decryption: ");
                String k=sc.nextLine();
                String decrypt=Vignere_Decrypt(encrypted,k);
                System.out.println("decrypted: "+decrypt);

                System.out.print("Enter plaintext: ");

                line=sc.nextLine();
                if(line.equals("end")){
                    dos.writeUTF("end");
                    System.out.println("Client Closed");
                    break;
                }
                System.out.print("Enter key for encrypted: ");
                key=sc.nextLine();
                String encrypt=Vignere_Encrypt(line,key);
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