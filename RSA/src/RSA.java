import java.util.*;
public class RSA {
    public static int MulInv(int b,int n){
        int a=n,t1=0,t2=1,s1=1,s2=0;
        int r=a%b;
        while(r!=0){
            int q=a/b;
            int s=s1-(s2*q);
            int t=t1-(t2*q);
            a=b;
            b=r;
            t1=t2;
            t2=t;
            s1=s2;
            s2=s;
            r=a%b;
        }

        while(t2<0){
            t2+=n;
        }
        return t2;
    }
    public static int gcd(int a,int b){
        if(b==0){
            return a;
        }
        return gcd(b,a%b);
    }
    public static ArrayList<Integer> RSA(int p,int q){
        int n=p*q;
        int phi_n=(p-1)*(q-1);
        int e=(int)(Math.random()*((phi_n)+1));
        while(gcd(e,phi_n)!=1){
            e=(int)(Math.random()*((phi_n)+1));
        }
        int d=MulInv(e,phi_n);

        ArrayList<Integer>temp=new ArrayList<>();
        temp.add(d);
        temp.add(n);
        temp.add(e);
        temp.add(n);
        return temp;
    }

}