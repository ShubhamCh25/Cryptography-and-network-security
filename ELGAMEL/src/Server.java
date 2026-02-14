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
                System.out.println("Enter q:");

                int q=sc.nextInt();
                ArrayList<Integer>keys=Elgammel.keys(q);
                int xa=keys.get(0);
                keys.remove(0);

                keys.remove(0);
                int alpha=keys.get(0);
                keys.remove(0);
                int ya=keys.get(0);
                keys.remove(0);
                System.out.println(xa+" "+q+" "+ya);
                System.out.printf("Private Key of Server is {%d}\n",xa);
                dos.writeUTF(Integer.toString(q));
                dos.writeUTF(Integer.toString(alpha));
                dos.writeUTF(Integer.toString(ya));
                int c1=Integer.parseInt(dis.readUTF());
                int c2=Integer.parseInt(dis.readUTF());
              //  int k=(int)(Math.pow(c1,xa)%q);
                BigInteger k1=new BigInteger(Integer.toString(c1));
                k1=k1.pow(xa).mod(new BigInteger(Integer.toString(q)));
                System.out.println("Received from client c2: "+c2+" and c1:"+c1);
                int K=k1.intValue();
                int k_inv=Elgammel.MulInv(K,q);
               // System.out.println(K+" "+k_inv);
                int plain=(c2*k_inv)%q;
               // System.out.println(plain);
                if(plain==-1){
                    dos.writeUTF("end");
                    break;
                }
                System.out.println("Decrypted by Server: "+plain);
                int q2=Integer.parseInt(dis.readUTF());
                int alpha2=Integer.parseInt(dis.readUTF());
                int yb=Integer.parseInt(dis.readUTF());
                System.out.printf("Public Key of client is {%d,%d,%d}\n",q2,alpha2,yb);
                System.out.println("Enter plaintext to encrypt");
                int m=sc.nextInt();
                int k=(int)(Math.random()*(q2-1)+1);
                BigInteger t1=new BigInteger(Integer.toString(alpha2));
                t1=t1.pow(k).mod(new BigInteger(Integer.toString(q2)));
                BigInteger K1=new BigInteger(Integer.toString(yb));
                K1=K1.pow(k).mod(new BigInteger(Integer.toString(q2)));
                c1=t1.intValue();
                c2=(K1.intValue()*m)%q2;
                System.out.println("Encrypted by server C1: "+c1+" and k-value: "+k);
                System.out.println("Encrypted by server C2: "+c2);
                dos.writeUTF(Integer.toString(c1));
                dos.writeUTF(Integer.toString(c2));
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
