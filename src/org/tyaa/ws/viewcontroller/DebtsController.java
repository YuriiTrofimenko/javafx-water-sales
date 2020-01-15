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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
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
import org.tyaa.ws.dao.DebtsFacade;
import org.tyaa.ws.dao.impl.DebtChangesDAOImpl;
import org.tyaa.ws.dao.impl.DebtsDAOImpl;
import org.tyaa.ws.dao.impl.ShopsDAOImpl;
import org.tyaa.ws.entity.DebtChange;
import org.tyaa.ws.entity.Shop;
import org.tyaa.ws.viewmodel.DebtModel;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class DebtsController implements Initializable, ControlledScreen {

    @FXML
    private TableView debtsTableView;
    
    @FXML
    private TableColumn idTableColumn;
    @FXML
    private TableColumn shopTableColumn;
    @FXML
    private TableColumn typeTableColumn;
    @FXML
    private TableColumn isSaleTableColumn;
    @FXML
    private TableColumn valueTableColumn;
    @FXML
    private TableColumn forDebtTableColumn;
    @FXML
    private TableColumn balanceTableColumn;
    @FXML
    private TableColumn dateTableColumn;
    @FXML
    private TableColumn isNotReqAmortTableColumn;
    
    @FXML
    private Button debtAmortButton;
    @FXML
    private Button creditAmortButton;
    
    @FXML
    private Pagination debtsPagination;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    /*For filter*/
    @FXML
    CheckBox filterCheckBox;
    @FXML
    CustomTextField shopCTextField;
    @FXML
    CheckBox onlyActiveDebtsCheckBox;
    @FXML
    CheckBox onlyActiveCreditsCheckBox;
    /*End*/
    
    ScreensController myController;
    
    private ObservableList<DebtModel> mDebtsObservableList;
    
    private List<DebtChange> mDebtChangesForPage;
    
    private DebtsDAOImpl mDebtsDAOImpl;
    private DebtChangesDAOImpl mDebtChangesDAOImpl;
    private ShopsDAOImpl mShopsDAOImpl;
    
    private DebtsFacade mDebtsFacade;
    
    /*For filter*/
    
    //for all shops list
    private List<Shop> mShops;
    
    //for allowed names lists
    private Set<String> mShopNamesSet;
    
    private AutoCompletionBinding<String> mShopAutoCompletionBinding;
    
    private Shop mSelectedShop;
    private Shop mSelectedInFilterShop;
    
    /*End*/
    
    //Объект выбранной строки таблицы
    private DebtModel mSelectedDebtModel;
    //Объект выбранн
    private DebtChange mSelectedDebtChange;
    
    private static int mCurrentPageIdx;
    private static int mRowsPerPage = 25;
    
    private int mDebtChangesCount;
//    ValidationSupport validationSupport;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        WS1.debtsControllerInstance = this;
        
        progressIndicator.visibleProperty().set(false);
        
        mDebtsObservableList = FXCollections.observableArrayList();
        
        mDebtsDAOImpl = new DebtsDAOImpl();
        mDebtChangesDAOImpl = new DebtChangesDAOImpl();
        mShopsDAOImpl = new ShopsDAOImpl();
        
        mDebtsFacade = new DebtsFacade();
        
        mShops = mShopsDAOImpl.getAllShops();
        mShopNamesSet = new HashSet<>();
        
        resetFilterForm();
        
        //По умолчанию после загрузки приложения действие визуального фильтра отключено,
        //и действие фильтра "только активные" - отключено
        mDebtsFacade.setFilter(false);
        mDebtsFacade.setFilterShopId(-1);
        mDebtsFacade.setActive(-1);
        mDebtsFacade.setActiveCredits(-1);
        
        setPageCount();
        
        //System.out.println((barrelsCount + mRowsPerPage - 1) / mRowsPerPage);
        
        debtsPagination.setPageFactory(
            new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {          
                    return createPage(pageIndex);               
                }
            }
        );
                
        idTableColumn.setCellValueFactory(
                new PropertyValueFactory<DebtModel, String>("id")
        );
        shopTableColumn.setCellValueFactory(
                new PropertyValueFactory<DebtModel, String>("shop")
        );
        typeTableColumn.setCellValueFactory(
                new PropertyValueFactory<DebtModel, String>("type")
        );
        isSaleTableColumn.setCellValueFactory(
                new PropertyValueFactory<DebtModel, String>("isSale")
        );
        valueTableColumn.setCellValueFactory(
                new PropertyValueFactory<DebtModel, String>("value")
        );
        forDebtTableColumn.setCellValueFactory(
                new PropertyValueFactory<DebtModel, String>("forDebt")
        );
        balanceTableColumn.setCellValueFactory(
                new PropertyValueFactory<DebtModel, String>("balance")
        );
        dateTableColumn.setCellValueFactory(
                new PropertyValueFactory<DebtModel, String>("date")
        );
        isNotReqAmortTableColumn.setCellValueFactory(
                new PropertyValueFactory<DebtModel, String>("notReqAmort")
        );
        
        //отображение типа изменения: долг или погашение
        typeTableColumn.setCellFactory(column ->{
            
            return new TableCell<DebtModel, Integer>(){
                @Override
                protected void updateItem(Integer item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        if (item == 1) {
                            setText("Долг");
                            setTextFill(Color.RED);
                        } else {
                            if (item == 0) {
                                setText("Возврат");
                                setTextFill(Color.GREEN);
                            } else if (item == 2) {
                                setText("Аванс");
                                setTextFill(Color.BLUE);
                            }
                        }
                    }
                }
            };
        });
        //отображение ячеек "при доставке?" - да или нет
        isSaleTableColumn.setCellFactory(column ->{
            
            return new TableCell<DebtModel, Boolean>(){
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
                        } else {
                            setText("Нет");
                        }
                    }
                }
            };
        });
        
        //отображение shop name
        shopTableColumn.setCellFactory(column ->{
            
            return new TableCell<DebtModel, Integer>(){
                @Override
                protected void updateItem(Integer item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(
                            mShopsDAOImpl.getShop(item).getName()
                        );
                    }
                }
            };
        });
        
        //отображение баланса (если нулевой - зеленый, ненулевой - красный)
        balanceTableColumn.setCellFactory(column ->{
            
            return new TableCell<DebtModel, Double>(){
                @Override
                protected void updateItem(Double item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        if (item == -1.0d) {
                            setText("");
                        } else {
                            setText(item.toString());
                        }
                        if (item == 0.0d) {
                            setTextFill(Color.BLUE);
                        } else if (item > 0.0d) {
                            setTextFill(Color.RED);
                        } else {
                            setTextFill(Color.BLACK);
                        }
                    }
                }
            };
        });
        
        //отображение ячеек "возврат не требуется?" - да или нет
        isNotReqAmortTableColumn.setCellFactory(column ->{
            
            return new TableCell<DebtModel, Boolean>(){
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
                            setTextFill(Color.GREEN);
                        } else {
                            setText("");
                        }
                    }
                }
            };
        });
        
        //отображение информации о долге, за который погашение
        //(если это не погашение - ставить "-")
        /*forDebtTableColumn.setCellFactory(column ->{
            
            return new TableCell<DebtModel, Integer>(){
                @Override
                protected void updateItem(Integer item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        if (item == -1) {
                            setText("-");
                            //setTextFill(Color.RED);
                        } else {
                            setText(
                                " ("
                                + mDebtsDAOImpl.getDebt(item.)//debtChangeItem.getValue()
                                + " грн "
                                + new SimpleDateFormat("dd.MM.yyyy").format(debtChangeItem.getDate())
                                + ")"
                            );
                            //setTextFill(Color.GREEN);
                        }
                    }
                }
            };
        });*/
        /*barrelReplaceTableColumn.setCellFactory(column ->{
            
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
        });*/
        
        //
        dateTableColumn.setCellFactory(column ->{
            
            return new TableCell<DebtModel, String>(){
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
        
        //обработка события "переключение чекбокса Filter on/off"
        filterCheckBox.setOnAction((event) -> {
            if (filterCheckBox.isSelected()) {
                //устанавливаем в фасаде режим "фильтр"
                mDebtsFacade.setFilter(true);
            } else {
                mDebtsFacade.setFilter(false);
            }
            updateDebtChangesForPage();
        });
        
        //обработка события "ввод названия магазина"
        shopCTextField.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                progressIndicator.visibleProperty().set(true);
                
                mDebtsObservableList.clear();
                
                if (!shopCTextField.textProperty().getValue().equals("")
                        && mShopNamesSet.contains(shopCTextField.textProperty().getValue())) {
                    
                    //получаем объект выбранного магазина
                    for (Shop shop : mShops) {
                        //
                        if (shop.getName().equals(shopCTextField.textProperty().getValue())) {
                            mSelectedInFilterShop = shop;
                            break;
                        }
                    }
                    
                    //устанавливаем в фасаде debts значение "ID магазина" для фильтра;
                    //вызываем в фасаде debts срабатывание фильтра
                    
                    mDebtsFacade.setFilterShopId(mSelectedInFilterShop.getId());
                } else {
                    
                    //иначе отправляем фасаду аргумент-сигнал "не выбран магазин"
                    mDebtsFacade.setFilterShopId(-1);
                }
                
                //обновляем содержимое страницы таблицы полученными данными
                updateDebtChangesForPage();
                
                progressIndicator.visibleProperty().set(false);
            }
        });
        
        //обработка события "переключение чекбокса только активные долги"
        onlyActiveDebtsCheckBox.setOnAction((event) -> {
            
            progressIndicator.visibleProperty().set(true);
            
            if (onlyActiveDebtsCheckBox.isSelected()) {
                
                mDebtsFacade.setActive(1);
                onlyActiveCreditsCheckBox.setDisable(true);
            } else {
                
                mDebtsFacade.setActive(-1);
                onlyActiveCreditsCheckBox.setDisable(false);
            }
            updateDebtChangesForPage();
            progressIndicator.visibleProperty().set(false);
        });
        
        //обработка события "переключение чекбокса only active credits"
        onlyActiveCreditsCheckBox.setOnAction((event) -> {
            
            progressIndicator.visibleProperty().set(true);
            
            if (onlyActiveCreditsCheckBox.isSelected()) {
                
                mDebtsFacade.setActiveCredits(1);
                onlyActiveDebtsCheckBox.setDisable(true);
            } else {
                
                mDebtsFacade.setActiveCredits(-1);
                onlyActiveDebtsCheckBox.setDisable(false);
            }
            updateDebtChangesForPage();
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
    
    /*@FXML
    private void goToAddAmortDebtScreen(ActionEvent event){
       myController.setScreen(WS1.addBarrelID);
       WS1.primaryStage.setMaximized(false);
       WS1.primaryStage.setWidth(600);
       WS1.primaryStage.setHeight(600);
       WS1.primaryStage.setY(30);
    }*/
    
    @FXML
    private void showDebtAmortDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedDebtModel =
            (DebtModel) debtsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedDebtModel != null) {
            
            if (mSelectedDebtModel.getForDebt().equals("-")
                && mSelectedDebtModel.getType() != 2) {
                
                TextInputDialog dialog = new TextInputDialog("0");
                dialog.setTitle("Возврат долга");
                dialog.setHeaderText("Сумма погашения долга магазином "
                        + mShopsDAOImpl.getShop(mSelectedDebtModel.getShopId()).getName());
                dialog.setContentText("Введите положительное целое или дробное число: ");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(
                    changeDebtString -> {

                        boolean hasNumFormatErrors = false;

                        Pattern pattern = 
                            Pattern.compile("[-]{0,1}[0-9]{1,8}([.][0-9]{1,2}){0,1}");
                        if (pattern.matcher(changeDebtString.replaceAll(",", ".")).matches()) {

                            //Находим долг, за который нужно сделать погашение
                            mSelectedDebtChange =
                                    mDebtsDAOImpl.getDebt(mSelectedDebtModel.getId());

                            //Находим магазин, долг которого нужно погасить
                            mSelectedShop =
                                    mShopsDAOImpl.getShop(mSelectedDebtModel.getShopId());
                            BigDecimal debt = mSelectedShop.getDebt();
                            try{

                                BigDecimal changeDebt =
                                    BigDecimal.valueOf(Double.valueOf(changeDebtString.replaceAll(",", ".")));
                                if (changeDebt.doubleValue() > mSelectedDebtChange.getBalance().doubleValue()) {
                                    
                                    throw new NumberFormatException();
                                }
                                debt = debt.subtract(changeDebt);
                                
                                //Добавляем строку "изменение долга" в таблицу БД
                                DebtChange debtChange = new DebtChange();
                                debtChange.setShopId(mSelectedShop.getId());
                                debtChange.setIsDebt(false);
                                debtChange.setDate(Date.from(Instant.now()));
                                debtChange.setValue(changeDebt);
                                debtChange.setDebtId(mSelectedDebtChange.getId());
                                debtChange.setBalance(
                                    BigDecimal.valueOf(
                                        -1.0
                                    )
                                );
                                debtChange.setSaleId(-1);
                                
                                /*mDebtsDAOImpl.createDebtChange(debtChange);
                                
                                //Изменяем в БД запись долга, который погашался
                                mSelectedDebtChange.setBalance(
                                    mSelectedDebtChange.getBalance().subtract(changeDebt)
                                );
                                mDebtsDAOImpl.updateDebtChange(mSelectedDebtChange);*/
                                
                                //Транзакционное погашение долга (создание записи о погашении
                                // и изменение баланса погашаемого долга)
                                int transResult = mDebtChangesDAOImpl.doAmortDebtChange(
                                    mSelectedDebtChange
                                        , debtChange);

                                if (transResult == -1) {

                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Ошибка");
                                    alert.setHeaderText("Выбранный долг не погашен");
                                    alert.setContentText("Сбой в системе не позволил выполнить транзакцию погашения долга");
                                    alert.showAndWait();
                                } else {
                                
                                    //Вносим изменение долга в БД для магазина
                                    mSelectedShop.setDebt(debt);
                                    mShopsDAOImpl.updateShop(mSelectedShop);
                                    
                                    //Вызываем обновление данных в коллекции-источнике для
                                    //таблицы магазинов
                                    WS1.shopsControllerInstance.updateShopsForPage();
                                    //
                                    WS1.salesControllerInstance.updateShops();
                                    WS1.salesControllerInstance.updateSalesForPage();
                                    //
                                    WS1.addSaleControllerInstance.updateShops();
                                    WS1.addSaleControllerInstance.updateDebts();
                                    //
                                    updateShops();
                                    updateDebtChangesForPage();
                                }
                            } catch (NumberFormatException ex){

                                hasNumFormatErrors = true;
                            }
                        } else {

                            hasNumFormatErrors = true;
                        }
                        if (hasNumFormatErrors) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка");
                            alert.setHeaderText("Изменение долга не выполнено");
                            alert.setContentText("Ошибка формата числа (нужно положительное или отрицательное значение, максимум 99999999.99, меньшее, чем баланс погашаемого долга)");
                            alert.showAndWait();
                        }
                    }
                );
            } else {
            
                Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
                warningAlert.setTitle("Предупреждение");
                warningAlert.setHeaderText("Выбрана строка неверного типа: погашение или аванс вместо долга");
                warningAlert.setContentText("Выделите строку долга в таблице");
                warningAlert.showAndWait();
            }
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни один долг");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showCreditAmortDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedDebtModel =
            (DebtModel) debtsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedDebtModel != null) {
            
            if (mSelectedDebtModel.getType() == 2) {
                
                TextInputDialog dialog = new TextInputDialog("0");
                dialog.setTitle("Погашение аванса");
                dialog.setHeaderText("Сумма использованной части аванса магазина "
                        + mShopsDAOImpl.getShop(mSelectedDebtModel.getShopId()).getName());
                dialog.setContentText("Введите положительное целое или дробное число: ");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(
                    changeCreditString -> {

                        boolean hasNumFormatErrors = false;

                        Pattern pattern = 
                            Pattern.compile("[-]{0,1}[0-9]{1,8}([.][0-9]{1,2}){0,1}");
                        if (
                                pattern.matcher(
                                    changeCreditString.replaceAll(",", ".")
                                ).matches()
                        ) {

                            //Находим аванс, для которого нужно сделать погашение
                            mSelectedDebtChange =
                                    mDebtsDAOImpl.getDebt(mSelectedDebtModel.getId());

                            BigDecimal creditBalance =
                                mSelectedDebtChange.getBalance();
                            try{

                                BigDecimal changeCredit =
                                    BigDecimal.valueOf(
                                        Double.valueOf(changeCreditString.replaceAll(",", "."))
                                    );
                                if (changeCredit.doubleValue() > creditBalance.doubleValue()) {
                                    
                                    throw new NumberFormatException();
                                }
                                
                                //Изменяем в БД запись credit, который погашался
                                mSelectedDebtChange.setBalance(
                                    creditBalance.subtract(changeCredit)
                                );
                                mDebtsDAOImpl.updateDebtChange(mSelectedDebtChange);
                                
                                //Вызываем обновление данных в коллекции-источнике для
                                //таблицы магазинов
                                WS1.shopsControllerInstance.updateShopsForPage();
                                //
                                WS1.salesControllerInstance.updateShops();
                                WS1.salesControllerInstance.updateSalesForPage();
                                //
                                WS1.addSaleControllerInstance.updateShops();
                                WS1.addSaleControllerInstance.updateDebts();
                                //
                                updateShops();
                                updateDebtChangesForPage();
                            } catch (NumberFormatException ex){

                                hasNumFormatErrors = true;
                            }
                        } else {

                            hasNumFormatErrors = true;
                        }
                        if (hasNumFormatErrors) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка");
                            alert.setHeaderText("Изменение аванса не выполнено");
                            alert.setContentText("Ошибка формата числа (нужно положительное или отрицательное значение, максимум 99999999.99), меньшее или равное балансу");
                            alert.showAndWait();
                        }
                    }
                );
            } else {
            
                Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
                warningAlert.setTitle("Предупреждение");
                warningAlert.setHeaderText("Выбрана строка неверного типа");
                warningAlert.setContentText("Выделите строку аванса в таблице");
                warningAlert.showAndWait();
            }
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни один аванс");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        
        progressIndicator.visibleProperty().set(false);
    }
    
    private void fillDebtsObservableList(List<DebtChange> _debtsList){
        
        String forDebtString = "";
        
        for (DebtChange debtChange : _debtsList) {
            
            if (debtChange.getDebtId() == -1) {
                
                forDebtString = "-";
            } else {
            
                DebtChange amortedDebt = mDebtsDAOImpl.getDebt(debtChange.getDebtId());
                forDebtString = amortedDebt.getValue()
                        + " грн "
                        + new SimpleDateFormat("dd.MM.yyyy").format(amortedDebt.getDate());
            }
                        
            Integer rowType = null;
            
            if (debtChange.getIsDebt()) {
                
                rowType = 1;
            } else {
            
                if (debtChange.getIsCredit()) {
                    
                    rowType = 2;
                } else {
                
                    rowType = 0;
                }
            }
            
            mDebtsObservableList.add(new DebtModel(
                    debtChange.getId(),
                    //mShopsDAOImpl.getShop(debtChange.getShopId()).getName(),
                    debtChange.getShopId(),
                    rowType,
                    (debtChange.getSaleId() != -1),
                    debtChange.getValue().doubleValue(),
                    forDebtString,
                    debtChange.getBalance().doubleValue(),
                    new SimpleDateFormat("yyyy.MM.dd").format(debtChange.getDate()),
                    debtChange.isNotReqAmort()
                )
            );
        }        
    }
    
    //page of the table
    private Node createPage(int _pageIndex) {
        
        progressIndicator.visibleProperty().set(true);
        
        mCurrentPageIdx = _pageIndex;
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        //System.out.println("fromIndex " + fromIndex);
        mDebtChangesForPage = mDebtsFacade.getDebtChangesForPage(mRowsPerPage, fromIndex);
        //System.out.println("mRowsPerPage " + mRowsPerPage);
        mDebtsObservableList.clear();
        
        fillDebtsObservableList(mDebtChangesForPage);
        
        debtsTableView.setItems(mDebtsObservableList);
        
        AnchorPane.setTopAnchor(debtsTableView, 0.0);
        AnchorPane.setRightAnchor(debtsTableView, 0.0);
        AnchorPane.setBottomAnchor(debtsTableView, 0.0);
        AnchorPane.setLeftAnchor(debtsTableView, 0.0);

        progressIndicator.visibleProperty().set(false);
        
        return new AnchorPane(debtsTableView);
    }
    
    public void updateDebtChangesForPage(){
        
        setPageCount();
        
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        mDebtChangesForPage = mDebtsFacade.getDebtChangesForPage(mRowsPerPage, fromIndex);
        mDebtsObservableList.clear();
        fillDebtsObservableList(mDebtChangesForPage);
        
        //setPageCount();
    }
    
    //Приведение формы filter в исходное состояние
    private void resetFilterForm(){
        
        shopCTextField.clear();
        
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
    }
    
    //To global class!
    public void updateShops(){
        
        mShops = mShopsDAOImpl.getAllShops();
        //resetFilterForm();
    }
    
    private void setPageCount(){
        
        //Настраиваем пагинатор общим количеством элементов в коллекции
        int debtChangesAllCount = mDebtsDAOImpl.getDebtChangesCount();
        //System.out.println("debtChangesAllCount " + debtChangesAllCount);
        debtsPagination.setPageCount(
            debtChangesAllCount > 0
            ? (debtChangesAllCount + mRowsPerPage - 1) / mRowsPerPage
            : 1
        );
        //System.out.println("Count " + (debtChangesAllCount + mRowsPerPage - 1) / mRowsPerPage);
    }
    
    public void hideEditDebtsButtons(){
    
        debtAmortButton.setVisible(false);
        debtAmortButton.setDisable(true);
        
        creditAmortButton.setVisible(false);
        creditAmortButton.setDisable(true);
    }
}
