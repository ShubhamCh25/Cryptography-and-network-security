import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int c=sc.nextInt();
        sc.nextLine();
        String input=sc.nextLine();
        int size=0;
        String plain="";
        for(char i:input.toCharArray()){
            if(i!=' '){
                plain+=i;
                size++;
            }
        }

        int col=c,row=(int)Math.ceil((double)size/c);

        char mat[][]=new char[row][col];
        String key=sc.nextLine();
        int st=0;
        char last=122;
        String encrypt="";
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                char ch;
                if(st<plain.length()){
                   ch=plain.charAt(st++);
                    mat[i][j]=ch;
                }
                else{
                   ch=last--;

                }
                mat[i][j]=ch;
            }
        }
        System.out.println(size+" "+col);
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                System.out.print(mat[i][j]+" ");
            }
            System.out.println();
        }


    }
}