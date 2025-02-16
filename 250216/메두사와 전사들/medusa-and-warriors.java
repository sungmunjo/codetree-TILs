

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
			if (!item.died && dontMove[item.r][item.c] == 0) {
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

		dontMove = new int[N][N];
		int[][] saveDontMove = new int[N][N];
		for (int d = 0; d < 4; d++) {
			boolean[][] cantWatch = new boolean[N][N];
			int stunCount = 0;
			saveDontMove = new int[N][N];
			Queue<LocationInfo> Q = new LinkedList<LocationInfo>();
			LocationInfo currentInfo = new LocationInfo();
			currentInfo.r = currentLoc.r;
			currentInfo.c = currentLoc.c;
			Q.add(currentInfo);
			while (!Q.isEmpty()) {
				LocationInfo item = Q.poll();

				for (int dir = 0; dir < 3; dir++) {
					int nr = item.r + lookDr[d][dir];
					int nc = item.c + lookDc[d][dir];

					if (checkMap(nr, nc) && !cantWatch[nr][nc]) {
						if (map[nr][nc].personSize > 0) {
							stunCount += map[nr][nc].personSize;
							checkCantWatch(cantWatch, d, nr, nc, currentInfo);
							cantWatch[nr][nc] = true;
							Q.add(new LocationInfo(nr, nc));
							saveDontMove[nr][nc] = 1;
						} else {
							Q.add(new LocationInfo(nr, nc));
							cantWatch[nr][nc] = true;
							saveDontMove[nr][nc] = 1;
						}
					}
				}
			}

			if (stunCount > maxStuned) {
				maxStuned = stunCount;
				maxDir = d;
				dontMove = saveDontMove;
			}
		}
//		dontMove = saveDontMove[maxDir];
		return maxStuned;

	}

	private static void checkCantWatch(boolean[][] cantWatch, int d, int nr, int nc, LocationInfo currentInfo) {
		if (d < 2) {
			if (nc > currentInfo.c) {
				int[] disDir = { 1, 2 };
				checkDisable(disDir, d, nr, nc, cantWatch);
			} else if (nc < currentInfo.c) {
				int[] disDir = { 0, 1 };
				checkDisable(disDir, d, nr, nc, cantWatch);
			} else {
				int[] disDir = { 1 };
				checkDisable(disDir, d, nr, nc, cantWatch);
			}
		} else {
			if (nr > currentInfo.r) {
				int[] disDir = { 1, 2 };
				checkDisable(disDir, d, nr, nc, cantWatch);
			} else if (nr < currentInfo.r) {
				int[] disDir = { 0, 1 };
				checkDisable(disDir, d, nr, nc, cantWatch);
			} else {
				int[] disDir = { 1 };
				checkDisable(disDir, d, nr, nc, cantWatch);
			}
		}
	}

	private static void checkDisable(int[] disDir, int d, int nr, int nc, boolean[][] cantWatch) {
		Queue<LocationInfo> Q = new LinkedList<LocationInfo>();
		LocationInfo currentInfo = new LocationInfo();
		currentInfo.r = nr;
		currentInfo.c = nc;
		Q.add(currentInfo);
		while (!Q.isEmpty()) {
			LocationInfo item = Q.poll();

			for (int index = 0; index < disDir.length; index++) {
				int nnr = item.r + lookDr[d][disDir[index]];
				int nnc = item.c + lookDc[d][disDir[index]];

				if (checkMap(nnr, nnc)) {
					cantWatch[nnr][nnc] = true;
					Q.add(new LocationInfo(nnr, nnc));
				}
			}

		}

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
