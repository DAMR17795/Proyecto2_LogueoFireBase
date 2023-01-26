package www.iesmurgi.proyecto2_logueofirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
            setup(user?.email.toString())
        }
    }

    fun setup(email: String) {
        //var correo = binding.
    }
}