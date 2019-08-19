package com.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.db.DatabaseHandler
import com.example.veriparkassignment.R
import com.helper.HelperMethods
import kotlinx.android.synthetic.main.activity_main.btn_calculate
import kotlinx.android.synthetic.main.activity_slabs.*

class SlabsActivity : AppCompatActivity(){

val db = DatabaseHandler(this@SlabsActivity)
    var helperMethods = HelperMethods.getInstance()
    var firstSlab  = 0
    var secondSlab = 0
    var thirdSlab  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slabs)
        initViews()
    }

    private fun initViews() {
        btn_calculate.setOnClickListener(submitButtonListener())
    }

    /**
     * <p>This is click listener of submit button for saving slabs in DB</p>
     */

    private inner class submitButtonListener : View.OnClickListener {
        override fun onClick(view: View) {

             if (edt_first_slab.text.toString().isEmpty() || edt_second_slab.text.toString().isEmpty() || edt_third_slab.toString().isEmpty()) {
                helperMethods.messagePopUp(getString(R.string.please_input_all_values),"error",this@SlabsActivity)
                return
            }

            firstSlab  = edt_first_slab.text.toString().toInt()
            secondSlab = edt_second_slab.text.toString().toInt()
            thirdSlab  = edt_third_slab.text.toString().toInt()

            if (secondSlab > firstSlab && secondSlab < thirdSlab) {

                helperMethods.saveSlabsInDataBase(this@SlabsActivity , edt_first_slab.text.toString().toInt(), edt_second_slab.text.toString().toInt(), edt_third_slab.text.toString().toInt())
                val intent = MainActivity.newIntent(this@SlabsActivity)
                finish()
                startActivity(intent)

            }else{
                helperMethods.messagePopUp(getString(R.string.slabs_input_values_error_message),"error",this@SlabsActivity)
                return
            }

        }
    }

    override fun onStop() {
        super.onStop()
        db.close()
    }
}
