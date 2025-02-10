package calculator;

import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


// Server class
public class Server {
    public static void main(String[] args) throws IOException {
        Scanner scn = new Scanner(System.in);
        int port = Integer.parseInt(scn.nextLine());
        ServerSocket ss = new ServerSocket(port);

        System.out.printf("Server running on port %d%n", port);

        // running infinite loop for getting
        // client request
        while (true) {
            Socket s = null;

            try {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos);
                dos.writeUTF("You Have Connected To the Server");
                // Invoking the start() method
                t.start();

            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread {
    private Model model = new Model();
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    static final String USERNAME = "constant";
    static final String PASSWORD = "constant";
    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String received;
        
        while (true) {
            try {
                received = dis.readUTF();
                String args[] = received.split(" ");
                try {
                    Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (received.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }
                else if("constant".equals(args[0])){
                    try{
                        Connection connection = DriverManager.getConnection("jdbc:derby:ConstantDB;create=true", USERNAME, PASSWORD);
                        PreparedStatement ps = connection.prepareStatement("SELECT * FROM ConstantTable WHERE ID=?");
                        ps.setString(1, args[1]);
                      
                        ResultSet rs = ps.executeQuery();
                        
                        if(rs.next()){
                            dos.writeUTF(String.valueOf(rs.getFloat("VAL")));
                        }
                        
                    }
                    catch(SQLException err){
                        err.printStackTrace();
                    }
                }
                else{
                    System.out.println(received);
                    dos.writeUTF(String.valueOf(model.calculate(Float.parseFloat(args[0]), Float.parseFloat(args[1]), args[2]))); 
                }
               
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
