package com.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.config.BillCalculator
import com.example.veriparkassignment.R
import com.helper.HelperMethods
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var helperMethods = HelperMethods.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        helperMethods.getSlabsData(this@MainActivity)
    }

    private fun initViews() {
        btn_calculate.setOnClickListener(CalculateBtnListener())
        btn_history.setOnClickListener(HistoryBtnListener())
        btn_settings.setOnClickListener(SettingsBtnListener())
    }

    /**
     * <p>This is click listener of calculate button</p>
     */

    private inner class CalculateBtnListener : View.OnClickListener {
        override fun onClick(view: View) {

            if (edt_units.text.toString().isEmpty() || edt_service_num.text.toString().isEmpty()) {
                helperMethods.messagePopUp(getString(R.string.please_extract_valid_input),"error",this@MainActivity)
                return
            }

            var units = Integer.parseInt(edt_units.text.toString())
            val serviceNumber = edt_service_num.text.toString()

            val billAmount = BillCalculator.calculateBill(units, this@MainActivity)
            val unitDivision = BillCalculator.getUnitDivision(units, this@MainActivity)

            txtCalculatedAmount.text = "Total Cost: Rs. "+java.lang.Double.toString(billAmount)
            txtBillDivision.text = unitDivision
            helperMethods.saveConsumerDataInDataBase(this@MainActivity, serviceNumber, units.toString(), billAmount.toString())
        }
    }

    /**
     * <p>This is click listener of history button</p>
     */
    private inner class HistoryBtnListener : View.OnClickListener {
        override fun onClick(view: View) {

            val serviceNumber = edt_service_num.text.toString()

            if (serviceNumber.isEmpty()) {
                helperMethods.messagePopUp(getString(R.string.please_input_service_number),"error",this@MainActivity)
                return
            }

            val intent = HistoryActivity.newIntent(this@MainActivity, serviceNumber)
            startActivity(intent)
        }
    }
    
    /**
     * <p>This is click listener of Settings button</p>
     */
    private inner class SettingsBtnListener : View.OnClickListener {
        override fun onClick(view: View) {

            val intent = SlabsActivity.newIntent(this@MainActivity)
            finish()
            startActivity(intent)
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            return intent
        }
    }
}
