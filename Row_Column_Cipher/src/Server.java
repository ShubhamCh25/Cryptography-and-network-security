import java.net.*;
import java.util.*;
import java.io.*;

class Server{

    private Socket socket=null;
    private ServerSocket ss=null;
    private DataInputStream dis=null;
    private DataOutputStream dos=null;
    private Scanner sc=null;

    public String Row_Col_Encrypt(String plain, int c,String order){

        int r=(int)Math.ceil((double)plain.length()/c);
        char mat[][]=new char[r][c];
        int index=0;

        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(index<plain.length()){
                    mat[i][j]=plain.charAt(index++);
                }
                else{
                    mat[i][j]='x';
                }
            }
        }
        String encrypted="";
        for(int i=0;i<c;i++){
            for(int j=0;j<r;j++){

                encrypted+=mat[j][order.charAt(i)-'0'-1];
            }
        }
        System.out.println("Resultant Matrix: ");
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){

                System.out.print(mat[i][j]+" ");
            }
            System.out.println();
        }
        return encrypted;
    }
    public String Row_Col_Decrypt(String encrypted,int c, String order){
        int r=(int)Math.ceil((double)encrypted.length()/c);
        int idx=0;
        char mat[][]=new char[r][c];
        for(int i=0;i<c;i++){
            int col=order.charAt(i)-'0'-1;

            for(int j=0;j<r;j++){
                mat[j][col]=encrypted.charAt(idx++);

            }


        }
        String decrypt="";
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                decrypt+=mat[i][j];
            }
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
       int c=0;
       String order="";
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
                System.out.print("Enter number of cols for encryption: ");
                c=sc.nextInt();
                sc.nextLine();
                System.out.print("Enter order for encryption: ");
                order=sc.nextLine();

                String encrypt= Row_Col_Encrypt(line,c,order);
                System.out.println("Encrypted by server: "+encrypt);
                dos.writeUTF(encrypt);

                String encrypted=dis.readUTF();
                if(encrypted.equals("end")){
                    break;
                }
                System.out.println("Received from client encrypted: "+encrypted);
                System.out.print("Enter number of cols for decryption: ");
                c=sc.nextInt();
                sc.nextLine();
                System.out.print("Enter order for decryption: ");
                order=sc.nextLine();
                String decrypt= Row_Col_Decrypt(encrypted,c,order);
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