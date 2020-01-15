/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.viewcontroller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.tyaa.ws.screensframework.ControlledScreen;
import org.tyaa.ws.screensframework.ScreensController;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import org.tyaa.ws.WS1;
import org.tyaa.ws.dao.ReportsFacade;
import org.tyaa.ws.dao.impl.CarsDAOImpl;
import org.tyaa.ws.dao.impl.DayReportItemsDAOImpl;
import org.tyaa.ws.dao.impl.DebtsDAOImpl;
import org.tyaa.ws.dao.impl.DriversDAOImpl;
import org.tyaa.ws.dao.impl.SalesDAOImpl;
import org.tyaa.ws.dao.util.DateUtil;
import org.tyaa.ws.entity.DayReportItem;
import org.tyaa.ws.entity.DebtChange;
import org.tyaa.ws.entity.Sale;
import org.tyaa.ws.viewmodel.DayReportItemModel;
import org.tyaa.ws.viewmodel.DayReportTotalModel;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class DayReportsController implements Initializable, ControlledScreen {

    /*Внедрение контролов для таблицы строк отчета*/
    
    @FXML
    private TableView dRepItemsTableView;
    
    @FXML
    private TableColumn dRepItemIdTableColumn;
    @FXML
    private TableColumn carTableColumn;
    @FXML
    private TableColumn driverTableColumn;
    @FXML
    private TableColumn toPayTableColumn;
    @FXML
    private TableColumn profitTableColumn;
    @FXML
    //private TableColumn profitFarTableColumn;
    private TableColumn toPayFarTableColumn;
    @FXML
    private TableColumn volumeTableColumn;
    @FXML
    private TableColumn volumeFarTableColumn;
    //@FXML
    //private TableColumn farTableColumn;
    @FXML
    private TableColumn debtAmortTableColumn;
    @FXML
    private TableColumn cleanCountTableColumn;
    @FXML
    private TableColumn replaceCountTableColumn;
    @FXML
    private TableColumn debtTableColumn;
    @FXML
    private TableColumn kMTableColumn;
    //Это НЕ топливо, а км / т воды!!!
    @FXML
    private TableColumn fuelTableColumn;
    /*@FXML
    private TableColumn fuelCostTableColumn;
    @FXML
    private TableColumn advertisingCostTableColumn;
    @FXML
    private TableColumn otherCostTableColumn;*/
    @FXML
    private TableColumn noticeTableColumn;
    
    @FXML
    DatePicker datePicker;
    @FXML
    DatePicker datePicker2;
    @FXML
    CheckBox intervalCheckBox;
    
    @FXML
    private Pagination dRepItemsPagination;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    /*Внедрение контролов для строки всего дневного отчета*/
    
    @FXML
    private TableView dRepTotalTableView;
    
    @FXML
    private TableColumn dRepIdTableColumn;
    @FXML
    private TableColumn dRepToPayTableColumn;
    @FXML
    private TableColumn dRepProfitNameTableColumn;
    @FXML
    private TableColumn dRepProfitFarNameTableColumn;
    @FXML
    private TableColumn dRepVolumeTableColumn;
    @FXML
    private TableColumn dRepVolumeFarTableColumn;
    @FXML
    private TableColumn dRepAvPriceTableColumn;
    @FXML
    private TableColumn dRepAvPriceNoFarTableColumn;
    /*@FXML
    private TableColumn dRepFarTableColumn;*/
    @FXML
    private TableColumn dRepDebtAmortTableColumn;
    @FXML
    private TableColumn dRepNoSalesDebtAmortTableColumn;
    @FXML
    private TableColumn dRepCleanCountTableColumn;
    @FXML
    private TableColumn dRepReplaceCountTableColumn;
    @FXML
    private TableColumn dRepKmTableColumn;
    //Это НЕ топливо, а км / т воды!!!
    @FXML
    private TableColumn dRepFuelTableColumn;
    //@FXML
    //private TableColumn dRepFuelCostTableColumn;
    //@FXML
    //private TableColumn dRepAdvertisingCostTableColumn;
    //@FXML
    //private TableColumn dRepOtherCostTableColumn;
    @FXML
    private TableColumn dRepDebtTableColumn;
    @FXML
    private TableColumn dRepTotalDebtAmortTableColumn;
    @FXML
    private TableColumn dRepTotalProfitTableColumn;
    //@FXML
    //private TableColumn dRepTotalCostTableColumn;
    @FXML
    private TableColumn dRepTotalBalanceTableColumn;
    
    
    ScreensController myController;
    
    private ObservableList<DayReportItemModel> mDayReportItemsObservableList;
    private ObservableList<DayReportTotalModel> mDayReportTotalObservableList;
    
    private List<DayReportItem> mDayReportItemsList;
    
    //private ShopsDAOImpl mShopsDAOImpl;
    
    private DriversDAOImpl mDriversDAOImpl;
    private CarsDAOImpl mCarsDAOImpl;
    private SalesDAOImpl mSalesDAOImpl;
    private DebtsDAOImpl mDebtsDAOImpl;
    
    private DayReportItemsDAOImpl mDayReportItemsDAOImpl;
    
    private ReportsFacade mReportsFacade;
    
    //DayReportItem, выбранный при помощи выделения строки в таблице
    private DayReportItemModel mSelectedDayReportItemModel;
    private DayReportItem mSelectedDayReportItem;
    
    //Номер текущей страницы таблицы магазинов
    private int mCurrentPageIdx;
    //Число строк на одной странице таблицы магазинов
    private final int mRowsPerPage = 25;
//    ValidationSupport validationSupport;
//    RemoteServiceRemote remoteServiceRemote;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
        WS1.dayReportsControllerInstance = this;
        
        progressIndicator.visibleProperty().set(false);
        
        mDayReportItemsObservableList = FXCollections.observableArrayList();
        mDayReportTotalObservableList = FXCollections.observableArrayList();
        
        dRepTotalTableView.setItems(mDayReportTotalObservableList);
        
        //mShopsDAOImpl = new ShopsDAOImpl();
        mSalesDAOImpl = new SalesDAOImpl();
        mDriversDAOImpl = new DriversDAOImpl();
        mCarsDAOImpl = new CarsDAOImpl();
        mDebtsDAOImpl = new DebtsDAOImpl();
        
        mDayReportItemsDAOImpl = new DayReportItemsDAOImpl();
        
        mReportsFacade = new ReportsFacade();
        
        mDayReportItemsList = new ArrayList<>();
                
        datePicker.setValue(LocalDate.now());
        datePicker2.setValue(LocalDate.now());
        
        datePicker2.setDisable(true);
                
        //to copy
        mDayReportItemsList = mReportsFacade.getDayReport(
                Date.from((datePicker
                .getValue()
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())).toInstant())
        );
                
        //dRepItemsPagination.setVisible(false);
        
        //dRepItemsPagination.setPageCount(1);
        
        if (mDayReportItemsList == null) {
            
            mDayReportItemsList = new ArrayList<>();
        }
        
        dRepItemsPagination.setPageCount(
                !mDayReportItemsList.isEmpty()
                ? (mDayReportItemsList.size() + mRowsPerPage - 1) / mRowsPerPage
                : 1
        );
                
        dRepItemsPagination.setPageFactory(
            new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {          
                    return createPage(pageIndex);               
                }
            }
        );
        
        /**/
                
        dRepItemIdTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("id")
        );
        carTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("car")
        );
        driverTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("driver")
        );
        toPayTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("toPay")
        );
        profitTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("profit")
        );
        toPayFarTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("toPayFar")
        );
        volumeTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("volume")
        );
        volumeFarTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("volumeFar")
        );
        /*farTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("farCount")
        );*/
        debtAmortTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("debtAmort")
        );
        cleanCountTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("cleanCount")
        );
        replaceCountTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("replaceCount")
        );
        debtTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("debt")
        );
        kMTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("kM")
        );
        fuelTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("fuel")
        );
        /*fuelCostTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("fuelCost")
        );
        advertisingCostTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("advertisingCost")
        );
        otherCostTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("otherCost")
        );*/
        noticeTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportItemModel, String>("notice")
        );
        
        //Когда выбирается дата, обновляем список строк отчета
        datePicker.setOnAction((event) -> {
            
            if (intervalCheckBox.isSelected()) {

                computeIntervalReport();
            } else {

                updateDayReportItemsForPage();
            }
        });
        
        //Когда выбирается дата 2 - 
        datePicker2.setOnAction((event) -> {
            
            if (intervalCheckBox.isSelected()) {
                
                computeIntervalReport();
            }
        });
        
        //
        intervalCheckBox.setOnAction((event) -> {
            
                if (intervalCheckBox.isSelected()) {
                
                    datePicker2.setDisable(false);
                    computeIntervalReport();
                } else {
                
                    datePicker2.setDisable(true);
                    updateDayReportItemsForPage();
                }
                
        });
        
        /*Привязка колонок для таблицы строк отчета*/
        
        dRepIdTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("id")
        );
        dRepToPayTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("toPay")
        );
        dRepProfitNameTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("profit")
        );
        dRepProfitFarNameTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("profitFar")
        );
        dRepVolumeTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("volume")
        );
        dRepVolumeFarTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("volumeFar")
        );
        dRepAvPriceTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportTotalModel, String>("avPrice")
        );
        dRepAvPriceNoFarTableColumn.setCellValueFactory(
                new PropertyValueFactory<DayReportTotalModel, String>("avPriceNoFar")
        );
        /*dRepFarTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("farCount")
        );*/
        dRepDebtAmortTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("debtAmort")
        );
        dRepNoSalesDebtAmortTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("nonSaleDebtAmort")
        );
        dRepCleanCountTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("cleanCount")
        );
        dRepReplaceCountTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("replaceCount")
        );
        dRepKmTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("kM")
        );
        dRepFuelTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("fuel")
        );
        /*dRepFuelCostTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("fuelCost")
        );
        dRepAdvertisingCostTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("advertisingCost")
        );
        dRepOtherCostTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("otherCost")
        );*/
        dRepDebtTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("debt")
        );
        dRepTotalDebtAmortTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("totalDebtAmort")
        );
        dRepTotalProfitTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("totalProfit")
        );
        /*dRepTotalCostTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("totalCost")
        );*/
        dRepTotalBalanceTableColumn.setCellValueFactory(
            new PropertyValueFactory<DayReportTotalModel, String>("totalBalance")
        );
        
        updateDayReportItemsForPage();
        
        
       /*multyline for total table columns*/ 
        makeHeaderWrappable(dRepToPayTableColumn);
        makeHeaderWrappable(dRepProfitNameTableColumn);
        makeHeaderWrappable(dRepVolumeFarTableColumn);
        
        makeHeaderWrappable(dRepAvPriceNoFarTableColumn);
        makeHeaderWrappable(dRepProfitFarNameTableColumn);
        
        makeHeaderWrappable(dRepDebtAmortTableColumn);
        
        makeHeaderWrappable(dRepNoSalesDebtAmortTableColumn);
        //
        //makeHeaderWrappable(dRepTotalDebtAmortTableColumn);
        //makeHeaderWrappable(dRepFuelTableColumn);
        //makeHeaderWrappable(dRepFuelCostTableColumn);
        
        //makeHeaderWrappable(dRepAdvertisingCostTableColumn);
        
        //makeHeaderWrappable(dRepOtherCostTableColumn);
        //makeHeaderWrappable(dRepDebtTableColumn);
        //makeHeaderWrappable(dRepTotalDebtTableColumn);
        //makeHeaderWrappable(dRepTotalProfitTableColumn);
        
        //makeHeaderWrappable(dRepTotalCostTableColumn);
        
        //makeHeaderWrappable(dRepTotalBalanceTableColumn);
        
        /*multyline for cars table columns*/
        
        makeHeaderWrappable(toPayTableColumn);
        makeHeaderWrappable(profitTableColumn);
        makeHeaderWrappable(volumeFarTableColumn);
        makeHeaderWrappable(toPayFarTableColumn);
        //makeHeaderWrappable(replaceCountTableColumn);
        
        /*Настройка контрастного фона отдельных колонок таблиц для лучшей читаемости*/
        //1 - колонки таблицы по автомобилям
        
        carTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, String>(){
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item);
                    }
                }
            };
        });
        
        driverTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, String>(){
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item);
                    }
                }
            };
        });
        
        volumeTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Integer>(){
                @Override
                protected void updateItem(Integer item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        volumeFarTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Integer>(){
                @Override
                protected void updateItem(Integer item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        toPayFarTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Double>(){
                @Override
                protected void updateItem(Double item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        cleanCountTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Integer>(){
                @Override
                protected void updateItem(Integer item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        replaceCountTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Integer>(){
                @Override
                protected void updateItem(Integer item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        //2 - колонки общей таблицы
        dRepVolumeTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Integer>(){
                @Override
                protected void updateItem(Integer item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        dRepToPayTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Double>(){
                @Override
                protected void updateItem(Double item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        dRepProfitNameTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Double>(){
                @Override
                protected void updateItem(Double item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        dRepDebtTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Double>(){
                @Override
                protected void updateItem(Double item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        dRepDebtAmortTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Double>(){
                @Override
                protected void updateItem(Double item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        dRepNoSalesDebtAmortTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Double>(){
                @Override
                protected void updateItem(Double item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        dRepAvPriceTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Double>(){
                @Override
                protected void updateItem(Double item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
        });
        
        dRepAvPriceNoFarTableColumn.setCellFactory(column ->{
            
            return new TableCell<DayReportItemModel, Double>(){
                @Override
                protected void updateItem(Double item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setStyle("-fx-background-color:lightblue");
                        setTextFill(Color.BLACK);
                        setText(item.toString());
                    }
                }
            };
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
    
    //Обработчик кнопки "переход на экран добавления строки в дневной отчет"
    @FXML
    private void goToAddDayReportScreen(ActionEvent event){
        
        progressIndicator.visibleProperty().set(true);
       
       //Пытаемся получить коллекцию доставок за указанный день
        List<Sale> daySales =
            mSalesDAOImpl.getFilteredSales(
                -1
                , -1
                , -1
                , Date.from((datePicker
                    .getValue()
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())).toInstant()
                )
                , Date.from((datePicker
                    .getValue()
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())).toInstant()
                )
                , -1
                , -1
                , -1
            );
        //
        if (daySales != null && daySales.size() > 0) {
            WS1.addDayReportItemControllerInstance.setCurrentDayReportDate(
                Date.from((datePicker
                .getValue()
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())).toInstant())
            );
            WS1.addDayReportItemControllerInstance.setDaySales(daySales);
            WS1.addDayReportItemControllerInstance.resetForm();
            myController.setScreen(WS1.addDayReportID);
            WS1.primaryStage.setMaximized(false);
            WS1.primaryStage.setWidth(600);
            WS1.primaryStage.setHeight(640);
            WS1.primaryStage.setY(30);
        } else {
            //System.out.println("goToAddDayReportScreen4");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Невозможно создать отчет за день");
            alert.setContentText("За этот день нет ни одной не обработанной доставки");
            alert.showAndWait();
        }
        
        progressIndicator.visibleProperty().set(false);
    }
    
    //Обработчик кнопки "переход на экран добавления строки в дневной отчет"
    @FXML
    private void showDelDayReportDialog(ActionEvent event){
        
        progressIndicator.visibleProperty().set(true);
        
        //
        mSelectedDayReportItemModel =
            (DayReportItemModel) dRepItemsTableView
                    .getSelectionModel()
                    .getSelectedItem();
        
        //
        if (mSelectedDayReportItemModel != null) {
        
            int delResult = 0;
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Удаление строки");
            alert.setHeaderText("Требуется подтверждение");
            alert.setContentText("Вы действительно хотите удалить выбранную строку дневного отчета?");

            Optional<ButtonType> result = alert.showAndWait();

            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {

                delResult =
                    mDayReportItemsDAOImpl.deleteReportItem(
                        mSelectedDayReportItemModel.getId()
                    );
                
                if (delResult == 0) {
                    
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText("Выбранная строка дневного отчета успешно удалена");
                    //Показать сообщение и ждать клика по кнопке Ok
                    alert.showAndWait();
                } else {
                
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Сбой в базе данных. Выбранная строка не найдена и не удалена.");
                    alert.showAndWait();
                }
                updateDayReportItemsForPage();
            }
            
        } else {
        
            Alert warningAlert =
                new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Предупреждение");
            warningAlert.setHeaderText("Не выбрана ни одна строка дневного отчета");
            warningAlert.setContentText("Выделите одну строку в таблице дневных отчетов");
            warningAlert.showAndWait();
        }
        
        //***
        
        
       
       /*//Пытаемся получить коллекцию доставок за указанный день
        List<Sale> daySales =
            mSalesDAOImpl.getFilteredSales(
                -1
                , -1
                , -1
                , Date.from((datePicker
                    .getValue()
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())).toInstant()
                )
                , Date.from((datePicker
                    .getValue()
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())).toInstant()
                )
                , -1
                , -1
                , -1
            );
        //
        if (daySales != null && daySales.size() > 0) {
            WS1.addDayReportItemControllerInstance.setCurrentDayReportDate(
                Date.from((datePicker
                .getValue()
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())).toInstant())
            );
            WS1.addDayReportItemControllerInstance.setDaySales(daySales);
            WS1.addDayReportItemControllerInstance.resetForm();
            myController.setScreen(WS1.addDayReportID);
            WS1.primaryStage.setMaximized(false);
            WS1.primaryStage.setWidth(600);
            WS1.primaryStage.setHeight(640);
            WS1.primaryStage.setY(30);
        } else {
            //System.out.println("goToAddDayReportScreen4");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Невозможно создать отчет за день");
            alert.setContentText("За этот день нет ни одной не обработанной доставки");
            alert.showAndWait();
        }*/
        
        progressIndicator.visibleProperty().set(false);
    }
    
    private void fillDayReportItemObservableList(List<DayReportItem> _dayReportItemList){
        
        for (DayReportItem dayReportItem : _dayReportItemList) {

            mDayReportItemsObservableList.add(new DayReportItemModel(
                    dayReportItem.getId()
                    , String.valueOf(
                            mCarsDAOImpl.getCar(dayReportItem.getCarId()).getNumber()
                    )
                    , String.valueOf(
                            mDriversDAOImpl.getDriver(dayReportItem.getDriverId()).getName()
                    )
                    , dayReportItem.getToPay().doubleValue()
                    , dayReportItem.getProfit().doubleValue()
                    , dayReportItem.getToPayFar().doubleValue()
                    , dayReportItem.getVolume()
                    , dayReportItem.getVolumeFar()
                    , dayReportItem.getFarCount()
                    , dayReportItem.getDebtAmort().doubleValue()
                    , dayReportItem.getCleanCount()
                    , dayReportItem.getReplaceCount()
                    , dayReportItem.getDebt().doubleValue()
                    , dayReportItem.getkM()
                    , dayReportItem.getKMTonn()
                    // dayReportItem.getFuelCost().doubleValue()
                    //, dayReportItem.getAdvertisingCost().doubleValue()
                    //, dayReportItem.getOtherCost().doubleValue()
                    , dayReportItem.getNotice()
                )
            );
        }        
    }
    
    private void fillDayReportTotalObservableList(List<DayReportItem> _dayReportItemList, BigDecimal _nonSaleDebtAmorts){
        
        //Prepare all data
        BigDecimal dayToPay = new BigDecimal(0);
        BigDecimal dayToPayFar = new BigDecimal(0);
        BigDecimal dayProfit = new BigDecimal(0);
        BigDecimal dayProfitFar = new BigDecimal(0);
        BigDecimal avPrice;
        BigDecimal avPriceNoFar = new BigDecimal(0);
        int dayVolume = 0;
        int dayVolumeFar = 0;
        int dayFarCount = 0;
        
        int dayCleanCount = 0;
        int dayReplaceCount = 0;
        int dayKm = 0;
        int dayFuel = 0;
        //BigDecimal dayFuelCost = new BigDecimal(0);
        //BigDecimal dayAdvertisingCost = new BigDecimal(0);
        //BigDecimal dayOtherCost = new BigDecimal(0);
        BigDecimal dayDebt = new BigDecimal(0);
        BigDecimal dayDebtAmort = new BigDecimal(0);
        BigDecimal dayNonSaleDebtAmort = new BigDecimal(0);
        
        BigDecimal totalDebtAmort = new BigDecimal(0);
        BigDecimal totalProfit = new BigDecimal(0);
        //BigDecimal totalCost = new BigDecimal(0);
        BigDecimal totalBalance = new BigDecimal(0);
        
        int itemsCount = 0;
        
        for (DayReportItem dayReportItem : _dayReportItemList) {
            
            dayToPay = dayToPay.add(dayReportItem.getToPay());
            dayToPayFar = dayToPayFar.add(dayReportItem.getToPayFar());
            dayProfit = dayProfit.add(dayReportItem.getProfit());
            dayProfitFar = dayProfitFar.add(dayReportItem.getProfitFar());
            dayVolume += dayReportItem.getVolume();
            dayVolumeFar += dayReportItem.getVolumeFar();
            /*if (mShopsDAOImpl.getShop(_daySale.getShopId()).getFar()) {
                
                dayCarFarCount++;
            }*/
            dayFarCount += dayReportItem.getFarCount();
            
            dayDebt = dayDebt.add(dayReportItem.getDebt());
            dayDebtAmort = dayDebtAmort.add(dayReportItem.getDebtAmort());
            /*if (dayReportItem.) {
                
            }*/
            
            dayCleanCount += dayReportItem.getCleanCount();
            dayReplaceCount += dayReportItem.getReplaceCount();
            dayKm += dayReportItem.getkM();
            dayFuel += dayReportItem.getKMTonn();
            //dayFuelCost = dayFuelCost.add(dayReportItem.getFuelCost());
            //dayAdvertisingCost = dayAdvertisingCost.add(dayReportItem.getAdvertisingCost());
            //dayOtherCost = dayOtherCost.add(dayReportItem.getOtherCost());
            
            
            itemsCount++;
        }
        
        if (itemsCount > 0) {
            
            dayFuel = (int)(dayFuel / itemsCount);
        }
        //если есть некоторый объем доставленной за день воды -
        //вычисляем среднюю цену за литр воды
        /*avPrice = (dayVolume > 0)
            ? new BigDecimal(dayProfit.doubleValue() / dayVolume)
            : new BigDecimal(0);*/
        avPrice = (dayVolume > 0)
            ? new BigDecimal(dayToPay.doubleValue() / dayVolume)
            : new BigDecimal(0);
        //если есть некоторый объем доставленной за день воды в удаленные магазины -
        //вычисляем среднюю цену за литр воды, доставленный только в мариупольские магазины,
        //иначе эта величина равна общей средней цене
        /*avPriceNoFar = (dayVolumeFar > 0)
            ? new BigDecimal((dayProfit.doubleValue() - dayProfitFar.doubleValue()) / (dayVolume - dayVolumeFar))
            : avPrice;*/
        //если же вся доставленная вода - не в Мариуполь, то подставляем "0"
        if ((dayVolume - dayVolumeFar) != 0.0d) {
            
            avPriceNoFar = (dayVolumeFar > 0)
                ? new BigDecimal((dayToPay.doubleValue() - dayToPayFar.doubleValue()) / (dayVolume - dayVolumeFar))
                : avPrice;
        } else {
            
            avPriceNoFar = new BigDecimal(0);
        }
        
        
        avPrice = avPrice.setScale(3, RoundingMode.HALF_UP);
        avPriceNoFar = avPriceNoFar.setScale(3, RoundingMode.HALF_UP);
        
        /*Дополнительно суммировать значения всех погашений,
        сделанных в эту дату вне доставок*/
        
        //Пытаемся получить список изменений долга
        //в эту дату
        
        /*System.out.println("date " + Date.from((datePicker
                    .getValue()
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())).toInstant()));*/
        
        //Если сумма возвратов долга не передана через аргумент -
        //подсчитываем ее для первой даты (из первого датапикера)
        if (_nonSaleDebtAmorts == null) {
            
            List<DebtChange> dayDebtChangeList =
            mDebtsDAOImpl.getFilteredDebts(
                -1
                , -1
                , Date.from((datePicker
                    .getValue()
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())).toInstant())
                , -1
                , 0
                , -1
                , -1
            );
            //
            if (dayDebtChangeList != null) {

                for (DebtChange debtChange : dayDebtChangeList) {
                    
                    System.out.println("debtChange " + debtChange);

                    if (debtChange.getSaleId() == -1) {

                        if (!debtChange.getIsCredit()) {
                        
                            dayNonSaleDebtAmort = dayNonSaleDebtAmort.add(debtChange.getValue());
                        }
                    }
                }
            }
        } else {
        
            dayNonSaleDebtAmort = _nonSaleDebtAmorts;
        }
        
        
        /**/
        
        totalDebtAmort = totalDebtAmort.add(dayDebtAmort);
        totalDebtAmort = totalDebtAmort.add(dayNonSaleDebtAmort);
        //Добавить суммирование с другими статьями дохода
        totalProfit = totalProfit.add(dayProfit);
        totalProfit = totalProfit.add(totalDebtAmort);
        //totalCost = totalCost.add(dayFuelCost);
        //totalCost = totalCost.add(dayAdvertisingCost);
        //totalCost = totalCost.add(dayOtherCost);
        //Итого
        totalBalance = totalBalance.add(totalProfit);
        totalBalance = totalBalance.subtract(dayDebt);
        //totalBalance = totalBalance.subtract(totalCost);
        
        mDayReportTotalObservableList.add(
            new DayReportTotalModel(
                1
                , dayToPay.doubleValue()
                //прибавляем к суммарному доходу за день
                //возвраты долгов вне доставок
                , dayProfit.doubleValue() + dayNonSaleDebtAmort.doubleValue()
                , dayProfitFar.doubleValue()
                , dayVolume
                , dayVolumeFar
                , avPrice.doubleValue()
                , avPriceNoFar.doubleValue()
                , dayFarCount
                , dayDebtAmort.doubleValue()
                , dayNonSaleDebtAmort.doubleValue()
                , dayCleanCount
                , dayReplaceCount
                , dayKm
                , dayFuel
                //, dayFuelCost.doubleValue()
                //, dayAdvertisingCost.doubleValue()
                //, dayOtherCost.doubleValue()
                , dayDebt.doubleValue()
                , totalDebtAmort.doubleValue()
                , totalProfit.doubleValue()
                //, totalCost.doubleValue()
                , totalBalance.doubleValue()
            )
        );
    }
    
    private Node createPage(int _pageIndex/*, int _rowsPerPage*/) {
        
        progressIndicator.visibleProperty().set(true);
        
        mCurrentPageIdx = _pageIndex;
        int fromIndex = mCurrentPageIdx * mRowsPerPage;
        
        int toIndex = Math.min(fromIndex + mRowsPerPage, mDayReportItemsList.size());
        
        //mShopsForPage = mShopsFacade.getShopsForPage(mRowsPerPage, fromIndex);
        mDayReportItemsObservableList.clear();
        fillDayReportItemObservableList(mDayReportItemsList.subList(fromIndex, toIndex));
        
        dRepItemsTableView.setItems(mDayReportItemsObservableList);
        AnchorPane.setTopAnchor(dRepItemsTableView, 0.0);
        AnchorPane.setRightAnchor(dRepItemsTableView, 0.0);
        AnchorPane.setBottomAnchor(dRepItemsTableView, 0.0);
        AnchorPane.setLeftAnchor(dRepItemsTableView, 0.0);
        
        progressIndicator.visibleProperty().set(false);

        return new AnchorPane(dRepItemsTableView);
    }
    
    public void updateDayReportItemsForPage(){
        
        progressIndicator.visibleProperty().set(true);
        
        /*if (mDayReportItemsList == null) {
            
            mDayReportItemsList = new ArrayList<>();
        }*/
        
        /*dRepItemsPagination.setPageCount(
                !mDayReportItemsList.isEmpty()
                ? (mDayReportItemsList.size() + mRowsPerPage - 1) / mRowsPerPage
                : 1
        );*/
        
        //int fromIndex = mCurrentPageIdx * mRowsPerPage;
        mDayReportItemsList = mReportsFacade.getDayReport(
            Date.from((datePicker
            .getValue()
            .atStartOfDay()
            .atZone(ZoneId.systemDefault())).toInstant())
        );
        
        if (mDayReportItemsList == null) {
            
            mDayReportItemsList = new ArrayList<>();
        }
        
        dRepItemsPagination.setPageCount(
                !mDayReportItemsList.isEmpty()
                ? (mDayReportItemsList.size() + mRowsPerPage - 1) / mRowsPerPage
                : 1
        );
//        for (DayReportItem dayReportItem : mDayReportItemsList) {
//
//            System.out.println(dayReportItem.getProfit());
//        }
        mDayReportItemsObservableList.clear();
        fillDayReportItemObservableList(mDayReportItemsList);
        
        mDayReportTotalObservableList.clear();
        fillDayReportTotalObservableList(mDayReportItemsList, null);
        dRepTotalTableView.setItems(mDayReportTotalObservableList);
        
        progressIndicator.visibleProperty().set(false);
    }
    
    /**/
        
        private void makeHeaderWrappable(TableColumn col) {
            Label label = new Label(col.getText());
            label.setStyle("-fx-padding: 8px;");
            label.setWrapText(true);
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);

            StackPane stack = new StackPane();
            stack.getChildren().add(label);
            stack.prefWidthProperty().bind(col.widthProperty().subtract(5));
            label.prefWidthProperty().bind(stack.prefWidthProperty());
            col.setGraphic(stack);
        }
        
        //
        private void computeIntervalReport(){
        
            progressIndicator.visibleProperty().set(true);
            
            //Форматирование даты для устранения информации о часах, минутах и секундах
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            //Стартовая дата диапазона
            Date currDate = Date.from((datePicker
                .getValue()
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())).toInstant());
            try {
                currDate = sdf.parse(sdf.format(currDate));
            } catch (ParseException ex) {
                Logger.getLogger(DayReportsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Замыкающая дата диапазона
            Date closureDate = Date.from((datePicker2
                .getValue()
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())).toInstant());
            try {
                closureDate = sdf.parse(sdf.format(closureDate));
            } catch (ParseException ex) {
                Logger.getLogger(DayReportsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            mDayReportItemsList = new ArrayList<>();
            
            BigDecimal dayNonSaleDebtAmort = new BigDecimal(0);
            
            //Пока текущая дата меньше или равна замыкающей
            while (currDate.compareTo(closureDate) < 1) {                
                
                List<DayReportItem> currentReportItemsList =
                        mReportsFacade.getDayReport(currDate);
                if (currentReportItemsList != null) {
                    
                    mDayReportItemsList.addAll(mReportsFacade.getDayReport(currDate));
                }
                
                //
                dayNonSaleDebtAmort = dayNonSaleDebtAmort.add(getNonSaleDebtAmorts(currDate));
                
                //дата на день позднее
                currDate = DateUtil.addDays(currDate, 1);
            }
            
            mDayReportItemsObservableList.clear();
            //fillDayReportItemObservableList(mDayReportItemsList);

            mDayReportTotalObservableList.clear();
            //
            fillDayReportTotalObservableList(mDayReportItemsList, dayNonSaleDebtAmort);
            dRepTotalTableView.setItems(mDayReportTotalObservableList);

            progressIndicator.visibleProperty().set(false);
        }
        
        private BigDecimal getNonSaleDebtAmorts(Date _date){
        
            BigDecimal dayNonSaleDebtAmort = new BigDecimal(0);
            
            List<DebtChange> dayDebtChangeList =
                mDebtsDAOImpl.getFilteredDebts(
                    -1
                    , -1
                    , _date
                    , -1
                    , 0
                    , -1
                    , -1
            );
            //
            if (dayDebtChangeList != null) {

                for (DebtChange debtChange : dayDebtChangeList) {

                    if (debtChange.getSaleId() == -1) {

                        if (!debtChange.getIsCredit()) {
                            
                            dayNonSaleDebtAmort = dayNonSaleDebtAmort.add(debtChange.getValue());
                        }
                    }
                }
            }
            
            return dayNonSaleDebtAmort;
        }
        
        //Rounding func for double values
        private double round(double value, int places) {
            
            if (places < 0) throw new IllegalArgumentException();

            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
}
