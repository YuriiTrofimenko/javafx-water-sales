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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.tools.ValueExtractor;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.tyaa.ws.WS1;
import org.tyaa.ws.common.Settings;
import org.tyaa.ws.dao.impl.BarrelCapacitiesDAOImpl;
import org.tyaa.ws.dao.impl.ShopsDAOImpl;
import org.tyaa.ws.dao.impl.BarrelsDAOImpl;
import org.tyaa.ws.entity.Shop;
import org.tyaa.ws.entity.Barrel;
import org.tyaa.ws.dao.impl.WaterTypesDAOImpl;
import org.tyaa.ws.entity.BarrelCapacity;
import org.tyaa.ws.entity.WaterType;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class AddBarrelController implements Initializable, ControlledScreen {

    /*Внедренные хендлеры элементов UI*/
    
    //элементы ввода информации
    @FXML
    CustomTextField shopCTextField;
    @FXML
    CustomTextField priceCTextField;
    @FXML
    CustomTextField typeCTextField;
    @FXML
    CustomTextField capacityCTextField;
    @FXML
    CustomTextField countCTextField;
    @FXML
    CustomTextField positionsCTextField;
    /*@FXML
    CheckBox replaceCheckBox;*/
    @FXML
    DatePicker lastCleanDatePicker;
    
    @FXML
    Label positionLabel;
    
    //Объекты доступа к данным
    //private SalesDAOImpl mSalesDAOImpl;
    private static ShopsDAOImpl mShopsDAOImpl;
    private BarrelsDAOImpl mBarrelsDAOImpl;
    private static BarrelCapacitiesDAOImpl mBarrelCapacitiesDAOImpl;
    private static WaterTypesDAOImpl mWaterTypesDAOImpl;
    //private DriversDAOImpl mDriversDAOImpl;
    //private CarsDAOImpl mCarsDAOImpl;
    
    //Списки объектов
    private static List<Shop> mShops;
    //private List<Barrel> mBarrels;
    //private List<Barrel> mShopBarrels;
    private List<WaterType> mWaterTypes;
    private List<BarrelCapacity> mBarrelCapacities;
    
    //Наборы полей из объектов
    private Set<String> mShopNamesSet;
    private Set<String> mWaterTypesSet;
    private Set<String> mBarrelCapacitiesSet;
    private Set<String> mBarrelPositionsSet;
    
    //хендлеры к наборам автодополнения
    private AutoCompletionBinding<String> mShopAutoCompletionBinding;
    private AutoCompletionBinding<String> mWaterTypeAutoCompletionBinding;
    private AutoCompletionBinding<String> mBarrelCapacityAutoCompletionBinding;
    
    //Выбранные объекты
    private Shop mSelectedShop;
    //private Barrel mSelectedBarrel;
    private WaterType mSelectedWaterType;
    private BarrelCapacity mSelectedBarrelCapacity;
    
    //Разрядность водомера
    private int mCounterMaxPosition;
    //Максимальное значение на водомере
    private int mCounterMaxNumber;
    
    //ObservableList<Barrel> mBarrelsObservableList;
    
    ScreensController myController;
    ValidationSupport validationSupport;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        WS1.addBarrelControllerInstance = this;
        
        //mSalesDAOImpl = new SalesDAOImpl();
        mShopsDAOImpl = new ShopsDAOImpl();
        mBarrelsDAOImpl = new BarrelsDAOImpl();
        mBarrelCapacitiesDAOImpl = new BarrelCapacitiesDAOImpl();
        mWaterTypesDAOImpl = new WaterTypesDAOImpl();
        
        mCounterMaxPosition = 7;
        mCounterMaxNumber = 9999999;
        
        //TODO вынести в глобальный класс (фасад?)
        mShops = mShopsDAOImpl.getAllShops();
        mShopNamesSet = new HashSet<>();
        
        //mBarrels = mBarrelsDAOImpl.getAllBarrels();
        //mShopBarrels = new ArrayList();
        
        List<WaterType> waterTypesTmp = mWaterTypesDAOImpl.getAllWaterTypes();
        if (waterTypesTmp == null) {
            
            waterTypesTmp = new ArrayList<>();
        }
        mWaterTypes =
            waterTypesTmp.stream()
                .filter(wType -> ((WaterType)wType).getActive())
                .collect(Collectors.toList());
        if (mWaterTypes == null) {
            
            mWaterTypes = new ArrayList<>();
        }
        mWaterTypesSet = new HashSet<>();
        
        List<BarrelCapacity> barrelCapacitiesTmp =
            mBarrelCapacitiesDAOImpl.getAllBarrelCapacities();
        if (barrelCapacitiesTmp == null) {
            
            barrelCapacitiesTmp = new ArrayList<>();
        }
        mBarrelCapacities =
                barrelCapacitiesTmp.stream()
                .filter(bCap -> ((BarrelCapacity)bCap).getActive())
                .collect(Collectors.toList());
        if (mBarrelCapacities == null) {
            
            mBarrelCapacities = new ArrayList<>();
        }
        mBarrelCapacitiesSet = new HashSet<>();
        
        //
        mBarrelPositionsSet = new HashSet<>();
        mBarrelPositionsSet.add("5");
        mBarrelPositionsSet.add("6");
        mBarrelPositionsSet.add("7");
        TextFields.bindAutoCompletion(positionsCTextField, mBarrelPositionsSet);
        //mBarrelsObservableList = FXCollections.observableArrayList();
        
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
                shopCTextField
                , Validator.createEmptyValidator("Название магазина обязательно"));
        /*validationSupport.registerValidator(
                barrelComboBox
                , Validator.createEmptyValidator("Barrel is required"));*/
        validationSupport.registerValidator(
                priceCTextField
                , Validator.createRegexValidator("Введите целое или дробное число (максимум - 99999.99)", "[0-9]{1,5}[.,]{0,1}[0-9]{0,2}", Severity.ERROR));
        validationSupport.registerValidator(
                typeCTextField
                , Validator.createEmptyValidator("Тип бочки обязателен"));
        validationSupport.registerValidator(
                capacityCTextField
                , Validator.createRegexValidator("Введите целое число от 0 до 9999", "[0-9]{1,4}", Severity.ERROR));
        validationSupport.registerValidator(
                countCTextField
                , Validator.createRegexValidator("Введите целое число от 0 до " + mCounterMaxNumber, "[0-9]{1," + mCounterMaxPosition + "}", Severity.ERROR));
        
        /*Настраиваем способ отображения информации из объектов,
        подключенных в составе коллекции-источника к контролам списочного типа*/
        
        //прорисовка выпадающего списка бочек
//        barrelComboBox.setCellFactory((comboBox) -> {
//            return new ListCell<Barrel>() {
//                @Override
//                protected void updateItem(Barrel barrelItem, boolean empty) {
//                    super.updateItem(barrelItem, empty);
//
//                    if (barrelItem == null || empty) {
//                        setText(null);
//                    } else {
//                        setText(
//                            barrelItem.getWhaterTId().getName()
//                            + ", "
//                            + mBarrelCapacitiesDAOImpl.getBarrelCapacity(
//                                    barrelItem.getCapacityId()
//                            ).getCapacity()
//                            + " л"
//                        );
//                    }
//                }
//            };
//        });
//        //прорисовка выбранного элемента выпадающего списка бочек
//        barrelComboBox.setConverter(new StringConverter<Barrel>() {
//            @Override
//            public String toString(Barrel barrel) {
//                if (barrel == null) {
//                    return null;
//                } else {
//                    return (
//                        barrel.getWhaterTId().getName()
//                        + ", "
//                        + mBarrelCapacitiesDAOImpl.getBarrelCapacity(
//                                barrel.getCapacityId()
//                        ).getCapacity()
//                        + " л"
//                    );
//                }
//            }
//
//            @Override
//            public Barrel fromString(String barrelString) {
//                return null; // No conversion fromString needed.
//            }
//        });
        
        /*Настраиваем здесь обработчики событий тех контрлов,
        для которых это невозможно при помощи внедрения */
        
        //Изменение значения поля "разрядность водомера"
        positionsCTextField.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                //
                String posString = positionsCTextField.getText();
                    
                switch(posString){

                    case "5":
                    {
                        mCounterMaxPosition = 5;
                        mCounterMaxNumber = 99999;
                        break;
                    }
                    case "6":
                    {
                        mCounterMaxPosition = 6;
                        mCounterMaxNumber = 999999;
                        break;
                    }
                    case "7":
                    {
                        mCounterMaxPosition = 7;
                        mCounterMaxNumber = 9999999;
                        break;
                    }
                    default:
                    {
                        mCounterMaxPosition = 6;
                        mCounterMaxNumber = 999999;
                        break;
                    }
                }
                positionLabel.setText("не более " + posString + " знаков");
            }
        });
        
        //обработка события "ввод названия магазина"
//        shopCTextField.textProperty().addListener(new ChangeListener(){
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                
//                mShopBarrels.clear();
//                mBarrelsObservableList.clear();
//                
//                //Выбираем бочки, относящиеся к выбранному магазину
//                if (!shopCTextField.textProperty().getValue().equals("")
//                        && mShopNamesSet.contains(shopCTextField.textProperty().getValue())) {
//                    for (Shop shop : mShops) {
//                        //System.out.println("shop");
//                        if (shop.getName().equals(shopCTextField.textProperty().getValue())) {
//                            mSelectedShop = shop;
//                            break;
//                        }
//                    }
//                    
//                    //barrelComboBox.getItems().clear();
//                    for (Barrel barrel : mBarrels) {
//                        if (Objects.equals(barrel.getShopId(), mSelectedShop.getId())) {
//                            mShopBarrels.add(barrel);
//                        }
//                    }
//                    for (Barrel barrel : mShopBarrels) {
////                        mBarrelsObservableList.add(
////                            barrel.getWhaterTId().getName()
////                                + ", "
////                                + mBarrelCapacitiesDAOImpl.getBarrelCapacity(
////                                        barrel.getCapacityId()
////                                ).getCapacity()
////                                + " л"
////                        );
//                        mBarrelsObservableList.add(barrel);
//                        //System.out.println(barrel.getCapacityId());
//                    }
//                    if (mBarrelsObservableList.size() > 0) {
//                        barrelComboBox.setItems(mBarrelsObservableList);
//                    }
//                }
//            }
//        });
        
        //обработка события "выбор бочки"
//        barrelComboBox.setOnAction((event) -> {
//            mSelectedBarrel = (Barrel) barrelComboBox
//                            .getSelectionModel()
//                            .getSelectedItem();
//            //System.out.println("ComboBox Action (selected: " + selectedPerson.toString() + ")");
//        });
//        
//        //обработка события "переключение чекбокса"
//        cleanCheckBox.setOnAction((event) -> {
//            if (cleanCheckBox.isSelected()) {
//                repairCheckBox.setSelected(false);
//            }
//        });
//        repairCheckBox.setOnAction((event) -> {
//            if (repairCheckBox.isSelected()) {
//                cleanCheckBox.setSelected(false);
//            }
//        });
        
        //обработка события "ввод текста в поле счетчик до"
//        countOldCTextField.textProperty().addListener(new ChangeListener(){
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                
//                //Копируем текст из поля "счетчик до"
//                //в поле "счетчик после"
//                //(в дальнейшем текст второго поля можно изменить вручную)
//                countNewCTextField.textProperty().setValue(
//                        countOldCTextField.textProperty().getValue()
//                );
//            }
//        });
    }    

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
    
    @FXML
    private void goToBarrelsScreen(ActionEvent event){
       myController.setScreen(WS1.barrelsID);
       WS1.primaryStage.setMaximized(true);
    }
    
    @FXML
    private void resetForm(ActionEvent event){
       resetForm();
    }
    
//    public void shopCTextFieldChanged(InputMethodEvent event){
//        System.out.println("event");
//        if (!shopCTextField.textProperty().getValue().equals("") && mShopNamesSet.contains(shopCTextField.textProperty().getValue())) {
//            System.out.println("shops");
//            for (Shop shop : mShops) {
//                System.out.println("shop");
//                if (shop.getName().equals(shopCTextField.textProperty().getValue())) {
//                    System.out.println("selected shop");
//                    mSelectedShop = shop;
//                }
//            }
//            mBarrels = mBarrelsDAOImpl.getAllBarrels();
//            for (Barrel barrel : mBarrels) {
//                System.out.println(barrel.getCapacityId());
//            }
//        }
//    }
    @FXML
    private void actionAddBarrel(ActionEvent actionEvent) {
        
        //securityNameCTextField.getStyleClass().remove("error-c-text-field");
        //securityNameCTextField.getStyleClass().add("custom-text-field");
        
        List<ValidationMessage> validationMessageList =
                (List<ValidationMessage>) validationSupport
                        .getValidationResult()
                        .getMessages();
        if (validationMessageList.isEmpty()) {
            
            //Если магазина с введенным названием нет в БД,
            //показываем окно ошибки
            String errorsString = "";
            boolean hasErrors = false;
            if (!mShopNamesSet.contains(shopCTextField.textProperty().getValue())) {
                errorsString += "Нет магазина с таким названием. ";
                hasErrors = true;
            }
//            if (mSelectedBarrel == null) {
//                errorsString += "Не выбрана бочка. ";
//                hasErrors = true;
//            }
            if (!mWaterTypesSet.contains(typeCTextField.textProperty().getValue())) {
                errorsString += "Нет типа воды с таким названием. ";
                hasErrors = true;
            }
            if (!mBarrelCapacitiesSet.contains(capacityCTextField.textProperty().getValue())) {
                errorsString += "Нет такой емкости. ";
                hasErrors = true;
            }
            if (!mBarrelPositionsSet.contains(positionsCTextField.textProperty().getValue())) {
                errorsString += "Нет такой разрядности водомера. ";
                hasErrors = true;
            }
            
            if (hasErrors) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Доставка не добавлена");
                alert.setContentText(errorsString);
                alert.showAndWait();
            } else {
                
                /*Устанавливаем объекты выбранного магазина, типа, емкости*/
                for (Shop shop : mShops) {
                    if (shop.getName().equals(shopCTextField.getText())) {
                        mSelectedShop = shop;
                    }
                }
                for (WaterType waterType : mWaterTypes) {
                    if (waterType.getName().equals(typeCTextField.getText())) {
                        mSelectedWaterType = waterType;
                    }
                }
                for (BarrelCapacity barrelCapacity : mBarrelCapacities) {
                    if (String.valueOf(barrelCapacity.getCapacity()).equals(capacityCTextField.getText())) {
                        mSelectedBarrelCapacity = barrelCapacity;
                    }
                }
                
                //Если пользователь удалил текст из поля ввода "долг",
                //устанавливаем значение для записи в БД: 0
                /*String debtString = !debtCTextField.getText().equals("")
                        ? debtCTextField.getText()
                        : "0";*/
                
                //TODO добавить предупреждение, если показания счетчика
                //или сумма опалты - подозрительные
                //TODO во время выполнения запроса отображать метку "Загрузка..."
                //TODO для замены бочки учитывать слив через счетчик
                //TODO добавлять второе значение счетчика в таблицу "Бочки"
                
                /*System.out.println(mSelectedShop.getId());
                System.out.println(mSelectedWaterType);
                System.out.println(mSelectedBarrelCapacity.getId());
                System.out.println(Long.parseLong(priceCTextField.getText()));
                System.out.println(Integer.parseInt(countCTextField.getText()));
                System.out.println(Date.from((lastCleanDatePicker
                    .getValue()
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())).toInstant()));
                System.out.println(replaceCheckBox.isSelected());*/
                
                Barrel newBarrel = new Barrel();
                newBarrel.setShopId(mSelectedShop.getId());
                newBarrel.setWhaterTId(mSelectedWaterType);
                newBarrel.setCapacityId(mSelectedBarrelCapacity.getId());
                newBarrel.setPrice(
                    BigDecimal.valueOf(Double.parseDouble(priceCTextField.getText().replaceAll(",", ".")))
                );
                newBarrel.setCounter(
                        Integer.parseInt(countCTextField.getText())
                );
                newBarrel.setPositions(
                        Integer.parseInt(positionsCTextField.getText())
                );
                newBarrel.setLastCDate(
                    Date.from((lastCleanDatePicker
                    .getValue()
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())).toInstant())
                );
                //newBarrel.setRecentlyReplaced(replaceCheckBox.isSelected());
                newBarrel.setRecentlyReplaced(true);
                //если в форму не введено значение допустимого остатка,
                //то вычисляем и устанавливаем значение по умолчанию
                newBarrel.setAllowedRest(
                    (int)(
                        mSelectedBarrelCapacity.getCapacity() * Settings.getAllowedRestPecent()
                    )
                );
                //дата создания бочки также устанавливается в качестве даты
                //последнего вычисления необходимого периода заправки
                newBarrel.setPeriodCalcDate(
                    Date.from((lastCleanDatePicker
                    .getValue()
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())).toInstant())
                );
                
                newBarrel.setPeriod(1);
                
                mBarrelsDAOImpl.createBarrel(newBarrel);
                
//                //если был включен чекбокс "замена",
//                //делаем настройку выбранной бочке "недавно заменена"
//                if (repairCheckBox.isSelected()) {
//                    mSelectedBarrel.setRecentlyReplaced(true);
//                } else {
//                    //иначе - сбрасываем настройку "недавно заменена"
//                    // выбранной бочке
//                    mSelectedBarrel.setRecentlyReplaced(false);
//                }
//                //если был включен чекбокс "чистка",
//                //делаем настройку выбранной бочке:
//                //обновляем дату последней чистки
//                if (cleanCheckBox.isSelected()) {
//                    mSelectedBarrel.setLastCDate(Date.from((createdDataPicker
//                        .getValue()
//                        .atStartOfDay()
//                        .atZone(ZoneId.systemDefault())).toInstant())
//                            
//                    );
//                }
//                mBarrelsDAOImpl.updateBarrel(mSelectedBarrel);
                
                //Сообщение пользователю об успешном добавлении бочки в БД
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Информация");
                alert.setHeaderText("Бочка успешно добавлена");
                //Показать сообщение и ждать
                alert.showAndWait();
                //Приводим форму в исходное состояние
                resetForm();
                //Вызываем обновление данных в коллекции-источнике для
                //таблицы бочек (в представлении бочек)
                WS1.barrelsControllerInstance.updateBarrelsForPage();
                //нотифицировать контроллер добавления доставок
                WS1.addSaleControllerInstance.updateBarrels();
                WS1.salesControllerInstance.updateBarrels();
            }
//            securityNameCTextField.promptTextProperty()
//                                .setValue("security name");
//            quantityCTextField.promptTextProperty()
//                                .setValue("quantity");
//            priceCTextField.promptTextProperty()
//                                .setValue("price");
//            securityNameCTextField.setStyle("-fx-border-color:lightgray;");
//            quantityCTextField.setStyle("-fx-border-color:lightgray;");
//            priceCTextField.setStyle("-fx-border-color:lightgray;");
            
            //System.out.println(securityNameCTextField.getText());
            
            //Добавление записи в таблицу "Продажи"
//            Sale newSale = new Sale();
//            newSale.setPrice((int)(Double.parseDouble(priceCTextField.getText()) * 100));
//            newSale.setQuantity(Integer.parseInt(quantityCTextField.getText()));
//            newSale.setSecurityName(securityNameCTextField.getText());
//            remoteServiceRemote.addSale(newSale, 1, 1);
            
//            securityNameCTextField.textProperty().setValue("");
//            quantityCTextField.textProperty().setValue("");
//            priceCTextField.textProperty().setValue("");
            
        } else {
            
            //Если хотя бы одно из валидируемых полей не заполнено,
            //показываем окно ошибки "Все поля должны быть заполнены!"
            
            String errorsString = "";
            
            for (ValidationMessage validationMessage : validationMessageList) {
                
                //System.out.println(validationMessage.getText());
                errorsString += "поле \""
                        + ((CustomTextField)validationMessage.getTarget()).getPromptText()
                        + "\": "
                        + validationMessage.getText()
                        + ". ";
            }
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Доставка не добавлена");
            alert.setContentText(errorsString);
            alert.showAndWait();
                        
//            String currentControlIdString;
//            for (ValidationMessage validationMessage : validationMessageList) {
//                currentControlIdString =
//                        ((CustomTextField)validationMessage.getTarget()).getId();
//                switch(currentControlIdString){
//                    case "securityNameCTextField" : {
//                        securityNameCTextField.promptTextProperty()
//                                .setValue(validationMessage.getText());
//                        //securityNameCTextField.getStyleClass().remove("custom-text-field");
//                        //securityNameCTextField.getStyleClass().add("error-c-text-field");
//                        securityNameCTextField.setStyle("-fx-border-color:red;");
//                        break;
//                    }
//                    case "quantityCTextField" : {
//                        quantityCTextField.promptTextProperty()
//                                .setValue(validationMessage.getText());
//                        quantityCTextField.setStyle("-fx-border-color:red;");
//                        break;
//                    }
//                    case "priceCTextField" : {
//                        priceCTextField.promptTextProperty()
//                                .setValue(validationMessage.getText());
//                        priceCTextField.setStyle("-fx-border-color:red;");
//                        break;
//                    }
//                }
                
                //System.out.println(((CustomTextField)validationMessage.getTarget()).getId());
                
            //}
        }
    }
    
    //Приведение формы в исходное состояние
    private void resetForm(){
        
        //очистка элементов ввода
        shopCTextField.clear();
        
        
        priceCTextField.clear();
        typeCTextField.clear();
        capacityCTextField.clear();
        countCTextField.clear();
        //replaceCheckBox.setSelected(true);
        lastCleanDatePicker.setValue(LocalDate.now());
        
        //по умолчанию предполагается разрядность водомера 6 знаков
        positionLabel.setText("не более 6 знаков");
        
        //Подключение автодополнения к полям с наборами вариантов выбора
        mShopNamesSet.clear();
        for (Shop shop : mShops) {
            mShopNamesSet.add(shop.getName());
        }
        if (mShopAutoCompletionBinding != null) {
            
            mShopAutoCompletionBinding.dispose();
        }
        mShopAutoCompletionBinding = TextFields.bindAutoCompletion(shopCTextField, mShopNamesSet);
        
        mWaterTypesSet.clear();
        for (WaterType waterType : mWaterTypes) {
            mWaterTypesSet.add(waterType.getName());
        }
        if (mWaterTypeAutoCompletionBinding != null) {
            
            mWaterTypeAutoCompletionBinding.dispose();
        }
        mWaterTypeAutoCompletionBinding = TextFields.bindAutoCompletion(typeCTextField, mWaterTypesSet);
        
        mBarrelCapacitiesSet.clear();
        for (BarrelCapacity barrelCapacity : mBarrelCapacities) {
            mBarrelCapacitiesSet.add(String.valueOf(barrelCapacity.getCapacity()));
        }
        if (mBarrelCapacityAutoCompletionBinding != null) {
            
            mBarrelCapacityAutoCompletionBinding.dispose();
        }
        mBarrelCapacityAutoCompletionBinding = TextFields.bindAutoCompletion(capacityCTextField, mBarrelCapacitiesSet);
    }
    
    //Обновление списка магазинов (вызывается извне,
    //если магазин был добавлен или деактивирован)
    public void updateShops(){
        
        mShops = mShopsDAOImpl.getAllShops();
        resetForm();
    }
    
    public void updateWaterTypes(){
    
        List<WaterType> waterTypesTmp = mWaterTypesDAOImpl.getAllWaterTypes();
        if (waterTypesTmp == null) {
            
            waterTypesTmp = new ArrayList<>();
        }
        mWaterTypes =
            waterTypesTmp.stream()
                .filter(wType -> ((WaterType)wType).getActive())
                .collect(Collectors.toList());
        if (mWaterTypes == null) {
            
            mWaterTypes = new ArrayList<>();
        }
        resetForm();
    }
    
    public void updateBarrelCapacities(){
    
        List<BarrelCapacity> barrelCapacitiesTmp =
                mBarrelCapacitiesDAOImpl.getAllBarrelCapacities();
        if (barrelCapacitiesTmp == null) {
            
            barrelCapacitiesTmp = new ArrayList<>();
        }
        mBarrelCapacities =
                barrelCapacitiesTmp.stream()
                .filter(bCap -> ((BarrelCapacity)bCap).getActive())
                .collect(Collectors.toList());
        if (mBarrelCapacities == null) {
            
            mBarrelCapacities = new ArrayList<>();
        }
        resetForm();
    }
}
