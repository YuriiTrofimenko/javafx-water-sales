/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.viewcontroller;

import org.tyaa.ws.screensframework.ControlledScreen;
import org.tyaa.ws.screensframework.ScreensController;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.tools.ValueExtractor;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.tyaa.ws.WS1;
import org.tyaa.ws.dao.impl.DriversDAOImpl;
import org.tyaa.ws.entity.Driver;
import org.tyaa.ws.events.interfaces.NewDriverListener;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class AddDriverController implements Initializable, ControlledScreen {
    
    //элементы ввода информации
    @FXML
    CustomTextField nameCTextField;
    @FXML
    CustomTextField phoneCTextField;
    //Объекты доступа к данным
    //private SalesDAOImpl mSalesDAOImpl;
    private DriversDAOImpl mDriversDAOImpl;
    
    //private List<NewDriverListener> mNewDriverListeners;
    
    private List<Driver> mDrivers;
    private Set<String> mDriverNamesSet;
    
    ScreensController myController;
    ValidationSupport validationSupport;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //mSalesDAOImpl = new SalesDAOImpl();
        mDriversDAOImpl = new DriversDAOImpl();
        
        mDriverNamesSet = new HashSet<>();

        //mNewDriverListeners = new ArrayList<NewDriverListener>();
        
        /*Приведение формы в исходное состояние*/
        
        resetForm();

        //Активация механизма валидации для элементов ввода типа CustomTextField
        ValueExtractor.addObservableValueExtractor(
                c -> c instanceof CustomTextField
                , c -> ((CustomTextField) c).textProperty());
        validationSupport = new ValidationSupport();
        //Явная настройка включения визуального оформления валидации
        validationSupport.setErrorDecorationEnabled(true);
        //Настройки валидации для каждого элемента ввода, подлежащего проверке
        validationSupport.registerValidator(
                nameCTextField
                , Validator.createEmptyValidator("Имя водителя обязательно"));
        validationSupport.registerValidator(
                phoneCTextField
                , Validator.createEmptyValidator("Телефонный номер обязателен"));
    }    

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
    
    @FXML
    private void goToDriversScreen(ActionEvent event){
       myController.setScreen(WS1.driversID);
       WS1.primaryStage.setMaximized(true);
    }
    
    @FXML
    private void resetForm(ActionEvent event){
       resetForm();
    }
    
    @FXML
    private void actionAddDriver(ActionEvent actionEvent) {
        
        List<ValidationMessage> validationMessageList =
                (List<ValidationMessage>) validationSupport
                        .getValidationResult()
                        .getMessages();
        if (validationMessageList.isEmpty()) {
                
            /**/
            String errorsString = "";
            boolean hasErrors = false;
            //get all existing active drivers list
            mDrivers = mDriversDAOImpl.getFilteredDrivers(1, -1, -1);
            if (mDrivers != null) {
                
                mDriverNamesSet.clear();
                for (Driver driver : mDrivers) {

                    mDriverNamesSet.add(driver.getName());
                }
            }
            if (mDriverNamesSet.contains(nameCTextField.getText())) {
                errorsString += "Уже есть водитель с таким именем. ";
                hasErrors = true;
            }

            if (hasErrors) {

                /*Alert warningConfirmationAlert =
                    new Alert(Alert.AlertType.CONFIRMATION);
                warningConfirmationAlert.setTitle("Подозрительные значения");
                warningConfirmationAlert.setHeaderText("Все равно добавить водителя?");
                warningConfirmationAlert.setContentText(warningString);

                Optional<ButtonType> result =
                        warningConfirmationAlert.showAndWait();
                if (result.get() == ButtonType.OK){

                    //Нажата кнопка OK - сохраняем водителя в БД
                    processDriverSaving();
                } else {
                    //Нажата кнопка CANCEL - показываем сообщение об отмене
                    Alert cancelWarningAlert =
                            new Alert(Alert.AlertType.WARNING);
                    cancelWarningAlert.setTitle("Предупреждение");
                    cancelWarningAlert.setHeaderText("Водитель не добавлен");
                    cancelWarningAlert.setContentText("Добавление водителя отменено");
                    cancelWarningAlert.showAndWait();
                }*/
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Водитель не добавлен");
                alert.setContentText(errorsString);
                alert.showAndWait();
            } else {

                //Не было ни ошибок, ни предупреждений -
                //сохраняем водителя в БД
                processDriverSaving();
            }
        } else {
            
            //Если хотя бы одно из валидируемых полей не заполнено,
            //показываем окно ошибки "Все поля должны быть заполнены!"
            
            String errorsString = "";
            
            for (ValidationMessage validationMessage : validationMessageList) {
                
                errorsString += "поле \""
                        + ((CustomTextField)validationMessage.getTarget()).getPromptText()
                        + "\": "
                        + validationMessage.getText()
                        + ". ";
            }
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Водитель не добавлен");
            alert.setContentText(errorsString);
            alert.showAndWait();
        }
    }
    
    //Приведение формы в исходное состояние
    private void resetForm(){
        
        //очистка элементов ввода
        nameCTextField.clear();
        phoneCTextField.clear();
    }
    
    /*public void addListener(NewDriverListener _toAdd) {
        mNewDriverListeners.add(_toAdd);
    }*/
    
    private void processDriverSaving(){
    
        Driver newDriver = new Driver();
        newDriver.setName(nameCTextField.getText());
        newDriver.setPhone(phoneCTextField.getText());

        mDriversDAOImpl.createDriver(newDriver);

        //Сообщение пользователю об успешном добавлении доставки в БД
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText("Водитель успешно добавлен");
        //Показать сообщение и ждать
        alert.showAndWait();
        //Приводим форму в исходное состояние
        resetForm();
        //Вызываем обновление данных в коллекции-источнике для
        //таблицы водителей (в представлении водителей)
        //DriversController.updateDriversForPage();
        
        WS1.driversControllerInstance.updateDriversForPage();

        WS1.addSaleControllerInstance.updateDrivers();
        WS1.salesControllerInstance.updateDrivers();
    }
}
