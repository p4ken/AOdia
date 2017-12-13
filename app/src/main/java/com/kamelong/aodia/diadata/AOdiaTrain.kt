package com.kamelong.aodia.diadata

/**
 * AOdiaで使用するTrainのInterface
 */
interface AOdiaTrain {
    val diaFile:AOdiaDiaFile

    var name:String
    var number:String
    var count:String
    var operation:String
    var remark:String

    var direction:Int

    var startAction:Int
    var startExchangeStop:Int
    var startExchangeTimeStart:Int
    var startExchangeTimeEnd:Int
    var endAction:Int
    var endExchangeStop:Int
    var endExchangeTimeStart:Int
    var endExchangeTimeEnd:Int

    fun getStationTime(station:Int):Long
    fun setStationTime(station:Int,value:Long)

    fun getDepartureTime(station:Int):Int
    fun getArrivalTime(station:Int):Int
    fun getDepartureTime(station:Int, startTime: Int):Int
    fun getArrivalTime(station:Int,startTime:Int):Int
    fun setDepartureTime(station:Int,value:Int)
    fun setArrivalTime(station:Int,value:Int)


    fun getStopNumber(station:Int):Int
    fun setStopNumber(station:Int,value:Int)

    fun getStopType(station:Int):Int
    fun setStopType(station:Int,value:Int)



    val trainType:AOdiaTrainType

    val startStation:Int
    val endStation:Int





}