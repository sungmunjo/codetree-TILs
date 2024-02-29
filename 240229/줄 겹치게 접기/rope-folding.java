import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    static int L;
    static int N;
    static int[] numList;
    static class returnVal{
        int startIndex;
        boolean isHelf;
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());

        numList = new int[N];


        for(int i=0;i<N;i++){
            st = new StringTokenizer(br.readLine());
            numList[i] = Integer.parseInt(st.nextToken());

        }
        Arrays.sort(numList);

        int startIndex = 0;
        boolean isHelf = true;
        int count = 0;
        while(startIndex < N - 1){
            int tempIndex = startIndex;
            int tempNextIndex;
            boolean flag;

            int defaultValue;

            if(isHelf){
                tempNextIndex = tempIndex + 1;
                flag = checkVal(tempIndex, tempNextIndex);
            }else{
                tempNextIndex = tempIndex;
                flag = checkVal(tempIndex, tempNextIndex);
            }

            if(flag){
                count++;
            }
            returnVal returnVal = nextVal(startIndex, isHelf);
            startIndex = returnVal.startIndex;
            isHelf = returnVal.isHelf;

        }
        sb.append(count);
        bw.write(sb.toString());
        bw.flush();



    }
    static boolean checkVal(int tempIndex, int tempNextIndex){
        boolean flag = true;
            double defaultValue = (numList[tempNextIndex] + numList[tempIndex])/2.0;
            while(tempNextIndex < N && tempIndex >= 0){
                if((defaultValue - numList[tempIndex]) != (numList[tempNextIndex] - defaultValue)){
                    flag = false;
                }
                tempNextIndex++;
                tempIndex--;
            }

        return flag;
    }


    static returnVal nextVal(int startIndex, boolean isHelf){
        returnVal returnVal = new returnVal();

        if(isHelf){
            returnVal.startIndex = startIndex+1;
            returnVal.isHelf = !isHelf;
        }else{
            returnVal.startIndex = startIndex;
            returnVal.isHelf = !isHelf;
        }
        return returnVal;
    }

}