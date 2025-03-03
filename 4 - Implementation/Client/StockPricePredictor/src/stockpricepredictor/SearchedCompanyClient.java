
package stockpricepredictor;
import Controller.PredictionViewController;
import Model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchedCompanyClient implements Runnable{
    String searchText;

    public SearchedCompanyClient(String searchText) {
        this.searchText = searchText;
    }

    ObjectInputStream objectInputStream;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    ArrayList<CompanyEnlistment> cenlist=new ArrayList<>();
    @Override
    public void run() {
        System.out.println("inside run-----");
        try {

            Socket socket = new Socket(StockPricePredictor.serverIP,4040);
            //socket.connect(socket.getRemoteSocketAddress());

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner socketScanner = new Scanner(socket.getInputStream());
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("connected in search");

            String str = "Search";
            printWriter.println(str);
            printWriter.flush();

            printWriter.println(searchText);
            printWriter.flush();

            int num = (Integer) objectInputStream.readObject();
            System.out.println("num = "+ num);

            for(int i=0;i<num;i++)
            {
                CompanyEnlistment companyEnlistment=(CompanyEnlistment)objectInputStream.readObject();
                cenlist.add(companyEnlistment);
                System.out.println(companyEnlistment);
            }

            printWriter.println("Search List Received");
            printWriter.flush();


            PredictionViewController.setTable(cenlist);

            socket.close();


        } catch (IOException ex) {
            Logger.getLogger(ListGetterClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ListGetterClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public ArrayList<CompanyEnlistment> getCEnlistment()
    {
        return cenlist;
    }

}
