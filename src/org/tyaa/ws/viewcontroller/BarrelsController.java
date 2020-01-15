/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.viewcontroller;

import java.io.IOException;
import java.math.BigDecimal;
import org.tyaa.ws.screensframework.ControlledScreen;
import org.tyaa.ws.screensframework.ScreensController;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.tyaa.ws.WS1;
import org.tyaa.ws.common.Globals;
import org.tyaa.ws.common.Settings;
import org.tyaa.ws.dao.BarrelsFacade;
import org.tyaa.ws.dao.impl.BarrelCapacitiesDAOImpl;
import org.tyaa.ws.dao.impl.BarrelsDAOImpl;
import org.tyaa.ws.dao.impl.ShopsDAOImpl;
import org.tyaa.ws.entity.Barrel;
import org.tyaa.ws.entity.Shop;
import org.tyaa.ws.util.ExcellWriter;
import org.tyaa.ws.util.model.DayTaskItem;
import org.tyaa.ws.util.model.ShopPricesItem;
import org.tyaa.ws.viewmodel.BarrelModel;
import org.tyaa.ws.viewmodel.ShopModel;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class BarrelsController implements Initializable, ControlledScreen {

    @FXML
    private TableView barrelsTableView;
    
    @FXML
    private TableColumn barrelIdTableColumn;
    @FXML
    private TableColumn numTableColumn;
    @FXML
    private TableColumn barrelShopTableColumn;
    @FXML
    private TableColumn barrelTypeTableColumn;
    @FXML
    private TableColumn barrelCapacityTableColumn;
    @FXML
    private TableColumn barrelPriceTableColumn;
    @FXML
    private TableColumn barrelCountTableColumn;
    @FXML
    private TableColumn allowedRestTableColumn;
    @FXML
    private TableColumn barrelCleanDateTableColumn;
    @FXML
    private TableColumn barrelReplaceTableColumn;
    @FXML
    private TableColumn barrelNeedChargeTableColumn;
    
    //Edit buttons
    @FXML
    private Button barrelEditARButton;
    @FXML
    private Button barrelEditPriceButton;
    @FXML
    private Button barrelEditPositionsButton;
    @FXML
    private Button barrelEditCounterButton;
    @FXML
    private Button barrelEditLastCDateButton;
    @FXML
    private Button barrelEditPeriodButton;
    @FXML
    private Button calcPeriodEnableButton;
    
    @FXML
    private Pagination barrelsPagination;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    /*For filter*/
    @FXML
    CheckBox filterCheckBox;
    @FXML
    CustomTextField shopCTextField;
    @FXML
    CheckBox needCleanCheckBox;
    @FXML
    CheckBox needChargeCheckBox;
    /*End*/
    
    ScreensController myController;
    
    private static ObservableList<BarrelModel> mBarrelsObservableList;
    
    private static List<Barrel> mBarrelsForPage;
    
    private static BarrelsDAOImpl mBarrelsDAOImpl;
    private static ShopsDAOImpl mShopsDAOImpl;
    private static BarrelCapacitiesDAOImpl mBarrelCapacitiesDAOImpl;
    
    private static BarrelsFacade mBarrelsFacade;
    
    /*For filter*/
    
    //for all shops list
    private List<Shop> mShops;
    
    //for allowed names lists
    private Set<String> mShopNamesSet;
    
    private AutoCompletionBinding<String> mShopAutoCompletionBinding;
    
    private Shop mSelectedShop;
    
    /*End*/
    
    //Объект выбранной строки таблицы
    private BarrelModel mSelectedBarrelModel;
    //Объект выбранной бочки
    private Barrel mSelectedBarrel;
    
    private static int mCurrentPageIdx;
    private static int mRowsPerPage = 25;
    
    private int mBarrelsCount;
    
    //
    private int mBarrelsAllCount = 0;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        WS1.barrelsControllerInstance = this;
        
        progressIndicator.visibleProperty().set(false);
        
        mBarrelsObservableList = FXCollections.observableArrayList();
        
        mBarrelsDAOImpl = new BarrelsDAOImpl();
        mShopsDAOImpl = new ShopsDAOImpl();
        mBarrelCapacitiesDAOImpl = new BarrelCapacitiesDAOImpl();
        
        mBarrelsFacade = new BarrelsFacade();
        
        mShops = mShopsDAOImpl.getAllShops();
        mShopNamesSet = new HashSet<>();
        
        resetFilterForm();
        
        //По умолчанию после загрузки приложения действие визуального фильтра отключено,
        //а действие фильтра "только активные" - включено
        mBarrelsFacade.setFilter(false);
        mBarrelsFacade.setFilterShopId(-1);
        mBarrelsFacade.setNeedClean(false);
        mBarrelsFacade.setNeedCharge(false);
        mBarrelsFacade.setNeedCleanOrCharge(false);
        mBarrelsFacade.setActive(1);
        
        mBarrelsAllCount = mBarrelsDAOImpl.getBarrelsCount();
        
        setPageCount();
                
        barrelsPagination.setPageFactory(
            new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {          
                    return createPage(pageIndex);               
                }
            }
        );
                
        barrelIdTableColumn.setCellValueFactory(
                new PropertyValueFactory<BarrelModel, String>("id")
        );
        barrelShopTableColumn.setCellValueFactory(
                new PropertyValueFactory<BarrelModel, String>("shopName")
        );
        barrelTypeTableColumn.setCellValueFactory(
                new PropertyValueFactory<BarrelModel, String>("type")
        );
        barrelCapacityTableColumn.setCellValueFactory(
                new PropertyValueFactory<BarrelModel, String>("capacity")
        );
        barrelPriceTableColumn.setCellValueFactory(
                new PropertyValueFactory<BarrelModel, String>("price")
        );
        barrelCountTableColumn.setCellValueFactory(
                new PropertyValueFactory<BarrelModel, String>("count")
        );
        allowedRestTableColumn.setCellValueFactory(
                new PropertyValueFactory<BarrelModel, String>("allowedRest")
        );
        barrelCleanDateTableColumn.setCellValueFactory(
                new PropertyValueFactory<BarrelModel, String>("cleanDate")
        );
        barrelReplaceTableColumn.setCellValueFactory(
                new PropertyValueFactory<BarrelModel, String>("replace")
        );
        barrelNeedChargeTableColumn.setCellValueFactory(
                new PropertyValueFactory<BarrelModel, String>("needChargeProperty")
        );
        
        //Вызов метода сквозной автонумерации строк в таблице
        setNumCellFactory();
        
        barrelReplaceTableColumn.setCellFactory(column ->{
            
            return new TableCell<ShopModel, Boolean>(){
                @Override
                protected void updateItem(Boolean item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        if (item == true) {
                            setText("Да");
                            setTextFill(Color.BLUE);
                        } else {
                            setText("Нет");
                            setTextFill(Color.BLACK);
                        }
                    }
                }
            };
        });
        
        //выводить дату последней чистки бочки желтым цветом,
        //если приближается время новой чистки, и красным - если просрочено
        barrelCleanDateTableColumn.setCellFactory(column ->{
            
            return new TableCell<ShopModel, String>(){
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        SimpleDateFormat formatter =
                            new SimpleDateFormat("yyyy.MM.dd");
                        SimpleDateFormat formatter2 =
                            new SimpleDateFormat("dd.MM.yyyy");
                        Date date = null;
                        try {
                            date = formatter.parse(item);
                        } catch (ParseException ex) {
                            setText("Неверный формат даты");
                        }
                        if (date != null) {
                            setText(formatter2.format(date));
                            if (
                                (
                                    Date.from(Instant.now()).getTime()
                                    - date.getTime()
                                ) < Settings.getCleaningTypicalCycleTime()
                            ) {
                                setTextFill(Color.BLACK);
                            } else if(
                                (
                                    Date.from(Instant.now()).getTime()
                                    - date.getTime()
                                ) < Settings.getCleaningOverdueCycleTime()){
                                setTextFill(Color.ORANGE);
                            } else {
                                setTextFill(Color.RED);
                            }
                        }
                    }
                }
            };
        });
        
        //выводить состояние необходимости заправки бочки цветами,
        //указанными в комментариях класса org.tyaa.ws.common.BarrelWarning
        barrelNeedChargeTableColumn.setCellFactory(column ->{
            
            return new TableCell<BarrelModel, Integer>(){
                @Override
                protected void updateItem(Integer item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        /*int barrelWarningLevel = 0;
                        try {
                            barrelWarningLevel = Integer.valueOf(item);
                        } catch (NumberFormatException ex) {
                            barrelWarningLevel = 0;
                            setText("Неверный формат целого числа");
                        }*/
                        
                        
                        
                        //if (date != null) {
                            
                            if (item == 0) {
                                setText("Нет"); 
                                setTextFill(Color.BLACK);
                            } else if(item == 1) {
                                setText("Да");
                                setTextFill(Color.ORANGE);
                            } else if(item == 2) {
                                setText("Срочно");
                                setTextFill(Color.RED);
                            } else {
                            
                                setText("Новая");
                                setTextFill(Color.BLUE);
                            }
                        //}
                    }
                }
            };
        });
        
        //обработка события "переключение чекбокса Filter on/off"
        filterCheckBox.setOnAction((event) -> {
            if (filterCheckBox.isSelected()) {
                //устанавливаем в фасаде barrels режим "фильтр"
                mBarrelsFacade.setFilter(true);
            } else {
                mBarrelsFacade.setFilter(false);
            }
            updateBarrelsForPage();
        });
        
        //обработка события "ввод названия магазина"
        shopCTextField.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                progressIndicator.visibleProperty().set(true);
                
                mBarrelsObservableList.clear();
                
                if (!shopCTextField.textProperty().getValue().equals("")
                        && mShopNamesSet.contains(shopCTextField.textProperty().getValue())) {
                    
                    //получаем объект выбранного магазина
                    for (Shop shop : mShops) {
                        //System.out.println("shop");
                        if (shop.getName().equals(shopCTextField.textProperty().getValue())) {
                            mSelectedShop = shop;
                            break;
                        }
                    }
                    
                    //устанавливаем в фасаде barrels значение "ID магазина" для фильтра;
                    //вызываем в фасаде barrels срабатывание фильтра
                    
                    mBarrelsFacade.setFilterShopId(mSelectedShop.getId());
                    
                    
                    /*//показываем в выпадающем списке бочек те,
                    //которые соответствуют выбранному магазину
                    for (Barrel barrel : mBarrels) {
                        if (Objects.equals(barrel.getShopId(), mSelectedShop.getId())) {
                            mShopBarrels.add(barrel);
                        }
                    }
                    for (Barrel barrel : mShopBarrels) {
                        mBarrelsObservableList.add(barrel);
                    }
                    if (mBarrelsObservableList.size() > 0) {
                        barrelComboBox.setItems(mBarrelsObservableList);
                    }*/
                } else {
                    
                    //иначе отправляем фасаду аргумент-сигнал "не выбран магазин"
                    mBarrelsFacade.setFilterShopId(-1);
                    //mSalesFacade.setFilterBarrelId(-1);
                    //updateSalesForPage();
                    //TODO temporary!
                    //mSalesFacade.setFilter(false);
                }
                
                //обновляем содержимое страницы таблицы полученными данными
                updateBarrelsForPage();
                
                /*mShopBarrels.clear();
                mBarrelsObservableList.clear();
                
                //Выбираем бочки, относящиеся к выбранному магазину
                if (!shopCTextField.textProperty().getValue().equals("")
                        && mShopNamesSet.contains(shopCTextField.textProperty().getValue())) {
                    for (Shop shop : mShops) {
                        //System.out.println("shop");
                        if (shop.getName().equals(shopCTextField.textProperty().getValue())) {
                            mSelectedShop = shop;
                            break;
                        }
                    }
                    
                    //barrelComboBox.getItems().clear();
                    for (Barrel barrel : mBarrels) {
                        if (Objects.equals(barrel.getShopId(), mSelectedShop.getId())) {
                            mShopBarrels.add(barrel);
                        }
                    }
                    for (Barrel barrel : mShopBarrels) {
//                        mBarrelsObservableList.add(
//                            barrel.getWhaterTId().getName()
//                                + ", "
//                                + mBarrelCapacitiesDAOImpl.getBarrelCapacity(
//                                        barrel.getCapacityId()
//                                ).getCapacity()
//                                + " л"
//                        );
                        mBarrelsObservableList.add(barrel);
                        //System.out.println(barrel.getCapacityId());
                    }
                    if (mBarrelsObservableList.size() > 0) {
                        barrelComboBox.setItems(mBarrelsObservableList);
                    }
                }*/
                progressIndicator.visibleProperty().set(false);
            }
        });
        
        //обработка события "переключение чекбокса NeedClean on/off"
        needCleanCheckBox.setOnAction((event) -> {
            
            progressIndicator.visibleProperty().set(true);
            
            if (needCleanCheckBox.isSelected()) {
                mBarrelsFacade.setNeedClean(true);
            } else {
                mBarrelsFacade.setNeedClean(false);
            }
            updateBarrelsForPage();
            progressIndicator.visibleProperty().set(false);
        });
        
        //обработка события "переключение чекбокса NeedCharge on/off"
        needChargeCheckBox.setOnAction((event) -> {
            
            progressIndicator.visibleProperty().set(true);
            
            if (needChargeCheckBox.isSelected()) {
                mBarrelsFacade.setNeedCharge(true);
            } else {
                mBarrelsFacade.setNeedCharge(false);
            }
            updateBarrelsForPage();
            progressIndicator.visibleProperty().set(false);
        });
    }
    
    

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
    
    @FXML
    private void goToSalesScreen(ActionEvent event){
       myController.setScreen(WS1.salesID);
    }
    
    @FXML
    private void goToShopsScreen(ActionEvent event){
       myController.setScreen(WS1.shopsID);
    }
    
    @FXML
    private void goToCarsScreen(ActionEvent event){
       myController.setScreen(WS1.carsID);
    }
    
    @FXML
    private void goToDriversScreen(ActionEvent event){
       myController.setScreen(WS1.driversID);
    }
    
    @FXML
    private void goToDebtsScreen(ActionEvent event){
       myController.setScreen(WS1.debtsID);
    }
    
    @FXML
    private void goToAddBarrelScreen(ActionEvent event){
       myController.setScreen(WS1.addBarrelID);
       WS1.primaryStage.setMaximized(false);
       WS1.primaryStage.setWidth(600);
       WS1.primaryStage.setHeight(570);
       WS1.primaryStage.setY(30);
    }
    
    //Обработчик кнопки "Удалить"
    @FXML
    private void deleteBarrel(ActionEvent event){
       
        progressIndicator.visibleProperty().set(true);
        
        mSelectedBarrelModel =
                (BarrelModel) barrelsTableView.getSelectionModel().getSelectedItem();
        
        if (mSelectedBarrelModel != null) {
            
            mSelectedBarrel =
                mBarrelsDAOImpl.getBarrel(mSelectedBarrelModel.getId());
            
            mSelectedBarrel.setActive(false);
            
            mBarrelsDAOImpl.updateBarrel(mSelectedBarrel);
            
            //Notify self
            updateBarrelsForPage();
            //нотифицировать контроллер добавления доставок
            WS1.addSaleControllerInstance.updateBarrels();
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одина бочка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showBarrelEditARDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedBarrelModel =
                (BarrelModel) barrelsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedBarrelModel != null) {
            
            mSelectedBarrel =
                mBarrelsDAOImpl.getBarrel(mSelectedBarrelModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(String.valueOf(mSelectedBarrel.getAllowedRest()));
            dialog.setTitle("Изменение допустимого остатка");
            dialog.setHeaderText("Какой минимально допустимый остаток воды в бочке?");
            dialog.setContentText("Укажите положительное число в литрах: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                changeAllowedRestString -> {

                    boolean hasNumFormatErrors = false;
                    //boolean tooMuchErrors = false;
                    
                    try{
                    
                        Pattern pattern = 
                            Pattern.compile("[0-9]{1,4}");
                        if (pattern.matcher(changeAllowedRestString).matches()
                            && Integer.valueOf(changeAllowedRestString)
                                <= mBarrelCapacitiesDAOImpl
                                    .getBarrelCapacity(
                                            mSelectedBarrel.getCapacityId()).getCapacity()
                                ) {

                                mSelectedBarrel.setAllowedRest(Integer.valueOf(changeAllowedRestString));
                                mBarrelsDAOImpl.updateBarrel(mSelectedBarrel);
                                //Notify self
                                updateBarrelsForPage();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateBarrels();
                        } else {

                            hasNumFormatErrors = true;
                        }
                    } catch (NumberFormatException ex){

                        hasNumFormatErrors = true;
                    }
                    if (hasNumFormatErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение допустимого остатка воды не выполнено");
                        alert.setContentText("Ошибка формата числа (максимум 9999) или число больше полного объема бочки");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна бочка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showBarrelEditPriceDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedBarrelModel =
                (BarrelModel) barrelsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedBarrelModel != null) {
            
            mSelectedBarrel =
                mBarrelsDAOImpl.getBarrel(mSelectedBarrelModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(String.valueOf(mSelectedBarrel.getPrice()));
            dialog.setTitle("Изменение цены");
            dialog.setHeaderText("Какая цена воды за литр для данной бочки?");
            dialog.setContentText("Укажите целое или дробное положительное число в гривнах: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                priceString -> {

                    boolean hasNumFormatErrors = false;
                    //boolean tooMuchErrors = false;
                    
                    try{
                    
                        Pattern pattern = 
                            Pattern.compile("[0-9]{1,5}[.,]{0,1}[0-9]{0,2}");
                        if (pattern.matcher(priceString).matches()) {

                                mSelectedBarrel.setPrice(
                                    BigDecimal.valueOf(Double.parseDouble(priceString.replaceAll(",", ".")))
                                );
                                mBarrelsDAOImpl.updateBarrel(mSelectedBarrel);
                                //Notify self
                                updateBarrelsForPage();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateBarrels();
                        } else {

                            hasNumFormatErrors = true;
                        }
                    } catch (NumberFormatException ex){

                        hasNumFormatErrors = true;
                    }
                    if (hasNumFormatErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение цены за литр воды не выполнено");
                        alert.setContentText("Ошибка формата числа: нужно целое или дробное положительное число (максимум - 99999.99)");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна бочка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showBarrelEditLastCDateDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedBarrelModel =
                (BarrelModel) barrelsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedBarrelModel != null) {
            
            mSelectedBarrel =
                mBarrelsDAOImpl.getBarrel(mSelectedBarrelModel.getId());
            //
            TextInputDialog dialog =
                new TextInputDialog(new SimpleDateFormat("dd/MM/yyyy").format(mSelectedBarrel.getLastCDate()));
            dialog.setTitle("Изменение даты последней мойки");
            dialog.setHeaderText("Когда бочка мылась в последний раз?");
            dialog.setContentText("Введите дату последней мойки в формате дд/мм/гггг: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                lastCDateString -> {

                    boolean hasErrors = false;                   
                    try{
                    
                        Pattern pattern = 
                            Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d");
                        if (pattern.matcher(lastCDateString).matches()) {

                                SimpleDateFormat simpleDateFormat =
                                    new SimpleDateFormat("dd/MM/yyyy");
                                mSelectedBarrel.setLastCDate(simpleDateFormat.parse(lastCDateString));
                                mBarrelsDAOImpl.updateBarrel(mSelectedBarrel);
                                //Разрешаем пересчет необходимой периодичности мойки бочек
                                Globals.mAllowCalcPeriod = true;
                                //Notify self
                                updateBarrelsForPage();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateBarrels();
                        } else {

                            hasErrors = true;
                        }
                    } catch (ParseException ex){

                        hasErrors = true;
                    }
                    if (hasErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение даты последней мойки не выполнено");
                        alert.setContentText("Ошибка формата даты: нужна строка по шаблону дд/мм/гггг");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна бочка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showBarrelEditPositionsDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedBarrelModel =
                (BarrelModel) barrelsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedBarrelModel != null) {
            
            mSelectedBarrel =
                mBarrelsDAOImpl.getBarrel(mSelectedBarrelModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(String.valueOf(mSelectedBarrel.getPositions()));
            dialog.setTitle("Изменение разрядности");
            dialog.setHeaderText("Какое число знаков водомера у данной бочки?");
            dialog.setContentText("Введите целое число 5 или 6 или 7: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                positionsString -> {

                    boolean hasNumFormatErrors = false;
                    //boolean tooMuchErrors = false;
                    
                    try{
                    
                        Pattern pattern = 
                            Pattern.compile("[5-7]");
                        if (pattern.matcher(positionsString).matches()) {

                                mSelectedBarrel.setPositions(
                                    Integer.parseInt(positionsString)
                                );
                                mBarrelsDAOImpl.updateBarrel(mSelectedBarrel);
                                //Notify self
                                updateBarrelsForPage();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateBarrels();
                        } else {

                            hasNumFormatErrors = true;
                        }
                    } catch (NumberFormatException ex){

                        hasNumFormatErrors = true;
                    }
                    if (hasNumFormatErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение максимального числа знаков водомера не выполнено");
                        alert.setContentText("Ошибка формата числа: нужно целое число от 5 до 7");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна бочка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    //Обработчик клика по кнопке Период
    //Открывает диалог редактирования периодичности необходимости доставки
    //Если задать число, не равное 100000, то в любой понедельник
    //это число будет пересчитано автоматически
    //(при условии, что было уже не менее 2 доставок для данной бочки
    //на интервале не более 3 месяцев)
    @FXML
    private void showBarrelEditPeriodDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedBarrelModel =
                (BarrelModel) barrelsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedBarrelModel != null) {
            
            mSelectedBarrel =
                mBarrelsDAOImpl.getBarrel(mSelectedBarrelModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(String.valueOf(mSelectedBarrel.getPeriod()));
            dialog.setTitle("Изменение периода");
            dialog.setHeaderText("Бочка требует заправки один раз в Х дней. Если задать 100000, расчет выполняться не будет");
            dialog.setContentText("Введите целое число от 1 до 199999: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                periodString -> {

                    boolean hasNumFormatErrors = false;
                    
                    try{
                    
                        Pattern pattern = 
                            Pattern.compile("[1-9][0-9]{0,5}");
                        if (pattern.matcher(periodString).matches()) {

                                mSelectedBarrel.setPeriod(
                                    Integer.parseInt(periodString)
                                );
                                mBarrelsDAOImpl.updateBarrel(mSelectedBarrel);
                                //Notify self
                                updateBarrelsForPage();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateBarrels();
                        } else {

                            hasNumFormatErrors = true;
                        }
                    } catch (NumberFormatException ex){

                        hasNumFormatErrors = true;
                    }
                    if (hasNumFormatErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение периода необходимости доставок не выполнено");
                        alert.setContentText("Ошибка формата числа: нужно целое число от 1 до 199999");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна бочка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showBarrelEditCounterDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedBarrelModel =
                (BarrelModel) barrelsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedBarrelModel != null) {
            
            mSelectedBarrel =
                mBarrelsDAOImpl.getBarrel(mSelectedBarrelModel.getId());
            int mCounterMaxPosition = mSelectedBarrel.getPositions();
            int mCounterMaxNumber;
            switch(mCounterMaxPosition){

                case 5:
                {
                    mCounterMaxNumber = 99999;
                    break;
                }
                case 6:
                {
                    mCounterMaxNumber = 999999;
                    break;
                }
                case 7:
                {
                    mCounterMaxNumber = 9999999;
                    break;
                }
                default:
                {
                    mCounterMaxNumber = 999999;
                    break;
                }
            }
            TextInputDialog dialog =
                new TextInputDialog(String.valueOf(mSelectedBarrel.getCounter()));
            dialog.setTitle("Изменение счетчика");
            dialog.setHeaderText("Какие последние показания водомера у данной бочки?");
            dialog.setContentText("Введите целое число от 0 до " + mCounterMaxNumber);

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                counterString -> {

                    boolean hasNumFormatErrors = false;
                    //boolean tooMuchErrors = false;
                    
                    try{
                    
                        Pattern pattern = 
                            Pattern.compile("[0-9]{1," + mCounterMaxPosition + "}");
                        if (pattern.matcher(counterString).matches()) {

                                mSelectedBarrel.setCounter(
                                    Integer.parseInt(counterString)
                                );
                                mBarrelsDAOImpl.updateBarrel(mSelectedBarrel);
                                //Notify self
                                updateBarrelsForPage();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateBarrels();
                        } else {

                            hasNumFormatErrors = true;
                        }
                    } catch (NumberFormatException ex){

                        hasNumFormatErrors = true;
                    }
                    if (hasNumFormatErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение показаний водомера не выполнено");
                        alert.setContentText("Ошибка формата числа: нужно целое число в границах, заданных разрядностью водомера");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна бочка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    //Переключить значение параметра "высичлять период доставок"
    @FXML
    private void calcPeriodEnable(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedBarrelModel =
                (BarrelModel) barrelsTableView.getSelectionModel().getSelectedItem();
        
        String messageString = "";
        
        //
        if (mSelectedBarrelModel != null) {
            
            mSelectedBarrel =
                mBarrelsDAOImpl.getBarrel(mSelectedBarrelModel.getId());
            
            Date periodCalcDate = mSelectedBarrel.getPeriodCalcDate();
            System.out.println("periodCalcDate.getTime() " + periodCalcDate.getTime());
            Date zeroDate = new Date();
            try {
                zeroDate = new SimpleDateFormat("dd.MM.yyyy").parse("12.12.2000");
            } catch (ParseException ex) {
                Logger.getLogger(BarrelsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (periodCalcDate.compareTo(zeroDate) == 0) {
                
                try {
                    mSelectedBarrel.setPeriodCalcDate(new SimpleDateFormat("dd.MM.yyyy").parse("12.12.2017"));
                } catch (ParseException ex) {
                    Logger.getLogger(BarrelsController.class.getName()).log(Level.SEVERE, null, ex);
                }
                messageString = "Период будет вычисляться при доставках";
            } else {
            
                try {
                    mSelectedBarrel.setPeriodCalcDate(new SimpleDateFormat("dd.MM.yyyy").parse("12.12.2000"));
                } catch (ParseException ex) {
                    Logger.getLogger(BarrelsController.class.getName()).log(Level.SEVERE, null, ex);
                }
                messageString = "Период НЕ будет вычисляться";
            }
            mBarrelsDAOImpl.updateBarrel(mSelectedBarrel);
            updateBarrelsForPage();
            WS1.addSaleControllerInstance.updateBarrels();
            System.out.println("mSelectedBarrel.getPeriodCalcDate() " + mSelectedBarrel.getPeriodCalcDate().getTime());
            Alert infoAlert =
                new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Информация");
            infoAlert.setHeaderText("Изменение работы с периодом доставок");
            infoAlert.setContentText(messageString);
            infoAlert.showAndWait();
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна бочка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void writeDayTask(ActionEvent event){
       
        progressIndicator.visibleProperty().set(true);
        
        filterCheckBox.setSelected(false);
        shopCTextField.setText("");
        needChargeCheckBox.setSelected(false);
        needCleanCheckBox.setSelected(false);
        
        mBarrelsFacade.setFilter(true);
        mBarrelsFacade.setFilterShopId(-1);
        mBarrelsFacade.setNeedCharge(true);
        mBarrelsFacade.setNeedClean(false);
        //mBarrelsFacade.setNeedCleanOrCharge(true);
        mBarrelsFacade.setNeedCleanOrCharge(false);
        
        updateBarrelsForPage();
        
        //
        //if (!filterCheckBox.isSelected()) {
        try{    
            //TODO перехватывать исключения
            ExcellWriter excellWriter = new ExcellWriter();
            //mBarrelsForPage
            List<DayTaskItem> dayTaskItemList = new ArrayList<>();
            
            int DayTaskItemIdx = 0;
            
            for (Barrel barrel : mBarrelsForPage) {
                
                boolean needClean = false;
                
                Date date = barrel.getLastCDate();
                if (
                    (
                        Date.from(Instant.now()).getTime()
                        - date.getTime()
                    ) < Settings.getCleaningTypicalCycleTime()
                ) {
                    //setTextFill(Color.BLACK);
                }/* else if(
                    (
                        Date.from(Instant.now()).getTime()
                        - date.getTime()
                    ) < Settings.getCleaningOverdueCycleTime()){
                    //setTextFill(Color.ORANGE);
                    needClean = true;
                }*/ else {
                    //setTextFill(Color.RED);
                    needClean = true;
                }
                
                //Integer needChargeLevel = null;
                int needChargeLevel = Globals.findBarrelWarningById(barrel.getId()).mNeedLevel;
                /*if (needChargeLevel == null) {

                    needChargeLevel = 0;
                }*/
                
                dayTaskItemList.add(
                    new DayTaskItem(
                        DayTaskItemIdx
                        , mShopsDAOImpl.getShop(barrel.getShopId()).getName()
                        , mShopsDAOImpl.getShop(barrel.getShopId()).getAddress()
                        , mShopsDAOImpl.getShop(barrel.getShopId()).getPhone()
                        , barrel.getWhaterTId().getName()
                            + " "
                            + mBarrelCapacitiesDAOImpl
                                .getBarrelCapacity(barrel.getCapacityId())
                                .getCapacity()
                        , needChargeLevel
                        , needClean
                        , barrel.getCounter()
                        , barrel.getPrice()
                        , mShopsDAOImpl.getShop(barrel.getShopId()).getDebt()
                    )
                );
                DayTaskItemIdx++;
            }
            try {
                excellWriter.writeDayTask(dayTaskItemList);
                //Сообщение пользователю об успешном создании файла с заданием
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Информация");
                alert.setHeaderText("Файл задания на день успешно создан");
                //Показать сообщение и ждать
                alert.showAndWait();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Файл задания не создан");
                alert.setContentText("Файл отчета уже сформирован и открыт, либо нет возможности создать директорию или файл");
                alert.showAndWait();
            }
        } catch(Exception ex){
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Файл задания не создан");
                alert.setContentText("Неизвестная ошибка");
                alert.showAndWait();
        } finally {
        
            mBarrelsFacade.setFilter(false);
            mBarrelsFacade.setNeedCharge(false);
            updateBarrelsForPage();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void writeShopPrices(ActionEvent event){
       
        progressIndicator.visibleProperty().set(true);
        
        filterCheckBox.setSelected(false);
        shopCTextField.setText("");
        needChargeCheckBox.setSelected(false);
        needCleanCheckBox.setSelected(false);
        
        mBarrelsFacade.setFilter(false);
        mBarrelsFacade.setFilterShopId(-1);
        mBarrelsFacade.setNeedClean(false);
        mBarrelsFacade.setNeedCharge(false);
        mBarrelsFacade.setNeedCleanOrCharge(false);
        mBarrelsFacade.setActive(1);
        
        updateBarrelsForPage();
        
        //
        try{    
            //TODO перехватывать исключения
            ExcellWriter excellWriter = new ExcellWriter();
            //mBarrelsForPage
            List<ShopPricesItem> shopPricesItemList = new ArrayList<>();
            
            int shopPricesItemIdx = 0;
            
            for (Barrel barrel : mBarrelsForPage) {
                
                shopPricesItemList.add(
                    new ShopPricesItem(
                        shopPricesItemIdx
                        , mShopsDAOImpl.getShop(barrel.getShopId()).getName()
                        , barrel.getWhaterTId().getName()
                            + " "
                            + mBarrelCapacitiesDAOImpl
                                .getBarrelCapacity(barrel.getCapacityId())
                                .getCapacity()
                        , barrel.getPrice()
                    )
                );
                shopPricesItemIdx++;
            }
            try {
                excellWriter.writeShopPrices(shopPricesItemList);
                //Сообщение пользователю об успешном создании файла с заданием
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Информация");
                alert.setHeaderText("Файл магазины-цены успешно создан");
                //Показать сообщение и ждать
                alert.showAndWait();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Файл магазины-цены не создан");
                alert.setContentText("Файл магазины-цены уже сформирован и открыт, либо нет возможности создать директорию или файл");
                alert.showAndWait();
            }
        } catch(Exception ex){
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Файл магазины-цены не создан");
                alert.setContentText("Неизвестная ошибка");
                alert.showAndWait();
        } finally {
        
            /*mBarrelsFacade.setFilter(false);
            mBarrelsFacade.setNeedCharge(false);
            updateBarrelsForPage();*/
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    private static void fillBarrelsObservableList(List<Barrel> _barrelsList){
        
        for (Barrel barrel : _barrelsList) {
            
            Integer needChargeLevel = null;
            needChargeLevel = Globals.findBarrelWarningById(barrel.getId()).mNeedLevel;
            if (needChargeLevel == null) {
                
                needChargeLevel = 0;
            }
                        
            mBarrelsObservableList.add(new BarrelModel(
                    barrel.getId(),
                    mShopsDAOImpl.getShop(barrel.getShopId()).getName(),
                    barrel.getWhaterTId().getName(),
                    mBarrelCapacitiesDAOImpl.getBarrelCapacity(barrel.getCapacityId()).getCapacity(),
                    new SimpleDateFormat("yyyy.MM.dd").format(barrel.getLastCDate()),
                    barrel.getPrice().doubleValue(),
                    barrel.getCounter(),
                    barrel.getAllowedRest(),
                    barrel.getRecentlyReplaced(),
                    needChargeLevel
                )
            );
        }        
    }
    
    //page of the table of the barrels creating 
    private Node createPage(int _pageIndex/*, int _rowsPerPage*/) {
        
        progressIndicator.visibleProperty().set(true);
        
        mCurrentPageIdx = _pageIndex;
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        
        //mBarrelsForPage = mBarrelsFacade.getBarrelsForPage(mRowsPerPage, fromIndex);
        int toIndex = Math.min(fromIndex + mRowsPerPage, mBarrelsCount);
        
        mBarrelsForPage = mBarrelsFacade.getBarrelsForPage(-1, -1);
        mBarrelsObservableList.clear();
        if (mBarrelsForPage.size() < toIndex) {
            
            toIndex = mBarrelsForPage.size();
        }
        fillBarrelsObservableList(mBarrelsForPage.subList(fromIndex, toIndex));
        
        barrelsTableView.setItems(mBarrelsObservableList);
        
        AnchorPane.setTopAnchor(barrelsTableView, 0.0);
        AnchorPane.setRightAnchor(barrelsTableView, 0.0);
        AnchorPane.setBottomAnchor(barrelsTableView, 0.0);
        AnchorPane.setLeftAnchor(barrelsTableView, 0.0);

        progressIndicator.visibleProperty().set(false);
        
        return new AnchorPane(barrelsTableView);
    }
    
    public void updateBarrelsForPage(){
        
        mBarrelsForPage = mBarrelsFacade.getBarrelsForPage(-1, -1);
        
        setPageCount();
        mBarrelsAllCount = mBarrelsDAOImpl.getBarrelsCount();
        
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        int toIndex = Math.min(fromIndex + mRowsPerPage, mBarrelsCount);
        //mBarrelsForPage = mBarrelsFacade.getBarrelsForPage(mRowsPerPage, fromIndex);
        
        mBarrelsObservableList.clear();
        if (mBarrelsForPage.size() < toIndex) {
            
            toIndex = mBarrelsForPage.size();
        }
        fillBarrelsObservableList(mBarrelsForPage.subList(fromIndex, toIndex));
        
        //resetFilterForm();
    }
    
    //Приведение формы filter в исходное состояние
    private void resetFilterForm(){
        
        shopCTextField.clear();
        //System.out.println("reset form");
        mShopNamesSet.clear();
        for (Shop shop : mShops) {
            
            if (shop.getActive()) {
                mShopNamesSet.add(shop.getName());
            }
        }
        //отписываемся от предыдущего набора автодополнения
        if (mShopAutoCompletionBinding != null) {
            
            mShopAutoCompletionBinding.dispose();
        }
        //подписываемся на новый набор автодополнения
        mShopAutoCompletionBinding = TextFields.bindAutoCompletion(shopCTextField, mShopNamesSet);
    }
    
    //To global class!
    public void updateShops(){
        
        mShops = mShopsDAOImpl.getAllShops();
        resetFilterForm();
    }
    
    private void setPageCount(){
    
        //mBarrelsCount = mBarrelsDAOImpl.getBarrelsCount();
        if (mBarrelsForPage != null) {
            
            mBarrelsCount = mBarrelsForPage.size();
            barrelsPagination.setPageCount(
                mBarrelsCount > 0
                ? (mBarrelsCount + mRowsPerPage - 1) / mRowsPerPage
                : 1
            );
        } else {
        
            mBarrelsCount = mBarrelsDAOImpl.getBarrelsCount();
            barrelsPagination.setPageCount(
                mBarrelsCount > 0
                ? (mBarrelsCount + mRowsPerPage - 1) / mRowsPerPage
                : 1
            );
            
            //barrelsPagination.setPageCount(1);
        }
    }

    public List<Shop> getShops()
    {
        if (mShops == null) {
            
            mShops = new ArrayList<>();
        }
        return mShops;
    }
    
    private void setNumCellFactory(){

        numTableColumn.setCellFactory(column ->{
            
            return new TableCell(){
                @Override
                public void updateItem(Object item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setGraphic(null);
                    setText(empty ? null :
                        (mBarrelsAllCount - getIndex())
                        - ((mCurrentPageIdx) * mRowsPerPage)
                        + ""
                    );
                }
            };
        });
    }
    
    public int callCalcPeriod(Barrel _barrel, Date _lastDate){
            
        return mBarrelsFacade.calcPeriod(_barrel, _lastDate);
    }
    
    /*
    */
    public void hideEditBarrelButtons(){
    
        barrelEditARButton.setVisible(false);
        barrelEditARButton.setDisable(true);
        
        barrelEditPriceButton.setVisible(false);
        barrelEditPriceButton.setDisable(true);
        
        barrelEditPositionsButton.setVisible(false);
        barrelEditPositionsButton.setDisable(true);
        
        barrelEditCounterButton.setVisible(false);
        barrelEditCounterButton.setDisable(true);
        
        barrelEditLastCDateButton.setVisible(false);
        barrelEditLastCDateButton.setDisable(true);
        
        barrelEditPeriodButton.setVisible(false);
        barrelEditPeriodButton.setDisable(true);
        
        calcPeriodEnableButton.setVisible(false);
        calcPeriodEnableButton.setDisable(true);
    }
}
