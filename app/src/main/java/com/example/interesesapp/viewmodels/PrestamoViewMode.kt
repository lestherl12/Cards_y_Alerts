package com.example.interesesapp.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.interesesapp.models.PrestamoState
import com.example.interesesapp.views.calcularCuota
import java.math.BigDecimal
import java.math.RoundingMode

class PrestamoViewModel : ViewModel(){
    var state by mutableStateOf(PrestamoState())
        private set

    fun confirmDialog(){
        state = state.copy(showAlert = false)
    }

    fun limpiar(){
        state = state.copy(
            montoPrestamo = "",
            cantCuotas = "",
            tasa = "",
            montoInteres = 0.0,
            montoCuota = 0.0
        )
    }

    fun onValue( value:String, campo:String){
        Log.i("jorge", campo)
        Log.i("jorge", value)

        when(campo){
            "montoPrestamo" -> state = state.copy(montoPrestamo = value)
            "cuotas" -> state = state.copy(cantCuotas = value)
            "tasa" -> state = state.copy(tasa = value)

        }
    }

    private fun calcularTotal(montoPrestamo: Double, cantCuotas: Int, tasaInteresAnual: Double):Double{
        val res = cantCuotas * calcularCuota(montoPrestamo, cantCuotas, tasaInteresAnual)
        return BigDecimal(res).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    private fun CalcularCuota(montoPrestamo: Double, cantCuotas: Int, tasaInteresAnual: Double): Double{
        val tasaInteresMensual = tasaInteresAnual / 12 / 100

        val cuota = montoPrestamo * tasaInteresMensual * Math.pow(
            1 + tasaInteresMensual,
            cantCuotas.toDouble()
        ) / (Math.pow(1 + tasaInteresMensual, cantCuotas.toDouble())-1)

        val cuotaRedondeada = BigDecimal(cuota).setScale(2, RoundingMode.HALF_UP).toDouble()
        return cuotaRedondeada
    }

    fun calcular(){
        val montoPrestamo = state.montoPrestamo
        val cantCuotas = state.cantCuotas
        val tasa = state.tasa

        if(montoPrestamo!="" && cantCuotas!="" && tasa!=""){
            state = state.copy(
                montoCuota = calcularCuota(montoPrestamo.toDouble(), cantCuotas.toInt(), tasa.toDouble()),
                montoInteres = calcularTotal(montoPrestamo.toDouble(), cantCuotas.toInt(), tasa.toDouble())
            )
        }else{
            state = state.copy(showAlert = true)
        }
    }
}