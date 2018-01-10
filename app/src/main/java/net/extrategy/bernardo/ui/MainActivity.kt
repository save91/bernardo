package net.extrategy.bernardo.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import net.extrategy.bernardo.R
import net.extrategy.bernardo.extensions.DelegatesExt
import net.extrategy.bernardo.services.HTTPService
import net.extrategy.bernardo.services.VolleyService
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val service = VolleyService()
    private val httpService = HTTPService(service)
    private var ipAddress: String by DelegatesExt.preference(this, SettingsActivity.IP_ADDRESS, SettingsActivity.DEFAULT_IP_ADDRESS)
    private var port: String by DelegatesExt.preference(this, SettingsActivity.PORT, SettingsActivity.DEFAULT_PORT)
    private var path: String by DelegatesExt.preference(this, SettingsActivity.PATH, SettingsActivity.DEFAULT_PATH)
    private var params_id: String by DelegatesExt.preference(this, SettingsActivity.PARAMS_ID, SettingsActivity.DEFAULT_PARAMS_ID)
    private var params_cs: String by DelegatesExt.preference(this, SettingsActivity.PARAMS_CS, SettingsActivity.DEFAULT_PARAMS_CS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val dialog = indeterminateProgressDialog(message = R.string.open)
            val params = HashMap<String, String>()
            params.put("id", params_id)
            params.put("cs", params_cs)
            httpService.post("$ipAddress:$port/$path", params) { response ->
                dialog.dismiss()
                when(response) {
                    is JSONObject -> longSnackbar(view, R.string.success_open)
                    else -> longSnackbar(view, message = R.string.error)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> goToSettings()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToSettings(): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        return true
    }
}
