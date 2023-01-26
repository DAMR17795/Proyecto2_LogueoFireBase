package www.iesmurgi.proyecto2_logueofirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import www.iesmurgi.proyecto2_logueofirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val btnIniciar = binding.btIniciar
        val btnRegistro = binding.btRegistro
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, PerfilActivity::class.java))
            finish()
        }
    }

    fun crearUsuario(email: String, clave: String) {

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, clave)
            .addOnCompleteListener{

                if (it.isSuccessful) {
                    //abrirPerfil()
                } else {
                    //showErrorAlert()
                }
        }
    }

    fun iniciarSesion(email: String, clave: String) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, clave)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    //abrirPerfil()
                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }

        }
    }


}