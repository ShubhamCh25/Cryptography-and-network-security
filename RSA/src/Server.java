import java.net.*;
import java.io.*;
import java.util.*;
import java.math.*;
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
                System.out.println("Enter prime numbers p and q:");
                int p=sc.nextInt();
                int q=sc.nextInt();
                ArrayList<Integer>keys=RSA.RSA(p,q);
                int d=keys.get(0);
                keys.remove(0);
                int n=keys.get(0);
                keys.remove(0);
                int e=keys.get(0);
                keys.remove(0);
                keys.remove(0);
                System.out.println(d+" "+e+" "+n);
                System.out.printf("Private Key of Server is {%d,%d}\n",d,n);
                dos.writeUTF(Integer.toString(e));
                dos.writeUTF(Integer.toString(n));

                String enc=dis.readUTF();
                System.out.println("Received from client encrypted: "+enc);
                String plain="";
                for(char i:enc.toCharArray()){
                    BigInteger t1=new BigInteger(Integer.toString(i));
                    int t2=t1.pow(d).mod(new BigInteger(Integer.toString(n))).intValue();

                    System.out.println((int)i+" "+t2);
                    plain+=(char)(t2+97);
                }
                if(plain.equals("end")){
                    dos.writeUTF("end");
                    break;
                }
                System.out.println("Decrypted by Server: "+plain);
                e=Integer.parseInt(dis.readUTF());
                n=Integer.parseInt(dis.readUTF());
                System.out.printf("Public Key of client is {%d,%d}\n",e,n);
                System.out.println("Enter plaintext to encrypt");
                sc.nextLine();
                plain=sc.nextLine();

                String encrypted="";
                for(char i:plain.toCharArray()){
                    BigInteger t1=new BigInteger(Integer.toString(i-97));
                    int x=t1.pow(e).mod(new BigInteger(Integer.toString(n))).intValue();
                    System.out.println((int)i+" "+x);
                    encrypted+=((char)(x));
                }
                System.out.println("Encrypted by server: "+encrypted);
                dos.writeUTF(encrypted);



            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        try{
            System.out.println("Server closed");
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
