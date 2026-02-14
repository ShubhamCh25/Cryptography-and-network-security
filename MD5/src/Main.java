
import java.util.*;
public class Main {
    public static String binToHex(String s){
        HashMap<String,Character>m=new HashMap<>();
        m.put("0000",'0');
        m.put("0001",'1');
        m.put("0010",'2');
        m.put("0011",'3');
        m.put("0100",'4');
        m.put("0101",'5');
        m.put("0110",'6');
        m.put("0111",'7');
        m.put("1000",'8');
        m.put("1001",'9');
        m.put("1010",'A');
        m.put("1011",'B');
        m.put("1100",'C');
        m.put("1101",'D');
        m.put("1110",'E');
        m.put("1111",'F');
        int st=0,e=4;
        String res="";

        while(e<=s.length()) {
            res+=m.get(s.substring(st,e));

            st=e;
            e+=4;
        }
        return res;
    }
    public static String hexToBin(String s){
        HashMap<Character,String>m=new HashMap<>();
        m.put('0', "0000");
        m.put('1', "0001");
        m.put('2', "0010");
        m.put('3', "0011");
        m.put('4', "0100");
        m.put('5', "0101");
        m.put('6', "0110");
        m.put('7', "0111");
        m.put('8', "1000");
        m.put('9', "1001");
        m.put('A', "1010");
        m.put('B', "1011");
        m.put('C', "1100");
        m.put('D', "1101");
        m.put('E', "1110");
        m.put('F', "1111");
        String res="";
        for(char i:s.toCharArray()){
            res+=m.get(i);
        }
        return res;
    }

    public static String endian(String s){
        String res="";
        int start=0,end=8;

        while(end<=s.length()){
            String sub=s.substring(start,end);
            start=end;
            end+=8;
            res=sub+res;
        }
        System.out.println(s+" "+res+" endian");
        return res;
    }
    public static String OR(String a,String b){
        a=hexToBin(a);
        b=hexToBin(b);
        String res="";
        for(int i=0;i<a.length();i++){
            char c1=a.charAt(i),c2=b.charAt(i);
            res+=(c1==c2 && c1=='0')?'0':'1';
        }
        return binToHex(res);
    }
    public static String AND(String a,String b){
        a=hexToBin(a);
        b=hexToBin(b);
        String res="";
        for(int i=0;i<a.length();i++){
            char c1=a.charAt(i),c2=b.charAt(i);
            res+=(c1=='0' || c2=='0')?'0':'1';
        }
        return binToHex(res);
    }
    public static String XOR(String a,String b){
        a=hexToBin(a);
        b=hexToBin(b);
        String res="";
        for(int i=0;i<a.length();i++){
            int c1=(a.charAt(i)-'0'),c2=b.charAt(i)-'0';
            res+=((c1+c2)%2==0)?'0':'1';
        }
        return binToHex(res);
    }
    public static String NOT(String a){
        a=hexToBin(a);

        String res="";
        for(int i=0;i<a.length();i++){
            char c=a.charAt(i);
            res+=(c=='0')?'1':'0';
        }
        return binToHex(res);
    }

    public static String nonlinear(int round,String b,String c,String d){
        if(round==1){
            return OR(AND(b,c),AND(NOT(b),d));
        }
        else if(round==2){
            return OR(AND(b,d),AND(c,NOT(d)));
        }
        else if(round==3){
            return XOR(XOR(b,c),d);
        }
        else{
            return XOR(c,OR(b,NOT(d)));
        }
    }
    public static String ADD(String a,String b){
        long op1 = Long.parseLong(a, 16) & 0xFFFFFFFFL;
        long op2 = Long.parseLong(b, 16) & 0xFFFFFFFFL;
        long sum = (op1 + op2) & 0xFFFFFFFFL;

        String res = Long.toBinaryString(sum);
       // String res=Integer.toBinaryString(op1);
        while(res.length()<32){
            res='0'+res;
        }
        return binToHex(res);
    }
    public static String left_shift(String s,int times){
        s=hexToBin(s);
        String res="";
        for(int i=times;i<32;i++){
            res+=s.charAt(i);
        }
        for(int i=0;i<times;i++){
            res+=s.charAt(i);
        }
        return binToHex(res);
    }
    public static ArrayList<String> MD5_helper(String A, String B, String C, String D, String plain){
        int key_count=0;
        String[] K = {
                "D76AA478","E8C7B756","242070DB","C1BDCEEE","F57C0FAF","4787C62A","A8304613","FD469501",
                "698098D8","8B44F7AF","FFFF5BB1","895CD7BE",
                "6B901122","FD987193","A679438E","49B40821",
                "F61E2562","C040B340","265E5A51","E9B6C7AA",
                "D62F105D","02441453","D8A1E681","E7D3FBC8",
                "21E1CDE6","C33707D6","F4D50D87","455A14ED",
                "A9E3E905","FCEFA3F8","676F02D9","8D2A4C8A",
                "FFFA3942","8771F681","6D9D6122","FDE5380C",
                "A4BEEA44","4BDECFA9","F6BB4B60","BEBFBC70",
                "289B7EC6","EAA127FA","D4EF3085","04881D05",
                "D9D4D039","E6DB99E5","1FA27CF8","C4AC5665",
                "F4292244","432AFF97","AB9423A7","FC93A039",
                "655B59C3","8F0CCC92","FFEFF47D","85845DD1",
                "6FA87E4F","FE2CE6E0","A3014314","4E0811A1",
                "F7537E82","BD3AF235","2AD7D2BB","EB86D391"
        };
        int[][] shift = {
                { 7,12,17,22, 7,12,17,22, 7,12,17,22, 7,12,17,22 },
                { 5, 9,14,20, 5, 9,14,20, 5, 9,14,20, 5, 9,14,20 },
                { 4,11,16,23, 4,11,16,23, 4,11,16,23, 4,11,16,23 },
                { 6,10,15,21, 6,10,15,21, 6,10,15,21, 6,10,15,21 }
        };
        String M[]=new String[16];
        int s=0,e=32;
        int idx=0;

        while(e<=plain.length()){
            M[idx++]=endian(plain.substring(s,e));
            s=e;
            e+=32;

        }

        int[][] m_values={
                { 0, 1, 2, 3, 4, 5, 6, 7,
                        8, 9,10,11,12,13,14,15 },
                { 1, 6,11, 0, 5,10,15, 4,
                        9,14, 3, 8,13, 2, 7,12 },
                { 5, 8,11,14, 1, 4, 7,10,
                        13, 0, 3, 6, 9,12,15, 2 },
                { 0, 7,14, 5,12, 3,10, 1,
                        8,15, 6,13, 4,11, 2, 9 }
        };

        for(int i=0;i<4;i++){

            for(int j=0;j<16;j++){

                String E=nonlinear(i+1,B,C,D);
                A=ADD(K[key_count++],ADD(binToHex(M[m_values[i][j]]),ADD(A,E)));
                A=left_shift(A,shift[i][j]);
                A=ADD(A,B);

                String temp=C;
                C=B;
               B=A;
               A=D;
               D=temp;
            }
        }
        ArrayList<String>res=new ArrayList<>();
        res.add(A);
        res.add(B);
        res.add(C);
        res.add(D);
          return res;
    }
    public static String MD5(String plain){

        int n=plain.length();
        int x=1;
        while(512*x-64<n){
            x++;
        }
        String bin=plain;
        int actual=512*x-64-n;
        bin+='1';
        for(int i=1;i<=actual-1;i++){
            bin+='0';
        }
        String padding=Integer.toBinaryString(n);

        while(padding.length()<64){
            padding='0'+padding;
        }

        bin+=endian(padding);
        String hashcode="";
        int start=0,end=512;
        String A = "67452301";
        String B = "EFCDAB89";
        String C = "98BADCFE";
        String D = "10325476";
        String AA = A, BB = B, CC = C, DD = D;
        for(int i=0;i<x;i++){
            String sub=bin.substring(start,end);
            start=end;
            end+=512;

            ArrayList<String>result=MD5_helper(A,B,C,D,sub);
            A=ADD(AA,result.get(0));
            result.remove(0);
            B=ADD(BB,result.get(0));
            result.remove(0);
            C=ADD(CC,result.get(0));
            result.remove(0);
            D=ADD(DD,result.get(0));
            result.remove(0);
            AA=A;
            BB=B;
            CC=C;
            DD=D;
        }
        A=hexToBin(A);
        B=hexToBin(B);
        C=hexToBin(C);
        D=hexToBin(D);

        return binToHex( endian(A)+endian(B)+endian(C)+endian(D));

    }
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        String plain=sc.nextLine();
        String bin="";
        for(char i:plain.toCharArray()){
            String x=Integer.toBinaryString((int)i);
            while(x.length()<8){
                x='0'+x;
            }
            bin+=x;
        }
        System.out.println(MD5(bin));
    }
}