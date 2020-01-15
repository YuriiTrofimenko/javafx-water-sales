/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.tyaa.ws.dao.util.DateUtil;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tyaa.ws.common.BarrelWarning;
import org.tyaa.ws.common.Globals;
import org.tyaa.ws.common.Settings;
import org.tyaa.ws.dao.impl.BarrelCapacitiesDAOImpl;
import org.tyaa.ws.dao.impl.BarrelsDAOImpl;
import org.tyaa.ws.dao.impl.SalesDAOImpl;
import org.tyaa.ws.entity.Barrel;
import org.tyaa.ws.entity.Sale;
import org.tyaa.ws.viewcontroller.BarrelsController;

/**
 *
 * @author Юрий
 */
public class BarrelsFacade
{
    private BarrelsDAOImpl mBarrelsDAOImpl;
    private BarrelCapacitiesDAOImpl mBarrelCapacitiesDAOImpl;
    private SalesDAOImpl mSalesDAOImpl;
    private List<Barrel> mBarrelsList;
    private int mMaxResults;
    private int mFirstResult;
    
    //Применять ли опции визуального фильтра?
    private boolean mFilter;
    
    //опции визуального фильтра
    private int mFilterShopId;
    private boolean mNeedClean;
    private boolean mNeedCharge;
    
    //Опция "требует заправки или чистки" - для составления заданий,
    //не зависит от опции mFilter
    private boolean mNeedCleanOrCharge;
    //Опция "только не удаленные (исключенные из оборота) бочки",
    //не зависит от опции mFilter
    //Значения: -1 не фильтровать,
    //0 - только не активные
    //1 - только активные
    private int mActive;
    
    public BarrelsFacade(){
        
        mBarrelsDAOImpl = new BarrelsDAOImpl();
        mBarrelCapacitiesDAOImpl = new BarrelCapacitiesDAOImpl();
        mSalesDAOImpl = new SalesDAOImpl();
    }
    
    public List<Barrel> getBarrelsForPage(int _maxResults, int _firstResult){
        
        mMaxResults = _maxResults;
        mFirstResult = _firstResult;
        
        if (mFilter) {
            
            //mBarrelsList = mBarrelsDAOImpl.getF
            mBarrelsList =  mBarrelsDAOImpl.getFilteredBarels(
                mFilterShopId
                //, mFilterBarrelId
                //, -1
                , null
                , mActive
                , _maxResults
                , _firstResult
            );
        } else if(mActive != -1){
        
            mBarrelsList =  mBarrelsDAOImpl.getFilteredBarels(
                -1
                //, mFilterBarrelId
                //, -1
                , null
                , mActive
                , _maxResults
                , _firstResult
            );
        } else {
        
            mBarrelsList = mBarrelsDAOImpl.getAllBarrels();
        }
        
        if (mBarrelsList == null) {
            
            mBarrelsList = new ArrayList<>();
        }
        
        /*if (mMaxResults != -1 && mFirstResult != -1) {
            
            return mBarrelsDAOImpl.getBarrelsRange(mMaxResults, mFirstResult);
        } else {
        
            return mBarrelsDAOImpl.getAllBarrels();
        }*/
        
        mBarrelsList = needChargeFilter(mBarrelsList);
        
        return mBarrelsList;
    }
    
    public void setFilter(boolean _filter)
    {
        mFilter = _filter;
    }
    
    public void setFilterShopId(int _filterShopId)
    {
        mFilterShopId = _filterShopId;
    }
    
    public void setNeedClean(boolean _needClean)
    {
        mNeedClean = _needClean;
    }
    
    public void setNeedCharge(boolean _needCharge)
    {
        mNeedCharge = _needCharge;
    }
    
    public void setNeedCleanOrCharge(boolean _needCleanOrCharge)
    {
        mNeedCleanOrCharge = _needCleanOrCharge;
    }
    
    public void setActive(int _active)
    {
        mActive = _active;
    }
    
    //заполняет глобальный список объектов-пар: ИД бочки - необходимость заправки,
    //а если включены соответствующие фильтры на форме - удаляет ненужные бочки
    //из списка для вывода в представление (которые не нужно чистить и/или заправлять)
    public List<Barrel> needChargeFilter(List<Barrel> _barrelsList){
        
        List<Barrel> barrelsList = _barrelsList;
        //Очищаем список объектов необходимости заправки
        Globals.mBarrelWarningList.clear();
        //
        if (barrelsList.size() > 0) {
            
            //текущая дата для определения давности последнего подсчета
            //предположительной необходимой периодичности заправок
            Date currentDate = Date.from(Instant.now());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            //int currentDayOfWeekNum = calendar.get(Calendar.DAY_OF_WEEK);
            //дата последней доставки воды для бочки
            Date lastSaleDate = null;

            //перебираем все полученные бочки с целью обновить устаревшие данные
            //необходимой периодичности доставок
            Iterator barrelIterator = barrelsList.iterator();
            while (barrelIterator.hasNext()) {
            //for (Barrel barrel : barrelsList) {

                Barrel barrel = (Barrel)barrelIterator.next();
                
                int capacity =
                        mBarrelCapacitiesDAOImpl
                                .getBarrelCapacity(barrel.getCapacityId())
                                .getCapacity();
                //
                int allowedRest = barrel.getAllowedRest();                
                
                //!!!old func begin!!!
                
                //дата последнего обновления периода из БД
                //Verify!
                Date oldPriodCalcDate = barrel.getPeriodCalcDate();
                //
                long diffMilliseconds = 0;
                int diffDays = 0;
                
                if (oldPriodCalcDate != null) {
                    
                    diffMilliseconds = currentDate.getTime() - oldPriodCalcDate.getTime();
                    
                    //давность последнего подсчета периодичности
                    diffDays =
                        (int)TimeUnit.DAYS.convert(
                                diffMilliseconds
                                , TimeUnit.MILLISECONDS
                        );
                }
                //последняя доставка
                Sale lastSale = null;
                Integer lastSaleId = barrel.getLastSaleId();
                //
                if (lastSaleId != null) {
                    
                    lastSale = mSalesDAOImpl.getSale(lastSaleId);
                    //получаем дату последней доставки
                    lastSaleDate = lastSale.getCreatedAt();
                }
                //если хотя бы одна доставка уже была,
                //подсчитанный период для доставок устарел на 7 или более дней,
                //в БД есть дата прошлого вычисления -
                //вычисляем новый период и сохраняем в БД
                //(дополнительно проверяем, не был ли осуществлен данный подсчет в данном сеансе работы с приложением)
                /*if ((diffDays >= 7) && lastSale != null && Globals.mAllowCalcPeriod) {

                    //расход воды за i дней
                    int waterConsumption = 0;
                    //средний расход воды за i дней
                    int averageWaterConsumption = 0;
                    //получаем дату последней доставки
                    //lastSaleDate = lastSale.getCreatedAt();

                    Sale xDaysBeforeSaleStart = null;
                    Sale tmpBeforeSale = null;
                    List<Sale> tmpBeforeSaleList = null;
                    List<Sale> daysBeforeSaleList = new ArrayList<>();
                    Date prevDate = null;
                    int daysBefore = 0;
                    //флаг, сигнализирующий о том, что была найдена первая доставка,
                    //совершенная 7 или больше дней назад
                    boolean firstFoundPrevSaleFlag = false;

                    //счетчик отмотанных дней
                    int i = 0;

                    //пока нет сигнала, что стартовая доставка диапазона найдена
                    while (!firstFoundPrevSaleFlag) {
                        //если доставок для диапазона вычисления не найдено,
                        //а число итераций уже слишком большое - прекращаем поиск
                        if (i == 50) {
                            break;
                        }
                        //дата на день ранее
                        prevDate = DateUtil.addDays(lastSaleDate, -(i + 1));
                        //попытка найти доставку за эту дату
                        tmpBeforeSaleList = 
                            mSalesDAOImpl.getFilteredSales(
                                -1
                                , barrel.getId()
                                , -1
                                , prevDate
                                , prevDate
                                , -1
                                , 1
                                , 0
                            );
                        if (tmpBeforeSaleList != null) {
                            
                            if (tmpBeforeSaleList.size() > 0) {
                                
                                tmpBeforeSale = tmpBeforeSaleList.get(0);
                            }
                        }
                        //если доставка найдена
                        if (tmpBeforeSale != null) {

                            //если ранее не была найдена первая доставка, на 7 или
                            //более дней предшествующая текущей дате
                            if (!firstFoundPrevSaleFlag) {

                                //добавляем в коллекцию найденную доставку
                                daysBeforeSaleList.add(tmpBeforeSale);
                                //если дата доставки - на 7 или больше дней ранее текущей
                                if (i >= 7) {
                                    //устанавливаем эту доставку как начальную в диапазоне
                                    xDaysBeforeSaleStart = tmpBeforeSale;
                                    //устанавливаем флаг: начальная доставка диапазона найдена
                                    firstFoundPrevSaleFlag = true;
                                }
                            }
                        }
                        i++;
                    }
                    //если диапазон доставок получен
                    if (firstFoundPrevSaleFlag) {
                        //перебираем все доставки в диапазоне
                        for (Sale sale : daysBeforeSaleList) {
                            //суммируем все расходы воды
                            waterConsumption += sale.getVolume();
                        }
                        //находим средний расход за диапазон
                        averageWaterConsumption = waterConsumption / i;
                        if (averageWaterConsumption == 0) {
                            averageWaterConsumption = 1;
                        }
                        //счетчик - периодичность
                        int j_period = 0;
                        //повторяем наращивание длительности периода до новой доставки,
                        //пока не достигнем остаточного объема воды, меньше минимально
                        //допустимого для данной бочки
                        while (true) {                        
                            if ((capacity - averageWaterConsumption * (j_period + 1)) > allowedRest) {
                                j_period++;
                            } else {
                                break;
                            }
                        }
                        //сохраняем новое значение и текущую дату в объекте бочки
                        if (j_period < 1) {
                            j_period = 1;
                        }
                        barrel.setPeriod(j_period);
                        barrel.setPeriodCalcDate(currentDate);
                        //сохраняем полученный необходимый период в БД
                        mBarrelsDAOImpl.updateBarrel(barrel);
                    }
                }*/
                //!!!old func end!!!
                
                /*new func begin*/
                //Если текущий день недели - ПН (!this requirement was removed!),
                //и в качестве периодичности не задано 100000
                //(дополнительно проверяем,
                //не был ли осуществлен данный подсчет в данном сеансе работы с приложением)
                //то пересчитываем переодичность
                //System.out.println("currentDayOfWeekNum "+currentDayOfWeekNum);
                //if (currentDayOfWeekNum == 2 && barrel.getPeriod() != 100000) {
                /*if (barrel.getPeriod() != 100000) {
                    
                    int newPeriod = 1;
                    //Старотовый диапазон дат -
                    //от предыдущего ПН включительно
                    //до ВС включительно
                    Date prevDate = DateUtil.addDays(currentDate, -1);
                    Date prevDate7 = DateUtil.addDays(currentDate, -8);
                    //Переменная для списка доставок за неделю
                    List<Sale> tmpBeforeSaleList = null;
                    //Доставленный объем / число дней с доставками
                    int volumeDivDays = 0;
                    //попытка найти доставки за прошлую неделю
                    tmpBeforeSaleList = 
                        mSalesDAOImpl.getFilteredSales(
                            -1
                            , barrel.getId()
                            , -1
                            , prevDate7
                            , prevDate
                            , -1
                            , -1
                            , -1
                        );
                    //если на неделе была хотя бы 2 (две) доставки
                    if (tmpBeforeSaleList != null && tmpBeforeSaleList.size() > 1) {

                        volumeDivDays = calcVolumeDivDays(tmpBeforeSaleList, 7);
                    } //если нет, то ищем доставки за прошедший месяц
                    else {
                    
                        Date prevDate14 = DateUtil.addDays(currentDate, -15);
                        tmpBeforeSaleList = 
                            mSalesDAOImpl.getFilteredSales(
                                -1
                                , barrel.getId()
                                , -1
                                , prevDate14
                                , prevDate
                                , -1
                                , -1
                                , -1
                            );
                        if (tmpBeforeSaleList != null && tmpBeforeSaleList.size() > 1) {

                            volumeDivDays = calcVolumeDivDays(tmpBeforeSaleList, 14);
                        }//если нет, то ищем за прошедшие три месяца
                        else {

                            Date prevDate28 = DateUtil.addDays(currentDate, -29);
                            tmpBeforeSaleList = 
                                mSalesDAOImpl.getFilteredSales(
                                    -1
                                    , barrel.getId()
                                    , -1
                                    , prevDate28
                                    , prevDate
                                    , -1
                                    , -1
                                    , -1
                                );
                            if (tmpBeforeSaleList != null && tmpBeforeSaleList.size() > 1) {

                                volumeDivDays = calcVolumeDivDays(tmpBeforeSaleList, 28);
                            }
                            else {
                            
                                Date prevDate56 = DateUtil.addDays(currentDate, -57);
                                tmpBeforeSaleList = 
                                    mSalesDAOImpl.getFilteredSales(
                                        -1
                                        , barrel.getId()
                                        , -1
                                        , prevDate56
                                        , prevDate
                                        , -1
                                        , -1
                                        , -1
                                    );
                                if (tmpBeforeSaleList != null && tmpBeforeSaleList.size() > 1) {

                                    volumeDivDays = calcVolumeDivDays(tmpBeforeSaleList, 56);
                                }
                                else {

                                    Date prevDate84 = DateUtil.addDays(currentDate, -85);
                                    tmpBeforeSaleList = 
                                        mSalesDAOImpl.getFilteredSales(
                                            -1
                                            , barrel.getId()
                                            , -1
                                            , prevDate84
                                            , prevDate
                                            , -1
                                            , -1
                                            , -1
                                        );
                                    if (tmpBeforeSaleList != null && tmpBeforeSaleList.size() > 1) {

                                        volumeDivDays = calcVolumeDivDays(tmpBeforeSaleList, 84);
                                    }
                                    //иначе назначаем период запрвки 1 раз в 100000 дней,
                                    //чтобы бочка в задании не появлялась,
                                    //и не пересчитывалась периодичность
                                    else{
                                        //Если хотя бы одна доставка для данной бочки
                                        //когда-либо была,
                                        //но за последнее время ни одной доставки не было
                                        //(последние 3 месяца),
                                        //то маркируем бочку, как не требующую доставок
                                        if (lastSale == null) {

                                            //...
                                            //newPeriod = 100000;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //Если новое соотношение найдено
                    if (volumeDivDays != 0) {
                        //Вычисляем новую периодичнорсть
                        //System.out.println("period1 "+(capacity - allowedRest)/((double)volumeDivDays));
                        newPeriod =
                            (int)Math.floor(
                                (capacity - allowedRest)/((double)volumeDivDays)
                            );
                        //System.out.println("period2 "+newPeriod);
                        if (newPeriod == 0) {
                        
                            newPeriod = 1;
                        }
                        barrel.setPeriod(newPeriod);
                        barrel.setPeriodCalcDate(currentDate);
                        //сохраняем полученный необходимый период в БД
                        mBarrelsDAOImpl.updateBarrel(barrel);
                    }//иначе если установлен маркер "не требует доставок" -
                    //сохраняем период = 1 раз в 100000 дней
                    else if (newPeriod == 100000) {
                        //the branch is deactivated now!!!
                        barrel.setPeriod(newPeriod);
                        barrel.setPeriodCalcDate(currentDate);
                        //сохраняем полученный необходимый период в БД
                        mBarrelsDAOImpl.updateBarrel(barrel);
                    }
                    
                }*/
                
                /*new func end*/

                //Объект состояния необходимости заправки
                BarrelWarning barrelWarning = new BarrelWarning();
                barrelWarning.mBarrelId = barrel.getId();
                
                //маркер "бочка удалена из текущей коллекции"
                boolean barrelRemoved = false;

                //
                boolean needRemove1 = false;
                //
                boolean needRemove2 = false;
                
                //если доставок для данной бочки не было - обратить внимание
                if (lastSale == null) {

                    //...
                    barrelWarning.mNeedLevel = 3;
                } else {

                    //иначе уточняем, когда была последняя заправка,
                    //и через сколько дней нужно заправлять
                    long diffSaleMilliseconds = currentDate.getTime() - lastSaleDate.getTime();

                    //давность последней заправки
                    int diffSaleDays =
                        (int)TimeUnit.DAYS.convert(
                                diffSaleMilliseconds
                                , TimeUnit.MILLISECONDS
                        );

                    //как раз пора заправлять
                    if (diffSaleDays == barrel.getPeriod()) {

                        barrelWarning.mNeedLevel = 1;
                    } else if(diffSaleDays > barrel.getPeriod()){

                        //просрочено на день или больше - срочно заправить!
                        barrelWarning.mNeedLevel = 2;
                    } else {

                        //Запрвлять рано
                        barrelWarning.mNeedLevel = 0;
                        //Если по условию фильтра нужно выдать на отображение
                        //только те бочки, которые пора заправлять -
                        //удаляем из списка данную бочку
                        if (mNeedCharge && mFilter) {

                            //barrelsList.remove(barrel.getId().intValue());
                            barrelIterator.remove();
                            barrelRemoved = true;
                        } else
                        //Удаляем бочку из выборки, если нужны только бочки для задания на день
                        if (mNeedCleanOrCharge) {

                            //barrelsList.remove(barrel.getId().intValue());
                            //barrelIterator.remove();
                            //barrelRemoved = true;
                            needRemove1 = true;
                        }
                    }
                }
                //Если по условию фильтра нужно выдать на отображение
                //только те бочки, которые пора чистить -
                //удаляем из списка данную бочку
                //(также предварительно проверяем, не была ли ранее удалена бочка)
                if (mNeedClean && mFilter && !barrelRemoved) {

                    Date lastCleanDate = barrel.getLastCDate();
                    
                    if (lastCleanDate != null) {
                        
                        if (
                                (
                                    currentDate.getTime()
                                    - lastCleanDate.getTime()
                                ) < Settings.getCleaningTypicalCycleTime()
                            ) {
                            
                            //barrelsList.remove(barrel.getId().intValue());
                            barrelIterator.remove();
                        }
                    }
                    //System.out.println();
                } else if(mNeedCleanOrCharge){
                
                    Date lastCleanDate = barrel.getLastCDate();
                    
                    if (lastCleanDate != null) {
                        
                        if (
                                ((
                                    currentDate.getTime()
                                    - lastCleanDate.getTime()
                                ) < Settings.getCleaningTypicalCycleTime())
                            ) {
                            
                            //barrelsList.remove(barrel.getId().intValue());
                            //barrelIterator.remove();
                            needRemove2 = true;
                        }
                    }
                    
                    
                }
                
                //Удаляем лишние бочки из коллекции для задания
                if (needRemove1 && needRemove2 && !barrelRemoved) {
                    
                    barrelIterator.remove();
                    barrelRemoved = true;
                }
                //
                Globals.mBarrelWarningList.add(barrelWarning);
            }
            //запрещаем пересчеты в текущем сеансе работы с приложением,
            //т.к. нет смысла делать это несколько раз в один день
            Globals.mAllowCalcPeriod = false;
        }
        return barrelsList;
    }
    
    //
    /*public void needCleanFilter(List<Barrel> _barrelsList){
    }*/
    private int calcVolumeDivDays(List _tmpBeforeSaleList, int _daysRange){
    
        //int daysCount = _tmpBeforeSaleList.size();
        
        List<Sale> tmpBeforeSaleList = _tmpBeforeSaleList;
        int volumeDivDays = 0;

        int volumeSum = 0;
        System.out.println("daysCount " + _daysRange);
        for (Sale sale : tmpBeforeSaleList) {

            System.out.println("sale.getVolume() " + sale.getVolume());
            volumeSum += sale.getVolume();
        }
        System.out.println("volumeSum " + volumeSum);
        //
        volumeDivDays = volumeSum / _daysRange;
        System.out.println("volumeDivDays " + volumeDivDays);
        return volumeDivDays;
    }
    
    private int calcDiffSaleDays(List<Sale> _tmpBeforeSaleList){
    
        Sale nowSale = _tmpBeforeSaleList.get(0);
        Sale startSale =
            _tmpBeforeSaleList.get(_tmpBeforeSaleList.size() - 1);
        
        System.out.println("nowSale.getCreatedAt() " + nowSale.getCreatedAt());
        System.out.println("startSale.getCreatedAt() " + startSale.getCreatedAt());

        long diffSaleMilliseconds =
            nowSale.getCreatedAt().getTime()
                - startSale.getCreatedAt().getTime();

        //ms -> days
        int diffSaleDays =
            (int)TimeUnit.DAYS.convert(
                    diffSaleMilliseconds
                    , TimeUnit.MILLISECONDS
            );
        
        System.out.println("diffSaleDays " + diffSaleDays);
        
        return diffSaleDays + 1;
    }
    
    public int calcPeriod(Barrel _barrel, Date _lastDate){
        
        System.out.println("_barrel.getPeriodCalcDate().getTime() " + _barrel.getPeriodCalcDate().getTime());
        
        Date zeroDate = new Date();
        try {
            zeroDate = new SimpleDateFormat("dd.MM.yyyy").parse("12.12.2000");
        } catch (ParseException ex) {
            Logger.getLogger(BarrelsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (_barrel.getPeriodCalcDate().compareTo(zeroDate) == 0) {
            
            return _barrel.getPeriod();
        }
        
        int newPeriod = 1;
    
        //Если в качестве периодичности не задано 100000,
        //то пересчитываем переодичность
        if (_barrel.getPeriod() != 100000) {
            
            //Date currentDate = Date.from(Instant.now());
            
            int capacity =
                mBarrelCapacitiesDAOImpl
                    .getBarrelCapacity(_barrel.getCapacityId())
                    .getCapacity();
            //
            int allowedRest = _barrel.getAllowedRest();
                    
            //Старотовый диапазон дат -
            //от текущего дня включительно
            //до дня на 7 дней ранее включительно
            //Date prevDate = DateUtil.addDays(currentDate, 0);
            Date prevDate7 = DateUtil.addDays(_lastDate, -6);
            //Переменная для списка доставок за неделю
            List<Sale> tmpBeforeSaleList = null;
            //Доставленный объем / число дней с доставками
            int volumeDivDays = 0;
            //попытка найти доставки за прошлую неделю
            tmpBeforeSaleList = 
                mSalesDAOImpl.getFilteredSales(
                    -1
                    , _barrel.getId()
                    , -1
                    , prevDate7
                    , _lastDate
                    , -1
                    , -1
                    , -1
                );
            //если за 7 дней были хотя бы 2 (две) доставки
            if (tmpBeforeSaleList != null && tmpBeforeSaleList.size() > 1) {
                
                int diffSaleDays = calcDiffSaleDays(tmpBeforeSaleList);

                volumeDivDays = calcVolumeDivDays(tmpBeforeSaleList, diffSaleDays);
            } //если нет, то ищем доставки за прошедший месяц
            else {

                Date prevDate14 = DateUtil.addDays(_lastDate, -13);
                tmpBeforeSaleList = 
                    mSalesDAOImpl.getFilteredSales(
                        -1
                        , _barrel.getId()
                        , -1
                        , prevDate14
                        , _lastDate
                        , -1
                        , -1
                        , -1
                    );
                if (tmpBeforeSaleList != null && tmpBeforeSaleList.size() > 1) {
                    
                    int diffSaleDays = calcDiffSaleDays(tmpBeforeSaleList);

                    volumeDivDays = calcVolumeDivDays(tmpBeforeSaleList, diffSaleDays);
                }//если нет, то ищем за прошедшие три месяца
                else {

                    Date prevDate28 = DateUtil.addDays(_lastDate, -27);
                    tmpBeforeSaleList = 
                        mSalesDAOImpl.getFilteredSales(
                            -1
                            , _barrel.getId()
                            , -1
                            , prevDate28
                            , _lastDate
                            , -1
                            , -1
                            , -1
                        );
                    if (tmpBeforeSaleList != null && tmpBeforeSaleList.size() > 1) {
                        
                        int diffSaleDays = calcDiffSaleDays(tmpBeforeSaleList);

                        volumeDivDays = calcVolumeDivDays(tmpBeforeSaleList, diffSaleDays);
                    }
                    else {

                        Date prevDate56 = DateUtil.addDays(_lastDate, -55);
                        tmpBeforeSaleList = 
                            mSalesDAOImpl.getFilteredSales(
                                -1
                                , _barrel.getId()
                                , -1
                                , prevDate56
                                , _lastDate
                                , -1
                                , -1
                                , -1
                            );
                        if (tmpBeforeSaleList != null && tmpBeforeSaleList.size() > 1) {
                            
                            int diffSaleDays = calcDiffSaleDays(tmpBeforeSaleList);

                            volumeDivDays = calcVolumeDivDays(tmpBeforeSaleList, diffSaleDays);
                        }
                        else {

                            Date prevDate84 = DateUtil.addDays(_lastDate, -83);
                            tmpBeforeSaleList = 
                                mSalesDAOImpl.getFilteredSales(
                                    -1
                                    , _barrel.getId()
                                    , -1
                                    , prevDate84
                                    , _lastDate
                                    , -1
                                    , -1
                                    , -1
                                );
                            if (tmpBeforeSaleList != null && tmpBeforeSaleList.size() > 1) {
                                
                                int diffSaleDays = calcDiffSaleDays(tmpBeforeSaleList);

                                volumeDivDays = calcVolumeDivDays(tmpBeforeSaleList, diffSaleDays);
                            }
                            //иначе назначаем период запрвки 1 раз в 100000 дней,
                            //чтобы бочка в задании не появлялась,
                            //и не пересчитывалась периодичность
                            else{
                                //Если хотя бы одна доставка для данной бочки
                                //когда-либо была,
                                //но за последнее время ни одной доставки не было
                                //(последние 3 месяца),
                                //то маркируем бочку, как не требующую доставок
                                //if (lastSale == null) {

                                    //...
                                    //newPeriod = 100000;
                                //}
                            }
                        }
                    }
                }
            }
            //Если новое соотношение найдено
            if (volumeDivDays != 0) {
                //Вычисляем новую периодичнорсть
                System.out.println("period1 "+(capacity - allowedRest)/((double)volumeDivDays));
                newPeriod =
                    (int)Math.floor(
                        (capacity - allowedRest)/((double)volumeDivDays)
                    );
                System.out.println("period2 "+newPeriod);
                if (newPeriod == 0) {

                    newPeriod = 1;
                }
                _barrel.setPeriod(newPeriod);
                _barrel.setPeriodCalcDate(_lastDate);
                //сохраняем полученный необходимый период в БД
                mBarrelsDAOImpl.updateBarrel(_barrel);
            }//иначе если установлен маркер "не требует доставок" -
            //сохраняем период = 1 раз в 100000 дней
            else if (newPeriod == 100000) {
                //the branch is deactivated now!!!
                _barrel.setPeriod(newPeriod);
                _barrel.setPeriodCalcDate(_lastDate);
                //сохраняем полученный необходимый период в БД
                mBarrelsDAOImpl.updateBarrel(_barrel);
            }
        } else {
        
            newPeriod = 100000;
        }
        
        return newPeriod;
    }
}
