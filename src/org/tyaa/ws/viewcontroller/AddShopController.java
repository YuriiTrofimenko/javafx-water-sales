/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.viewcontroller;

import java.math.BigDecimal;
import org.tyaa.ws.screensframework.ControlledScreen;
import org.tyaa.ws.screensframework.ScreensController;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.tools.ValueExtractor;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.tyaa.ws.WS1;
import org.tyaa.ws.dao.impl.ShopsDAOImpl;
import org.tyaa.ws.entity.Shop;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class AddShopController implements Initializable, ControlledScreen {

    /*Внедренные хендлеры элементов UI*/
    
    //элементы ввода информации
    @FXML
    CustomTextField nameCTextField;
    @FXML
    CustomTextField legalNameCTextField;
    @FXML
    CustomTextField addressCTextField;
    @FXML
    CustomTextField phoneCTextField;
    //дата начала сотрудничества
    @FXML
    DatePicker bcDatePicker;
    //условия сотрудничества
    @FXML
    CustomTextField cTermsCTextField;
    //находится ли магазин за чертой города?
    @FXML
    CheckBox farCheckBox;
    
    //Объекты доступа к данным
    //private SalesDAOImpl mSalesDAOImpl;
    private ShopsDAOImpl mShopsDAOImpl;
    
    private List<Shop> mShops;
    private Set<String> mShopNamesSet;
    
    ScreensController myController;
    ValidationSupport validationSupport;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //mSalesDAOImpl = new SalesDAOImpl();
        mShopsDAOImpl = new ShopsDAOImpl();
        
        mShopNamesSet = new HashSet<>();
        
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
                , Validator.createEmptyValidator("Название магазина обязательно"));
//        validationSupport.registerValidator(
//                legalNameCTextField
//                , Validator.createEmptyValidator("Юридическое название обязательно"));
        validationSupport.registerValidator(
                addressCTextField
                , Validator.createEmptyValidator("Адрес обязателен"));
        validationSupport.registerValidator(
                phoneCTextField
                , Validator.createEmptyValidator("Телефонный номер обязателен"));
    }    

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
    
    @FXML
    private void goToShopsScreen(ActionEvent event){
       myController.setScreen(WS1.shopsID);
       WS1.primaryStage.setMaximized(true);
    }
    
    @FXML
    private void resetForm(ActionEvent event){
       resetForm();
    }
    
    @FXML
    private void actionAddShop(ActionEvent actionEvent) {
        
        List<ValidationMessage> validationMessageList =
                (List<ValidationMessage>) validationSupport
                        .getValidationResult()
                        .getMessages();
        if (validationMessageList.isEmpty()) {
                
            /*System.out.println(mSelectedShop.getId());
            System.out.println(mSelectedBarrel.getId());
            System.out.println(mSelectedDriver.getId());
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

            String errorsString = "";
            boolean hasErrors = false;
            
            mShops = mShopsDAOImpl.getFilteredShops(1, -1, -1);
            if (mShops != null) {
                
                mShopNamesSet.clear();
                for (Shop shop : mShops) {

                    mShopNamesSet.add(shop.getName());
                }
            }
            if (mShopNamesSet.contains(nameCTextField.getText())) {
                
                errorsString += "Уже есть магазаин с таким названием ";
                hasErrors = true;
            }
            
            if (hasErrors) {
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Магазин не добавлен");
                alert.setContentText(errorsString);
                alert.showAndWait();
            } else {
            
                processShopSaving();
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
            alert.setHeaderText("Магазин не добавлен");
            alert.setContentText(errorsString);
            alert.showAndWait();
        }
    }
    
    //Приведение формы в исходное состояние
    private void resetForm(){
        
        //очистка элементов ввода
        nameCTextField.clear();
        legalNameCTextField.clear();
        addressCTextField.clear();
        phoneCTextField.clear();
        cTermsCTextField.clear();
        farCheckBox.setSelected(false);
        //Элементу "выбор даты" устанавливаем текущую дату
        bcDatePicker.setValue(LocalDate.now());
    }
    
    private void processShopSaving(){
    
        Shop newShop = new Shop();
        newShop.setName(nameCTextField.getText());
        newShop.setLegalName(legalNameCTextField.getText());
        newShop.setAddress(addressCTextField.getText());
        newShop.setPhone(phoneCTextField.getText());
        newShop.setBCDate(
            Date.from((bcDatePicker
                .getValue()
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())).toInstant())
        );
        newShop.setCTerms(cTermsCTextField.getText());
        //start debt balance = 0
        newShop.setDebt(BigDecimal.ZERO);
        newShop.setFar(farCheckBox.isSelected());
        //
        mShopsDAOImpl.createShop(newShop);

        //Сообщение пользователю об успешном добавлении доставки в БД
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText("Магазин успешно добавлен");
        //Показать сообщение и ждать
        alert.showAndWait();
        //Приводим форму в исходное состояние
        resetForm();
        //Вызываем обновление данных в коллекции-источнике для
        //таблицы магазинов (в представлении магазинов)
        WS1.shopsControllerInstance.updateShopsForPage();
        //Вызываем обновление данных в списке
        //всех магазинов (в контроллерах добавления доставок и бочек)
        WS1.addSaleControllerInstance.updateShops();
        WS1.salesControllerInstance.updateShops();
        WS1.barrelsControllerInstance.updateShops();
        WS1.addBarrelControllerInstance.updateShops();
        WS1.shopsControllerInstance.updateFilterShops();
    }
}
