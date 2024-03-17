import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.util.*;

public class Main {
    static int L, Q;

    static HashMap<String, Customer> customers;
    static HashMap<String, ArrayList<Susi>> susis;
    static PriorityQueue<Susi> susiOnTable;

    static int countCustomer;
    static int countSusi;

    static class Susi implements Comparable<Susi> {
        int time;
        int location;
        boolean eaten;
        String name;

        int eatenTime;

        @Override
        public int compareTo(Susi o) {
            return this.eatenTime - o.eatenTime;
        }
    }

    static class Customer {
        int time;
        String name;
        int toEat;
        int location;
        boolean goHome;

    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        L = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());
        countCustomer = 0;
        countSusi = 0;
        susiOnTable = new PriorityQueue<>();
        customers = new HashMap<>();
        susis = new HashMap<>();
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int order = Integer.parseInt(st.nextToken());

            switch (order) {
                case 100:
                    int susiTime = Integer.parseInt(st.nextToken());
                    int susiLocation = Integer.parseInt(st.nextToken());

                    String susiName = st.nextToken();

                    makeSusi(susiTime, susiLocation, susiName);
                    break;
                case 200:
                    int customerTime = Integer.parseInt(st.nextToken());
                    int customerLocation = Integer.parseInt(st.nextToken());
                    String customerName = st.nextToken();
                    int customerToEat = Integer.parseInt(st.nextToken());

                    customerIn(customerTime, customerLocation, customerName, customerToEat);

                    break;
                case 300:
                    int pictureTime = Integer.parseInt(st.nextToken());

                    int[] calResult = takePicture(pictureTime);

                    sb.append(calResult[0] + " " + calResult[1] + "\n");
                    break;
                default:
                    break;
            }


        }

        bw.write(sb.toString());
        bw.flush();

    }

    private static void customerIn(int customerTime, int customerLocation, String customerName, int customerToEat) {
        Customer customer = new Customer();
        customer.time = customerTime;
        customer.location = customerLocation;
        customer.name = customerName;
        customer.toEat = customerToEat;
        customer.goHome = false;
        if (susis.get(customerName) != null) {
            ArrayList<Susi> sList = susis.get(customerName);

            for (int i = 0; i < sList.size(); i++) {
                Susi susiItem = sList.get(i);

                int currentLoc = (susiItem.location + customerTime - susiItem.time) % L;
                int locDiff;
                if (currentLoc > customerLocation) {
                    customerLocation += L;
                    locDiff = customerLocation - currentLoc;
                } else {
                    locDiff = customerLocation - currentLoc;

                }

                susiItem.eatenTime = customerTime + locDiff;

                susiOnTable.add(susiItem);
            }
        }

        countCustomer++;
        customers.put(customerName, customer);

    }

    private static void makeSusi(int susiTime, int susiLocation, String susiName) {
        Susi newSusi = new Susi();
        newSusi.eaten = false;
        newSusi.location = susiLocation;
        newSusi.name = susiName;
        newSusi.time = susiTime;
        if (susis.get(susiName) == null) {
            susis.put(susiName, new ArrayList<Susi>());
        }
        if(customers.get(susiName) != null){
            Customer cItem = customers.get(susiName);
            if (cItem != null) {

                int currentLoc = newSusi.location;
                int locDiff;
                int customerLocation;
                if (currentLoc > cItem.location) {
                    customerLocation = L + cItem.location;
                    locDiff = customerLocation - currentLoc;
                } else {
                    customerLocation = cItem.location;
                    locDiff = customerLocation - currentLoc;

                }

                newSusi.eatenTime = susiTime + locDiff;

                susiOnTable.add(newSusi);

            }
        }


        countSusi++;
        susis.get(susiName).add(newSusi);
    }


    public static int[] takePicture(int pictureTime) {
        eatSusi(pictureTime);
        int[] returnVal = new int[2];
        returnVal[0] = countCustomer;
        returnVal[1] = countSusi;

        return returnVal;

    }

    public static void eatSusi(int pictureTime) {
        while (!susiOnTable.isEmpty()) {
            Susi tempSusi = susiOnTable.peek();
            if (tempSusi.eatenTime > pictureTime) {
                break;
            }

            susiOnTable.poll();
            countSusi--;

            Customer tempCustomer = customers.get(tempSusi.name);

            tempCustomer.toEat--;
            if (tempCustomer.toEat <= 0) {
                tempCustomer.goHome = true;
                countCustomer--;
            }
        }
    }

}