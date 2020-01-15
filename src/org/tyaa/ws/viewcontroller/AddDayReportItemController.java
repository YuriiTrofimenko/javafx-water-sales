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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.tools.ValueExtractor;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.tyaa.ws.WS1;
import org.tyaa.ws.dao.ReportsFacade;
import org.tyaa.ws.dao.impl.BarrelCapacitiesDAOImpl;
import org.tyaa.ws.dao.impl.ShopsDAOImpl;
import org.tyaa.ws.dao.impl.BarrelsDAOImpl;
import org.tyaa.ws.dao.impl.DriversDAOImpl;
import org.tyaa.ws.dao.impl.CarsDAOImpl;
import org.tyaa.ws.dao.impl.DayReportItemsDAOImpl;
import org.tyaa.ws.dao.impl.DayReportsDAOImpl;
import org.tyaa.ws.dao.impl.DebtsDAOImpl;
import org.tyaa.ws.dao.impl.SalesDAOImpl;
import org.tyaa.ws.dao.util.DateUtil;
import org.tyaa.ws.entity.Sale;
import org.tyaa.ws.entity.Shop;
import org.tyaa.ws.entity.Barrel;
import org.tyaa.ws.entity.Driver;
import org.tyaa.ws.entity.Car;
import org.tyaa.ws.entity.DayReport;
import org.tyaa.ws.entity.DayReportItem;
import org.tyaa.ws.entity.DebtChange;

/**
 * FXML Controller class
 *
 * @author Yurii
 */
public class AddDayReportItemController implements Initializable, ControlledScreen {

    /*Внедренные хендлеры элементов UI*/
    
    //элементы ввода информации
    @FXML
    ComboBox carComboBox;    
    @FXML
    CustomTextField driverCTextField;
    @FXML
    CustomTextField fuelCountOldCTextField;
    @FXML
    CustomTextField fuelCountNewCTextField;
    @FXML
    CustomTextField kMCTextField;
    /*@FXML
    CustomTextField fuelCostCTextField;
    @FXML
    CustomTextField advertisementCTextField;    
    @FXML
    CustomTextField otherCTextField;*/
    @FXML
    CustomTextField noticeCTextField;
    
    //Объекты доступа к данным
    private SalesDAOImpl mSalesDAOImpl;
    private ShopsDAOImpl mShopsDAOImpl;
    private BarrelsDAOImpl mBarrelsDAOImpl;
    private BarrelCapacitiesDAOImpl mBarrelCapacitiesDAOImpl;
    private DriversDAOImpl mDriversDAOImpl;
    private CarsDAOImpl mCarsDAOImpl;
    private DebtsDAOImpl mDebtsDAOImpl;
    
    private ReportsFacade mReportsFacade;
    private DayReportsDAOImpl mDayReportsDAOImpl;
    private DayReportItemsDAOImpl mDayReportItemsDAOImpl;
    
    //Списки объектов
    private List<Sale> mDaySales;
    private Set<Car> mDayCars;
    private List<Sale> mDayCarSales;
    
    private List<Shop> mShops;
    private List<Barrel> mBarrels;
    private List<Barrel> mShopBarrels;
    private List<Driver> mDrivers;
    private List<Car> mCars;
    
    //Наборы полей из объектов
    private Set<String> mShopNamesSet;
    private Set<String> mDriverNamesSet;
    private Set<String> mCarNamesSet;
    
    //Выбранные объекты
    private Shop mSelectedShop;
    private Barrel mSelectedBarrel;
    private Driver mSelectedDriver;
    private Car mSelectedCar;
    
    //Дата, выбранная в данный момент в разделе Отчеты - День
    private Date mCurrentDayReportDate;
    
    //Рассчитанные значения
    
    //
    private int mCounterOld;
    //
    private int mCounterNew;
    //km
    private int mKm;
    //km/tonn of water
    private int mKmTonn;
    
    //ID отчета, который создается при добавлении первой строки отчета в БД
    //private int mDayReportId;
    
    ObservableList<Car> mCarsObservableList;
    
    ScreensController myController;
    ValidationSupport validationSupport;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        WS1.addDayReportItemControllerInstance = this;
        
        mSalesDAOImpl = new SalesDAOImpl();
        mShopsDAOImpl = new ShopsDAOImpl();
        mBarrelsDAOImpl = new BarrelsDAOImpl();
        mBarrelCapacitiesDAOImpl = new BarrelCapacitiesDAOImpl();
        mDriversDAOImpl = new DriversDAOImpl();
        mCarsDAOImpl = new CarsDAOImpl();
        mDebtsDAOImpl = new DebtsDAOImpl();
        
        mDayReportsDAOImpl = new DayReportsDAOImpl();
        mDayReportItemsDAOImpl = new DayReportItemsDAOImpl();
        
        mReportsFacade = new ReportsFacade();
        
        mDayCarSales = new ArrayList<>();
        
        mDayCars = new HashSet<>();
                
        mCarsObservableList = FXCollections.observableArrayList();
        
        mCounterOld = -1;
        mCounterNew = -1;
        mKm = -1;
        mKmTonn = -1;
        
        /*Приведение формы в исходное состояние*/
        //System.out.println("WS1.addDayReportItemControllerInstance 1");
        resetForm();
        //System.out.println("WS1.addDayReportItemControllerInstance 4");

        //Активация механизма валидации для элементов ввода типа CustomTextField
        ValueExtractor.addObservableValueExtractor(
                c -> c instanceof CustomTextField
                , c -> ((CustomTextField) c).textProperty());
        validationSupport = new ValidationSupport();
        //Явная настройка включения визуального оформления валидации
        validationSupport.setErrorDecorationEnabled(true);
        //Настройки валидации для каждого элемента ввода, подлежащего проверке
        
        /*validationSupport.registerValidator(
                fuelCountOldCTextField
                , Validator.createRegexValidator("Введите целое число (максимум - 999999)", "[0-9]{1,6}", Severity.ERROR));
        validationSupport.registerValidator(
                fuelCountNewCTextField
                , Validator.createRegexValidator("Введите целое число (максимум - 999999)", "[0-9]{1,6}", Severity.ERROR));
        validationSupport.registerValidator(
                kMCTextField
                , Validator.createRegexValidator("Введите целое число (максимум - 99999)", "[0-9]{1,5}", Severity.ERROR));*/
        
        /*validationSupport.registerValidator(
                fuelCostCTextField
                , Validator.createRegexValidator("Введите целое или дробное число (максимум - 9999.99)", "[0-9]{1,4}[.]{0,1}[0-9]{0,2}", Severity.ERROR));
        validationSupport.registerValidator(
                advertisementCTextField
                , Validator.createRegexValidator("Введите целое или дробное число (максимум - 9999.99)", "[0-9]{1,4}[.]{0,1}[0-9]{0,2}", Severity.ERROR));
        validationSupport.registerValidator(
                otherCTextField
                , Validator.createRegexValidator("Введите целое или дробное число (максимум - 9999.99)", "[0-9]{1,4}[.]{0,1}[0-9]{0,2}", Severity.ERROR));*/
        
        /*Настраиваем способ отображения информации из объектов,
        подключенных в составе коллекции-источника к контролам списочного типа*/
        
        //прорисовка выпадающего списка автомобилей
        carComboBox.setCellFactory((comboBox) -> {
            return new ListCell<Car>() {
                @Override
                protected void updateItem(Car carItem, boolean empty) {
                    super.updateItem(carItem, empty);

                    if (carItem == null || empty) {
                        setText(null);
                    } else {
                        setText(
                            String.valueOf(carItem.getNumber())
                        );
                    }
                }
            };
        });
        //прорисовка выбранного элемента выпадающего списка автомобилей
        carComboBox.setConverter(new StringConverter<Car>() {
            @Override
            public String toString(Car car) {
                if (car == null) {
                    return null;
                } else {
                    return (
                        String.valueOf(car.getNumber())
                    );
                }
            }

            @Override
            public Car fromString(String carString) {
                return null; // No conversion fromString needed.
            }
        });
        
        /*Настраиваем здесь обработчики событий тех контрлов,
        для которых это невозможно при помощи внедрения */
        
        //обработка события "выбор автомобиля"
        carComboBox.setOnAction((event) -> {
            mSelectedCar = (Car) carComboBox
                .getSelectionModel()
                .getSelectedItem();
            if (mSelectedCar != null) {
                //Получение списка продаж за текущий день
                //для выбранного автомобиля
                if (mDayCarSales != null && mDayCarSales.size() > 0) {
                    
                    mDayCarSales.clear();
                }
                for (Sale _daySale : mDaySales) {
                    
                    if (_daySale.getCarId() == mSelectedCar.getId()) {
                        
                        mDayCarSales.add(_daySale);
                    }
                }
                
                //из списка продаж за текущий день для данного автомобиля
                //узнать ИД водителя и подставить имя водителя в поле
                mSelectedDriver =
                        mDriversDAOImpl.getDriver(
                                mDayCarSales.get(0).getDriverId()
                        );
                driverCTextField.setText(mSelectedDriver.getName());
                
                /*Пытаемся найти предыдущий пункт отчета (за ближайший прошедший день)
                и подставить из него вечернее значение спидометра в поле
                текущего утреннего значения*/
                DayReportItem prevDRepItem = findLastDRepItem();
                
                if (prevDRepItem != null) {
                    
                    //если сегодня нет утренних показаний,
                    //но вчера были вечерние - они становятся сегодняшними утренними
                    if (mCounterOld == -1 && prevDRepItem.getCountNew() != -1) {

                        fuelCountOldCTextField.setText(String.valueOf(prevDRepItem.getCountNew()));
                    }
                    prevDRepItem = null;
                }
            } else {
                //countOldCTextField.setText("");
            }
            
            //System.out.println("ComboBox Action (selected: " + mSelectedBarrel.getCounter() + ")");
        });
        
        carComboBox.setOnKeyPressed((event) -> {
            
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) {
                
                carComboBox.show();
            }
        });
        
        //обработка события "ввод текста в поле счетчик New"
        fuelCountNewCTextField.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                calcFuelVolume();
            }
        });
        
        //обработка события "ввод текста в поле счетчик Old"
        fuelCountOldCTextField.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                calcFuelVolume();
            }
        });
    }    

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
    
    @FXML
    private void goToDayReportsScreen(ActionEvent event){
       myController.setScreen(WS1.dayReportsID);
       WS1.primaryStage.setMaximized(true);
    }
    
    @FXML
    private void resetForm(ActionEvent event){
       
       resetForm();
    }
    
    @FXML
    private void actionAddDayReportItem(ActionEvent actionEvent) {
        
        /*List<ValidationMessage> validationMessageList =
                (List<ValidationMessage>) validationSupport
                        .getValidationResult()
                        .getMessages();
        if (validationMessageList.isEmpty()) {*/
            
            //Если магазина с введенным названием нет в БД,
            //показываем окно ошибки
            String errorsString = "";
            boolean hasErrors = false;//mSelectedCar
            
            if (mSelectedCar == null) {
                errorsString += "Не выбран автомобиль. ";
                hasErrors = true;
            }
            
            /*if (mCounterOld == -1 && mCounterNew == -1) {
                
                errorsString += "Хотя бы одно из двух полей счетчика должно быть заполнено. ";
                hasErrors = true;
            }*/
            
            String unsignedIntPatternString = "[0-9]{1,6}";
            
            if (!Pattern.matches(unsignedIntPatternString, fuelCountOldCTextField.getText())
                    && !fuelCountOldCTextField.getText().equals("")) {
            
                errorsString += "В поле утренних показаний счетчика некорректные данные. ";
                hasErrors = true;
            }
            
            if (!Pattern.matches(unsignedIntPatternString, fuelCountNewCTextField.getText())
                    && !fuelCountNewCTextField.getText().equals("")) {
            
                errorsString += "В поле вечерних показаний счетчика некорректные данные. ";
                hasErrors = true;
            }
            
            if (hasErrors) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Строка отчета не добавлена");
                alert.setContentText(errorsString);
                alert.showAndWait();
            } else {
                
                /*проверки на подозрительные данные о счетчике и объеме*/
                
                String warningString = "";
                boolean hasWarnings = false;
            
                //по расзности счетчика с прошлым значением: израсходовано больше,
                //чем емкость бочки (проверять всегда, а если предыдущая продажа
                //не найдена - пропускать)
                
                /*//находим ID предыдущ. дост. для данной бочки
                Integer lastSaleId = mSelectedBarrel.getLastSaleId();
                //емкость выбранной бочки
                int selectedBarrelCapacityInt =
                    mBarrelCapacitiesDAOImpl.getBarrelCapacity(
                            mSelectedBarrel.getCapacityId()
                    ).getCapacity();
                //максимальная емкость, доступная сейчас
                int maxCurrentCapacity = selectedBarrelCapacityInt;
                
                if (lastSaleId != null) {
                    
                        int diff = Integer.parseInt(
                                countNewCTextField.getText()
                        ) - mSelectedBarrel.getCounter();
                        
                        int diffTest = diff - (int)((double)diff * 0.02D);
                        if (diffTest > selectedBarrelCapacityInt) {
                            warningString +=
                                    "Израсходовано больше, чем емкость бочки. ";
                            hasWarnings = true;
                        }
                        
                        //корректируем максимальную емкость, доступную сейчас
                        maxCurrentCapacity =
                            maxCurrentCapacity -
                            (selectedBarrelCapacityInt - diff);
                }
                

                //заправлено больше, чем емкость бочки [+ остаток]
                
                //если чистка или замена произведены,
                //то доступная полная емкость бочки
                if (cleanCheckBox.isSelected() || repairCheckBox.isSelected()) {
                    maxCurrentCapacity = selectedBarrelCapacityInt;
                }
                
                if (Integer.parseInt(volumeCTextField.getText()) > maxCurrentCapacity) {
                    warningString +=
                                    "Заправлено больше, чем доступная емкость бочки. ";
                            hasWarnings = true;
                }*/
                
                //Были подготовлены предупреждения - показываем их,
                //и спрашиваем: добавлять достаку или нет?
                if (hasWarnings) {
                    
                    /*Alert warningConfirmationAlert =
                        new Alert(Alert.AlertType.CONFIRMATION);
                    warningConfirmationAlert.setTitle("Подозрительные значения");
                    warningConfirmationAlert.setHeaderText("Все равно добавить доставку?");
                    warningConfirmationAlert.setContentText(warningString);

                    Optional<ButtonType> result =
                            warningConfirmationAlert.showAndWait();
                    if (result.get() == ButtonType.OK){

                        //Нажата кнопка OK - сохраняем доставку в БД
                        processDayReportSaving();
                    } else {
                        //Нажата кнопка CANCEL - показываем сообщение об отмене
                        Alert cancelWarningAlert =
                                new Alert(Alert.AlertType.WARNING);
                        cancelWarningAlert.setTitle("Предупреждение");
                        cancelWarningAlert.setHeaderText("Доставка не добавлена");
                        cancelWarningAlert.setContentText("Добавление доставки отменено");
                        cancelWarningAlert.showAndWait();
                    }*/
                } else {
                    
                    //Не было ни ошибок, ни предупреждений -
                    //сохраняем доставку в БД
                    processDayReportSaving();
                }
            }
        /*} /*else {
            
            //Если хотя бы одно из валидируемых полей не заполнено,
            //показываем окно ошибок валидации
            
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
            alert.setHeaderText("Строка отчета не добавлена");
            alert.setContentText(errorsString);
            alert.showAndWait();
        }*/
    }
    
    //Приведение формы в исходное состояние
    public void resetForm(){

        /*Пытаемся получить список уже имеющихся в БД записей отчета;
        если получили - формируем список автомобилей, для которых есть записи;*/
        
        List<DayReportItem> dayReportItemsList =
            mReportsFacade.getDayReport(mCurrentDayReportDate);
        List<Integer> savedReportCarIdList = new ArrayList<>();
        if (dayReportItemsList != null) {
            
            for (DayReportItem dayReportItem : dayReportItemsList) {
                
                savedReportCarIdList.add(dayReportItem.getCarId());
            }
        }

        //если метод очистки формы вызван не при инициализации,
        //а при клике на кнопке в вызывающей форме, значит, список доставок
        //за день передан, и можно заполнить контрол выбора автомобиля
        //(или после добавления строки отчета)
        if (mDaySales != null) {
                        
            //формируем коллекцию автомобилей, для которых
            //существуют доставки за выбранный день (с неповторяющимися объектами)
            if (mDayCars != null && mDayCars.size() > 0 ) {
                
                mDayCars.clear();
            }
            
            for (Sale daySale : mDaySales) {

                //Если запись отчета для данного авто уже есть в БД,
                //то не добавляем это авто в источник выпадающего списка
                if (!savedReportCarIdList.contains(daySale.getCarId())) {
                    
                    mDayCars.add(mCarsDAOImpl.getCar(daySale.getCarId()));
                }
            }
            
            //очищаем наблюдабельный набор автомобилей для выпадающего списка
            mCarsObservableList.clear();
            
            //заполняем наблюдабельный набор автомобилей для выпадающего списка
            for (Car dayCar : mDayCars) {

                mCarsObservableList.add(dayCar);
            }
        }
        
        
        //System.out.println("WS1.addDayReportItemControllerInstance 3");
        
        
        //очистка элементов ввода и установка новых наборов значений
        //carComboBox.getItems().clear();
        if (mCarsObservableList.size() > 0) {
            //заполняем выпадающий список автомобилей из набл-й коллекции
            carComboBox.setItems(mCarsObservableList);
        }
        
        driverCTextField.clear();
        fuelCountOldCTextField.setText("");
        fuelCountNewCTextField.setText("");
        kMCTextField.setText("");
        
        mCounterOld = -1;
        mCounterNew = -1;
        mKm = -1;
        mKmTonn = -1;
    }
    
    private void processDayReportSaving(){

        //
        DayReport dayReport =
            mDayReportsDAOImpl.getReport(mCurrentDayReportDate);
        if (dayReport == null) {
            
            dayReport = new DayReport();
            dayReport.setDate(mCurrentDayReportDate);
            
            //Добавляем в БД новую запись "дневной отчет"
            mDayReportsDAOImpl.createReport(dayReport);
        }
        
        /*показания спидометра - попытки восполнения недостающих данных
        из текущего дня в предыдущий или наоборот*/
        
        DayReportItem prevDRepItem = findLastDRepItem();
        
        //если нашли - проверяем, есть ли у него вечерние показания
        if (prevDRepItem != null) {
            //если сегодня есть утренние показания - они становятся
            //при необходимости также вчерашними вечерними
            if (prevDRepItem.getCountNew() == -1 && mCounterOld != -1) {
                
                prevDRepItem.setCountNew(mCounterOld);
                
                //также считаем пройденный путь и расход за прошлый раз
                int prevCountOld = prevDRepItem.getCountOld();
                int prevCountNew = mCounterOld;
                int prevKM = -1;
                int prevKmTonn = -1;
                
                prevKM = prevCountNew - prevCountOld;
                if (prevKM < 0) {

                    prevKM =
                        (999999 - prevCountOld)
                        + prevCountNew
                        + 1;
                    
                    //Введено значение нового счетчика меньше значения старого,
                    //и при этом объем потребления оказался слишком большим
                    if (prevKM > 9999) {

                        prevKM = -1;
                    }
                }
                prevDRepItem.setkM(prevKM);
                
                int prevDayCarVolume = prevDRepItem.getVolume();
                if (prevDayCarVolume > 0) {
                    //Пробег на доставку 1 тонны воды - вычисляем:
                    //(S,km * 1000,L) / Vwater,L
                    prevKmTonn =
                        (int)Math.round((prevKM * 1000) / prevDayCarVolume);
                    prevDRepItem.setKMTonn(prevKmTonn);
                } else {

                    prevKmTonn = -1;
                }
                
                //сохраняем все сделанные изменния в БД
                mDayReportItemsDAOImpl.updateReportItem(prevDRepItem);
            }
            //если сегодня нет утренних показаний,
            //но вчера были вечерние - они становятся сегодняшними утренними
            if (mCounterOld == -1 && prevDRepItem.getCountNew() != -1) {
                
                mCounterOld = prevDRepItem.getCountNew();
                
                //также считаем пройденный путь и расход
                mKm = mCounterNew - mCounterOld;
                if (mKm < 0) {

                    mKm =
                        (999999 - mCounterOld)
                        + mCounterNew
                        + 1;
                    //Введено значение нового счетчика меньше значения старого,
                    //и при этом объем потребления оказался слишком большим
                    if (mKm > 9999) {

                        mKm = -1;
                    }
                }
                int dayCarVolume = 0;
                //System.out.println("1");
                if (mDayCarSales != null) {
                //System.out.println("2"); 
                    for (Sale _daySale : mDayCarSales) {
        //System.out.println("3");
                        dayCarVolume += _daySale.getVolume();
                    }
                }
                if (dayCarVolume > 0) {
                    //Пробег на доставку 1 тонны воды - вычисляем:
                    //(S,km * 1000,L) / Vwater,L
                    mKmTonn =
                        (int)Math.round((mKm * 1000) / dayCarVolume);
                } else {

                    mKmTonn = -1;
                }
            }
            prevDRepItem = null;
        }
        
        /*DayReport dayReport2 =
            mDayReportsDAOImpl.getReport(mCurrentDayReportDate);*/
        int dayReportId = dayReport.getId();
        
        //Prepare all data
        BigDecimal dayCarToPay = new BigDecimal(0);
        BigDecimal dayCarToPayFar = new BigDecimal(0);
        BigDecimal dayCarProfit = new BigDecimal(0);
        BigDecimal dayCarProfitFar = new BigDecimal(0);
        
        int dayCarVolume = 0;
        int dayCarVolumeFar = 0;
        int dayCarFarCount = 0;
        
        BigDecimal dayCarDebtAmort = new BigDecimal(0);
        BigDecimal dayCarDebtAmortFar = new BigDecimal(0);
        
        //Суммы погашений, которые нужно вычесть из
        //сумм действительных доходов, т.к. это отражения авансовых прибавок
        BigDecimal dayCarDebtAmortForSubstr = new BigDecimal(0);
        BigDecimal dayCarDebtAmortFarForSubstr = new BigDecimal(0);
        
        int dayCarCleanCount = 0;
        int dayCarReplaceCount = 0;
        BigDecimal dayCarDebt = new BigDecimal(0);
        
        for (Sale _daySale : mDayCarSales) {
            
            dayCarToPay = dayCarToPay.add(_daySale.getToPay());
            dayCarProfit = dayCarProfit.add(_daySale.getProfit());
            dayCarVolume += _daySale.getVolume();
            //Доставка была в удаленный магазин - зачисляем балл
            //дальних доставок
            if (mShopsDAOImpl.getShop(_daySale.getShopId()).getFar()) {
                
                dayCarFarCount++;
                //также в отдельное поле прибавляем доход,
                dayCarProfitFar = dayCarProfitFar.add(_daySale.getProfit());
                //
                dayCarToPayFar = dayCarToPayFar.add(_daySale.getToPay());
                //... и доставленный объем воды
                dayCarVolumeFar += _daySale.getVolume();
            }
            if (_daySale.getCleaning()) {
                
                dayCarCleanCount++;
            }
            if (_daySale.getRepair()) {
                
                dayCarReplaceCount++;
            }
            //Пытаемся получить весь список изменений долга,
            //произошедших при данной доставке
            /*System.out.println("_daySale.getId() "
                + _daySale.getId());*/
            List<DebtChange> dayDebtChangeList =
                mDebtsDAOImpl.getFilteredDebts(
                    -1
                    , _daySale.getId()
                    , null
                    , -1
                    , -1
                    , -1
                    , -1
                );
            /*System.out.println("dayDebtChangeList "
                + dayDebtChangeList);*/
            for (DebtChange debtChange : dayDebtChangeList) {
                
                /*System.out.println("debtChange "
                    + debtChange);*/
                
                //Флаг "это - погашение аванса"
                boolean isDebtAmortBecauseCreditAmorted = false;
                
                if (debtChange.getIsDebt()) {
                    
                    //прибавляем долг к сумме долгов
                    dayCarDebt = dayCarDebt.add(debtChange.getValue());
                    
                } else {
                    
                    //Получаем объект долга, за который это погашение
                    DebtChange debt = null;
                    if (debtChange.getDebtId() != -1) {
                        
                        debt = mDebtsDAOImpl.getDebt(debtChange.getDebtId());
                        /*double valueBalanceDiffDouble =
                        debtChange.getValue().doubleValue() -
                            debtChange.getBalance().doubleValue();*/
                        
                        //If это временный фиктивный долг, который был тут же погашен
                        //с погашением вручную аванса, который использовался
                        /*System.out.println("debt.getValue().doubleValue() !=\n"
                                + " debt.getBalance().doubleValue()"
                                + debt.getValue().doubleValue()
                                + " "
                                + debt.getBalance().doubleValue());*/
                        /*System.out.println("debtChange.getDate().getTime() ==\n"
                                + " debt.getDate().getTime()"
                                + debtChange.getDate().getTime()
                                + " "
                                + debt.getDate().getTime());*/
                        if (debt.getValue().doubleValue() !=
                            debt.getBalance().doubleValue()
                                && debtChange.getDate().getTime() ==
                                debt.getDate().getTime()) {

                            isDebtAmortBecauseCreditAmorted = true;
                        }
                    }
                    
                    //Если это не погашение с использованием аванса -
                    //суммируем его с другими погашениями
                    if (!isDebtAmortBecauseCreditAmorted) {
                        
                        //погашение прибавляем к сумме погашений долгов
                        dayCarDebtAmort = dayCarDebtAmort.add(debtChange.getValue());
                        
                        if (debtChange.getIsCredit()) {
                            
                            dayCarDebtAmortForSubstr =
                                dayCarDebtAmortForSubstr.add(debtChange.getValue());
                        }
                        //если доставка в удаленный магазин - 
                        //погашение прибавляем также к сумме погашений долгов удаленных магазинов
                        if (mShopsDAOImpl.getShop(_daySale.getShopId()).getFar()) {

                            dayCarDebtAmortFar = dayCarDebtAmortFar.add(debtChange.getValue());
                            
                            if (debtChange.getIsCredit()) {

                                dayCarDebtAmortFarForSubstr =
                                    dayCarDebtAmortFarForSubstr.add(debtChange.getValue());
                            }
                        }
                    }
                }
            }
        }
        
        //полученные выбором из списка
//        System.out.println("mSelectedCar.getId() " + mSelectedCar.getId());
//        System.out.println("mSelectedDriver.getId() " + mSelectedDriver.getId());
        
        //полученные из доставок
//        System.out.println("dayCarProfit " + dayCarProfit.doubleValue());
//        System.out.println("dayCarVolume " + dayCarVolume);
//        System.out.println("dayCarFarCount " + dayCarFarCount);
//        System.out.println("dayCarCleanCount " + dayCarCleanCount);
//        System.out.println("dayCarReplaceCount " + dayCarReplaceCount);
//        System.out.println("dayCarDebt " + dayCarDebt.doubleValue());
        
        //вычисленное
//        System.out.println("mFuelVolume " + mFuelVolume);
        
        //введенные пользователем
//        System.out.println("fuelCostCTextField " + fuelCostCTextField.getText());
//        System.out.println("advertisementCTextField " + advertisementCTextField.getText());
//        System.out.println("otherCTextField " + otherCTextField.getText());
//        System.out.println("noticeCTextField " + noticeCTextField.getText());
        
        
        

        DayReportItem dayReportItem = new DayReportItem();
        dayReportItem.setReportId(dayReportId);
        dayReportItem.setCarId(mSelectedCar.getId());
        dayReportItem.setDriverId(mSelectedDriver.getId());
        dayReportItem.setToPay(dayCarToPay);
        dayReportItem.setToPayFar(dayCarToPayFar);
        //при сохранении дневных доходов автомобиля
        //прибавляем к ним возвращенные долги
        dayCarProfit = dayCarProfit.add(dayCarDebtAmort);
        dayCarProfitFar = dayCarProfitFar.add(dayCarDebtAmortFar);
        //и вычитаем из полученных значений суммы новых авансов
        dayCarProfit = dayCarProfit.subtract(dayCarDebtAmortForSubstr);
        dayCarProfitFar = dayCarProfitFar.subtract(dayCarDebtAmortFarForSubstr);
        
        dayReportItem.setProfit(dayCarProfit);
        dayReportItem.setProfitFar(dayCarProfitFar);
        
        dayReportItem.setVolume(dayCarVolume);
        dayReportItem.setVolumeFar(dayCarVolumeFar);
        dayReportItem.setFarCount(dayCarFarCount);
        dayReportItem.setDebtAmort(dayCarDebtAmort);
        dayReportItem.setCleanCount(dayCarCleanCount);
        dayReportItem.setReplaceCount(dayCarReplaceCount);
        dayReportItem.setDebt(dayCarDebt);
        dayReportItem.setCountOld(mCounterOld);
        dayReportItem.setCountNew(mCounterNew);
        dayReportItem.setkM(mKm);
        dayReportItem.setKMTonn(mKmTonn);
        //dayReportItem.setFuelCost(new BigDecimal(fuelCostCTextField.getText()));
        //dayReportItem.setAdvertisingCost(new BigDecimal(advertisementCTextField.getText()));
        //dayReportItem.setOtherCost(new BigDecimal(otherCTextField.getText()));
        dayReportItem.setNotice(noticeCTextField.getText());
        
        mDayReportItemsDAOImpl.createReportItem(dayReportItem);

        //Сообщение пользователю об успешном добавлении доставки в БД
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText("Строка отчета успешно добавлена");
        //Показать сообщение и ждать
        alert.showAndWait();
        //Приводим форму в исходное состояние
        resetForm();
        //Вызываем обновление данных в коллекции-источнике для
        //таблицы дневных отчетов (в представлении дневных отчетов)
        WS1.dayReportsControllerInstance.updateDayReportItemsForPage();
    }
    
    //метод установки списка доставок за данное число
    public void setDaySales(List<Sale> _daySales)
    {
        mDaySales = _daySales;
    }
    
    //метод установки даты
    public void setCurrentDayReportDate(Date _currentDayReportDate)
    {
        mCurrentDayReportDate = _currentDayReportDate;
    }
    
    //Вычисляем пробег автомобиля за день (км)
    //как разность утренних и вечерних показаний спидометра
    private void calcFuelVolume(){
        
        //                
        //TODO если существует предыдущий отчет для данного автомобиля,
        //взять из него вечерние показания счетчика топлива
        //и вывести в соотв-е поле (если нет - можно ввести вручную)
        //или вариант 2: добавить поле в сущность автомобиль и работать с ним
        //ПОКА ОСТАВЛЯЕМ РУЧНОЙ ВВОД
        
        int dayCarVolume = 0;
        //System.out.println("1");
        if (mDayCarSales != null) {
        //System.out.println("2"); 
            for (Sale _daySale : mDayCarSales) {
//System.out.println("3");
                dayCarVolume += _daySale.getVolume();
            }
        }
        //System.out.println(dayCarVolume);
        //String signedIntPatternString = "[-]{0,1}[0-9]{1,}";
        String unsignedIntPatternString = "[0-9]{1,6}";
        
        if (!fuelCountOldCTextField.getText().equals("")) {
            //System.out.println(4);
            if (Pattern.matches(unsignedIntPatternString, fuelCountOldCTextField.getText())){
            //System.out.println(5);
                mCounterOld = Integer.valueOf(fuelCountOldCTextField.getText());
            }
        } else {
        //System.out.println(6);
            mCounterOld = -1;
            mKm = -1;
            mKmTonn = -1;
        }
        
        if (!fuelCountNewCTextField.getText().equals("")) {
            
            if (Pattern.matches(unsignedIntPatternString, fuelCountNewCTextField.getText())){
            
                mCounterNew = Integer.valueOf(fuelCountNewCTextField.getText());
            }
        } else {
        
            mCounterNew = -1;
            mKm = -1;
            mKmTonn = -1;
        }
        
        if (Pattern.matches(unsignedIntPatternString, fuelCountNewCTextField.getText())
                && Pattern.matches(unsignedIntPatternString, fuelCountOldCTextField.getText())) {
            
            //вычисляем, сколько км пройдено
            mKm = (Integer.valueOf(
                    fuelCountNewCTextField.getText()
                ) - Integer.valueOf(fuelCountOldCTextField.getText())
            );
            
            //Если значение спидометра пересекло максимум,
            //пересчитываем пробег с учетом этой особенности
            if (mKm < 0) {

                mKm =
                    (999999 - Integer.valueOf(fuelCountOldCTextField.getText()))
                    + Integer.valueOf(
                    fuelCountNewCTextField.getText())
                    + 1;
                //Введено значение нового счетчика меньше значения старого,
                //и при этом объем потребления оказался слишком большим
                if (mKm > 9999) {

                    mKm = -1;
                }
            }
            
            //В поле пробега показываем вычисленное значение
            if (mKm != -1) {
             
                kMCTextField.textProperty()
                    .setValue(String.valueOf(mKm));
            } else {
            
                kMCTextField.setText("");
            }
            //System.out.println(mKm);
            if (dayCarVolume > 0) {
                //Пробег на доставку 1 тонны воды - вычисляем:
                //(S,km * 1000,L) / Vwater,L
                mKmTonn =
                    (int)Math.round((mKm * 1000) / dayCarVolume);
            } else {

                mKmTonn = -1;
                kMCTextField.textProperty().setValue("");
            }
        }
    }
    
    private DayReportItem findLastDRepItem(){
    
        //пытаемся найти предыдущий пункт отчета с данной парой авто-водитель
        //boolean prevDayRepFound = false;
        Date prevDate = null;
        //счетчик отмотанных дней
        int i = 0;
        DayReport prevDayReport = null;
        DayReportItem prevDRepItem = null;
        
        BIG_CICLE:while (true) {            
            
            if (i == 50) {

                break;
            }
            
            //дата на день ранее
            prevDate = DateUtil.addDays(mCurrentDayReportDate, -(i + 1));
            //
            prevDayReport = mDayReportsDAOImpl.getReport(prevDate);
            //если отчет за предыдущий день найден - ищем в нем пункт
            //с нужным автомобилем
            if (prevDayReport != null) {
                
                List<DayReportItem> tmpDRepItems =
                    mDayReportItemsDAOImpl.getReports(prevDayReport.getId());
                if (tmpDRepItems != null && tmpDRepItems.size() > 0) {
                    
                    for (DayReportItem tmpDRepItem : tmpDRepItems) {
                        
                        if (tmpDRepItem.getCarId() == mSelectedCar.getId()) {
                            
                            prevDRepItem = tmpDRepItem;
                            break BIG_CICLE;
                        }
                    }
                }
            }
            i++;
        }
        return prevDRepItem;
    }
}
