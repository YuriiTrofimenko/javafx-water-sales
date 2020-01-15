/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.viewcontroller;

import org.tyaa.ws.screensframework.ControlledScreen;
import org.tyaa.ws.screensframework.ScreensController;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.tyaa.ws.WS1;
import org.tyaa.ws.dao.DriversFacade;
import org.tyaa.ws.dao.impl.DriversDAOImpl;
import org.tyaa.ws.entity.Driver;
import org.tyaa.ws.viewmodel.DriverModel;
import org.tyaa.ws.viewmodel.ShopModel;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class DriversController implements Initializable, ControlledScreen {

    @FXML
    private TableView driversTableView;
    
    @FXML
    private TableColumn driverIdTableColumn;
    @FXML
    private TableColumn nameTableColumn;
    @FXML
    private TableColumn phoneTableColumn;
    
    @FXML
    private Pagination driversPagination;
    
    ScreensController myController;
    
    private ObservableList<DriverModel> mDriversObservableList;
    
    private List<Driver> mDriversForPage;
    
    private DriversDAOImpl mDriversDAOImpl;
    
    private DriversFacade mDriversFacade;
    
    private DriverModel mSelectedDriverModel;
    private Driver mSelectedDriver;
    
    private int mCurrentPageIdx;
    private final int mRowsPerPage = 25;
//    ValidationSupport validationSupport;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        WS1.driversControllerInstance = this;
        
        mDriversObservableList = FXCollections.observableArrayList();
        
        mDriversDAOImpl = new DriversDAOImpl();
        
        mDriversFacade = new DriversFacade();
        
        //driversPagination.setPageCount((mDriversDAOImpl.getDriversCount() / 2 + 1));
        //driversPagination.setPageCount(1);
        
        int driversAllCount = mDriversDAOImpl.getDriversCount();
        driversPagination.setPageCount(
                driversAllCount > 0
                ? (driversAllCount + mRowsPerPage - 1) / mRowsPerPage
                : 1
        );
        
        driversPagination.setPageFactory(
            new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {          
                    return createPage(pageIndex);               
                }
            }
        );
        
        driverIdTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("id")
        );
        nameTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("name")
        );
        phoneTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("phone")
        );
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
    private void goToDebtsScreen(ActionEvent event){
       myController.setScreen(WS1.debtsID);
    }
    
    @FXML
    private void goToAddDriverScreen(ActionEvent event){
       myController.setScreen(WS1.addDriverID);
       WS1.primaryStage.setMaximized(false);
       WS1.primaryStage.setWidth(600);
       WS1.primaryStage.setHeight(350);
       WS1.primaryStage.setY(30);
    }
    
    @FXML
    private void showDriverEditPhoneDialog(){
    
        //
        mSelectedDriverModel =
            (DriverModel) driversTableView.getSelectionModel().getSelectedItem();
        
        //
        if (mSelectedDriverModel != null) {
            
            mSelectedDriver =
                mDriversDAOImpl.getDriver(mSelectedDriverModel.getId());
            
            TextInputDialog dialog =
                new TextInputDialog(mSelectedDriver.getPhone());
            dialog.setTitle("Изменение телефона");
            dialog.setHeaderText("Какой новый номер телефона?");
            dialog.setContentText("Введите новый номер телефона: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(
                phoneString -> {

                    boolean hasErrors = false;
                    
                        Pattern pattern = 
                            Pattern.compile(".{1,25}");
                        if (pattern.matcher(phoneString).matches()) {

                                mSelectedDriver.setPhone(phoneString);
                                mDriversDAOImpl.updateDriver(mSelectedDriver);
                                //Notify self
                                updateDriversForPage();
                                //нотифицировать контроллер добавления доставок
                                WS1.addSaleControllerInstance.updateDrivers();
                        } else {

                            hasErrors = true;
                        }
                    if (hasErrors) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Изменение телефона не выполнено");
                        alert.setContentText("Ошибка длины телефонного номера: нужна строка от 1 до 25 символов");
                        alert.showAndWait();
                    }
                }
            );
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни однин водитель");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
    }
    
    @FXML
    private void deleteDriver(ActionEvent event){
       
        mSelectedDriverModel =
                (DriverModel) driversTableView.getSelectionModel().getSelectedItem();
        
        if (mSelectedDriverModel != null) {
            
            mSelectedDriver =
                mDriversDAOImpl.getDriver(mSelectedDriverModel.getId());
            
            mSelectedDriver.setActive(false);
            
            mDriversDAOImpl.updateDriver(mSelectedDriver);
            
            //Notify self
            updateDriversForPage();
            //нотифицировать контроллер добавления доставок
            WS1.addSaleControllerInstance.updateDrivers();
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни один водитель");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
    }
    
    private void fillDriversObservableList(List<Driver> _driversList){
        
        for (Driver driver : _driversList) {
                        
            mDriversObservableList.add(new DriverModel(
                    driver.getId(),
                    driver.getName(),
                    driver.getPhone()
                )
            );
        }        
    }
    
    private Node createPage(int _pageIndex/*, int _rowsPerPage*/) {
        
        mCurrentPageIdx = _pageIndex;
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        
        /*List<Driver> driversForPageTmp =
                mDriversFacade.getDriversForPage(mRowsPerPage, fromIndex);
        if (driversForPageTmp == null) {
            
            driversForPageTmp = new ArrayList<>();
        }
        mDriversForPage =
            driversForPageTmp.stream()
                .filter(driver -> ((Driver)driver).getActive())
                .collect(Collectors.toList());
        if (mDriversForPage == null) {
            
            mDriversForPage = new ArrayList<>();
        }*/
        
        //mDriversForPage = mDriversFacade.getDriversForPage(mRowsPerPage, fromIndex);
        mDriversForPage = mDriversDAOImpl.getFilteredDrivers(1, mRowsPerPage, fromIndex);
        
        mDriversObservableList.clear();
        fillDriversObservableList(mDriversForPage);
        driversTableView.setItems(mDriversObservableList);
        AnchorPane.setTopAnchor(driversTableView, 0.0);
        AnchorPane.setRightAnchor(driversTableView, 0.0);
        AnchorPane.setBottomAnchor(driversTableView, 0.0);
        AnchorPane.setLeftAnchor(driversTableView, 0.0);

        return new AnchorPane(driversTableView);
    }
    
    public void updateDriversForPage(){
        
        int driversAllCount = mDriversDAOImpl.getDriversCount();
        driversPagination.setPageCount(
                driversAllCount > 0
                ? (driversAllCount + mRowsPerPage - 1) / mRowsPerPage
                : 1
        );
        
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        
        /*List<Driver> driversForPageTmp =
                mDriversFacade.getDriversForPage(mRowsPerPage, fromIndex);
        if (driversForPageTmp == null) {
            
            driversForPageTmp = new ArrayList<>();
        }
        mDriversForPage =
            driversForPageTmp.stream()
                .filter(driver -> ((Driver)driver).getActive())
                .collect(Collectors.toList());
        if (mDriversForPage == null) {
            
            mDriversForPage = new ArrayList<>();
        }*/
        
        //mDriversForPage = mDriversFacade.getDriversForPage(mRowsPerPage, fromIndex);
        mDriversForPage = mDriversDAOImpl.getFilteredDrivers(1, mRowsPerPage, fromIndex);
        
        mDriversObservableList.clear();
        fillDriversObservableList(mDriversForPage);
    }
}
