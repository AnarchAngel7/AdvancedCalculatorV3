package com.example.calculator


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    lateinit var outputTextView: TextView
    private var lastNumeric: Boolean = false
    private var stateError: Boolean = false
    private var lastDot: Boolean = false
    private lateinit var btn: Button
    private var myhistory: MutableList<String> = mutableListOf()
    var h = 0
    var newH = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        outputTextView = findViewById(R.id.txtInput)
        btn = findViewById(R.id.btnEqual)
    }


    fun onDigit(view: View) {
        if (stateError) {
            outputTextView.text = (view as Button).text
            stateError = false
        } else {
            outputTextView.append((view as Button).text)
        }
        lastNumeric = true
    }

    fun onDecimalPoint(view: View) {
        if (lastNumeric && !stateError && !lastDot) {
            outputTextView.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View) {
        if (lastNumeric && !stateError) {
            outputTextView.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }


    fun onClear(view: View) {
        this.outputTextView.text =
            outputTextView.text.substring(0, outputTextView.length() - outputTextView.length())
        lastNumeric = false
        stateError = false
        lastDot = false
    }

    fun onEqual(view: View) {
        if (lastNumeric && !stateError) {
            val history = History()
            val text = outputTextView.text.toString()
            val expression = ExpressionBuilder(text).build()
            history.equation = text
            Thread {
                HistoryDatabase.getInstance(applicationContext)
                    .historyDatabaseDao
                    .insert(history)
            }.start()
            try {
                val result = expression.evaluate()
                outputTextView.text = result.toString()
                lastDot = true
            } catch (ex: Exception) {
                outputTextView.text = getString(R.string.error)
                stateError = true
                lastNumeric = false
            }
        }
    }


    fun onHist(view: View) {
        Thread {
            val hist = HistoryDatabase.getInstance(applicationContext)
                .historyDatabaseDao.getLastHistory().equation
            outputTextView.text = hist
            lastNumeric = true
        }.start()
    }

    fun onHistList(view: View) {
        Thread {
            val hist: List<History> = HistoryDatabase.getInstance(applicationContext)
                .historyDatabaseDao
                .getHistory()
            try {
                for (i in hist.indices) {
                    myhistory.add(hist[i].equation)
                }
                val getVal: Int = hist.size - h
                if (getVal < 0) {
                    h = 1
                    outputTextView.text = myhistory[hist.size - h]
                } else {
                    outputTextView.text = myhistory[getVal]
                    lastNumeric = true
                }
            } catch (E: Exception) {
                outputTextView.text = E.toString()
                Log.d("Exception", E.toString())
            }
            newH = hist.size
        }.start()
        h += 1
    }

    fun onDel(view: View) {
        if (this.outputTextView.text != "") {
            try {
                this.outputTextView.text =
                    outputTextView.text.substring(0, outputTextView.length() - 1)
            } catch (e: IndexOutOfBoundsException) {
                this.outputTextView.text = ""
            }
        } else {
            this.outputTextView.text = ""
        }
    }

}
