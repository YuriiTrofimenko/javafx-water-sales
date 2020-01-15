/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.viewcontroller;

import org.tyaa.ws.screensframework.ControlledScreen;
import org.tyaa.ws.screensframework.ScreensController;
import java.net.URL;
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
import org.tyaa.ws.dao.impl.CarsDAOImpl;
import org.tyaa.ws.entity.Car;
import org.tyaa.ws.entity.Driver;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class AddCarController implements Initializable, ControlledScreen {
    
    //элементы ввода информации
    @FXML
    CustomTextField numberCTextField;
    @FXML
    CustomTextField tonnageCTextField;
    @FXML
    CustomTextField govNumCTextField;
    //Объекты доступа к данным
    //private SalesDAOImpl mSalesDAOImpl;
    private CarsDAOImpl mCarsDAOImpl;
    
    private List<Car> mCars;
    private Set<String> mCarNamesSet;
        
    ScreensController myController;
    ValidationSupport validationSupport;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //mSalesDAOImpl = new SalesDAOImpl();
        mCarsDAOImpl = new CarsDAOImpl();
        
        mCarNamesSet = new HashSet<>();
        
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
                numberCTextField
                , Validator.createEmptyValidator("Номер автомобиля обязателен"));
        validationSupport.registerValidator(
                tonnageCTextField
                , Validator.createEmptyValidator("Тоннаж обязателен"));
        validationSupport.registerValidator(
                govNumCTextField
                , Validator.createEmptyValidator("Гос. номер обязателен"));
    }    

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
    
    @FXML
    private void goToCarsScreen(ActionEvent event){
       myController.setScreen(WS1.carsID);
       WS1.primaryStage.setMaximized(true);
    }
    
    @FXML
    private void resetForm(ActionEvent event){
       resetForm();
    }
    
    @FXML
    private void actionAddCar(ActionEvent actionEvent) {
        
        List<ValidationMessage> validationMessageList =
                (List<ValidationMessage>) validationSupport
                        .getValidationResult()
                        .getMessages();
        if (validationMessageList.isEmpty()) {
                
            /*System.out.println(mSelectedShop.getId());
            System.out.println(mSelectedBarrel.getId());
            System.out.println(mSelectedCar.getId());
            System.out.println(mSelectedCar.getId());
            System.out.println(countOldCTextField.getText());
            System.out.println(countNewCTextField.getText());
            System.out.println(volumeCTextField.getText());
            System.out.println(cleanCheckBox.isSelected());
            System.out.println(repairCheckBox.isSelected());
            System.out.println(profitCTextField.getText());
            System.out.println(debtCTextField.getText());
            System.out.println(createdDataPicker.getValue());
            System.out.println(noticeCTextField.getText());*/

            /**/
            String errorsString = "";
            boolean hasErrors = false;
            //get all existing active cars list
            mCars = mCarsDAOImpl.getFilteredCars(1, -1, -1);
            if (mCars != null) {
                
                mCarNamesSet.clear();
                for (Car car : mCars) {

                    mCarNamesSet.add(String.valueOf(car.getNumber()));
                }
            }
            if (mCarNamesSet.contains(numberCTextField.getText())) {
                errorsString += "Уже есть автомобиль с таким номером. ";
                hasErrors = true;
            }
            if (mCars != null) {
                
                mCarNamesSet.clear();
                for (Car car : mCars) {

                    mCarNamesSet.add(car.getGovNum());
                }
            }
            if (mCarNamesSet.contains(govNumCTextField.getText())) {
                errorsString += "Уже есть автомобиль с таким гос. номером. ";
                hasErrors = true;
            }

            if (hasErrors) {

                /*Alert warningConfirmationAlert =
                    new Alert(Alert.AlertType.CONFIRMATION);
                warningConfirmationAlert.setTitle("Подозрительные значения");
                warningConfirmationAlert.setHeaderText("Все равно добавить автомобиль?");
                warningConfirmationAlert.setContentText(warningString);

                Optional<ButtonType> result =
                        warningConfirmationAlert.showAndWait();
                if (result.get() == ButtonType.OK){

                    //Нажата кнопка OK - сохраняем автомобиль в БД
                    processCarSaving();
                } else {
                    //Нажата кнопка CANCEL - показываем сообщение об отмене
                    Alert cancelWarningAlert =
                            new Alert(Alert.AlertType.WARNING);
                    cancelWarningAlert.setTitle("Предупреждение");
                    cancelWarningAlert.setHeaderText("Автомобиль не добавлен");
                    cancelWarningAlert.setContentText("Добавление автомобиля отменено");
                    cancelWarningAlert.showAndWait();
                }*/
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Автомобиль не добавлен");
                alert.setContentText(errorsString);
                alert.showAndWait();
            } else {

                //Не было ни ошибок, ни предупреждений -
                //сохраняем автомобиль в БД
                processCarSaving();
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
            alert.setHeaderText("Автомобиль не добавлен");
            alert.setContentText(errorsString);
            alert.showAndWait();
        }
    }
    
    //Приведение формы в исходное состояние
    private void resetForm(){
        
        //очистка элементов ввода
        numberCTextField.clear();
        tonnageCTextField.clear();
        govNumCTextField.clear();
    }
    
    private void processCarSaving(){
    
        Car newCar = new Car();
        newCar.setNumber(
                Integer.parseInt(numberCTextField.getText())
        );
        newCar.setTonnage(
                Integer.parseInt(tonnageCTextField.getText())
        );
        newCar.setGovNum(govNumCTextField.getText());

        mCarsDAOImpl.createCar(newCar);

        //Сообщение пользователю об успешном добавлении доставки в БД
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText("Автомобиль успешно добавлен");
        //Показать сообщение и ждать
        alert.showAndWait();
        //Приводим форму в исходное состояние
        resetForm();
        //Вызываем обновление данных в коллекции-источнике для
        //таблицы водителей (в представлении водителей)
        //CarsController.updateCarsForPage();
        
        WS1.carsControllerInstance.updateCarsForPage();

//            for (NewCarListener ndl : mNewCarListeners){
//                
//                System.out.println("event!");
//                ndl.createdCar();
//            }

        WS1.addSaleControllerInstance.updateCars();
        WS1.salesControllerInstance.updateCars();
    }
}
