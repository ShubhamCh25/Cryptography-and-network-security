import java.net.*;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

class Client {

    private Socket socket=null;
    private DataInputStream dis=null;
    private DataOutputStream dos=null;
    Scanner sc=null;
    char matrix[][]=new char[5][5];
    public String Playfair_Encrypt(String plain,String key){
        int idx=0;
        HashSet<Character>used=new HashSet<>();
        int idx2=97;

        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(idx<key.length()){
                    char c=key.charAt(idx++);
                    matrix[i][j]=c;
                    used.add(c);
                }
                else{

                    while(used.contains((char)idx2)){
                        idx2++;
                    }
                    if(idx2==106 && !used.contains((char)105)){
                        idx2++;

                    }
                    matrix[i][j]=(char)idx2;

                    idx2++;
                }
            }
        }
        if(plain.length()%2!=0){
            plain+='x';
        }
        System.out.println("Playfair Matrix: ");

        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
        int i=0;
        String encrypt="";
        while(i<plain.length()){
            char char1=plain.charAt(i);
            char char2=plain.charAt(i+1);

            int r1=0,c1=0,r2=0,c2=0;

            for(int r = 0; r <5; r++){
                for(int c = 0; c <5; c++){
                    if(matrix[r][c]==char1){
                        r1= r;
                        c1= c;
                    }
                    if(matrix[r][c]==char2){
                        r2= r;
                        c2= c;
                    }
                }

            }
            if(r1==r2){
                c1=(c1+1)%5;
                c2=(c2+1)%5;
                encrypt+=matrix[r1][c1];
                encrypt+=matrix[r1][c2];
            }
            else if(c1==c2){
                r1=(r1+1)%5;
                r2=(r2+1)%5;
                encrypt+=matrix[r1][c1];
                encrypt+=matrix[r2][c1];
            }
            else{
                encrypt+=matrix[r1][c2];
                encrypt+=matrix[r2][c1];
            }
            i+=2;

        }

        return encrypt;
    }
    public String Playfair_Decrypt(String plain,char mat[][]){
        String decrypt="";
        int i=0;
        while(i<plain.length()){
            char char1=plain.charAt(i);
            char char2=plain.charAt(i+1);

            int r1=0,c1=0,r2=0,c2=0;

            for(int r = 0; r <5; r++){
                for(int c = 0; c <5; c++){
                    if(mat[r][c]==char1){
                        r1= r;
                        c1= c;
                    }
                    if(mat[r][c]==char2){
                        r2= r;
                        c2= c;
                    }
                }
            }
            if(r1==r2){
                c1=c1-1;
                c2=c2-1;
                if(c1<0){
                    c1+=5;
                }
                if(c2<0){
                    c2+=5;
                }
                decrypt+=mat[r1][c1];
                decrypt+=mat[r1][c2];
            }
            else if(c1==c2){
                r1=r1-1;
                r2=r2-1;
                if(r1<0){
                    r1+=5;
                }
                if(r2<0){
                    r2+=5;
                }
                decrypt+=mat[r1][c1];
                decrypt+=mat[r2][c1];
            }
            else{
                decrypt+=mat[r1][c2];
                decrypt+=mat[r2][c1];
            }
            i+=2;
        }
        return decrypt;
    }
    public char[][] Matrix(String key){
        int idx=0;
        HashSet<Character>used=new HashSet<>();
        int idx2=97;
        char mat[][]=new char[5][5];
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(idx<key.length()){
                    char c=key.charAt(idx++);
                    mat[i][j]=c;
                    used.add(c);
                }
                else{

                    while(used.contains((char)idx2)){
                        idx2++;
                    }
                    if(idx2==106 && !used.contains((char)105)){
                        idx2++;

                    }
                    mat[i][j]=(char)idx2;

                    idx2++;
                }
            }
        }
        return mat;

    }
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
        String key;
        boolean flag=false;
        try {
            while (true) {

                String encrypted=dis.readUTF();
                if(encrypted.equals("end")){
                    break;
                }
                System.out.println("Received from server encrypted: "+encrypted);
                System.out.print("Enter key for decryption: ");
                key=sc.nextLine();

                String decrypt=Playfair_Decrypt(encrypted,Matrix(key));
                System.out.println("decrypted: "+decrypt);

                System.out.print("Enter plaintext: ");

                line=sc.nextLine();
                if(line.equals("end")){
                    System.out.println("Client Closed");
                    break;
                }
                System.out.print("Enter key for encryption: ");



                key=sc.nextLine();


                String encrypt=Playfair_Encrypt(line,key);
                System.out.println("Encrypted by Client: "+encrypt);
                dos.writeUTF(encrypt);




            }
        }
        catch(IOException e){
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