import java.io.*;
import java.util.*;

public class Main {
    static int R;
    static int C;
    static int K;
    static int[][] map;

    static int dr[] = {-1, 0, 1, 0};
    static int dc[] = {0, 1, 0, -1};

    static int currentRobotR;
    static int currentRobotC;
    static int currentRobotDir;
    static int currentSeqNum;

    static int[][] bottomEmpty = {{1, -1}, {2, 0}, {1, 1}};
    static int[][] leftTurnEmpty = {{-1, -1}, {0, -2}, {1, -1}, {1, -2}, {2, -1}};
    static int[][] rightTrunEmpty = {{-1, 1}, {0, 2}, {1, 1}, {1, 2}, {2, 1}};
    static HashMap<Integer, Integer> maxValPerRobot;
    static HashMap<Integer, ArrayList<Integer>> rebotLink;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        rebotLink = new HashMap<>();
        maxValPerRobot = new HashMap<>();
        st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        // 북으로 2행위부터 시작 (-2)
        map = new int[R + 3][C];
        int score = 0;
        for (int k = 1; k <= K; k++) {
            st = new StringTokenizer(br.readLine());
            int col = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());

            currentRobotR = 1;
            currentRobotC = col - 1;
            currentRobotDir = dir;
            currentSeqNum = k;
            // 끝에 도달함 여부
            boolean findMaxFlag = false;

            // 출구 찾아서 나갈 때 까지 반복
            while (!findMaxFlag) {
                // 남쪽 탐색 후 내려감
                findMaxFlag = findBottom();
                if (!findMaxFlag) {
                    continue;
                }
                // 서쪽 탐색 후 내려감
                findMaxFlag = findLeft();
                if (!findMaxFlag) {
                    continue;
                }
                // 동쪽 탐색 후 내려감
                findMaxFlag = findRight();
                if (!findMaxFlag) {
                    continue;
                }
                // 다 못했다면 골렘이 밖에 있는지 확인 -> 맵 초기화 하고, 다시 처음 부터
                if (!checkInside()) {
//                    System.out.println("???");
                    initSetting();
                    break;
                }

                // 다 못했다면 출구 찾아서 나감.


                int[] outLoc = colorMap();
//                printMap();
                linkToRobot(outLoc);
                linkFromRobot();
                score += findMaxRow();
//                System.out.println(score);


            }

        }

        sb.append(score);
        bw.write(sb.toString());
        bw.flush();


    }
    private static void printMap(){
        for(int r=0;r<R + 3;r++){
            for (int c=0;c<C;c++){
                System.out.print(map[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();

    }
    private static int findMaxRow() {
        boolean[] visitedList = new boolean[K + 1];
        int maxVal = 0;
        Queue<Integer> Q = new LinkedList<>();
        visitedList[currentSeqNum] = true;
        Q.add(currentSeqNum);
        while (!Q.isEmpty()) {
            int tempSeqNum = Q.poll();
            int tempMaxVal = maxValPerRobot.get(tempSeqNum);
            if (tempMaxVal > maxVal) {
                maxVal = tempMaxVal;
            }
            ArrayList<Integer> tempList = rebotLink.get(tempSeqNum);
            for (int item : tempList) {
//                System.out.println(item);
                if (!visitedList[item]) {
                    Q.add(item);
                    visitedList[item] = true;
                }
            }
        }
        return maxVal;

    }

    private static void linkFromRobot() {
        for (int d = 0; d < 4; d++) {
            int tempr = currentRobotR + dr[d];
            int tempc = currentRobotC + dc[d];

            for (int tempd = 0; tempd < 4; tempd++) {
                int nr = tempr + dr[tempd];
                int nc = tempc + dc[tempd];

                if (checkMap(nr, nc) && map[nr][nc] > 1000) {
                    ArrayList<Integer> tempList = rebotLink.get(map[nr][nc] - 1000);

                    if (!tempList.contains(currentSeqNum)) {
                        tempList.add(currentSeqNum);
                    }
                }
            }

        }
    }

    private static void linkToRobot(int[] outLoc) {
        ArrayList<Integer> linkList = new ArrayList<>();
        int tempr = outLoc[0];
        int tempc = outLoc[1];
        linkList.add(currentSeqNum);

        for (int d = 0; d < 4; d++) {
            int nr = tempr + dr[d];
            int nc = tempc + dc[d];
            if (checkMap(nr, nc) && currentSeqNum != map[nr][nc] && map[nr][nc] != 0) {
                int realMapSeqNum = (map[nr][nc] > 1000 ? map[nr][nc] - 1000 : map[nr][nc]);
                if (!linkList.contains(realMapSeqNum)) {
                    linkList.add(realMapSeqNum);
                }
            }
        }
        rebotLink.put(currentSeqNum, linkList);
        // from -> to 저장 가능
        // to -> from 은?
        // 직전에 맞닫는 애들만 찾아주기
        // 1. 내 출구에 닿는 애들은 내가 from 상대가 to
        // 2. 내 주변 탐색했는데, 다른 로봇의 출구가 있는 경우, 내가 to 상대가 from
    }

    private static int[] colorMap() {
        int[] currentOutLoc = new int[2];
        map[currentRobotR][currentRobotC] = currentSeqNum;
        for (int d = 0; d < 4; d++) {
            int tempr = currentRobotR + dr[d];
            int tempc = currentRobotC + dc[d];
            if (d == currentRobotDir) {
                currentOutLoc[0] = tempr;
                currentOutLoc[1] = tempc;
                map[tempr][tempc] = currentSeqNum + 1000;
            } else {
                map[tempr][tempc] = currentSeqNum;
            }
        }
        maxValPerRobot.put(currentSeqNum, currentRobotR - 1);
        return currentOutLoc;
    }

    private static boolean checkInside() {
        for (int d = 0; d < 4; d++) {
            int tempr = currentRobotR + dr[d];
            int tempc = currentRobotC + dc[d];

            if (tempr < 3 || tempr >= R + 3 || tempc < 0 || tempc >= C) return false;
        }
        return true;
    }

    private static void initSetting() {
        map = new int[R + 3][C];
        rebotLink = new HashMap<>();
    }

    private static boolean findRight() {
        int tempr = currentRobotR;
        int tempc = currentRobotC;
        boolean availableFlag = true;
        for (int i = 0; i < rightTrunEmpty.length; i++) {
            int colculR = tempr + rightTrunEmpty[i][0];
            int colculC = tempc + rightTrunEmpty[i][1];
            if (!checkMap(colculR, colculC) || map[colculR][colculC] != 0) {
                availableFlag = false;
                break;
            }
        }
        if (availableFlag) {
            currentRobotR++;
            currentRobotC++;
            currentRobotDir = (currentRobotDir + 1) % 4;
            return false;
        }
        return true;
    }

    private static boolean findLeft() {
        int tempr = currentRobotR;
        int tempc = currentRobotC;
        boolean availableFlag = true;
        for (int i = 0; i < leftTurnEmpty.length; i++) {
            int colculR = tempr + leftTurnEmpty[i][0];
            int colculC = tempc + leftTurnEmpty[i][1];
            if (!checkMap(colculR, colculC) || map[colculR][colculC] != 0) {
                availableFlag = false;
                break;
            }
        }
        if (availableFlag) {
            currentRobotR++;
            currentRobotC--;
            currentRobotDir = (currentRobotDir + 3) % 4;
            return false;
        }
        return true;
    }

    private static boolean findBottom() {
        int tempr = currentRobotR;
        int tempc = currentRobotC;
        boolean availableFlag = true;
        for (int i = 0; i < bottomEmpty.length; i++) {
            int colculR = tempr + bottomEmpty[i][0];
            int colculC = tempc + bottomEmpty[i][1];
            if (!checkMap(colculR, colculC) || map[colculR][colculC] != 0) {
                availableFlag = false;
                break;
            }
        }
        if (availableFlag) {
            currentRobotR++;
            return false;
        }
        return true;
    }

    private static boolean checkMap(int nr, int nc) {
        if (nr >= 0 && nr < R + 3 && nc >= 0 && nc < C) return true;
        return false;
    }
}