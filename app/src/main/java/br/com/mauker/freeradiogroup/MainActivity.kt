package br.com.mauker.freeradiogroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import br.com.mauker.lib.freeradiogroup.FreeRadioGroup
import br.com.mauker.lib.freeradiogroup.OnCheckedChangeListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val group: FreeRadioGroup = findViewById(R.id.radioGroup)

        findViewById<Button>(R.id.btClear).setOnClickListener {
            group.clearCheck()
        }

        group.setOnCheckedChangeListener(object: OnCheckedChangeListener {
            override fun onCheckedChanged(group: FreeRadioGroup, checkedId: Int) {
                Toast.makeText(
                    this@MainActivity,
                    "Item $checkedId selected.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}