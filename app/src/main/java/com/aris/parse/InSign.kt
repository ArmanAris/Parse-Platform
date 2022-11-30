package com.aris.parse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.aris.parse.databinding.ActivityInSignBinding
import com.aris.parse.ext.CheckNetwork
import com.parse.LogInCallback
import com.parse.ParseUser

class InSign : BaseActivity() {
    private lateinit var binding: ActivityInSignBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInSignBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_in_sign -> {
                if (!binding.textEmail.text.toString().trim().isEmpty()
                    && !binding.textPassword.text.toString().trim().isEmpty()
                ) {
                    if (Patterns.EMAIL_ADDRESS.matcher(binding.textEmail.text!!.trim()).matches()) {
                        if (CheckNetwork(this).checkNetwork) {

                            binding.animationViewSignIn.visibility = View.VISIBLE

                            val userName = binding.textEmail.text.toString().trim()
                            val password = binding.textPassword.text.toString().trim()

                            ParseUser.logInInBackground(userName,
                                password,
                                LogInCallback { user, error ->

                                    binding.animationViewSignIn.visibility = View.INVISIBLE

                                    if (user != null) {
                                        Toast.makeText(this, "خوش آمدید", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, "خطا", Toast.LENGTH_SHORT).show()
                                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                })

                        } else {
                            Toast.makeText(this, "شیکه خود را بررسی کنید", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this, "فرمت ایمیل نادرست است!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "لطفا تمام مقادیر را پر کنید", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.text_in_sign -> {
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
        }
    }

}