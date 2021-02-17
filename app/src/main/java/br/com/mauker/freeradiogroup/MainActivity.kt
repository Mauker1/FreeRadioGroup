package br.com.mauker.freeradiogroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import br.com.mauker.lib.freeradiogroup.FreeRadioGroup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val group: FreeRadioGroup = findViewById(R.id.radioGroup)

        findViewById<Button>(R.id.btClear).setOnClickListener {
            group.clearCheck()
        }
    }
}