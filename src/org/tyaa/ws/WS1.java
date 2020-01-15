/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws;

import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.tyaa.ws.screensframework.ScreensController;
import org.tyaa.ws.viewcontroller.AddBarrelController;
import org.tyaa.ws.viewcontroller.AddDayReportItemController;
import org.tyaa.ws.viewcontroller.AddSaleController;
import org.tyaa.ws.viewcontroller.BarrelsController;
import org.tyaa.ws.viewcontroller.CarsController;
import org.tyaa.ws.viewcontroller.SalesController;
import org.tyaa.ws.viewcontroller.DayReportsController;
import org.tyaa.ws.viewcontroller.DebtsController;
import org.tyaa.ws.viewcontroller.DriversController;
import org.tyaa.ws.viewcontroller.ShopsController;

//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//
//import org.tyaa.ws.entity.BarrelCapacity;

/**
 *
 * @author Юрий
 */
public class WS1 extends Application
{
    //
    public static Stage primaryStage;
    
    public static Dimension d;
    
    //water types
    public static String waterTypesID = "water_types";
    public static String waterTypesView = "/org/tyaa/ws/view/WaterTypes.fxml";
    //create water type
    public static String addWaterTypeID = "add_water_type";
    public static String addWaterTypeView = "/org/tyaa/ws/view/AddWaterType.fxml";
    //update water type
    public static String updWaterTypeID = "upd_water_type";
    public static String updWaterTypeView = "/org/tyaa/ws/view/UpdWaterType.fxml";
    
    //barrel capacities
    public static String barrelCapacitiesID = "barrel_capacities";
    public static String barrelCapacitiesView = "/org/tyaa/ws/view/BarrelCapacities.fxml";
    //create barrel capacity
    public static String addBarrelCapacityID = "add_barrel_capacity";
    public static String addBarrelCapacityView = "/org/tyaa/ws/view/AddBarrelCapacity.fxml";
    
    //barrels
    public static String barrelsID = "barrels";
    public static String barrelsView = "/org/tyaa/ws/view/Barrels.fxml";
    //create barrel
    public static String addBarrelID = "add_barrel";
    public static String addBarrelView = "/org/tyaa/ws/view/AddBarrel.fxml";
    //move barrel
    public static String moveBarrelID = "move_barrel";
    public static String moveBarrelView = "/org/tyaa/ws/view/MoveBarrel.fxml";
    
    //shops
    public static String shopsID = "shops";
    public static String shopsView = "/org/tyaa/ws/view/Shops.fxml";
    //create shop
    public static String addShopID = "add_shop";
    public static String addShopView = "/org/tyaa/ws/view/AddShop.fxml";
    //update shop
    public static String updShopID = "upd_shop";
    public static String updShopView = "/org/tyaa/ws/view/UpdShop.fxml";
    
    //cars
    public static String carsID = "cars";
    public static String carsView = "/org/tyaa/ws/view/Cars.fxml";
    //create car
    public static String addCarID = "add_car";
    public static String addCarView = "/org/tyaa/ws/view/AddCar.fxml";
    //update car
    public static String updCarID = "upd_car";
    public static String updCarView = "/org/tyaa/ws/view/UpdCar.fxml";
    
    //drivers
    public static String driversID = "drivers";
    public static String driversView = "/org/tyaa/ws/view/Drivers.fxml";
    //create driver
    public static String addDriverID = "add_driver";
    public static String addDriverView = "/org/tyaa/ws/view/AddDriver.fxml";
    //update driver
    public static String updDriverID = "upd_driver";
    public static String updDriverView = "/org/tyaa/ws/view/UpdDriver.fxml";
    
    //sales
    public static String salesID = "sales";
    public static String salesView = "/org/tyaa/ws/view/Sales.fxml";
    //create sale
    public static String addSaleID = "add_sale";
    public static String addSaleView = "/org/tyaa/ws/view/AddSale.fxml";
    //update sale
    public static String updSaleID = "upd_sale";
    public static String updSaleView = "/org/tyaa/ws/view/UpdSale.fxml";
    
    //Common settings
    public static String settingsID = "settings";
    public static String settingsView = "/org/tyaa/ws/view/Settings.fxml";
    
    //Login form
    public static String loginID = "login";
    public static String loginView = "/org/tyaa/ws/view/Login.fxml";
    
    //Reports menu
    public static String reportsID = "reports";
    public static String reportsView = "/org/tyaa/ws/view/Reports.fxml";
    //Day reports
    public static String dayReportsID = "day_reports";
    public static String dayReportsView = "/org/tyaa/ws/view/DayReports.fxml";
    //Add day report
    public static String addDayReportID = "add_day_report";
    public static String addDayReportView = "/org/tyaa/ws/view/AddDayReportItem.fxml";
    //Weekly reports
    public static String weeklyReportsID = "weekly_reports";
    public static String weeklyReportsView = "/org/tyaa/ws/view/WeeklyReports.fxml";
    //Add weekly report
    public static String addWeeklyReportID = "add_weekly_report";
    public static String addWeeklyReportView = "/org/tyaa/ws/view/AddWeeklyReport.fxml";
    //Monthly reports
    public static String monthlyReportsID = "monthly_reports";
    public static String monthlyReportsView = "/org/tyaa/ws/view/MonthlyReports.fxml";
    //Add monthly report
    public static String addMonthlyReportID = "add_monthly_report";
    public static String addMonthlyReportView = "/org/tyaa/ws/view/AddMonthlyReport.fxml";
    //Annual reports
    public static String annualReportsID = "annual_reports";
    public static String annualReportsView = "/org/tyaa/ws/view/AnnualReports.fxml";
    //Add annual report
    public static String addAnnualReportID = "add_annual_report";
    public static String addAnnualReportView = "/org/tyaa/ws/view/AddAnnualReport.fxml";
    
    //Debts
    public static String debtsID = "debts";
    public static String debtsView = "/org/tyaa/ws/view/Debts.fxml";
    
    //Get the task
    //public static String taskID = "task";
    //public static String taskView = "/org/tyaa/ws/view/Task.fxml";
    
    //
    public static AddSaleController addSaleControllerInstance;
    public static AddBarrelController addBarrelControllerInstance;
    public static BarrelsController barrelsControllerInstance;
    public static ShopsController shopsControllerInstance;
    public static SalesController salesControllerInstance;
    public static DayReportsController dayReportsControllerInstance;
    public static AddDayReportItemController addDayReportItemControllerInstance;
    public static DebtsController debtsControllerInstance;
    public static DriversController driversControllerInstance;
    public static CarsController carsControllerInstance;
    
    /**
     * @param args the command line arguments
     */    
    public static void main(String[] args)
    {
        launch(args);
        //System.out.println(Date.from(Instant.now()).getTime() - Date.from(Instant.now()).getDate() + );
//        WS1 main = new WS1();
//        main.initEntityManager();
//        main.createAndRead();
        //main.createAndRollback();
    }
    
    @Override
    public void start(Stage _primaryStage){
        
        primaryStage = _primaryStage;
        
        //Создаем объект скринс-фреймворка (контейнер представлений)
        ScreensController screensContainer = new ScreensController();
        //Добавляем в него представления главного окна и окна добавления продажи
        screensContainer.loadScreen(WS1.salesID, WS1.salesView);
        screensContainer.loadScreen(WS1.addSaleID, WS1.addSaleView);
        
        screensContainer.loadScreen(WS1.barrelsID, WS1.barrelsView);
        screensContainer.loadScreen(WS1.addBarrelID, WS1.addBarrelView);
        
        screensContainer.loadScreen(WS1.shopsID, WS1.shopsView);
        screensContainer.loadScreen(WS1.addShopID, WS1.addShopView);
        
        screensContainer.loadScreen(WS1.driversID, WS1.driversView);
        screensContainer.loadScreen(WS1.addDriverID, WS1.addDriverView);
        
        screensContainer.loadScreen(WS1.carsID, WS1.carsView);
        screensContainer.loadScreen(WS1.addCarID, WS1.addCarView);
        
        /*Reports*/
        screensContainer.loadScreen(WS1.dayReportsID, WS1.dayReportsView);
        screensContainer.loadScreen(WS1.addDayReportID, WS1.addDayReportView);
        
        /*Debts*/
        screensContainer.loadScreen(WS1.debtsID, WS1.debtsView);
        //screensContainer.loadScreen(WS1.addDayReportID, WS1.addDayReportView);
        
        /*Настройки*/
        screensContainer.loadScreen(WS1.settingsID, WS1.settingsView);
        
        /*Форма входа (ввода имени и пароля)*/
        screensContainer.loadScreen(WS1.loginID, WS1.loginView);
        
        //Устанавливаем представление экрана входа в качестве текущего
        screensContainer.setScreen(WS1.loginID);
        //Создаем корневой контейнер, помещаем в него наш контейнер представлений,
        //на его базе - сцену, которую подключаем в главный стейдж и отображаем стейдж
        
//        BorderPane root = new BorderPane();
//        
//        AnchorPane menuRoot = new AnchorPane();
//        FXMLLoader loader = new FXMLLoader();
        
        AnchorPane root = new AnchorPane();
        AnchorPane.setTopAnchor(screensContainer, 0.0);
        AnchorPane.setRightAnchor(screensContainer, 0.0);
        AnchorPane.setLeftAnchor(screensContainer, 0.0);
        AnchorPane.setBottomAnchor(screensContainer, 0.0);
        root.getChildren().addAll(screensContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Доставка воды. Основное приложение");
        //primaryStage.setFullScreen(true);
        //primaryStage.setMaximized(true);
        WS1.d = Toolkit.getDefaultToolkit().getScreenSize();
        WS1.primaryStage.setMaximized(false);
        WS1.primaryStage.setWidth(400);
        WS1.primaryStage.setHeight(300);
        WS1.primaryStage.setX(d.width/2-(primaryStage.getWidth()/2));
        WS1.primaryStage.setY(d.height/2-(primaryStage.getHeight()/2));
        primaryStage.show();
    }
}
