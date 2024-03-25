import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.util.*;

public class Main {

    static int Q;
    static int N;
    static HashMap<String , Integer> limitTime;
    static HashMap<String, PriorityQueue<Problem>> readyQ;
//    static PriorityQueue<Problem> readyQ;
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
        int count = 0;
        for(String key : readyQ.keySet()){
            count += readyQ.get(key).size();
        }
        sb.append(count + "\n");
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

        if(readySolver.isEmpty()){
            return;
        }
        String targetDomain = "";
        int minRequestTime = 99999999;
        int minPriority = 99999999;
        for(String key : readyQ.keySet()){
            PriorityQueue<Problem> list = readyQ.get(key);

            Problem pitem = list.peek();
//            System.out.println(pitem.url + " : " + limitTime.get(pitem.domain));
            if(pitem != null && limitTime.get(pitem.domain) <= solveTime) {
                if(pitem.priority < minPriority){
                    minPriority = pitem.priority;
                    minRequestTime = pitem.requestTime;
                    targetDomain = pitem.domain;
                }else if(pitem.priority == minPriority && pitem.requestTime < minRequestTime){
                    minPriority = pitem.priority;
                    minRequestTime = pitem.requestTime;
                    targetDomain = pitem.domain;
                }
            }
        }
        if(minRequestTime == 99999999 && minPriority == 99999999){
            return;
        }

        Problem pitem = readyQ.get(targetDomain).poll();
        Solver solver = readySolver.poll();
        solver.solvingP = pitem;
        solver.isPorcessing = true;
        limitTime.put(pitem.domain, 99999999);
        pitem.startTime = solveTime;
    }

    private static void requestProblem(int requestTime, int requestPriority, String requestUrl) {
        Problem pitem = new Problem();
        StringTokenizer st = new StringTokenizer(requestUrl, "/");
        pitem.domain = st.nextToken();
        pitem.pid = Integer.parseInt(st.nextToken());
        pitem.url = requestUrl;
        pitem.requestTime = requestTime;
        pitem.priority = requestPriority;
        if(!readyQ.containsKey(pitem.domain)){
            readyQ.put(pitem.domain,new PriorityQueue<>());
        }
        if(readyQ.get(pitem.domain).contains(pitem)){
            return;
        }
        if(!limitTime.containsKey(pitem.domain)){
            limitTime.put(pitem.domain, 0);
        }

        readyQ.get(pitem.domain).add(pitem);

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
        readyQ = new HashMap<>();
        limitTime = new HashMap<>();

        Problem pitem = new Problem();
        StringTokenizer st = new StringTokenizer(url , "/");
        pitem.url = url;
        pitem.domain = st.nextToken();
        pitem.pid = Integer.parseInt(st.nextToken());
        pitem.priority = 1;
        pitem.requestTime = 0;
        readyQ.put(pitem.domain, new PriorityQueue<Problem>());
        readyQ.get(pitem.domain).add(pitem);
        limitTime.put(pitem.domain, 0);

    }
}