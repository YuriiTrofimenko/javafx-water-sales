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
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;
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
import org.tyaa.ws.dao.impl.SettingsDAOImpl;
import org.tyaa.ws.entity.Shop;
import org.tyaa.ws.entity.Barrel;
import org.tyaa.ws.dao.impl.WaterTypesDAOImpl;
import org.tyaa.ws.entity.BarrelCapacity;
import org.tyaa.ws.entity.WaterType;
import org.tyaa.ws.viewmodel.BarrelCapacityModel;
import org.tyaa.ws.viewmodel.WaterTypeModel;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class SettingsController implements Initializable, ControlledScreen {

    /*Внедренные хендлеры элементов UI*/
    
    //
    @FXML
    ListView waterTypesListView;
    @FXML
    ListView capacitiesListView;
    
    @FXML
    CustomTextField waterTypeCTextField;
    @FXML
    CustomTextField capacityCTextField;
    @FXML
    CustomTextField сleaningTCTimeCTextField;
    @FXML
    CustomTextField сleaningOCTimeCTextField;
    @FXML
    CustomTextField allowedRestPercent;
    
    /*@FXML
    Button waterTypesAddButton;
    @FXML
    Button waterTypesDeleteButton;
    @FXML
    Button capacityAddButton;
    @FXML
    Button capacityDeleteButton;*/
    /*@FXML
    CheckBox replaceCheckBox;
    @FXML
    DatePicker lastCleanDatePicker;*/
    
    //Объекты доступа к данным
    //private SalesDAOImpl mSalesDAOImpl;
    //private static ShopsDAOImpl mShopsDAOImpl;
    //private BarrelsDAOImpl mBarrelsDAOImpl;
    private BarrelCapacitiesDAOImpl mBarrelCapacitiesDAOImpl;
    private WaterTypesDAOImpl mWaterTypesDAOImpl;
    
    private SettingsDAOImpl mSettingsDAOImpl;
    //private DriversDAOImpl mDriversDAOImpl;
    //private CarsDAOImpl mCarsDAOImpl;
    
    //Списки объектов
    //private static List<Shop> mShops;
    //private List<Barrel> mBarrels;
    //private List<Barrel> mShopBarrels;
    private List<WaterType> mWaterTypes;
    private List<BarrelCapacity> mBarrelCapacities;
    
    //Наборы полей из объектов
    //private Set<String> mShopNamesSet;
    //private Set<String> mWaterTypesSet;
    //private Set<String> mBarrelCapacitiesSet;
    
    //Выбранные объекты
    //private Shop mSelectedShop;
    //private Barrel mSelectedBarrel;
    //private WaterType mSelectedWaterType;
    //private BarrelCapacity mSelectedBarrelCapacity;
    
    ObservableList<WaterTypeModel> mWaterTypesObservableList;
    ObservableList<BarrelCapacityModel> mBarrelCapacitiesObservableList;
    
    ScreensController myController;
    ValidationSupport validationSupport;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //WS1.addBarrelControllerInstance = this;
        //System.out.println("SettingsController");
        //mSalesDAOImpl = new SalesDAOImpl();
        //mShopsDAOImpl = new ShopsDAOImpl();
        //mBarrelsDAOImpl = new BarrelsDAOImpl();
        mBarrelCapacitiesDAOImpl = new BarrelCapacitiesDAOImpl();
        mWaterTypesDAOImpl = new WaterTypesDAOImpl();
        
        mSettingsDAOImpl = new SettingsDAOImpl();
        //mShops = mShopsDAOImpl.getAllShops();
        //mShopNamesSet = new HashSet<>();
        
        //mBarrels = mBarrelsDAOImpl.getAllBarrels();
        //mShopBarrels = new ArrayList();
        
        /*mWaterTypes = mWaterTypesDAOImpl.getAllWaterTypes();
        mWaterTypesSet = new HashSet<>();
        
        mBarrelCapacities = mBarrelCapacitiesDAOImpl.getAllBarrelCapacities();
        mBarrelCapacitiesSet = new HashSet<>();*/
        
        mWaterTypesObservableList = FXCollections.observableArrayList();
        mBarrelCapacitiesObservableList = FXCollections.observableArrayList();
        
        waterTypesListView.setItems(mWaterTypesObservableList);
        capacitiesListView.setItems(mBarrelCapacitiesObservableList);
        
        //updateWaterTypes();
        //updateBarrelCapacities();
        
        /*Приведение формы в исходное состояние*/
        
        resetForm();
        
        allowedRestPercent.setText(String.valueOf((int)(Settings.getAllowedRestPecent() * 100)));
        сleaningOCTimeCTextField.setText(String.valueOf(Settings.getCleaningOverdueCycleTime() / 2592000000D));
        сleaningTCTimeCTextField.setText(String.valueOf(Settings.getCleaningTypicalCycleTime() / 2592000000D));

        //Активация механизма валидации для элементов ввода типа CustomTextField
        ValueExtractor.addObservableValueExtractor(
                c -> c instanceof CustomTextField
                , c -> ((CustomTextField) c).textProperty());
        validationSupport = new ValidationSupport();
        //Явная настройка включения визуального оформления валидации
        validationSupport.setErrorDecorationEnabled(true);
        //Настройки валидации для каждого элемента ввода, подлежащего проверке
        validationSupport.registerValidator(
            waterTypeCTextField
            , Validator.createRegexValidator("Введите от одного до 50 символов", ".{1,50}", Severity.ERROR));
        validationSupport.registerValidator(
            capacityCTextField
            , Validator.createRegexValidator("Введите целое число от 0 до 9999", "[0-9]{1,4}", Severity.ERROR));
        /*validationSupport.registerValidator(
                priceCTextField
                , Validator.createRegexValidator("Введите целое или дробное число (максимум - 99999.99)", "[0-9]{1,5}[.]{0,1}[0-9]{0,2}", Severity.ERROR));
        validationSupport.registerValidator(
                typeCTextField
                , Validator.createEmptyValidator("Тип бочки обязателен"));
        validationSupport.registerValidator(
                capacityCTextField
                , Validator.createRegexValidator("Введите целое число от 0 до 9999", "[0-9]{1,4}", Severity.ERROR));
        validationSupport.registerValidator(
                countCTextField
                , Validator.createRegexValidator("Введите целое число от 0 до 99999", "[0-9]{1,5}", Severity.ERROR));*/
        
        /*Настраиваем способ отображения информации из объектов,
        подключенных в составе коллекции-источника к контролам списочного типа*/
        
        waterTypesListView.setCellFactory((listView) -> {
        
            return new ListCell<WaterTypeModel>(){
                @Override
                protected void updateItem(WaterTypeModel item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });
        
        capacitiesListView.setCellFactory((listView) -> {
        
            return new ListCell<BarrelCapacityModel>(){
                @Override
                protected void updateItem(BarrelCapacityModel item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(String.valueOf(item.getCapacity()));
                    }
                }
            };
        });
        
//        waterTypesListView.setConverter(new StringConverter<WaterType>() {
//            
//            @Override
//            public String toString(WaterType _waterType) {
//                if (_waterType == null) {
//                    return null;
//                } else {
//                    return (_waterType.getName());
//                }
//            }
//            @Override
//                public WaterType fromString(String _waterTypeString) {
//                    return null; // No conversion fromString needed.
//                }
//            });
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
    private void addWaterType(ActionEvent event){
       
        Pattern waterTypePattern = Pattern.compile(".{1,50}");
        String newWaterTypeNameString = waterTypeCTextField.textProperty().getValue();
        if (waterTypePattern.matcher(newWaterTypeNameString).matches()) {
            
            boolean existFlag = false;
            for (WaterType waterType : mWaterTypes) {
                
                if (newWaterTypeNameString.equals(waterType.getName())) {
                    
                    existFlag = true;
                }
            }
            if (!existFlag) {
                
                WaterType newWaterType = new WaterType();
                newWaterType.setName(newWaterTypeNameString);
                newWaterType.setActive(true);
                mWaterTypesDAOImpl.createWaterType(newWaterType);
                updateWaterTypes();
                WS1.addBarrelControllerInstance.updateWaterTypes();
            }
        }
    }
    
    @FXML
    private void deleteWaterType(ActionEvent event){
       
        WaterTypeModel selectedWaterTypeModel =
            (WaterTypeModel) waterTypesListView.getSelectionModel().getSelectedItem();
        if (selectedWaterTypeModel != null) {
            
            WaterType selectedWaterType =
                mWaterTypesDAOImpl.getWaterType(selectedWaterTypeModel.getId());
            
            selectedWaterType.setActive(false);
            mWaterTypesDAOImpl.updateWaterType(selectedWaterType);
            updateWaterTypes();
            WS1.addBarrelControllerInstance.updateWaterTypes();
        }
    }
    
    @FXML
    private void addCapacity(ActionEvent event){
       
        Pattern capacityPattern = Pattern.compile("[0-9]{1,4}");
        String newCapacityString = capacityCTextField.textProperty().getValue();
        if (capacityPattern.matcher(newCapacityString).matches()) {
            
            boolean existFlag = false;
            for (BarrelCapacity _barrelCapacity : mBarrelCapacities) {
                
                if (newCapacityString.equals(String.valueOf(_barrelCapacity.getCapacity()))) {
                    
                    existFlag = true;
                }
            }
            if (!existFlag) {
                
                BarrelCapacity newBarrelCapacity = new BarrelCapacity();
                newBarrelCapacity.setCapacity(Integer.valueOf(newCapacityString));
                newBarrelCapacity.setActive(true);
                mBarrelCapacitiesDAOImpl.createBarrelCapacity(newBarrelCapacity);
                updateBarrelCapacities();
                WS1.addBarrelControllerInstance.updateBarrelCapacities();
            }
        }
    }
    
    @FXML
    private void deleteCapacity(ActionEvent event){
       
        BarrelCapacityModel selectedBarrelCapacityModel =
            (BarrelCapacityModel) capacitiesListView.getSelectionModel().getSelectedItem();
        if (selectedBarrelCapacityModel != null) {
            
            BarrelCapacity selectedBarrelCapacity =
                mBarrelCapacitiesDAOImpl.getBarrelCapacity(selectedBarrelCapacityModel.getId());
            
            selectedBarrelCapacity.setActive(false);
            mBarrelCapacitiesDAOImpl.updateBarrelCapacity(selectedBarrelCapacity);
            updateBarrelCapacities();
            WS1.addBarrelControllerInstance.updateBarrelCapacities();
        }
    }
    
    @FXML
    private void setCleaningTypicalCycleTime(ActionEvent event){
       
        Pattern cTCPattern = Pattern.compile("[1][0-2]([/.][0-9]){0,1}");
        Pattern cTCPattern2 = Pattern.compile("[1-9]([/.][0-9]){0,1}");
        String cTCString = сleaningTCTimeCTextField.textProperty().getValue().replaceAll(",", ".");
        if (cTCPattern.matcher(cTCString).matches()
                || cTCPattern2.matcher(cTCString).matches()) {
        
            mSettingsDAOImpl.updateSettings(
                new org.tyaa.ws.entity.Settings(
                    1
                    , "cleaning_period"
                    , String.valueOf((long)(Float.valueOf(cTCString) * 2592000000L)))
            );
            Settings.setCleaningTypicalCycleTime((long)(Float.valueOf(cTCString) * 2592000000L));
            WS1.barrelsControllerInstance.updateBarrelsForPage();
        } else {
        
            сleaningTCTimeCTextField.setText(String.valueOf(Settings.getCleaningTypicalCycleTime() / 2592000000D));
        }
    }
    
    @FXML
    private void setCleaningOverdueCycleTime(ActionEvent event){
       
        Pattern cOCPattern = Pattern.compile("[1][0-2]([/.][0-9]){0,1}");
        Pattern cOCPattern2 = Pattern.compile("[1-9]([/.][0-9]){0,1}");
        String cOCString = сleaningOCTimeCTextField.textProperty().getValue().replaceAll(",", ".");
        if (cOCPattern.matcher(cOCString).matches()
                || cOCPattern2.matcher(cOCString).matches()) {
        
            mSettingsDAOImpl.updateSettings(
                new org.tyaa.ws.entity.Settings(
                    2
                    , "cleaning_period_over"
                    , String.valueOf((long)(Float.valueOf(cOCString) * 2592000000L)))
            );
            Settings.setCleaningOverdueCycleTime((long)(Float.valueOf(cOCString) * 2592000000L));
            WS1.barrelsControllerInstance.updateBarrelsForPage();
        } else {
        
            сleaningOCTimeCTextField.setText(String.valueOf(Settings.getCleaningOverdueCycleTime() / 2592000000D));
        }
    }
    
    @FXML
    private void setAllowedRestPercent(ActionEvent event){
       
        Pattern aRPPattern = Pattern.compile("[1-9]");
        Pattern aRPPattern2 = Pattern.compile("[1-9][0-9]");
        String aRPString = allowedRestPercent.textProperty().getValue();
        if (aRPPattern.matcher(aRPString).matches()
                || aRPPattern2.matcher(aRPString).matches()) {
        
            //Обновляем параметр в БД
            mSettingsDAOImpl.updateSettings(
                new org.tyaa.ws.entity.Settings(
                    3
                    , "allowed_rest_percent"
                    , String.valueOf((float)Integer.valueOf(aRPString) / 100F)
                )
            );
            //... и в глобальном объекте
            Settings.setAllowedRestPecent((float)Integer.valueOf(aRPString) / 100F);
            WS1.barrelsControllerInstance.updateBarrelsForPage();
        } else {
        
            allowedRestPercent.setText(String.valueOf(Settings.getAllowedRestPecent() * 100));
        }
    }
    
    @FXML
    private void goToSalesScreen(ActionEvent event){
       myController.setScreen(WS1.salesID);
       WS1.primaryStage.setMaximized(true);
    }
    
    //Приведение формы в исходное состояние
    private void resetForm(){
        
        waterTypeCTextField.setText("");
        capacityCTextField.setText("");
        
        //mWaterTypes = mWaterTypesDAOImpl.getAllWaterTypes();
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
        
        //mBarrelCapacities = mBarrelCapacitiesDAOImpl.getAllBarrelCapacities();
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
        
        /*Fill observable collections*/
        
        mWaterTypesObservableList.clear();
        
        if (mWaterTypes == null) {
         
            mWaterTypes = new ArrayList<>();
        }
            
        for (WaterType waterType : mWaterTypes) {

            mWaterTypesObservableList.add(
                new WaterTypeModel(waterType.getId(), waterType.getName())
            );
        }
        
        mBarrelCapacitiesObservableList.clear();
        
        if (mBarrelCapacities == null) {
         
            mBarrelCapacities = new ArrayList<>();
        }
            
        for (BarrelCapacity barrelCapacity : mBarrelCapacities) {

            mBarrelCapacitiesObservableList.add(new BarrelCapacityModel(
                    barrelCapacity.getId(), barrelCapacity.getCapacity()
                )
            );
        }
        
        //очистка элементов ввода
        /*shopCTextField.clear();
        
        
        priceCTextField.clear();
        typeCTextField.clear();
        capacityCTextField.clear();
        countCTextField.clear();
        replaceCheckBox.setSelected(true);
        lastCleanDatePicker.setValue(LocalDate.now());*/
        
        //Подключение автодополнения к полям с наборами вариантов выбора
        /*mShopNamesSet.clear();
        for (Shop shop : mShops) {
            mShopNamesSet.add(shop.getName());
        }
        TextFields.bindAutoCompletion(shopCTextField, mShopNamesSet);
        
        mWaterTypesSet.clear();
        for (WaterType waterType : mWaterTypes) {
            mWaterTypesSet.add(waterType.getName());
        }
        TextFields.bindAutoCompletion(typeCTextField, mWaterTypesSet);
        
        mBarrelCapacitiesSet.clear();
        for (BarrelCapacity barrelCapacity : mBarrelCapacities) {
            mBarrelCapacitiesSet.add(String.valueOf(barrelCapacity.getCapacity()));
        }
        TextFields.bindAutoCompletion(capacityCTextField, mBarrelCapacitiesSet);*/
    }
    
    public void updateWaterTypes(){
        
        mWaterTypes = mWaterTypesDAOImpl.getAllWaterTypes();
        resetForm();
    }
    
    public void updateBarrelCapacities(){
        
        mBarrelCapacities = mBarrelCapacitiesDAOImpl.getAllBarrelCapacities();
        resetForm();
    }
}
