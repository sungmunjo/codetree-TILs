import java.io.*;
import java.util.*;

public class Main {

    static int Q;
    static int N;
    static HashMap<String , Integer> limitTime;
    static PriorityQueue<Problem> readyQ;
    static PriorityQueue<Solver> readySolver;
    static Solver[] solvers;

    static StringBuilder sb;
    static class Problem implements Comparable<Problem>{
        int requestTime;
        String url;
        String domain;
        int pid;
        int priority;
        int startTime;



        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Problem){
                return Objects.equals(this.url, ((Problem) obj).url);
            }
            return false;
        }

        @Override
        public int compareTo(Problem o) {
            return this.priority == o.priority? this.requestTime - o.requestTime :  this.priority - o.priority;
        }
    }

    static class Solver implements Comparable<Solver>{
        int index;
        boolean isPorcessing;
        Problem solvingP;


        @Override
        public int compareTo(Solver o) {
            return this.index - o.index;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        sb = new StringBuilder();
        StringTokenizer st;

        Q = Integer.parseInt(br.readLine());

        for (int q = 0; q < Q; q++) {
            st = new StringTokenizer(br.readLine());
            int order = Integer.parseInt(st.nextToken());

            switch(order){
                case 100:
                    int tempN = Integer.parseInt(st.nextToken());
                    String url = st.nextToken();
                    readySolvers(tempN, url);

                    break;
                case 200:
                    int requestTime = Integer.parseInt(st.nextToken());
                    int requestPriority = Integer.parseInt(st.nextToken());
                    String requestUrl = st.nextToken();
                    requestProblem(requestTime, requestPriority, requestUrl);
                    
                    break;
                case 300:
                    int solveTime = Integer.parseInt(st.nextToken());
                    solveProblem(solveTime);
                    break;
                case 400:
                    int solvedTime = Integer.parseInt(st.nextToken());
                    int solvedIndex = Integer.parseInt(st.nextToken());
                    solvedProblem(solvedTime, solvedIndex);
                    break;
                case 500:
                    int peekTime = Integer.parseInt(st.nextToken());
                    peekProblems(peekTime);
                    break;
                default:
                    break;
            }
        }
        bw.write(sb.toString());
        bw.flush();
    }

    private static void peekProblems(int peekTime) {
        sb.append(readyQ.size() + "\n");
    }

    private static void solvedProblem(int solvedTime, int solvedIndex) {
        Solver solver = solvers[solvedIndex - 1];
        if(!solver.isPorcessing)return;
        Problem pitem = solver.solvingP;

        int timeDiff = solvedTime - pitem.startTime;
        limitTime.put(pitem.domain, (pitem.startTime + 3 * timeDiff));
//        System.out.println(limitTime.get(pitem.domain));
        solver.isPorcessing = false;
        solver.solvingP = null;
        readySolver.add(solver);
    }

    private static void solveProblem(int solveTime) {
        Queue<Problem> saveP = new LinkedList<>();

        while(!readyQ.isEmpty()){
            Problem pitem = readyQ.poll();
            if(limitTime.get(pitem.domain) <= solveTime){
                if(readySolver.isEmpty()){
                    while(!saveP.isEmpty()){
                        readyQ.add(saveP.poll());
                    }
                    return;
                }
                Solver solver = readySolver.poll();
                solver.solvingP = pitem;
                solver.isPorcessing = true;
                limitTime.put(pitem.domain, 99999999);
                pitem.startTime = solveTime;
//                System.out.println(solveTime + " : " + pitem.url);
                break;
            }
            saveP.add(pitem);
        }
        while(!saveP.isEmpty()){
            readyQ.add(saveP.poll());
        }
    }

    private static void requestProblem(int requestTime, int requestPriority, String requestUrl) {
        Problem pitem = new Problem();
        StringTokenizer st = new StringTokenizer(requestUrl, "/");
        pitem.domain = st.nextToken();
        pitem.pid = Integer.parseInt(st.nextToken());
        pitem.url = requestUrl;
        pitem.requestTime = requestTime;
        pitem.priority = requestPriority;

        if(readyQ.contains(pitem)){
            return;
        }
        if(!limitTime.containsKey(pitem.domain)){
            limitTime.put(pitem.domain, 0);
        }

        readyQ.add(pitem);

    }

    private static void readySolvers(int tempN, String url) {
        N = tempN;
        solvers = new Solver[N];
        readySolver = new PriorityQueue<>();
        for(int n=0;n<N;n++){
            solvers[n] = new Solver();
            solvers[n].index = n;
            solvers[n].isPorcessing = false;
            solvers[n].solvingP = null;
            readySolver.add(solvers[n]);
        }
        readyQ = new PriorityQueue<>();
        limitTime = new HashMap<>();

        Problem pitem = new Problem();
        StringTokenizer st = new StringTokenizer(url , "/");
        pitem.url = url;
        pitem.domain = st.nextToken();
        pitem.pid = Integer.parseInt(st.nextToken());
        pitem.priority = 1;
        pitem.requestTime = 0;
        readyQ.add(pitem);
        limitTime.put(pitem.domain, 0);

    }
}