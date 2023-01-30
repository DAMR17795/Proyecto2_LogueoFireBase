package www.iesmurgi.proyecto2_logueofirebase

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import www.iesmurgi.proyecto2_logueofirebase.databinding.PerfilactivityBinding


class PerfilActivity : AppCompatActivity () {

    var auth:FirebaseAuth = FirebaseAuth.getInstance()
    var user:FirebaseUser? = auth.currentUser

    private lateinit var binding: PerfilactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PerfilactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (user != null) {
            setup()
        }

        var btAjustes = binding.btAjustes

        //Ir a pantalla de ajustes
        btAjustes.setOnClickListener {
            val enviar = Intent(this, AjustesActivity::class.java)
            startActivity(enviar) }
    }

    //Función para cargar los datos de la base de datos
    fun setup() {

        var bienvenida = binding.tvBienvenida
        var correo = binding.tvCorreo
        var nombre = binding.tvNombre
        var nacionalidad = binding.tvNacionalidad
        var edad = binding.tvEdad
        val db = Firebase.firestore
        db.collection("user").document(user?.email.toString()).get().addOnSuccessListener {
                documento ->

            val email: String? = documento.getString("email")
            val user: String? = documento.getString("usuario")
            val nacionality: String? = documento.getString("nacionalidad")
            val age: String? = documento.getString("edad")

            bienvenida.text = user
            correo.text = email
            nombre.text = user
            nacionalidad.text = nacionality
            edad.text = age

        }

        val btnCerrar = binding.btCerrar
        btnCerrar.setOnClickListener {

            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))

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