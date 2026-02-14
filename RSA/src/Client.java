import java.net.*;
import java.io.*;
import java.util.*;
import java.math.*;
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
                int e=Integer.parseInt(dis.readUTF());
                int n=Integer.parseInt(dis.readUTF());
                System.out.printf("Public Key of server is {%d,%d}\n",e,n);
                System.out.println("Enter plaintext to encrypt");
                String plain=sc.nextLine();
                String encrypted="";
                for(char i:plain.toCharArray()){
                    BigInteger p=new BigInteger(Integer.toString(i-97));
                    int x=p.pow(e).mod(new BigInteger(Integer.toString(n))).intValue();
                    System.out.println((int)(i-97)+" "+x);
                    encrypted+=((char)(x));
                }
                System.out.println("Encrypted: "+encrypted);
                dos.writeUTF(encrypted);
                System.out.println("Enter prime numbers p and q:");
                int p=sc.nextInt();
                int q=sc.nextInt();
                sc.nextLine();
                ArrayList<Integer>keys=RSA.RSA(p,q);
                int d=keys.get(0);
                keys.remove(0);
                n=keys.get(0);
                keys.remove(0);
                e=keys.get(0);
                keys.remove(0);
                keys.remove(0);
                System.out.println(d+" "+e+" "+n);
                System.out.printf("Private Key of Client is {%d,%d}\n",d,n);
                dos.writeUTF(Integer.toString(e));
                dos.writeUTF(Integer.toString(n));
                String enc=dis.readUTF();
                System.out.println("Received from server encrypted: "+enc);
                plain="";
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
                System.out.println("Decrypted by Client: "+plain);
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
