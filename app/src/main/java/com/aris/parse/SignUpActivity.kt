package com.aris.parse

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.aris.parse.databinding.ActivitySignUpBinding
import com.aris.parse.ext.CheckNetwork
import com.parse.ParseFile
import com.parse.ParseUser
import com.parse.SaveCallback
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*


const val UserName = "Name"

class SignUpActivity : BaseActivity() {

    var selectImage: Uri? = null
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val image: Uri? = data!!.data

            val des: String = StringBuilder(UUID.randomUUID().toString()).toString()
            UCrop.of(Uri.parse(image.toString()), Uri.fromFile(File(cacheDir, des)))
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(360, 360).start(this)
        }

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri: Uri? = UCrop.getOutput(data!!)
            setImage(resultUri!!)

        } else if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, "مشکل در برش عکس", Toast.LENGTH_SHORT).show()
        }


    }

    @SuppressLint("Recycle")
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





                                if (selectImage != null && selectImage!! != Uri.EMPTY) {
                                    binding.animationView.visibility = View.VISIBLE
                                    // send Image
                                    val name = UUID.randomUUID().toString() + ".jpg"  //random name
                                    val fileByteArray =
                                        contentResolver.openInputStream(selectImage!!)?.buffered()
                                            .use { it?.readBytes() }
                                    val parseFile = ParseFile(name, fileByteArray)
                                    parseFile.saveInBackground(SaveCallback {
                                        if (it == null) {
                                            val user = ParseUser()
                                            user.username =
                                                binding.edtEmailSignUp.text.toString().trim()
                                            user.setPassword(binding.edtPasswordSignUp.text.toString()
                                                .trim())
                                            user.email =
                                                binding.edtEmailSignUp.text.toString().trim()
                                            user.put(UserName,
                                                binding.edtNameSignUp.text.toString().trim())
                                            user.put("Image", parseFile)

                                            user.signUpInBackground { error ->

                                                binding.animationView.visibility = View.INVISIBLE

                                                if (error == null) {
                                                    Toast.makeText(this,
                                                        "خوش آمدید",
                                                        Toast.LENGTH_SHORT).show()
                                                    startActivity(Intent(this,
                                                        MainActivity::class.java))
                                                    finish()
                                                } else {
                                                    Toast.makeText(this, "خطا", Toast.LENGTH_SHORT)
                                                        .show()
                                                    Toast.makeText(this,
                                                        error.toString(),
                                                        Toast.LENGTH_SHORT)
                                                        .show()
                                                }

                                            }
                                        }
                                    })
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
                                            Toast.makeText(this,
                                                "خوش آمدید",
                                                Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this,
                                                MainActivity::class.java))
                                            finish()
                                        } else {
                                            Toast.makeText(this, "خطا", Toast.LENGTH_SHORT)
                                                .show()
                                            Toast.makeText(this,
                                                error.toString(),
                                                Toast.LENGTH_SHORT)
                                                .show()
                                        }

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

    fun setImage(uri: Uri) {
        selectImage = uri
        Picasso.with(this).load(uri).into(binding.profileImage)
    }
}