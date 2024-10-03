import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
	static class Node {
		int updateTime;
		int createTime;
		int colorChangeTime;
		int color;
		int id;
		int pid;
		int maxDepth;
		int score;
		boolean[] childColor;
		
		HashMap<Integer, boolean []> childColors; 
		
	}
	
	static HashMap<Integer, Node> nodes;
	
	static int orderIndex = 0;
	
	static List<Node> mainNodes;
	
	public static void main(String args[]) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		StringBuffer sb = new StringBuffer();
		nodes = new HashMap<>();
		mainNodes = new ArrayList<>();
		int orderCount = Integer.parseInt(br.readLine());
		
		for(int i = 0;i < orderCount; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			
			int order = Integer.parseInt(st.nextToken());
			
			switch (order) {
			case 100: 
				int nodeId = Integer.parseInt(st.nextToken());
				int pId = Integer.parseInt(st.nextToken());
				int color = Integer.parseInt(st.nextToken());
				int maxDepth = Integer.parseInt(st.nextToken());
				addNode(nodeId, pId, color, maxDepth);
				break;
			case 200:
				int targetId = Integer.parseInt(st.nextToken());
				int changeColor = Integer.parseInt(st.nextToken());
				changeColor(targetId, changeColor);
				break;
			case 300:
				int searchId = Integer.parseInt(st.nextToken());
				int resultColor = searchColor(searchId);
				sb.append(resultColor + "\n");
				break;
			case 400:
				int score = searchScore();
				sb.append(score + "\n");
				break;
			default:
			}
			orderIndex++;
		}
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}

	private static int searchScore() {
		int score = 0;
		int returnValue = 0;
		for(Integer key : nodes.keySet()) {
			Node item = nodes.get(key);
			int tempId = item.id;
			score = item.score;
			while(tempId != -1) {
				Node targetNode = nodes.get(tempId);
				if(targetNode.colorChangeTime > item.updateTime) {
					score = 1;
					break;
				}
				tempId = targetNode.pid;
			}
//			System.out.println(key + " : " + tempId + " : " +  (score * score));
			returnValue += (score * score);
			
		}
//		System.out.println();
		
		
		for(int  i = 0; i < mainNodes.size();i++) {
			int tempscore = mainNodes.get(i).score * mainNodes.get(i).score;
			score +=  tempscore;
		}
		return returnValue;
	}

	private static int searchColor(int searchId) {
		Node targetNode = nodes.get(searchId);
		int currentColor = targetNode.color;
		int currentIndex = targetNode.colorChangeTime;
		int curretId = targetNode.id;
		while(curretId != -1) {
			targetNode = nodes.get(curretId);
			
			if(targetNode.colorChangeTime > currentIndex) {
				currentColor = targetNode.color;
				currentIndex = targetNode.colorChangeTime;
			}
			curretId = targetNode.pid;
		}
		return currentColor;
	}

	private static void changeColor(int targetId, int changeColor) {
		Node targetNode = nodes.get(targetId);
		
		targetNode.childColor = new boolean[6];
		targetNode.childColor[changeColor] = true;
		targetNode.color = changeColor;
		targetNode.updateTime = orderIndex;
		targetNode.colorChangeTime = orderIndex;
		targetNode.score = 1;
		
		boolean [] tempColor = new boolean [6];
		tempColor[changeColor] = true;
		
		for(Integer key : targetNode.childColors.keySet()) {
			targetNode.childColors.put(key, tempColor.clone());
		}
		int nodeIndex = targetNode.pid;
		int lastIndex = targetId;
		while(nodeIndex != -1) {
			Node tempNode = nodes.get(nodeIndex);
			tempColor[tempNode.color] = true;
			boolean [] insertColorList = tempColor.clone();
			tempNode.childColors.put(lastIndex, insertColorList);
			int score = 0;
			boolean[] insertColor = new boolean[6];
			insertColor[tempNode.color] = true;
			for(Integer key : tempNode.childColors.keySet()) {
				boolean [] tempboolList = tempNode.childColors.get(key);
				
				for(int i = 0; i < 6; i++) {
//					if(targetId == 4 && changeColor == 4) {
//						System.out.print(tempNode.id + " : " + tempboolList[i] + " ");
//					}
					if(tempboolList[i]) {
						insertColor[i] = true;
						tempColor[i] = true;
					}
				}
//				if(targetId == 4 && changeColor == 4) {
//					System.out.println();
//				}
			}
			
			for(int i = 0; i < 6; i++) {
				if(insertColor[i]) {
					score++;
				}
			}
			
			
//			if(targetId == 4 && changeColor == 4) {
//				System.out.print(tempNode.id + " : " + score + " ");
//			}
			
			tempNode.childColor = insertColor;
			tempNode.score = score;
			tempNode.updateTime = orderIndex;
			
			lastIndex = nodeIndex;
			nodeIndex = tempNode.pid;
		}
	}

	private static void addNode(int nodeId, int pId, int color, int maxDepth) {
		Node item = new Node();
		item.id = nodeId;
		item.pid = pId;
		item.color = color;
		item.score = 1;
		item.childColor = new boolean[6];
		item.childColor[color] = true;
		item.maxDepth = maxDepth;
		item.createTime = orderIndex;
		item.updateTime = orderIndex;
		item.colorChangeTime = orderIndex;
		item.childColors = new HashMap<>();
		boolean [] tempColor = new boolean [6];
		tempColor[color] = true;
		if(pId == -1) {
			mainNodes.add(item);
			nodes.put(nodeId, item);
		}else {
			boolean available = true;
			int depth = 1;
			int tempPid = pId;
			while(tempPid != -1) {
				Node tempNode = nodes.get(tempPid);

				
				if(tempNode.maxDepth <= depth) {
					available = false;
				}
				
				
				tempPid = tempNode.pid;
				depth++;
			}
			if(available) {
				int lastId = nodeId;
				tempPid = pId;
				while(tempPid != -1) {
					boolean[] insertColor = new boolean[6];
					boolean[] insertChildColor = new boolean[6];
					
					for(int i = 0; i < 6; i++) {
						if(tempColor[i]) {
							insertChildColor[i] = true;
						}
					}
					int score = 0;
					Node tempNode = nodes.get(tempPid);
					insertColor[tempNode.color] = true;
					tempColor[tempNode.color] = true;
					if(tempNode.childColors.get(lastId) == null) {
						tempNode.childColors.put(lastId, insertChildColor);
					}else {
						tempNode.childColors.put(lastId, insertChildColor);
					}
					
					for(Integer key : tempNode.childColors.keySet()) {
						boolean [] tempboolList = tempNode.childColors.get(key);
						
						for(int i = 0; i <6; i++) {
							if(tempboolList[i]) {
								insertColor[i] = true;
								tempColor[i] = true;
							}
						}
					}
					
					for(int i = 0; i < 6; i++) {
						if(insertColor[i]) {
							score++;
						}
					}
//					System.out.println(tempNode.id + " : " + score);
					tempNode.childColor = insertColor;
					tempNode.score = score;
					tempNode.updateTime = orderIndex;
					lastId = tempPid;
					tempPid = tempNode.pid;
				}
				nodes.put(nodeId, item);
			}
		}
	}
}