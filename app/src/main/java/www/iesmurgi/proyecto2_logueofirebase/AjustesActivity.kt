package www.iesmurgi.proyecto2_logueofirebase

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import www.iesmurgi.proyecto2_logueofirebase.databinding.AjustesBinding

class AjustesActivity:AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var binding: AjustesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AjustesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        var btnRestablecerClave = binding.btRestablecerContra
        var btnBorrarUsuario = binding.btBorrarUsu
        var btnActualizarUsuario = binding.btActualizarNombre
        var btnActualizarNacio = binding.btActualizarNacion
        var btnActualizarEdad = binding.btActualizarEdad

        //Restablecer clave
        btnRestablecerClave.setOnClickListener{
            restablecerClave()
        }
        //BorrarUsuario
        btnBorrarUsuario.setOnClickListener{
            borrarUsuario()
        }
        //Actualizar Usuario
        btnActualizarUsuario.setOnClickListener {
            actualizarUsuario()
        }

        //Actualizar Nacionalidad
        btnActualizarNacio.setOnClickListener {
            actualizarNacionalidad()
        }

        //Actualizar Edad
        btnActualizarEdad.setOnClickListener {
            actualizarEdad()
        }

    }

    //Actualizar Edad
    fun actualizarEdad() {
        var txtCambio: String = ""
        val builder = AlertDialog.Builder(this)

        builder.setTitle(this.resources.getString(R.string.actualizaredad))

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            //Asigna al texto ingresado en el EditText a la variable "textoIngresado"
            txtCambio = input.text.toString()
            val documentReference =
                Firebase.firestore.collection("user").document(user.email.toString())
            documentReference.update("edad", txtCambio)

            Toast.makeText(this, this.resources.getString(R.string.actualizarEdad),
                Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, PerfilActivity::class.java))
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    //Actualizar Nacionalidad
    fun actualizarNacionalidad() {
        var txtCambio: String = ""
        val builder = AlertDialog.Builder(this)

        builder.setTitle(this.resources.getString(R.string.actualizarnac))

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            //Asigna al texto ingresado en el EditText a la variable "textoIngresado"
            txtCambio = input.text.toString()
            val documentReference =
                Firebase.firestore.collection("user").document(user.email.toString())
            documentReference.update("nacionalidad", txtCambio)

            Toast.makeText(this, this.resources.getString(R.string.actualizarNacio),
                Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, PerfilActivity::class.java))
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    //Actualizar usuario
    fun actualizarUsuario() {
        var txtCambio: String = ""
        val builder = AlertDialog.Builder(this)

        builder.setTitle(this.resources.getString(R.string.actualizarnom))

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            //Asigna al texto ingresado en el EditText a la variable "textoIngresado"
            txtCambio = input.text.toString()
            val documentReference =
                Firebase.firestore.collection("user").document(user.email.toString())
            documentReference.update("usuario", txtCambio)

            Toast.makeText(this, this.resources.getString(R.string.msg_actualizarUser),
                Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, PerfilActivity::class.java))
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    //Metodo para restablecer clave
    fun restablecerClave() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(this.resources.getString(R.string.restablecer))
        builder.setMessage(this.resources.getString(R.string.restablecerContra))
        builder.setPositiveButton("Ok") { dialog, which ->

            auth.sendPasswordResetEmail(user.email.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        this,
                        this.resources.getString(R.string.msg_restablecerclave),
                        Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, it.exception.toString(),
                        Toast.LENGTH_SHORT).show()
                }
            }

        }
        builder.setNegativeButton("Cancel") { dialog, which ->

        }


        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.verde))

            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.rojo))
        }
        dialog.show()
    }

    //Metodo borrar usuario
    fun borrarUsuario() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(this.resources.getString(R.string.borrarusu))
        builder.setMessage(this.resources.getString(R.string.borrarUsuarioTexto))
        builder.setPositiveButton("Ok") { dialog, which ->

            user.delete().addOnCompleteListener {
                val dialog = builder.create()
                if (it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this,
                        this.resources.getString(R.string.msg_borrarusu), Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(this, it.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
        }

        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.verde))

            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.rojo))
        }
        dialog.show()
    }


}