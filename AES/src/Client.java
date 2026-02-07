import java.net.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class Client {

    private Socket socket=null;
    private DataInputStream dis=null;
    private DataOutputStream dos=null;
    Scanner sc=null;
    public static String decimalToBin(int t){
        StringBuilder sb=new StringBuilder();
        while(t>0){
            sb.append(t%2);
            t/=2;
        }
        while(sb.length()<8){
            sb.append('0');
        }
        return sb.reverse().toString();
    }

    public static String hexToBin(char c){
        if(c>='0'&&c<='9')return decimalToBin(c-'0').substring(4);
        return decimalToBin(c-'A'+10).substring(4);
    }

    public static String toBin(String hx){
        StringBuilder sb=new StringBuilder();
        for(char c:hx.toUpperCase().toCharArray()){
            sb.append(hexToBin(c));
        }
        return sb.toString();
    }

    public static String toHexFromBin(String b){
        HashMap<String,String> t=new HashMap<>();
        t.put("0000","0");
        t.put("0001","1");
        t.put("0010","2");
        t.put("0011","3");
        t.put("0100","4");
        t.put("0101","5");
        t.put("0110","6");
        t.put("0111","7");
        t.put("1000","8");
        t.put("1001","9");
        t.put("1010","A");
        t.put("1011","B");
        t.put("1100","C");
        t.put("1101","D");
        t.put("1110","E");
        t.put("1111","F");
        String res="";
        for(int i=0;i<b.length();i+=4){
            res+=t.get(b.substring(i,i+4));
        }
        return res;
    }

    public static String xor(String a,String b){
        String res="";
        for(int i=0;i<a.length();i++){
            res+=((a.charAt(i)==b.charAt(i))?'0':'1');
        }
        return res;
    }

    public static String toHex(String b1,String b2){
        return toHexFromBin(xor(b1,b2));
    }

    public static int[] sbox={
            0x63,0x7C,0x77,0x7B,0xF2,0x6B,0x6F,0xC5,0x30,0x01,0x67,0x2B,0xFE,0xD7,0xAB,0x76,
            0xCA,0x82,0xC9,0x7D,0xFA,0x59,0x47,0xF0,0xAD,0xD4,0xA2,0xAF,0x9C,0xA4,0x72,0xC0,
            0xB7,0xFD,0x93,0x26,0x36,0x3F,0xF7,0xCC,0x34,0xA5,0xE5,0xF1,0x71,0xD8,0x31,0x15,
            0x04,0xC7,0x23,0xC3,0x18,0x96,0x05,0x9A,0x07,0x12,0x80,0xE2,0xEB,0x27,0xB2,0x75,
            0x09,0x83,0x2C,0x1A,0x1B,0x6E,0x5A,0xA0,0x52,0x3B,0xD6,0xB3,0x29,0xE3,0x2F,0x84,
            0x53,0xD1,0x00,0xED,0x20,0xFC,0xB1,0x5B,0x6A,0xCB,0xBE,0x39,0x4A,0x4C,0x58,0xCF,
            0xD0,0xEF,0xAA,0xFB,0x43,0x4D,0x33,0x85,0x45,0xF9,0x02,0x7F,0x50,0x3C,0x9F,0xA8,
            0x51,0xA3,0x40,0x8F,0x92,0x9D,0x38,0xF5,0xBC,0xB6,0xDA,0x21,0x10,0xFF,0xF3,0xD2,
            0xCD,0x0C,0x13,0xEC,0x5F,0x97,0x44,0x17,0xC4,0xA7,0x7E,0x3D,0x64,0x5D,0x19,0x73,
            0x60,0x81,0x4F,0xDC,0x22,0x2A,0x90,0x88,0x46,0xEE,0xB8,0x14,0xDE,0x5E,0x0B,0xDB,
            0xE0,0x32,0x3A,0x0A,0x49,0x06,0x24,0x5C,0xC2,0xD3,0xAC,0x62,0x91,0x95,0xE4,0x79,
            0xE7,0xC8,0x37,0x6D,0x8D,0xD5,0x4E,0xA9,0x6C,0x56,0xF4,0xEA,0x65,0x7A,0xAE,0x08,
            0xBA,0x78,0x25,0x2E,0x1C,0xA6,0xB4,0xC6,0xE8,0xDD,0x74,0x1F,0x4B,0xBD,0x8B,0x8A,
            0x70,0x3E,0xB5,0x66,0x48,0x03,0xF6,0x0E,0x61,0x35,0x57,0xB9,0x86,0xC1,0x1D,0x9E,
            0xE1,0xF8,0x98,0x11,0x69,0xD9,0x8E,0x94,0x9B,0x1E,0x87,0xE9,0xCE,0x55,0x28,0xDF,
            0x8C,0xA1,0x89,0x0D,0xBF,0xE6,0x42,0x68,0x41,0x99,0x2D,0x0F,0xB0,0x54,0xBB,0x16
    };

    public static int[] invsbox={
            0x52,0x09,0x6A,0xD5,0x30,0x36,0xA5,0x38,0xBF,0x40,0xA3,0x9E,0x81,0xF3,0xD7,0xFB,
            0x7C,0xE3,0x39,0x82,0x9B,0x2F,0xFF,0x87,0x34,0x8E,0x43,0x44,0xC4,0xDE,0xE9,0xCB,
            0x54,0x7B,0x94,0x32,0xA6,0xC2,0x23,0x3D,0xEE,0x4C,0x95,0x0B,0x42,0xFA,0xC3,0x4E,
            0x08,0x2E,0xA1,0x66,0x28,0xD9,0x24,0xB2,0x76,0x5B,0xA2,0x49,0x6D,0x8B,0xD1,0x25,
            0x72,0xF8,0xF6,0x64,0x86,0x68,0x98,0x16,0xD4,0xA4,0x5C,0xCC,0x5D,0x65,0xB6,0x92,
            0x6C,0x70,0x48,0x50,0xFD,0xED,0xB9,0xDA,0x5E,0x15,0x46,0x57,0xA7,0x8D,0x9D,0x84,
            0x90,0xD8,0xAB,0x00,0x8C,0xBC,0xD3,0x0A,0xF7,0xE4,0x58,0x05,0xB8,0xB3,0x45,0x06,
            0xD0,0x2C,0x1E,0x8F,0xCA,0x3F,0x0F,0x02,0xC1,0xAF,0xBD,0x03,0x01,0x13,0x8A,0x6B,
            0x3A,0x91,0x11,0x41,0x4F,0x67,0xDC,0xEA,0x97,0xF2,0xCF,0xCE,0xF0,0xB4,0xE6,0x73,
            0x96,0xAC,0x74,0x22,0xE7,0xAD,0x35,0x85,0xE2,0xF9,0x37,0xE8,0x1C,0x75,0xDF,0x6E,
            0x47,0xF1,0x1A,0x71,0x1D,0x29,0xC5,0x89,0x6F,0xB7,0x62,0x0E,0xAA,0x18,0xBE,0x1B,
            0xFC,0x56,0x3E,0x4B,0xC6,0xD2,0x79,0x20,0x9A,0xDB,0xC0,0xFE,0x78,0xCD,0x5A,0xF4,
            0x1F,0xDD,0xA8,0x33,0x88,0x07,0xC7,0x31,0xB1,0x12,0x10,0x59,0x27,0x80,0xEC,0x5F,
            0x60,0x51,0x7F,0xA9,0x19,0xB5,0x4A,0x0D,0x2D,0xE5,0x7A,0x9F,0x93,0xC9,0x9C,0xEF,
            0xA0,0xE0,0x3B,0x4D,0xAE,0x2A,0xF5,0xB0,0xC8,0xEB,0xBB,0x3C,0x83,0x53,0x99,0x61,
            0x17,0x2B,0x04,0x7E,0xBA,0x77,0xD6,0x26,0xE1,0x69,0x14,0x63,0x55,0x21,0x0C,0x7D
    };

    public static int[] rcon={0x01,0x02,0x04,0x08,0x10,0x20,0x40,0x80,0x1B,0x36};

    public static String[] rotword(String[] w){
        return new String[]{w[1],w[2],w[3],w[0]};
    }

    public static String[] subword(String[] w){
        String[] x=new String[4];
        for(int i=0;i<4;i++){
            x[i]=to2Hex(sbox[Integer.parseInt(w[i],16)]);
        }
        return x;
    }

    public static String to2Hex(int v){
        String x=Integer.toHexString(v&255).toUpperCase();
        if(x.length()==1)x="0"+x;
        return x;
    }

    public static String[] xorword(String[] a,String[] b){
        String[] x=new String[4];
        for(int i=0;i<4;i++){
            x[i]=toHex(toBin(a[i]),toBin(b[i]));
        }
        return x;
    }

    public static String[] gword(String[] w,int ri){
        w=rotword(w);
        w=subword(w);
        int v=Integer.parseInt(w[0],16)^rcon[ri];
        w[0]=to2Hex(v);
        return w;
    }

    public static int xtime(int a){
        a&=255;
        if((a&0x80)!=0){
            return ((a<<1)^0x1B)&255;
        }
        return (a<<1)&255;
    }

    public static int gfmul(int a,int b){
        int res=0;
        while(b>0){
            if((b&1)==1){
                res^=a;
            }
            a=xtime(a);
            b>>=1;
        }
        return res&255;
    }

    public static void Subbox(String[][] mat){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                int v=Integer.parseInt(mat[i][j],16);
                mat[i][j]=to2Hex(sbox[v]);
            }
        }
    }

    public static void InvSubbox(String[][] mat){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                int v=Integer.parseInt(mat[i][j],16);
                mat[i][j]=to2Hex(invsbox[v]);
            }
        }
    }

    public static void ShiftRows(String[][] mat){
        String t=mat[1][0];
        mat[1][0]=mat[1][1];mat[1][1]=mat[1][2];mat[1][2]=mat[1][3];mat[1][3]=t;

        String t1=mat[2][0],t2=mat[2][1];
        mat[2][0]=mat[2][2];mat[2][1]=mat[2][3];mat[2][2]=t1;mat[2][3]=t2;

        t=mat[3][3];
        mat[3][3]=mat[3][2];mat[3][2]=mat[3][1];mat[3][1]=mat[3][0];mat[3][0]=t;
    }

    public static void InvShiftRows(String[][] mat){
        String t=mat[1][3];
        mat[1][3]=mat[1][2];
        mat[1][2]=mat[1][1];
        mat[1][1]=mat[1][0];
        mat[1][0]=t;

        String t1=mat[2][0],t2=mat[2][1];
        mat[2][0]=mat[2][2];mat[2][1]=mat[2][3];mat[2][2]=t1;mat[2][3]=t2;

        t=mat[3][0];
        mat[3][0]=mat[3][1];mat[3][1]=mat[3][2];mat[3][2]=mat[3][3];mat[3][3]=t;
    }

    public static void MixColumns(String[][] mat){
        for(int c=0;c<4;c++){
            int s0=Integer.parseInt(mat[0][c],16);
            int s1=Integer.parseInt(mat[1][c],16);
            int s2=Integer.parseInt(mat[2][c],16);
            int s3=Integer.parseInt(mat[3][c],16);

            int r0=gfmul(s0,2)^gfmul(s1,3)^gfmul(s2,1)^gfmul(s3,1);
            int r1=gfmul(s0,1)^gfmul(s1,2)^gfmul(s2,3)^gfmul(s3,1);
            int r2=gfmul(s0,1)^gfmul(s1,1)^gfmul(s2,2)^gfmul(s3,3);
            int r3=gfmul(s0,3)^gfmul(s1,1)^gfmul(s2,1)^gfmul(s3,2);

            mat[0][c]=to2Hex(r0);
            mat[1][c]=to2Hex(r1);
            mat[2][c]=to2Hex(r2);
            mat[3][c]=to2Hex(r3);
        }
    }

    public static void InvMixColumns(String[][] mat){
        for(int c=0;c<4;c++){
            int s0=Integer.parseInt(mat[0][c],16);
            int s1=Integer.parseInt(mat[1][c],16);
            int s2=Integer.parseInt(mat[2][c],16);
            int s3=Integer.parseInt(mat[3][c],16);

            int r0=gfmul(s0,14)^gfmul(s1,11)^gfmul(s2,13)^gfmul(s3,9);
            int r1=gfmul(s0,9)^gfmul(s1,14)^gfmul(s2,11)^gfmul(s3,13);
            int r2=gfmul(s0,13)^gfmul(s1,9)^gfmul(s2,14)^gfmul(s3,11);
            int r3=gfmul(s0,11)^gfmul(s1,13)^gfmul(s2,9)^gfmul(s3,14);

            mat[0][c]=to2Hex(r0);
            mat[1][c]=to2Hex(r1);
            mat[2][c]=to2Hex(r2);
            mat[3][c]=to2Hex(r3);
        }
    }

    public static void XOR(String[][] mat,String[][] rk){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                mat[i][j]=toHex(toBin(mat[i][j]),toBin(rk[i][j]));
            }
        }
    }

    public static void Round_keys(String[][][] r,String key,int nk,int nr,int nw){
        key=key.toUpperCase();
        String[][] w=new String[nw][4];
        int s=0,e=2;
        for(int i=0;i<nk;i++){
            for(int j=0;j<4;j++){
                w[i][j]=key.substring(s,e);
                s=e;
                e+=2;
            }
        }
        for(int i=nk;i<nw;i++){
            String[] temp=new String[]{w[i-1][0],w[i-1][1],w[i-1][2],w[i-1][3]};
            if(i%nk==0){
                temp=gword(temp,(i/nk)-1);
            }
            String[] prev=new String[4];
            for(int j=0;j<4;j++){
                prev[j]=w[i-nk][j];
            }

            String[] pw=xorword(prev,temp);
            for(int j=0;j<4;j++){
                w[i][j]=pw[j];
            }

        }
        int wi=0;
        for(int rr=0;rr<=nr;rr++){
            for(int c=0;c<4;c++){
                for(int row=0;row<4;row++){
                    r[rr][row][c]=w[wi+c][row];
                }
            }
            wi+=4;
        }
    }

    public static String AES_encrypt(String p,String k){
        int keys=0,rounds=0,words=0;
        if(k.length()==32){
            keys=4;
            rounds=10;
            words=44;
            System.out.print("Performing AES-128");
        }
        else if(k.length()==48){
            keys=6;
            rounds=12;
            words=52;
            System.out.print("Performing AES-192");
        }
        else if(k.length()==64){
            keys=8;
            rounds=14;
            words=60;
            System.out.print("Performing AES-259");
        }
        System.out.println(" Encryption: ");
        String mat[][]=new String[4][4];
        int s=0,e=2;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                mat[j][i]=p.substring(s,e);
                s=e;
                e+=2;
            }
        }

        String rks[][][]=new String[rounds+1][4][4];
        Round_keys(rks,k,keys,rounds,words);

        XOR(mat,rks[0]);

        for(int i=1;i<=rounds-1;i++){
            Subbox(mat);
            ShiftRows(mat);
            MixColumns(mat);
            XOR(mat,rks[i]);
        }

        Subbox(mat);
        ShiftRows(mat);
        XOR(mat,rks[rounds]);

        String enc="";
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                enc+=mat[j][i];
            }
        }
        return enc;
    }

    public static String AES_decrypt(String enc,String k){
        int keys=0,rounds=0,words=0;
        if(k.length()==32){
            keys=4;
            rounds=10;
            words=44;
            System.out.print("Performing AES-128");
        }
        else if(k.length()==48){
            keys=6;
            rounds=12;
            words=52;
            System.out.print("Performing AES-192");
        }
        else if(k.length()==64){
            keys=8;
            rounds=14;
            words=60;
            System.out.print("Performing AES-259");
        }
        System.out.println(" Decryption: ");
        enc=enc.toUpperCase();
        k=k.toUpperCase();
        String rks[][][]=new String[rounds+1][4][4];
        Round_keys(rks,k,keys,rounds,words);

        String mat[][]=new String[4][4];
        int s=0,e=2;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                mat[j][i]=enc.substring(s,e);
                s=e;
                e+=2;
            }
        }

        XOR(mat,rks[rounds]);

        for(int i=rounds-1;i>=1;i--){
            InvShiftRows(mat);
            InvSubbox(mat);
            XOR(mat,rks[i]);
            InvMixColumns(mat);
        }

        InvShiftRows(mat);
        InvSubbox(mat);
        XOR(mat,rks[0]);

        String pt="";
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                pt+=mat[j][i];
            }
        }
        return pt;
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
        String key="";
        int l,r;
        boolean flag=false;
        try {
            while (true) {

                l=0;
                r=16;
                String decrypted="";
                String encrypted=dis.readUTF();
                if(encrypted.equals("end")){
                    System.out.println("Client Closed");
                    break;
                }
                System.out.println("Received from server encrypted: "+encrypted);
                System.out.print("Enter Key for decryption: ");
                key=sc.nextLine();

                decrypted+=AES_decrypt(encrypted,key);

                System.out.println("Decrypted by client: "+decrypted);
                System.out.print("Enter plaintext: ");
                line=sc.nextLine();
                if(line.equals("end")){
                    System.out.println("Client Closed");
                    dos.writeUTF("end");
                    break;
                }




                encrypted="";
                System.out.print("Enter key for encryption(in hexadecimal format): ");
                key=sc.nextLine();


                encrypted+=AES_encrypt(line,key);

                System.out.println("Encrypted by client: "+encrypted);
                dos.writeUTF(encrypted);
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

        catch(IOException e){

            System.out.println(e.getMessage());

        }

    }

    public static void main(String args[]){

        Client c=new Client("127.0.0.1",3000);

    }

}