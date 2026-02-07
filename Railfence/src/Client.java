import java.net.*;
import java.io.*;
import java.util.*;

class Client{
    private Socket socket=null;
    private DataInputStream dis=null;
    private DataOutputStream dos=null;
    Scanner sc=null;
    public String Rail_Encrypt(String plain,int key){
        char mat[][]=new char[key][plain.length()];
        for(int i=0;i<key;i++)for(int j=0;j<plain.length();j++)mat[i][j]='\n';
        boolean down=false;
        int row=0;
        for(int i=0;i<plain.length();i++){
            mat[row][i]=plain.charAt(i);
            if(row==0||row==key-1)down=!down;
            row+=down?1:-1;
        }
        String encrypted="";
        for(int i=0;i<key;i++)for(int j=0;j<plain.length();j++)if(mat[i][j]!='\n')encrypted+=mat[i][j];
        return encrypted;
    }
    public String Rail_Decrypt(String encrypted,int key){
        char mat[][]=new char[key][encrypted.length()];
        for(int i=0;i<key;i++)for(int j=0;j<encrypted.length();j++)mat[i][j]='\n';
        boolean down=false;
        int row=0;
        for(int i=0;i<encrypted.length();i++){
            mat[row][i]='*';
            if(row==0||row==key-1)down=!down;
            row+=down?1:-1;
        }
        int idx=0;
        for(int i=0;i<key;i++)for(int j=0;j<encrypted.length();j++)if(mat[i][j]=='*')mat[i][j]=encrypted.charAt(idx++);
        String res="";
        row=0;down=false;
        for(int i=0;i<encrypted.length();i++){
            res+=mat[row][i];
            if(row==0||row==key-1)down=!down;
            row+=down?1:-1;
        }
        return res;
    }
    Client(String ip,int port){
        try{
            sc=new Scanner(System.in);
            socket=new Socket(ip,port);
            dis=new DataInputStream(socket.getInputStream());
            dos=new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected");
        }
        catch(Exception e){}
        String line="";
        int key=0;
        try{
            while(true){
                String encrypted=dis.readUTF();
                if(encrypted.equals("end")){
                    break;
                }
                System.out.println("Received from server encrypted: "+encrypted);
                System.out.print("Enter key for decryption: ");
                key=sc.nextInt();
                sc.nextLine();
                String decrypt=Rail_Decrypt(encrypted,key);
                System.out.println("Decrypted: "+decrypt);
                System.out.print("Enter plaintext: ");
                line=sc.nextLine();
                if(line.equals("end")){
                    System.out.println("Client Closed");
                    break;
                }
                System.out.print("Enter key for encryption: ");
                key=sc.nextInt();
                sc.nextLine();
                String enc=Rail_Encrypt(line,key);
                System.out.println("Encrypted by client: "+enc);
                dos.writeUTF(enc);

            }
        }
        catch(Exception e){}
        try{
            socket.close();
            dis.close();
            dos.close();
        }
        catch(Exception e){}
    }
    public static void main(String args[]){
        Client c=new Client("127.0.0.1",3000);
    }
}
