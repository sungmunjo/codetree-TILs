import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Main {

    static int[] iceArray;

    static ArrayList<Integer> upperCase;
    static ArrayList<Integer> lowerCase;
    static int N;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        upperCase = new ArrayList<>();
        lowerCase = new ArrayList<>();
        N = Integer.parseInt(br.readLine());
        iceArray = new int[N];
        for (int i = 0; i < N; i++) {
            iceArray[i] = Integer.parseInt(br.readLine());

        }
        int upperCaseCount = 0;
        int lowerCaseCount = 0;
        boolean wasHigh = false;
        if (iceArray.length > 1 && iceArray[0] > iceArray[1]) {
            wasHigh = true;
            upperCase.add(iceArray[0]);
        }
        if (iceArray.length > 1 && iceArray[N - 1] >= iceArray[N - 2]) {
            upperCase.add(iceArray[N-1]);
        }

        if (iceArray.length == 1) {
            upperCase.add(iceArray[0] );
        }

        for (int i = 1; i < N - 1; i++) {
            if (wasHigh && iceArray[i] <= iceArray[i - 1] && iceArray[i] < iceArray[i + 1]) {
//                lowerCase.set(lowerCaseCount++, iceArray[i]);
                wasHigh = false;
                lowerCase.add(iceArray[i]);
            }
            if (!wasHigh && iceArray[i] >= iceArray[i - 1] && iceArray[i] > iceArray[i + 1]) {
//                upperCase.set(lowerCaseCount++, iceArray[i]);
                wasHigh = true;
                upperCase.add(iceArray[i]);
            }
        }
        Collections.sort(lowerCase);
        Collections.sort(upperCase);

        int maxValue = -1;

//        System.out.println(upperCase.size());
        for (int i = 0; i < upperCase.size(); i++) {
            int targetNum = upperCase.get(i) - 1;

            int startIndex = 0;
            int endIndex = lowerCase.size() - 1;

            while (startIndex <= endIndex) {
                int middleIndex = (startIndex + endIndex) / 2;

                if (lowerCase.get(middleIndex) <= targetNum) {
                    startIndex = middleIndex + 1;
                } else {
                    endIndex = middleIndex - 1;
                }
            }

            int tempVal = upperCase.size() - i;

            tempVal -= (lowerCase.size() - startIndex);

            if(tempVal > maxValue){
                maxValue = tempVal;

            }

        }
        sb.append(maxValue);

        bw.write(sb.toString());
        bw.flush();
    }
}