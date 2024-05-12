import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class codeTreeAncient {

    static int [] numbers;
    static int numberIndex;
    static int map[][];
    static int K;
    static int M;
    static int [] dr = {0, 1, 0, -1};
    static int [] dc = {1, 0 ,-1, 0};


    public static void main(String[] args) throws Exception {
//        System.out.println("test");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringBuffer sb = new StringBuffer();
        StringTokenizer st;
        numberIndex = 0;
        st = new StringTokenizer(br.readLine());
        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[5][5];

        for(int r=0;r<5;r++){
            st = new StringTokenizer(br.readLine());
            for(int c=0;c<5;c++){
                map[r][c] = Integer.parseInt(st.nextToken());
            }
        }
        numbers = new int[M];
        st = new StringTokenizer(br.readLine());
        for(int m = 0; m< M ;m++){
            numbers[m] = Integer.parseInt(st.nextToken());
        }

        for(int k = 0; k < K; k++){
            int maxScore = -1;
            int[] maxValues = new int [3];
            for(int tempr = 1; tempr <= 3; tempr++){
                for(int tempc = 1; tempc <= 3; tempc++){
                    int [][] copyArray = copyArrayFromRoot(map);
                    for(int turn = 0; turn < 3; turn ++){
                        copyArray = turn90Map(tempr, tempc, copyArray);
                        int tempScore = calculScore(copyArray, false);
                        if(maxScore < tempScore) {
                            maxScore = tempScore;
                            maxValues[0] = tempr;
                            maxValues[1] = tempc;
                            maxValues[2] = turn;
                        }
                    }
                }
            }
            if(maxValues[0] == 0 && maxValues[1] == 0)break;
            System.out.println(maxValues[0] + " : " + maxValues[1] + " : " + maxValues[2] + " : " + maxScore);
            printMap();
            for(int i = 0; i < maxValues[2] + 1; i++){
                map = turn90Map(maxValues[0], maxValues[1], map);
            }
            System.out.println("2");
            printMap();
            int totalScore = 0;
            int tempScore = -1;
            while (tempScore != 0){
                tempScore = calculScore(map, true);
                if(tempScore != -1){
                    totalScore += tempScore;
                }
//                printMap();
                reFill(map);
//                System.out.println("3");
//                printMap();
            }
            sb.append(totalScore + " ");




        }
        bw.write(sb.toString());
        bw.flush();


    }

    private static void printMap() {
        for(int r=0;r<5;r++){
            for(int c=0;c<5;c++){
                System.out.print(map[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void reFill(int[][] map) {
        for(int r = 4;r >=0; r--){
            for(int c = 0; c<5;c++){
                if(map[r][c] == 0){
                    map[r][c] = numbers[numberIndex];
                    numberIndex = (numberIndex+1)%numbers.length;
                }
            }
        }

    }

    private static int calculScore(int[][] copyArray, boolean deleteFlag) {
        boolean [][] visited = new boolean[5][5];
        int score = 0;
        for(int r=0;r<5;r++){
            for(int c=0;c<5;c++){
                if(visited[r][c]){
                    continue;
                }
                Queue<int[]> Q = new LinkedList<>();
                int locVal = map[r][c];
                Q.add(new int [] {r,c});
                vis

            ited[r][c] = true;
                int count = 0;
                while(!Q.isEmpty()){
                    int[] tempLoc = Q.poll();
                    count++;
                    if(deleteFlag){
                        copyArray[tempLoc[0]][tempLoc[1]] = 0;
                    }
                    for(int d=0;d<4;d++){
                        int nr = tempLoc[0] + dr[d];
                        int nc = tempLoc[1] + dc[d];

                        if(checkMap(nr,nc) && !visited[nr][nc] && map[nr][nc] == locVal){
                            visited[nr][nc] = true;
                            Q.add(new int [] {nr,nc});

                        }
                    }

                }
                if(count >= 3){
                    score+= count;
                }

            }
        }

        return score;
    }

    private static boolean checkMap(int nr, int nc) {
        if(nr >= 0 && nr < 5 && nc >= 0 && nc < 5)return true;
        return false;

    }

    private static int[][] turn90Map(int tempr, int tempc,int[][] copyArray) {
        int [][] tempMap = new int [5][5];

        for(int r=0;r<5;r++){
            for(int c=0;c<5;c++){
                tempMap[r][c] = copyArray[r][c];
            }
        }

        for(int r=0;r<3;r++){
            for(int c=0;c<3;c++){
                int tempMapR = tempr + r - 1;
                int tempMapC = tempc + c - 1;
                int replaceR = tempr + 1 - c;
                int replaceC = tempc + r - 1;
                tempMap[tempMapR][tempMapC] = copyArray[replaceR][replaceC];
            }
        }

        return tempMap;


    }

    private static int[][] copyArrayFromRoot(int[][] map) {
        int[][] tempMap = new int [5][5];
        for(int r=0;r<5;r++){
            for(int c=0;c<5;c++){
                tempMap[r][c] = map[r][c];
            }
        }
        return tempMap;


    }
}
