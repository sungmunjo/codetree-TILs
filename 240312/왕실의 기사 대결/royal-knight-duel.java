import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int L, N, Q;

    static int dr[] = {-1, 0, 1, 0};
    static int dc[] = {0, 1, 0, -1};
    static int[][] map;

    static int[][] Kmap;
    static Knight[] knights;

    static class Knight {
        int r;
        int c;
        boolean dead;
        int life;

        int width;
        int height;

    }
    static int [] damage;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(br.readLine());

        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        map = new int[L][L];
        Kmap = new int[L][L];
        for (int r = 0; r < L; r++) {
            st = new StringTokenizer(br.readLine());
            for (int c = 0; c < L; c++) {
                map[r][c] = Integer.parseInt(st.nextToken());
            }
        }

        knights = new Knight[N + 1];
        damage = new int [N + 1];
        for (int i = 1; i <= N; i++) {
            Knight item = new Knight();
            st = new StringTokenizer(br.readLine());

            item.dead = false;
            item.r = Integer.parseInt(st.nextToken()) - 1;
            item.c = Integer.parseInt(st.nextToken()) - 1;
            item.height = Integer.parseInt(st.nextToken());
            item.width = Integer.parseInt(st.nextToken());
            item.life = Integer.parseInt(st.nextToken());

            for (int r = 0; r < item.height; r++) {
                for (int c = 0; c < item.width; c++) {
                    Kmap[item.r + r][item.c + c] = i;
                }
            }
            knights[i] = item;
        }

//        for(int r=0;r<L;r++){
//            for(int c=0;c<L;c++){
//                System.out.print(map[r][c] + " ");
//            }
//            System.out.println();
//        }
//        for(int r=0;r<L;r++){
//            for(int c=0;c<L;c++){
//                System.out.print(Kmap[r][c] + " ");
//            }
//            System.out.println();
//        }

        for (int order = 0; order < Q; order++) {
            Queue<Integer> Q = new LinkedList<>();
            Queue<Integer> saveQ = new LinkedList<>();
            boolean[] visited = new boolean[N + 1];
            st = new StringTokenizer(br.readLine());

            int startK = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            visited[startK] = true;
            Q.add(startK);
            saveQ.add(startK);
            boolean flag = true;
            while (!Q.isEmpty()) {
                int kINdex = Q.poll();

                Knight tempK = knights[kINdex];

                for (int r = 0; r < tempK.height; r++) {
                    for (int c = 0; c < tempK.width; c++) {
                        int nr = tempK.r + r;
                        int nc = tempK.c + c;

                        int mover = nr + dr[dir];

                        int tempc = nc + dc[dir];

                        if (!checkMap(mover, tempc) || map[mover][tempc] == 2) {
                            flag = false;
                            break;
                        }
                        if (Kmap[mover][tempc] != 0 && !visited[Kmap[mover][tempc]] && !knights[Kmap[mover][tempc]].dead) {
//                            System.out.println(order + " : " + Kmap[mover][tempc]);
                            Q.add(Kmap[mover][tempc]);
                            saveQ.add(Kmap[mover][tempc]);
                            visited[Kmap[mover][tempc]] = true;
                        }

                    }
                    if (!flag) {
                        break;
                    }

                }
                if (!flag) {
                    break;
                }
            }
            if (!flag) {
                continue;
            }
            int startIndex = saveQ.poll();
            Knight startKi = knights[startIndex];
            for (int r = 0; r < startKi.height; r++) {
                for (int c = 0; c < startKi.width; c++) {
                    Kmap[startKi.r + r][startKi.c + c] = 0;
                }
            }
            for (int r = 0; r < startKi.height; r++) {
                for (int c = 0; c < startKi.width; c++) {
                    int nr = startKi.r + r + dr[dir];
                    int nc = startKi.c + c + dc[dir];
                    Kmap[nr][nc] = startIndex;
                }
            }
            startKi.r += dr[dir];
            startKi.c +=dc[dir];

            while (!saveQ.isEmpty()) {
                int kIndex = saveQ.poll();
//                System.out.println(order + " : " + kIndex);
                Knight tempK = knights[kIndex];
                for (int r = 0; r < tempK.height; r++) {
                    for (int c = 0; c < tempK.width; c++) {
                        if(Kmap[tempK.r + r][tempK.c + c] == kIndex) {
                            Kmap[tempK.r + r][tempK.c + c] = 0;
                        }
                    }
                }
                for (int r = 0; r < tempK.height; r++) {
                    for (int c = 0; c < tempK.width; c++) {
                        int nr = tempK.r + r + dr[dir];
                        int nc = tempK.c + c + dc[dir];
                        Kmap[nr][nc] = kIndex;
                        if(map[nr][nc] == 1){
                            tempK.life--;
                            damage[kIndex]++;
                        }
                        
                        if(tempK.life <= 0){
                            tempK.dead = true;
                        }
                    }
                }
                tempK.r += dr[dir];
                tempK.c += dc[dir];

            }
        }
        int count = 0;
        for(int n=1;n<N+1;n++){
            if(!knights[n].dead){
                count += damage[n];
            }
        }
        sb.append(count);
        bw.write(sb.toString());
        bw.flush();


    }

    public static boolean checkMap(int nr, int nc) {
        if (nr >= 0 && nr < L && nc >= 0 && nc < L) return true;
        return false;
    }
}