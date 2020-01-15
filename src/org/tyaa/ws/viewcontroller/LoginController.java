/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.viewcontroller;

import org.tyaa.ws.screensframework.ControlledScreen;
import org.tyaa.ws.screensframework.ScreensController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import org.controlsfx.control.textfield.CustomTextField;
import org.tyaa.ws.WS1;
import org.tyaa.ws.common.Globals;
import org.tyaa.ws.dao.impl.UsersDAOImpl;
import org.tyaa.ws.entity.User;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class LoginController implements Initializable, ControlledScreen {
    
    @FXML
    private CustomTextField loginCTextField;
    @FXML
    private PasswordField passwordField;
    
    ScreensController myController;
    
    private UsersDAOImpl mUsersDAOImpl;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        mUsersDAOImpl = new UsersDAOImpl();
    }    

    @Override
    public void setScreenParent(ScreensController screenParent) {
        
        myController = screenParent;
    }
    
    @FXML
    private void goToSalesScreen(ActionEvent event){
       
        boolean valid = false;
        
        for (User user : mUsersDAOImpl.getAllUsers()) {
            
            if (loginCTextField.getText().equals(user.getLogin())) {
                
                if (passwordField.getText().equals(user.getPassword())) {
                    
                    valid = true;
                    Globals.mCurrentUserString = user.getLogin();
                    //if (!Globals.mCurrentUserString.equals("manager")
                    if (user.getId() != 1
                        && WS1.salesControllerInstance != null) {
                    
                        //WS1.salesControllerInstance.hideSaleEditCounterNewButton();
                        WS1.salesControllerInstance.hideEditSaleButtons();
                        WS1.shopsControllerInstance.hideEditShopButtons();
                        WS1.barrelsControllerInstance.hideEditBarrelButtons();
                        WS1.debtsControllerInstance.hideEditDebtsButtons();
                    }
                    break;
                }
            }
        }
        
        if (valid) {
         
            myController.setScreen(WS1.salesID);
            WS1.primaryStage.setMaximized(true);
        } else {
        
            Alert errorAlert =
                new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Ошибка");
            errorAlert.setHeaderText("Ошибка входа");
            errorAlert.setContentText("Неверное имя и/или пароль");
            errorAlert.showAndWait();
            resetForm();
        }
    }
    
    @FXML
    private void actionExit(ActionEvent event){
       
       Platform.exit();
       System.exit(0);
    }
    
    @FXML
    private void resetForm(ActionEvent event){
       
        resetForm();
    }
    
    private void resetForm(){
       
        loginCTextField.clear();
        passwordField.clear();
    }
}
