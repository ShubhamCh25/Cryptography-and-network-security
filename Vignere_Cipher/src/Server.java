import java.net.*;
import java.util.*;
import java.io.*;

class Server{

    private Socket socket=null;
    private ServerSocket ss=null;
    private DataInputStream dis=null;
    private DataOutputStream dos=null;
    private Scanner sc=null;
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
        else if(key.length()<plain.length()) {
            String t = key;
            while (key.length() < plain.length()) {
                key += t;
            }

            key = key.substring(0, plain.length());

        }

        for(int i=0;i<plain.length();i++){
            int row=plain.charAt(i)-97;
            int col=key.charAt(i)-97;
            encrypt+=table[row][col];

        }

        return encrypt;
    }
    public String Vignere_Decrypt(String encrypted, String key){
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
        String decrypt="";
        int idx=0;
        for(int i=0;i<encrypted.length();i++){
            for(int j=0;j<26;j++){

                if(table[key.charAt(i)-97][j]==encrypted.charAt(idx)){

                    decrypt+=(char)(j+97);
                    break;
                }
            }

            idx++;
        }
        return decrypt;
    }
    Server(int port){

        try{
            Initialize_table();
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
        String key;
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
                key=sc.nextLine();


                String encrypt= Vignere_Encrypt(line,key);
                dos.writeUTF(encrypt);


                String encrypted=dis.readUTF();
                if(encrypted.equals("end")){
                    break;
                }
                System.out.println("Received from client encrypted: "+encrypted);
                System.out.print("Enter Key for decryption: ");
                String k=sc.nextLine();
                String decrypt= Vignere_Decrypt(encrypted,k);
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