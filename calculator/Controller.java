package calculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.*;

import javafx.scene.text.Text;

public class Controller {

    @FXML
    private Text output;

    public Float number1 = 0f;
    private String operator = "";
    private boolean start = true;
    private int clearCount = 0;

    private Model model = new Model();

    @FXML
    private void processNumpad(ActionEvent event) {
        clearCount = 0;
        if (start || "0".equals(output.getText()) || !"".equals(operator)) {
            output.setText("");
            start = false;
        }

        String value = ((Button)event.getSource()).getText();
        output.setText(output.getText() + value);
    }
    
    @FXML
    private void processConstant(ActionEvent event){
        clearCount = 0;
        output.setText("");
        start = false;
        
        String value = ((Button)event.getSource()).getText();
        try{
            Calculator.dos.writeUTF("constant " + value);
            output.setText(String.valueOf(Calculator.dis.readUTF()));
        }
         catch(IOException e) {
             e.printStackTrace();
         }
        
    }
    
    @FXML
    private void processOperator(ActionEvent event) {
        String value = ((Button)event.getSource()).getText();
        
        if("C".equals(value)){
            if(clearCount > 0){
                start = true;
                clearCount = 0;
                operator = "";
            }
            output.setText("");
            if(operator.equals("")) start = true;
            clearCount++;
        }
        else if (!"=".equals(value)) {
            clearCount = 0;
            if(operator.isEmpty() && output.getText().isEmpty()){
                return;
            }
            if(operator.isEmpty() && !output.getText().isEmpty()){
                number1 = Float.parseFloat(output.getText());
            }
            operator = value;
        
            System.out.println(number1);
        }
        else{
            clearCount = 0;

            if (operator.isEmpty() || output.getText().isEmpty())
                return;
            try{
                Calculator.dos.writeUTF(String.valueOf(number1) + " " + String.valueOf(Float.parseFloat(output.getText())) + " " + operator);
                output.setText(String.valueOf(Calculator.dis.readUTF()));
            }
            catch(IOException e){
                e.printStackTrace();
            }
            operator = "";
            start = true;
        }
    }
}
