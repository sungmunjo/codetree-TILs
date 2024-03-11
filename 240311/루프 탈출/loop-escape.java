import java.io.*;
import java.nio.Buffer;

public class Main {

    static int n;

    static int union[];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        n = Integer.parseInt(br.readLine());

        union = new int[n + 1];
        for (int i = 1; i < n + 1; i++) {
            union[i] = Integer.parseInt(br.readLine());
        }

        int count = 0;

        for(int i = 1; i<n + 1;i++){
            boolean visited[] = new boolean[n + 1];
            int parent = i;
            boolean flag = false;
            while(parent != 0){
                parent = union[parent];
                if(visited[parent]){
                    flag = true;
                    break;
                }else{
                    visited[parent] = true;
                }
            }
            if(!flag){
//                System.out.println(i);
                count++;
            }
        }

        sb.append(count);
        bw.write(sb.toString());
        bw.flush();

    }

}