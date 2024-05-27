import java.io.*;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
    static int N;
    static int M;

    static int minLinkVal[][];
    static int minLength[];
    static int startIndex;
    static boolean deleteFlag[];
    static class Product implements Comparable<Product> {
        public int id;
        public int price;
        public int dest;
        public int dist;



        @Override
        public int compareTo(Product o) {
            int benifit = this.price - this.dist;
            int otherBenifit = o.price - o.dist;
            if(benifit == otherBenifit){
                return this.id - o.id;
            }
            return otherBenifit - benifit;

        }
    }

    static PriorityQueue<Product> productList;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        sb = new StringBuilder();

        StringTokenizer st;

        int orders = Integer.parseInt(br.readLine());

        for (int o = 0; o < orders; o++) {
            st = new StringTokenizer(br.readLine());
            int order = Integer.parseInt(st.nextToken());

            switch (order) {
                case 100:
                    int n = Integer.parseInt(st.nextToken());
                    int m = Integer.parseInt(st.nextToken());
                    int[] linkArray = new int[(3 * m)];
                    for (int i = 0; i < 3 * m; i++) {
                        linkArray[i] = Integer.parseInt(st.nextToken());
                    }
                    initMap(n, m, linkArray);
                    dijk();
                    break;
                case 200:
                    int id = Integer.parseInt(st.nextToken());
                    int price = Integer.parseInt(st.nextToken());
                    int dest = Integer.parseInt(st.nextToken());
                    createProduct(id, price, dest);
                    break;
                case 300:
                    int delId = Integer.parseInt(st.nextToken());
                    deleteProduct(delId);
                    break;
                case 400:
                    sellProduct();
                    break;
                case 500:
                    int newStartIndex = Integer.parseInt(st.nextToken());
                    changeStart(newStartIndex);
                    break;
                default:
            }
        }
        bw.write(sb.toString());
        bw.flush();


    }

    private static void changeStart(int newStartIndex) {
        startIndex = newStartIndex;
        dijk();
        PriorityQueue<Product> changeQ = new PriorityQueue<>();
        while(!productList.isEmpty()){
            Product tempP = productList.poll();
            tempP.dist = minLength[tempP.dest];
            changeQ.add(tempP);
        }
        productList = changeQ;
    }

    private static void sellProduct() {
        Product tempP = productList.poll();
        while(deleteFlag[tempP.id - 1]){
            tempP = productList.poll();
        }
        if(tempP.price - tempP.dist < 0){
            productList.add(tempP);
            sb.append("-1\n");
            return;
        }

        sb.append(tempP.id + "\n");
        return;
    }

    private static void deleteProduct(int delId) {
        deleteFlag[delId - 1] = true;
    }

    private static void createProduct(int id, int price, int dest) {
        Product p = new Product();
        p.id = id;
        p.dist = minLength[dest];
        p.dest = dest;
        p.price = price;
        productList.add(p);
    }

    private static void dijk() {
        boolean visited[] = new boolean[N];
        for (int r = 0; r < N; r++) {
            minLength[r] = 987654321;
        }
        minLength[startIndex] = 0;

        for (int n = 0; n < N; n++) {
            int minValIndex = startIndex;
            int actualMinVal = 987654321;

            for (int v = 0; v < N; v++) {
                if (!visited[v] && minLength[v] < actualMinVal) {
                    actualMinVal = minLength[v];
                    minValIndex = v;
                }
            }
            visited[minValIndex] = true;
            for (int v = 0; v < N; v++) {
                if(!visited[v] && minLength[v] > actualMinVal + minLinkVal[minValIndex][v]){
                    minLength[v] = actualMinVal + minLinkVal[minValIndex][v];
                }
            }


        }

    }

    private static void initMap(int n, int m, int[] linkArray) {
        N = n;
        M = m;
        minLinkVal = new int[N][N];
        minLength = new int[N];
        startIndex = 0;
        productList = new PriorityQueue<>();
        deleteFlag = new boolean[N];
        for (int r = 0; r < N; r++) {

            for (int c = 0; c < N; c++) {
                minLinkVal[r][c] = 987654321;
            }
        }

        for (int i = 0; i < M; i++) {
            int start = linkArray[(3 * i)];
            int end = linkArray[(3 * i + 1)];
            int value = linkArray[(3 * i + 2)];
            if (value < minLinkVal[start][end]) {
                minLinkVal[start][end] = value;
                minLinkVal[end][start] = value;
            }
        }


    }
}