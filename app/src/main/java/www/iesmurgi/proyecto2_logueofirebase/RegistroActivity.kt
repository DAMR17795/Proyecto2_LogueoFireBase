package www.iesmurgi.proyecto2_logueofirebase

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import www.iesmurgi.proyecto2_logueofirebase.databinding.RegistroBinding
import java.util.regex.Pattern


class RegistroActivity: AppCompatActivity() {

    private lateinit var binding: RegistroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var contra: EditText
    private lateinit var contraCon:EditText
    private lateinit var usu:EditText
    private val PATRON: Pattern = Pattern.compile(
        "^" +
                "(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=.*[a-zA-Z])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        contra = binding.tvContra
        contraCon = binding.tvConfirmContra
        usu = binding.tvEmail

        binding.btRegistrar.setOnClickListener {
            if (comprobarClavesIguales() && comprobarContrasenia() && comprobarContraseniaConfirm() && comprobarEmail()) {
                crearUsuario(binding.tvEmail.getText().toString(), binding.tvConfirmContra.getText().toString())
            }

        }
    }

    //Escribir usuario en la bbdd
    fun writeNewUser(email:String) {
        val db = Firebase.firestore

        val data = hashMapOf(
            "email" to email,
            "usuario" to "nouser",
            "nacionalidad" to "nonacionality",
            "edad" to "0"
        )

        db.collection("user").document(email)
            .set(data)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener{ e -> Log.w(ContentValues.TAG, "Error writing document", e)}
    }


    //Metodo para el boton de registrarse
    fun crearUsuario(email: String, clave: String) {

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, clave)
            .addOnCompleteListener{

                if (it.isSuccessful) {
                    writeNewUser(email)
                    abrirPerfil()
                } else {
                    showErrorAlert()
                }
            }
    }

    fun abrirPerfil() {
        val enviar = Intent(this, PerfilActivity::class.java)
        startActivity(enviar)
    }

    fun comprobarClavesIguales(): Boolean {
        if (contra.text.toString().equals(contraCon.text.toString())) {
            return true
        } else {
            contraCon.error = this.resources.getString(R.string.noigualcontra)
            return false
        }

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, PerfilActivity::class.java))
            finish()
        }
    }
    fun showErrorAlert() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un errror autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    fun comprobarEmail(): Boolean {
        if (usu.text.toString().length == 0) {
            usu.error = this.resources.getString(R.string.emailvacio)
            return false
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(usu.text.toString()).matches()) {
                usu.error =
                    this.resources.getString(R.string.falloemail) + "\n" + this.resources.getString(
                        R.string.ejemploemail
                    )
                return false

            } else {

                return true
            }
        }

    }

    fun comprobarContraseniaConfirm(): Boolean {
        if (contraCon.text.toString().length == 0) {
            contraCon.error = this.resources.getString(R.string.contravacia)
            return false

        } else {
            if (!PATRON.matcher(contra.text.toString()).matches()) {
                contraCon.error =
                    this.resources.getString(R.string.fallocontra) + "\n" + this.resources.getString(
                        R.string.ejemplocontra
                    )
                return false
            } else {
                return true
            }
        }
    }

    fun comprobarContrasenia(): Boolean {
        if (contra.text.toString().length == 0) {
            contra.error = this.resources.getString(R.string.contravacia)
            return false
        } else {
            if (!PATRON.matcher(contra.text.toString()).matches()) {
                contra.error =
                    this.resources.getString(R.string.fallocontra) + "\n" + this.resources.getString(
                        R.string.ejemplocontra
                    )
                return false
            } else {
                return true
            }
        }
    }

    //Options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    //Options item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Op2 -> {
                mostrarAbout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun mostrarAbout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.acerca).setIcon(R.drawable.acercadeicon).setMessage(getString(R.string.about) + "\n\nDaniel Alejandro Martín Romero - 2ºDAM")
        //Mostramos el dialogo
        val dialog = builder.create()
        dialog.show()
    }



}