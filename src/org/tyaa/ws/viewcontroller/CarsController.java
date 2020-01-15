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
import java.util.ResourceBundle;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.tyaa.ws.WS1;
import org.tyaa.ws.dao.CarsFacade;
import org.tyaa.ws.dao.impl.CarsDAOImpl;
import org.tyaa.ws.entity.Car;
import org.tyaa.ws.viewmodel.CarModel;
import org.tyaa.ws.viewmodel.ShopModel;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class CarsController implements Initializable, ControlledScreen {

    @FXML
    private TableView carsTableView;
    
    @FXML
    private TableColumn carIdTableColumn;
    @FXML
    private TableColumn numberTableColumn;
    @FXML
    private TableColumn tonnageTableColumn;
    @FXML
    private TableColumn govNumTableColumn;
    
    @FXML
    private Pagination carsPagination;
    
    ScreensController myController;
    
    private ObservableList<CarModel> mCarsObservableList;
    
    private List<Car> mCarsForPage;
    
    private CarsDAOImpl mCarsDAOImpl;
    
    private CarsFacade mCarsFacade;
    
    private CarModel mSelectedCarModel;
    private Car mSelectedCar;
    
    private int mCurrentPageIdx;
    private final int mRowsPerPage = 25;
//    ValidationSupport validationSupport;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        WS1.carsControllerInstance = this;
        
        mCarsObservableList = FXCollections.observableArrayList();
        
        mCarsDAOImpl = new CarsDAOImpl();
        
        mCarsFacade = new CarsFacade();
        
        //carsPagination.setPageCount((mCarsDAOImpl.getCarsCount() / 2 + 1));
        //carsPagination.setPageCount(1);
        
        int carsAllCount = mCarsDAOImpl.getCarsCount();
        carsPagination.setPageCount(
                carsAllCount > 0
                ? (carsAllCount + mRowsPerPage - 1) / mRowsPerPage
                : 1
        );
        
        carsPagination.setPageFactory(
            new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {          
                    return createPage(pageIndex);               
                }
            }
        );
        
        carIdTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("id")
        );
        numberTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("number")
        );
        tonnageTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("tonnage")
        );
        govNumTableColumn.setCellValueFactory(
                new PropertyValueFactory<ShopModel, String>("govNum")
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
    private void goToDriversScreen(ActionEvent event){
       myController.setScreen(WS1.driversID);
    }
    
    @FXML
    private void goToDebtsScreen(ActionEvent event){
       myController.setScreen(WS1.debtsID);
    }
    
    @FXML
    private void goToAddCarScreen(ActionEvent event){
       myController.setScreen(WS1.addCarID);
       WS1.primaryStage.setMaximized(false);
       WS1.primaryStage.setWidth(600);
       WS1.primaryStage.setHeight(360);
       WS1.primaryStage.setY(30);
    }
    
    @FXML
    private void deleteCar(ActionEvent event){
       
        //
        mSelectedCarModel =
                (CarModel) carsTableView.getSelectionModel().getSelectedItem();
        
        if (mSelectedCarModel != null) {
            
            mSelectedCar =
                mCarsDAOImpl.getCar(mSelectedCarModel.getId());
            
            mSelectedCar.setActive(false);
            
            mCarsDAOImpl.updateCar(mSelectedCar);
            
            //Notify self
            updateCarsForPage();
            //нотифицировать контроллер добавления доставок
            WS1.addSaleControllerInstance.updateCars();
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбран ни один автомобиль");
            warningAlert.setContentText("Выделите одну строку в таблице");
            warningAlert.showAndWait();
        }
    }
    
    private void fillCarsObservableList(List<Car> _carsList){
        
        for (Car car : _carsList) {
                        
            mCarsObservableList.add(new CarModel(
                    car.getId(),
                    car.getNumber(),
                    car.getTonnage(),
                    car.getGovNum()
                )
            );
        }        
    }
    
    private Node createPage(int _pageIndex/*, int _rowsPerPage*/) {
        
        mCurrentPageIdx = _pageIndex;
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        
        /*List<Car> carsForPageTmp =
                mCarsFacade.getCarsForPage(mRowsPerPage, fromIndex);
        if (carsForPageTmp == null) {
            
            carsForPageTmp = new ArrayList<>();
        }
        mCarsForPage =
            carsForPageTmp.stream()
                .filter(car -> ((Car)car).getActive())
                .collect(Collectors.toList());
        if (mCarsForPage == null) {
            
            mCarsForPage = new ArrayList<>();
        }*/
        //mCarsForPage = mCarsFacade.getCarsForPage(mRowsPerPage, fromIndex);
        mCarsForPage = mCarsDAOImpl.getFilteredCars(1, mRowsPerPage, fromIndex);
        
        mCarsObservableList.clear();
        fillCarsObservableList(mCarsForPage);
        carsTableView.setItems(mCarsObservableList);
        AnchorPane.setTopAnchor(carsTableView, 0.0);
        AnchorPane.setRightAnchor(carsTableView, 0.0);
        AnchorPane.setBottomAnchor(carsTableView, 0.0);
        AnchorPane.setLeftAnchor(carsTableView, 0.0);

        return new AnchorPane(carsTableView);
    }
    
    public void updateCarsForPage(){
        
        int carsAllCount = mCarsDAOImpl.getCarsCount();
        carsPagination.setPageCount(
                carsAllCount > 0
                ? (carsAllCount + mRowsPerPage - 1) / mRowsPerPage
                : 1
        );
        
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        
        //mCarsForPage = mCarsFacade.getCarsForPage(mRowsPerPage, fromIndex);
        
        mCarsForPage = mCarsDAOImpl.getFilteredCars(1, mRowsPerPage, fromIndex);
        
        /*List<Car> carsForPageTmp =
                mCarsFacade.getCarsForPage(mRowsPerPage, fromIndex);
        if (carsForPageTmp == null) {
            
            carsForPageTmp = new ArrayList<>();
        }
        mCarsForPage =
            carsForPageTmp.stream()
                .filter(car -> ((Car)car).getActive())
                .collect(Collectors.toList());
        if (mCarsForPage == null) {
            
            mCarsForPage = new ArrayList<>();
        }*/
        
        mCarsObservableList.clear();
        fillCarsObservableList(mCarsForPage);
    }
}
