package www.iesmurgi.proyecto2_logueofirebase

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import www.iesmurgi.proyecto2_logueofirebase.databinding.ActivityMainBinding
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth
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
    private lateinit var tvUsu:EditText
    private lateinit var tvContra:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val btnIniciar = binding.btIniciar
        val btnRegistro = binding.btRegistro
        val btnGoogle = binding.btGoogle
        tvUsu = binding.tfUsuario
        tvContra = binding.tfContra

        //Iniciar sesion google
        btnGoogle.setOnClickListener {
            iniciarSesionGoogle()
        }
        //Iniciar sesion con contraseña
        btnIniciar.setOnClickListener {
            if (comprobarEmail() && comprobarContrasenia()) {
                iniciarSesion(binding.tfUsuario.getText().toString(), binding.tfContra.getText().toString())
            }
        }
        //Registro
        btnRegistro.setOnClickListener {
            abrirVentanaRegistro()
        }
    }
    //Abrir ventana registro
    fun abrirVentanaRegistro() {

        val enviar = Intent(this, RegistroActivity::class.java)

        startActivity(enviar)
    }


    //Metodo para iniciar sesion
    fun iniciarSesion(email: String, clave: String) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, clave)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    abrirPerfil()
                } else {
                    Toast.makeText(this, this.resources.getString(R.string.error), Toast.LENGTH_LONG).show()
                }

        }
    }

    fun abrirPerfil() {
        val enviar = Intent(this, PerfilActivity::class.java)
        startActivity(enviar)
    }

    companion object {
        private const val RC_SIGN_IN = 423
    }

    //Metodo para iniciar sesion con google
    fun iniciarSesionGoogle() {
        val providerGoogle = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providerGoogle).build(),
            Companion.RC_SIGN_IN)

    }
    //Escribimos usuario en la bbdd
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Companion.RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser

                val db = FirebaseFirestore.getInstance()

                val userRef = db.collection("user").document(user?.email.toString())

               userRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            //El documento existe y empezamos actividad

                            startActivity(Intent(this, PerfilActivity::class.java))
                            finish()

                        } else {
                            // El documento no existe
                            //Damos de alta al usuario
                            writeNewUser(user?.email.toString())
                            //Empezamos actividad
                            startActivity(Intent(this, PerfilActivity::class.java))
                            finish()
                        }
                    }
                    /*.addOnFailureListener { exception ->
                        // Error al obtener los datos del usuario
                    }*/
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, PerfilActivity::class.java))
            finish()
        }
    }

    fun comprobarEmail(): Boolean {
        if (tvUsu.text.toString().length == 0) {
            tvUsu.error = this.resources.getString(R.string.emailvacio)
            return false
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(tvUsu.text.toString()).matches()) {
                tvUsu.error =
                    this.resources.getString(R.string.falloemail) + "\n" + this.resources.getString(
                        R.string.ejemploemail
                    )
                return false

            } else {

                return true
            }
        }

    }

    fun comprobarContrasenia(): Boolean {
        if (tvContra.text.toString().length == 0) {
            tvContra.error = this.resources.getString(R.string.contravacia)
            return false

        } else {
            if (!PATRON.matcher(tvContra.text.toString()).matches()) {
                tvContra.error =
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