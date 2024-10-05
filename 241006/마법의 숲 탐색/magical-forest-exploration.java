import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static int R;
	static int C;
	static int K;

	static int[][] map;

	static class moveReturn {
		boolean checkMove;
		int afterR;
		int afterC;
		int afterDir;
	}

	static class Golam {
		int id;
		int dir;
		int locc;
		int locr;
	}

	static int[] dr = { -1, 0, 1, 0 };
	static int[] dc = { 0, 1, 0, -1 };

	static HashMap<Integer, Golam> golamList;

	static int[][][] checkList = { { { 2, 0 }, { 1, -1 }, { 1, 1 } },
			{ { -1, -1 }, { 0, -2 }, { 1, -1 }, { 1, -2 }, { 2, -1 } },
			{ { -1, 1 }, { 0, 2 }, { 1, 1 }, { 1, 2 }, { 2, 1 } } };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

		StringBuffer sb = new StringBuffer();

		golamList = new HashMap<>();

		StringTokenizer st = new StringTokenizer(br.readLine());

		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		map = new int[R + 3][C];

		int result = 0;
		
		for (int gCount = 0; gCount < K; gCount++) {
			st = new StringTokenizer(br.readLine());
			int currentR = 1;
			int currentC = Integer.parseInt(st.nextToken()) - 1;
			int currentDir = Integer.parseInt(st.nextToken());

			boolean checkMove = true;
			
			int moveCount = 0;
			
			while (checkMove) {
				moveReturn returnValue = moveGloam(currentR, currentC, currentDir);

				checkMove = returnValue.checkMove;
				
				if(!checkMove) {
					checkMove = false;
				}else {
					currentR = returnValue.afterR;
					currentC = returnValue.afterC;
					currentDir = returnValue.afterDir;
				}
//				System.out.println(gCount + " : " +currentR + " : " + currentC + " : " + currentDir);
				moveCount++;
				
			}
			
			if(currentR < 3) {
				resetMap();
				continue;
			}
			
			Golam newG = new Golam();
			newG.id = gCount + 1;
			newG.locr = currentR;
			newG.locc = currentC;
			newG.dir = currentDir;
			golamList.put(newG.id, newG);
			result += (calculScore(newG) + 1);
//			System.out.println(result);
			drowMap(newG);
//			showMap();
			
		}
		sb.append(result);
		
		bw.write(sb.toString());
		bw.flush();

	}

	private static void showMap() {
		for(int r=0;r<R + 3; r++) {
			for(int c=0;c<C;c++) {
//				System.out.print(map[r][c] + " ");
			}
//			System.out.println();
		}
	}

	private static void drowMap(Golam newG) {
		map[newG.locr][newG.locc] = newG.id;
		for(int d=0; d<4;d++) {
			int nr = newG.locr + dr[d];
			int nc = newG.locc + dc[d];
			
			map[nr][nc] = newG.id;
		}
	}

	private static int calculScore(Golam newG) {
		Queue<Golam> Q = new LinkedList<>();
		Q.add(newG);
		boolean [] visited = new boolean[K + 1];
		visited[newG.id] = true;
		
		int finMax = -1;
		
		while(!Q.isEmpty()) {
			Golam item = Q.poll();
			
			int exitR = item.locr + dr[item.dir];
			int exitC = item.locc + dc[item.dir];
			
			if(item.locr > finMax) {
				finMax = item.locr;
			}
			
			for(int d=0;d<4;d++) {
				int nr = exitR + dr[d];
				int nc = exitC + dc[d];
				
				if(checkMap(nr,nc) && map[nr][nc] != 0 && !visited[map[nr][nc]]) {
					Q.add(golamList.get(map[nr][nc]));
					visited[map[nr][nc]] = true;
				}
			}
		}
		
		return finMax - 2;
	}

	private static void resetMap() {
		map = new int[R + 3][C];
	}

	private static moveReturn moveGloam(int currentR, int currentC, int currentDir) {
		moveReturn output = new moveReturn();
		boolean moveable = false;
		int moveMode = 0;
		for (int i = 0; i < checkList.length; i++) {
			boolean subMoveCheck = true;

			for (int r = 0; r < checkList[i].length; r++) {
				int nr = currentR + checkList[i][r][0];
				int nc = currentC + checkList[i][r][1];
//				System.out.println(nr + " : " + nc);
				if (!checkPostible(nr, nc)) {
					
//					System.out.println("?");
					subMoveCheck = false;
					break;
				}
			}

			if (subMoveCheck) {
				moveMode = i;
				moveable = true;
				break;
			}
		}
		if (moveable) {
			if (moveMode == 0) {
				output.afterR = currentR + 1;
				output.afterC = currentC;
				output.afterDir = currentDir;

			} else if (moveMode == 1) {
				output.afterR = currentR + 1;
				output.afterC = currentC - 1;
				output.afterDir = (currentDir + 3) % 4;
			} else if (moveMode == 2) {
				output.afterR = currentR + 1;
				output.afterC = currentC + 1;
				output.afterDir = (currentDir + 1) % 4;
			}
			output.checkMove = true;
		} else {
			output.checkMove = false;
		}

		return output;
	}
	
	private static boolean checkMap(int nr, int nc) {
		if(nr >= (R + 3) || nr < 0 || nc >= (C) || nc < 0) return false;
		return true;
	}
	

	private static boolean checkPostible(int nr, int nc) {
		if ((nr >= R + 3) || nr < 0 || (nc >= C) || nc < 0 || map[nr][nc] != 0)
			return false;
		return true;
	}
}