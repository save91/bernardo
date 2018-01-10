package net.extrategy.bernardo.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_settings.*
import net.extrategy.bernardo.R
import net.extrategy.bernardo.extensions.DelegatesExt

/**
 * Created by saverio on 08/01/18.
 */
class SettingsActivity : AppCompatActivity() {

    companion object {
        val IP_ADDRESS = "ipAddress"
        val DEFAULT_IP_ADDRESS = "127.0.0.1"
        val PORT = "port"
        val DEFAULT_PORT= "80"
        val PARAMS_ID = "params_id"
        val DEFAULT_PARAMS_ID = ""
        val PARAMS_CS = "params_cs"
        val DEFAULT_PARAMS_CS = ""
        val PATH = "path"
        val DEFAULT_PATH= ""
    }

    private var ipAddress: String by DelegatesExt.preference(this, IP_ADDRESS, DEFAULT_IP_ADDRESS)
    private var port: String by DelegatesExt.preference(this, PORT, DEFAULT_PORT)
    private var params_id: String by DelegatesExt.preference(this, PARAMS_ID, DEFAULT_PARAMS_ID)
    private var params_cs: String by DelegatesExt.preference(this, PARAMS_CS, DEFAULT_PARAMS_CS)
    private var path: String by DelegatesExt.preference(this, PATH, DEFAULT_PATH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ipServer.setText(ipAddress)
        portServer.setText(port)
        pathServer.setText(path)
        paramsIdServer.setText(params_id)
        paramsCsServer.setText(params_id)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ipAddress = ipServer.text.toString()
        port = portServer.text.toString()
        path = pathServer.text.toString()
        params_id = paramsIdServer.text.toString()
        params_cs = paramsCsServer.text.toString()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> false
        }
    }

}
