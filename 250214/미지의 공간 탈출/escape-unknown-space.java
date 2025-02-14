
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static int[][] nextMap = { { 3, 2, 5, 4 }, { 2, 3, 5, 4 }, { 0, 1, 5, 4 }, { 1, 0, 5, 4 }, { 0, 1, 2, 3 },
			{ -1, -1, -1, -1 } };
	static int[] dr = { 0, 0, 1, -1 };
	static int[] dc = { 1, -1, 0, 0 };
	static int[][][] map;

	static int N;
	static int M;
	static int F;
	static disableBlock[] disableList;

	static location dest;
	static location firstLoc;

	static int minDivRow;
	static int maxDivRow;
	static int minDivCol;
	static int maxDivCol;

	static class location {
		int divition;
		int row;
		int col;
	}

	static class disableBlock {
		int row;
		int col;
		int dir;
		int v;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

		StringTokenizer st;
		StringBuilder sb = new StringBuilder();

		st = new StringTokenizer(br.readLine());
		dest = new location();
		firstLoc = new location();
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		F = Integer.parseInt(st.nextToken());

		map = new int[6][][];

		map[5] = new int[N][N];
		boolean finMin = false;
		for (int r = 0; r < N; r++) {
			st = new StringTokenizer(br.readLine());
			for (int c = 0; c < N; c++) {
				map[5][r][c] = Integer.parseInt(st.nextToken());

				if (map[5][r][c] == 4) {
					dest.divition = 5;
					dest.row = r;
					dest.col = c;
					
					map[5][r][c] = 0;
				} else if (map[5][r][c] == 3) {
					if (!finMin) {
						minDivRow = r;
						minDivCol = c;
						finMin = true;
					}
					maxDivRow = r;
					maxDivCol = c;
				}
			}
		}

		for (int i = 0; i < 5; i++) {
			map[i] = new int[M][M];
		}

		for (int i = 0; i < 5; i++) {
			for (int r = 0; r < M; r++) {
				st = new StringTokenizer(br.readLine());
				for (int c = 0; c < M; c++) {
					map[i][r][c] = Integer.parseInt(st.nextToken());

					if (map[i][r][c] == 2) {
						firstLoc.divition = i;
						firstLoc.row = r;
						firstLoc.col = c;
					}
				}
			}
		}
		disableList = new disableBlock[F];

		for (int i = 0; i < F; i++) {
			st = new StringTokenizer(br.readLine());
			disableBlock item = new disableBlock();
			item.row = Integer.parseInt(st.nextToken());
			item.col = Integer.parseInt(st.nextToken());
			item.dir = Integer.parseInt(st.nextToken());
			item.v = Integer.parseInt(st.nextToken());
			map[5][item.row][item.col] = 1;
			disableList[i] = item;
		}

		int result = findMinDir(firstLoc);
		sb.append(result);
		bw.write(sb.toString());
		bw.flush();

	}

	private static int findMinDir(location reqIn) {
		location temp = new location();
		temp.divition = reqIn.divition;
		temp.col = reqIn.col;
		temp.row = reqIn.row;

		boolean visited[][][] = new boolean[6][][];
		visited[5] = new boolean[N][N];
		for (int i = 0; i < 5; i++) {
			visited[i] = new boolean[M][M];
		}

		visited[temp.divition][temp.row][temp.col] = true;

		Queue<location> Q = new LinkedList<location>();
		LinkedList<int[]> saveList = new LinkedList<int[]>();
		Q.add(temp);
		saveList.add(new int [] {temp.divition, temp.row, temp.col});
		int count = 0;

		while (!Q.isEmpty()) {

			int Qsize = Q.size();
			count++;
			for (int i = 0; i < Qsize; i++) {
				location pullOut = Q.poll();
				if(pullOut.divition == 5 && pullOut.row == dest.row && pullOut.col == dest.col) {
					return count - 1;
				}
				
				for(int dis = 0; dis < disableList.length; dis++) {
					disableBlock item = disableList[dis];
					if(count % item.v == 0) {
						int length = count / item.v;
						location tempLoc = new location();
						tempLoc.row = item.row + dr[item.dir] * length;
						tempLoc.col = item.col + dc[item.dir] * length;
						tempLoc.divition = 5;
						
						if(checkLocation(tempLoc)) {
							map[tempLoc.divition][tempLoc.row][tempLoc.col] = 1;
						}
					}
				}
				
				for (int d = 0; d < 4; d++) {
					int nr = pullOut.row + dr[d];
					int nc = pullOut.col + dc[d];
					int division = pullOut.divition;
					location nextLocation = colculPlace(nr, nc, division);
					
					if(checkLocation(nextLocation) 
							&& map[nextLocation.divition][nextLocation.row][nextLocation.col] == 0 
							&& !visited[nextLocation.divition][nextLocation.row][nextLocation.col]) {
						visited[nextLocation.divition][nextLocation.row][nextLocation.col] = true;
						Q.add(nextLocation);
						saveList.add(new int [] {nextLocation.divition, nextLocation.row, nextLocation.col});
					}
					
					
				}

			}

		}
		
		for(int i = 0; i < saveList.size(); i++) {
			System.out.println(saveList.get(i)[0] + " " + saveList.get(i)[1] + " " + saveList.get(i)[2] + " " );
		}
		return -1;

	}

	private static boolean checkLocation(location nextLocation) {
		if(nextLocation.divition == -1) {
			return false;
		}
		
		int [][] tempMap = map[nextLocation.divition];
		
		if(nextLocation.row >= 0 && nextLocation.row < tempMap.length && nextLocation.col >= 0 && nextLocation.col < tempMap[0].length) {
			return true;
		}
		return false;
		
	}

	private static location colculPlace(int nr, int nc, int division) {
		location returnVal = new location();
		if (nr >= 0 && nr < (division == 5 ? N : M) && nc >= 0 && nc < (division == 5 ? N : M)) {
			returnVal.row = nr;
			returnVal.col = nc;
			returnVal.divition = division;
			return returnVal;
		}
		int nextDivition = 0;
		if (nr < 0) {
			nextDivition = nextMap[division][3];
		} else if (nc < 0) {
			nextDivition = nextMap[division][1];
		} else if (nr >= (division == 5 ? N : M)) {
			nextDivition = nextMap[division][2];
		} else {
			nextDivition = nextMap[division][0];
		}
		returnVal.divition = nextDivition;
		if (division == 4) {
			if (nextDivition == 0) {
				returnVal.row = 0;
				returnVal.col = M - nr - 1;
			} else if (nextDivition == 1) {
				returnVal.row = 0;
				returnVal.col = nr;
			} else if (nextDivition == 2) {
				returnVal.row = 0;
				returnVal.col = nc;
			} else {
				returnVal.row = 0;
				returnVal.col = M - nc - 1;
			}
		} else if (nextDivition == 4) {
			if (division == 0) {
				returnVal.col = M - 1;
				returnVal.row = M - nc - 1;
			} else if (division == 1) {
				returnVal.col = 0;
				returnVal.row = nc;
			} else if (division == 2) {
				returnVal.col = nc;
				returnVal.row = M - 1;
			} else {
				returnVal.col = M - nc - 1;
				returnVal.row = 0;
			}
		} else if (nextDivition == 5) {
			if (division == 0) {
				returnVal.row = maxDivRow - nc;
				returnVal.col = maxDivCol + 1;
			} else if (division == 1) {
				returnVal.row = minDivRow + nc;
				returnVal.col = minDivCol - 1;
			} else if (division == 2) {
				returnVal.row = maxDivRow + 1;
				returnVal.col = minDivCol + nc;
			} else {
				returnVal.row = minDivRow - 1;
				returnVal.col = maxDivCol - nc;
			}
		} else {
			returnVal.row = nr;
			returnVal.col = (nc + M) % M;

		}
		
		return returnVal;
	}

}
