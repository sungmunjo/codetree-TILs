import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    static int[] parents;
    static int[][] tree;
    static int[] power;
    static boolean[] onoff;

    static int N;
    static int Q;


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        for (int q = 0; q < Q; q++) {
            st = new StringTokenizer(br.readLine());
            int order;
            order = Integer.parseInt(st.nextToken());
            switch (order) {
                case 100:
                    int[] sendParents = new int[N];
                    int[] authority = new int[N];
                    for (int i = 0; i < N; i++) {
                        sendParents[i] = Integer.parseInt(st.nextToken());
                    }
                    for (int i = 0; i < N; i++) {
                        authority[i] = Integer.parseInt(st.nextToken());
                    }
                    initArray(sendParents, authority);
                    break;
                case 200:
                    int offIndex = Integer.parseInt(st.nextToken());
                    alarmOff(offIndex);
                    break;
                case 300:
                    int changeIndex = Integer.parseInt(st.nextToken());
                    int changePower = Integer.parseInt(st.nextToken());
                    changePowerValue(changeIndex, changePower);

                    break;
                case 400:
                    int AIndex = Integer.parseInt(st.nextToken());
                    int BIndex = Integer.parseInt(st.nextToken());
                    changeLoc(AIndex, BIndex);
                    break;
                case 500:
                    int findIndex = Integer.parseInt(st.nextToken());
                    int result = findAlarms(findIndex);
                    sb.append(result + "\n");
                    break;
                default:
                    break;

            }

            // for(int i=0;i<20;i++){
            //     System.out.print(tree[1][i] + " ");
            // }
            // System.out.println();

        }
        bw.write(sb.toString());
        bw.flush();

    }

    private static int findAlarms(int findIndex) {
        int count = 0;
        int[] findArray = tree[findIndex];
        for (int i = 0; i < findArray.length; i++) {
            count += findArray[i];
        }
        return count;
    }

    private static void changeLoc(int aIndex, int bIndex) {
        int[] recArray = tree[aIndex];
        int tempIndex = parents[aIndex];
        int tempPower = power[aIndex];
        tempPower = Math.min(tempPower, 21);
        int count = 1;
        if(onoff[aIndex]) {
            while (tempIndex != 0 && onoff[tempIndex]) {
                int[] tempArray = tree[tempIndex];
                for (int i = 0; i < recArray.length - count; i++) {
                    tempArray[i] -= recArray[i + count];
                }
                if (tempPower > 0) {
                    tempPower--;
                    tempArray[tempPower]--;
                }
                tempIndex = parents[tempIndex];
                count++;
            }
        }

        recArray = tree[bIndex];
        tempIndex = parents[bIndex];
        tempPower = power[bIndex];
        tempPower = Math.min(tempPower, 21);
        count = 1;
        if(onoff[bIndex]) {
            while (tempIndex != 0 && onoff[tempIndex]) {
                int[] tempArray = tree[tempIndex];
                for (int i = 0; i < recArray.length - count; i++) {
                    tempArray[i] -= recArray[i + count];
                }
                if (tempPower > 0) {
                    tempPower--;
                    tempArray[tempPower]--;
                }
                tempIndex = parents[tempIndex];
                count++;
            }
        }

        int temp = parents[aIndex];
        parents[aIndex] = parents[bIndex];
        parents[bIndex] = temp;

        recArray = tree[bIndex];
        tempIndex = parents[bIndex];
        tempPower = power[bIndex];
        tempPower = Math.min(tempPower, 21);
        count = 1;
        if(onoff[bIndex]) {
            while (tempIndex != 0 && onoff[tempIndex]) {
                int[] tempArray = tree[tempIndex];
                for (int i = 0; i < recArray.length - count; i++) {
                    tempArray[i] += recArray[i + count];
                }
                if (tempPower > 0) {
                    tempPower--;
                    tempArray[tempPower]++;
                }
                tempIndex = parents[tempIndex];
                count++;
            }
        }

        recArray = tree[aIndex];
        tempIndex = parents[aIndex];
        tempPower = power[aIndex];
        tempPower = Math.min(tempPower, 21);
        count = 1;
        if(onoff[aIndex]){
            while (tempIndex != 0 && onoff[tempIndex]) {
                int[] tempArray = tree[tempIndex];
                for (int i = 0; i < recArray.length - count; i++) {
                    tempArray[i] += recArray[i + count];
                }
                if (tempPower > 0) {
                    tempPower--;
                    tempArray[tempPower]++;
                }
                tempIndex = parents[tempIndex];
                count++;
            }
        }



    }

    private static void changePowerValue(int changeIndex, int changePower) {
        if(!onoff[changeIndex]){
            return;
        }

        int tempIndex = parents[changeIndex];
        int tempPower = power[changeIndex];
        tempPower = Math.min(tempPower, 21);

        while (tempIndex != 0 && tempPower > 0 && onoff[tempIndex]) {
            int[] tempArray = tree[tempIndex];
            tempPower--;
            tempArray[tempPower]--;
            tempIndex = parents[tempIndex];
        }

        tempPower = Math.min(changePower, 21);
        tempIndex = parents[changeIndex];
        while (tempIndex != 0 && tempPower > 0 && onoff[tempIndex]) {
            int[] tempArray = tree[tempIndex];
            tempPower--;
            tempArray[tempPower]++;
            tempIndex = parents[tempIndex];
        }
        power[changeIndex] = changePower;

    }

    private static void alarmOff(int offIndex) {
        int[] recArray = tree[offIndex];
        int tempIndex = parents[offIndex];
        int tempPower = power[offIndex];
        tempPower = Math.min(tempPower, 21);
        int count = 1;
        while (tempIndex != 0 && onoff[tempIndex]) {
            int[] tempArray = tree[tempIndex];
            for (int i = 0; i < recArray.length - count; i++) {
                if (onoff[offIndex]) {
                    tempArray[i] -= recArray[i + count];
                } else {
                    tempArray[i] += recArray[i + count];
                }
            }

            if (tempPower > 0) {
                tempPower--;
                if (onoff[offIndex]) {
                    tempArray[tempPower]--;
                } else {
                    tempArray[tempPower]++;
                }
            }
            tempIndex = parents[tempIndex];
            count++;
        }
        onoff[offIndex] = !onoff[offIndex];

    }

    private static void initArray(int[] sendParents, int[] authority) {
        parents = new int[N + 1];
        power = new int[N + 1];
        tree = new int[N + 1][21];
        onoff = new boolean[N + 1];
        for (int i = 0; i < N; i++) {
            parents[i + 1] = sendParents[i];
            power[i + 1] = authority[i];
        }
        for (int i = 0; i <= N; i++) {
            onoff[i] = true;
        }

        for (int i = 0; i < parents.length; i++) {
            int tempIndex = parents[i];
            int tempPower = power[i];
            tempPower = Math.min(tempPower, 21);
            while (tempIndex != 0 && tempPower != 0) {
                tempPower--;
                tree[tempIndex][tempPower]++;
                tempIndex = parents[tempIndex];
            }
        }


    }
}