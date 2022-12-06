package com.aris.parse

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.aris.parse.databinding.ActivitySignUpBinding
import com.aris.parse.ext.CheckNetwork
import com.parse.ParseUser

const val UserName = "Name"

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val image = data!!.data
            Toast.makeText( this,"ok", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClick(view: View) {

        when (view.id) {
            R.id.profile_image -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                intent.flags = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                startActivityForResult(intent, 1)
            }

            R.id.btn_sign_up -> {
                if (binding.edtNameSignUp.text!!.trim().isEmpty()
                    || binding.edtEmailSignUp.text!!.trim().isEmpty()
                    || binding.edtPasswordSignUp.text!!.trim().isEmpty()
                ) {
                    Toast.makeText(this, "لطفا تمام مقادیر را پر کنید", Toast.LENGTH_SHORT).show()
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmailSignUp.text!!.trim())
                            .matches()
                    ) {
                        Toast.makeText(this, "فرمت ایمیل نادرست است!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        if (!CheckNetwork(this).checkNetwork) {
                            Toast.makeText(this, "شیکه خود را بررسی کنید", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            if (!binding.chbTerms.isChecked) {
                                Toast.makeText(this,
                                    "قوانین را تایید نکرده اید!!!",
                                    Toast.LENGTH_SHORT).show()
                            } else {
                                binding.animationView.visibility = View.VISIBLE

                                val user = ParseUser()
                                user.username = binding.edtEmailSignUp.text.toString().trim()
                                user.setPassword(binding.edtPasswordSignUp.text.toString().trim())
                                user.email = binding.edtEmailSignUp.text.toString().trim()
                                user.put(UserName, binding.edtNameSignUp.text.toString().trim())

                                user.signUpInBackground { error ->

                                    binding.animationView.visibility = View.INVISIBLE

                                    if (error == null) {
                                        Toast.makeText(this, "خوش آمدید", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, "خطا", Toast.LENGTH_SHORT).show()
                                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                }
                            }
                        }
                    }
                }
            }


            R.id.btn_intent_to_sign_in -> {
                startActivity(Intent(this, InSign::class.java))
                finish()
            }
            R.id.txt_go_to_terms -> {
                Toast.makeText(this, "رفتن به صفحه قوانین", Toast.LENGTH_SHORT).show()
            }
        }
    }
}