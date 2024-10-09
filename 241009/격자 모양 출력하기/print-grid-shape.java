import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M;
	
	static int [][] checkMap;
	
	public static void main(String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		checkMap = new int [N][N];
		
		for(int m = 0; m < M; m++) {
			st = new StringTokenizer(br.readLine());
			
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			
			checkMap[r-1][c-1] = (r) * (c);
			
		}
		
		for(int r=0;r<N;r++) {
			for(int c=0;c<N;c++) {
				sb.append(checkMap[r][c] + " ");
			}
			sb.append("\n");
		}
		bw.write(sb.toString());
		bw.flush();
		
	}
}