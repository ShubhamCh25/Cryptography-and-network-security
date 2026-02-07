import java.net.*;
import java.util.*;
import java.io.*;

class Server{

    private Socket socket=null;
    private ServerSocket ss=null;
    private DataInputStream dis=null;
    private DataOutputStream dos=null;
    private Scanner sc=null;
    public  static  String hexToBin(char c){
        if(c<65 || c>97){
            return decimalToBin(c-'0');
        }
        else{
            int x=c-'A'+10;
            return decimalToBin(x);
        }
    }
    public  static  String toBin(String b){
        StringBuilder sb=new StringBuilder();
        for(char i:b.toCharArray()){
            sb.append(hexToBin(i));
        }
        return sb.toString();
    }
    public  static String toHex(String h) {

        HashMap<String, String> t = new HashMap<>();
        t.put("0000", "0");
        t.put("0001", "1");
        t.put("0010", "2");
        t.put("0011", "3");
        t.put("0100", "4");
        t.put("0101", "5");
        t.put("0110", "6");
        t.put("0111", "7");
        t.put("1000", "8");
        t.put("1001", "9");
        t.put("1010", "A");
        t.put("1011", "B");
        t.put("1100", "C");
        t.put("1101", "D");
        t.put("1110", "E");
        t.put("1111", "F");

        StringBuilder ans = new StringBuilder();
        int r = 0;
        while (r < h.length()) {
            String chunk = h.substring(r, r + 4);
            ans.append(t.get(chunk));
            r += 4;
        }
        return ans.toString();
    }

    public  static String permut(String s,int arr[]){
        String res="";
        for(int i:arr){
            res+=s.charAt(i-1);
        }
        return res;
    }
    public  static String left_shift(String s){
        String res="";
        for(int i=1;i<s.length();i++){
            res+=s.charAt(i);
        }
        res+=s.charAt(0);
        return res;
    }
    public  static String  xor(String s1,String s2){
        String res="";

        for(int i=0;i<s1.length();i++){
            char c1=s1.charAt(i);
            char c2=s2.charAt(i);
            if(c1!=c2){
                res+='1';
            }
            else{
                res+='0';
            }
        }
        return res;
    }
    public  static String decimalToBin(int t){
        StringBuilder sb=new StringBuilder();
        int p=t;
        while(t>0){
            sb.append(t%2);
            t/=2;
        }
        while(sb.length()<4){
            sb.append(0);
        }
        //  System.out.println(p+" "+sb.reverse().toString());
        return sb.reverse().toString();
    }
    public static String sbox(String s){
        int r=6,st=0;
        String res="";
        int[][] sboxes={
                {14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7,0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8,4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0,15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13},
                {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10,3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5,0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15,13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9},
                {10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8,13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1,13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7,1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12},
                {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15,13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9,10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4,3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14},
                {2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9,14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6,4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14,11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3},
                {12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11,10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8,9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6,4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13},
                {4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1,13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6,1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2,6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12},
                {13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7,1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2,7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8,2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}
        };
        int idx=0;

        while(idx<8){
            String sub=s.substring(st,r);
            st=r;
            r+=6;

            String temp=sub.substring(1,5);

            temp=Character.toString(sub.charAt(0))+Character.toString(sub.charAt(5))+temp;

            int col=Integer.parseInt(temp,2);
            //   System.out.println("here:"+col+" "+temp);
            res+=decimalToBin(sboxes[idx][col]);
            idx++;
        }
        return res;
    }
    public static List<String> Round_keys(String k) {
        k=toBin(k);
        int pc1[] = {
                57,49,41,33,25,17,9,
                1,58,50,42,34,26,18,
                10,2,59,51,43,35,27,
                19,11,3,60,52,44,36,
                63,55,47,39,31,23,15,
                7,62,54,46,38,30,22,
                14,6,61,53,45,37,29,
                21,13,5,28,20,12,4
        };
        int pbox2[]={14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,20,13,2,41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,46,42,50,36,29,32};
        k=permut(k,pc1);
        List<String>rk=new ArrayList<>();
        String kl=k.substring(0,28);
        String kr=k.substring(28);
        for(int i=1;i<=16;i++) {

            if(i==2||i==1||i==9||i==16) {
                kl = left_shift(kl);
                kr = left_shift(kr);
            }
            else{
                kl=left_shift(kl);
                kr=left_shift(kr);
                kl=left_shift(kl);
                kr=left_shift(kr);
            }
            rk.add(permut(kl+kr,pbox2));
        }
        return rk;
    }
    public static String DES_encrypt(String p,String k,int flag){


        List<String>round=Round_keys(k);
        p=toBin(p);


        int initial_perm1[]={58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8,57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7};
        String t=permut(p,initial_perm1);


        int expansion[]={32,1,2,3,4,5,4,5,6,7,8,9,8,9,10,11,12,13,12,13,14,15,16,17,16,17,18,19,20,21,20,21,22,23,24,25,24,25,26,27,28,29,28,29,30,31,32,1};
        int transposition[]={16,7,20,21,29,12,28,17,1,15,23,26,5,18,31,10,2,8,24,14,32,27,3,9,19,13,30,6,22,11,4,25};
        int initial_reverse[]={40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,34,2,42,10,50,18,58,26,33,1,41,9,49,17,57,25};


        String left="";
        String right="";
        left=t.substring(0,32);
        right=t.substring(32);
        if(flag==1){
            List<String>temp=new ArrayList<>();
            for(String i:round){
                temp.add(i);
            }
            round.clear();
            for(int i=15;i>=0;i--){
                round.add(temp.get(i));
            }
        }
        System.out.println("\n\nROUND"+"\t\t"+"LEFT"+"\t\t"+"RIGHT"+"\t\t"+"ROUND KEY");
        for(int i=0;i<=15;i++){
            String prev_right=right;
            right=permut(right,expansion);
            right=xor(right,round.get(i));
            right=sbox(right);
            right=permut(right,transposition);
            right=xor(left,right);

            left=prev_right;
            System.out.println((i+1)+"\t\t"+toHex(left)+"\t\t"+toHex(right)+"\t\t"+toHex(round.get(i)));
        }

        String enc=right+left;

        enc=permut(enc,initial_reverse);
        enc=toHex(enc);

        return enc;

    }
    public static String encode(String s){
        StringBuilder sb=new StringBuilder();
        for(char i:s.toCharArray()){
            String bin=decimalToBin(i);
            while(bin.length()<8){
                bin='0'+bin;
            }
            sb.append(bin);
        }
        while(sb.length()%64!=0){
            sb.append("01111000");
        }
        return sb.toString();
    }
    public static String decode(String s){
        int l=0,r=8;
        StringBuilder sb=new StringBuilder();
        while(r<=s.length()){
            String t=s.substring(l,r);
            sb.append((char)(Integer.parseInt(t,2)));
            l=r;
            r+=8;
        }
        return sb.toString();
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
        String key;
        boolean flag=false;

        int l,r;
        while(true){

            try{
                System.out.print("Enter plaintext: ");
                line=sc.nextLine();
                if(line.equals("end")){
                    System.out.println("Server Closed");
                    dos.writeUTF("end");
                    break;
                }
                String binary_plain=encode(line);

                l=0;
                r=64;

                String encrypted="";
                System.out.print("Enter key for encryption(in hexadecimal format): ");
                key=sc.nextLine();

                while(r<=binary_plain.length()){
                    encrypted+=DES_encrypt(toHex(binary_plain.substring(l,r)),key,0);
                    l=r;
                    r+=64;
                }
                System.out.println("Encrypted by server: "+encrypted);
                dos.writeUTF(encrypted);
                l=0;
                r=16;
                String decrypted="";


                encrypted=dis.readUTF();
                if(encrypted.equals("end")){
                    System.out.println("Server Closed");
                    break;
                }
                System.out.println("Received from client encrypted: "+encrypted);
                System.out.print("Enter Key for decryption: ");
                key=sc.nextLine();
                while(r<=encrypted.length()){
                    decrypted+=DES_encrypt(encrypted.substring(l,r),key,1);
                    l=r;
                    r+=16;
                }
                System.out.println("Decrypted by server: "+decode(toBin(decrypted)));

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