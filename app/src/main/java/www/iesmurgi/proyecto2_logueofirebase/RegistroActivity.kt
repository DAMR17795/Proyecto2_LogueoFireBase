package www.iesmurgi.proyecto2_logueofirebase

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import www.iesmurgi.proyecto2_logueofirebase.databinding.RegistroBinding


class RegistroActivity: AppCompatActivity() {

    private lateinit var binding: RegistroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var contra: EditText
    private lateinit var contraCon:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        contra = binding.tvContra
        contraCon = binding.tvConfirmContra

        binding.btRegistrar.setOnClickListener {
            if (comprobarClavesIguales()) {
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



}