import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static class Product implements Comparable<Product> {
		int id;
		int cost;
		int revenue;
		int dest;

		@Override
		public int compareTo(Product o) {
			return (this.revenue - this.cost) == (o.revenue - o.cost) ? (this.id - o.id)
					: ((o.revenue - o.cost) - (this.revenue - this.cost));
		}
	}
	
	static class CompareCity implements Comparable<CompareCity> {
		int cityId;
		int cost;
		@Override
		public int compareTo(CompareCity o) {
			return this.cost - o.cost;
		}
	}
	

	static int[][] costMap;

	static int Q;

	static int N;

	static int M;

	static Queue<Product> products;

	static boolean[] createdFlag;
	static boolean[] deletedFlag;

	static int fromCityId;
	static int costList[];
	
	
	public static void main(String args[]) throws NumberFormatException, IOException {
		
		products = new PriorityQueue();
		createdFlag = new boolean[30001];
		deletedFlag = new boolean[30001];
		
		fromCityId = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

		StringBuffer sb = new StringBuffer();
		StringTokenizer st;

		Q = Integer.parseInt(br.readLine());

		for (int q = 0; q < Q; q++) {
			st = new StringTokenizer(br.readLine());

			int order = Integer.parseInt(st.nextToken());

			switch (order) {
			case 100:
				N = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				makeMap(st);
				break;
			case 200:
				int productId = Integer.parseInt(st.nextToken());
				int revenue = Integer.parseInt(st.nextToken());
				int destId = Integer.parseInt(st.nextToken());
				makeProduct(productId, revenue, destId);
				break;
			case 300:
				int deleteId = Integer.parseInt(st.nextToken());
				deleteProduct(deleteId);
				break;
			case 400:
				int bestProduct = getBestProduct();
				sb.append(bestProduct + "\n");
				break;
			case 500:
				int changeStartId = Integer.parseInt(st.nextToken());
				changeStartPos(changeStartId);
				break;
			default:
				break;
			}

		}
		bw.write(sb.toString());
		bw.flush();

	}
	
	private static void getBestProcuct() {
		boolean[] visited = new boolean[N];
		costList = new int [N + 1];
		for(int i=0;i<N + 1;i++) {
			costList[i] = 987654321;
		}
		costList[fromCityId] = 0;
		CompareCity qItem = new CompareCity();
		qItem.cityId = fromCityId;
		qItem.cost = 0;
		PriorityQueue<CompareCity> pq = new PriorityQueue<>();
		pq.add(qItem);
		while(!pq.isEmpty()) {
			CompareCity pollItem = pq.poll();
			
			
			if(pollItem.cost > costList[pollItem.cityId]) {
				continue;
			}
			visited[pollItem.cityId] = true;
			
			for(int i=0;i<N;i++) {
				if(costMap[pollItem.cityId][i] != 987654321) {
					int fromCity = pollItem.cityId;
					int toCity = i;
					int cost = costMap[pollItem.cityId][i];
					if(!visited[toCity] && (cost + costList[fromCity] < costList[toCity])) {
						costList[toCity] = cost + costList[fromCity];
						CompareCity newItem = new CompareCity();
						newItem.cityId = toCity;
						newItem.cost = costList[toCity];
						
						pq.add(newItem);
					}
				}
			}
			
			
		}
	}

	private static void changeStartPos(int changeStartId) {
		fromCityId = changeStartId;
		getBestProcuct();
		Queue<Product> newQ = new PriorityQueue<>();
		
		while(!products.isEmpty()) {
			Product item = products.poll();
			
			Product newI = new Product();
			newI.id = item.id;
			newI.dest = item.dest;
			newI.cost = costList[item.dest];
			newI.revenue = item.revenue;
			
			newQ.add(newI);
		}
		products = newQ;
	}

	private static int getBestProduct() {
		int bestId = -1;
		Queue<Product> tempQ = new LinkedList<>();
		while(bestId == -1) {
			if(products.size() == 0)break;
			
			Product item = products.peek();
			
			if(!deletedFlag[item.id] && (item.revenue - item.cost) >= 0) {
				item = products.poll();
				bestId = item.id;
			}else if(deletedFlag[item.id]) {
				products.poll();
			}else {
				tempQ.add(products.poll());
			}
		}
		while(!tempQ.isEmpty()) {
			products.add(tempQ.poll());
		}
		
		return bestId;
	}

	private static void deleteProduct(int deleteId) {
		if(createdFlag[deleteId]) {
			deletedFlag[deleteId] = true;
		}
	}

	private static void makeProduct(int productId, int revenue, int destId) {
		
		
		Product newP = new Product();
		newP.id = productId;
		newP.dest = destId;
		newP.revenue = revenue;
		newP.cost = costList[destId];
		
		createdFlag[newP.id] = true;
		products.add(newP);
	}

	private static void makeMap(StringTokenizer st) {
		costMap = new int[N][N];
		costList = new int [N + 1];
		
		
		for(int r=0;r<N;r++) {
			for(int c=0;c<N;c++) {
				costMap[r][c] = 987654321;
			}
		}
		
		for (int m = 0; m < M; m++) {
			int fromCity = Integer.parseInt(st.nextToken());
			int toCity = Integer.parseInt(st.nextToken());
			int cost = Integer.parseInt(st.nextToken());
			
			
			if(costMap[fromCity][toCity] > cost) {
				costMap[fromCity][toCity] = cost;
			}
			if(costMap[toCity][fromCity] > cost) {
				costMap[toCity][fromCity] = cost;
			}
			
		}
		getBestProcuct();
	}
}