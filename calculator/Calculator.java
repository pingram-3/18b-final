package calculator;

import java.net.*;
import java.io.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Calculator extends Application {

    public static DataInputStream dis;
    public static DataOutputStream dos;
    public static Socket s;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("calculatorui.fxml"));
        try {
            InetAddress ip = InetAddress.getByName("localhost");

            int port = 3000;
            s = new Socket(ip, port); 

            // obtaining input and out streams
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

            System.out.println(dis.readUTF());

        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setScene(
                new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        try{
            System.out.println("Disconnecting from Server");
            dos.writeUTF("Exit");
            s.close();
            dis.close();
            dos.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
