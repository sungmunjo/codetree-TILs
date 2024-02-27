import java.io.*;
import java.util.StringTokenizer;

public class Main {
    static int N;
    static int [] arr;

    static int maxCount = -1;
    static int tempVal;
    static int toRemove;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        arr = new int[N];

        for(int i=0;i<N;i++){
            st = new StringTokenizer(br.readLine());
            arr[i] = Integer.parseInt(st.nextToken());
        }
        int count = 0;

        for(int i=0;i<N;i++){
            toRemove = arr[i];
            tempVal = -1;
            count = 0;
            for(int j=0;j<N;j++){
                if(arr[j] != toRemove){
                    if(tempVal == arr[j]){
                        count++;
                    }else{
                        maxCount = Math.max(maxCount, count);
                        tempVal = arr[j];
                        count = 1;
                    }
                }
            }
        }

        sb.append(maxCount);
        bw.write(sb.toString());
        bw.flush();




    }
}