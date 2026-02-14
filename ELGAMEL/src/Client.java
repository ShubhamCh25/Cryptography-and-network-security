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
                int q=Integer.parseInt(dis.readUTF());
                int alpha=Integer.parseInt(dis.readUTF());
                int ya=Integer.parseInt(dis.readUTF());
                System.out.printf("Public Key of server is {%d,%d,%d}\n",q,alpha,ya);
                System.out.println("Enter plaintext to encrypt");
                int m= sc.nextInt();

                int k=(int)(Math.random()*(q-1)+1);
                BigInteger t1=new BigInteger(Integer.toString(alpha));
                t1=t1.pow(k).mod(new BigInteger(Integer.toString(q)));
                BigInteger K1=new BigInteger(Integer.toString(ya));
                K1=K1.pow(k).mod(new BigInteger(Integer.toString(q)));
                int K=K1.intValue();
                String c1=Integer.toString(t1.intValue());
                String c2=Integer.toString((K*m)%q);
                System.out.println("Encrypted by client C1: "+c1+" and k-value: "+k);
                System.out.println("Encrypted by client C2: "+c2);
                dos.writeUTF(c1);
                dos.writeUTF(c2);
                System.out.println("Enter q:");
                int q2=sc.nextInt();
                ArrayList<Integer>keys=Elgammel.keys(q2);
                int xb=keys.get(0);
                int alpha2=keys.get(2);
                int yb=keys.get(3);
                System.out.printf("Private Key of Client is {%d}\n",xb);
                dos.writeUTF(Integer.toString(q2));
                dos.writeUTF(Integer.toString(alpha2));
                dos.writeUTF(Integer.toString(yb));
                int c1b=Integer.parseInt(dis.readUTF());
                int c2b=Integer.parseInt(dis.readUTF());
                BigInteger k2=new BigInteger(Integer.toString(c1b));
                k2=k2.pow(xb).mod(new BigInteger(Integer.toString(q2)));
                int Kb=k2.intValue();
                int k_inv=Elgammel.MulInv(Kb,q2);
                int plain2=(c2b*k_inv)%q2;
                System.out.println("Decrypted by Client: "+plain2);

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
