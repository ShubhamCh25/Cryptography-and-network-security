import java.util.*;
import java.math.BigInteger;

class IDEA{

    public static int add(int a,int b){
        return (a+b)&0xFFFF;
    }

    public static int mul(int a,int b){
        long x=a;
        long y=b;
        if(x==0){
            x=65536;
        }
        if(y==0){
            y=65536;
        }
        long r=(x*y)%65537;
        if(r==65536){
            r=0;
        }
        return (int)r;
    }

    public static int invAdd(int x){
        return (65536-x)&0xFFFF;
    }

    public static int eea(int x){
        if(x==0){
            return 0;
        }
        int t0=0;
        int t1=1;
        int r0=65537;
        int r1=x;
        while(r1!=0){
            int q=r0/r1;

            int r2=r0-q*r1;
            r0=r1;
            r1=r2;

            int t2=t0-q*t1;
            t0=t1;
            t1=t2;
        }
        if(t0<0){
            t0+=65537;
        }
        return t0;
    }

    public static int invMul(int x){
        int a=x;
        if(a==0){
            a=65536;
        }
        int inv=eea(a);
        if(inv==65536){
            inv=0;
        }
        return inv&0xFFFF;
    }

    public static long rotl128(long hi,long lo,int n){
        n=n%128;
        if(n==0){
            return 0;
        }
        return 0;
    }

    public static int[] subkeys(String keyHex){
        if(keyHex.length()!=32){
            throw new RuntimeException("key must be 32 hex chars");
        }
        BigInteger key=new BigInteger(keyHex,16);
        BigInteger mask=BigInteger.ONE.shiftLeft(128).subtract(BigInteger.ONE);
        int[] sub=new int[52];
        for(int i=0;i<52;i++){
            BigInteger top=key.shiftRight(112).and(BigInteger.valueOf(0xFFFF));
            sub[i]=top.intValue();
            BigInteger left=key.shiftLeft(25).and(mask);
            BigInteger right=key.shiftRight(103);
            key=left.or(right);
        }
        return sub;
    }

    public static int[] decrypt_key(int[] enc){
        int[] dec=new int[52];
        int j=0;

        dec[j]=invMul(enc[48]); j++;
        dec[j]=invAdd(enc[49]); j++;
        dec[j]=invAdd(enc[50]); j++;
        dec[j]=invMul(enc[51]); j++;

        for(int i=0;i<7;i++){
            int k=42-6*i;

            dec[j]=enc[k+4]; j++;
            dec[j]=enc[k+5]; j++;

            dec[j]=invMul(enc[k]); j++;
            dec[j]=invAdd(enc[k+2]); j++;
            dec[j]=invAdd(enc[k+1]); j++;
            dec[j]=invMul(enc[k+3]); j++;
        }

        dec[j]=enc[4]; j++;
        dec[j]=enc[5]; j++;

        dec[j]=invMul(enc[0]); j++;
        dec[j]=invAdd(enc[1]); j++;
        dec[j]=invAdd(enc[2]); j++;
        dec[j]=invMul(enc[3]);

        return dec;
    }

    public static int[] hexBlockToWords(String plainHex){
        if(plainHex.length()!=16){
            throw new RuntimeException("block must be 16 hex chars");
        }
        int[] w=new int[4];
        for(int i=0;i<4;i++){
            String t=plainHex.substring(i*4,i*4+4);
            w[i]=Integer.parseInt(t,16)&0xFFFF;
        }
        return w;
    }

    public static String wordsToHexBlock(int[] w){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<4;i++){
            String t=Integer.toHexString(w[i]&0xFFFF).toUpperCase();
            while(t.length()<4){
                t="0"+t;
            }
            sb.append(t);
        }
        return sb.toString();
    }

    public static int[] idea_cipher(int[] block,int[] keys){
        int X1=block[0];
        int X2=block[1];
        int X3=block[2];
        int X4=block[3];

        int idx=0;
        for(int r=0;r<8;r++){
            int K1=keys[idx+0];
            int K2=keys[idx+1];
            int K3=keys[idx+2];
            int K4=keys[idx+3];
            int K5=keys[idx+4];
            int K6=keys[idx+5];
            idx+=6;

            X1=mul(X1,K1);
            X2=add(X2,K2);
            X3=add(X3,K3);
            X4=mul(X4,K4);

            int t1=X1^X3;
            int t2=X2^X4;

            t1=mul(t1,K5);
            t2=add(t2,t1);
            t2=mul(t2,K6);
            t1=add(t1,t2);

            X1=X1^t2;
            X4=X4^t1;
            X2=X2^t1;
            X3=X3^t2;

            if(r<7){
                int temp=X2;
                X2=X3;
                X3=temp;
            }
        }

        int K1=keys[idx+0];
        int K2=keys[idx+1];
        int K3=keys[idx+2];
        int K4=keys[idx+3];

        int[] out=new int[4];
        out[0]=mul(X1,K1);
        out[1]=add(X2,K2);
        out[2]=add(X3,K3);
        out[3]=mul(X4,K4);
        return out;
    }

    public static String IDEA_encrypt(String plainHex,String keyHex){
        int[] enc=subkeys(keyHex);
        int[] block=hexBlockToWords(plainHex);
        int[] out=idea_cipher(block,enc);
        return wordsToHexBlock(out);
    }

    public static String IDEA_decrypt(String cipherHex,String keyHex){
        int[] enc=subkeys(keyHex);
        int[] dec=decrypt_key(enc);
        int[] block=hexBlockToWords(cipherHex);
        int[] out=idea_cipher(block,dec);
        return wordsToHexBlock(out);
    }
}
