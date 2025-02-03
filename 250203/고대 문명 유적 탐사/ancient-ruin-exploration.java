
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static int K;
	static int M;
	static int map[][];
	static int nextVal[];
	static int dr[] = { -1, 0, 1, 0 };
	static int dc[] = { 0, 1, 0, -1 };
	static int addIndex = 0;

	static class maxLocation {
		int maxRoute;
		int maxCol;
		int maxRow;
		int maxScore;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

		StringTokenizer st;
		StringBuilder sb = new StringBuilder();

		st = new StringTokenizer(br.readLine());

		K = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		map = new int[5][5];
		for (int r = 0; r < 5; r++) {
			st = new StringTokenizer(br.readLine());
			for (int c = 0; c < 5; c++) {
				map[r][c] = Integer.parseInt(st.nextToken());
			}
		}

		nextVal = new int[M];
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			nextVal[i] = Integer.parseInt(st.nextToken());
		}

		int score = 0;
		for (int i = 0; i < K; i++) {
			score = 0;
			maxLocation mLoc = searchMaxScore();
			if (mLoc.maxScore == 0) {
				break;
			}
			score += mLoc.maxScore;
			map = routeOriginalMap(mLoc.maxRoute, mLoc.maxRow, mLoc.maxCol);
			clearandInputMap();
			int tempScore = 0;
			while ((tempScore = checkPoints(map)) != 0) {
				score += tempScore;
				clearandInputMap();
			}
			sb.append(score + " ");
		}

		bw.write(sb.toString());
		bw.flush();
	}

	private static void clearandInputMap() {
		boolean[][] visited = new boolean[5][5];
		for (int r = 0; r < 5; r++) {
			for (int c = 0; c < 5; c++) {
				if (visited[r][c]) {
					continue;
				}
				int targetVal = map[r][c];
				Queue<int[]> Q = new LinkedList<int[]>();
				Queue<int[]> saveQ = new LinkedList<int[]>();
				int[] index = new int[2];
				index[0] = r;
				index[1] = c;
				Q.add(index);
				saveQ.add(index);
				visited[r][c] = true;
				int nearBlock = 0;

				while (!Q.isEmpty()) {
					int[] item = Q.poll();

					nearBlock++;

					for (int d = 0; d < 4; d++) {
						int nr = item[0] + dr[d];
						int nc = item[1] + dc[d];

						if (checkMap(nr, nc) && !visited[nr][nc] && map[nr][nc] == targetVal) {
							int[] newIndex = new int[2];
							newIndex[0] = nr;
							newIndex[1] = nc;
							visited[nr][nc] = true;
							Q.add(newIndex);
							saveQ.add(newIndex);
						}

					}

				}
				if (nearBlock >= 3) {
					while (!saveQ.isEmpty()) {
						int[] temp = saveQ.poll();
						map[temp[0]][temp[1]] = 0;
					}
				}

			}
		}

		for (int c = 0; c < 5; c++) {
			for (int r = 4; r >= 0; r--) {
				if (map[r][c] == 0) {
					map[r][c] = nextVal[addIndex++];
				}
			}
		}

	}

	private static maxLocation searchMaxScore() {
		maxLocation mLoc = new maxLocation();
		mLoc.maxScore = 0;
		mLoc.maxRoute = -1;
		mLoc.maxRow = -1;
		mLoc.maxCol = -1;
		for (int route = 1; route < 4; route++) {
			for (int c = 0; c < 3; c++) {
				for (int r = 0; r < 3; r++) {
					int[][] routeMap = routeOriginalMap(route, r, c);

					int tempScore = checkPoints(routeMap);

					if (tempScore > mLoc.maxScore) {

						mLoc.maxScore = tempScore;
						mLoc.maxRoute = route;
						mLoc.maxRow = r;
						mLoc.maxCol = c;
					}

				}
			}
		}
		return mLoc;
	}

	private static int checkPoints(int[][] routeMap) {
		boolean[][] visited = new boolean[5][5];
		int score = 0;
		for (int r = 0; r < 5; r++) {
			for (int c = 0; c < 5; c++) {
				if (visited[r][c]) {
					continue;
				}
				int targetVal = routeMap[r][c];
				Queue<int[]> Q = new LinkedList<int[]>();
				int[] index = new int[2];
				index[0] = r;
				index[1] = c;
				Q.add(index);
				visited[r][c] = true;
				int nearBlock = 0;

				while (!Q.isEmpty()) {
					int[] item = Q.poll();

					nearBlock++;

					for (int d = 0; d < 4; d++) {
						int nr = item[0] + dr[d];
						int nc = item[1] + dc[d];

						if (checkMap(nr, nc) && !visited[nr][nc] && routeMap[nr][nc] == targetVal) {
							int[] newIndex = new int[2];
							newIndex[0] = nr;
							newIndex[1] = nc;
							visited[nr][nc] = true;
							Q.add(newIndex);

						}

					}

				}
				if (nearBlock >= 3) {
					score += nearBlock;
				}

			}
		}

		return score;
	}

	private static boolean checkMap(int nr, int nc) {
		if (nr >= 0 && nr < 5 && nc >= 0 && nc < 5)
			return true;
		return false;
	}

	private static int[][] routeOriginalMap(int route, int r, int c) {
		int[][] copyMap = new int[5][5];
		int[][] tempMap = new int[5][5];
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 5; col++) {
				copyMap[row][col] = map[row][col];
			}
		}
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 5; col++) {
				tempMap[row][col] = map[row][col];
			}
		}
		for (int rTime = 0; rTime < route; rTime++) {
			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					copyMap[r + row][c + col] = tempMap[r + (2 - col)][c + row];
				}
			}

			for (int row = 0; row < 5; row++) {
				for (int col = 0; col < 5; col++) {
					tempMap[row][col] = copyMap[row][col];
				}
			}
		}

		return copyMap;
	}

}
