import java.io.*;
import java.util.StringTokenizer;

public class Main {
    static int N;
    static int M;
    static int P;
    static int C;
    static int D;
    static int[][] map;
    static int[] scores;
    static Santa[] santas;
    static int dr[] = {-1, 0, 1, 0};
    static int dc[] = {0, 1, 0, -1};

    static int ddr[] = {-1, -1, -1, 0, 1, 1, 1, 0};
    static int ddc[] = {-1, 0, 1, 1, 1, 0, -1, -1};

    static int deerR;
    static int deerC;
    static int remainSanta;

    static class Santa {
        int index;
        boolean stund;
        boolean died;
        int stunTime;
        int rloc;
        int cloc;
        int score;

    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());

        map = new int[N + 1][N + 1];
        santas = new Santa[P + 1];
        st = new StringTokenizer(br.readLine());
        int tempr = Integer.parseInt(st.nextToken());
        int tempc = Integer.parseInt(st.nextToken());
        deerR = tempr;
        deerC = tempc;
        scores = new int[P + 1];
        remainSanta = P;
        map[tempr][tempc] = -1;
        for (int p = 0; p < P; p++) {
            st = new StringTokenizer(br.readLine());
            int santaIndex = Integer.parseInt(st.nextToken());
            tempr = Integer.parseInt(st.nextToken());
            tempc = Integer.parseInt(st.nextToken());

            Santa sitem = new Santa();
            sitem.rloc = tempr;
            sitem.cloc = tempc;
            sitem.stund = false;
            sitem.stunTime = 0;
            sitem.score = 0;
            sitem.index = santaIndex;
            sitem.died = false;
            map[tempr][tempc] = santaIndex;

            santas[santaIndex] = sitem;

        }
        for (int k = 0; k < M; k++) {

            spendTurn();
            if(remainSanta == 0)break;
        }

        for(int p=1;p<=P;p++){
            sb.append(santas[p].score + " ");
        }
        bw.write(sb.toString());
        bw.flush();
    }

    private static void spendTurn() {
        moveDeer();
        if(remainSanta == 0)return;

        moveSanta();
        if(remainSanta == 0)return;

        getPoints();

    }

    private static void getPoints() {
        for (int p = 1; p <= P; p++) {
            Santa getSanta = santas[p];
            if(getSanta.died)continue;;

            getSanta.score++;
            scores[p]++;
            if(getSanta.stund){
                getSanta.stunTime--;
                if(getSanta.stunTime == 0){
                    getSanta.stund = false;
                }
            }

        }
    }

    private static void moveSanta() {
        for (int p = 1; p <= P; p++) {
            Santa moveSanta = santas[p];
            if (moveSanta.stund || moveSanta.died) {
                continue;
            }
            int moveR = -1;
            int moveC = -1;
            int minDis = 99999999;
            int currentDis = 0;
            int tempr = moveSanta.rloc;
            int tempc = moveSanta.cloc;
            currentDis += (int) Math.pow((tempr - deerR), 2);
            currentDis += (int) Math.pow((tempc - deerC), 2);
            int moveDir = -1;
            for (int d = 0; d < 4; d++) {
                int nr = tempr + dr[d];
                int nc = tempc + dc[d];

                if (!checkMap(nr, nc) || (map[nr][nc] > 0 && map[nr][nc] != moveSanta.index )) continue;

                int calDis = 0;
                calDis += (int) Math.pow((nr - deerR), 2);
                calDis += (int) Math.pow((nc - deerC), 2);

                if (calDis < minDis && calDis < currentDis) {
                    minDis = calDis;
                    moveDir = d;

                }
            }
            if (moveDir == -1) continue;

            moveR = tempr + dr[moveDir];
            moveC = tempc + dc[moveDir];
            moveSanta.rloc = moveR;
            moveSanta.cloc = moveC;
            map[tempr][tempc] = 0;
            if (map[moveR][moveC] == -1) {
                moveDir = (moveDir + 2) % 4;
                pushSanta(moveSanta, D, true, moveDir, false);
            }else{
                map[moveR][moveC] = moveSanta.index;
            }



        }

    }

    private static void moveDeer() {
        int tempDeerR = deerR;
        int tempDeerC = deerC;

        int minDistance = 9999999;
        int saveR = -1;
        int saveC = -1;
        int targetIndex = -1;
        for (int p = 1; p <= P; p++) {
            Santa tempSanta = santas[p];
            if (tempSanta.died) {
                continue;
            }
            int calDis = 0;
            calDis += (int) Math.pow((tempSanta.rloc - tempDeerR), 2);
            calDis += (int) Math.pow((tempSanta.cloc - tempDeerC), 2);

            if (calDis < minDistance) {
                minDistance = calDis;
                saveR = tempSanta.rloc;
                saveC = tempSanta.cloc;
                targetIndex = p;
            } else if (calDis == minDistance) {
                if ((saveR < tempSanta.rloc) || (saveR == tempSanta.rloc && saveC < tempSanta.cloc)) {
                    minDistance = calDis;
                    saveR = tempSanta.rloc;
                    saveC = tempSanta.cloc;
                    targetIndex = p;
                }
            }
        }
        Santa targetSanta = santas[targetIndex];
        int moveR = -1;
        int moveC = -1;
        int pushDir = -1;
        minDistance = 999999999;
        for (int i = 0; i < 8; i++) {
            int dtempr = tempDeerR + ddr[i];
            int dtempc = tempDeerC + ddc[i];

            int calDis = 0;
            calDis += Math.pow((dtempr - targetSanta.rloc), 2);
            calDis += Math.pow((dtempc - targetSanta.cloc), 2);

            if (calDis < minDistance) {
                minDistance = calDis;
                moveR = dtempr;
                moveC = dtempc;
                pushDir = i;
            }
        }
        map[deerR][deerC] = 0;
        if (map[moveR][moveC] != 0) {
            pushSanta(targetSanta, C, true, pushDir, true);
        }

        deerR = moveR;
        deerC = moveC;
        map[deerR][deerC] = -1;
    }

    private static void pushSanta(Santa targetSanta, int c, boolean getScore, int pushDir, boolean pushByDeer) {
        if (getScore) {
            targetSanta.score += c;
            scores[targetSanta.index] += c;

        }
        int moveR = targetSanta.rloc;
        if (pushByDeer) {
            moveR += ddr[pushDir] * c;
        } else {
            moveR += dr[pushDir] * c;
        }
        int moveC = targetSanta.cloc;
        if (pushByDeer) {
            moveC += ddc[pushDir] * c;
        } else {
            moveC += dc[pushDir] * c;
        }

        if (!checkMap(moveR, moveC)) {
            targetSanta.died = true;
            remainSanta--;
            return;
        }

        if (map[moveR][moveC] != 0) {
            Santa hitSanta = santas[map[moveR][moveC]];
            pushSanta(hitSanta, 1, false, pushDir, pushByDeer);
        }

        map[moveR][moveC] = targetSanta.index;
        targetSanta.rloc = moveR;
        targetSanta.cloc = moveC;

        if (getScore) {
            targetSanta.stund = true;
            targetSanta.stunTime = 2;
        }

    }

    private static boolean checkMap(int nr, int nc) {
        if (nr >= 1 && nr < N + 1 && nc >= 1 && nc < N + 1) return true;
        return false;
    }
}