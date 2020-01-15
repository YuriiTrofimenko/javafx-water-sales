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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.tyaa.ws.WS1;
import org.tyaa.ws.dao.ShopsFacade;
import org.tyaa.ws.dao.impl.DebtChangesDAOImpl;
import org.tyaa.ws.dao.impl.ShopsDAOImpl;
import org.tyaa.ws.entity.Shop;
import org.tyaa.ws.viewmodel.ShopModel;
import javafx.scene.control.TextInputDialog;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.tyaa.ws.dao.impl.BarrelsDAOImpl;
import org.tyaa.ws.entity.Barrel;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class ShopsController implements Initializable, ControlledScreen {

    @FXML
    private TableView shopsTableView;
    
    @FXML
    private TableColumn shopIdTableColumn;
    @FXML
    private TableColumn numTableColumn;
    @FXML
    private TableColumn shopNameTableColumn;
    @FXML
    private TableColumn shopAddressTableColumn;
    @FXML
    private TableColumn shopPhoneTableColumn;
    @FXML
    private TableColumn shopBCDateTableColumn;
    @FXML
    private TableColumn shopCTermsTableColumn;
    @FXML
    private TableColumn shopLegalNameTableColumn;
    @FXML
    private TableColumn shopFarTableColumn;
    @FXML
    private TableColumn shopDebtTableColumn;
    
    //Edit buttons
    @FXML
    private Button shopEditNameButton;
    @FXML
    private Button shopAddressEditNameButton;
    @FXML
    private Button shopEditLegalNameButton;
    @FXML
    private Button shopEditPhoneButton;
    @FXML
    private Button shopEditBCDateButton;
    @FXML
    private Button shopEditCTermsButton;
    @FXML
    private Button shopEditFarButton;
    @FXML
    private Button freeChangeDebtButton;
    
    @FXML
    private Pagination shopsPagination;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    /*For filter*/
    @FXML
    CheckBox filterCheckBox;
    @FXML
    CustomTextField shopCTextField;
    @FXML
    CustomTextField legalShopCTextField;
    @FXML
    CustomTextField phoneShopCTextField;
    /*End*/
    
    ScreensController myController;
    
    private ObservableList<ShopModel> mShopsObservableList;
    
    private List<Shop> mShopsForPage;
    
    private ShopsDAOImpl mShopsDAOImpl;
    private DebtChangesDAOImpl mDebtChangesDAOImpl;
    
    private ShopsFacade mShopsFacade;
    private BarrelsDAOImpl mBarrelsDAOImpl;
    
    //Магазин, выбранный при помощи выделения строки в таблице
    private ShopModel mSelectedShopModel;
    private Shop mSelectedShop;
    
    //for all shops list
    private List<Shop> mShops;
    //for allowed names lists
    private Set<String> mShopNamesSet;
    private Set<String> mShopLegalNamesSet;
    private Set<String> mShopPhonesSet;
    //
    private Shop mSelectedFilterShop;
    private AutoCompletionBinding<String> mShopAutoCompletionBinding;
    private AutoCompletionBinding<String> mShopLegalAutoCompletionBinding;
    private AutoCompletionBinding<String> mShopPhoneAutoCompletionBinding;
    
    //Номер текущей страницы таблицы магазинов
    private int mCurrentPageIdx;
    //Число строк на одной странице таблицы магазинов
    private int mRowsPerPage = 25;
    
    private int mShopsAllCount = 0;
//    ValidationSupport validationSupport;
//    RemoteServiceRemote remoteServiceRemote;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        WS1.shopsControllerInstance = this;
        
        progressIndicator.visibleProperty().set(false);
        
        mShopsObservableList = FXCollections.observableArrayList();
        
        mShopsDAOImpl = new ShopsDAOImpl();
        mDebtChangesDAOImpl = new DebtChangesDAOImpl();
        
        mShopsFacade = new ShopsFacade();
        mBarrelsDAOImpl = new BarrelsDAOImpl();
        
        mShops = WS1.barrelsControllerInstance.getShops();
        mShopNamesSet = new HashSet<>();
        mShopLegalNamesSet = new HashSet<>();
        mShopPhonesSet = new HashSet<>();
        
        resetFilterForm();
        
        mShopsFacade.setFilter(false);
        mShopsFacade.setFilterShopId(-1);
        mShopsFacade.setFilterShopLegalName(null);
        mShopsFacade.setFilterShopPhone(null);
        mShopsFacade.setActive(1);
        
        //shopsPagination.setPageCount((mShopsDAOImpl.getShopsCount() / 2 + 1));
        int shopsAllCount = mShopsDAOImpl.getShopsCount();
        mShopsAllCount = shopsAllCount;
        //setNumCellFactory();
        shopsPagination.setPageCount(
                shopsAllCount > 0
                ? (shopsAllCount + mRowsPerPage - 1) / mRowsPerPage
                : 1
        );
        
        shopsPagination.setPageFactory(
            new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {          
                    return createPage(pageIndex);               
                }
            }
        );
        
        shopIdTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("id")
        );
        shopNameTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("shopName")
        );
        shopAddressTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("address")
        );
        shopPhoneTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("phone")
        );
        shopBCDateTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("bCDate")
        );
        shopCTermsTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("cTerms")
        );
        shopLegalNameTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("legalName")
        );
        shopFarTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("far")
        );
        shopDebtTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("debt")
        );
        
        //Вызов метода сквозной автонумерации строк в таблице
        setNumCellFactory();
        
        /*numTableColumn.setCellFactory(column ->{
            
            return new TableCell(){
                @Override
                public void updateItem(Object item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setGraphic( null );
                    setText( empty ? null : mShopsAllCount - getIndex() + "");
                }
            };
        });*/
        
        shopFarTableColumn.setCellFactory(column ->{
            
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
        
        //
        shopBCDateTableColumn.setCellFactory(column ->{
            
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
                        }
                    }
                }
            };
        });
        
        /*Обработчики событий для контролов фильтра*/
        
        //обработка события "переключение чекбокса Filter on/off"
        filterCheckBox.setOnAction((event) -> {
            if (filterCheckBox.isSelected()) {
                //устанавливаем в фасаде магазинов режим "фильтр"
                mShopsFacade.setFilter(true);
            } else {
                mShopsFacade.setFilter(false);
            }
            updateShopsForPage();
        });
        
        //обработка события "ввод названия магазина"
        shopCTextField.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                progressIndicator.visibleProperty().set(true);
                
                mShopsObservableList.clear();
                
                if (!shopCTextField.textProperty().getValue().equals("")
                        && mShopNamesSet.contains(shopCTextField.textProperty().getValue())) {
                    
                    //получаем объект выбранного магазина
                    for (Shop shop : mShops) {
                        
                        if (shop.getName().equals(shopCTextField.textProperty().getValue())) {
                            
                            mSelectedFilterShop = shop;
                            break;
                        }
                    }
                    
                    //устанавливаем в фасаде shops значение "ID магазина" для фильтра;
                    //вызываем в фасаде shops срабатывание фильтра
                    
                    mShopsFacade.setFilterShopId(mSelectedFilterShop.getId());
                } else {
                    
                    //иначе отправляем фасаду аргумент-сигнал "не выбран магазин"
                    mShopsFacade.setFilterShopId(-1);
                }
                
                //обновляем содержимое страницы таблицы полученными данными
                updateShopsForPage();
                
                progressIndicator.visibleProperty().set(false);
            }
        });
        
        //обработка события "ввод имени ЧП"
        legalShopCTextField.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                progressIndicator.visibleProperty().set(true);
                
                mShopsObservableList.clear();
                
                if (!legalShopCTextField.textProperty().getValue().equals("")
                        && mShopLegalNamesSet.contains(legalShopCTextField.textProperty().getValue())) {
                    
                    //устанавливаем в фасаде shops значение "legal name" для фильтра;
                    //вызываем в фасаде shops срабатывание фильтра
                    mShopsFacade.setFilterShopLegalName(legalShopCTextField.getText());
                } else {
                    
                    //иначе отправляем фасаду аргумент-сигнал "не выбран legal name"
                    mShopsFacade.setFilterShopLegalName(null);
                }
                
                //обновляем содержимое страницы таблицы полученными данными
                updateShopsForPage();
                
                progressIndicator.visibleProperty().set(false);
            }
        });
        
        //обработка события "ввод phone"
        phoneShopCTextField.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                progressIndicator.visibleProperty().set(true);
                
                mShopsObservableList.clear();
                
                if (!phoneShopCTextField.textProperty().getValue().equals("")
                        && mShopPhonesSet.contains(phoneShopCTextField.textProperty().getValue())) {
                    
                    //устанавливаем в фасаде shops значение "legal name" для фильтра;
                    //вызываем в фасаде shops срабатывание фильтра
                    mShopsFacade.setFilterShopPhone(phoneShopCTextField.getText());
                } else {
                    
                    //иначе отправляем фасаду аргумент-сигнал "не выбран legal name"
                    mShopsFacade.setFilterShopPhone(null);
                }
                
                //обновляем содержимое страницы таблицы полученными данными
                updateShopsForPage();
                
                progressIndicator.visibleProperty().set(false);
            }
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
    
    //действие добавление магазина
    @FXML
    private void goToAddShopScreen(ActionEvent event){
       myController.setScreen(WS1.addShopID);
       WS1.primaryStage.setMaximized(false);
       WS1.primaryStage.setWidth(600);
       WS1.primaryStage.setHeight(600);
       WS1.primaryStage.setY(30);
    }
    
    /*Действия изменения свойств магазина*/
    
    @FXML
    private void showShopEditNameDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedShopModel =
            (ShopModel) shopsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedShopModel != null) {
            
            mSelectedShop =
                mShopsDAOImpl.getShop(mSelectedShopModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(mSelectedShop.getName());
            dialog.setTitle("Изменение названия");
            dialog.setHeaderText("Какое название магазина?");
            dialog.setContentText("Введите новое название: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                nameString -> {

                    boolean hasErrors = false;
                    
                        Pattern pattern = 
                            Pattern.compile(".{1,64}");
                        if (pattern.matcher(nameString).matches()) {

                                mSelectedShop.setName(nameString);
                                mShopsDAOImpl.updateShop(mSelectedShop);
                                //Notify self
                                updateShopsForPage();
                                WS1.salesControllerInstance.updateShops();
                                WS1.barrelsControllerInstance.updateShops();
                                WS1.addBarrelControllerInstance.updateShops();
                                //update visual filter's data
                                //WS1.shopsControllerInstance.updateFilterShops();
                                
                                mShopNamesSet.clear();
                                mShops = WS1.barrelsControllerInstance.getShops();
                                
                                for (Shop shop : mShops) {

                                    if (shop.getActive()) {

                                        mShopNamesSet.add(shop.getName());
                                    }
                                }
                                String shopName = shopCTextField.getText();
                                shopCTextField.clear();
                                
                                //отписываемся от предыдущего набора автодополнения
                                if (mShopAutoCompletionBinding != null) {

                                    mShopAutoCompletionBinding.dispose();
                                }
                                //подписываемся на новый набор автодополнения
                                mShopAutoCompletionBinding = TextFields.bindAutoCompletion(shopCTextField, mShopNamesSet);
                                if (!shopName.equals("")) {
                                    shopCTextField.setText(nameString);
                                }
                                
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateShops();
                        } else {

                            hasErrors = true;
                        }
                    if (hasErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение названия не выполнено");
                        alert.setContentText("Ошибка длины названия: нужна строка от 1 до 64 символов");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни однин магазин");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showShopAddressEditNameDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedShopModel =
            (ShopModel) shopsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedShopModel != null) {
            
            mSelectedShop =
                mShopsDAOImpl.getShop(mSelectedShopModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(mSelectedShop.getAddress());
            dialog.setTitle("Изменение адреса");
            dialog.setHeaderText("Какой адрес магазина?");
            dialog.setContentText("Введите новый адрес: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                addressString -> {

                    boolean hasErrors = false;
                    
                        Pattern pattern = 
                            Pattern.compile(".{1,128}");
                        if (pattern.matcher(addressString).matches()) {

                                mSelectedShop.setAddress(addressString);
                                mShopsDAOImpl.updateShop(mSelectedShop);
                                //Notify self
                                updateShopsForPage();
                                WS1.salesControllerInstance.updateShops();
                                WS1.barrelsControllerInstance.updateShops();
                                WS1.addBarrelControllerInstance.updateShops();
                                //update visual filter's data
                                //WS1.shopsControllerInstance.updateFilterShops();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateShops();
                        } else {

                            hasErrors = true;
                        }
                    if (hasErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение адреса не выполнено");
                        alert.setContentText("Ошибка длины адреса: нужна строка от 1 до 128 символов");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни однин магазин");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showShopEditLegalNameDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedShopModel =
            (ShopModel) shopsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedShopModel != null) {
            
            mSelectedShop =
                mShopsDAOImpl.getShop(mSelectedShopModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(mSelectedShop.getLegalName());
            dialog.setTitle("Изменение имени ЧП");
            dialog.setHeaderText("Какое имя ЧП?");
            dialog.setContentText("Введите новое имя ЧП: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                legalNameString -> {

                    boolean hasErrors = false;
                    
                        Pattern pattern = 
                            Pattern.compile(".{1,128}");
                        if (pattern.matcher(legalNameString).matches()) {

                                mSelectedShop.setLegalName(legalNameString);
                                mShopsDAOImpl.updateShop(mSelectedShop);
                                //Notify self
                                updateShopsForPage();
                                WS1.salesControllerInstance.updateShops();
                                WS1.barrelsControllerInstance.updateShops();
                                WS1.addBarrelControllerInstance.updateShops();
                                //update visual filter's data
                                //WS1.shopsControllerInstance.updateFilterShops();
                                
                                mShopLegalNamesSet.clear();
                                mShops = WS1.barrelsControllerInstance.getShops();
                                for (Shop shop : mShops) {

                                    if (shop.getActive()) {

                                        mShopLegalNamesSet.add(shop.getLegalName());
                                    }
                                }
                                String shopLegalName = legalShopCTextField.getText();
                                legalShopCTextField.clear();
                                if (mShopLegalAutoCompletionBinding != null) {

                                    mShopLegalAutoCompletionBinding.dispose();
                                }
                                mShopLegalAutoCompletionBinding = TextFields.bindAutoCompletion(legalShopCTextField, mShopLegalNamesSet);
                                if (!shopLegalName.equals("")) {
                                    legalShopCTextField.setText(legalNameString);
                                }
                                                                
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateShops();
                        } else {

                            hasErrors = true;
                        }
                    if (hasErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение имени ЧП не выполнено");
                        alert.setContentText("Ошибка длины имени ЧП: нужна строка от 1 до 128 символов");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни однин магазин");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showShopEditPhoneDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedShopModel =
            (ShopModel) shopsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedShopModel != null) {
            
            mSelectedShop =
                mShopsDAOImpl.getShop(mSelectedShopModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(mSelectedShop.getPhone());
            dialog.setTitle("Изменение телефона");
            dialog.setHeaderText("Какой новый номер телефона?");
            dialog.setContentText("Введите новый номер телефона: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                phoneString -> {

                    boolean hasErrors = false;
                    
                        Pattern pattern = 
                            Pattern.compile(".{1,100}");
                        if (pattern.matcher(phoneString).matches()) {

                                mSelectedShop.setPhone(phoneString);
                                mShopsDAOImpl.updateShop(mSelectedShop);
                                //Notify self
                                updateShopsForPage();
                                WS1.salesControllerInstance.updateShops();
                                WS1.barrelsControllerInstance.updateShops();
                                WS1.addBarrelControllerInstance.updateShops();
                                //update visual filter's data
                                //WS1.shopsControllerInstance.updateFilterShops();
                                
                                mShopPhonesSet.clear();
                                mShops = WS1.barrelsControllerInstance.getShops();
                                for (Shop shop : mShops) {

                                    if (shop.getActive()) {

                                        mShopPhonesSet.add(shop.getPhone());
                                    }
                                }
                                String shopPhone = phoneShopCTextField.getText();
                                phoneShopCTextField.clear();
                                if (mShopPhoneAutoCompletionBinding != null) {

                                    mShopPhoneAutoCompletionBinding.dispose();
                                }
                                mShopPhoneAutoCompletionBinding = TextFields.bindAutoCompletion(phoneShopCTextField, mShopPhonesSet);
                                if (!shopPhone.equals("")) {
                                    phoneShopCTextField.setText(phoneString);
                                }
                                
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateShops();
                        } else {

                            hasErrors = true;
                        }
                    if (hasErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение телефона не выполнено");
                        alert.setContentText("Ошибка длины телефонного номера: нужна строка от 1 до 100 символов");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни однин магазин");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showShopEditBCDateDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedShopModel =
            (ShopModel) shopsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedShopModel != null) {
            
            mSelectedShop =
                mShopsDAOImpl.getShop(mSelectedShopModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(new SimpleDateFormat("dd/MM/yyyy").format(mSelectedShop.getBCDate()));
            dialog.setTitle("Изменение даты начала сотрудничества");
            dialog.setHeaderText("Какая дата начала нового сотрудничества?");
            dialog.setContentText("Измените дату начала сотрудничества (дд/мм/гггг): ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                bCDateString -> {

                    boolean hasErrors = false;
                    
                        Pattern pattern = 
                            Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d");
                        if (pattern.matcher(bCDateString).matches()) {

                            try {
                                
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                mSelectedShop.setBCDate(simpleDateFormat.parse(bCDateString));
                                mShopsDAOImpl.updateShop(mSelectedShop);
                                //Notify self
                                updateShopsForPage();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateShops();
                            } catch (ParseException ex) {
                                
                                hasErrors = true;
                            }
                        } else {

                            hasErrors = true;
                        }
                    if (hasErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение даты начала сотрудничества не выполнено");
                        alert.setContentText("Ошибка формата даты: нужна строка по шаблону дд/мм/гггг");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни однин магазин");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    @FXML
    private void showShopEditCTermsDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedShopModel =
            (ShopModel) shopsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedShopModel != null) {
            
            mSelectedShop =
                mShopsDAOImpl.getShop(mSelectedShopModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(mSelectedShop.getCTerms());
            dialog.setTitle("Изменение условий сотрудничества");
            dialog.setHeaderText("Какие новые условия сотрудничества?");
            dialog.setContentText("Измените условия сотрудничества: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                cTermsString -> {

                    boolean hasErrors = false;
                    
                        Pattern pattern = 
                            Pattern.compile(".{1,512}");
                        if (pattern.matcher(cTermsString).matches()) {

                                mSelectedShop.setCTerms(cTermsString);
                                mShopsDAOImpl.updateShop(mSelectedShop);
                                //Notify self
                                updateShopsForPage();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateShops();
                        } else {

                            hasErrors = true;
                        }
                    if (hasErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение условий сотрудничества не выполнено");
                        alert.setContentText("Ошибка длины строки: нужна строка от 1 до 512 символов");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни однин магазин");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    /*@FXML
    private void showShopEditLegalNameDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedShopModel =
            (ShopModel) shopsTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedShopModel != null) {
            
            mSelectedShop =
                mShopsDAOImpl.getShop(mSelectedShopModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(mSelectedShop.getLegalName());
            dialog.setTitle("Изменение юридического названия");
            dialog.setHeaderText("Какое юридическое название предприятия?");
            dialog.setContentText("Введите новое название ЧП: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                leganNameString -> {

                    boolean hasNumFormatErrors = false;
                    
                    try{
                    
                        Pattern pattern = 
                            Pattern.compile("[.]{1,128}");
                        if (pattern.matcher(leganNameString).matches()) {

                                mSelectedShop.setLegalName(leganNameString);
                                mShopsDAOImpl.updateShop(mSelectedShop);
                                //Notify self
                                updateShopsForPage();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateShops();
                        } else {

                            hasNumFormatErrors = true;
                        }
                    } catch (NumberFormatException ex){

                        hasNumFormatErrors = true;
                    }
                    if (hasNumFormatErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение названия ЧП не выполнено");
                        alert.setContentText("Ошибка длины названия ЧП: нужна строка от 1 до 128 символов");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни однин магазин");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }*/
    
    //действие "удаленный/нет" для магазина
    @FXML
    private void toggleFarShop(ActionEvent event){
        
        progressIndicator.visibleProperty().set(true);
       
        //
        mSelectedShopModel =
            (ShopModel) shopsTableView.getSelectionModel().getSelectedItem();
        if (mSelectedShopModel != null) {
        
            //
            mSelectedShop =
                    mShopsDAOImpl.getShop(mSelectedShopModel.getId());
            if (mSelectedShop.getFar()) {
                
                mSelectedShop.setFar(false);
            } else {
            
                mSelectedShop.setFar(true);
            }
            mShopsDAOImpl.updateShop(mSelectedShop);
            
            updateShopsForPage();
            WS1.salesControllerInstance.updateShops();
            WS1.barrelsControllerInstance.updateShops();
            WS1.addBarrelControllerInstance.updateShops();
            //notify add sales controller
            WS1.addSaleControllerInstance.updateShops();
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни один магазин");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    //действие удаления магазина
    @FXML
    private void deleteShop(ActionEvent event){
        
        progressIndicator.visibleProperty().set(true);
       
        //
        mSelectedShopModel =
            (ShopModel) shopsTableView.getSelectionModel().getSelectedItem();
        if (mSelectedShopModel != null) {
        
            //
            mSelectedShop =
                    mShopsDAOImpl.getShop(mSelectedShopModel.getId());
            
            mSelectedShop.setActive(false);
            mShopsDAOImpl.updateShop(mSelectedShop);
            
            //all active barrels
            List<Barrel> allBarrels =
                mBarrelsDAOImpl.getFilteredBarels(
                    -1
                    , null
                    , 1
                    , -1
                    , -1
                );
            
            for (Barrel barrel : allBarrels) {
                
                if (barrel.getShopId().intValue() == mSelectedShop.getId().intValue()) {
                    
                    barrel.setActive(false);
                    mBarrelsDAOImpl.updateBarrel(barrel);
                    WS1.barrelsControllerInstance.updateBarrelsForPage();
                    WS1.barrelsControllerInstance.updateShops();
                    WS1.addSaleControllerInstance.updateBarrels();
                }
            }
            updateShops();
            updateShopsForPage();
            //notify add sales controller
            WS1.addSaleControllerInstance.updateShops();
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни один магазин");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        progressIndicator.visibleProperty().set(false);
    }
    
    /*@FXML
    private void showDebtEditDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedShopModel =
            (ShopModel) shopsTableView.getSelectionModel().getSelectedItem();
        
        
        //
        if (mSelectedShopModel != null) {
            
            TextInputDialog dialog = new TextInputDialog("0");
            dialog.setTitle("Изменение долга");
            dialog.setHeaderText("Как изменился долг магазина " + mSelectedShopModel.getShopName() + " ?");
            dialog.setContentText("Для увеличения введите положительное число, для погашения - отрицательное: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                changeDebtString -> {

                    boolean hasNumFormatErrors = false;
                    
                    Pattern pattern = 
                        Pattern.compile("[-]{0,1}[0-9]{1,8}([.][0-9]{1,2}){0,1}");
                    if (pattern.matcher(changeDebtString).matches()) {

                        //
                        mSelectedShop =
                                mShopsDAOImpl.getShop(mSelectedShopModel.getId());
                        BigDecimal debt = mSelectedShop.getDebt();
                        try{
                            
                            BigDecimal changeDebt =
                                    BigDecimal.valueOf(Double.valueOf(changeDebtString));
                            debt = debt.add(changeDebt);
                            //Вносим изменение долга в БД для магазина
                            mSelectedShop.setDebt(debt);
                            mShopsDAOImpl.updateShop(mSelectedShop);
                            //Добавляем строку "изменение долга" в таблицу БД
                            DebtChange debtChange = new DebtChange();
                            debtChange.setShopId(mSelectedShop.getId());
                            //System.out.println(Date.from(Instant.now()));
                            debtChange.setDate(Date.from(Instant.now()));
                            debtChange.setValue(changeDebt);
                            mDebtChangesDAOImpl.createDebtChange(debtChange);
                            //Вызываем обновление данных в коллекции-источнике для
                            //таблицы магазинов (в текущем представлении)
                            WS1.shopsControllerInstance.updateShopsForPage();
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
                        alert.setContentText("Ошибка формата числа (нужно положительное или отрицательное значение, максимум 99999999.99)");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни один магазин");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        
        progressIndicator.visibleProperty().set(false);
    }*/
    
    //Действие изменения суммы изменения долга, произошедшего при доставке
    //(не отражается на других данных системы)
    @FXML
    private void showFreeChangeDebtDialog(){
        
        progressIndicator.visibleProperty().set(true);
    
        //
        mSelectedShopModel =
            (ShopModel) shopsTableView.getSelectionModel().getSelectedItem();
        
        if (mSelectedShopModel != null) {
            
            Shop selectedShop =
                mShopsDAOImpl.getShop(mSelectedShopModel.getId());
        
            TextInputDialog dialog =
                new TextInputDialog(String.valueOf(selectedShop.getDebt()));
            dialog.setTitle("Изменение долга");
            dialog.setHeaderText("Сумма долга магазина "
                    + mSelectedShopModel.getShopName());
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
                            selectedShop.setDebt(
                                BigDecimal.valueOf(
                                    Double.parseDouble(changeDebtString.replaceAll(",", "."))
                                )
                            );
                            //
                            mShopsDAOImpl.updateShop(selectedShop);
                            //
                            updateShopsForPage();
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
            warningAlert.setHeaderText("Не выбран ни однин магазин");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
        
        progressIndicator.visibleProperty().set(false);
    }
    
    private void fillShopsObservableList(List<Shop> _shopsList){
        
        for (Shop shop : _shopsList) {
                        
            mShopsObservableList.add(new ShopModel(
                    shop.getId(),
                    shop.getName(),
                    shop.getAddress(),
                    shop.getPhone(),
                    new SimpleDateFormat("yyyy.MM.dd").format(shop.getBCDate()),
                    shop.getCTerms(),
                    shop.getLegalName(),
                    shop.getFar(),
                    shop.getDebt().doubleValue()
                )
            );
        }        
    }
    
    private Node createPage(int _pageIndex/*, int _rowsPerPage*/) {
        
        progressIndicator.visibleProperty().set(true);
        
        mCurrentPageIdx = _pageIndex;
        int fromIndex = mCurrentPageIdx * mRowsPerPage;        
        mShopsForPage = mShopsFacade.getShopsForPage(mRowsPerPage, fromIndex);
        mShopsObservableList.clear();
        fillShopsObservableList(mShopsForPage);
        shopsTableView.setItems(mShopsObservableList);
        
        AnchorPane.setTopAnchor(shopsTableView, 0.0);
        AnchorPane.setRightAnchor(shopsTableView, 0.0);
        AnchorPane.setBottomAnchor(shopsTableView, 0.0);
        AnchorPane.setLeftAnchor(shopsTableView, 0.0);

        progressIndicator.visibleProperty().set(false);
        
        return new AnchorPane(shopsTableView);
    }
    
    public void updateShopsForPage(){
        
        int shopsAllCount = mShopsDAOImpl.getShopsCount();
        mShopsAllCount = shopsAllCount;
        //shopsPagination.get
        //setNumCellFactory();
        shopsPagination.setPageCount(
                shopsAllCount > 0
                ? (shopsAllCount + mRowsPerPage - 1) / mRowsPerPage
                : 1
        );
                
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        mShopsForPage = mShopsFacade.getShopsForPage(mRowsPerPage, fromIndex);
        mShopsObservableList.clear();
        fillShopsObservableList(mShopsForPage);
    }
    
    //Приведение формы filter в исходное состояние
    private void resetFilterForm(){
        
        shopCTextField.clear();
        mShopNamesSet.clear();
        mShopLegalNamesSet.clear();
        mShopPhonesSet.clear();
        
        for (Shop shop : mShops) {
            
            if (shop.getActive()) {
                
                mShopNamesSet.add(shop.getName());
                mShopLegalNamesSet.add(shop.getLegalName());
                mShopPhonesSet.add(shop.getPhone());
            }
        }
        //отписываемся от предыдущего набора автодополнения
        if (mShopAutoCompletionBinding != null) {
            
            mShopAutoCompletionBinding.dispose();
        }
        //подписываемся на новый набор автодополнения
        mShopAutoCompletionBinding = TextFields.bindAutoCompletion(shopCTextField, mShopNamesSet);
        
        if (mShopLegalAutoCompletionBinding != null) {
            
            mShopLegalAutoCompletionBinding.dispose();
        }
        mShopLegalAutoCompletionBinding = TextFields.bindAutoCompletion(legalShopCTextField, mShopLegalNamesSet);
        
        if (mShopPhoneAutoCompletionBinding != null) {
            
            mShopPhoneAutoCompletionBinding.dispose();
        }
        mShopPhoneAutoCompletionBinding = TextFields.bindAutoCompletion(phoneShopCTextField, mShopPhonesSet);
    }
    
    public void updateFilterShops(){
    
        mShops = WS1.barrelsControllerInstance.getShops();
        resetFilterForm();
    }
    
    private void setNumCellFactory(){

        numTableColumn.setCellFactory(column ->{
            
            return new TableCell(){
                @Override
                public void updateItem(Object item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setGraphic(null);
                    //System.out.println("mShopsAllCount " + mShopsAllCount);
                    //System.out.println("getIndex() " + getIndex());
                    //System.out.println("mCurrentPageIdx " + mCurrentPageIdx);
                    //System.out.println("mRowsPerPage " + mRowsPerPage);
                    setText(empty ? null :
                        (mShopsAllCount - getIndex())
                        - ((mCurrentPageIdx) * mRowsPerPage)
                        + ""
                    );
                }
            };
        });
    }
    
    public void updateShops(){
        
        mShops = mShopsDAOImpl.getAllShops();
        resetFilterForm();
    }
    
    public void hideEditShopButtons(){
    
        shopEditNameButton.setVisible(false);
        shopEditNameButton.setDisable(true);
        
        shopAddressEditNameButton.setVisible(false);
        shopAddressEditNameButton.setDisable(true);
        
        shopEditLegalNameButton.setVisible(false);
        shopEditLegalNameButton.setDisable(true);
        
        shopEditPhoneButton.setVisible(false);
        shopEditPhoneButton.setDisable(true);
        
        shopEditBCDateButton.setVisible(false);
        shopEditBCDateButton.setDisable(true);
        
        shopEditCTermsButton.setVisible(false);
        shopEditCTermsButton.setDisable(true);
        
        shopEditFarButton.setVisible(false);
        shopEditFarButton.setDisable(true);
        
        freeChangeDebtButton.setVisible(false);
        freeChangeDebtButton.setDisable(true);
    }
}
