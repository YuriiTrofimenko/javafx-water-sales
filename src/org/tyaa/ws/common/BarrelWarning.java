/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.common;

/**
 *
 * @author Юрий
 */

/* Объект для хранения пар "ИД бочки - потребность в заправке" */
public class BarrelWarning
{
    public int mBarrelId;
    //Если уровень потребности 0 - заправлять рано - черный
    //1 - как раз пора - желтый
    //2 - срочно! - красный
    //3 - обратить внимание (необычное состояние: есть бочка, но еще не было заправок) - синий
    public int mNeedLevel;
}
