package com.example.calculaterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.text.isDigitsOnly
import com.example.calculaterapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    fun numberAction(view: View){
        if(view is Button){
            if(view.text == "."){
                if(canAddDecimal) {
                    binding.workingText.append(view.text)
                    canAddDecimal = false

                }
            }else {
                binding.workingText.append(view.text)
                canAddOperation = true
            }

        }
    }

    fun operatorAction(view: View){
        if(view is Button && canAddOperation) {
            binding.workingText.append(view.text)
            canAddOperation =false
            canAddDecimal = true
        }
    }
    fun allClearAction(view: View){
        binding.workingText.text = ""
        binding.resultText.text = ""
    }
    fun backSpaceAction(view: View){
        val length = binding.workingText.length()
        if(length>0){
            binding.workingText.text = binding.workingText.text.subSequence(0, length-1)
            canAddOperation = true
        }
    }
    fun equalAction(view: View){
        binding.resultText.text = calcResult().toString()
    }

    private fun calcResult(): String {

        val digitOperation = digitOperation()
        if(digitOperation.isEmpty()){
            return ""
        }

        val timesDivision = timeDivisionCalc(digitOperation)
        if(timesDivision.isEmpty()){
            return ""
        }

        val result = addSubtractClac(timesDivision)
        return  result.toString()
    }

    private fun addSubtractClac(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices){

            if(passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float

                if(operator == '+'){
                    result += nextDigit
                }
                if(operator == '-'){
                    result -= nextDigit
                }
            }


        }

        return result
    }

    private fun timeDivisionCalc(passedList: MutableList<Any>): MutableList<Any> {

        var list = passedList
        while (list.contains('x') || list.contains('/')){
            list = calcTimesDivison(list)
        }
        return list

    }

    private fun calcTimesDivison(passedList: MutableList<Any>): MutableList<Any> {
        var newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices){
            if(passedList[i] is Char && i <restartIndex && i != passedList.lastIndex ){
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float

                when(operator){

                    'x'->{
                        newList.add(prevDigit*nextDigit)
                    }
                    '/'->{
                        newList.add(prevDigit / nextDigit)
                    }
                    else ->{
                        newList.add(prevDigit)
                        newList.add(operator)

                    }


                }
            }
        }



        return newList
    }

    private fun digitOperation(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in binding.workingText.text) {
            if (character.isDigit() || character == '.'){
                currentDigit +=character


            }else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)

            }
        }
        if(currentDigit != ""){
            list.add(currentDigit.toFloat())
        }

        return  list
    }
}