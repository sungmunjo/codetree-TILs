import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.util.*;

public class Main {
    static int L, Q;

    static ArrayList<Customer> customers;
    static HashMap<String, ArrayList<Susi>> susis;
    static class Susi{
        int time;
        int location;
        boolean eaten;
        String name;

    }

    static class Customer{
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
        customers = new ArrayList<>();
        susis = new HashMap<>();
        for(int i=0;i<Q;i++){
            st = new StringTokenizer(br.readLine());
            int order = Integer.parseInt(st.nextToken());

            switch(order){
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

                    int [] calResult = takePicture(pictureTime);

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

        customers.add(customer);

    }

    private static void makeSusi(int susiTime, int susiLocation, String susiName) {
        Susi newSusi = new Susi();
        newSusi.eaten = false;
        newSusi.location = susiLocation;
        newSusi.name = susiName;
        newSusi.time = susiTime;
        if(susis.get(susiName) == null){
            susis.put(susiName, new ArrayList<Susi>());
        }
        susis.get(susiName).add(newSusi);
    }


    public static int[] takePicture(int pictureTime){
        eatSusi(pictureTime);
        int [] returnVal = new int[2];

        for(int i=0 ;i<customers.size();i++) {
            Customer cItem = customers.get(i);
            if(!cItem.goHome){
                returnVal[0]++;
            }
        }

//        Set<Map.Entry<String, ArrayList<Susi>>> item = susis.entrySet();
        for(Map.Entry<String, ArrayList<Susi>> eitem : susis.entrySet()){
            ArrayList<Susi> sList = eitem.getValue();
            for(int i=0;i<sList.size();i++){
                if(!sList.get(i).eaten){
                    returnVal[1]++;
                }
            }
        }

        return returnVal;

    }

    public static void eatSusi(int pictureTime){
        for(int i=0 ;i<customers.size();i++){
            Customer cItem = customers.get(i);

            ArrayList<Susi> susiList = susis.get(cItem.name);

            for(int j=0;j<susiList.size();j++){
                if(susiList.get(j).eaten)continue;
                Susi sItem = susiList.get(j);
                int startLocation ;
                int timeDiff;
                if(cItem.time > sItem.time){
                    startLocation = (sItem.location + cItem.time - sItem.time) % L;
                    timeDiff = pictureTime - cItem.time;
                }else{
                    startLocation = (sItem.location);
                    timeDiff = pictureTime - sItem.time;
                }

                int currentLocation  = (startLocation + timeDiff) % L;
                if(timeDiff >= L){
                    sItem.eaten = true;
                    cItem.toEat--;
                    if(cItem.toEat <= 0){
                        cItem.goHome = true;
                    }
                    continue;
                }

                if(currentLocation > startLocation){
                    if(cItem.location <= currentLocation && cItem.location >= startLocation){
                        sItem.eaten = true;
                        cItem.toEat--;
                        if(cItem.toEat <= 0){
                            cItem.goHome = true;
                        }
                    }
                }else{
                    if(cItem.location >= startLocation || cItem.location <= currentLocation){
                        sItem.eaten = true;
                        cItem.toEat--;
                        if(cItem.toEat <= 0){
                            cItem.goHome = true;
                        }
                    }
                }


            }
        }
    }

}