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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.tyaa.ws.WS1;
import org.tyaa.ws.common.Globals;
import org.tyaa.ws.dao.SalesFacade;
import org.tyaa.ws.dao.impl.BarrelsDAOImpl;
import org.tyaa.ws.dao.impl.SalesDAOImpl;
import org.tyaa.ws.dao.impl.ShopsDAOImpl;
import org.tyaa.ws.dao.impl.BarrelCapacitiesDAOImpl;
import org.tyaa.ws.dao.impl.CarsDAOImpl;
import org.tyaa.ws.dao.impl.DebtChangesDAOImpl;
import org.tyaa.ws.dao.impl.DebtsDAOImpl;
import org.tyaa.ws.dao.impl.DriversDAOImpl;
import org.tyaa.ws.dao.util.DateUtil;
//import org.tyaa.ws.dao.impl.WaterTypesDAOImp;
import org.tyaa.ws.entity.*;
import org.tyaa.ws.viewmodel.BarrelModel;
import org.tyaa.ws.viewmodel.SaleModel;
import org.tyaa.ws.viewmodel.ShopModel;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class SalesController implements Initializable, ControlledScreen {

    @FXML
    private TableView salesTableView;
    
    @FXML
    private TableColumn saleIdTableColumn;
    @FXML
    private TableColumn saleShopTableColumn;
    @FXML
    private TableColumn saleBarrelTableColumn;
    @FXML
    private TableColumn saleDriverTableColumn;
    @FXML
    private TableColumn saleCarTableColumn;
    @FXML
    private TableColumn saleCountOldTableColumn;
    @FXML
    private TableColumn saleCountNewTableColumn;
    @FXML
    private TableColumn saleCountDiffTableColumn;
    @FXML
    private TableColumn saleCleaningTableColumn;
    @FXML
    private TableColumn saleRepairTableColumn;
    @FXML
    private TableColumn saleToPayTableColumn;
    @FXML
    private TableColumn saleProfitTableColumn;
    @FXML
    private TableColumn saleDebtTableColumn;
    @FXML
    private TableColumn saleDateTableColumn;
    @FXML
    private TableColumn saleNoticeTableColumn;
    
    @FXML
    private MenuButton reportsMenuButton;
    @FXML
    private MenuItem dayReportsMenuItem;
    
    /*@FXML
    private Button saleEditCounterNewButton;*/
    @FXML
    private Button deleteSaleButton;
    @FXML
    private Button editSaleButton;
    @FXML
    private Button addSaleDebtButton;
    @FXML
    private Button editSaleDebtButton;
    @FXML
    private Button addSaleDebtAmortButton;
    @FXML
    private Button freeChangeDebtButton;
    
    @FXML
    private Pagination salesPagination;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    /*For filter*/
    @FXML
    CheckBox filterCheckBox;
    @FXML
    CustomTextField shopCTextField;
    @FXML
    ComboBox barrelComboBox;
    @FXML
    Button resetBarrelButton;
    @FXML
    DatePicker fromDatePicker;
    @FXML
    DatePicker toDatePicker;
    @FXML
    CustomTextField carCTextField;
    @FXML
    CustomTextField driverCTextField;
    /*End*/
    
    private ScreensController myController;
    
    private ObservableList<SaleModel> mSalesObservableList;
    private List<Sale> mSalesForPage;
    
    private SalesDAOImpl mSalesDAOImpl;
    
    private ShopsDAOImpl mShopsDAOImpl;
    private BarrelsDAOImpl mBarrelsDAOImpl;
    
    private DriversDAOImpl mDriversDAOImpl;
    private CarsDAOImpl mCarsDAOImpl;
    
    private BarrelCapacitiesDAOImpl mBarrelCapacitiesDAOImpl;
    
    private SalesFacade mSalesFacade;
    
    //Номер текущей страницы таблицы доставок
    private int mCurrentPageIdx;
    //Число строк на одной странице таблицы доставок
    private int mRowsPerPage = 25;
    
    /*For filter*/
    private List<Shop> mShops;
    private List<Barrel> mBarrels;
    private List<Barrel> mShopBarrels;
    private List<Car> mCars;
    private List<Driver> mDrivers;
    
    private Set<String> mShopNamesSet;
    private Set<String> mCarNamesSet;
    private Set<String> mDriverNamesSet;
    
    //хендлеры к наборам автодополнения
    private AutoCompletionBinding<String> mShopAutoCompletionBinding;
    private AutoCompletionBinding<String> mDriverAutoCompletionBinding;
    private AutoCompletionBinding<String> mCarAutoCompletionBinding;
    
    private Shop mSelectedShop;
    private Barrel mSelectedBarrel;
    private Car mSelectedCar;
    private Driver mSelectedDriver;
    
    ObservableList<Barrel> mBarrelsObservableList;
    /*End*/
    
    //Активные, не активные, все?
    //(1, 0, -1)
    private int mActive;
    
    //
    private Sale mSelectedSale;
    private SaleModel mSelectedSaleModel;
    
    private DebtsDAOImpl mDebtsDAOImpl;
    private DebtChangesDAOImpl mDebtChangesDAOImpl;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        WS1.salesControllerInstance = this;
        
        progressIndicator.visibleProperty().set(false);
        
        mSalesObservableList = FXCollections.observableArrayList();
        
        mSalesDAOImpl = new SalesDAOImpl();
        mShopsDAOImpl = new ShopsDAOImpl();
        mBarrelsDAOImpl = new BarrelsDAOImpl();
        mBarrelCapacitiesDAOImpl = new BarrelCapacitiesDAOImpl();
        mDriversDAOImpl = new DriversDAOImpl();
        mCarsDAOImpl = new CarsDAOImpl();
        
        mDebtsDAOImpl = new DebtsDAOImpl();
        mDebtChangesDAOImpl = new DebtChangesDAOImpl();
        
        mActive = 1;
        
        mSalesFacade = new SalesFacade();
        
        mShops = mShopsDAOImpl.getAllShops();
        mShopNamesSet = new HashSet<>();
        
        mCars = mCarsDAOImpl.getAllCars();
        mCarNamesSet = new HashSet<>();
        
        mDrivers = mDriversDAOImpl.getAllDrivers();
        mDriverNamesSet = new HashSet<>();
        
        mBarrels = mBarrelsDAOImpl.getAllBarrels();
        
        mShopBarrels = new ArrayList();
        
        mBarrelsObservableList = FXCollections.observableArrayList();
        
        resetFilterForm();
        
        mSalesFacade.setFilterShopId(-1);
        mSalesFacade.setFilterBarrelId(-1);
        mSalesFacade.setFilterCarId(-1);
        mSalesFacade.setFilterDriverId(-1);
        
        //обработка события "ввод названия магазина"
        shopCTextField.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                progressIndicator.visibleProperty().set(true);
                
                mShopBarrels.clear();
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
                    
                    //устанавливаем в фасаде доставок значение "ИД магазина" для фильтра;
                    //вызываем в фасаде доставок срабатывание фильтра и обновляем
                    //содержимое страницы таблицы полученными данными
                    
                    mSalesFacade.setFilterShopId(mSelectedShop.getId());
                    updateSalesForPage();
                    
                    //показываем в выпадающем списке бочек те,
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
                    }
                } else {
                    
                    //иначе отправляем фасаду аргумент-сигнал "не выбран магазин"
                    mSalesFacade.setFilterShopId(-1);
                    mSalesFacade.setFilterBarrelId(-1);
                    updateSalesForPage();
                }
                progressIndicator.visibleProperty().set(false);
            }
        });
        
        carCTextField.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                progressIndicator.visibleProperty().set(true);
                
                if (!carCTextField.textProperty().getValue().equals("")
                        && mCarNamesSet.contains(carCTextField.textProperty().getValue())) {
                    
                    //получаем объект выбранного car
                    for (Car car : mCars) {

                        if (String.valueOf(car.getNumber()).equals(carCTextField.getText())) {
                            mSelectedCar = car;
                            break;
                        }
                    }
                    
                    //устанавливаем в фасаде доставок значение "carId" для фильтра;
                    //вызываем в фасаде доставок срабатывание фильтра и обновляем
                    //содержимое страницы таблицы полученными данными
                    
                    mSalesFacade.setFilterCarId(mSelectedCar.getId());
                    updateSalesForPage();
                } else {
                    
                    //иначе отправляем фасаду аргумент-сигнал "не выбран car"
                    mSalesFacade.setFilterCarId(-1);
                    updateSalesForPage();
                }
                progressIndicator.visibleProperty().set(false);
            }
        });
        
        driverCTextField.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                progressIndicator.visibleProperty().set(true);
                
                if (!driverCTextField.textProperty().getValue().equals("")
                        && mDriverNamesSet.contains(driverCTextField.textProperty().getValue())) {
                    
                    //получаем объект выбранного driver
                    for (Driver driver : mDrivers) {

                        if (driver.getName().equals(driverCTextField.getText())) {
                            mSelectedDriver = driver;
                            break;
                        }
                    }
                    
                    //устанавливаем в фасаде доставок значение "driverId" для фильтра;
                    //вызываем в фасаде доставок срабатывание фильтра и обновляем
                    //содержимое страницы таблицы полученными данными
                    
                    mSalesFacade.setFilterDriverId(mSelectedDriver.getId());
                    updateSalesForPage();
                } else {
                    
                    //иначе отправляем фасаду аргумент-сигнал "не выбран driver"
                    mSalesFacade.setFilterDriverId(-1);
                    updateSalesForPage();
                }
                progressIndicator.visibleProperty().set(false);
            }
        });
        
        //обработка события "переключение чекбокса Filter on/off"
        filterCheckBox.setOnAction((event) -> {
            if (filterCheckBox.isSelected()) {
                //устанавливаем в фасаде доставок режим "фильтр"
                mSalesFacade.setFilter(true);
            } else {
                mSalesFacade.setFilter(false);
            }
            updateSalesForPage();
        });
        
        //прорисовка выпадающего списка бочек
        barrelComboBox.setCellFactory((comboBox) -> {
            return new ListCell<Barrel>() {
                @Override
                protected void updateItem(Barrel barrelItem, boolean empty) {
                    super.updateItem(barrelItem, empty);

                    if (barrelItem == null || empty) {
                        setText(null);
                    } else {
                        setText(
                            barrelItem.getWhaterTId().getName()
                            + ", "
                            + mBarrelCapacitiesDAOImpl.getBarrelCapacity(
                                    barrelItem.getCapacityId()
                            ).getCapacity()
                            + " л"
                        );
                    }
                }
            };
        });
        //прорисовка выбранного элемента выпадающего списка бочек
        barrelComboBox.setConverter(new StringConverter<Barrel>() {
            @Override
            public String toString(Barrel barrel) {
                if (barrel == null) {
                    return null;
                } else {
                    return (
                        barrel.getWhaterTId().getName()
                        + ", "
                        + mBarrelCapacitiesDAOImpl.getBarrelCapacity(
                                barrel.getCapacityId()
                        ).getCapacity()
                        + " л"
                    );
                }
            }

            @Override
            public Barrel fromString(String barrelString) {
                return null; // No conversion fromString needed.
            }
        });
        
        /*Настройки постраничности для таблицы доставок*/

        //Настраиваем пагинатор общим количеством элементов в коллекции доставок
        //salesPagination.setPageCount((mSalesDAOImpl.getSalesCount() / 2 + 1));
        int salesAllCount = mSalesDAOImpl.getSalesCount();
        salesPagination.setPageCount(
                salesAllCount > 0
                ? (salesAllCount + mRowsPerPage - 1) / mRowsPerPage
                : 1
        );
        //Максимальное число кнопок-ссылок на страницы
        //salesPagination.setMaxPageIndicatorCount(10);
        //Указываем пагинатору имя метода-генератора страниц
        salesPagination.setPageFactory(
            new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {          
                    return createPage(pageIndex);               
                }
            }
        );

        /**/
        
        saleIdTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("id")
        );
        saleShopTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("shopName")
        );
        saleBarrelTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("barrelName")
        );
        saleDriverTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("driverName")
        );
        
        saleCarTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("carNumber")
        );
        saleCountOldTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("countOld")
        );
        saleCountNewTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("countNew")
        );
        saleCountDiffTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("volume")
        );
        saleCleaningTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("cleaning")
        );
        saleRepairTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("repair")
        );
        saleToPayTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("toPay")
        );
        saleProfitTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("profit")
        );
        saleDebtTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("debt")
        );
        saleDateTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("date")
        );
        saleNoticeTableColumn.setCellValueFactory(
                new PropertyValueFactory<SaleModel, String>("notice")
        );
//        quantityTableColumn.setCellValueFactory(
//                new PropertyValueFactory<SaleModel, String>("quantity")
//        );
//        priceTableColumn.setCellValueFactory(
//                new PropertyValueFactory<SaleModel, Double>("price")
//        );
//        NumberFormat doubleToCurrencyFormatter =
//                NumberFormat.getCurrencyInstance(new Locale("en", "US"));
//        doubleToCurrencyFormatter.setMinimumIntegerDigits(1);
//        doubleToCurrencyFormatter.setMinimumFractionDigits(2);
//        priceTableColumn.setCellFactory(column ->{
//            
//            return new TableCell<SaleModel, Double>(){
//                @Override
//                protected void updateItem(Double item, boolean empty)
//                {
//                    super.updateItem(item, empty);
//                    if (item == null || empty) {
//                        setText(null);
//                        setStyle("");
//                    } else {
//                        setText(doubleToCurrencyFormatter.format(item / 100));
//                    }
//                }
//                
//            };
//        });

        

        //Настройка оформления вывода информации булевого типа
        saleCleaningTableColumn.setCellFactory(column ->{
            
            return new TableCell<SaleModel, Boolean>(){
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
        
        saleRepairTableColumn.setCellFactory(column ->{
            
            return new TableCell<SaleModel, Boolean>(){
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

        //
        saleDateTableColumn.setCellFactory(column ->{
            
            return new TableCell<SaleModel, String>(){
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
                        }
                    }
                }
            };
        });
        //salesTableView.setItems(sales);
        
        //обработка события "выбор бочки"
        barrelComboBox.setOnAction((event) -> {
            mSelectedBarrel = (Barrel) barrelComboBox
                            .getSelectionModel()
                            .getSelectedItem();
            //System.out.println(mSelectedBarrel);
            if (mSelectedBarrel != null) {
                mSalesFacade.setFilterBarrelId(mSelectedBarrel.getId());
                updateSalesForPage();
            }
            //System.out.println("ComboBox Action (selected: " + selectedPerson.toString() + ")");
        });
        
        resetBarrelButton.setOnMouseClicked((event) -> {
            //mShopBarrels.clear();
            //mBarrelsObservableList.clear();
            barrelComboBox.valueProperty().set(null);
            mSalesFacade.setFilterBarrelId(-1);
            updateSalesForPage();
        });
        
        //Когда выбирается дата from
        fromDatePicker.setOnAction((event) -> {
            
            //
            mSalesFacade.setFilterFromDate(
            
                Date.from((fromDatePicker
                    .getValue()
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())).toInstant()
                )
            );
            updateSalesForPage();
        });
        
        //Когда выбирается дата to
        toDatePicker.setOnAction((event) -> {
            
            progressIndicator.visibleProperty().set(true);
            
            //
            mSalesFacade.setFilterToDate(
            
                Date.from((toDatePicker
                    .getValue()
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())).toInstant()
                )
            );
            updateSalesForPage();
            progressIndicator.visibleProperty().set(false);
        });
        
        /*Reports menu button*/
        dayReportsMenuItem.setOnAction((event) -> {
            
            //event.getTarget();
            //System.out.println(event.getTarget());
            myController.setScreen(WS1.dayReportsID);
        });
    }    

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
    
    @FXML
    private void goToShopsScreen(ActionEvent event){
       myController.setScreen(WS1.shopsID);
    }
    
    @FXML
    private void goToBarrelsScreen(ActionEvent event){
       myController.setScreen(WS1.barrelsID);
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
    private void goToSettingsScreen(ActionEvent event){
       myController.setScreen(WS1.settingsID);
       WS1.primaryStage.setMaximized(false);
       WS1.primaryStage.setWidth(600);
       WS1.primaryStage.setHeight(620);
       WS1.primaryStage.setY(30);
    }
    
    @FXML
    private void goToAddSaleScreen(ActionEvent event){
       myController.setScreen(WS1.addSaleID);
       WS1.primaryStage.setMaximized(false);
       WS1.primaryStage.setWidth(600);
       WS1.primaryStage.setHeight(820);
       WS1.primaryStage.setY(0);
    }
    
    //
    @FXML
    private void goToEditSaleScreen(){
        
        progressIndicator.visibleProperty().set(true);

        //
        mSelectedSaleModel =
            (SaleModel) salesTableView
                .getSelectionModel()
                .getSelectedItem();

        //
        if (mSelectedSaleModel != null) {

            mSelectedSale =
                mSalesDAOImpl.getSale(mSelectedSaleModel.getId());
            
            WS1.addSaleControllerInstance.setEditingSale(mSelectedSale);
            WS1.addSaleControllerInstance.setEditMode(true);
            
            if (!WS1.addSaleControllerInstance.mAbortState) {
                
                myController.setScreen(WS1.addSaleID);
                WS1.primaryStage.setMaximized(false);
                WS1.primaryStage.setWidth(600);
                WS1.primaryStage.setHeight(820);
                WS1.primaryStage.setY(0);
            } else {
            
                WS1.addSaleControllerInstance.mAbortState = false;
            }
            
            
        } else {

            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна доставка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    //Отображение диалога удаления доставки
    @FXML
    private void showDeleteSaleDialog(){
    
        progressIndicator.visibleProperty().set(true);

        //
        mSelectedSaleModel =
            (SaleModel) salesTableView
                .getSelectionModel()
                .getSelectedItem();

        //
        if (mSelectedSaleModel != null) {

            mSelectedSale =
                mSalesDAOImpl.getSale(mSelectedSaleModel.getId());
            //Получаем объект бочки, для которой производилась доставка
            mSelectedBarrel =
                mBarrelsDAOImpl.getBarrel(mSelectedSale.getBarrelId());
            //Проверяем, последняя ли это доставка для данной бочки,
            
            if (Objects.equals(mSelectedSale.getId(), mSelectedBarrel.getLastSaleId())) {
                
                int delResult = 0;

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Удаление доставки");
                alert.setHeaderText("Требуется подтверждение");
                alert.setContentText("Вы действительно хотите удалить выбранную доставку?");

                Optional<ButtonType> result = alert.showAndWait();

                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {

                    //перед удалением доставки привести другие данные
                    //в состояние до фиксации этой доставки
                    
                    //1. barrel
                    
                    int barrelChangingResult = 0;
                    
                    try{
                        Barrel barrel =
                            mBarrelsDAOImpl.getBarrel(mSelectedSale.getBarrelId());

                        Date deletedSaleDate = mSelectedSale.getCreatedAt();
                        Date prevSaleDate = mSelectedSale.getCreatedAt();
                        //флаг, сигнализирующий о том,
                        //что была найдена доставка для данной бочки,
                        //предшествующая удаляемой
                        boolean firstFoundPrevSaleFlag = false;
                        //счетчик отмотанных дней
                        int i = 0;
                        //пока нет сигнала, что стартовая доставка диапазона найдена
                        while (!firstFoundPrevSaleFlag) {
                            //если доставок для диапазона вычисления не найдено,
                            //а число итераций уже слишком большое - прекращаем поиск
                            //System.out.println(i);
                            if (i == 365) {
                                //System.out.println("not found");
                                break;
                            }
                            //дата текущая (i = 0) или на i дней ранее (i > 0)
                            prevSaleDate = DateUtil.addDays(deletedSaleDate, -i);
                            //System.out.println(prevSaleDate);
                            //prevDate = DateUtil.addDays(lastSaleDate, -(i + 1));
                            //Переменная для списка доставок за день
                            List<Sale> tmpBeforeSaleList = null;
                            Sale tmpBeforeSale = null;
                            //начинаем поиск с текущей даты
                            //попытка найти доставки за эту дату,
                            //а с каждой следующей итерацией -
                            //на день раньше
                            tmpBeforeSaleList = 
                                mSalesDAOImpl.getFilteredSales(
                                    -1
                                    , barrel.getId()
                                    , -1
                                    , prevSaleDate
                                    , prevSaleDate
                                    , -1
                                    , -1
                                    , -1
                                );
                            //если доставки за текущую дату найдены
                            if (tmpBeforeSaleList != null) {

                                if (tmpBeforeSaleList.size() > 0) {
                                    //сортируем их по идентификатору,
                                    //чтобы первой извлекалась доставка с
                                    //самым большим ИД
                                    Collections.sort(tmpBeforeSaleList, 
                                        (o1, o2) ->
                                            o2.getId().compareTo(o1.getId()));
                                    /*for (Sale tmpBeforeSaleItem : tmpBeforeSaleList) {
                                        System.out.println(
                                            tmpBeforeSaleItem.getId()
                                            + "; " + tmpBeforeSaleItem.getBarrelId()
                                            + "; " + tmpBeforeSaleItem.getDriverId()
                                        );
                                    }*/
                                    //если это доставки за дату удаляемой доставки,
                                    //и доставок блоее одной,
                                    //то выбираем в качестве новой крайней доставки
                                    //для текущей бочки предпоследнюю
                                    if (mSelectedSale.getCreatedAt().getTime()
                                        == tmpBeforeSaleList.get(0).getCreatedAt().getTime()) {

                                        if (tmpBeforeSaleList.size() > 1) {

                                            tmpBeforeSale = tmpBeforeSaleList.get(1);
                                            firstFoundPrevSaleFlag = true;

                                            /*System.out.println(
                                                tmpBeforeSale.getId()
                                                + "; " + tmpBeforeSale.getBarrelId()
                                                + "; " + tmpBeforeSale.getDriverId()
                                            );*/
                                        }
                                        //если за текущую дату есть только
                                        //удаляемая доставка - пропускаем шаг,
                                        //чтобы попасть еще на день ранее
                                    } else {
                                        //если текущая дата не совпадает с датой
                                        //удаляемой доставки - удаляем первый
                                        //элемент в списке
                                        tmpBeforeSale = tmpBeforeSaleList.get(0);
                                        firstFoundPrevSaleFlag = true;
                                        /*System.out.println(
                                            tmpBeforeSale.getId()
                                            + "; " + tmpBeforeSale.getBarrelId()
                                            + "; " + tmpBeforeSale.getDriverId()
                                        );*/
                                    }
                                }
                            }
                            //если доставка найдена
                            if (firstFoundPrevSaleFlag && tmpBeforeSale != null) {

                                //
                                mSelectedBarrel.setLastSaleId(tmpBeforeSale.getId());
                            } else {

                                mSelectedBarrel.setLastSaleId(null);
                            }
                            //увкличиваем счетчик отматываемых дней
                            i++;
                        }

                        //сохраняем значение счетчика:
                        //current count - the last added volume
                        mSelectedBarrel.setCounter(
                            mSelectedBarrel.getCounter() - mSelectedSale.getVolume()
                        );

                        mBarrelsDAOImpl.updateBarrel(mSelectedBarrel);
                    } catch(Exception ex){
                        
                        barrelChangingResult = -1;
                    }
                    //2. debts
                    
                    int debtChangesDelResult = 0;
                    
                    //all the debts by selected sale's date
                    List<DebtChange> selectedSaleDebtChangeList =
                        mDebtsDAOImpl.getFilteredDebts(
                            -1
                            , -1
                            , mSelectedSale.getCreatedAt()
                            , -1
                            , -1
                            , -1
                            , -1
                        );
                    
                    //only selected sale's debt changes
                    selectedSaleDebtChangeList =
                        selectedSaleDebtChangeList.stream()
                            .filter(debt ->
                                ((DebtChange)debt).getSaleId() == mSelectedSale.getId())
                            .collect(Collectors.toList());
                    for (DebtChange selectedSaleDebtChange : selectedSaleDebtChangeList) {
                        
                        if (selectedSaleDebtChange.getIsDebt()) {
                            
                            //debt change value
                            Double debtDouble =
                                selectedSaleDebtChange.getValue().doubleValue();
                            //delete the debt
                            debtChangesDelResult =
                                mDebtChangesDAOImpl.deleteDebt(
                                    selectedSaleDebtChange.getId()
                                );
                            //change debt balance of the shop
                            Shop deletedSaleShop =
                                mShopsDAOImpl.getShop(mSelectedBarrel.getShopId());
                            BigDecimal shopDebt = deletedSaleShop.getDebt();
                            Double shopDebtDouble = shopDebt.doubleValue();
                            shopDebtDouble = shopDebtDouble - debtDouble;
                            deletedSaleShop.setDebt(new BigDecimal(shopDebtDouble));
                            mShopsDAOImpl.updateShop(deletedSaleShop);
                        } else {
                            
                            if (selectedSaleDebtChange.getIsCredit()) {
                                
                                //delete the credit
                                debtChangesDelResult =
                                    mDebtChangesDAOImpl.deleteDebt(
                                        selectedSaleDebtChange.getId()
                                    );
                            } else {
                            
                                //debt change value
                                Double debtDouble =
                                    selectedSaleDebtChange.getValue().doubleValue();
                                //find the amorted debt and restore the balance
                                //amorted debt id
                                int amortedDebtId =
                                    selectedSaleDebtChange.getDebtId();
                                //amorted debt
                                DebtChange amortedDebt =
                                    mDebtChangesDAOImpl.getDebtChange(amortedDebtId);
                                //restore the amorted debt balance
                                Double amortedDebtBalance =
                                    amortedDebt.getBalance().doubleValue();
                                amortedDebtBalance = amortedDebtBalance + debtDouble;
                                amortedDebt.setBalance(
                                    new BigDecimal(amortedDebtBalance)
                                );
                                mDebtChangesDAOImpl.updateDebtChange(amortedDebt);

                                //delete the amort
                                debtChangesDelResult =
                                    mDebtChangesDAOImpl.deleteDebt(
                                        selectedSaleDebtChange.getId()
                                    );

                                //change debt balance in the shop
                                Shop deletedSaleShop =
                                    mShopsDAOImpl.getShop(mSelectedBarrel.getShopId());
                                BigDecimal shopDebt = deletedSaleShop.getDebt();
                                Double shopDebtDouble = shopDebt.doubleValue();
                                shopDebtDouble = shopDebtDouble + debtDouble;
                                deletedSaleShop.setDebt(new BigDecimal(shopDebtDouble));
                                mShopsDAOImpl.updateShop(deletedSaleShop);
                            }
                        }
                    }
                    
                    //Удаляем запись о доставке из БД,
                    //если получилось - возвращается значение true
                    delResult =
                        mSalesDAOImpl.deleteSale(
                            mSelectedSale.getId()
                        );
                    
                    updateSalesForPage();
                    WS1.barrelsControllerInstance.updateBarrelsForPage();
                    WS1.shopsControllerInstance.updateShopsForPage();
                    //upd debts and add sale pages
                    WS1.debtsControllerInstance.updateDebtChangesForPage();
                    WS1.addSaleControllerInstance.updateBarrels();
                    WS1.addSaleControllerInstance.updateShops();
                    WS1.addSaleControllerInstance.updateDebts();

                    if (delResult == 0) {
                        
                        
                        if (barrelChangingResult == -1) {
                            
                            alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Предупреждение");
                            alert.setHeaderText("При удалении доставки возникли проблемы с внесением изменений в данные бочки");
                            //Показать сообщение и ждать клика по кнопке Ok
                            alert.showAndWait();
                        }
                        
                        if (debtChangesDelResult == -1) {
                            
                            alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Предупреждение");
                            alert.setHeaderText("При удалении доставки возникли проблемы с изменением данных долгов");
                            //Показать сообщение и ждать клика по кнопке Ok
                            alert.showAndWait();
                        }

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Информация");
                        alert.setHeaderText("Выбранная доставка удалена");
                        //Показать сообщение и ждать клика по кнопке Ok
                        alert.showAndWait();
                    } else {

                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Сбой в базе данных. Выбранная доставка не найдена и не удалена.");
                        alert.showAndWait();
                    }
                    //
                    //updateSalesForPage();
                }
            } else {
            
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Доставка не удалена");
                alert.setContentText("Отмена попытки удалить не самую последнюю доставку для данной бочки. Это действие могло бы привести базу данных в некорректное состояние");
                alert.showAndWait();
            }
            //mDebtChangesDAOImpl.
        } else {

            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна доставка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    //
    @FXML
    private void showAddSaleDebtDialog(){
    
        progressIndicator.visibleProperty().set(true);

        //
        mSelectedSaleModel =
            (SaleModel) salesTableView
                .getSelectionModel()
                .getSelectedItem();

        //
        if (mSelectedSaleModel != null) {

            mSelectedSale =
                mSalesDAOImpl.getSale(mSelectedSaleModel.getId());
            if (mSelectedSale.getDebt().doubleValue() <= 0.0d) {
                
                TextInputDialog dialog =
                    new TextInputDialog("0");
                dialog.setTitle("Добавление долга");
                dialog.setHeaderText("Нужно указать сумму долга, добавившегося при доставке");
                dialog.setContentText("Введите положительное целое или дробное число: ");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(
                    debtNewString -> {
                    
                        boolean hasNumFormatErrors = false;
                        try{

                            Pattern pattern = 
                                Pattern.compile("[0-9]{1,5}[.,]{0,1}[0-9]{0,2}");
                            if (pattern.matcher(debtNewString).matches()) {

                                    Double debtNewDouble =
                                        Double.parseDouble(
                                            debtNewString.replaceAll(",", ".")
                                        );
                                    //---
                                    //Сохраняем сумму долга в доставку
                                    BigDecimal saleDebt =
                                        mSelectedSale.getDebt();
                                    //прибавляем разность нового добавившегося долга
                                    saleDebt =
                                        saleDebt.add(
                                            BigDecimal.valueOf(debtNewDouble)
                                        );//---
                                    mSelectedSale.setDebt(
                                        BigDecimal.valueOf(saleDebt.doubleValue())
                                    );
                                    mSalesDAOImpl.updateSale(mSelectedSale);
                                    
                                    //Суммируем новое значение долга с балансом долга магазина
                                    Shop saleShop =
                                        mShopsDAOImpl.getShop(
                                            mSelectedSale.getShopId()
                                        );
                                    BigDecimal shopDebt = saleShop.getDebt();
                                    shopDebt =
                                        shopDebt.add(
                                            BigDecimal.valueOf(debtNewDouble)
                                        );
                                    saleShop.setDebt(shopDebt);
                                    mShopsDAOImpl.updateShop(saleShop);
                                    
                                    //добавляем запись в таблицу изменений долгов
                                    DebtChange newDebt = new DebtChange();
                                    newDebt.setShopId(saleShop.getId());
                                    newDebt.setIsDebt(true);
                                    newDebt.setValue(
                                        BigDecimal.valueOf(debtNewDouble)
                                    );
                                    newDebt.setDate(mSelectedSale.getCreatedAt());
                                    newDebt.setDebtId(-1);
                                    newDebt.setBalance(
                                        BigDecimal.valueOf(debtNewDouble)
                                    );
                                    newDebt.setSaleId(mSelectedSale.getId());
                                    //
                                    ButtonType yesButtonType = new ButtonType("Да", ButtonBar.ButtonData.YES);
                                    ButtonType noButtonType = new ButtonType("Нет", ButtonBar.ButtonData.NO);
                                    Alert alert =
                                        new Alert(
                                            Alert.AlertType.CONFIRMATION
                                                , ""
                                                , yesButtonType
                                                , noButtonType
                                        );
                                    alert.setTitle("Выбор типа долга");
                                    alert.setHeaderText("Этот долг требует погашения?");
                                    alert.setContentText("Кликните Да, если требует, или Нет, если не требует");
                                    boolean notReqDebtAmort = false;
                                    String headerText = "К информации о доставке добавлен новый долг, ТРЕБУЮЩИЙ ПОГАШЕНИЯ";
                                    Optional<ButtonType> reqAmortResult = alert.showAndWait();
                                    if ((reqAmortResult.isPresent())
                                        && (reqAmortResult.get() == yesButtonType)) {

                                    } else {

                                        notReqDebtAmort = true;
                                        headerText = "К информации о доставке добавлен новый долг, НЕ требующий погашения";
                                    }
                                    newDebt.setNotReqAmort(notReqDebtAmort);
                                    mDebtChangesDAOImpl.createDebtChange(newDebt);
                                    WS1.dayReportsControllerInstance.updateDayReportItemsForPage();
                                    //Notify self
                                    updateSalesForPage();
                                    //нотифицировать контроллер добавления доставок
                                    WS1.addSaleControllerInstance.updateShops();
                                    alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Информация");
                                    alert.setHeaderText(headerText);
                                    //Показать сообщение и ждать клика по кнопке Ok
                                    alert.showAndWait();
                            } else {

                                hasNumFormatErrors = true;
                            }
                        } catch (NumberFormatException ex){

                            hasNumFormatErrors = true;
                        }
                        if (hasNumFormatErrors) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка");
                            alert.setHeaderText("Долг не добавлен");
                            alert.setContentText("Ошибка формата числа: нужно целое или дробное число (максимум - 99999.99)");
                            alert.showAndWait();
                        }
                    });
            } else {
            
                Alert errorAlert =
                    new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText("Долг не добавлен");
                errorAlert.setContentText("При данной доставке уже есть новый долг. Его величину можно изменить в разделе долгов");
                errorAlert.showAndWait();
            }
            //mDebtChangesDAOImpl.
        } else {

            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна доставка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showSaleEditDebtDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        mSelectedSaleModel =
            (SaleModel) salesTableView
                .getSelectionModel()
                .getSelectedItem();

        //
        if (mSelectedSaleModel != null) {

            mSelectedSale =
                mSalesDAOImpl.getSale(mSelectedSaleModel.getId());
            
            List<DebtChange> saleDayDebtChangeList =
                mDebtsDAOImpl.getFilteredDebts(
                    -1
                    , -1
                    , mSelectedSale.getCreatedAt()
                    , -1
                    , 0
                    , -1
                    , -1
                );
            saleDayDebtChangeList =
                saleDayDebtChangeList.stream()
                    .filter(debt ->
                        ((DebtChange)debt).getSaleId() == mSelectedSale.getId()
                            && ((DebtChange)debt).getIsDebt()
                    )
                    .collect(Collectors.toList());
            DebtChange oldDebtTmp = null;
            if (saleDayDebtChangeList != null && saleDayDebtChangeList.size() > 0) {
                
                oldDebtTmp = saleDayDebtChangeList.get(0);
            }
            final DebtChange oldDebt = oldDebtTmp;
            if (oldDebt != null) {
                
                double oldDebtValue = oldDebt.getValue().doubleValue();
                
                TextInputDialog dialog =
                    new TextInputDialog(String.valueOf(oldDebtValue));
                dialog.setTitle("Изменение долга");
                dialog.setHeaderText("Изменить сумму долга, добавившегося при доставке");
                dialog.setContentText("Введите положительное целое или дробное число: ");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(
                    debtNewString -> {
                    
                        boolean hasNumFormatErrors = false;
                        try{

                            Pattern pattern = 
                                Pattern.compile("[0-9]{1,5}[.,]{0,1}[0-9]{0,2}");
                            if (pattern.matcher(debtNewString).matches()) {

                                    Double debtNewDouble =
                                        Double.parseDouble(
                                            debtNewString.replaceAll(",", ".")
                                        );
                                    
                                    //Суммируем изменение долга с балансом долга магазина
                                    double deltaDebtValue = 0;
                                    deltaDebtValue = debtNewDouble - oldDebtValue;
                                    Shop saleShop =
                                        mShopsDAOImpl.getShop(
                                            mSelectedSale.getShopId()
                                        );
                                    BigDecimal shopDebt = saleShop.getDebt();
                                    shopDebt =
                                        shopDebt.add(
                                            BigDecimal.valueOf(deltaDebtValue)
                                        );
                                    saleShop.setDebt(shopDebt);
                                    mShopsDAOImpl.updateShop(saleShop);
                                    
                                    //Сохраняем сумму change долга в доставку
                                    //получаем старое значение изменения долга
                                    
                                    BigDecimal saleDebt =
                                        mSelectedSale.getDebt();
                                    //System.out.println(saleDebt);
                                    //System.out.println(deltaDebtValue);
                                    //System.out.println(BigDecimal.valueOf(deltaDebtValue));
                                    //прибавляем разность нового и старого добавившегося долга
                                    saleDebt =
                                        saleDebt.add(
                                            BigDecimal.valueOf(deltaDebtValue)
                                        );
                                    //System.out.println(saleDebt);
                                    //System.out.println(saleDebt.doubleValue());
                                    mSelectedSale.setDebt(
                                        BigDecimal.valueOf(saleDebt.doubleValue())
                                    );
                                    mSalesDAOImpl.updateSale(mSelectedSale);
                                    //окончание
                                    
                                    //обновляем запись в таблице изменений долгов
                                    //new balance:
                                    double newDebtBalanceDouble =
                                        oldDebt.getBalance().doubleValue()
                                            + deltaDebtValue;
                                    DebtChange updatedDebtChange =
                                        new DebtChange(
                                            oldDebt.getId()
                                            , oldDebt.getShopId()
                                            , oldDebt.getIsDebt()
                                            , BigDecimal.valueOf(debtNewDouble)
                                            , oldDebt.getDate()
                                            , oldDebt.getDebtId()
                                            , BigDecimal.valueOf(newDebtBalanceDouble)
                                            , oldDebt.getSaleId()
                                            , oldDebt.isNotReqAmort()
                                            , oldDebt.getIsCredit()
                                        );
                                    
                                    updatedDebtChange.setNotReqAmort(oldDebt.isNotReqAmort());
                                    mDebtChangesDAOImpl.updateDebtChange(updatedDebtChange);
                                    WS1.dayReportsControllerInstance.updateDayReportItemsForPage();
                                    //Notify self
                                    updateSalesForPage();
                                    //нотифицировать контроллер добавления доставок
                                    WS1.addSaleControllerInstance.updateShops();
                                    //
                                    WS1.shopsControllerInstance.updateShopsForPage();
                                    WS1.debtsControllerInstance.updateShops();
                                    WS1.debtsControllerInstance.updateDebtChangesForPage();
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Информация");
                                    alert.setHeaderText("Сумма добавленного при доставке долга изменена");
                                    //Показать сообщение и ждать клика по кнопке Ok
                                    alert.showAndWait();
                            } else {

                                hasNumFormatErrors = true;
                            }
                        } catch (NumberFormatException ex){

                            hasNumFormatErrors = true;
                        }
                        if (hasNumFormatErrors) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка");
                            alert.setHeaderText("Изменение суммы добавленного при доставке долга не выполнено");
                            alert.setContentText("Ошибка формата числа: нужно целое или дробное число (максимум - 99999.99)");
                            alert.showAndWait();
                        }
                    });
            } else {
            
                Alert errorAlert =
                    new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText("Долг отсутствует");
                errorAlert.setContentText("При данной доставке не создавался новый долг. Его можно добавить при помощи кнопки Добавить долг");
                errorAlert.showAndWait();
            }
            //mDebtChangesDAOImpl.
        } else {

            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна доставка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    //Отображение диалога добавления к доставке
    //одного нового погашения долга
    @FXML
    private void showAddSaleDebtAmortDialog(){
    
        progressIndicator.visibleProperty().set(true);

        //
        mSelectedSaleModel =
            (SaleModel) salesTableView
                .getSelectionModel()
                .getSelectedItem();

        //
        if (mSelectedSaleModel != null) {
            
            mSelectedSale =
                mSalesDAOImpl.getSale(mSelectedSaleModel.getId());
            ObservableList<DebtChange> debtChangesObservableList =
                FXCollections.observableArrayList();
            Shop selectedShop =
                mShopsDAOImpl.getShop(mSelectedSale.getShopId());
            List<DebtChange> shopActiveDebts =
                mDebtChangesDAOImpl.getFilteredDebtChanges(
                    selectedShop.getId()
                    , true
                );
            if (shopActiveDebts != null) {
            
                for (DebtChange debtChange : shopActiveDebts) {
                    //отбираем только те долги, которые требуют погашения
                    if (!debtChange.isNotReqAmort()) {

                        debtChangesObservableList.add(debtChange);
                    }
                }
            }
                        
            if (!debtChangesObservableList.isEmpty()) {
                
                List<String> debtChangesStringList = new ArrayList<>();
                for (DebtChange debtChange : debtChangesObservableList) {
                    
                    debtChangesStringList.add(
                        debtChange.getBalance()
                            + " ("
                            + debtChange.getValue()
                            + " грн "
                            + new SimpleDateFormat("dd.MM.yyyy").format(debtChange.getDate())
                            + ")"
                    );
                }
                
                ChoiceDialog<String> dialog =
                    new ChoiceDialog<>(debtChangesStringList.get(0), debtChangesStringList);
                dialog.setTitle("Добавление погашения долга");
                dialog.setHeaderText("Какой долг нужно погасить?");
                dialog.setContentText("Выберите из списка:");
                
                Optional<String> result = dialog.showAndWait();

                result.ifPresent(
                    debtString -> {
                    
                        //System.out.println(debtAmortNewString);
                        
                        DebtChange selectedDebtChangeTmp = null;
                        
                        for (DebtChange debtChange : debtChangesObservableList) {
                            
                            String currentDebtChangeString =
                                debtChange.getBalance()
                                + " ("
                                + debtChange.getValue()
                                + " грн "
                                + new SimpleDateFormat("dd.MM.yyyy").format(debtChange.getDate())
                                + ")";
                            if (currentDebtChangeString.equals(debtString)) {
                                
                                selectedDebtChangeTmp = debtChange;
                                break;
                            }
                        }
                        
                        final DebtChange selectedDebtChange = selectedDebtChangeTmp;
                        
                        TextInputDialog dialog2 =
                            new TextInputDialog(
                                String.valueOf(selectedDebtChange.getBalance())
                            );
                        dialog2.setTitle("Изменение долга");
                        dialog2.setHeaderText("Изменить сумму долга, добавившегося при доставке");
                        dialog2.setContentText("Введите положительное целое или дробное число: ");

                        Optional<String> result2 = dialog2.showAndWait();
                        result2.ifPresent(debtAmortNewString -> {
                                
                            Pattern pattern = 
                                Pattern.compile("[0-9]{1,5}[.,]{0,1}[0-9]{0,2}");
                            if (pattern.matcher(debtAmortNewString).matches()) {

                                Double debtNewAmortDouble =
                                    Double.parseDouble(
                                        debtAmortNewString.replaceAll(",", ".")
                                    );
                                //Если сумма погашения больше суммы баланса выбранного для погашения долга -
                                //показываем сообщение об ошибке и выходим из обработчика события
                                if (debtNewAmortDouble > selectedDebtChange.getBalance().doubleValue()) {

                                    Alert warningAlert =
                                        new Alert(Alert.AlertType.WARNING);
                                    warningAlert.setTitle("Предупреждение");
                                    warningAlert.setHeaderText("Предотвращена попытка погасить долг на сумму, бОльшую, чем его баланс");
                                    warningAlert.setContentText("Введите сумму погашения, меньшую, чем сумма погашаемого долга");
                                    warningAlert.showAndWait();

                                    return;
                                }
                                
                                //Суммируем изменение долга с балансом долга магазина
                                BigDecimal shopDebt = selectedShop.getDebt();
                                shopDebt =
                                    shopDebt.add(
                                        BigDecimal.valueOf(-debtNewAmortDouble)
                                    );
                                selectedShop.setDebt(shopDebt);
                                mShopsDAOImpl.updateShop(selectedShop);
                                
                                //Получаем старое значение изменения долга в доставке
                                BigDecimal saleDebt =
                                    mSelectedSale.getDebt();
                                //Суммируем изменение долга с суммой изменения долга
                                //в доставке
                                saleDebt =
                                    saleDebt.add(
                                        BigDecimal.valueOf(-debtNewAmortDouble)
                                    );
                                mSelectedSale.setDebt(
                                    BigDecimal.valueOf(saleDebt.doubleValue())
                                );
                                mSalesDAOImpl.updateSale(mSelectedSale);
                                
                                //Создаем новое погашение в таблице изменений долгов
                                //и там же уменьшаем баланс в погашаемом долге
                                DebtChange newDebtAmort = new DebtChange();
                                newDebtAmort.setShopId(selectedShop.getId());
                                //'это не долг (это - запись погашения)'
                                newDebtAmort.setIsDebt(false);
                                newDebtAmort.setValue(
                                    BigDecimal.valueOf(debtNewAmortDouble)
                                );
                                newDebtAmort.setDate(mSelectedSale.getCreatedAt());
                                newDebtAmort.setDebtId(selectedDebtChange.getId());
                                //Отрицательный баланс - признак отсутствия баланса, т.к. это не
                                //долг, а погашение
                                newDebtAmort.setBalance(
                                    BigDecimal.valueOf(
                                        -1.0
                                    )
                                );
                                newDebtAmort.setSaleId(mSelectedSale.getId());
                                //Добавляем в БД новую запись погашения
                                mDebtChangesDAOImpl.createDebtChange(newDebtAmort);
                                //Изменяем в БД запись долга, который погашался
                                selectedDebtChange.setBalance(
                                    selectedDebtChange.getBalance().add(
                                        BigDecimal.valueOf(-debtNewAmortDouble)
                                    )
                                );
                                mDebtChangesDAOImpl.updateDebtChange(selectedDebtChange);
                                
                                //***
                                
                                //Вызываем обновление данных в коллекции-источнике для
                                //таблицы магазинов
                                WS1.shopsControllerInstance.updateShopsForPage();
                                //обновляем отображение в отчетах
                                WS1.dayReportsControllerInstance.updateDayReportItemsForPage();
                                //Notify self
                                updateSalesForPage();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateShops();
                                //обновление в разделе долгов
                                WS1.debtsControllerInstance.updateShops();
                                WS1.debtsControllerInstance.updateDebtChangesForPage();
                                //сообщение об успешном погашении
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Информация");
                                alert.setHeaderText("Погашение выбранного долга успешно выполнено");
                                //Показать сообщение и ждать клика по кнопке Ok
                                alert.showAndWait();
                            }
                        });
                    });
            } else {
            
                Alert warningAlert =
                    new Alert(Alert.AlertType.WARNING);
                warningAlert.setTitle("Предупреждение");
                warningAlert.setHeaderText("Погашение долга не добавлено");
                warningAlert.setContentText("У данного магазина нет долгов для погашения");
                warningAlert.showAndWait();
            }
        } else {

            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна доставка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    //Действие изменения суммы изменения долга, произошедшего при доставке
    //(не отражается на других данных системы)
    @FXML
    private void showFreeChangeDebtDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedSaleModel =
            (SaleModel) salesTableView.getSelectionModel().getSelectedItem();
        
        if (mSelectedSaleModel != null) {
            
            Sale selectedSale =
                mSalesDAOImpl.getSale(mSelectedSaleModel.getId());
        
            TextInputDialog dialog =
                new TextInputDialog(String.valueOf(selectedSale.getDebt()));
            dialog.setTitle("Изменение долга");
            dialog.setHeaderText("Сумма изменения долга при доставке в магазин "
                    + mSelectedSaleModel.getShopName());
            dialog.setContentText("Введите целое или дробное число: ");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(
                changeDebtString -> {

                    boolean hasNumFormatErrors = false;
                    try{
                    
                        Pattern pattern = 
                            Pattern.compile("[-]{0,1}[0-9]{1,8}([.][0-9]{1,2}){0,1}");
                        if (pattern.matcher(changeDebtString.replaceAll(",", ".")).matches()) {

                            //
                            selectedSale.setDebt(
                                BigDecimal.valueOf(
                                    Double.parseDouble(changeDebtString.replaceAll(",", "."))
                                )
                            );
                            //
                            mSalesDAOImpl.updateSale(selectedSale);
                            //
                            updateSalesForPage();
                        } else {

                            hasNumFormatErrors = true;
                        }
                    
                    } catch (NumberFormatException ex){

                        hasNumFormatErrors = true;
                    }
                    if (hasNumFormatErrors) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение долга не выполнено");
                        alert.setContentText("Ошибка формата числа (нужно положительное или отрицательное значение, максимум 99999999.99)");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна доставка");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        
        progressIndicator.visibleProperty().set(false);
    }
    /*Code snippets*/
    
    private Node createPage(int _pageIndex) {
        
        progressIndicator.visibleProperty().set(true);
        
        mCurrentPageIdx = _pageIndex;
        
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        
        //getSalesForPage
        mSalesForPage = mSalesFacade.getSalesForPage(mRowsPerPage, fromIndex);
        
        mSalesObservableList.clear();
        fillSalesObservableList(mSalesForPage);
        
        salesTableView.setItems(mSalesObservableList);
        
        AnchorPane.setTopAnchor(salesTableView, 0.0);
        AnchorPane.setRightAnchor(salesTableView, 0.0);
        AnchorPane.setBottomAnchor(salesTableView, 0.0);
        AnchorPane.setLeftAnchor(salesTableView, 0.0);

        progressIndicator.visibleProperty().set(false);
        
        return new AnchorPane(salesTableView);
    }
    
    private void fillSalesObservableList(List<Sale> _salesList){
        
        for (Sale sale : _salesList) {
                        
//            int countOld = sale.getCounterOld();
//            int countNew = sale.getCounterNew();
//            int countDiff = countNew - countOld;
            mSalesObservableList.add(new SaleModel(
                    sale.getId(),
                    mShopsDAOImpl.getShop(sale.getShopId()).getName(),
                    mBarrelsDAOImpl.getBarrel(sale.getBarrelId()).getWhaterTId().getName()
                        + ", "
                        + mBarrelCapacitiesDAOImpl.getBarrelCapacity(
                            mBarrelsDAOImpl.getBarrel(sale.getBarrelId()).getCapacityId()
                        ).getCapacity()
                        + " л",
                    mDriversDAOImpl.getDriver(sale.getDriverId()).getName(),
                    mCarsDAOImpl.getCar(sale.getCarId()).getNumber(),
                    sale.getCounterOld(),
                    sale.getCounterNew(),
                    sale.getVolume(),
                    sale.getCleaning(),
                    sale.getRepair(),
                    sale.getToPay().doubleValue(),
                    sale.getProfit().doubleValue(),
                    sale.getDebt().doubleValue(),
                    new SimpleDateFormat("yyyy.MM.dd").format(sale.getCreatedAt()),
                    sale.getNotice()
                )
            );
        }        
    }
    
    //Вызываем обновление данных в коллекции-источнике для
    //таблицы доставок (вызывается из контроллера представления "Добавить доставку")
    //после успешного добавления новой доставки
    //или из кода фильтра в текущем контроллере, если данные фильтрации изменились
    public void updateSalesForPage(){
        
        int salesAllCount = mSalesDAOImpl.getSalesCount();
        salesPagination.setPageCount(
                salesAllCount > 0
                ? (salesAllCount + mRowsPerPage - 1) / mRowsPerPage
                : 1
        );
        
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        mSalesForPage = mSalesFacade.getSalesForPage(mRowsPerPage, fromIndex);
        //TODO Вычислять количество страниц таблицы по полному количеству
        //строк в фильтрованной выборке
        mSalesObservableList.clear();
        fillSalesObservableList(mSalesForPage);
    }
    
    //Приведение формы filter в исходное состояние
    private void resetFilterForm(){
        
        progressIndicator.visibleProperty().set(true);
        
        shopCTextField.clear();
        carCTextField.clear();
        driverCTextField.clear();
        
        mShopNamesSet.clear();
        for (Shop shop : mShops) {
            mShopNamesSet.add(shop.getName());
        }
        //отписываемся от предыдущего набора автодополнения
        if (mShopAutoCompletionBinding != null) {
            
            mShopAutoCompletionBinding.dispose();
        }
        //подписываемся на новый набор автодополнения
        mShopAutoCompletionBinding = TextFields.bindAutoCompletion(shopCTextField, mShopNamesSet);
        
        mCarNamesSet.clear();
        for (Car car : mCars) {
            mCarNamesSet.add(String.valueOf(car.getNumber()));
        }
        if (mCarAutoCompletionBinding != null) {
            
            mCarAutoCompletionBinding.dispose();
        }
        mCarAutoCompletionBinding = TextFields.bindAutoCompletion(carCTextField, mCarNamesSet);
        
        mDriverNamesSet.clear();
        for (Driver driver : mDrivers) {
            mDriverNamesSet.add(driver.getName());
        }
        if (mDriverAutoCompletionBinding != null) {
            
            mDriverAutoCompletionBinding.dispose();
        }
        mDriverAutoCompletionBinding = TextFields.bindAutoCompletion(driverCTextField, mDriverNamesSet);
        
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setValue(LocalDate.now());
        
        progressIndicator.visibleProperty().set(false);
    }
    
    public void updateShops(){
        
        mShops = mShopsDAOImpl.getAllShops();
        resetFilterForm();
    }
    
    public void updateBarrels(){
        
        mBarrels = mBarrelsDAOImpl.getAllBarrels();
        resetFilterForm();
    }
    
    public void updateCars(){
        
        mCars = mCarsDAOImpl.getAllCars();
        resetFilterForm();
    }
    
    public void updateDrivers(){
        
        mDrivers = mDriversDAOImpl.getAllDrivers();
        resetFilterForm();
    }
    
    /*public void hideSaleEditCounterNewButton(){
    
        saleEditCounterNewButton.setVisible(false);
        saleEditCounterNewButton.setDisable(true);
    }*/
    
    public void hideEditSaleButtons(){
    
        editSaleButton.setVisible(false);
        editSaleButton.setDisable(true);
        
        addSaleDebtButton.setVisible(false);
        addSaleDebtButton.setDisable(true);
        
        editSaleDebtButton.setVisible(false);
        editSaleDebtButton.setDisable(true);
        
        addSaleDebtAmortButton.setVisible(false);
        addSaleDebtAmortButton.setDisable(true);
        
        deleteSaleButton.setVisible(false);
        deleteSaleButton.setDisable(true);
        
        freeChangeDebtButton.setVisible(false);
        freeChangeDebtButton.setDisable(true);
    }

    public SaleModel getSelectedSaleModel()
    {
        return mSelectedSaleModel;
    }
}
