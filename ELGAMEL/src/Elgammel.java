import java.util.*;
import java.math.*;
public class Elgammel {
    public static int gcd(int a,int b){
        if(b==0){
            return a;
        }
        return gcd(b,a%b);
    }
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
    public static ArrayList<Integer> keys(int q){
        int alpha;
        int phi_n=0;
        ArrayList<Integer>q_star=new ArrayList<>();
        for(int i=1;i<q;i++){
            if(gcd(i,q)==1){
                phi_n++;
                q_star.add(i);
            }
        }

        ArrayList<Integer>prim_roots=new ArrayList<>();

        for(int i:q_star){
            int p=i;
            for(int j=2;j<=phi_n;j++){
                p*=i;
                p=p%q;

                if(p==1 && j==phi_n && i!=1){
                    prim_roots.add(i);
                }
            }
        }
        int count=prim_roots.size();

        alpha=prim_roots.get((int)(Math.random()*count));
        int xa=(int)(Math.random()*(q-2-2+1)+2);
        int ya=((int)Math.pow(alpha,xa))%q;
        ArrayList<Integer>keys=new ArrayList<>();
        keys.add(xa);
        keys.add(q);
        keys.add(alpha);
        keys.add(ya);
        return keys;
    }
}
