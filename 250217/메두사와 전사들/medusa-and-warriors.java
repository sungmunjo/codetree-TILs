

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static int N;
	static int M;

	static Tile map[][];

	static int dr[] = { -1, 1, 0, 0 };
	static int dr2[] = { 0, 0, -1, 1 };
	static int dc[] = { 0, 0, -1, 1 };
	static int dc2[] = { -1, 1, 0, 0 };
	static int distanceMap[][];
	static int lookDr[][] = { { -1, -1, -1 }, { 1, 1, 1 }, { -1, 0, 1 }, { -1, 0, 1 } };
	static int lookDc[][] = { { -1, 0, 1 }, { -1, 0, 1 }, { -1, -1, -1 }, { 1, 1, 1 } };
	static int[][] dontMove;
	static LocationInfo currentLoc;
	static LocationInfo dest;
	static Person[] armi;
	static int minDistance;

	static class Tile {
		boolean tree;
		int personSize;
	}

	static class Person {
		int r;
		int c;
		boolean died;
		boolean stuned;
	}

	static class LocationInfo {
		int r;
		int c;

		public LocationInfo(int nr, int nc) {
			this.r = nr;
			this.c = nc;
		}

		public LocationInfo() {
			this.r = 0;
			this.c = 0;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

		StringTokenizer st;
		StringBuffer sb = new StringBuffer();
		st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(br.readLine());
		currentLoc = new LocationInfo();
		dest = new LocationInfo();
		currentLoc.r = Integer.parseInt(st.nextToken());
		currentLoc.c = Integer.parseInt(st.nextToken());
		dest.r = Integer.parseInt(st.nextToken());
		dest.c = Integer.parseInt(st.nextToken());

		armi = new Person[M];

		st = new StringTokenizer(br.readLine());
		for (int m = 0; m < M; m++) {
			armi[m] = new Person();
			armi[m].r = Integer.parseInt(st.nextToken());
			armi[m].c = Integer.parseInt(st.nextToken());
			armi[m].died = false;
			armi[m].stuned = false;
		}

		map = new Tile[N][N];
		distanceMap = new int[N][N];
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				distanceMap[r][c] = 987654321;
			}
		}
		dontMove = new int[N][N];
		for (int r = 0; r < N; r++) {
			st = new StringTokenizer(br.readLine());
			for (int c = 0; c < N; c++) {
				map[r][c] = new Tile();
				map[r][c].personSize = 0;
				map[r][c].tree = Integer.parseInt(st.nextToken()) == 1 ? true : false;
			}
		}

		for (int m = 0; m < M; m++) {
			map[armi[m].r][armi[m].c].personSize++;
		}

		findRoad();

		if (distanceMap[currentLoc.r][currentLoc.c] == 987654321) {
			sb.append("-1");
			bw.write(sb.toString());
			bw.flush();
		} else {
			findDest(sb);
			bw.write(sb.toString());
			bw.flush();
		}

	}

	private static void findRoad() {
		Queue<LocationInfo> Q = new LinkedList<LocationInfo>();
		boolean[][] visited = new boolean[N][N];
		int startR = dest.r;
		int startC = dest.c;
		visited[startR][startC] = true;
		Q.add(new LocationInfo(startR, startC));
		int count = 0;
		while (!Q.isEmpty()) {
			int size = Q.size();
			count++;
			for (int s = 0; s < size; s++) {
				LocationInfo item = Q.poll();
				distanceMap[item.r][item.c] = count;
				for (int d = 0; d < 4; d++) {
					int nr = item.r + dr[d];
					int nc = item.c + dc[d];
					if (checkMap(nr, nc) && !map[nr][nc].tree && !visited[nr][nc]) {
						visited[nr][nc] = true;

						Q.add(new LocationInfo(nr, nc));
					}
				}
			}
		}

	}

	private static boolean checkMap(int nr, int nc) {
		if (nr >= 0 && nr < N && nc >= 0 && nc < N)
			return true;
		return false;
	}

	private static void findDest(StringBuffer sb) {
		while (true) {
//		for(int i=0;i<5;i++) {
			move();
			int stunCount = watch();
//			for (int r = 0; r < N; r++) {
//				for (int c = 0; c < N; c++) {
//					if(r == currentLoc.r && c == currentLoc.c) {
//						System.out.print("A ");
//					}
//					else if(map[r][c].personSize > 0) {
//						System.out.print(map[r][c].personSize + " ");
//					}
//					else {
//						System.out.print(dontMove[r][c] == 1 ? "B " : dontMove[r][c] == 2 ? "C " : "D ");
//					}
//					
//					
//				}
//				System.out.println();
//			}
//			System.out.println();
			int MoveCount = armiMove(1);
			MoveCount += armiMove(2);
			int killCount = killArmi();

			if (currentLoc.r == dest.r && currentLoc.c == dest.c) {
				sb.append("0");
				break;
			} else {
				sb.append(MoveCount + " " + stunCount + " " + killCount + "\n");
			}
		}

//		}

	}

	private static int killArmi() {
		int killCount = 0;
		for (Person item : armi) {
			if (!item.died && item.r == currentLoc.r && item.c == currentLoc.c) {
				item.died = true;
				map[item.r][item.c].personSize--;
				killCount++;
			}
		}
		return killCount;
	}

	private static int armiMove(int seq) {
		int moveCount = 0;
		for (Person item : armi) {
			if (!item.died && dontMove[item.r][item.c] != 1) {
				int minDistance = Math.abs(currentLoc.r - item.r) + Math.abs(currentLoc.c - item.c);
				int minDir = -1;
				for (int d = 0; d < 4; d++) {
					int nr = item.r;
					int nc = item.c;

					if (seq == 1) {
						nr += dr[d];
						nc += dc[d];
					} else {
						nr += dr2[d];
						nc += dc2[d];
					}

					if (checkMap(nr, nc) && dontMove[nr][nc] != 1) {
						int distacne = Math.abs(nr - currentLoc.r) + Math.abs(nc - currentLoc.c);

						if (distacne < minDistance) {
							minDistance = distacne;
							minDir = d;
						}
					}
				}
				if (minDir != -1) {
					int toMoveR = item.r;
					int toMoveC = item.c;
					if (seq == 1) {
						toMoveR += dr[minDir];
						toMoveC += dc[minDir];
					} else {
						toMoveR += dr2[minDir];
						toMoveC += dc2[minDir];
					}

					map[item.r][item.c].personSize--;
					map[toMoveR][toMoveC].personSize++;

					item.r = toMoveR;
					item.c = toMoveC;
					moveCount++;
				}
			}
		}
		return moveCount;
	}

	private static int watch() {
		int maxStuned = -1;
		int maxDir = -1;

		for (int d = 0; d < 4; d++) {
			for (int r = 0; r < N; r++) {
				for (int c = 0; c < N; c++) {
					dontMove[r][c] = 0;
				}
			}
			
			if(d == 0) {
				int tempStunCount = lookUp(false);
				if (tempStunCount > maxStuned) {
					maxStuned = tempStunCount;
					maxDir = d;
				}
			}else if(d == 1) {
				int tempStunCount = lookDown(false);
				if (tempStunCount > maxStuned) {
					maxStuned = tempStunCount;
					maxDir = d;
				}
			}else if(d == 2) {
				int tempStunCount = lookLeft(false);
				if (tempStunCount > maxStuned) {
					maxStuned = tempStunCount;
					maxDir = d;
				}
			}else {
				int tempStunCount = lookRight(false);
				if (tempStunCount > maxStuned) {
					maxStuned = tempStunCount;
					maxDir = d;
				}
			}
		}
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				dontMove[r][c] = 0;
			}
		}
		if(maxDir == 0) {
			lookUp(false);
		}else if(maxDir == 1) {
			lookDown(false);
		}else if( maxDir == 2) {
			lookLeft(false);
		}else {
			lookRight(false);
		}
//		checkDontMove(maxStuned, maxDir);
//		dontMove = saveDontMove[maxDir];
		return maxStuned;

	}

	private static int lookRight(boolean b) {
		int count = 0;
		for(int c = currentLoc.c + 1; c < N; c++) {
			for(int r = currentLoc.r - (c - currentLoc.c); r <= currentLoc.r + (c - currentLoc.c); r++) {
				if(checkMap(r, c)) {
					if(map[r][c].personSize > 0) {
						count += map[r][c].personSize;
						checkCantWatch(3, r, c);
						dontMove[r][c] = 1;
					}else if(dontMove[r][c] == 0){
						dontMove[r][c] = 1;
					}
				}
			}
		}
		return count;
	}

	private static int lookLeft(boolean b) {
		int count = 0;
		for(int c = currentLoc.c - 1; c >= 0; c--) {
			for(int r = currentLoc.r - (currentLoc.c - c); r <= currentLoc.r + (currentLoc.c - c); r++) {
				if(checkMap(r, c)) {
					if(map[r][c].personSize > 0) {
						count += map[r][c].personSize;
						checkCantWatch(2, r, c);
						dontMove[r][c] = 1;
					}else if(dontMove[r][c] == 0){
						dontMove[r][c] = 1;
					}
				}
			}
		}
		return count;
	}

	private static int lookDown(boolean b) {
		int count = 0;
		for(int r = currentLoc.r + 1; r < N; r++) {
			for(int c = currentLoc.c - (r - currentLoc.r); c <= currentLoc.c + (r - currentLoc.r); c++) {
				if(checkMap(r, c)) {
					if(map[r][c].personSize > 0) {
						count += map[r][c].personSize;
						checkCantWatch(1, r, c);
						dontMove[r][c] = 1;
					}else if(dontMove[r][c] == 0){
						dontMove[r][c] = 1;
					}
				}
			}
		}
		return count;
	}

	private static int lookUp(boolean b) {
		int count = 0;
		for(int r = currentLoc.r - 1; r >= 0; r--) {
			for(int c = currentLoc.c - (currentLoc.r - r); c <= currentLoc.c + (currentLoc.r - r); c++) {
				if(checkMap(r, c)) {
					if(map[r][c].personSize > 0 && dontMove[r][c] == 0) {
						count += map[r][c].personSize;
						checkCantWatch(0, r, c);
						dontMove[r][c] = 1;
					}else if(dontMove[r][c] == 0){
						dontMove[r][c] = 1;
					}
				}
			}
		}
		return count;
	}

	private static void checkDontMove(int maxStuned, int maxDir) {
//		for (int r = 0; r < N; r++) {
//			for (int c = 0; c < N; c++) {
//				dontMove[r][c] = 0;
//			}
//		}
//		Queue<LocationInfo> Q = new LinkedList<LocationInfo>();
//		LocationInfo currentInfo = new LocationInfo();
//		currentInfo.r = currentLoc.r;
//		currentInfo.c = currentLoc.c;
//		Q.add(currentInfo);
//		while (!Q.isEmpty()) {
//			LocationInfo item = Q.poll();
//
//			for (int dir = 0; dir < 3; dir++) {
//				int nr = item.r + lookDr[maxDir][dir];
//				int nc = item.c + lookDc[maxDir][dir];
//
//				if (checkMap(nr, nc) && (dontMove[nr][nc] == 0)) {
//					if (map[nr][nc].personSize > 0) {
//						checkCantWatch(maxDir, nr, nc, currentInfo);
//						Q.add(new LocationInfo(nr, nc));
//						dontMove[nr][nc] = 1;
//					} else {
//						Q.add(new LocationInfo(nr, nc));
//						dontMove[nr][nc] = 1;
//					}
//				}
//			}
//		}

	}

	private static void checkCantWatch(int d, int nr, int nc) {
		if (d < 2) {
			if (nc > currentLoc.c) {
				int[] disDir = { 1, 2 };
				checkDisable(disDir, d, nr, nc);
			} else if (nc < currentLoc.c) {
				int[] disDir = { 0, 1 };
				checkDisable(disDir, d, nr, nc);
			} else {
				int[] disDir = { 1, 1 };
				checkDisable(disDir, d, nr, nc);
			}
		} else {
			if (nr > currentLoc.r) {
				int[] disDir = { 1, 2 };
				checkDisable(disDir, d, nr, nc);
			} else if (nr < currentLoc.r) {
				int[] disDir = { 0, 1 };
				checkDisable(disDir, d, nr, nc);
			} else {
				int[] disDir = { 1, 1 };
				checkDisable(disDir, d, nr, nc);
			}
		}
	}

	private static void checkDisable(int[] disDir, int d, int nr, int nc) {
		if(d == 0) {
			for(int r = nr - 1; r >= 0; r--) {
				for (int c = nc + (nr - r) * lookDc[d][disDir[0]] ;
						c <= nc + (nr - r) * lookDc[d][disDir[1]];
						c++) {
					if(checkMap(r, c)) {
						dontMove[r][c] = 2;
					}
				}
			}
			
		}else if(d == 1){
			for(int r = nr + 1; r <  N; r++) {
				for (int c = nc + (r - nr) * lookDc[d][disDir[0]] ;
						c <= nc + (r - nr) * lookDc[d][disDir[1]];
						c++) {
					if(checkMap(r, c)) {
						dontMove[r][c] = 2;
					}
				}
			}
		}else if(d == 2) {
			for(int c = nc - 1; c >= 0; c--) {
				for (int r = nr + (nc - c) * lookDr[d][disDir[0]] ;
						r <= nr + (nc - c) * lookDr[d][disDir[1]];
						r++) {
					if(checkMap(r, c)) {
						dontMove[r][c] = 2;
					}
				}
			}
		}else {
			for(int c = nc + 1; c < N; c++) {
				for (int r = nr + (c - nc) * lookDr[d][disDir[0]] ;
						r < nr + (c - nc) * lookDr[d][disDir[1]];
						r++) {
					if(checkMap(r, c)) {
						dontMove[r][c] = 2;
					}
				}
			}
		}
		
		
//		Queue<LocationInfo> Q = new LinkedList<LocationInfo>();
//		LocationInfo currentInfo = new LocationInfo();
//		currentInfo.r = nr;
//		currentInfo.c = nc;
//		Q.add(currentInfo);
//		while (!Q.isEmpty()) {
//			LocationInfo item = Q.poll();
//
//			for (int index = 0; index < disDir.length; index++) {
//				int nnr = item.r + lookDr[d][disDir[index]];
//				int nnc = item.c + lookDc[d][disDir[index]];
//
//				if (checkMap(nnr, nnc)) {
//					dontMove[nnr][nnc] = 2;
//					Q.add(new LocationInfo(nnr, nnc));
//				}
//			}
//
//		}

	}

	private static void move() {
		int minDir = -1;
		int minDis = 987654321;
		for (int d = 0; d < 4; d++) {
			int nr = currentLoc.r + dr[d];
			int nc = currentLoc.c + dc[d];

			if (checkMap(nr, nc) && distanceMap[nr][nc] < minDis && !map[nr][nc].tree) {
				minDis = distanceMap[nr][nc];
				minDir = d;
			}

		}

//		System.out.println(minDir);
		currentLoc.r = currentLoc.r + dr[minDir];
		currentLoc.c = currentLoc.c + dc[minDir];
//		System.out.println(currentLoc.r + " " + currentLoc.c);
		if (map[currentLoc.r][currentLoc.c].personSize != 0) {
			for (int m = 0; m < M; m++) {
				if (armi[m].r == currentLoc.r && armi[m].c == currentLoc.c) {
					armi[m].died = true;
					map[armi[m].r][armi[m].c].personSize--;
				}
			}
		}
	}

}
