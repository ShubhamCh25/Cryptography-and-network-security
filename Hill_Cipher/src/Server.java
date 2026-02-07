import java.net.*;
import java.util.*;
import java.io.*;

class Server{
    private Socket socket=null;
    private ServerSocket ss=null;
    private DataInputStream dis=null;
    private DataOutputStream dos=null;
    private Scanner sc=null;
    static int m;
    static int[][] key;
    int mod(int a){
        a%=26;
        if(a<0) {
            a += 26;
        }
        return a;
    }
    int modInv(int a)
    {
        a=a%26;
        if(a<0){
            a+=26;
        }
        for(int i=1;i<26;i++)
        {
            if((a * i) % 26 == 1)
                return i;
        }
        return -1;
    }

    int det(int[][] a,int n){
        if(n==1)return a[0][0];
        if(n==2)return a[0][0]*a[1][1]-a[0][1]*a[1][0];
        int d=0;
        for(int p=0;p<n;p++){
            int[][] t=new int[n-1][n-1];
            for(int i=1;i<n;i++){
                int c2=0;
                for(int j=0;j<n;j++){
                    if(j==p)continue;
                    t[i-1][c2++]=a[i][j];
                }
            }
            d+=a[0][p]*Math.pow(-1,p)*det(t,n-1);
        }
        return d;
    }
    int[][] adjoint(int[][] a){
        int[][] adj=new int[m][m];
        if(m==1){adj[0][0]=1;return adj;}
        for(int i=0;i<m;i++){
            for(int j=0;j<m;j++){
                int[][] t=new int[m-1][m-1];
                int r=0;
                for(int x=0;x<m;x++){
                    if(x==i)continue;
                    int c=0;
                    for(int y=0;y<m;y++){
                        if(y==j)continue;
                        t[r][c++]=a[x][y];
                    }
                    r++;
                }
                int sign=((i+j)%2==0)?1:-1;
                adj[j][i]=sign*det(t,m-1);
            }
        }
        return adj;
    }
    void makeKey(String s){
        key=new int[m][m];
        int idx=0;
        for(int i=0;i<m;i++)for(int j=0;j<m;j++)key[i][j]=(s.charAt(idx++%s.length())-'a'+26)%26;
    }
    public String Hill_Encrypt(String plain,int mm,String k){
        m=mm;
        makeKey(k);
        plain=plain.toLowerCase().replaceAll("[^a-z]","");
        while(plain.length()%m!=0)plain+="x";
        String res="";
        for(int t=0;t<plain.length();t+=m){
            int[] b=new int[m];
            for(int i=0;i<m;i++)b[i]=plain.charAt(t+i)-'a';
            int[] r=new int[m];
            for(int i=0;i<m;i++){
                r[i]=0;
                for(int j=0;j<m;j++)r[i]+=key[i][j]*b[j];
                r[i]=mod(r[i]);
                res+=(char)(r[i]+'A');
            }
        }
        return res;
    }
    public String Hill_Decrypt(String cipher,int mm,String k){
        m=mm;
        makeKey(k);
        int d=det(key,m);
        d=mod(d);
        int dinv=modInv(d);
        int[][] adj=adjoint(key);
        int[][] inv=new int[m][m];
        for(int i=0;i<m;i++)for(int j=0;j<m;j++)inv[i][j]=mod(adj[i][j]*dinv);
        String res="";
        for(int t=0;t<cipher.length();t+=m){
            int[] b=new int[m];
            for(int i=0;i<m;i++)b[i]=cipher.charAt(t+i)-'A';
            int[] r=new int[m];
            for(int i=0;i<m;i++){
                r[i]=0;
                for(int j=0;j<m;j++)r[i]+=inv[i][j]*b[j];
                r[i]=mod(r[i]);
                res+=(char)(r[i]+'a');
            }
        }
        return res;
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
        catch(Exception e){}
        String line="";
        int mm=0;
        String key="";
        while(true){
            try{
                System.out.print("Enter plaintext: ");
                line=sc.nextLine();
                if(line.equals("end")){
                    System.out.println("Server Closed");
                    dos.writeUTF("end");
                    break;
                }
                System.out.print("Enter matrix size: ");
                mm=sc.nextInt();
                sc.nextLine();
                System.out.print("Enter key: ");
                key=sc.nextLine();
                String encrypt=Hill_Encrypt(line,mm,key);
                System.out.println("Encrypted by server: "+encrypt);
                dos.writeUTF(encrypt);

                String encrypted=dis.readUTF();
                if(encrypted.equals("end")){
                    System.out.println("Server Closed");
                    break;
                }
                System.out.println("Received from client encrypted: "+encrypted);
                System.out.print("Enter matrix size for decryption: ");
                mm=sc.nextInt();
                sc.nextLine();
                System.out.print("Enter key for decryption: ");
                key=sc.nextLine();
                String decrypt=Hill_Decrypt(encrypted,mm,key);
                System.out.println("Decrypted: "+decrypt);
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
        catch(Exception e){}
    }
    public static void main(String args[]){
        Server s=new Server(3000);
    }
}
